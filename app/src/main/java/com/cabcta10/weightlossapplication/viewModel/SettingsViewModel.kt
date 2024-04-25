package com.cabcta10.weightlossapplication.viewModel

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.cabcta10.weightlossapplication.repository.GeofenceCoordinatesRepository
import com.cabcta10.weightlossapplication.repository.SettingsRepository
import com.cabcta10.weightlossapplication.service.GeofenceManagerService
import com.cabcta10.weightlossapplication.uiState.SettingsScreenUiState
import com.cabcta10.weightlossapplication.uiState.UserUpdateValues
import com.cabcta10.weightlossapplication.uiState.toSettings
import com.cabcta10.weightlossapplication.workerThread.SleepRequestManager
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.TimeUnit
private const val SLEEP_WORK_TAG = "SleepMonitoring"

class SettingsViewModel (private val settingsRepository: SettingsRepository, private val geofenceCoordinatesRepository: GeofenceCoordinatesRepository , private val context: Context) : ViewModel()  {
    private val _settingsScreenUiState = MutableStateFlow(SettingsScreenUiState())
    val settingsScreenUiState: StateFlow<SettingsScreenUiState> = _settingsScreenUiState.asStateFlow()
    private var settingsExists: Boolean = false
    private var sleepRequestManager: SleepRequestManager? = null
    private val geofenceManagerService = GeofenceManagerService.getInstance(context)
//    private val alarmManager: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//    private val REQUEST_CODE_ALARM = 12345 // Define your request code here

    init {
        fetchDataFromDatabase()
    }

    private fun fetchDataFromDatabase() {
        viewModelScope.launch {
            settingsRepository.getSettings().collect { settingsFromDatabase ->
                if (settingsFromDatabase != null) {
                    settingsExists = true

                    val userUpdateValues = UserUpdateValues(
                        settingsFromDatabase.defaultStepCount.toString(),
                        settingsFromDatabase.waterIntake.toString(),
                        settingsFromDatabase.sleepStartTime.toString(),
                        settingsFromDatabase.sleepEndTime.toString()
                    )
                    _settingsScreenUiState.value = _settingsScreenUiState.value.copy(
                        grocerySelectedLocation = settingsFromDatabase.grocerySelectedLocation,
                        fitnessSelectedLocation = settingsFromDatabase.fitnessSelectedLocation,
                        userUpdateValues = userUpdateValues
                    )

                }
                geofenceCoordinatesRepository.getCoordinates().collect { coordinatesList ->
                    _settingsScreenUiState.value = _settingsScreenUiState.value.copy(
                        geofenceCoordinates = coordinatesList
                    )
                }
            }
        }
    }


     fun updateStoreCoordinates(grocerySelectedLocation: Int) {
        _settingsScreenUiState.update { currentState ->
            currentState.copy(
                grocerySelectedLocation = grocerySelectedLocation
            )
        }
    }

    fun updateFitnessCoordinates(fitnessSelectedLocation: Int) {
        _settingsScreenUiState.update { currentState ->
            currentState.copy(
                fitnessSelectedLocation = fitnessSelectedLocation
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun updateUserDetailsValue(userValues: UserUpdateValues) {
        _settingsScreenUiState.value = _settingsScreenUiState.value.copy(
            userUpdateValues = userValues
        )
    }

    @OptIn(DelicateCoroutinesApi::class)
    suspend fun saveSettings() {
        if (!settingsExists)
            settingsRepository.insertSettings(settingsScreenUiState.value.toSettings())
        else
            settingsRepository.updateSettings(settingsScreenUiState.value.toSettings())

        GlobalScope.launch {
            if (hasActivityRecognitionPermission()) {
                scheduleSleepMonitoring()
            } else {
                requestActivityRecognitionPermission()
            }
        }
        //getting grocery Coordinates
        geofenceCoordinatesRepository.getCoordinatesById(settingsScreenUiState.value.grocerySelectedLocation).collect {geofenceCoordinate ->
            geofenceManagerService.addGeofence(geofenceCoordinate.latitude.toDouble(),geofenceCoordinate.longitude.toDouble(), false)

            GlobalScope.launch {
                delay(3000)
                println("Creating 2nd Geofence with 3 sec delay")
                geofenceCoordinatesRepository.getCoordinatesById(settingsScreenUiState.value.fitnessSelectedLocation).collect { geofenceCoordinate ->
                    geofenceManagerService.addGeofence(geofenceCoordinate.latitude.toDouble(),geofenceCoordinate.longitude.toDouble(), true)
                }
            }
        }

    }

    fun resetSettingsScreen() {
        _settingsScreenUiState.value = _settingsScreenUiState.value.copy(
            grocerySelectedLocation = 0,
            fitnessSelectedLocation = 0,
            userUpdateValues = UserUpdateValues()
        )
    }

    private fun scheduleSleepMonitoring() {
        val settings = _settingsScreenUiState.value.toSettings()

        val sleepStartTime = parseTimeToCalendar(settings.sleepStartTime)
        val sleepEndTime = parseTimeToCalendar(settings.sleepEndTime)
        val currentTimeMillis = System.currentTimeMillis()

        if (currentTimeMillis > sleepEndTime.timeInMillis) {
            Log.d(TAG, "Current time is after sleep end time. Sleep monitoring not scheduled.")
            return
        }

        val delayMillis = sleepStartTime.timeInMillis - currentTimeMillis
        val durationMillis = sleepEndTime.timeInMillis - sleepStartTime.timeInMillis

        // Create a unique work request with a dynamic schedule
        val workRequest = PeriodicWorkRequestBuilder<SleepRequestManager>(
            repeatInterval = 10, // 10 minutes interval
            repeatIntervalTimeUnit = TimeUnit.MINUTES
        )
            .setInitialDelay(delayMillis, TimeUnit.MILLISECONDS)
            .build()

        // Enqueue the periodic work request
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            SLEEP_WORK_TAG,
            ExistingPeriodicWorkPolicy.REPLACE,
            workRequest
        )
    }
    suspend fun deleteSettings() {
        settingsRepository.deleteSettings(settingsScreenUiState.value.toSettings())
    }


    private fun hasActivityRecognitionPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACTIVITY_RECOGNITION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestActivityRecognitionPermission() {
        // Request the ACTIVITY_RECOGNITION permission
        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(Manifest.permission.ACTIVITY_RECOGNITION),
            PERMISSION_REQUEST_CODE
        )
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


    companion object {
        private const val PERMISSION_REQUEST_CODE = 123
    }
}
