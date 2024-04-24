package com.cabcta10.weightlossapplication

import android.content.Context
import com.cabcta10.weightlossapplication.repository.GeofenceCoordinatesRepository
import com.cabcta10.weightlossapplication.repository.SettingsRepository
import com.cabcta10.weightlossapplication.repositoryImpl.GeofenceCoordinatesRepositoryImpl
import com.cabcta10.weightlossapplication.repositoryImpl.SettingsRepositoryImpl

interface AppContainer {
    val settingsRepository: SettingsRepository
    val geofenceCoordinatesRepository: GeofenceCoordinatesRepository
}

class AppDataContainer(private val context: Context) : AppContainer {

    override val settingsRepository: SettingsRepository by lazy {
        SettingsRepositoryImpl(AppDatabase.getDatabase(context).settingsDAO())
    }

    override val geofenceCoordinatesRepository: GeofenceCoordinatesRepository by lazy {
        GeofenceCoordinatesRepositoryImpl(AppDatabase.getDatabase(context).geofenceCoordinatesDao())
    }
}