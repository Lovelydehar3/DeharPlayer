package com.dehar.player.core.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Playlist types
 */
enum class PlaylistType {
    AUDIO,
    VIDEO,
    MIXED
}

/**
 * Playlist entity
 */
@Entity(tableName = "playlists")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val type: PlaylistType,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val coverArtPath: String? = null,
    val description: String = "",
    val sortOrder: Int = 0,
    val isSystemPlaylist: Boolean = false   // for "Favorites", "Recently Played", "Most Played"
) {
    /**
     * System playlist types
     */
    enum class SystemPlaylistType {
        FAVORITES,
        RECENTLY_PLAYED,
        MOST_PLAYED,
        RECENTLY_ADDED
    }
    
    companion object {
        /**
         * Create a system playlist
         */
        fun createSystemPlaylist(
            name: String,
            type: PlaylistType,
            systemType: SystemPlaylistType
        ): PlaylistEntity {
            return PlaylistEntity(
                id = when (systemType) {
                    SystemPlaylistType.FAVORITES -> -1
                    SystemPlaylistType.RECENTLY_PLAYED -> -2
                    SystemPlaylistType.MOST_PLAYED -> -3
                    SystemPlaylistType.RECENTLY_ADDED -> -4
                },
                name = name,
                type = type,
                isSystemPlaylist = true
            )
        }
    }
}

/**
 * Playlist-Song junction entity
 */
@Entity(
    tableName = "playlist_songs",
    primaryKeys = ["playlistId", "songId"],
    foreignKeys = [
        ForeignKey(
            entity = PlaylistEntity::class,
            parentColumns = ["id"],
            childColumns = ["playlistId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = SongEntity::class,
            parentColumns = ["id"],
            childColumns = ["songId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["playlistId"]), Index(value = ["songId"])]
)
data class PlaylistSongCrossRef(
    val playlistId: Long,
    val songId: Long,
    val position: Int,
    val addedAt: Long = System.currentTimeMillis()
)

/**
 * Playlist-Video junction entity
 */
@Entity(
    tableName = "playlist_videos",
    primaryKeys = ["playlistId", "videoId"],
    foreignKeys = [
        ForeignKey(
            entity = PlaylistEntity::class,
            parentColumns = ["id"],
            childColumns = ["playlistId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = VideoEntity::class,
            parentColumns = ["id"],
            childColumns = ["videoId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["playlistId"]), Index(value = ["videoId"])]
)
data class PlaylistVideoCrossRef(
    val playlistId: Long,
    val videoId: Long,
    val position: Int,
    val addedAt: Long = System.currentTimeMillis()
)