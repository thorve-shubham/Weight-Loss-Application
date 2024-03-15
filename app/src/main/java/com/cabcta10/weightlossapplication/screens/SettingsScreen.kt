package com.cabcta10.weightlossapplication.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.cabcta10.weightlossapplication.uiState.GroceryCoordinates

@Composable
fun GroceryStoreCoordinates(groceryCoordinates: GroceryCoordinates,
                            updateStoreCoordinates: (GroceryCoordinates)-> Unit) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = groceryCoordinates.latitude,
            onValueChange = { updateStoreCoordinates(groceryCoordinates.copy(latitude = it)) },
            label = { Text("Latitude") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = groceryCoordinates.longitude,
            onValueChange = { updateStoreCoordinates(groceryCoordinates.copy(longitude = it)) },
            label = { Text("Longitude") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

