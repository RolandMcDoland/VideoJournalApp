package com.rolandmcdoland.videojournalapp.form

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rolandmcdoland.videojournalapp.data.repository.VideoRepository
import kotlinx.coroutines.launch

class FormViewModel(
    private val videoRepository: VideoRepository
): ViewModel() {
    var insertState by mutableStateOf<InsertSate?>(null)
        private set

    fun insertVideo(
        videoUri: String,
        description: String?,
        thumbnailUri: String?
    ) {
        viewModelScope.launch {
            try {
                insertState = InsertSate.Loading

                videoRepository.insertVideo(
                    System.currentTimeMillis(),
                    videoUri,
                    description,
                    thumbnailUri
                )

                insertState = InsertSate.Success
            } catch(e: Exception) {
                insertState = InsertSate.Error
            }
        }
    }
}

sealed interface InsertSate {
    data object Loading: InsertSate
    data object Success: InsertSate
    data object Error: InsertSate
}