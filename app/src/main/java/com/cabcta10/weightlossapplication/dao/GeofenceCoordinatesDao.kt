package com.cabcta10.weightlossapplication.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cabcta10.weightlossapplication.entity.GeofenceCoordinates
import kotlinx.coroutines.flow.Flow

@Dao
interface GeofenceCoordinatesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: GeofenceCoordinates)

    @Query(value = "SELECT * from geofenceCoordinates")
    fun getCoordinates(): Flow<List<GeofenceCoordinates>>

    @Query(value = "SELECT * from geofenceCoordinates where id=:id")
    fun getCoordinatesById(id :Int): Flow<GeofenceCoordinates>

}