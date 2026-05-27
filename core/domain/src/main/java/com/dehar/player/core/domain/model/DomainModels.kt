package com.dehar.player.core.domain.model

import androidx.compose.runtime.Stable

@Stable
data class SongItem(
    val id: Long,
    val title: String,
    val artist: String,
    val album: String,
    val path: String,
    val duration: Long,
    val trackNumber: Int = 0,
    val discNumber: Int = 0,
    val year: Int = 0,
    val bitrate: Long = 0,
    val sampleRate: Int = 0,
    val mimeType: String = "audio/mpeg",
    val playCount: Int = 0,
    val isFavorite: Boolean = false,
    val lyricsPath: String? = null,
    val embeddedLyrics: String? = null
)

@Stable
data class VideoItem(
    val id: Long,
    val title: String,
    val path: String,
    val duration: Long,
    val size: Long,
    val thumbnailPath: String? = null,
    val resolution: String = "unknown",
    val lastPlayedPosition: Long = 0,
    val playCount: Int = 0,
    val isFavorite: Boolean = false
)

@Stable
data class PlaylistItem(
    val id: Long,
    val name: String,
    val songCount: Int = 0,
    val totalDuration: Long = 0,
    val createdAt: Long = System.currentTimeMillis(),
    val songs: List<SongItem> = emptyList()
)

@Stable
data class AlbumItem(
    val id: Long,
    val name: String,
    val artist: String,
    val songCount: Int = 0,
    val year: Int = 0,
    val artPath: String? = null,
    val genre: String? = null
)

@Stable
data class ArtistItem(
    val id: Long,
    val name: String,
    val songCount: Int = 0,
    val albumCount: Int = 0,
    val artPath: String? = null
)

@Stable
data class GenreItem(
    val id: Long,
    val name: String,
    val songCount: Int = 0,
    val artPath: String? = null
)

// Bookmark for saving specific timestamps
@Stable
data class BookmarkItem(
    val id: Long,
    val mediaPath: String,
    val positionMs: Long,
    val title: String? = null,
    val notes: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)

// Playback history entry
@Stable
data class PlaybackHistoryItem(
    val id: Long,
    val mediaPath: String,
    val mediaType: String, // "VIDEO" or "AUDIO"
    val lastPlayedAt: Long = System.currentTimeMillis(),
    val playCount: Int = 1,
    val lastPositionMs: Long = 0
)

// Download entry
@Stable
data class DownloadItem(
    val id: Long,
    val url: String,
    val localPath: String,
    val status: DownloadStatus = DownloadStatus.PENDING,
    val progress: Int = 0,
    val totalSize: Long = 0,
    val downloadedSize: Long = 0
)

enum class DownloadStatus {
    PENDING, IN_PROGRESS, COMPLETED, PAUSED, FAILED, CANCELLED
}

// Network stream item
@Stable
data class NetworkStreamItem(
    val id: Long,
    val title: String,
    val url: String,
    val type: String, // "HTTP", "RTSP", "HLS", "DASH"
    val lastAccessedAt: Long = System.currentTimeMillis()
)

// EQ Preset
@Stable
data class EqPresetItem(
    val id: Long,
    val name: String,
    val bandValues: List<Int> = listOf(0, 0, 0, 0, 0),
    val isActive: Boolean = false,
    val isSystem: Boolean = false
)

// Subtitle track
@Stable
data class SubtitleTrackItem(
    val id: Long,
    val videoPath: String,
    val subtitlePath: String? = null,
    val language: String = "en",
    val format: String = "srt",
    val isEmbedded: Boolean = false,
    val index: Int = 0
)
