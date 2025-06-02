package com.rolandmcdoland.videojournalapp.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import com.rolandmcdoland.videojournalapp.data.repository.VideoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class FeedViewModel(
    private val player: Player,
    videoRepository: VideoRepository
): ViewModel() {
    val videos = videoRepository
        .fetchAllVideos()
        .flowOn(Dispatchers.IO)
        .map {
            it.sortedByDescending {
                video -> video.timestamp
            }
        }

    fun requestPlayer(videoUri: String): Player {
        player.stop()
        viewModelScope.launch {
            playVideo(videoUri)
        }
        return player
    }

    private fun playVideo(videoUri: String) {
        player.setMediaItem(MediaItem.fromUri(videoUri))
        player.prepare()
        player.play()
    }

    override fun onCleared() {
        super.onCleared()
        player.release()
    }
}