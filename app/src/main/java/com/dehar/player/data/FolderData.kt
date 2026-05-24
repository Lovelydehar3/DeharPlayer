package com.dehar.player.data

data class FolderData(
    val name: String,
    val path: String,
    val videos: List<VideoData>
) {
    val videoCount: Int get() = videos.size
    val firstVideo: VideoData? get() = videos.firstOrNull()
    val totalSize: Long get() = videos.sumOf { it.size }
}
