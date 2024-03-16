package com.cabcta10.weightlossapplication.broadcastReceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent

class GeofenceBroadcastReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent != null) {
            val geofencingEvent = GeofencingEvent.fromIntent(intent)
            if (geofencingEvent != null) {
                if (geofencingEvent.hasError()) {
                    val errorMessage = geofencingEvent.errorCode
                    Log.e(TAG, "Geofence error: $errorMessage")
                    return
                }
            }
            when(geofencingEvent?.geofenceTransition) {
                Geofence.GEOFENCE_TRANSITION_ENTER -> "Handle Enter"
                Geofence.GEOFENCE_TRANSITION_DWELL -> "Handle Dwell"
                else -> "Unknown"
            }
        }
    }

    companion object {
        private const val TAG = "GeofenceBroadcastReceiver"
    }
}