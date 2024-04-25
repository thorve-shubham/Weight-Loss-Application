package com.cabcta10.weightlossapplication.workerThread

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.cabcta10.weightlossapplication.broadcastReceiver.SleepAPIBroadcastReceiver
import com.google.android.gms.location.ActivityRecognition
import com.google.android.gms.location.SleepSegmentRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

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
        if (hasActivityRecognitionPermission()) {
            subscribeToSleepUpdates()
        } else {
            Log.e(TAG, "Activity recognition permission not granted.")
            return Result.failure()
        }

        // Simulate work for demonstration purposes (replace with actual work)
        repeat(10) { index ->
            if (isStopped) {
                Log.d(TAG, "Worker stopped. Cleaning up...")
                unsubscribeFromSleepUpdates()
                return Result.success()
            }
            Log.d(TAG, "Sleep updates in progress...$index")
            // Simulated work delay
            kotlinx.coroutines.delay(1000)
        }

        // Perform any final cleanup before completing the work
        unsubscribeFromSleepUpdates()

        return Result.success()
    }
    private fun hasActivityRecognitionPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context, PERMISSION_ACTIVITY_RECOGNITION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun subscribeToSleepUpdates() {
        Log.d(TAG, "Requesting sleep segment updates...")

        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACTIVITY_RECOGNITION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val sleepSegmentRequest = SleepSegmentRequest.getDefaultSleepSegmentRequest()

            sleepReceiverPendingIntent = createPendingIntent()

            ActivityRecognition.getClient(context)
                .requestSleepSegmentUpdates(sleepReceiverPendingIntent!!, sleepSegmentRequest)
                .addOnSuccessListener {
                    Log.d(TAG, "Sleep segment updates request successful.")
                    isSubscribed = true
                }
                .addOnFailureListener { exception ->
                    Log.e(TAG, "Error requesting sleep segment updates: ${exception.message}")
                    isSubscribed = false
                }
        } else {
            Log.e(TAG, "Permission for activity recognition not granted.")
        }
    }

    private fun createPendingIntent(): PendingIntent {
        val intent = Intent(context, SleepAPIBroadcastReceiver::class.java)
        return PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )
    }

    // Method to unsubscribe from sleep updates
    private fun unsubscribeFromSleepUpdates() {
        if (isSubscribed && sleepReceiverPendingIntent != null) {
            ActivityRecognition.getClient(context)
                .removeSleepSegmentUpdates(sleepReceiverPendingIntent!!)
                .addOnCompleteListener {
                    Log.d(TAG, "Unsubscribed from sleep updates.")
                    isSubscribed = false
                    sleepReceiverPendingIntent = null
                }
                .addOnFailureListener { exception ->
                    Log.e(TAG, "Error unsubscribing from sleep updates: ${exception.message}")
                }
        }
    }
}
