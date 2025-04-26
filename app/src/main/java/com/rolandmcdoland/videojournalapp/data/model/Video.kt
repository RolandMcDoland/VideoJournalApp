package com.rolandmcdoland.videojournalapp.data.model

data class Video(
    val id: Long,
    val videoUri: String,
    val description: String?,
    val thumbnailUri: String?
)
