package com.dehar.player.core.`data`.database

import androidx.room.InvalidationTracker
import androidx.room.RoomOpenDelegate
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.room.util.TableInfo
import androidx.room.util.TableInfo.Companion.read
import androidx.room.util.dropFtsSyncTriggers
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import javax.`annotation`.processing.Generated
import kotlin.Lazy
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.MutableList
import kotlin.collections.MutableMap
import kotlin.collections.MutableSet
import kotlin.collections.Set
import kotlin.collections.mutableListOf
import kotlin.collections.mutableMapOf
import kotlin.collections.mutableSetOf
import kotlin.reflect.KClass

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class DeharDatabase_Impl : DeharDatabase() {
  private val _videoDao: Lazy<VideoDao> = lazy {
    VideoDao_Impl(this)
  }

  private val _songDao: Lazy<SongDao> = lazy {
    SongDao_Impl(this)
  }

  private val _playlistDao: Lazy<PlaylistDao> = lazy {
    PlaylistDao_Impl(this)
  }

  private val _bookmarkDao: Lazy<BookmarkDao> = lazy {
    BookmarkDao_Impl(this)
  }

  private val _subtitleDao: Lazy<SubtitleDao> = lazy {
    SubtitleDao_Impl(this)
  }

  private val _playbackHistoryDao: Lazy<PlaybackHistoryDao> = lazy {
    PlaybackHistoryDao_Impl(this)
  }

  private val _privateVaultDao: Lazy<PrivateVaultDao> = lazy {
    PrivateVaultDao_Impl(this)
  }

  private val _downloadDao: Lazy<DownloadDao> = lazy {
    DownloadDao_Impl(this)
  }

  private val _networkStreamDao: Lazy<NetworkStreamDao> = lazy {
    NetworkStreamDao_Impl(this)
  }

  private val _eqPresetDao: Lazy<EqPresetDao> = lazy {
    EqPresetDao_Impl(this)
  }

  private val _recycleBinDao: Lazy<RecycleBinDao> = lazy {
    RecycleBinDao_Impl(this)
  }

  protected override fun createOpenDelegate(): RoomOpenDelegate {
    val _openDelegate: RoomOpenDelegate = object : RoomOpenDelegate(1,
        "4a1264776e5ae1b46331c9cd2d780a7d", "9abe2e5d35842a6a8894d0f9a33540f6") {
      public override fun createAllTables(connection: SQLiteConnection) {
        connection.execSQL("CREATE TABLE IF NOT EXISTS `videos` (`id` INTEGER NOT NULL, `title` TEXT NOT NULL, `displayName` TEXT NOT NULL, `path` TEXT NOT NULL, `size` INTEGER NOT NULL, `duration` INTEGER NOT NULL, `width` INTEGER NOT NULL, `height` INTEGER NOT NULL, `mimeType` TEXT NOT NULL, `dateAdded` INTEGER NOT NULL, `dateModified` INTEGER NOT NULL, `bucketId` INTEGER NOT NULL, `bucketName` TEXT NOT NULL, `resolution` TEXT NOT NULL, `frameRate` INTEGER NOT NULL, `bitrate` INTEGER NOT NULL, `codecName` TEXT NOT NULL, `lastPlayedAt` INTEGER NOT NULL, `lastPlayedPosition` INTEGER NOT NULL, `playCount` INTEGER NOT NULL, `isFavorite` INTEGER NOT NULL, `userRating` INTEGER NOT NULL, `customThumbnailPath` TEXT, `isHidden` INTEGER NOT NULL, `privateVaultId` TEXT, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_videos_bucketId` ON `videos` (`bucketId`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_videos_dateAdded` ON `videos` (`dateAdded`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_videos_lastPlayedAt` ON `videos` (`lastPlayedAt`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_videos_isFavorite` ON `videos` (`isFavorite`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_videos_isHidden` ON `videos` (`isHidden`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `songs` (`id` INTEGER NOT NULL, `title` TEXT NOT NULL, `artist` TEXT NOT NULL, `album` TEXT NOT NULL, `albumArtist` TEXT NOT NULL, `genre` TEXT NOT NULL, `path` TEXT NOT NULL, `size` INTEGER NOT NULL, `duration` INTEGER NOT NULL, `trackNumber` INTEGER NOT NULL, `discNumber` INTEGER NOT NULL, `year` INTEGER NOT NULL, `dateAdded` INTEGER NOT NULL, `dateModified` INTEGER NOT NULL, `albumId` INTEGER NOT NULL, `artistId` INTEGER NOT NULL, `bitrate` INTEGER NOT NULL, `sampleRate` INTEGER NOT NULL, `mimeType` TEXT NOT NULL, `lastPlayedAt` INTEGER NOT NULL, `lastPlayedPosition` INTEGER NOT NULL, `playCount` INTEGER NOT NULL, `isFavorite` INTEGER NOT NULL, `lyricsPath` TEXT, `embeddedLyrics` TEXT, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_songs_albumId` ON `songs` (`albumId`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_songs_artistId` ON `songs` (`artistId`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_songs_album` ON `songs` (`album`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_songs_artist` ON `songs` (`artist`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_songs_genre` ON `songs` (`genre`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_songs_dateAdded` ON `songs` (`dateAdded`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_songs_lastPlayedAt` ON `songs` (`lastPlayedAt`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_songs_isFavorite` ON `songs` (`isFavorite`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `playlists` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `type` TEXT NOT NULL, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL, `coverArtPath` TEXT, `description` TEXT NOT NULL, `sortOrder` INTEGER NOT NULL, `isSystemPlaylist` INTEGER NOT NULL)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `playlist_songs` (`playlistId` INTEGER NOT NULL, `songId` INTEGER NOT NULL, `position` INTEGER NOT NULL, `addedAt` INTEGER NOT NULL, PRIMARY KEY(`playlistId`, `songId`), FOREIGN KEY(`playlistId`) REFERENCES `playlists`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`songId`) REFERENCES `songs`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_playlist_songs_playlistId` ON `playlist_songs` (`playlistId`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_playlist_songs_songId` ON `playlist_songs` (`songId`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `playlist_videos` (`playlistId` INTEGER NOT NULL, `videoId` INTEGER NOT NULL, `position` INTEGER NOT NULL, `addedAt` INTEGER NOT NULL, PRIMARY KEY(`playlistId`, `videoId`), FOREIGN KEY(`playlistId`) REFERENCES `playlists`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`videoId`) REFERENCES `videos`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_playlist_videos_playlistId` ON `playlist_videos` (`playlistId`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_playlist_videos_videoId` ON `playlist_videos` (`videoId`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `bookmarks` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `mediaId` INTEGER NOT NULL, `mediaType` TEXT NOT NULL, `positionMs` INTEGER NOT NULL, `label` TEXT NOT NULL, `createdAt` INTEGER NOT NULL, `thumbnailPath` TEXT)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_bookmarks_mediaId_mediaType` ON `bookmarks` (`mediaId`, `mediaType`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `subtitle_tracks` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `videoId` INTEGER NOT NULL, `language` TEXT NOT NULL, `label` TEXT NOT NULL, `path` TEXT NOT NULL, `format` TEXT NOT NULL, `encoding` TEXT NOT NULL, `isDefault` INTEGER NOT NULL, `offsetMs` INTEGER NOT NULL)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_subtitle_tracks_videoId` ON `subtitle_tracks` (`videoId`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `playback_history` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `mediaId` INTEGER NOT NULL, `mediaType` TEXT NOT NULL, `playedAt` INTEGER NOT NULL, `durationPlayedMs` INTEGER NOT NULL, `completionPercent` REAL NOT NULL)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_playback_history_mediaId_mediaType` ON `playback_history` (`mediaId`, `mediaType`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_playback_history_playedAt` ON `playback_history` (`playedAt`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `private_vault` (`id` TEXT NOT NULL, `originalPath` TEXT NOT NULL, `encryptedPath` TEXT NOT NULL, `mediaType` TEXT NOT NULL, `addedAt` INTEGER NOT NULL, `thumbnailPath` TEXT, `title` TEXT NOT NULL, `duration` INTEGER NOT NULL, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `downloads` (`id` TEXT NOT NULL, `url` TEXT NOT NULL, `title` TEXT NOT NULL, `destinationPath` TEXT NOT NULL, `totalBytes` INTEGER NOT NULL, `downloadedBytes` INTEGER NOT NULL, `status` TEXT NOT NULL, `mediaType` TEXT NOT NULL, `startedAt` INTEGER NOT NULL, `completedAt` INTEGER, `errorMessage` TEXT, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `network_streams` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `url` TEXT NOT NULL, `title` TEXT NOT NULL, `addedAt` INTEGER NOT NULL, `lastPlayedAt` INTEGER, `mediaType` TEXT NOT NULL, `headers` TEXT NOT NULL)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `eq_presets` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `bands` TEXT NOT NULL, `bassBoost` INTEGER NOT NULL, `virtualizer` INTEGER NOT NULL, `loudnessEnhancer` INTEGER NOT NULL, `isSystem` INTEGER NOT NULL, `isActive` INTEGER NOT NULL)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `recycle_bin` (`id` TEXT NOT NULL, `originalPath` TEXT NOT NULL, `originalName` TEXT NOT NULL, `mediaType` TEXT NOT NULL, `size` INTEGER NOT NULL, `deletedAt` INTEGER NOT NULL, `thumbnailPath` TEXT, `restorePath` TEXT, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_recycle_bin_deletedAt` ON `recycle_bin` (`deletedAt`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)")
        connection.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '4a1264776e5ae1b46331c9cd2d780a7d')")
      }

      public override fun dropAllTables(connection: SQLiteConnection) {
        connection.execSQL("DROP TABLE IF EXISTS `videos`")
        connection.execSQL("DROP TABLE IF EXISTS `songs`")
        connection.execSQL("DROP TABLE IF EXISTS `playlists`")
        connection.execSQL("DROP TABLE IF EXISTS `playlist_songs`")
        connection.execSQL("DROP TABLE IF EXISTS `playlist_videos`")
        connection.execSQL("DROP TABLE IF EXISTS `bookmarks`")
        connection.execSQL("DROP TABLE IF EXISTS `subtitle_tracks`")
        connection.execSQL("DROP TABLE IF EXISTS `playback_history`")
        connection.execSQL("DROP TABLE IF EXISTS `private_vault`")
        connection.execSQL("DROP TABLE IF EXISTS `downloads`")
        connection.execSQL("DROP TABLE IF EXISTS `network_streams`")
        connection.execSQL("DROP TABLE IF EXISTS `eq_presets`")
        connection.execSQL("DROP TABLE IF EXISTS `recycle_bin`")
      }

      public override fun onCreate(connection: SQLiteConnection) {
      }

      public override fun onOpen(connection: SQLiteConnection) {
        connection.execSQL("PRAGMA foreign_keys = ON")
        internalInitInvalidationTracker(connection)
      }

      public override fun onPreMigrate(connection: SQLiteConnection) {
        dropFtsSyncTriggers(connection)
      }

      public override fun onPostMigrate(connection: SQLiteConnection) {
      }

      public override fun onValidateSchema(connection: SQLiteConnection):
          RoomOpenDelegate.ValidationResult {
        val _columnsVideos: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsVideos.put("id", TableInfo.Column("id", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsVideos.put("title", TableInfo.Column("title", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsVideos.put("displayName", TableInfo.Column("displayName", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsVideos.put("path", TableInfo.Column("path", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsVideos.put("size", TableInfo.Column("size", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsVideos.put("duration", TableInfo.Column("duration", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsVideos.put("width", TableInfo.Column("width", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsVideos.put("height", TableInfo.Column("height", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsVideos.put("mimeType", TableInfo.Column("mimeType", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsVideos.put("dateAdded", TableInfo.Column("dateAdded", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsVideos.put("dateModified", TableInfo.Column("dateModified", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsVideos.put("bucketId", TableInfo.Column("bucketId", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsVideos.put("bucketName", TableInfo.Column("bucketName", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsVideos.put("resolution", TableInfo.Column("resolution", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsVideos.put("frameRate", TableInfo.Column("frameRate", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsVideos.put("bitrate", TableInfo.Column("bitrate", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsVideos.put("codecName", TableInfo.Column("codecName", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsVideos.put("lastPlayedAt", TableInfo.Column("lastPlayedAt", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsVideos.put("lastPlayedPosition", TableInfo.Column("lastPlayedPosition", "INTEGER",
            true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsVideos.put("playCount", TableInfo.Column("playCount", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsVideos.put("isFavorite", TableInfo.Column("isFavorite", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsVideos.put("userRating", TableInfo.Column("userRating", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsVideos.put("customThumbnailPath", TableInfo.Column("customThumbnailPath", "TEXT",
            false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsVideos.put("isHidden", TableInfo.Column("isHidden", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsVideos.put("privateVaultId", TableInfo.Column("privateVaultId", "TEXT", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysVideos: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesVideos: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesVideos.add(TableInfo.Index("index_videos_bucketId", false, listOf("bucketId"),
            listOf("ASC")))
        _indicesVideos.add(TableInfo.Index("index_videos_dateAdded", false, listOf("dateAdded"),
            listOf("ASC")))
        _indicesVideos.add(TableInfo.Index("index_videos_lastPlayedAt", false,
            listOf("lastPlayedAt"), listOf("ASC")))
        _indicesVideos.add(TableInfo.Index("index_videos_isFavorite", false, listOf("isFavorite"),
            listOf("ASC")))
        _indicesVideos.add(TableInfo.Index("index_videos_isHidden", false, listOf("isHidden"),
            listOf("ASC")))
        val _infoVideos: TableInfo = TableInfo("videos", _columnsVideos, _foreignKeysVideos,
            _indicesVideos)
        val _existingVideos: TableInfo = read(connection, "videos")
        if (!_infoVideos.equals(_existingVideos)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |videos(com.dehar.player.core.data.model.VideoEntity).
              | Expected:
              |""".trimMargin() + _infoVideos + """
              |
              | Found:
              |""".trimMargin() + _existingVideos)
        }
        val _columnsSongs: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsSongs.put("id", TableInfo.Column("id", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSongs.put("title", TableInfo.Column("title", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSongs.put("artist", TableInfo.Column("artist", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSongs.put("album", TableInfo.Column("album", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSongs.put("albumArtist", TableInfo.Column("albumArtist", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSongs.put("genre", TableInfo.Column("genre", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSongs.put("path", TableInfo.Column("path", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSongs.put("size", TableInfo.Column("size", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSongs.put("duration", TableInfo.Column("duration", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSongs.put("trackNumber", TableInfo.Column("trackNumber", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSongs.put("discNumber", TableInfo.Column("discNumber", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSongs.put("year", TableInfo.Column("year", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSongs.put("dateAdded", TableInfo.Column("dateAdded", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSongs.put("dateModified", TableInfo.Column("dateModified", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSongs.put("albumId", TableInfo.Column("albumId", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSongs.put("artistId", TableInfo.Column("artistId", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSongs.put("bitrate", TableInfo.Column("bitrate", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSongs.put("sampleRate", TableInfo.Column("sampleRate", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSongs.put("mimeType", TableInfo.Column("mimeType", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSongs.put("lastPlayedAt", TableInfo.Column("lastPlayedAt", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSongs.put("lastPlayedPosition", TableInfo.Column("lastPlayedPosition", "INTEGER",
            true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSongs.put("playCount", TableInfo.Column("playCount", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSongs.put("isFavorite", TableInfo.Column("isFavorite", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSongs.put("lyricsPath", TableInfo.Column("lyricsPath", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSongs.put("embeddedLyrics", TableInfo.Column("embeddedLyrics", "TEXT", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysSongs: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesSongs: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesSongs.add(TableInfo.Index("index_songs_albumId", false, listOf("albumId"),
            listOf("ASC")))
        _indicesSongs.add(TableInfo.Index("index_songs_artistId", false, listOf("artistId"),
            listOf("ASC")))
        _indicesSongs.add(TableInfo.Index("index_songs_album", false, listOf("album"),
            listOf("ASC")))
        _indicesSongs.add(TableInfo.Index("index_songs_artist", false, listOf("artist"),
            listOf("ASC")))
        _indicesSongs.add(TableInfo.Index("index_songs_genre", false, listOf("genre"),
            listOf("ASC")))
        _indicesSongs.add(TableInfo.Index("index_songs_dateAdded", false, listOf("dateAdded"),
            listOf("ASC")))
        _indicesSongs.add(TableInfo.Index("index_songs_lastPlayedAt", false, listOf("lastPlayedAt"),
            listOf("ASC")))
        _indicesSongs.add(TableInfo.Index("index_songs_isFavorite", false, listOf("isFavorite"),
            listOf("ASC")))
        val _infoSongs: TableInfo = TableInfo("songs", _columnsSongs, _foreignKeysSongs,
            _indicesSongs)
        val _existingSongs: TableInfo = read(connection, "songs")
        if (!_infoSongs.equals(_existingSongs)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |songs(com.dehar.player.core.data.model.SongEntity).
              | Expected:
              |""".trimMargin() + _infoSongs + """
              |
              | Found:
              |""".trimMargin() + _existingSongs)
        }
        val _columnsPlaylists: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsPlaylists.put("id", TableInfo.Column("id", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsPlaylists.put("name", TableInfo.Column("name", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsPlaylists.put("type", TableInfo.Column("type", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsPlaylists.put("createdAt", TableInfo.Column("createdAt", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsPlaylists.put("updatedAt", TableInfo.Column("updatedAt", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsPlaylists.put("coverArtPath", TableInfo.Column("coverArtPath", "TEXT", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsPlaylists.put("description", TableInfo.Column("description", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsPlaylists.put("sortOrder", TableInfo.Column("sortOrder", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsPlaylists.put("isSystemPlaylist", TableInfo.Column("isSystemPlaylist", "INTEGER",
            true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysPlaylists: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesPlaylists: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoPlaylists: TableInfo = TableInfo("playlists", _columnsPlaylists,
            _foreignKeysPlaylists, _indicesPlaylists)
        val _existingPlaylists: TableInfo = read(connection, "playlists")
        if (!_infoPlaylists.equals(_existingPlaylists)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |playlists(com.dehar.player.core.data.model.PlaylistEntity).
              | Expected:
              |""".trimMargin() + _infoPlaylists + """
              |
              | Found:
              |""".trimMargin() + _existingPlaylists)
        }
        val _columnsPlaylistSongs: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsPlaylistSongs.put("playlistId", TableInfo.Column("playlistId", "INTEGER", true, 1,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsPlaylistSongs.put("songId", TableInfo.Column("songId", "INTEGER", true, 2, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsPlaylistSongs.put("position", TableInfo.Column("position", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsPlaylistSongs.put("addedAt", TableInfo.Column("addedAt", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysPlaylistSongs: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        _foreignKeysPlaylistSongs.add(TableInfo.ForeignKey("playlists", "CASCADE", "NO ACTION",
            listOf("playlistId"), listOf("id")))
        _foreignKeysPlaylistSongs.add(TableInfo.ForeignKey("songs", "CASCADE", "NO ACTION",
            listOf("songId"), listOf("id")))
        val _indicesPlaylistSongs: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesPlaylistSongs.add(TableInfo.Index("index_playlist_songs_playlistId", false,
            listOf("playlistId"), listOf("ASC")))
        _indicesPlaylistSongs.add(TableInfo.Index("index_playlist_songs_songId", false,
            listOf("songId"), listOf("ASC")))
        val _infoPlaylistSongs: TableInfo = TableInfo("playlist_songs", _columnsPlaylistSongs,
            _foreignKeysPlaylistSongs, _indicesPlaylistSongs)
        val _existingPlaylistSongs: TableInfo = read(connection, "playlist_songs")
        if (!_infoPlaylistSongs.equals(_existingPlaylistSongs)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |playlist_songs(com.dehar.player.core.data.model.PlaylistSongCrossRef).
              | Expected:
              |""".trimMargin() + _infoPlaylistSongs + """
              |
              | Found:
              |""".trimMargin() + _existingPlaylistSongs)
        }
        val _columnsPlaylistVideos: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsPlaylistVideos.put("playlistId", TableInfo.Column("playlistId", "INTEGER", true, 1,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsPlaylistVideos.put("videoId", TableInfo.Column("videoId", "INTEGER", true, 2, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsPlaylistVideos.put("position", TableInfo.Column("position", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsPlaylistVideos.put("addedAt", TableInfo.Column("addedAt", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysPlaylistVideos: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        _foreignKeysPlaylistVideos.add(TableInfo.ForeignKey("playlists", "CASCADE", "NO ACTION",
            listOf("playlistId"), listOf("id")))
        _foreignKeysPlaylistVideos.add(TableInfo.ForeignKey("videos", "CASCADE", "NO ACTION",
            listOf("videoId"), listOf("id")))
        val _indicesPlaylistVideos: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesPlaylistVideos.add(TableInfo.Index("index_playlist_videos_playlistId", false,
            listOf("playlistId"), listOf("ASC")))
        _indicesPlaylistVideos.add(TableInfo.Index("index_playlist_videos_videoId", false,
            listOf("videoId"), listOf("ASC")))
        val _infoPlaylistVideos: TableInfo = TableInfo("playlist_videos", _columnsPlaylistVideos,
            _foreignKeysPlaylistVideos, _indicesPlaylistVideos)
        val _existingPlaylistVideos: TableInfo = read(connection, "playlist_videos")
        if (!_infoPlaylistVideos.equals(_existingPlaylistVideos)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |playlist_videos(com.dehar.player.core.data.model.PlaylistVideoCrossRef).
              | Expected:
              |""".trimMargin() + _infoPlaylistVideos + """
              |
              | Found:
              |""".trimMargin() + _existingPlaylistVideos)
        }
        val _columnsBookmarks: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsBookmarks.put("id", TableInfo.Column("id", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsBookmarks.put("mediaId", TableInfo.Column("mediaId", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsBookmarks.put("mediaType", TableInfo.Column("mediaType", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsBookmarks.put("positionMs", TableInfo.Column("positionMs", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsBookmarks.put("label", TableInfo.Column("label", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsBookmarks.put("createdAt", TableInfo.Column("createdAt", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsBookmarks.put("thumbnailPath", TableInfo.Column("thumbnailPath", "TEXT", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysBookmarks: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesBookmarks: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesBookmarks.add(TableInfo.Index("index_bookmarks_mediaId_mediaType", false,
            listOf("mediaId", "mediaType"), listOf("ASC", "ASC")))
        val _infoBookmarks: TableInfo = TableInfo("bookmarks", _columnsBookmarks,
            _foreignKeysBookmarks, _indicesBookmarks)
        val _existingBookmarks: TableInfo = read(connection, "bookmarks")
        if (!_infoBookmarks.equals(_existingBookmarks)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |bookmarks(com.dehar.player.core.data.model.BookmarkEntity).
              | Expected:
              |""".trimMargin() + _infoBookmarks + """
              |
              | Found:
              |""".trimMargin() + _existingBookmarks)
        }
        val _columnsSubtitleTracks: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsSubtitleTracks.put("id", TableInfo.Column("id", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSubtitleTracks.put("videoId", TableInfo.Column("videoId", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSubtitleTracks.put("language", TableInfo.Column("language", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSubtitleTracks.put("label", TableInfo.Column("label", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSubtitleTracks.put("path", TableInfo.Column("path", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSubtitleTracks.put("format", TableInfo.Column("format", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSubtitleTracks.put("encoding", TableInfo.Column("encoding", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSubtitleTracks.put("isDefault", TableInfo.Column("isDefault", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSubtitleTracks.put("offsetMs", TableInfo.Column("offsetMs", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysSubtitleTracks: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesSubtitleTracks: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesSubtitleTracks.add(TableInfo.Index("index_subtitle_tracks_videoId", false,
            listOf("videoId"), listOf("ASC")))
        val _infoSubtitleTracks: TableInfo = TableInfo("subtitle_tracks", _columnsSubtitleTracks,
            _foreignKeysSubtitleTracks, _indicesSubtitleTracks)
        val _existingSubtitleTracks: TableInfo = read(connection, "subtitle_tracks")
        if (!_infoSubtitleTracks.equals(_existingSubtitleTracks)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |subtitle_tracks(com.dehar.player.core.data.model.SubtitleTrackEntity).
              | Expected:
              |""".trimMargin() + _infoSubtitleTracks + """
              |
              | Found:
              |""".trimMargin() + _existingSubtitleTracks)
        }
        val _columnsPlaybackHistory: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsPlaybackHistory.put("id", TableInfo.Column("id", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsPlaybackHistory.put("mediaId", TableInfo.Column("mediaId", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsPlaybackHistory.put("mediaType", TableInfo.Column("mediaType", "TEXT", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsPlaybackHistory.put("playedAt", TableInfo.Column("playedAt", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsPlaybackHistory.put("durationPlayedMs", TableInfo.Column("durationPlayedMs",
            "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsPlaybackHistory.put("completionPercent", TableInfo.Column("completionPercent",
            "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysPlaybackHistory: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesPlaybackHistory: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesPlaybackHistory.add(TableInfo.Index("index_playback_history_mediaId_mediaType",
            false, listOf("mediaId", "mediaType"), listOf("ASC", "ASC")))
        _indicesPlaybackHistory.add(TableInfo.Index("index_playback_history_playedAt", false,
            listOf("playedAt"), listOf("ASC")))
        val _infoPlaybackHistory: TableInfo = TableInfo("playback_history", _columnsPlaybackHistory,
            _foreignKeysPlaybackHistory, _indicesPlaybackHistory)
        val _existingPlaybackHistory: TableInfo = read(connection, "playback_history")
        if (!_infoPlaybackHistory.equals(_existingPlaybackHistory)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |playback_history(com.dehar.player.core.data.model.PlaybackHistoryEntity).
              | Expected:
              |""".trimMargin() + _infoPlaybackHistory + """
              |
              | Found:
              |""".trimMargin() + _existingPlaybackHistory)
        }
        val _columnsPrivateVault: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsPrivateVault.put("id", TableInfo.Column("id", "TEXT", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsPrivateVault.put("originalPath", TableInfo.Column("originalPath", "TEXT", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsPrivateVault.put("encryptedPath", TableInfo.Column("encryptedPath", "TEXT", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsPrivateVault.put("mediaType", TableInfo.Column("mediaType", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsPrivateVault.put("addedAt", TableInfo.Column("addedAt", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsPrivateVault.put("thumbnailPath", TableInfo.Column("thumbnailPath", "TEXT", false,
            0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsPrivateVault.put("title", TableInfo.Column("title", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsPrivateVault.put("duration", TableInfo.Column("duration", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysPrivateVault: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesPrivateVault: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoPrivateVault: TableInfo = TableInfo("private_vault", _columnsPrivateVault,
            _foreignKeysPrivateVault, _indicesPrivateVault)
        val _existingPrivateVault: TableInfo = read(connection, "private_vault")
        if (!_infoPrivateVault.equals(_existingPrivateVault)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |private_vault(com.dehar.player.core.data.model.PrivateVaultItemEntity).
              | Expected:
              |""".trimMargin() + _infoPrivateVault + """
              |
              | Found:
              |""".trimMargin() + _existingPrivateVault)
        }
        val _columnsDownloads: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsDownloads.put("id", TableInfo.Column("id", "TEXT", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsDownloads.put("url", TableInfo.Column("url", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsDownloads.put("title", TableInfo.Column("title", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsDownloads.put("destinationPath", TableInfo.Column("destinationPath", "TEXT", true,
            0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsDownloads.put("totalBytes", TableInfo.Column("totalBytes", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsDownloads.put("downloadedBytes", TableInfo.Column("downloadedBytes", "INTEGER",
            true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsDownloads.put("status", TableInfo.Column("status", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsDownloads.put("mediaType", TableInfo.Column("mediaType", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsDownloads.put("startedAt", TableInfo.Column("startedAt", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsDownloads.put("completedAt", TableInfo.Column("completedAt", "INTEGER", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsDownloads.put("errorMessage", TableInfo.Column("errorMessage", "TEXT", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysDownloads: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesDownloads: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoDownloads: TableInfo = TableInfo("downloads", _columnsDownloads,
            _foreignKeysDownloads, _indicesDownloads)
        val _existingDownloads: TableInfo = read(connection, "downloads")
        if (!_infoDownloads.equals(_existingDownloads)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |downloads(com.dehar.player.core.data.model.DownloadEntity).
              | Expected:
              |""".trimMargin() + _infoDownloads + """
              |
              | Found:
              |""".trimMargin() + _existingDownloads)
        }
        val _columnsNetworkStreams: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsNetworkStreams.put("id", TableInfo.Column("id", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsNetworkStreams.put("url", TableInfo.Column("url", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsNetworkStreams.put("title", TableInfo.Column("title", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsNetworkStreams.put("addedAt", TableInfo.Column("addedAt", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsNetworkStreams.put("lastPlayedAt", TableInfo.Column("lastPlayedAt", "INTEGER",
            false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsNetworkStreams.put("mediaType", TableInfo.Column("mediaType", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsNetworkStreams.put("headers", TableInfo.Column("headers", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysNetworkStreams: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesNetworkStreams: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoNetworkStreams: TableInfo = TableInfo("network_streams", _columnsNetworkStreams,
            _foreignKeysNetworkStreams, _indicesNetworkStreams)
        val _existingNetworkStreams: TableInfo = read(connection, "network_streams")
        if (!_infoNetworkStreams.equals(_existingNetworkStreams)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |network_streams(com.dehar.player.core.data.model.NetworkStreamEntity).
              | Expected:
              |""".trimMargin() + _infoNetworkStreams + """
              |
              | Found:
              |""".trimMargin() + _existingNetworkStreams)
        }
        val _columnsEqPresets: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsEqPresets.put("id", TableInfo.Column("id", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsEqPresets.put("name", TableInfo.Column("name", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsEqPresets.put("bands", TableInfo.Column("bands", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsEqPresets.put("bassBoost", TableInfo.Column("bassBoost", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsEqPresets.put("virtualizer", TableInfo.Column("virtualizer", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsEqPresets.put("loudnessEnhancer", TableInfo.Column("loudnessEnhancer", "INTEGER",
            true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsEqPresets.put("isSystem", TableInfo.Column("isSystem", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsEqPresets.put("isActive", TableInfo.Column("isActive", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysEqPresets: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesEqPresets: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoEqPresets: TableInfo = TableInfo("eq_presets", _columnsEqPresets,
            _foreignKeysEqPresets, _indicesEqPresets)
        val _existingEqPresets: TableInfo = read(connection, "eq_presets")
        if (!_infoEqPresets.equals(_existingEqPresets)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |eq_presets(com.dehar.player.core.data.model.EqPresetEntity).
              | Expected:
              |""".trimMargin() + _infoEqPresets + """
              |
              | Found:
              |""".trimMargin() + _existingEqPresets)
        }
        val _columnsRecycleBin: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsRecycleBin.put("id", TableInfo.Column("id", "TEXT", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsRecycleBin.put("originalPath", TableInfo.Column("originalPath", "TEXT", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsRecycleBin.put("originalName", TableInfo.Column("originalName", "TEXT", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsRecycleBin.put("mediaType", TableInfo.Column("mediaType", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsRecycleBin.put("size", TableInfo.Column("size", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsRecycleBin.put("deletedAt", TableInfo.Column("deletedAt", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsRecycleBin.put("thumbnailPath", TableInfo.Column("thumbnailPath", "TEXT", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsRecycleBin.put("restorePath", TableInfo.Column("restorePath", "TEXT", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysRecycleBin: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesRecycleBin: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesRecycleBin.add(TableInfo.Index("index_recycle_bin_deletedAt", false,
            listOf("deletedAt"), listOf("ASC")))
        val _infoRecycleBin: TableInfo = TableInfo("recycle_bin", _columnsRecycleBin,
            _foreignKeysRecycleBin, _indicesRecycleBin)
        val _existingRecycleBin: TableInfo = read(connection, "recycle_bin")
        if (!_infoRecycleBin.equals(_existingRecycleBin)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |recycle_bin(com.dehar.player.core.data.model.RecycleBinEntity).
              | Expected:
              |""".trimMargin() + _infoRecycleBin + """
              |
              | Found:
              |""".trimMargin() + _existingRecycleBin)
        }
        return RoomOpenDelegate.ValidationResult(true, null)
      }
    }
    return _openDelegate
  }

  protected override fun createInvalidationTracker(): InvalidationTracker {
    val _shadowTablesMap: MutableMap<String, String> = mutableMapOf()
    val _viewTables: MutableMap<String, Set<String>> = mutableMapOf()
    return InvalidationTracker(this, _shadowTablesMap, _viewTables, "videos", "songs", "playlists",
        "playlist_songs", "playlist_videos", "bookmarks", "subtitle_tracks", "playback_history",
        "private_vault", "downloads", "network_streams", "eq_presets", "recycle_bin")
  }

  public override fun clearAllTables() {
    super.performClear(true, "videos", "songs", "playlists", "playlist_songs", "playlist_videos",
        "bookmarks", "subtitle_tracks", "playback_history", "private_vault", "downloads",
        "network_streams", "eq_presets", "recycle_bin")
  }

  protected override fun getRequiredTypeConverterClasses(): Map<KClass<*>, List<KClass<*>>> {
    val _typeConvertersMap: MutableMap<KClass<*>, List<KClass<*>>> = mutableMapOf()
    _typeConvertersMap.put(VideoDao::class, VideoDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(SongDao::class, SongDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(PlaylistDao::class, PlaylistDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(BookmarkDao::class, BookmarkDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(SubtitleDao::class, SubtitleDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(PlaybackHistoryDao::class,
        PlaybackHistoryDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(PrivateVaultDao::class, PrivateVaultDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(DownloadDao::class, DownloadDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(NetworkStreamDao::class, NetworkStreamDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(EqPresetDao::class, EqPresetDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(RecycleBinDao::class, RecycleBinDao_Impl.getRequiredConverters())
    return _typeConvertersMap
  }

  public override fun getRequiredAutoMigrationSpecClasses(): Set<KClass<out AutoMigrationSpec>> {
    val _autoMigrationSpecsSet: MutableSet<KClass<out AutoMigrationSpec>> = mutableSetOf()
    return _autoMigrationSpecsSet
  }

  public override
      fun createAutoMigrations(autoMigrationSpecs: Map<KClass<out AutoMigrationSpec>, AutoMigrationSpec>):
      List<Migration> {
    val _autoMigrations: MutableList<Migration> = mutableListOf()
    return _autoMigrations
  }

  public override fun videoDao(): VideoDao = _videoDao.value

  public override fun songDao(): SongDao = _songDao.value

  public override fun playlistDao(): PlaylistDao = _playlistDao.value

  public override fun bookmarkDao(): BookmarkDao = _bookmarkDao.value

  public override fun subtitleDao(): SubtitleDao = _subtitleDao.value

  public override fun historyDao(): PlaybackHistoryDao = _playbackHistoryDao.value

  public override fun vaultDao(): PrivateVaultDao = _privateVaultDao.value

  public override fun downloadDao(): DownloadDao = _downloadDao.value

  public override fun streamDao(): NetworkStreamDao = _networkStreamDao.value

  public override fun eqPresetDao(): EqPresetDao = _eqPresetDao.value

  public override fun recycleBinDao(): RecycleBinDao = _recycleBinDao.value
}
