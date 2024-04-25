package com.cabcta10.weightlossapplication.uiState

import android.content.Context
import com.cabcta10.weightlossapplication.entity.Settings
import java.util.Calendar

data class SettingsScreenUiState(
    val groceryStoreCoordinates: GroceryCoordinates = GroceryCoordinates(),
    val userUpdateValues: UserUpdateValues = UserUpdateValues()
)

data class GroceryCoordinates(
    val latitude : String = "",
    val longitude : String = "",
)

data class UserUpdateValues(
    //val weight : String = "",
    //val height : String = "",
    //val targetWeight : String = "",
    val defaultStepCount : String = "",
    val waterIntake : String = "",
    val groceryLocationLatitude : String = "",
    val groceryLocationLongitude : String = "",
    val gymLocationLatitude : String = "",
    val gymLocationLongitude : String = "",
    val sleepStartTime: String = "23:00",
    val sleepEndTime: String = "7:00"
    )

fun SettingsScreenUiState.toSettings() : Settings = Settings(
    id = 1,
    //latitude  = groceryStoreCoordinates.latitude.toDouble(),
    //longitude =  groceryStoreCoordinates.longitude.toDouble(),
    //weight = userUpdateValues.weight.toDouble(),
    //height = userUpdateValues.height.toDouble(),
    //targetWeight = userUpdateValues.targetWeight.toDouble(),
    defaultStepCount = userUpdateValues.defaultStepCount.takeIf { it.isNotBlank() }?.toDoubleOrNull() ?: 0.0,
    waterIntake = userUpdateValues.waterIntake.takeIf { it.isNotBlank() }?.toDoubleOrNull() ?: 0.0,
    groceryLocationLatitude = userUpdateValues.groceryLocationLatitude.takeIf { it.isNotBlank() }?.toDoubleOrNull() ?: 0.0,
    groceryLocationLongitude = userUpdateValues.groceryLocationLongitude.takeIf { it.isNotBlank() }?.toDoubleOrNull() ?: 0.0,
    gymLocationLatitude = userUpdateValues.gymLocationLatitude.takeIf { it.isNotBlank() }?.toDoubleOrNull() ?: 0.0,
    gymLocationLongitude = userUpdateValues.gymLocationLongitude.takeIf { it.isNotBlank() }?.toDoubleOrNull() ?: 0.0,
    sleepStartTime = userUpdateValues.sleepStartTime ?: "23:00",
    sleepEndTime = userUpdateValues.sleepEndTime ?: "7:00"
)


