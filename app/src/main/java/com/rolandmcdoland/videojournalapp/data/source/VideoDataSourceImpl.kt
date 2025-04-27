package com.rolandmcdoland.videojournalapp.data.source

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.rolandmcdoland.videojournalapp.VideoDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import videojournal.videodb.VideoEntity

class VideoDataSourceImpl(private val db: VideoDatabase): VideoDataSource {
    override fun getAllVideos(): Flow<List<VideoEntity>> {
        return db.videoEntityQueries.getAllVideos().asFlow().mapToList(Dispatchers.IO)
    }

    override suspend fun insertVideo(
        timestamp: Long,
        videoUri: String,
        description: String?,
        thumbnailUri: String?
    ) {
        withContext(Dispatchers.IO) {
            db.videoEntityQueries.insertVideo(
                null,
                timestamp,
                videoUri,
                description,
                thumbnailUri
            )
        }
    }
}