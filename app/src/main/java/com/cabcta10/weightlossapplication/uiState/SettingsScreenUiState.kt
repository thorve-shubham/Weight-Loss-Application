package com.cabcta10.weightlossapplication.uiState

import com.cabcta10.weightlossapplication.entity.Settings
import kotlinx.coroutines.flow.Flow

data class SettingsScreenUiState(
    val groceryStoreCoordinates: GroceryCoordinates = GroceryCoordinates()
)

data class GroceryCoordinates(
    val latitude : String = "",
    val longitude : String = "",
)

fun SettingsScreenUiState.toSettings() : Settings = Settings(
    id = 1,
    latitude  = groceryStoreCoordinates.latitude.toDouble(),
    longitude =  groceryStoreCoordinates.longitude.toDouble()
)