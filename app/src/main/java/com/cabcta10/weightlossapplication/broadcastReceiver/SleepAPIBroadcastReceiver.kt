package com.cabcta10.weightlossapplication.broadcastReceiver

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.android.gms.location.SleepClassifyEvent
import com.google.android.gms.location.SleepSegmentEvent

class SleepAPIBroadcastReceiver : BroadcastReceiver() {

    companion object {
        private const val TAG = "SLEEP_RECEIVER"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent == null || context == null) return

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
                handleSleepSegmentEvents(events, sleepStartCalendar, sleepEndCalendar)
            } else if (SleepClassifyEvent.hasEvents(intent)) {
                val events = SleepClassifyEvent.extractEvents(intent)
                handleSleepClassifyEvents(events, sleepStartCalendar, sleepEndCalendar)
            }
        }
    }

    private fun handleSleepSegmentEvents(
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
                    // Handle awake state during sleep range
                    // Example: Trigger a notification or perform action for awake event
                    // triggerAwakeNotification(context)
                }
            }
        }
    }

    private fun handleSleepClassifyEvents(
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
                if (event.confidence > 50 && event.motion > 0) {
                    Log.d(TAG, "User was likely awake at ${eventTime.time}")
                    // Handle awake state during sleep range
                    // Example: Trigger a notification or perform action for awake event
                    // triggerAwakeNotification(context)
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
//        if (hour.toInt() < 12) {
//            calendar.add(Calendar.DATE, 1) // If end hour is less than start hour, add one day
//        }
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
