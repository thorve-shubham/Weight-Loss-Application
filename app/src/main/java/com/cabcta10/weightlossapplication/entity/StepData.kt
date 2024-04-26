package com.cabcta10.weightlossapplication.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class StepData(
    @PrimaryKey val date: String,
    val steps:Int,
    val informed: Boolean = false
)
