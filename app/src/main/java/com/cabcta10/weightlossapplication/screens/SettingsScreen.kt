package com.cabcta10.weightlossapplication.screens
import SimpleTimePicker
import TimeUtil
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.shapes.RectShape
import android.graphics.drawable.shapes.Shape
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cabcta10.weightlossapplication.AppProvider
import com.cabcta10.weightlossapplication.R
import com.cabcta10.weightlossapplication.uiState.GroceryCoordinates
import com.cabcta10.weightlossapplication.uiState.UserUpdateValues
import com.cabcta10.weightlossapplication.viewModel.SettingsViewModel
import kotlinx.coroutines.launch
import kotlin.math.round

@Composable
fun GroceryStoreCoordinates(groceryCoordinates: GroceryCoordinates,
                            updateStoreCoordinates: (GroceryCoordinates)-> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(text = "Enter Grocery Coordinates", modifier = Modifier.padding(10.dp))
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
@Composable
fun ApplySettings(
    onCancelClick: () -> Unit,
    onApplyClick: () -> Unit
) {
    var snackbarVisible by remember { mutableStateOf(false) }

    if (snackbarVisible) {
        Snackbar(
            modifier = Modifier.padding(16.dp),
            action = {
                TextButton(onClick = { snackbarVisible = false }) {
                    Text(text = "Dismiss")
                }
            },
            content = {
                Text(text = "Changes saved successfully!")
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 20.dp)
    ) {
        Spacer(modifier = Modifier.weight(1f))

        // Buttons at the bottom with 50% width and evenly spaced
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Button(
                onClick = onCancelClick,
                modifier = Modifier
                    .weight(.4f)
                    .padding(vertical = 8.dp, horizontal = 10.dp),
                shape = RoundedCornerShape(5.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary) // Example: Apply button with blue background

            ) {
                Text(text = "Reset")
            }

            Button(
                onClick = {
                    onApplyClick()
                    snackbarVisible = true
                },
                modifier = Modifier
                    .weight(.4f)
                    .padding(vertical = 8.dp, horizontal = 10.dp),
                shape = RoundedCornerShape(5.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary) // Example: Apply button with blue background
            ) {
                Text(text = "Apply", color = Color.White)
            }
        }
    }
}

@Composable
fun ApplicationTitle() {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(20.dp),
                horizontalArrangement = Arrangement.Center

            ) {
                Image(
                    painter = painterResource(R.drawable.dumbell),
                    contentDescription = null,
                    modifier = Modifier.size(45.dp).padding(end=5.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Weight Loss",
                    fontSize = 30.sp
                )
            }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun userDetailsUpdate (userUpdateValues: UserUpdateValues,
                       updateUserDetailsValue: (UserUpdateValues)-> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp)
            .verticalScroll(rememberScrollState())
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
        Text(text = "Select the Grocery and Gym Locations: ", modifier = Modifier.padding(10.dp))
        var isExpanded by remember {
            mutableStateOf(value = false)
        }
        var locations by remember {
            mutableStateOf(value = "")
        }
        var latitude by remember {
            mutableStateOf(value = "")
        }
        var longitude by remember {
            mutableStateOf(value = "")
        }
        ExposedDropdownMenuBox(expanded = isExpanded, onExpandedChange = {
            isExpanded = it
        },
        ) {

            TextField(
                value = locations,
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
                },
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                modifier = Modifier
                    .fillMaxWidth(1.2f) // Adjust the width here
                    .padding(end = 20.dp) // Add padding to match your previous code
                    .menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = isExpanded,


                onDismissRequest = { isExpanded = false},
            ) {
                DropdownMenuItem(text = {
                    Text(text = "Aldi"
                    )},
                    onClick = {
                        locations = "Aldi"
                        latitude = "19"
                        longitude = "21"
                        isExpanded = false
                        updateUserDetailsValue(userUpdateValues.copy(groceryLocationLatitude = latitude, groceryLocationLongitude = longitude))
                    },
                )
                DropdownMenuItem(text = {
                    Text(text = "Tesco") },
                    onClick = {
                        locations = "Tesco"
                        latitude = "219"
                        longitude = "221"
                        isExpanded = false
                        updateUserDetailsValue(userUpdateValues.copy(groceryLocationLatitude = latitude, groceryLocationLongitude = longitude))
                    })
            }

        }

        /*OutlinedTextField(
           value = userUpdateValues.groceryLocation,
           onValueChange = { updateUserDetailsValue(userUpdateValues.copy(groceryLocation = it)) },
           label = { Text("Grocery Location") },
           keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
           modifier = Modifier.fillMaxWidth()
       )*/


        Spacer(modifier = Modifier.padding(16.dp))
        var isExpandedGym by remember {
            mutableStateOf(value = false)
        }
        var locationsGym by remember {
            mutableStateOf(value = "")
        }
        var latitudeGym by remember {
            mutableStateOf(value = "")
        }
        var longitudeGym by remember {
            mutableStateOf(value = "")
        }
        ExposedDropdownMenuBox(expanded = isExpandedGym, onExpandedChange = {
            isExpandedGym = it
        }) {

            TextField(
                value = locationsGym,
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpandedGym)
                },
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                modifier = Modifier
                    .fillMaxWidth(1.2f) // Adjust the width here
                    .padding(end = 20.dp) // Add padding to match your previous code
                    .menuAnchor())
            ExposedDropdownMenu(
                expanded = isExpandedGym,
                onDismissRequest = { isExpandedGym = false}
            ) {
                DropdownMenuItem(text = {
                    Text(text = "Strath Sport") },
                    onClick = {
                        locationsGym = "Strath Sport"
                        latitudeGym = "119"
                        longitudeGym = "121"
                        isExpandedGym = false
                        updateUserDetailsValue(userUpdateValues.copy(gymLocationLatitude = latitudeGym, gymLocationLongitude = longitudeGym))
                    },
                )
                DropdownMenuItem(text = {
                    Text(text = "Glasgow Gym") },
                    onClick = {
                        locationsGym = "Glasgow Gym"
                        latitudeGym = "319"
                        longitudeGym = "321"
                        isExpandedGym = false
                        updateUserDetailsValue(userUpdateValues.copy(gymLocationLatitude = latitudeGym, gymLocationLongitude = longitudeGym))
                    })
            }

        }

        /*OutlinedTextField(
            value = userUpdateValues.gymLocation,
            onValueChange = { updateUserDetailsValue(userUpdateValues.copy(gymLocation = it)) },
            label = { Text("Gym Location") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )*/
        var showStartDialog by remember { mutableStateOf(false) }
        var showEndDialog by remember { mutableStateOf(false) }

        Spacer(modifier = Modifier.padding(16.dp))
        Text("Sleep Start Time")
        OutlinedButton(
            onClick = { showStartDialog = true },
            modifier = Modifier.fillMaxWidth().height(45.dp),
            shape = RoundedCornerShape(0),
            content = {
                Text(text = userUpdateValues.sleepStartTime,
                    textAlign = TextAlign.Start, // Align text to the left
                    color = Color.Black)

            }
        )


        Spacer(modifier = Modifier.padding(16.dp))
        Text("Sleep End Time")
        OutlinedButton(
            onClick = {
                showEndDialog = true
                      },
            modifier = Modifier.fillMaxWidth().height(45.dp),
            shape = RoundedCornerShape(0),
            content = {
                Text(text = userUpdateValues.sleepEndTime,
                    textAlign = TextAlign.Left, // Align text to the left
                    color = Color.Black)
            }
        )



        if (showStartDialog) {
            SimpleTimePicker(timeString = userUpdateValues.sleepStartTime) { newStartTime ->
                updateUserDetailsValue(
                    userUpdateValues.copy(
                        sleepStartTime = newStartTime
                    )
                )
                showStartDialog = false
            }
        }

        if (showEndDialog) {
            SimpleTimePicker(timeString = userUpdateValues.sleepEndTime) { newEndTime ->
                updateUserDetailsValue(
                    userUpdateValues.copy(
                        sleepEndTime = newEndTime
                    )
                )
                showEndDialog = false
            }
        }

    }
}

fun userDetailsReset(userUpdateValues: UserUpdateValues,
                     updateUserDetailsValue: (UserUpdateValues)-> Unit
) {
    updateUserDetailsValue(
        userUpdateValues.copy(
            waterIntake = "0.0",
            defaultStepCount = "0.0",
            sleepStartTime = "23:00",
            sleepEndTime = "7:00"
        )
    )

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
//            .padding(horizontal = 16.dp)
    ) {
        ApplicationTitle()
        userDetailsUpdate(
            userUpdateValues = settingsScreenUiState.userUpdateValues,
            updateUserDetailsValue = settingsViewModel::updateUserDetailsValue,

            )
        /*GroceryStoreCoordinates(groceryCoordinates = settingsScreenUiState.groceryStoreCoordinates,
            updateStoreCoordinates = settingsViewModel::updateStoreCoordinates
        )*/

        ApplySettings({  coroutineScope.launch {
            settingsViewModel.deleteSettings()
            userDetailsReset(userUpdateValues = settingsScreenUiState.userUpdateValues,
                updateUserDetailsValue = settingsViewModel::updateUserDetailsValue)
        } }, {
            coroutineScope.launch {
                settingsViewModel.saveSettings()
            }
        })
    }
}



