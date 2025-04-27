package com.rolandmcdoland.videojournalapp

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rolandmcdoland.videojournalapp.feed.FeedScreen
import com.rolandmcdoland.videojournalapp.form.FormScreen
import java.io.File

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    var fileUri by rememberSaveable { mutableStateOf<Uri?>(null) }

    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = Feed,
        modifier = modifier
    ) {
        composable<Feed> {
            FeedScreen(
                onVideoRecorded = { navController.navigate(Form) },
                onRequestFileUri = {
                    fileUri = FileProvider.getUriForFile(
                        context,
                        context.applicationContext.packageName + ".fileprovider",
                        context.createTempVideoFile()
                    )
                    fileUri
                }
            )
        }
        composable<Form> {
            fileUri?.let { uri ->
                FormScreen(
                    videoUri = uri,
                    onVideoSaved = { navController.navigate(Feed) }
                )
            } ?: run {
                Toast
                    .makeText(
                        context,
                        context.getString(R.string.video_capture_error),
                        Toast.LENGTH_SHORT
                    )
                    .show()
                navController.navigate(Feed)
            }
        }
    }
}

fun Context.createTempVideoFile(): File {
    val directory = this.getExternalFilesDir(Environment.DIRECTORY_DCIM)
    return File.createTempFile("video_journal_", ".mp4", directory)
}