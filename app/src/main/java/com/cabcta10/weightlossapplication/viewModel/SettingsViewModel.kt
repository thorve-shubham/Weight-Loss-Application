package com.cabcta10.weightlossapplication.viewModel

import androidx.lifecycle.ViewModel
import com.cabcta10.weightlossapplication.repository.SettingsRepository
import com.cabcta10.weightlossapplication.uiState.GroceryCoordinates
import com.cabcta10.weightlossapplication.uiState.SettingsScreenUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SettingsViewModel(private val settingsRepository: SettingsRepository) : ViewModel()  {

    private val _settingsScreenUiState = MutableStateFlow(SettingsScreenUiState())
    val settingsScreenUiState: StateFlow<SettingsScreenUiState> = _settingsScreenUiState.asStateFlow()

    fun updateStoreCoordinates(groceryCoordinates: GroceryCoordinates) {
        _settingsScreenUiState.update { currentState ->
            currentState.copy(
                groceryStoreCoordinates = groceryCoordinates
            )
        }
    }
}