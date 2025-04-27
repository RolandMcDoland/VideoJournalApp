package com.rolandmcdoland.videojournalapp

import android.app.Application
import com.rolandmcdoland.videojournalapp.data.di.dataModule
import com.rolandmcdoland.videojournalapp.feed.di.feedModule
import com.rolandmcdoland.videojournalapp.form.di.formModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class VideoJournalApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@VideoJournalApplication)
            modules(feedModule, dataModule, formModule)
        }
    }
}