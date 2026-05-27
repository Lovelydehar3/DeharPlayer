package com.dehar.player.core.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Audio/Song entity — cached metadata from ContentResolver
 */
@Entity(
    tableName = "songs",
    indices = [
        Index(value = ["albumId"]),
        Index(value = ["artistId"]),
        Index(value = ["album"]),
        Index(value = ["artist"]),
        Index(value = ["genre"]),
        Index(value = ["dateAdded"]),
        Index(value = ["lastPlayedAt"]),
        Index(value = ["isFavorite"])
    ]
)
data class SongEntity(
    @PrimaryKey val id: Long,
    val title: String,
    val artist: String,
    val album: String,
    val albumArtist: String,
    val genre: String,
    val path: String,
    val size: Long,
    val duration: Long,
    val trackNumber: Int,
    val discNumber: Int,
    val year: Int,
    val dateAdded: Long,
    val dateModified: Long,
    val albumId: Long,
    val artistId: Long,
    val bitrate: Long,
    val sampleRate: Int,
    val mimeType: String,
    val lastPlayedAt: Long = 0L,
    val lastPlayedPosition: Long = 0L,
    val playCount: Int = 0,
    val isFavorite: Boolean = false,
    val lyricsPath: String? = null,   // local .lrc file path
    val embeddedLyrics: String? = null
) {
    /**
     * Get formatted duration string
     */
    fun getFormattedDuration(): String {
        val totalSeconds = duration / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        
        return String.format("%d:%02d", minutes, seconds)
    }
    
    /**
     * Get display title (falls back to filename if title is missing)
     */
    fun getDisplayTitle(): String {
        return if (title.isNotBlank() && title != "<unknown title>") {
            title
        } else {
            path.substringAfterLast('/').substringAfterLast('\\')
        }
    }
    
    /**
     * Get display artist (falls back to "Unknown Artist" if missing)
     */
    fun getDisplayArtist(): String {
        return if (artist.isNotBlank() && artist != "<unknown>") {
            artist
        } else {
            "Unknown Artist"
        }
    }
    
    /**
     * Get display album (falls back to "Unknown Album" if missing)
     */
    fun getDisplayAlbum(): String {
        return if (album.isNotBlank() && album != "<unknown>") {
            album
        } else {
            "Unknown Album"
        }
    }
    
    /**
     * Get formatted file size
     */
    fun getFormattedSize(): String {
        val kb = size / 1024.0
        return if (kb > 1024) {
            String.format("%.1f MB", kb / 1024)
        } else {
            String.format("%.0f KB", kb)
        }
    }
    
    /**
     * Check if song has lyrics
     */
    fun hasLyrics(): Boolean = !lyricsPath.isNullOrEmpty() || !embeddedLyrics.isNullOrEmpty()
    
    /**
     * Get playback progress percentage
     */
    fun getPlaybackProgress(): Float {
        return if (duration > 0 && lastPlayedPosition > 0) {
            (lastPlayedPosition.toFloat() / duration.toFloat()).coerceIn(0f, 1f)
        } else {
            0f
        }
    }
}