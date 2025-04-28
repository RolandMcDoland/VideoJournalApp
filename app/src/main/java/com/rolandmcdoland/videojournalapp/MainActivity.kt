package com.rolandmcdoland.videojournalapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.rolandmcdoland.videojournalapp.ui.theme.VideoJournalAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VideoJournalAppTheme(dynamicColor = false) {
                MainScreen()
            }
        }
    }
}