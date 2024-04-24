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
            when(geofencingEvent?.geofenceTransition) {
                Geofence.GEOFENCE_TRANSITION_ENTER -> {
                    NotificationUtil.displayNotification(context, getRandomNotificationMessage(), R.drawable.grocery_store)
                    println("Entered")
                }
            }
        }
    }

    fun getRandomNotificationMessage (): String {
        val randomIndex = Random.nextInt(0, groceryMessages.size)

        // Return the message at the randomly generated index
        return groceryMessages[randomIndex]
    }

    companion object {
        private const val TAG = "GeofenceBroadcastReceiver"
    }
}