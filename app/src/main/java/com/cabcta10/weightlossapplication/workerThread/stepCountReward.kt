import android.content.Context
import androidx.compose.foundation.layout.Column
//import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.Constraints
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.*
import com.cabcta10.weightlossapplication.R
import com.cabcta10.weightlossapplication.service.NotificationUtil
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

/*@Composable
fun StepCountReward(viewModel: StepCountRewardViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val stepCount = viewModel.stepCount.value
    val DEFAULT_STEP_THRESHOLD = 10000
    val notificationContent = if (stepCount > DEFAULT_STEP_THRESHOLD) {
        "Congratulations! You have reached your daily step goal."
    } else {
        "Keep going! You're getting closer to your daily step goal."
    }

    Column {
        Text("Step Count: $stepCount")
        Text(notificationContent)
    }
}*/

class StepCountRewardWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val database = Database()
        val count = database.getStepCount()

        if (count > DEFAULT_STEP_THRESHOLD) {
            NotificationUtil.displayNotification(applicationContext, "Congratulations! You have reached your daily step goal.", R.drawable.grocery_store)
            //showNotification("Congratulations! You have reached your daily step goal.")
        } else {
            NotificationUtil.displayNotification(applicationContext, "Keep going! You're getting closer to your daily step goal.", R.drawable.grocery_store)
            //showNotification("Keep going! You're getting closer to your daily step goal.")
        }

        return Result.success()
    }

   /* private fun showNotification(message: String) {
        // Code to show notification goes here
        // For simplicity, showing a log message
        println("Notification: $message")
        NotificationUtil.displayNotification(context, message, R.drawable.grocery_store)
    }*/

    companion object {
        private const val DEFAULT_STEP_THRESHOLD = 10000 // Adjust threshold as needed

        fun scheduleWorker(context: Context) {
            val currentTimeMillis = System.currentTimeMillis()
            val sevenPM = currentTimeMillis - currentTimeMillis % TimeUnit.DAYS.toMillis(1) + TimeUnit.HOURS.toMillis(19)
            val delay = sevenPM - currentTimeMillis

            /*val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()*/

            val workRequest = OneTimeWorkRequestBuilder<StepCountRewardWorker>()
                //.setConstraints(constraints)
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .build()

            WorkManager.getInstance(context).enqueue(workRequest)
        }
    }
}
