package com.cabcta10.weightlossapplication.repository

import com.cabcta10.weightlossapplication.entity.GeofenceCoordinates
import kotlinx.coroutines.flow.Flow

interface GeofenceCoordinatesRepository {
    fun getCoordinates(): Flow<List<GeofenceCoordinates>>
    suspend fun insertCoordinates(coordinates: GeofenceCoordinates)

    fun getCoordinatesById(id : Int) : Flow<GeofenceCoordinates>
}