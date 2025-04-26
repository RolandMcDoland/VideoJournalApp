package com.rolandmcdoland.videojournalapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rolandmcdoland.videojournalapp.feed.FeedScreen

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Feed,
        modifier = modifier
    ) {
        composable<Feed> {
            FeedScreen()
        }
    }
}