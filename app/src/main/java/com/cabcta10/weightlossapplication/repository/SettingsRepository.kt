package com.cabcta10.weightlossapplication.repository

import com.cabcta10.weightlossapplication.entity.Settings
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun getSettings(): Flow<Settings>


    suspend fun insertSettings(settings: Settings)

    suspend fun updateSettings(settings: Settings)

    suspend fun deleteSettings(settings: Settings)
}