package com.dehar.player.data

import android.net.Uri

data class VideoData(
    val id: Long,
    val title: String,
    val displayName: String,
    val path: String,
    val uri: Uri,
    val duration: Long,    // in milliseconds
    val size: Long,        // in bytes
    val width: Int,
    val height: Int,
    val dateAdded: Long,   // Unix timestamp seconds
    val folderName: String = "",
    val folderPath: String = "",
    val isFavorite: Boolean = false,
    val playCount: Int = 0
    )

