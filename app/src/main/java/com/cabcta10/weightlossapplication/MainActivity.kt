package com.cabcta10.weightlossapplication

import StepCountRewardWorker
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.cabcta10.weightlossapplication.screens.PermissionRequestScreen
import com.cabcta10.weightlossapplication.screens.SettingsScreen
import com.cabcta10.weightlossapplication.ui.theme.WeightLossApplicationTheme
import java.util.concurrent.TimeUnit

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
        // Call StepCountRewardWorker.scheduleWorker(context) here to register the worker
        scheduleStepCounterWorker()
    }

    fun scheduleStepCounterWorker() {
        val workRequest = PeriodicWorkRequestBuilder<StepCountRewardWorker>(
            16,
            TimeUnit.MINUTES
        ).build()
        WorkManager.getInstance(this).enqueue(
            workRequest
        )
        println("Scheduled..")
    }
}

@Composable
fun Base(context: Context) {
    SettingsScreen(context)
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


