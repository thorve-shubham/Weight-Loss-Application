package com.cabcta10.weightlossapplication.broadcastReceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.cabcta10.weightlossapplication.R
import com.cabcta10.weightlossapplication.service.NotificationUtil
import com.google.android.gms.location.SleepClassifyEvent
import com.google.android.gms.location.SleepSegmentEvent
import java.util.*

class SleepAPIBroadcastReceiver : BroadcastReceiver() {

    companion object {
        private const val TAG = "SLEEP_RECEIVER"
        const val INTERACTION_ACTION = "com.example.sleepapi.INTERACTION_ACTION"
        const val EXTRA_INTERACTION_STATUS = "interaction_status"
    }

    private lateinit var interactionDetector: InteractionDetector

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent == null || context == null) return

        // Initialize interaction detector if not already initialized
        if (!::interactionDetector.isInitialized) {
            interactionDetector = InteractionDetector(context)
        }

        // Start monitoring for interactions during sleep period
        interactionDetector.startMonitoring()

        // Get the sleep start time and sleep end time from the database
        val sleepStartTimeString = "00:00" // Replace with dynamic value if needed
        val sleepEndTimeString = "07:00"

        val sleepStartCalendar = parseTimeToCalendar(sleepStartTimeString)
        val sleepEndCalendar = parseTimeToCalendar(sleepEndTimeString)
        val currentTime = Calendar.getInstance()
        Log.d(TAG, "sleepStartCalendar time (${sleepStartCalendar.time}) is within sleep period")
        Log.d(TAG, "sleepEndCalendar time (${sleepEndCalendar.time}) is within sleep period")
        Log.d(TAG, "Current time (${currentTime.time}) is within sleep period")

        if (isWithinSleepPeriod(currentTime, sleepStartCalendar, sleepEndCalendar)) {
            // Current time falls within the sleep period
            Log.d(TAG, "Current time (${currentTime.time}) is within sleep period")

            if (SleepSegmentEvent.hasEvents(intent)) {
                val events = SleepSegmentEvent.extractEvents(intent)
                handleSleepSegmentEvents(context, events, sleepStartCalendar, sleepEndCalendar)
            } else if (SleepClassifyEvent.hasEvents(intent)) {
                val events = SleepClassifyEvent.extractEvents(intent)
                handleSleepClassifyEvents(context, events, sleepStartCalendar, sleepEndCalendar)
            }
        }

        // After processing sleep events, check if user was interacting
        val isInteracting = interactionDetector.isInteracting()
        Log.d(TAG, "User interaction status during sleep: $isInteracting")

        // Stop monitoring for interactions when no longer needed
        interactionDetector.stopMonitoring()
    }

    private fun handleSleepSegmentEvents(
        context: Context,
        events: List<SleepSegmentEvent>,
        sleepStartCalendar: Calendar,
        sleepEndCalendar: Calendar
    ) {
        for (event in events) {
            val eventStartTime = Calendar.getInstance().apply {
                timeInMillis = event.startTimeMillis
            }
            val eventEndTime = Calendar.getInstance().apply {
                timeInMillis = event.endTimeMillis
            }

            // Check if the event overlaps with the defined sleep range
            if (eventStartTime.before(sleepEndCalendar) && eventEndTime.after(sleepStartCalendar)) {
                // Event occurred during sleep range, check if user was awake
                if (event.status == 1) {
                    Log.d(TAG, "User was awake from ${eventStartTime.time} to ${eventEndTime.time}")
                }
            }
        }
    }

    private fun handleSleepClassifyEvents(
        context: Context,
        events: List<SleepClassifyEvent>,
        sleepStartCalendar: Calendar,
        sleepEndCalendar: Calendar
    ) {
        for (event in events) {
            val eventTime = Calendar.getInstance().apply {
                timeInMillis = event.timestampMillis
            }

            // Check if the event time is within the defined sleep range
            if (eventTime.before(sleepEndCalendar) && eventTime.after(sleepStartCalendar)) {
                if (event.confidence > 50 && (event.motion > 0 || interactionDetector.isInteracting())) {
                    // Check if the confidence level is high and either motion is detected or user is interacting
                    Log.d(TAG, "User was likely awake at ${eventTime.time}")
                    NotificationUtil.displayNotification(context, "User was likely awake at ${eventTime.time}", R.drawable.ic_launcher_background)
                }
            }
        }
    }

    private fun parseTimeToCalendar(timeString: String): Calendar {
        val calendar = Calendar.getInstance()
        val (hour, minute) = timeString.split(":")
        calendar.set(Calendar.HOUR_OF_DAY, hour.toInt())
        calendar.set(Calendar.MINUTE, minute.toInt())
        calendar.set(Calendar.SECOND, 0)
        return calendar
    }

    private fun isWithinSleepPeriod(
        currentTime: Calendar,
        sleepStartCalendar: Calendar,
        sleepEndCalendar: Calendar
    ): Boolean {
        return currentTime.after(sleepStartCalendar) && currentTime.before(sleepEndCalendar)
    }
}
