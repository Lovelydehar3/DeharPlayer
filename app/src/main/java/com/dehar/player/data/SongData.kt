package com.dehar.player.data

data class SongData(
    val id: Long,
    val uri: String,
    val title: String,
    val artist: String,
    val album: String,
    val duration: Long,
    val size: Long,
    val path: String,
    val dateAdded: Long,
    val trackNumber: Int = 0,
    val isFavorite: Boolean = false
) {
    val displayName: String
        get() = title.ifBlank { "Unknown Song" }
}
