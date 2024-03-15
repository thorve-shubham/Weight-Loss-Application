package com.cabcta10.weightlossapplication.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cabcta10.weightlossapplication.repository.SettingsRepository
import com.cabcta10.weightlossapplication.uiState.GroceryCoordinates
import com.cabcta10.weightlossapplication.uiState.SettingsScreenUiState
import com.cabcta10.weightlossapplication.uiState.toSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class SettingsViewModel(private val settingsRepository: SettingsRepository) : ViewModel()  {

    private val _settingsScreenUiState = MutableStateFlow(SettingsScreenUiState())
    val settingsScreenUiState: StateFlow<SettingsScreenUiState> = _settingsScreenUiState.asStateFlow()

    init {
        fetchDataFromDatabase()
    }

    private fun fetchDataFromDatabase() {
        viewModelScope.launch {
            settingsRepository.getSettings().collect { settingsFromDatabase ->
                val groceryCoordinates = GroceryCoordinates(latitude = settingsFromDatabase.latitude.toString(), longitude = settingsFromDatabase.longitude.toString())
                _settingsScreenUiState.value = _settingsScreenUiState.value.copy(
                    groceryStoreCoordinates = groceryCoordinates
                )
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

    suspend fun saveSettings() {
        //vaidate something here
        settingsRepository.insertSettings(settingsScreenUiState.value.toSettings())
    }


}