
package com.dehar.player.core.data.database

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.DeleteColumn
import androidx.room.RenameColumn
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.dehar.player.core.data.model.*
import com.dehar.player.core.common.Constants

/**
 * Type converters for Room database
 */
class Converters {
    
    @TypeConverter
    fun fromPlaylistType(type: PlaylistType): String = type.name
    
    @TypeConverter
    fun toPlaylistType(value: String): PlaylistType = PlaylistType.valueOf(value)
    
    @TypeConverter
    fun fromMediaType(type: MediaType): String = type.name
    
    @TypeConverter
    fun toMediaType(value: String): MediaType = MediaType.valueOf(value)
    
    @TypeConverter
    fun fromSubtitleFormat(format: SubtitleFormat): String = format.name
    
    @TypeConverter
    fun toSubtitleFormat(value: String): SubtitleFormat = SubtitleFormat.valueOf(value)
    
    @TypeConverter
    fun fromDownloadStatus(status: DownloadStatus): String = status.name
    
    @TypeConverter
    fun toDownloadStatus(value: String): DownloadStatus = DownloadStatus.valueOf(value)
}

/**
 * Main Room database for Dehar Player
 * Contains all entities for videos, songs, playlists, and other media data
 */
@Database(
    entities = [
        // Video entities
        VideoEntity::class,
        
        // Audio entities
        SongEntity::class,
        
        // Playlist entities
        PlaylistEntity::class,
        PlaylistSongCrossRef::class,
        PlaylistVideoCrossRef::class,
        
        // Bookmark entities
        BookmarkEntity::class,
        
        // Subtitle entities
        SubtitleTrackEntity::class,
        
        // History entities
        PlaybackHistoryEntity::class,
        
        // Private vault entities
        PrivateVaultItemEntity::class,
        
        // Download entities
        DownloadEntity::class,
        
        // Network stream entities
        NetworkStreamEntity::class,
        
        // EQ preset entities
        EqPresetEntity::class,
        
        // Recycle bin entities
        RecycleBinEntity::class
    ],
    version = 1,
    exportSchema = false,
    autoMigrations = []
)
@TypeConverters(Converters::class)
abstract class DeharDatabase : RoomDatabase() {
    
    // DAOs
    abstract fun videoDao(): VideoDao
    abstract fun songDao(): SongDao
    abstract fun playlistDao(): PlaylistDao
    abstract fun bookmarkDao(): BookmarkDao
    abstract fun subtitleDao(): SubtitleDao
    abstract fun historyDao(): PlaybackHistoryDao
    abstract fun vaultDao(): PrivateVaultDao
    abstract fun downloadDao(): DownloadDao
    abstract fun streamDao(): NetworkStreamDao
    abstract fun eqPresetDao(): EqPresetDao
    abstract fun recycleBinDao(): RecycleBinDao
    
    companion object {
        @Volatile
        private var INSTANCE: DeharDatabase? = null
        
        /**
         * Get the singleton database instance
         */
        fun getDatabase(context: Context): DeharDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DeharDatabase::class.java,
                    Constants.DB_NAME
                )
                    // Enable WAL mode for better performance
                    .setJournalMode(JournalMode.WRITE_AHEAD_LOGGING)
                    
                    // Enable query logging in debug builds
                    //.addCallback(object : Callback() {
                    //    override fun onOpen(db: SupportSQLiteDatabase) {
                    //        super.onOpen(db)
                    //        if (BuildConfig.DEBUG) {
                    //            db.setVersion(1) // Force version check
                    //        }
                    //    }
                    //})
                    
                    // Fallback to destructive migration for development
                    // Remove this in production and implement proper migrations
                    .fallbackToDestructiveMigration(dropAllTables = true)
                    
                    // Allow main thread queries (not recommended, but useful for initialization)
                    //.allowMainThreadQueries()
                    
                    .build()
                
                INSTANCE = instance
                instance
            }
        }
        
        /**
         * Close the database (for testing purposes)
         */
        fun closeDatabase() {
            INSTANCE?.close()
            INSTANCE = null
        }
    }
}