package com.cabcta10.weightlossapplication.screens

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cabcta10.weightlossapplication.AppProvider
import com.cabcta10.weightlossapplication.entity.GeofenceCoordinates
import com.cabcta10.weightlossapplication.viewModel.SettingsViewModel
import kotlinx.coroutines.launch
import kotlin.reflect.KFunction1



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroceryStoreCoordinates(
    grocerySelectedLocation: Int,
    updateStoreCoordinates: KFunction1<Int, Unit>,
    geofenceCoordinates: List<GeofenceCoordinates>
) {
    var expanded by remember { mutableStateOf(false) }

    var groceryLocation = geofenceCoordinates.firstOrNull { it.id == grocerySelectedLocation }

    var groceryCoordinates = geofenceCoordinates.filter { it.type == "grocery" }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(text = "Where do you buy Groceries ?", modifier = Modifier.padding(10.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {
            if (groceryLocation != null) {
                TextField(
                    value = groceryLocation!!.name,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor()
                )
            } else {
                // Show placeholder text when groceryLocation is empty
                TextField(
                    value = "Select Grocery Shop",
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor()
                )
            }

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                groceryCoordinates.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(text = item.name) },
                        onClick = {
                            groceryLocation = item
                            expanded = false
                            updateStoreCoordinates(item.id)
                        }
                    )
                }
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FitnessStoreCoordinates(
    fitnessSelectedLocation: Int,
    updateFitnessCoordinates: KFunction1<Int, Unit>,
    geofenceCoordinates: List<GeofenceCoordinates>
) {
    var expanded by remember { mutableStateOf(false) }

    var fitnessLocation = geofenceCoordinates.firstOrNull { it.id == fitnessSelectedLocation }

    var fitnessCoordinates = geofenceCoordinates.filter { it.type == "fitness" }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(text = "Where do you Workout ?", modifier = Modifier.padding(10.dp))
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {
            if (fitnessLocation != null) {
                TextField(
                    value = fitnessLocation!!.name,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor()
                )
            } else {
                // Show placeholder text when groceryLocation is empty
                TextField(
                    value = "Select Fitness Studio",
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor()
                )
            }

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                fitnessCoordinates.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(text = item.name) },
                        onClick = {
                            fitnessLocation = item
                            expanded = false
                            updateFitnessCoordinates(item.id)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ApplySettings(
    onCancelClick: () -> Unit,
    onApplyClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = onCancelClick) {
                Text(text = "Cancel")
            }
            Button(onClick = onApplyClick) {
                Text(text = "Apply")
            }
        }
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun SettingsScreen(
    context: Context = LocalContext.current,
    settingsViewModel: SettingsViewModel = viewModel(factory = AppProvider.Factory(context))
) {

    val coroutineScope = rememberCoroutineScope()
    val settingsScreenUiState by settingsViewModel.settingsScreenUiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        GroceryStoreCoordinates(
            grocerySelectedLocation = settingsScreenUiState.grocerySelectedLocation,
            updateStoreCoordinates = settingsViewModel::updateStoreCoordinates,
            geofenceCoordinates = settingsScreenUiState.geofenceCoordinates
        )


        FitnessStoreCoordinates(
            fitnessSelectedLocation = settingsScreenUiState.fitnessSelectedLocation,
            updateFitnessCoordinates = settingsViewModel::updateFitnessCoordinates,
            geofenceCoordinates = settingsScreenUiState.geofenceCoordinates
        )


        ApplySettings({ 1 + 2 }, {
            coroutineScope.launch {
                settingsViewModel.saveSettings()
            }
        })
    }
}




