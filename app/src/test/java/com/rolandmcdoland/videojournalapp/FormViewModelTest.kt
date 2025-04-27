package com.rolandmcdoland.videojournalapp

import com.rolandmcdoland.videojournalapp.data.FakeVideoRepository
import com.rolandmcdoland.videojournalapp.form.FormViewModel
import com.rolandmcdoland.videojournalapp.form.InsertSate
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import videojournal.videodb.VideoEntity

class FormViewModelTest {
    private lateinit var viewModel: FormViewModel

    private lateinit var fakeVideoRepository: FakeVideoRepository

    @Before
    fun setUp() {
        fakeVideoRepository = FakeVideoRepository()
        viewModel = FormViewModel(fakeVideoRepository)

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
    fun formViewModel_VideoInsertionSuccess_InsertStateSetToSuccess() {
        runBlocking {
            viewModel.insertVideo(
                videoUri = "test",
                description = "",
                thumbnailUri = null
            )
        }

        assert(viewModel.insertState is InsertSate.Success)

        val videos = runBlocking { fakeVideoRepository.fetchAllVideos().first() }
        assert(videos.any { it.videoUri == "test" })
    }

    @Test
    fun formViewModel_VideoInsertionError_InsertStateSetToError() {
        fakeVideoRepository.shouldThrowException = true

        runBlocking {
            viewModel.insertVideo(
                videoUri = "",
                description = "",
                thumbnailUri = null
            )
        }

        assert(viewModel.insertState is InsertSate.Error)
    }

    @Test
    fun formViewModel_VideoInsertBlankDescription_DescriptionIsNull() {
        runBlocking {
            viewModel.insertVideo(
                videoUri = "test",
                description = "",
                thumbnailUri = null
            )
        }

        val videos = runBlocking { fakeVideoRepository.fetchAllVideos().first() }
        val video = videos.first { it.videoUri == "test" }
        assert(video.description == null)
    }
}