package com.rolandmcdoland.videojournalapp

import androidx.media3.common.Player
import com.rolandmcdoland.videojournalapp.data.FakeVideoRepository
import com.rolandmcdoland.videojournalapp.feed.FeedViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import videojournal.videodb.VideoEntity

@RunWith(MockitoJUnitRunner::class)
class FeedViewModelTest {
    private lateinit var viewModel: FeedViewModel

    private lateinit var fakeVideoRepository: FakeVideoRepository

    @Mock
    private lateinit var mockPlayer: Player

    @Before
    fun setUp() {
        fakeVideoRepository = FakeVideoRepository()
        viewModel = FeedViewModel(mockPlayer, fakeVideoRepository)

        val videosToInsert = mutableListOf<VideoEntity>()
        (1..26).forEach {
            videosToInsert.add(
                VideoEntity(
                    id = it.toLong(),
                    timestamp = it.toLong(),
                    videoUri = "",
                    description = null,
                    thumbnailUri = null
                )
            )
        }
        videosToInsert.shuffle()

        runBlocking {
            videosToInsert.forEach {
                fakeVideoRepository.insertVideo(
                    timestamp = it.timestamp,
                    videoUri = it.videoUri,
                    description = it.description,
                    thumbnailUri = it.thumbnailUri
                )
            }
        }
    }

    @Test
    fun feedViewModel_VideosSortedByTimestampDesc() {
        val videos = runBlocking {
            viewModel.videos.first()
        }
        for(i in 0..videos.size - 2) {
            assert(videos[i].timestamp > videos[i + 1].timestamp)
        }
    }
}