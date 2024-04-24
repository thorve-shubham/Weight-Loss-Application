package com.cabcta10.weightlossapplication.screens

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cabcta10.weightlossapplication.AppProvider
import com.cabcta10.weightlossapplication.R
import com.cabcta10.weightlossapplication.entity.GeofenceCoordinates
import com.cabcta10.weightlossapplication.uiState.UserUpdateValues
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
    val (saveClicked, setSaveClicked) = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        // Content of your settings here

        Spacer(modifier = Modifier.weight(1f))

        if (saveClicked) {
            // Show the Snackbar and reset saveClicked after a short delay
            Snackbar(
                modifier = Modifier.padding(16.dp),
                action = {
                    TextButton(onClick = { setSaveClicked(false) }) {
                        Text(text = stringResource(id = android.R.string.ok))
                    }
                },
                content = { Text("Changes saved successfully!") },
            )
        }
        // Buttons at the bottom
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = onCancelClick) {
                Text(text = "Reset")
            }
            Button(onClick = {
                onApplyClick()
                setSaveClicked(true)
            }) {
                Text(text = "Apply")
            }

        }

    }
}

@Composable
fun ApplicationTitle(){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(30.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.dumbell),
                contentDescription = null,
                //contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(40.dp)
                    .height(40.dp)
            )
            Text(
                text = "Weight Loss",
                modifier = Modifier.padding(10.dp),
                fontSize = 25.sp
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun userDetailsUpdate (userUpdateValues: UserUpdateValues,
                       updateUserDetailsValue: (UserUpdateValues)-> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 120.dp, start = 16.dp, end = 16.dp)
    ) {
        Text(text = "Enter The Details Below", modifier = Modifier.padding(20.dp))
        OutlinedTextField(
            value = userUpdateValues.defaultStepCount,
            onValueChange = { updateUserDetailsValue(userUpdateValues.copy(defaultStepCount = it)) },
            label = { Text("Step Count Per Day") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next),
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.padding(16.dp))

        OutlinedTextField(
            value = userUpdateValues.waterIntake,
            onValueChange = { updateUserDetailsValue(userUpdateValues.copy(waterIntake = it)) },
            //enabled = false,
            label = { Text("Water Intake") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.padding(16.dp))

        OutlinedTextField(
            value = userUpdateValues.sleepHours,
            onValueChange = { updateUserDetailsValue(userUpdateValues.copy(sleepHours = it)) },
            label = { Text("Sleep Hours") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

//fun userDetailsReset(userUpdateValues: UserUpdateValues,
//                     updateUserDetailsValue: (UserUpdateValues)-> Unit
//) {
//    updateUserDetailsValue(
//        userUpdateValues.copy(
//            waterIntake = "",
//            defaultStepCount = "",
//            sleepHours = ""
//        )
//    )
//
//}

@SuppressLint("StateFlowValueCalledInComposition", "NewApi")
@Composable
fun SettingsScreen(
    context: Context = LocalContext.current,
    settingsViewModel: SettingsViewModel = viewModel(factory = AppProvider.Factory(context))
) {

    val coroutineScope = rememberCoroutineScope()
    val settingsScreenUiState by settingsViewModel.settingsScreenUiState.collectAsState()
    ApplicationTitle()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        userDetailsUpdate(
            userUpdateValues = settingsScreenUiState.userUpdateValues,
            updateUserDetailsValue = settingsViewModel::updateUserDetailsValue
        )

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

        ApplySettings({
            coroutineScope.launch {
                settingsViewModel.deleteSettings()
                settingsViewModel.resetSettingsScreen()
            }
        }, {
            coroutineScope.launch {
                settingsViewModel.saveSettings()
            }
        })
    }
}






