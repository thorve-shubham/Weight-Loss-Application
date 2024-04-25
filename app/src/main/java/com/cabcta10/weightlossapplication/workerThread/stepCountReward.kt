
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.cabcta10.weightlossapplication.AppDatabase
import com.cabcta10.weightlossapplication.R
import com.cabcta10.weightlossapplication.repository.SettingsRepository
import com.cabcta10.weightlossapplication.repositoryImpl.SettingsRepositoryImpl
import com.cabcta10.weightlossapplication.service.NotificationUtil
import kotlinx.coroutines.flow.firstOrNull


    private suspend fun fetchStepCountValuesFromDatabase(settingsRepository: SettingsRepository): Int {
        val s = settingsRepository.getSettings().firstOrNull()

            if (s != null) {
                return 10000 //return s.defaultStepCount .toInt()  // have to change according to the database String Name
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
        val targetStepCount = fetchTargetStepCount(settingsRepository)
        val stepCount = fetchStepCountValuesFromDatabase(settingsRepository)

        if (stepCount == targetStepCount) {
            NotificationUtil.displayNotification(applicationContext, "Congratulations! You've reached your step goal. Keep up the momentum to stay active.\n", R.drawable.dumbbell)
        }
        return Result.success()
    }
}
