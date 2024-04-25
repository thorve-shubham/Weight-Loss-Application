package com.cabcta10.weightlossapplication.viewModel

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.*
import com.cabcta10.weightlossapplication.broadcastReceiver.SleepAPIBroadcastReceiver
import com.cabcta10.weightlossapplication.repository.SettingsRepository
import com.cabcta10.weightlossapplication.service.GeofenceManagerService
import com.cabcta10.weightlossapplication.uiState.GroceryCoordinates
import com.cabcta10.weightlossapplication.uiState.SettingsScreenUiState
import com.cabcta10.weightlossapplication.uiState.UserUpdateValues
import com.cabcta10.weightlossapplication.uiState.toSettings
import com.cabcta10.weightlossapplication.workerThread.SleepRequestManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

class SettingsViewModel(private val settingsRepository: SettingsRepository, private val context: Context) :
    ViewModel() {

    private val _settingsScreenUiState = MutableStateFlow(SettingsScreenUiState())
    val settingsScreenUiState: StateFlow<SettingsScreenUiState> = _settingsScreenUiState.asStateFlow()
    private var settingsExists: Boolean = false
    private var sleepRequestManager: SleepRequestManager? = null
    private val geofenceManagerService = GeofenceManagerService.getInstance(context)
    private val alarmManager: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    private val REQUEST_CODE_ALARM = 12345 // Define your request code here

    init {
        fetchDataFromDatabase()
    }

    private fun fetchDataFromDatabase() {
        viewModelScope.launch {
            settingsRepository.getSettings().collect { settingsFromDatabase ->
                if (settingsFromDatabase != null) {
                    settingsExists = true
                    val groceryCoordinates = GroceryCoordinates(
                        latitude = settingsFromDatabase.groceryLocationLatitude.toString(),
                        longitude = settingsFromDatabase.groceryLocationLongitude.toString()
                    )
                    _settingsScreenUiState.value = _settingsScreenUiState.value.copy(
                        groceryStoreCoordinates = groceryCoordinates
                    )
                }
            }
        }
    }

    fun updateStoreCoordinates(groceryCoordinates: GroceryCoordinates) {
        _settingsScreenUiState.value = _settingsScreenUiState.value.copy(
            groceryStoreCoordinates = groceryCoordinates
        )
    }

    fun updateUserDetailsValue(userValues: UserUpdateValues) {
        _settingsScreenUiState.value = _settingsScreenUiState.value.copy(
            userUpdateValues = userValues
        )
    }

    suspend fun saveSettings() {
        if (!settingsExists)
            settingsRepository.insertSettings(settingsScreenUiState.value.toSettings())
        else
            settingsRepository.updateSettings(settingsScreenUiState.value.toSettings())

        geofenceManagerService.addGeofence(
            _settingsScreenUiState.value.groceryStoreCoordinates.latitude.toDouble(),
            _settingsScreenUiState.value.groceryStoreCoordinates.longitude.toDouble()
        )



        viewModelScope.launch {
            if (hasActivityRecognitionPermission()) {
                // Permission is granted, proceed to save settings
                scheduleSleepMonitoring()
            } else {
                // Permission is not granted, request it
                requestActivityRecognitionPermission()
            }
        }
    }

    @SuppressLint("ScheduleExactAlarm")
    fun scheduleSleepMonitoring() {
        val sleepStartCalendar = Calendar.getInstance()
        val sleepEndCalendar = Calendar.getInstance()
        sleepEndCalendar.add(Calendar.HOUR_OF_DAY, 8) // Example: 8 hours sleep duration
        Log.d(sleepEndCalendar.toString(), "")
        val intent = Intent(context, SleepAPIBroadcastReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            REQUEST_CODE_ALARM,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )

        // Schedule the alarm to trigger sleep monitoring
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            sleepStartCalendar.timeInMillis,
            pendingIntent
        )

        // Create constraints for WorkManager
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        // Create a OneTimeWorkRequest for SleepMonitorWorker
        val workRequest = OneTimeWorkRequestBuilder<SleepRequestManager>()
            .setConstraints(constraints)
            .build()

        // Enqueue the work request with WorkManager
        WorkManager.getInstance(context).enqueue(workRequest)
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

    companion object {
        private const val PERMISSION_REQUEST_CODE = 123
    }
}
