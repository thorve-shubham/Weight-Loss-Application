package com.cabcta10.weightlossapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cabcta10.weightlossapplication.screens.GroceryStoreCoordinates
import com.cabcta10.weightlossapplication.screens.SettingsScreen
import com.cabcta10.weightlossapplication.ui.theme.WeightLossApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeightLossApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Base()
                }
            }
        }
    }
}

@Composable
fun Base() {
    SettingsScreen()
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    WeightLossApplicationTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Base()
        }
    }
}
