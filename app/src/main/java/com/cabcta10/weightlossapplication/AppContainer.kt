package com.cabcta10.weightlossapplication

import android.content.Context
import com.cabcta10.weightlossapplication.repository.SettingsRepository
import com.cabcta10.weightlossapplication.repositoryImpl.SettingsRepositoryImpl

interface AppContainer {
    val settingsRepository: SettingsRepository
}

class AppDataContainer(private val context: Context) : AppContainer {

    override val settingsRepository: SettingsRepository by lazy {
        SettingsRepositoryImpl(AppDatabase.getDatabase(context).settingsDAO())
    }
}