package com.cabcta10.weightlossapplication.screenui

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
//import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cabcta10.weightlossapplication.AppProvider
import com.cabcta10.weightlossapplication.viewModel.SettingsViewModel
import com.cabcta10.weightlossapplication.viewModel.StepCounterViewModel
//import com.example.myapplication.viwemodel.StepCounterViewModel

@Composable
fun StepCounterScreen(
    context: Context = LocalContext.current) {
    Column(modifier = Modifier.padding(16.dp)) {
//        val steps = stepCounterViewModel.steps.value ?: 0
        //  Text(text = "Steps taken: $steps")
        Button(onClick = { /* Handle button click, maybe reset steps */ }) {
            Text(text = "Reset Steps")
        }
    }
}