package com.rolandmcdoland.videojournalapp.data.repository

import com.rolandmcdoland.videojournalapp.data.source.VideoDataSource
import kotlinx.coroutines.flow.Flow
import videojournal.videodb.VideoEntity

class VideoRepositoryImpl(private val videoDataSource: VideoDataSource): VideoRepository {
    override fun fetchAllVideos() = videoDataSource.getAllVideos()

    override suspend fun insertVideo(
        timestamp: Long,
        videoUri: String,
        description: String?,
        thumbnailUri: String?
    ) = videoDataSource.insertVideo(
        timestamp,
        videoUri,
        description,
        thumbnailUri
    )
}