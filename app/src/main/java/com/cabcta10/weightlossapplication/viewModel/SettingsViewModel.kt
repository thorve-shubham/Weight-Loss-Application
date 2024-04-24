package com.cabcta10.weightlossapplication.viewModel

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cabcta10.weightlossapplication.repository.GeofenceCoordinatesRepository
import com.cabcta10.weightlossapplication.repository.SettingsRepository
import com.cabcta10.weightlossapplication.service.GeofenceManagerService
import com.cabcta10.weightlossapplication.uiState.SettingsScreenUiState
import com.cabcta10.weightlossapplication.uiState.UserUpdateValues
import com.cabcta10.weightlossapplication.uiState.toSettings
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class SettingsViewModel (private val settingsRepository: SettingsRepository, private val geofenceCoordinatesRepository: GeofenceCoordinatesRepository , private val context: Context) : ViewModel()  {

    private val _settingsScreenUiState = MutableStateFlow(SettingsScreenUiState())
    val settingsScreenUiState: StateFlow<SettingsScreenUiState> = _settingsScreenUiState.asStateFlow()
    private var settingsExists : Boolean = false;

    private val geofenceManagerService = GeofenceManagerService.getInstance(context)

    init {
        fetchDataFromDatabase()
    }

    private fun fetchDataFromDatabase() {
        viewModelScope.launch {
            settingsRepository.getSettings().collect { settingsFromDatabase ->
                if(settingsFromDatabase != null) {
                    settingsExists = true
                    val userUpdateValues = UserUpdateValues(
                        settingsFromDatabase.defaultStepCount.toString(),
                        settingsFromDatabase.waterIntake.toString(),
                        settingsFromDatabase.sleepHours.toString()
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
        _settingsScreenUiState.update { currentState ->
            currentState.copy(
                userUpdateValues = userValues
            )
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    suspend fun saveSettings() {
        if(!settingsExists)
            settingsRepository.insertSettings(settingsScreenUiState.value.toSettings())
        else
            settingsRepository.updateSettings(settingsScreenUiState.value.toSettings())

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

    suspend fun deleteSettings() {
        settingsRepository.deleteSettings(settingsScreenUiState.value.toSettings())
    }

}