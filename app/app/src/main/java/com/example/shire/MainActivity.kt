package com.example.shire

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.shire.ui.theme.ShireTheme
import com.example.shire.ui.screens.HomeScreenHotels

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShireTheme {
                Dashboard()
            }
        }
    }
}

@Composable
fun Dashboard() {
    HomeScreenHotels()
}

@Preview(showBackground = true)
@Composable
fun ViewDashboard(){
    Dashboard()
}