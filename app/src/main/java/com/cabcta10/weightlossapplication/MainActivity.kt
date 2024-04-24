package com.cabcta10.weightlossapplication

import android.Manifest
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cabcta10.weightlossapplication.permissions.HandleRequest
import com.cabcta10.weightlossapplication.permissions.HandleRequests
import com.cabcta10.weightlossapplication.permissions.PermissionDeniedContent
import com.cabcta10.weightlossapplication.screens.SettingsScreen
import com.cabcta10.weightlossapplication.ui.theme.WeightLossApplicationTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.*

class MainActivity : ComponentActivity() {

    private val deniedMessage: String = "App needs notification and location permission to help you achieve your weight loss goal faster. \uD83D\uDCAA"
    private val rationaleMessage: String = "To use this app's functionalities to its fullest, you need to give app the needed permissions."

    @OptIn(ExperimentalPermissionsApi::class)
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {

        val permissionsToRequest = arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.POST_NOTIFICATIONS,
        )

        super.onCreate(savedInstanceState)

        setContent {

            WeightLossApplicationTheme {
                val multiplePermissionsState = rememberMultiplePermissionsState(permissionsToRequest.toList())

                HandleRequests(
                    multiplePermissionsState = multiplePermissionsState,
                    deniedContent = { shouldShowRationale ->
                        PermissionDeniedContent(
                            deniedMessage = deniedMessage,
                            rationaleMessage = rationaleMessage,
                            shouldShowRationale = shouldShowRationale,
                            onRequestPermission = { multiplePermissionsState.launchMultiplePermissionRequest() }
                        )
                    },
                    content = {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            Base(this)
                        }
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun Base(context: Context) {

    val permissionState = rememberPermissionState(Manifest.permission.ACCESS_BACKGROUND_LOCATION)

    val deniedMessageBackgroundLocation = "App Needs background permission access all the time."
    val rationaleMessageBackgroundLocation = "To take the advantage of location based notifications app needs background location access all the time"

    HandleRequest(
        permissionState = permissionState,
        deniedContent = { shouldShowRationale ->
            PermissionDeniedContent(
                deniedMessage = deniedMessageBackgroundLocation,
                rationaleMessage = rationaleMessageBackgroundLocation,
                shouldShowRationale = shouldShowRationale,
                onRequestPermission = { permissionState.launchPermissionRequest() }
            )
        },
        content = {
            SettingsScreen(context)
        }
    )
}


