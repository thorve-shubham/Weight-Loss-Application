package com.cabcta10.weightlossapplication

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cabcta10.weightlossapplication.screens.PermissionRequestScreen
import com.cabcta10.weightlossapplication.screens.SettingsScreen
import com.cabcta10.weightlossapplication.ui.theme.WeightLossApplicationTheme

class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WeightLossApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Base(this)
                }
            }
        }

        val permissionRequester = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (!isGranted) {
                requestActivityRecognitionPermission(this)
            } else {
                // Permission is granted, handle accordingly
            }
        }
    }
}

@Composable
fun Base(context: Context) {
    SettingsScreen(context)
}


private fun requestActivityRecognitionPermission(context: Context) {

    val intent = Intent().apply {
        action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        data = Uri.fromParts("package", context.packageName, null)
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }

    context.startActivity(intent)
}



@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview(){
    WeightLossApplicationTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            PermissionRequestScreen {

            }
        }
    }
}


