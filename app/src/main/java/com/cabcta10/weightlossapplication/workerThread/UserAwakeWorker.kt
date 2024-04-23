package com.cabcta10.weightlossapplication.workerThread

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.work.WorkerParameters
import androidx.work.CoroutineWorker
import com.google.android.gms.location.ActivityRecognition
import com.google.android.gms.location.ActivityRecognitionClient
import com.google.android.gms.location.DetectedActivity
import kotlinx.coroutines.coroutineScope

class UserAwakeWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    private val activityRecognitionClient: ActivityRecognitionClient =
        ActivityRecognition.getClient(context)


    @RequiresApi(Build.VERSION_CODES.Q)
    override suspend fun doWork(): Result {
        try {
            if (!hasActivityRecognitionPermission(applicationContext)) {
                // Permission is not granted, show a permission request
//                requestActivityRecognitionPermission()
                // Return a failure result since permission is not granted
                return Result.failure()
            }



        } catch (ex: Exception) {

            return Result.failure()
        }
        return Result.success()
    }



    @RequiresApi(Build.VERSION_CODES.Q)
    private suspend fun hasActivityRecognitionPermission(context: Context): Boolean {
        return ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACTIVITY_RECOGNITION
        ) == PackageManager.PERMISSION_GRANTED
    }
}
