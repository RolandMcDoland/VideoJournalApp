package com.rolandmcdoland.videojournalapp.data.repository

import kotlinx.coroutines.flow.Flow
import videojournal.videodb.VideoEntity

interface VideoRepository {
    fun fetchAllVideos(): Flow<List<VideoEntity>>

    suspend fun insertVideo(
        timestamp: Long,
        videoUri: String,
        description: String?,
        thumbnailUri: String?
    )
}