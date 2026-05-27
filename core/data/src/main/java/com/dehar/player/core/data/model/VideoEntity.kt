package com.dehar.player.core.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Video entity — cached metadata from ContentResolver
 */
@Entity(
    tableName = "videos",
    indices = [
        Index(value = ["bucketId"]),
        Index(value = ["dateAdded"]),
        Index(value = ["lastPlayedAt"]),
        Index(value = ["isFavorite"]),
        Index(value = ["isHidden"])
    ]
)
data class VideoEntity(
    @PrimaryKey val id: Long,
    val title: String,
    val displayName: String,
    val path: String,
    val size: Long,
    val duration: Long,             // milliseconds
    val width: Int,
    val height: Int,
    val mimeType: String,
    val dateAdded: Long,
    val dateModified: Long,
    val bucketId: Long,             // folder ID
    val bucketName: String,         // folder name
    val resolution: String,         // "1920x1080"
    val frameRate: Int,
    val bitrate: Long,
    val codecName: String,          // "H.265 / HEVC"
    val lastPlayedAt: Long = 0L,
    val lastPlayedPosition: Long = 0L,   // ms — for resume
    val playCount: Int = 0,
    val isFavorite: Boolean = false,
    val userRating: Int = 0,        // 0-5 stars
    val customThumbnailPath: String? = null,
    val isHidden: Boolean = false,
    val privateVaultId: String? = null
) {
    /**
     * Get resolution label (4K, FHD, HD, SD)
     */
    fun getResolutionLabel(): String {
        return when {
            width >= 3840 -> "4K"
            width >= 1920 -> "FHD"
            width >= 1280 -> "HD"
            width >= 720 -> "SD"
            else -> "LD"
        }
    }
    
    /**
     * Get formatted duration string
     */
    fun getFormattedDuration(): String {
        val totalSeconds = duration / 1000
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60
        
        return if (hours > 0) {
            String.format("%d:%02d:%02d", hours, minutes, seconds)
        } else {
            String.format("%d:%02d", minutes, seconds)
        }
    }
    
    /**
     * Check if video is 4K
     */
    fun is4K(): Boolean = width >= 3840
    
    /**
     * Check if video is HD (720p or higher)
     */
    fun isHD(): Boolean = width >= 1280
    
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
    
    /**
     * Check if video is partially watched
     */
    fun isPartiallyWatched(): Boolean {
        val progress = getPlaybackProgress()
        return progress in 0.02f..0.98f
    }
}