package com.cabcta10.weightlossapplication.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cabcta10.weightlossapplication.AppProvider
import com.cabcta10.weightlossapplication.uiState.GroceryCoordinates
import com.cabcta10.weightlossapplication.viewModel.SettingsViewModel
import kotlinx.coroutines.launch

@Composable
fun GroceryStoreCoordinates(groceryCoordinates: GroceryCoordinates,
                            updateStoreCoordinates: (GroceryCoordinates)-> Unit,
                            onSave: () -> Unit) {

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

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel = viewModel(factory = AppProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val settingsScreenUiState by settingsViewModel.settingsScreenUiState.collectAsState()
    GroceryStoreCoordinates(groceryCoordinates = settingsScreenUiState.groceryStoreCoordinates,
        updateStoreCoordinates = settingsViewModel::updateStoreCoordinates,
        onSave = {
            coroutineScope.launch {
                settingsViewModel.saveSettings()
            }
        })
}

