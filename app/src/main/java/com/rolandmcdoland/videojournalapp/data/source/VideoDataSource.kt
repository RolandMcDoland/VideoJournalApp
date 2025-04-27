package com.rolandmcdoland.videojournalapp.data.source

import kotlinx.coroutines.flow.Flow
import videojournal.videodb.VideoEntity

interface VideoDataSource {
    fun getAllVideos(): Flow<List<VideoEntity>>

    suspend fun insertVideo(
        timestamp: Long,
        videoUri: String,
        description: String?,
        thumbnailUri: String?
    )
}