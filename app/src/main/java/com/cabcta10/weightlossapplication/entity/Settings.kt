package com.cabcta10.weightlossapplication.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "settings")
data class Settings (
    @PrimaryKey(autoGenerate = false)
    val id: Int = 1,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
)