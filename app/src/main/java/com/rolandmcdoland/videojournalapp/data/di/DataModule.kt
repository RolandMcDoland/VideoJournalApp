package com.rolandmcdoland.videojournalapp.data.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.rolandmcdoland.videojournalapp.VideoDatabase
import com.rolandmcdoland.videojournalapp.data.repository.VideoRepository
import com.rolandmcdoland.videojournalapp.data.repository.VideoRepositoryImpl
import com.rolandmcdoland.videojournalapp.data.source.VideoDataSource
import com.rolandmcdoland.videojournalapp.data.source.VideoDataSourceImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {
    single<SqlDriver> {
        AndroidSqliteDriver(
            schema = VideoDatabase.Schema,
            context = androidContext(),
            name = "video,db"
        )
    }
    single<VideoDataSource> {
        VideoDataSourceImpl(VideoDatabase(get()))
    }
    single<VideoRepository> {
        VideoRepositoryImpl(get())
    }
}