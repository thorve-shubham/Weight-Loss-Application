package com.cabcta10.weightlossapplication.workerThread

import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.WorkManager
import android.content.Context
import com.cabcta10.weightlossapplication.AppDatabase
import com.cabcta10.weightlossapplication.entity.StepData

class StepWorker(appContext: Context, workerParams: WorkerParameters):
    CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {


        val stepDao = AppDatabase.getDatabase(applicationContext).stepDao()

        //set cpunter to 0 in state
        return Result.success()
    }
}