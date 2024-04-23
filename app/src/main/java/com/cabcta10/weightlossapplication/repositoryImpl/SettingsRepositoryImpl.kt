package com.cabcta10.weightlossapplication.repositoryImpl

import com.cabcta10.weightlossapplication.dao.SettingsDao
import com.cabcta10.weightlossapplication.entity.Settings
import com.cabcta10.weightlossapplication.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow

class SettingsRepositoryImpl(private val settingsDao: SettingsDao) : SettingsRepository {
    override fun getSettings(): Flow<Settings> {
        return settingsDao.getSettings()
    }

    override suspend fun insertSettings(settings: Settings) {
        settingsDao.insert(settings)
    }

    override suspend fun updateSettings(settings: Settings) {
        settingsDao.update(settings)
    }

    override suspend fun deleteSettings(settings: Settings) {
        settingsDao.delete(settings)
    }

}