package com.cabcta10.weightlossapplication.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "settings")
data class Settings (
    @PrimaryKey(autoGenerate = false)
    val id: Int = 1,
    //val latitude: Double = 0.0,
    //val longitude: Double = 0.0,
    //val weight : Double = 0.0,
    //val height : Double = 0.0,
    //val targetWeight : Double = 0.0,
    val defaultStepCount : Double = 0.0,
    val waterIntake : Double = 0.0,
    val groceryLocationLatitude : Double = 0.0,
    val groceryLocationLongitude : Double = 0.0,
    val gymLocationLatitude : Double = 0.0,
    val gymLocationLongitude : Double = 0.0,
    val sleepHours : Double = 0.0,

    )