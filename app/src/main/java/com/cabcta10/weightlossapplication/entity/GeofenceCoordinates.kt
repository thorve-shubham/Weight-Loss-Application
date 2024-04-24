package com.cabcta10.weightlossapplication.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "geofenceCoordinates")
data class GeofenceCoordinates(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val latitude: String,
    val longitude: String,
    val type: String,
    val name: String
)
