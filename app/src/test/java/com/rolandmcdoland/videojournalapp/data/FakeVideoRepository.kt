package com.rolandmcdoland.videojournalapp.data

import com.rolandmcdoland.videojournalapp.data.repository.VideoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import videojournal.videodb.VideoEntity

class FakeVideoRepository: VideoRepository {
    private val videos = mutableListOf<VideoEntity>()

    var shouldThrowException = false

    override fun fetchAllVideos(): Flow<List<VideoEntity>> =
        flow { emit(videos) }

    override suspend fun insertVideo(
        timestamp: Long,
        videoUri: String,
        description: String?,
        thumbnailUri: String?
    ) {
        if(shouldThrowException)
            throw Exception()

        videos.add(
            VideoEntity(
                videos.size.toLong(),
                timestamp,
                videoUri,
                description,
                thumbnailUri
            )
        )
    }
}