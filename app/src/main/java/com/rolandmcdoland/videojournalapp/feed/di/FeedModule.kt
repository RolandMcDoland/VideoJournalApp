package com.rolandmcdoland.videojournalapp.feed.di

import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import com.rolandmcdoland.videojournalapp.feed.FeedViewModel
import org.koin.android.ext.koin.androidContext


val feedModule = module {
    single<Player> { ExoPlayer.Builder(androidContext()).build() }
    viewModelOf(::FeedViewModel)
}