package com.rolandmcdoland.videojournalapp.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import com.rolandmcdoland.videojournalapp.data.model.Video
import kotlinx.coroutines.launch

class FeedViewModel(private val player: Player): ViewModel() {
    var videos = listOf(
        Video(0L, "https://storage.googleapis.com/exoplayer-test-media-0/shortform_2.mp4", "Video 1", "https://cross-play.pl/wp-content/uploads/2024/02/Cyberpunk-2077-Ultimate-Edition-wallpaper.jpg"),
        Video(1L, "https://storage.googleapis.com/exoplayer-test-media-0/shortform_3.mp4", "Video 2", "https://cross-play.pl/wp-content/uploads/2024/02/Cyberpunk-2077-Ultimate-Edition-wallpaper.jpg"),
        Video(2L, "https://html5demos.com/assets/dizzy.mp4", "Video 3", "https://cross-play.pl/wp-content/uploads/2024/02/Cyberpunk-2077-Ultimate-Edition-wallpaper.jpg")
    )

    fun requestPlayer(videoId: Long): Player {
        player.stop()
        viewModelScope.launch {
            playVideo(videoId)
        }
        return player
    }

    private fun playVideo(videoId: Long) {
        val videoToPlay = videos.find { it.id == videoId }
        videoToPlay?.let { video ->
            player.setMediaItem(MediaItem.fromUri(video.videoUri))
            player.prepare()
            player.play()
        }
    }

    override fun onCleared() {
        super.onCleared()
        player.release()
    }
}