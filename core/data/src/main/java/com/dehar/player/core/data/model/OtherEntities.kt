package com.dehar.player.core.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Media types
 */
enum class MediaType {
    VIDEO,
    AUDIO
}

/**
 * Bookmark entity for saving playback positions
 */
@Entity(
    tableName = "bookmarks",
    indices = [Index(value = ["mediaId", "mediaType"])]
)
data class BookmarkEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val mediaId: Long,
    val mediaType: MediaType,
    val positionMs: Long,
    val label: String,
    val createdAt: Long = System.currentTimeMillis(),
    val thumbnailPath: String? = null
)

/**
 * Subtitle format types
 */
enum class SubtitleFormat {
    SRT,
    SSA,
    ASS,
    SAMI,
    MICRODVD,
    SUBVIEWER2,
    MPL2,
    TMPLAYER,
    PJS,
    POWERDIVX,
    VTT,
    BITMAP_PGS
}

/**
 * Subtitle tracks cache entity
 */
@Entity(
    tableName = "subtitle_tracks",
    indices = [Index(value = ["videoId"])]
)
data class SubtitleTrackEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val videoId: Long,
    val language: String,
    val label: String,
    val path: String,         // local path or URL
    val format: SubtitleFormat,
    val encoding: String = "UTF-8",
    val isDefault: Boolean = false,
    val offsetMs: Long = 0L
)

/**
 * Playback history entity
 */
@Entity(
    tableName = "playback_history",
    indices = [
        Index(value = ["mediaId", "mediaType"]),
        Index(value = ["playedAt"])
    ]
)
data class PlaybackHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val mediaId: Long,
    val mediaType: MediaType,
    val playedAt: Long = System.currentTimeMillis(),
    val durationPlayedMs: Long,
    val completionPercent: Float
)

/**
 * Private vault entry entity
 */
@Entity(tableName = "private_vault")
data class PrivateVaultItemEntity(
    @PrimaryKey val id: String,   // UUID
    val originalPath: String,
    val encryptedPath: String,
    val mediaType: MediaType,
    val addedAt: Long = System.currentTimeMillis(),
    val thumbnailPath: String? = null,
    val title: String,
    val duration: Long
)

/**
 * Download status enum
 */
enum class DownloadStatus {
    QUEUED,
    DOWNLOADING,
    PAUSED,
    COMPLETED,
    FAILED
}

/**
 * Download queue entity
 */
@Entity(tableName = "downloads")
data class DownloadEntity(
    @PrimaryKey val id: String,  // UUID
    val url: String,
    val title: String,
    val destinationPath: String,
    val totalBytes: Long = 0L,
    val downloadedBytes: Long = 0L,
    val status: DownloadStatus,
    val mediaType: MediaType,
    val startedAt: Long = System.currentTimeMillis(),
    val completedAt: Long? = null,
    val errorMessage: String? = null
) {
    /**
     * Get download progress percentage
     */
    fun getProgress(): Float {
        return if (totalBytes > 0) {
            (downloadedBytes.toFloat() / totalBytes.toFloat()).coerceIn(0f, 1f)
        } else {
            0f
        }
    }
    
    /**
     * Check if download is complete
     */
    fun isComplete(): Boolean = status == DownloadStatus.COMPLETED
    
    /**
     * Check if download is in progress
     */
    fun isInProgress(): Boolean = status == DownloadStatus.DOWNLOADING
}

/**
 * Network streams (saved URLs) entity
 */
@Entity(tableName = "network_streams")
data class NetworkStreamEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val url: String,
    val title: String,
    val addedAt: Long = System.currentTimeMillis(),
    val lastPlayedAt: Long? = null,
    val mediaType: MediaType,
    val headers: String = "{}"   // JSON map of custom headers
)

/**
 * EQ preset entity
 */
@Entity(tableName = "eq_presets")
data class EqPresetEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val bands: String,   // JSON: [60Hz, 230Hz, 910Hz, 3kHz, 14kHz] values -15..+15 dB
    val bassBoost: Int = 0,
    val virtualizer: Int = 0,
    val loudnessEnhancer: Int = 0,
    val isSystem: Boolean = false,
    val isActive: Boolean = false
) {
    companion object {
        /**
         * Create a flat/default EQ preset
         */
        fun createFlatPreset(name: String = "Flat"): EqPresetEntity {
            return EqPresetEntity(
                name = name,
                bands = "[0,0,0,0,0]",
                isSystem = true
            )
        }
        
        /**
         * Create a bass boost preset
         */
        fun createBassBoostPreset(strength: Int = 8): EqPresetEntity {
            return EqPresetEntity(
                name = "Bass Boost",
                bands = "[${strength},${strength/2},0,0,0]",
                bassBoost = strength * 100,
                isSystem = true
            )
        }
        
        /**
         * Create a vocal boost preset
         */
        fun createVocalBoostPreset(): EqPresetEntity {
            return EqPresetEntity(
                name = "Vocal Boost",
                bands = "[-2,2,6,4,0]",
                isSystem = true
            )
        }
    }
}

/**
 * Recycle bin entity for deleted files
 */
@Entity(
    tableName = "recycle_bin",
    indices = [Index(value = ["deletedAt"])]
)
data class RecycleBinEntity(
    @PrimaryKey val id: String,  // UUID
    val originalPath: String,
    val originalName: String,
    val mediaType: MediaType,
    val size: Long,
    val deletedAt: Long = System.currentTimeMillis(),
    val thumbnailPath: String? = null,
    val restorePath: String? = null  // Where to restore to (null = original location)
) {
    /**
     * Check if item should be auto-purged based on retention days
     */
    fun shouldAutoPurge(retentionDays: Int): Boolean {
        val purgeTime = retentionDays * 24 * 60 * 60 * 1000L
        return (System.currentTimeMillis() - deletedAt) > purgeTime
    }
}