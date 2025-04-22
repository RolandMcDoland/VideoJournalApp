package com.rolandmcdoland.videojournalapp

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class VideoJournalApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@VideoJournalApplication)
            modules()
        }
    }
}