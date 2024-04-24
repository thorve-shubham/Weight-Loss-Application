package com.cabcta10.weightlossapplication.repositoryImpl

import com.cabcta10.weightlossapplication.dao.GeofenceCoordinatesDao
import com.cabcta10.weightlossapplication.dao.SettingsDao
import com.cabcta10.weightlossapplication.entity.GeofenceCoordinates
import com.cabcta10.weightlossapplication.entity.Settings
import com.cabcta10.weightlossapplication.repository.GeofenceCoordinatesRepository
import com.cabcta10.weightlossapplication.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow

class GeofenceCoordinatesRepositoryImpl(private val geofenceCoordinatesDao: GeofenceCoordinatesDao) : GeofenceCoordinatesRepository {

    override fun getCoordinates(): Flow<List<GeofenceCoordinates>> {
        return geofenceCoordinatesDao.getCoordinates()
    }

    override suspend fun insertCoordinates(coordinates: GeofenceCoordinates) {
        geofenceCoordinatesDao.insert(coordinates)
    }

    override fun getCoordinatesById(id: Int): Flow<GeofenceCoordinates> {
        return geofenceCoordinatesDao.getCoordinatesById(id)
    }
}