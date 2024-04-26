
import android.content.Context
import android.icu.util.Calendar
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.cabcta10.weightlossapplication.AppDatabase
import com.cabcta10.weightlossapplication.R
import com.cabcta10.weightlossapplication.repository.SettingsRepository
import com.cabcta10.weightlossapplication.repository.StepRepository
import com.cabcta10.weightlossapplication.repositoryImpl.SettingsRepositoryImpl
import com.cabcta10.weightlossapplication.repositoryImpl.StepRepositoryImpl
import com.cabcta10.weightlossapplication.service.NotificationUtil
import kotlinx.coroutines.flow.firstOrNull
import java.time.LocalDate
import java.time.format.DateTimeFormatter


private suspend fun fetchStepCountValuesFromDatabase(stepRepository: StepRepository): Int {
        var today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
        val s = stepRepository.getbyDate(today.toString()).firstOrNull()

            if (s != null) {
                return s.steps// have to change according to the database String Name
            } else {
                return 0;
            }
    }
    private suspend fun fetchTargetStepCount(settingsRepository: SettingsRepository): Int {

            val s = settingsRepository.getSettings().firstOrNull()

                if (s != null) {
                    return s.defaultStepCount .toInt()
                } else {
                    return 0;
                }

    }

class StepCountRewardWorker(appContext: Context, workerParams: WorkerParameters
) :    CoroutineWorker(appContext, workerParams) {


    override suspend fun doWork(): Result {
        val settingsRepository = SettingsRepositoryImpl(AppDatabase.getDatabase(applicationContext).settingsDAO())
        val stepRepository = StepRepositoryImpl(AppDatabase.getDatabase(applicationContext).stepDao())
        val targetStepCount = fetchTargetStepCount(settingsRepository)
        val stepCount = fetchStepCountValuesFromDatabase(stepRepository)

        if (stepCount >= targetStepCount) {
            NotificationUtil.displayNotification(applicationContext, "Congratulations! You've reached your step goal. Keep up the momentum to stay active.\n", R.drawable.dumbbell)
        } else {
            NotificationUtil.displayNotification(applicationContext, "Keep Going! Walking and Running will help you reach your goal for the day.", R.drawable.dumbbell)
        }
        return Result.success()
    }
}
