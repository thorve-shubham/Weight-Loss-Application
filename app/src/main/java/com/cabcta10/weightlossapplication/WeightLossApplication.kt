package com.cabcta10.weightlossapplication

import android.app.Application

class WeightLossApplication: Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
        println("on Create app")
    }
}