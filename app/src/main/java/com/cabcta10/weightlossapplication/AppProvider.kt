package com.cabcta10.weightlossapplication

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.cabcta10.weightlossapplication.viewModel.SettingsViewModel

object AppProvider {
    val Factory = viewModelFactory {
        initializer {
            SettingsViewModel(this.healthApp().container.settingsRepository)
        }
    }
}

fun CreationExtras.healthApp(): WeightLossApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as WeightLossApplication)