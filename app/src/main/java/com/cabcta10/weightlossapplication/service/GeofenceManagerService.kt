package com.cabcta10.weightlossapplication.service

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.cabcta10.weightlossapplication.broadcastReceiver.GeofenceBroadcastReceiver
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices

class GeofenceManagerService(private val context: Context) {

    private val geofencingClient: GeofencingClient = LocationServices.getGeofencingClient(context)

    fun addGeofence(
        latitude: Double,
        longitude: Double,
        isFitness: Boolean
    ) {
        println("here")
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            println("App dont have enough permissions to create Geofence...")
            return
        }

        var geofencingRequest = ""
        if(isFitness) {
            geofencingRequest = "FITNESS"
        } else {
            geofencingRequest = "GROCERY"
        }

        println("we have permissions")
        val geofence = Geofence.Builder()
            .setRequestId(geofencingRequest)
            .setCircularRegion(latitude, longitude, 200f)
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
            .setLoiteringDelay(5000)
            .build()

        removeGeofence(listOf(geofencingRequest))
        println("removed existing geofence..\nRequest ID : $geofencingRequest")
        geofencingClient.addGeofences(getGeofencingRequest(geofence), getPendingIntent(context, geofencingRequest)).run {
            addOnSuccessListener {
                println("Added Geofence...$latitude and $longitude \nRequest Id : $geofencingRequest")
            }
            addOnFailureListener {
                it.printStackTrace()
                println("Failed to add Geofence...\n$latitude and $longitude \n" +
                        "Request Id : $geofencingRequest")
            }
        }
    }

    private fun getPendingIntent(context: Context, geofencingRequest : String): PendingIntent {
        val intent = Intent(context, GeofenceBroadcastReceiver::class.java).apply {
            putExtra("GEOFENCE_REQUEST_ID", geofencingRequest)
        }
        return PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )
    }

    private fun removeGeofence(geofencingRequest : List<String>) {
        geofencingClient.removeGeofences(geofencingRequest)
    }

    private fun getGeofencingRequest(geofence: Geofence): GeofencingRequest {
        return GeofencingRequest.Builder()
            .setInitialTrigger(
                GeofencingRequest.INITIAL_TRIGGER_ENTER
                        or GeofencingRequest.INITIAL_TRIGGER_EXIT
                        or GeofencingRequest.INITIAL_TRIGGER_DWELL
            )
            .addGeofence(geofence)
            .build()
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: GeofenceManagerService? = null

        fun getInstance(context: Context): GeofenceManagerService {
            return INSTANCE ?: synchronized(this) {
                val instance = GeofenceManagerService(context.applicationContext)
                INSTANCE = instance
                instance
            }
        }
    }

}