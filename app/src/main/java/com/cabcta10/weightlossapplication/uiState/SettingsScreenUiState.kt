package com.cabcta10.weightlossapplication.uiState

import com.cabcta10.weightlossapplication.entity.Settings

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
    val sleepHours : String = ""
//    val sleepStartTime: String = "",
//    val sleepEndTime: String = ""
    )

fun SettingsScreenUiState.toSettings() : Settings = Settings(
    id = 1,
    //latitude  = groceryStoreCoordinates.latitude.toDouble(),
    //longitude =  groceryStoreCoordinates.longitude.toDouble(),
    //weight = userUpdateValues.weight.toDouble(),
    //height = userUpdateValues.height.toDouble(),
    //targetWeight = userUpdateValues.targetWeight.toDouble(),
    defaultStepCount = userUpdateValues.defaultStepCount.toDouble(),
    waterIntake = userUpdateValues.waterIntake.toDouble(),
    groceryLocationLatitude = userUpdateValues.groceryLocationLatitude.toDouble(),
    groceryLocationLongitude = userUpdateValues.groceryLocationLongitude.toDouble(),
    gymLocationLatitude = userUpdateValues.gymLocationLatitude.toDouble(),
    gymLocationLongitude = userUpdateValues.gymLocationLongitude.toDouble(),
    sleepHours = userUpdateValues.sleepHours.toDouble()

)