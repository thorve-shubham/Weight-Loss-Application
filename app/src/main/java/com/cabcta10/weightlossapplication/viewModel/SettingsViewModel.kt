package com.cabcta10.weightlossapplication.viewModel

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cabcta10.weightlossapplication.entity.GeofenceCoordinates
import com.cabcta10.weightlossapplication.repository.GeofenceCoordinatesRepository
import com.cabcta10.weightlossapplication.repository.SettingsRepository
import com.cabcta10.weightlossapplication.service.GeofenceManagerService
import com.cabcta10.weightlossapplication.uiState.GroceryCoordinates
import com.cabcta10.weightlossapplication.uiState.SettingsScreenUiState
import com.cabcta10.weightlossapplication.uiState.toSettings
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.toList
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
    @OptIn(ExperimentalCoroutinesApi::class)
    private fun fetchDataFromDatabase() {
        viewModelScope.launch {
            settingsRepository.getSettings().collect { settingsFromDatabase ->
                System.out.println("hehehe")
                if(settingsFromDatabase != null) {
                    settingsExists = true
                    _settingsScreenUiState.value = _settingsScreenUiState.value.copy(
                        grocerySelectedLocation = settingsFromDatabase.grocerySelectedLocation,
                        fitnessSelectedLocation = settingsFromDatabase.fitnessSelectedLocation
                    )

                }
                geofenceCoordinatesRepository.getCoordinates().collect { coordinatesList ->
                    // Update _settingsScreenUiState with all coordinates
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
    suspend fun saveSettings() {
        if(!settingsExists)
            settingsRepository.insertSettings(settingsScreenUiState.value.toSettings())
        else
            settingsRepository.updateSettings(settingsScreenUiState.value.toSettings())

        //getting grocery Coordinates
        geofenceCoordinatesRepository.getCoordinatesById(settingsScreenUiState.value.grocerySelectedLocation).collect {geofenceCoordinate ->
            geofenceManagerService.addGeofence(geofenceCoordinate.latitude.toDouble(),geofenceCoordinate.longitude.toDouble())

//            println("Adding 2nd now")
//
//            geofenceCoordinatesRepository.getCoordinatesById(settingsScreenUiState.value.fitnessSelectedLocation).collect { geofenceCoordinate ->
//                geofenceManagerService.addGeofence(geofenceCoordinate.latitude.toDouble(),geofenceCoordinate.longitude.toDouble(), true)
//            }
        }

    }

}