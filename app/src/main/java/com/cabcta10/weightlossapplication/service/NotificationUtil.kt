package com.cabcta10.weightlossapplication.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.cabcta10.weightlossapplication.constants.NOTIFICATION_CHANNEL_ID
import com.cabcta10.weightlossapplication.constants.NOTIFICATION_CHANNEL_NAME
import com.cabcta10.weightlossapplication.constants.NOTIFICATION_ID

object NotificationUtil {
    fun displayNotification(context: Context, geofenceTransition: String, image : Int){
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel(notificationManager)

        val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(image)
            .setContentTitle("Weight Loss Application")
            .setContentText(geofenceTransition)
        notificationManager.notify(NOTIFICATION_ID, notification.build())
    }

    private fun createNotificationChannel(notificationManager: NotificationManager){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
    }
}