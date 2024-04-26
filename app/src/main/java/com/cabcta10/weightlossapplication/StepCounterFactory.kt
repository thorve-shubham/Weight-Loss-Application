package com.cabcta10.weightlossapplication

import android.app.Application
import android.content.Context
import android.hardware.SensorManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cabcta10.weightlossapplication.repositoryImpl.SettingsRepositoryImpl
import com.cabcta10.weightlossapplication.repositoryImpl.StepRepositoryImpl
import com.cabcta10.weightlossapplication.viewModel.StepCounterViewModel

class StepCounterViewModelFactory(
    private val sensorManager: SensorManager,
    private val context : Context,
    private val application : Application
    // Add other dependencies as necessary
) : ViewModelProvider.Factory {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StepCounterViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StepCounterViewModel(application, sensorManager, StepRepositoryImpl(AppDatabase.getDatabase(context).stepDao()),
                SettingsRepositoryImpl(AppDatabase.getDatabase(context).settingsDAO())
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}