package com.cabcta10.weightlossapplication.viewModel

import android.app.Application
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cabcta10.weightlossapplication.R
import com.cabcta10.weightlossapplication.WeightLossApplication
import com.cabcta10.weightlossapplication.entity.StepData
import com.cabcta10.weightlossapplication.repository.SettingsRepository
import com.cabcta10.weightlossapplication.repository.StepRepository
import com.cabcta10.weightlossapplication.service.NotificationUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
class StepCounterViewModel (private val application: Application, private val sensorManager: SensorManager, private val stepRepository: StepRepository, private val settingsRepository: SettingsRepository) : AndroidViewModel(application), SensorEventListener {

    private val _steps = MutableLiveData(0)
    val steps: LiveData<Int> = _steps

    private var lastTotalSteps = 0 // Hold the total steps count at the end of the day
    @RequiresApi(Build.VERSION_CODES.O)
    private var today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)

    init {
        loadLastTotalSteps()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadLastTotalSteps() {
        viewModelScope.launch(Dispatchers.IO) {
            // Fetch the last saved step data from the repository
            val stepData = stepRepository.getbyDate(today.toString()).firstOrNull()
            lastTotalSteps = stepData?.steps ?: 0
        }
    }

    fun startCounting() {
        val stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        if (stepSensor != null) {
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)
        } else {
            // Handle the case where the sensor is not available
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_STEP_COUNTER) {
            // Calculate the number of steps taken today
            val totalSteps = event.values[0].toInt()//4000
            val diffSteps = totalSteps - lastTotalSteps //0

            if (diffSteps > lastTotalSteps) {//4000 >0
                lastTotalSteps += 1
            }

            val dailySteps = lastTotalSteps
            println("Step : "+ _steps.value)
            println("Total Step : $totalSteps")
            println("daily Step : $dailySteps")
            val currentDate = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
            viewModelScope.launch(Dispatchers.IO) {
                val record = stepRepository.getbyDate(currentDate).firstOrNull()
                if (record == null|| record?.steps == null) {
                    stepRepository.insert(StepData(date = currentDate, steps = 0, informed = false))
                } else {
                    stepRepository.update(today, dailySteps)
                }
            }
//            if (today != currentDate) {
//                // New day has started
//                today = currentDate
////                lastTotalSteps = totalSteps
//                // Reset the daily steps in LiveData
//                _steps.postValue(0)
//
//                viewModelScope.launch(Dispatchers.IO) {
//                    // Save the new day with 0 steps initially
//                    stepRepository.insert(StepData(date = currentDate, steps = 0, informed = false))
//                }
//            }
            viewModelScope.launch(Dispatchers.IO) {
                val goalSteps = settingsRepository.getSettings().firstOrNull()?.defaultStepCount
                val informed = stepRepository.getbyDate(currentDate)?.firstOrNull()?.informed;

                Log.d("Step Notification", "STARTED ")
                if(goalSteps!= null) {
                    if(dailySteps >= goalSteps!!) {
                        Log.d("Step Notification", dailySteps.toString())
                        if (informed == null || informed == false) {
                            NotificationUtil.displayNotification(getApplication<WeightLossApplication>().applicationContext, "You have achieved your daily step goal of $goalSteps", R.drawable.dumbbell)
                            stepRepository.updateInformed(currentDate, true)
                        }
                    }
                }
            }

        }

    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        // Can be ignored for this simple app
    }

    override fun onCleared() {
        super.onCleared()
        sensorManager.unregisterListener(this)
    }



}