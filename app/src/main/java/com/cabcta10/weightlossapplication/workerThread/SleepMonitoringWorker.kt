package com.cabcta10.weightlossapplication.workerThread

import android.Manifest
import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.PowerManager
import android.telephony.TelephonyManager
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.cabcta10.weightlossapplication.AppDatabase
import com.cabcta10.weightlossapplication.R
import com.cabcta10.weightlossapplication.broadcastReceiver.UserActivityReceiver
import com.cabcta10.weightlossapplication.repositoryImpl.SettingsRepositoryImpl
import com.cabcta10.weightlossapplication.service.NotificationUtil
import com.google.android.gms.location.ActivityRecognition
import com.google.android.gms.location.ActivityTransition
import com.google.android.gms.location.ActivityTransitionRequest
import com.google.android.gms.location.DetectedActivity
import com.google.android.gms.location.SleepSegmentRequest
import com.google.android.gms.tasks.Tasks
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import java.util.*
import java.util.concurrent.TimeUnit

private const val LIGHT_THRESHOLD = 1000 // Adjust this threshold based on ambient light level
private const val ACTIVITY_TRANSITION_INTERVAL_MILLIS = 10000L // Interval for activity transitions check (in milliseconds)
private const val TIMEOUT_MILLISECONDS = 30000L // Timeout duration in milliseconds (30 seconds)

