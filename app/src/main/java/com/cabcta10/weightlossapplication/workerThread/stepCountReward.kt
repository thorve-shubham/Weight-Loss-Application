package com.cabcta10.weightlossapplication.workerThread/*
import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

// Dummy class representing your database
class Database {
    suspend fun getStepCount(): Int {
        // Simulate fetching step count from database
        return withContext(Dispatchers.IO) {
            // Actual database query or operation here
            5000
        }
    }
}

class StepCountRewardViewModel : ViewModel() {
    val stepCount: MutableState<Int> = mutableStateOf(0)

    fun fetchStepCountFromDatabase() {
        viewModelScope.launch {
            val database = Database()
            val count = database.getStepCount()
            stepCount.value = count
        }
    }
}

@Composable
fun StepCountReward(viewModel: StepCountRewardViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val stepCount = viewModel.stepCount.value
    val notificationContent = if (stepCount > DEFAULT_STEP_THRESHOLD) {
        "Congratulations! You have reached your daily step goal."
    } else {
        "Keep going! You're getting closer to your daily step goal."
    }

    Column {
        Text("Step Count: $stepCount")
        Text(notificationContent)
    }
}

class StepCountRewardWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val database = Database()
        val count = database.getStepCount()

        if (count > DEFAULT_STEP_THRESHOLD) {
            showNotification("Congratulations! You have reached your daily step goal.")
        } else {
            showNotification("Keep going! You're getting closer to your daily step goal.")
        }

        return Result.success()
    }

    private fun showNotification(message: String) {
        // Code to show notification goes here
        // For simplicity, showing a log message
        println("Notification: $message")
    }

    companion object {
        private const val DEFAULT_STEP_THRESHOLD = 10000 // Adjust threshold as needed

        fun scheduleWorker(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val workRequest = PeriodicWorkRequestBuilder<StepCountRewardWorker>(1, TimeUnit.DAYS)
                .setConstraints(constraints)
                .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                "StepCountRewardWorker",
                ExistingPeriodicWorkPolicy.REPLACE,
                workRequest
            )
        }
    }
}*/
