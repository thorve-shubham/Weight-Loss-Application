package com.cabcta10.weightlossapplication.uiState

import com.cabcta10.weightlossapplication.entity.GeofenceCoordinates
import com.cabcta10.weightlossapplication.entity.Settings

data class SettingsScreenUiState(
    val grocerySelectedLocation : Int = 0,
    val fitnessSelectedLocation : Int = 0,
    val geofenceCoordinates: List<GeofenceCoordinates> = mutableListOf()
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
    val sleepHours : String = "",

    )

fun SettingsScreenUiState.toSettings() : Settings = Settings(
    id = 1,
    grocerySelectedLocation = grocerySelectedLocation,
    fitnessSelectedLocation = fitnessSelectedLocation,
    defaultStepCount = userUpdateValues.defaultStepCount.toDouble(),
    waterIntake = userUpdateValues.waterIntake.toDouble(),
    groceryLocationLatitude = userUpdateValues.groceryLocationLatitude.toDouble(),
    groceryLocationLongitude = userUpdateValues.groceryLocationLongitude.toDouble(),
    gymLocationLatitude = userUpdateValues.gymLocationLatitude.toDouble(),
    gymLocationLongitude = userUpdateValues.gymLocationLongitude.toDouble(),
    sleepHours = userUpdateValues.sleepHours.toDouble()

)