class SleepRequestManager(private val context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {

    companion object {
        private const val TAG = "SleepRequestManager"
        private const val PERMISSION_ACTIVITY_RECOGNITION = Manifest.permission.ACTIVITY_RECOGNITION
    }

    private var isSubscribed = false
    private var sleepReceiverPendingIntent: PendingIntent? = null

    override suspend fun doWork(): Result {
        Log.d(TAG, "Starting sleep updates subscription...")

        // Check activity recognition permission
        if (!hasActivityRecognitionPermission()) {
            Log.e(TAG, "Activity recognition permission not granted.")
            return Result.failure()
        }

        val settings = SettingsRepositoryImpl(AppDatabase.getDatabase(applicationContext).settingsDAO()).getSettings().firstOrNull()
        // Obtain sleep time window from settings
        val sleepStartTime = parseTimeToCalendar(settings?.sleepStartTime ?: "23:00")
        val sleepEndTime = parseTimeToCalendar(settings?.sleepEndTime ?: "07:00")

        var isAwakeDetected = false

        while (true) {
            delay(1000)

            if (isStopped) {
                Log.d(TAG, "Worker stopped. Cleaning up...")
                unsubscribeFromActivityRecognition()
                return Result.success()
            }

            val currentTime = Calendar.getInstance()
            if (currentTime.after(sleepEndTime)) {
                Log.d(TAG, "Sleep monitoring ended at SleepEndTime.")
                unsubscribeFromActivityRecognition()
                return Result.success()
            }

            // Check if current time is within sleep time window
            val isWithinSleepWindow = currentTime.after(sleepStartTime) || currentTime.before(sleepEndTime)

            if (isWithinSleepWindow) {
                // Check for user awake conditions only within sleep time window
                if (!isSubscribed) {
                    subscribeToActivityRecognition()
                    isSubscribed = true
                }
                val isAwake = isUserAwake()
                if (isAwake && !isAwakeDetected) {
                    Log.d(TAG, "User is awake.")
                    sendAwakeNotification()
                    isAwakeDetected = true // Set flag to true after first awake detection
                }
            }
        }
    }

    private fun sendAwakeNotification() {
        Log.d(TAG, "Sending awake notification...")
        NotificationUtil.displayNotification(
            applicationContext,
            "Up and active! Remember, 7-8 hours of sleep can help with weight loss, per NHS.",
            R.drawable.sleep
        )
    }
    private fun isUserAwake(): Boolean {
        // Implement awake detection logic based on user interactions, environment, and activity recognition
        return isScreenInteractive() || isPhoneInUse() || isLightOn(context) || isPhysicalActivityDetected(context)
    }


    private fun hasActivityRecognitionPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context, PERMISSION_ACTIVITY_RECOGNITION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun subscribeToActivityRecognition() {
        Log.d(TAG, "Subscribing to ActivityRecognition updates...")

        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACTIVITY_RECOGNITION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val activityPendingIntent = createActivityRecognitionPendingIntent()

            ActivityRecognition.getClient(context)
                .requestActivityUpdates(0, activityPendingIntent)
                .addOnSuccessListener {
                    Log.d(TAG, "ActivityRecognition updates request successful.")
                    isSubscribed = true
                }
                .addOnFailureListener { exception ->
                    Log.e(TAG, "Error requesting ActivityRecognition updates: ${exception.message}")
                    isSubscribed = false
                }
        } else {
            Log.e(TAG, "Permission for activity recognition not granted.")
        }
    }

    private fun createActivityRecognitionPendingIntent(): PendingIntent {
        val intent = Intent(context, UserActivityReceiver::class.java)
        return PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )
    }

    @SuppressLint("ServiceCast")
    private fun isScreenInteractive(): Boolean {
        val powerManager = context.getSystemService(Context.POWER_SERVICE) as? PowerManager
        return powerManager?.isInteractive == true
    }

    private fun isPhoneInUse(): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager
        val recentTasks = activityManager?.getRunningTasks(1)
        if (recentTasks != null && recentTasks.isNotEmpty()) {
            val topTask = recentTasks[0]
            val packageName = topTask.baseActivity?.packageName
            if (packageName != null && packageName == context.packageName) {
                // The current foreground task belongs to this app (active usage)
                return true
            }
        }

        // Check if there is ongoing phone call
        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as? TelephonyManager
        return telephonyManager?.callState != TelephonyManager.CALL_STATE_IDLE
    }


    private fun isLightOn(context: Context): Boolean {
        var isLightOn = false

        // Get the sensor manager
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager

        // Get the light sensor
        val lightSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_LIGHT)

        // Define a sensor listener
        val sensorListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                if (event != null && event.sensor.type == Sensor.TYPE_LIGHT) {
                    val lightLevel = event.values[0]

                    // Check if light level exceeds the threshold
                    isLightOn = lightLevel > LIGHT_THRESHOLD

                    // Unregister listener after obtaining the light level
                    sensorManager?.unregisterListener(this)
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                // Not used
            }
        }

        // Register the sensor listener
        sensorManager?.registerListener(sensorListener, lightSensor, SensorManager.SENSOR_DELAY_NORMAL)
        return isLightOn
    }

    private fun isPhysicalActivityDetected(context: Context): Boolean {
        // Check if activity recognition permission is granted
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACTIVITY_RECOGNITION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.e(TAG, "Activity recognition permission not granted.")
            return false
        }

        val activityTransitionRequest = buildActivityTransitionRequest()
        val activityRecognitionClient = ActivityRecognition.getClient(context)

        try {
            val task = activityRecognitionClient.requestActivityTransitionUpdates(activityTransitionRequest, createPendingIntent())
            val result = Tasks.await(task, TIMEOUT_MILLISECONDS, TimeUnit.SECONDS)
            return result != null
        } catch (e: Exception) {
            Log.e(TAG, "Error detecting activity transitions: ${e.message}")
            return false
        } finally {
            activityRecognitionClient.removeActivityTransitionUpdates(createPendingIntent())
        }
    }


    private fun buildActivityTransitionRequest(): ActivityTransitionRequest {
        val transitions = listOf(
            ActivityTransition.Builder()
                .setActivityType(DetectedActivity.IN_VEHICLE)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                .build(),
            ActivityTransition.Builder()
                .setActivityType(DetectedActivity.ON_BICYCLE)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                .build(),
            ActivityTransition.Builder()
                .setActivityType(DetectedActivity.WALKING)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                .build(),
            ActivityTransition.Builder()
                .setActivityType(DetectedActivity.RUNNING)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                .build()
        )

        return ActivityTransitionRequest(transitions)
    }

    private fun createPendingIntent(): PendingIntent {
        val intent = Intent(context, UserActivityReceiver ::class.java)
        return PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private fun unsubscribeFromActivityRecognition() {
        if (isSubscribed) {


            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACTIVITY_RECOGNITION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val activityPendingIntent = createActivityRecognitionPendingIntent()

                ActivityRecognition.getClient(context)
                    .removeActivityUpdates(activityPendingIntent)
                    .addOnCompleteListener {
                        Log.d(TAG, "Unsubscribed from ActivityRecognition updates.")
                        isSubscribed = false
                    }
                    .addOnFailureListener { exception ->
                        Log.e(TAG, "Error unsubscribing from ActivityRecognition updates: ${exception.message}")
                    }
            } else {

            }
        }
    }

    private fun parseTimeToCalendar(timeString: String): Calendar {
        val calendar = Calendar.getInstance()
        val (hour, minute) = timeString.split(":")

        val parsedHour = hour.toInt()
        val parsedMinute = minute.toInt()

        if (parsedHour < calendar.get(Calendar.HOUR_OF_DAY) || (parsedHour == calendar.get(Calendar.HOUR_OF_DAY) && parsedMinute < calendar.get(Calendar.MINUTE))) {
            // End time is on the next day
            calendar.add(Calendar.DAY_OF_YEAR, 1) // Move to the next day
        }

        calendar.set(Calendar.HOUR_OF_DAY, hour.toInt())
        calendar.set(Calendar.MINUTE, minute.toInt())
        calendar.set(Calendar.SECOND, 0)
        return calendar
    }
}
