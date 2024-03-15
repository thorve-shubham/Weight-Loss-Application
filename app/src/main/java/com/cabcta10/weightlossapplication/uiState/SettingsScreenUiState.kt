package com.cabcta10.weightlossapplication.uiState

data class SettingsScreenUiState (
    val groceryStoreCoordinates: GroceryCoordinates = GroceryCoordinates()
)

data class GroceryCoordinates(
    val latitude : String = "",
    val longitude : String = "",
)