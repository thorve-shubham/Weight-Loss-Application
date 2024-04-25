package com.cabcta10.weightlossapplication.screens
import SimpleTimePicker
import TimeUtil
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.shapes.RectShape
import android.graphics.drawable.shapes.Shape
import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.lazy.LazyColumn
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
import com.cabcta10.weightlossapplication.entity.GeofenceCoordinates
import com.cabcta10.weightlossapplication.uiState.UserUpdateValues
import com.cabcta10.weightlossapplication.viewModel.SettingsViewModel
import kotlinx.coroutines.launch
import kotlin.math.round
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
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                fitnessCoordinates.forEach { item ->
                    DropdownMenuItem(
                        modifier = Modifier.fillMaxWidth(),
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
            .padding(vertical = 5.dp)
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
                    modifier = Modifier.size(45.dp).padding(end=0.dp)
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
        Spacer(modifier = Modifier.padding(5.dp))

        OutlinedTextField(
            value = userUpdateValues.waterIntake,
            onValueChange = { updateUserDetailsValue(userUpdateValues.copy(waterIntake = it)) },
            //enabled = false,
            label = { Text("Water Intake") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.padding(10.dp))

        var showStartDialog by remember { mutableStateOf(false) }
        var showEndDialog by remember { mutableStateOf(false) }

        Spacer(modifier = Modifier.padding(5.dp))
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


        Spacer(modifier = Modifier.padding(5.dp))
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
@SuppressLint("StateFlowValueCalledInComposition", "NewApi")
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
            .padding(10.dp)
    ) {

        ApplicationTitle()
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
        Spacer(modifier = Modifier.height(5.dp))
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



