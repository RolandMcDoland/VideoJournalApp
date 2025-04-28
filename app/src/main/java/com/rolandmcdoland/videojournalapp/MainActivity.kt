package com.rolandmcdoland.videojournalapp

import android.content.Intent
import android.net.Uri
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
                MainScreen(
                    onShareClick = { videoUri ->
                        shareVideo(videoUri)
                    }
                )
            }
        }
    }

    private fun shareVideo(videoUri: Uri) {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.setType("video/mp4")
        sharingIntent.putExtra(Intent.EXTRA_STREAM, videoUri)
        startActivity(Intent.createChooser(sharingIntent, "share:"))
    }
}