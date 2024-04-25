package com.cabcta10.weightlossapplication.broadcastReceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.location.ActivityTransition
import com.google.android.gms.location.ActivityTransitionResult
import com.google.android.gms.location.DetectedActivity

class UserActivityReceiver : BroadcastReceiver() {

    companion object {
        private const val TAG = "UserActivityReceiver"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent == null || !ActivityTransitionResult.hasResult(intent)) {
            Log.d(TAG, "No activity transition result or invalid intent received.")
            return
        }

        // Get the activity transition result
        val result = ActivityTransitionResult.extractResult(intent)
        if (result != null && result.transitionEvents.isNotEmpty()) {
            // Process each detected activity transition event
            for (event in result.transitionEvents) {
                Log.d(TAG, "Activity Transition Detected: ${event.activityType} - ${event.transitionType}")

                // Handle the detected activity transition based on your requirements
                // For example, trigger specific actions or update the awake state
                handleActivityTransition(event.activityType, event.transitionType)
            }
        }
    }

    private fun handleActivityTransition(activityType: Int, transitionType: Int) {
        when (activityType) {
            DetectedActivity.WALKING -> {
                if (transitionType == ActivityTransition.ACTIVITY_TRANSITION_ENTER) {
                    Log.d(TAG, "User started walking.")
                } else if (transitionType == ActivityTransition.ACTIVITY_TRANSITION_EXIT) {
                    Log.d(TAG, "User stopped walking.")
                }
            }
            DetectedActivity.RUNNING -> {
                if (transitionType == ActivityTransition.ACTIVITY_TRANSITION_ENTER) {
                    Log.d(TAG, "User started running.")
                } else if (transitionType == ActivityTransition.ACTIVITY_TRANSITION_EXIT) {
                    Log.d(TAG, "User stopped running.")
                }
            }
            else -> {
                Log.d(TAG, "Unhandled activity type: $activityType")
            }
        }
    }

}
