package com.cabcta10.weightlossapplication.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cabcta10.weightlossapplication.repository.SettingsRepository
import com.cabcta10.weightlossapplication.service.GeofenceManagerService
import com.cabcta10.weightlossapplication.uiState.GroceryCoordinates
import com.cabcta10.weightlossapplication.uiState.SettingsScreenUiState
import com.cabcta10.weightlossapplication.uiState.UserUpdateValues
import com.cabcta10.weightlossapplication.uiState.toSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class SettingsViewModel (private val settingsRepository: SettingsRepository, private val context: Context) : ViewModel()  {

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
                    val groceryCoordinates = GroceryCoordinates(latitude = settingsFromDatabase.groceryLocationLatitude.toString(), longitude = settingsFromDatabase.groceryLocationLongitude.toString())
                    _settingsScreenUiState.value = _settingsScreenUiState.value.copy(
                        groceryStoreCoordinates = groceryCoordinates
                    )
                }
            }
        }
    }

    fun updateStoreCoordinates(groceryCoordinates: GroceryCoordinates) {
        _settingsScreenUiState.update { currentState ->
            currentState.copy(
                groceryStoreCoordinates = groceryCoordinates
            )
        }
    }

    fun updateUserDetailsValue(userValues: UserUpdateValues) {
        _settingsScreenUiState.update { currentState ->
            currentState.copy(
                userUpdateValues = userValues
            )
        }
    }

    suspend fun saveSettings() {
        if(!settingsExists)
            settingsRepository.insertSettings(settingsScreenUiState.value.toSettings())
        else
            settingsRepository.updateSettings(settingsScreenUiState.value.toSettings())

        geofenceManagerService.addGeofence(_settingsScreenUiState.value.groceryStoreCoordinates.latitude.toDouble(),
            _settingsScreenUiState.value.groceryStoreCoordinates.longitude.toDouble())

    }

    suspend fun deleteSettings() {
        settingsRepository.deleteSettings(settingsScreenUiState.value.toSettings())
    }

}