package com.cabcta10.weightlossapplication.uiState

import com.cabcta10.weightlossapplication.entity.GeofenceCoordinates
import com.cabcta10.weightlossapplication.entity.Settings

data class SettingsScreenUiState(
    val grocerySelectedLocation : Int = 0,
    val fitnessSelectedLocation : Int = 0,
    val geofenceCoordinates: List<GeofenceCoordinates> = mutableListOf()
)

data class GroceryCoordinates(
    val latitude : String = "",
    val longitude : String = "",
)

fun SettingsScreenUiState.toSettings() : Settings = Settings(
    id = 1,
    grocerySelectedLocation = grocerySelectedLocation,
    fitnessSelectedLocation = fitnessSelectedLocation
)