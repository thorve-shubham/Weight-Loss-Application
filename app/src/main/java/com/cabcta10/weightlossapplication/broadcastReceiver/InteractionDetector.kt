package com.cabcta10.weightlossapplication.broadcastReceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter

class InteractionDetector(private val context: Context) {

    private var isInteracting = false
    private var receiverRegistered = false

    private val screenReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            // Handle screen touch or interaction events here
            if (intent?.action == Intent.ACTION_SCREEN_ON) {
                // Screen has been turned on, user is likely interacting
                isInteracting = true
            } else if (intent?.action == Intent.ACTION_SCREEN_OFF) {
                // Screen has been turned off, interaction might have stopped
                isInteracting = false
            }
        }
    }

    fun startMonitoring() {
        if (!receiverRegistered) {
            val filter = IntentFilter().apply {
                addAction(Intent.ACTION_SCREEN_ON)
                addAction(Intent.ACTION_SCREEN_OFF)
            }
            context.registerReceiver(screenReceiver, filter)
            receiverRegistered = true
        }
    }

    fun stopMonitoring() {
        if (receiverRegistered) {
            context.unregisterReceiver(screenReceiver)
            receiverRegistered = false
        }
    }

    fun isInteracting(): Boolean {
        return isInteracting
    }
}
