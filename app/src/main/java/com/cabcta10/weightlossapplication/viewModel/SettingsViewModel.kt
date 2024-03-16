package com.cabcta10.weightlossapplication.viewModel

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cabcta10.weightlossapplication.repository.SettingsRepository
import com.cabcta10.weightlossapplication.uiState.GroceryCoordinates
import com.cabcta10.weightlossapplication.uiState.SettingsScreenUiState
import com.cabcta10.weightlossapplication.uiState.toSettings
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class SettingsViewModel(private val settingsRepository: SettingsRepository, private val appContext: Context) : ViewModel()  {

    private val _settingsScreenUiState = MutableStateFlow(SettingsScreenUiState())
    val settingsScreenUiState: StateFlow<SettingsScreenUiState> = _settingsScreenUiState.asStateFlow()

    private val geofencingClient: GeofencingClient = LocationServices.getGeofencingClient(appContext)

    init {
        fetchDataFromDatabase()
    }

    private fun fetchDataFromDatabase() {
        viewModelScope.launch {
            settingsRepository.getSettings().collect { settingsFromDatabase ->
                if(settingsFromDatabase != null) {
                    val groceryCoordinates = GroceryCoordinates(latitude = settingsFromDatabase.latitude.toString(), longitude = settingsFromDatabase.longitude.toString())
                    _settingsScreenUiState.value = _settingsScreenUiState.value.copy(
                        groceryStoreCoordinates = groceryCoordinates
                    )
                }
            }
        }
    }

     fun updateStoreCoordinates(groceryCoordinates: GroceryCoordinates) {
        _settingsScreenUiState.update { currentState ->
            currentState.copy(
                groceryStoreCoordinates = groceryCoordinates
            )
        }
    }

    suspend fun saveSettings() {
        //vaidate something here
        settingsRepository.insertSettings(settingsScreenUiState.value.toSettings())
    }


    //Geofencing Logic

    fun addGeofence(geofence: Geofence, pendingIntent: PendingIntent) {
        if (ActivityCompat.checkSelfPermission(
                appContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        } else {
            geofencingClient.addGeofences(geofenceRequest(geofence), pendingIntent).run {
                addOnSuccessListener {
                    // Geofence added successfully
                }
                addOnFailureListener {
                    // Failed to add geofence
                }
            }
        }

    }

    fun removeGeofence(pendingIntent: PendingIntent) {
        geofencingClient.removeGeofences(pendingIntent).run {
            addOnSuccessListener {
                // Geofences removed successfully
            }
            addOnFailureListener {
                // Failed to remove geofences
            }
        }
    }

    private fun geofenceRequest(geofence: Geofence): GeofencingRequest {
        return GeofencingRequest.Builder()
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            .addGeofence(geofence)
            .build()
    }
}