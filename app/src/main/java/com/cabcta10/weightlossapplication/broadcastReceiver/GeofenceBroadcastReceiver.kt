package com.cabcta10.weightlossapplication.broadcastReceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.cabcta10.weightlossapplication.R
import com.cabcta10.weightlossapplication.service.NotificationUtil
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import kotlin.random.Random

class GeofenceBroadcastReceiver(): BroadcastReceiver() {

    private val groceryMessages = arrayOf(
        "Green-labeled groceries are usually healthier than amber or red \uD83D\uDED2\uD83E\uDD66\uD83C\uDF4E",
        "\uD83D\uDED2 Time to make healthier choices! Swap sugary cereal for wholegrain options \uD83C\uDF3E\uD83E\uDD63",
        "\uD83D\uDED2 Make a healthier choice today! Swap sugary drinks for refreshing water. Don't forget to pick up some lemons or limes at the grocery store for added flavor! \uD83D\uDCA7\uD83C\uDF4B",
    )

    private val fitnessMessages = arrayOf(
        "\uD83D\uDCA7 Keep yourself hydrated during workout to keep the weight loss journey intact!",
        "\uD83D\uDCAA Keep up the momentum! A quick session at the gym is all it takes to stay on track with your weight loss journey. You've got this!",
        "\uD83D\uDEB4\u200D♀\uFE0F Pedal your way to progress! The fitness studio nearby is waiting to help you achieve your weight loss goals. Keep moving forward!",
    )

    override fun onReceive(context: Context?, intent: Intent?) {
        println("received intent....")
        if (intent != null && context!= null) {
            val geofencingEvent = GeofencingEvent.fromIntent(intent)
            if (geofencingEvent != null) {
                if (geofencingEvent.hasError()) {
                    val errorMessage = geofencingEvent.errorCode
                    Log.e(TAG, "Geofence error: $errorMessage")
                    return
                }
            }
            val geofenceRequestId = intent.getStringExtra("GEOFENCE_REQUEST_ID")
            var image : Int = 0;
            if(geofenceRequestId == "GROCERY")
                image = R.drawable.grocery_store
            else
                image = R.drawable.dumbbell
            when(geofencingEvent?.geofenceTransition) {
                Geofence.GEOFENCE_TRANSITION_ENTER -> {
                    NotificationUtil.displayNotification(context, getRandomNotificationMessage(geofenceRequestId), image)
                    println("Entered")
                }
            }
        }
    }

    private fun getRandomNotificationMessage (geofenceRequestId : String?): String {
        if(geofenceRequestId == "GROCERY"){
            val randomIndex = Random.nextInt(0, groceryMessages.size)
            return groceryMessages[randomIndex]
        } else {
            val randomIndex = Random.nextInt(0, fitnessMessages.size)
            return fitnessMessages[randomIndex]
        }

    }

    companion object {
        private const val TAG = "GeofenceBroadcastReceiver"
    }
}