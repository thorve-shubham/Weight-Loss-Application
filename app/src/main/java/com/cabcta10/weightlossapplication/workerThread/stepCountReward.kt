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
import com.cabcta10.weightlossapplication.uiState.SettingsScreenUiState
import com.cabcta10.weightlossapplication.viewModel.SettingsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

// Dummy class representing your database
/*class Database {
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
}*/

/*@Composable
fun StepCountReward(viewModel: SettingsViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val stepCount = viewModel.stepCount.value
    //val DEFAULT_STEP_THRESHOLD = 10000
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
    private val _settingsScreenUiState = MutableStateFlow(SettingsScreenUiState())
    val settingsScreenUiState: StateFlow<SettingsScreenUiState> = _settingsScreenUiState.asStateFlow()

    fun fetchStepCountValuesFromDatabase(): Int {
       return 10000;
    }

    fun fetchTargetStepCount(): Int {
        val defaultStepCountString = _settingsScreenUiState.value.userUpdateValues.defaultStepCount
        return try {
            defaultStepCountString.toIntOrNull() ?: 0
        } catch (e: NumberFormatException) {
            0
        }
    }

class StepCountRewardWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {

        val stepCount = fetchStepCountValuesFromDatabase()

        val targetStepCount = fetchTargetStepCount()
        println("Periodic")
        if (stepCount == targetStepCount) {
            println("Periodic")
            NotificationUtil.displayNotification(applicationContext, "Congratulations! You have reached your daily step goal.", R.drawable.grocery_store)
        }
        return Result.success()
    }




}
