package com.dehar.player.core.`data`.database

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.dehar.player.core.`data`.model.PlaylistEntity
import com.dehar.player.core.`data`.model.PlaylistSongCrossRef
import com.dehar.player.core.`data`.model.PlaylistType
import com.dehar.player.core.`data`.model.PlaylistVideoCrossRef
import com.dehar.player.core.`data`.model.SongEntity
import com.dehar.player.core.`data`.model.VideoEntity
import javax.`annotation`.processing.Generated
import kotlin.Boolean
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class PlaylistDao_Impl(
  __db: RoomDatabase,
) : PlaylistDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfPlaylistEntity: EntityInsertAdapter<PlaylistEntity>

  private val __converters: Converters = Converters()

  private val __insertAdapterOfPlaylistSongCrossRef: EntityInsertAdapter<PlaylistSongCrossRef>

  private val __insertAdapterOfPlaylistVideoCrossRef: EntityInsertAdapter<PlaylistVideoCrossRef>

  private val __deleteAdapterOfPlaylistEntity: EntityDeleteOrUpdateAdapter<PlaylistEntity>

  private val __updateAdapterOfPlaylistEntity: EntityDeleteOrUpdateAdapter<PlaylistEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfPlaylistEntity = object : EntityInsertAdapter<PlaylistEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `playlists` (`id`,`name`,`type`,`createdAt`,`updatedAt`,`coverArtPath`,`description`,`sortOrder`,`isSystemPlaylist`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: PlaylistEntity) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.name)
        val _tmp: String = __converters.fromPlaylistType(entity.type)
        statement.bindText(3, _tmp)
        statement.bindLong(4, entity.createdAt)
        statement.bindLong(5, entity.updatedAt)
        val _tmpCoverArtPath: String? = entity.coverArtPath
        if (_tmpCoverArtPath == null) {
          statement.bindNull(6)
        } else {
          statement.bindText(6, _tmpCoverArtPath)
        }
        statement.bindText(7, entity.description)
        statement.bindLong(8, entity.sortOrder.toLong())
        val _tmp_1: Int = if (entity.isSystemPlaylist) 1 else 0
        statement.bindLong(9, _tmp_1.toLong())
      }
    }
    this.__insertAdapterOfPlaylistSongCrossRef = object :
        EntityInsertAdapter<PlaylistSongCrossRef>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `playlist_songs` (`playlistId`,`songId`,`position`,`addedAt`) VALUES (?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: PlaylistSongCrossRef) {
        statement.bindLong(1, entity.playlistId)
        statement.bindLong(2, entity.songId)
        statement.bindLong(3, entity.position.toLong())
        statement.bindLong(4, entity.addedAt)
      }
    }
    this.__insertAdapterOfPlaylistVideoCrossRef = object :
        EntityInsertAdapter<PlaylistVideoCrossRef>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `playlist_videos` (`playlistId`,`videoId`,`position`,`addedAt`) VALUES (?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: PlaylistVideoCrossRef) {
        statement.bindLong(1, entity.playlistId)
        statement.bindLong(2, entity.videoId)
        statement.bindLong(3, entity.position.toLong())
        statement.bindLong(4, entity.addedAt)
      }
    }
    this.__deleteAdapterOfPlaylistEntity = object : EntityDeleteOrUpdateAdapter<PlaylistEntity>() {
      protected override fun createQuery(): String = "DELETE FROM `playlists` WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: PlaylistEntity) {
        statement.bindLong(1, entity.id)
      }
    }
    this.__updateAdapterOfPlaylistEntity = object : EntityDeleteOrUpdateAdapter<PlaylistEntity>() {
      protected override fun createQuery(): String =
          "UPDATE OR ABORT `playlists` SET `id` = ?,`name` = ?,`type` = ?,`createdAt` = ?,`updatedAt` = ?,`coverArtPath` = ?,`description` = ?,`sortOrder` = ?,`isSystemPlaylist` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: PlaylistEntity) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.name)
        val _tmp: String = __converters.fromPlaylistType(entity.type)
        statement.bindText(3, _tmp)
        statement.bindLong(4, entity.createdAt)
        statement.bindLong(5, entity.updatedAt)
        val _tmpCoverArtPath: String? = entity.coverArtPath
        if (_tmpCoverArtPath == null) {
          statement.bindNull(6)
        } else {
          statement.bindText(6, _tmpCoverArtPath)
        }
        statement.bindText(7, entity.description)
        statement.bindLong(8, entity.sortOrder.toLong())
        val _tmp_1: Int = if (entity.isSystemPlaylist) 1 else 0
        statement.bindLong(9, _tmp_1.toLong())
        statement.bindLong(10, entity.id)
      }
    }
  }

  public override suspend fun insertPlaylist(playlist: PlaylistEntity): Long =
      performSuspending(__db, false, true) { _connection ->
    val _result: Long = __insertAdapterOfPlaylistEntity.insertAndReturnId(_connection, playlist)
    _result
  }

  public override suspend fun addSongToPlaylist(crossRef: PlaylistSongCrossRef): Unit =
      performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfPlaylistSongCrossRef.insert(_connection, crossRef)
  }

  public override suspend fun addVideoToPlaylist(crossRef: PlaylistVideoCrossRef): Unit =
      performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfPlaylistVideoCrossRef.insert(_connection, crossRef)
  }

  public override suspend fun deletePlaylist(playlist: PlaylistEntity): Unit =
      performSuspending(__db, false, true) { _connection ->
    __deleteAdapterOfPlaylistEntity.handle(_connection, playlist)
  }

  public override suspend fun updatePlaylist(playlist: PlaylistEntity): Unit =
      performSuspending(__db, false, true) { _connection ->
    __updateAdapterOfPlaylistEntity.handle(_connection, playlist)
  }

  public override fun getAllPlaylists(): Flow<List<PlaylistEntity>> {
    val _sql: String = "SELECT * FROM playlists WHERE isSystemPlaylist = 0 ORDER BY name ASC"
    return createFlow(__db, false, arrayOf("playlists")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfType: Int = getColumnIndexOrThrow(_stmt, "type")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfCoverArtPath: Int = getColumnIndexOrThrow(_stmt, "coverArtPath")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfSortOrder: Int = getColumnIndexOrThrow(_stmt, "sortOrder")
        val _columnIndexOfIsSystemPlaylist: Int = getColumnIndexOrThrow(_stmt, "isSystemPlaylist")
        val _result: MutableList<PlaylistEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: PlaylistEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpType: PlaylistType
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfType)
          _tmpType = __converters.toPlaylistType(_tmp)
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          val _tmpUpdatedAt: Long
          _tmpUpdatedAt = _stmt.getLong(_columnIndexOfUpdatedAt)
          val _tmpCoverArtPath: String?
          if (_stmt.isNull(_columnIndexOfCoverArtPath)) {
            _tmpCoverArtPath = null
          } else {
            _tmpCoverArtPath = _stmt.getText(_columnIndexOfCoverArtPath)
          }
          val _tmpDescription: String
          _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          val _tmpSortOrder: Int
          _tmpSortOrder = _stmt.getLong(_columnIndexOfSortOrder).toInt()
          val _tmpIsSystemPlaylist: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsSystemPlaylist).toInt()
          _tmpIsSystemPlaylist = _tmp_1 != 0
          _item =
              PlaylistEntity(_tmpId,_tmpName,_tmpType,_tmpCreatedAt,_tmpUpdatedAt,_tmpCoverArtPath,_tmpDescription,_tmpSortOrder,_tmpIsSystemPlaylist)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getSystemPlaylists(): Flow<List<PlaylistEntity>> {
    val _sql: String = "SELECT * FROM playlists WHERE isSystemPlaylist = 1 ORDER BY id ASC"
    return createFlow(__db, false, arrayOf("playlists")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfType: Int = getColumnIndexOrThrow(_stmt, "type")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfCoverArtPath: Int = getColumnIndexOrThrow(_stmt, "coverArtPath")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfSortOrder: Int = getColumnIndexOrThrow(_stmt, "sortOrder")
        val _columnIndexOfIsSystemPlaylist: Int = getColumnIndexOrThrow(_stmt, "isSystemPlaylist")
        val _result: MutableList<PlaylistEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: PlaylistEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpType: PlaylistType
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfType)
          _tmpType = __converters.toPlaylistType(_tmp)
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          val _tmpUpdatedAt: Long
          _tmpUpdatedAt = _stmt.getLong(_columnIndexOfUpdatedAt)
          val _tmpCoverArtPath: String?
          if (_stmt.isNull(_columnIndexOfCoverArtPath)) {
            _tmpCoverArtPath = null
          } else {
            _tmpCoverArtPath = _stmt.getText(_columnIndexOfCoverArtPath)
          }
          val _tmpDescription: String
          _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          val _tmpSortOrder: Int
          _tmpSortOrder = _stmt.getLong(_columnIndexOfSortOrder).toInt()
          val _tmpIsSystemPlaylist: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsSystemPlaylist).toInt()
          _tmpIsSystemPlaylist = _tmp_1 != 0
          _item =
              PlaylistEntity(_tmpId,_tmpName,_tmpType,_tmpCreatedAt,_tmpUpdatedAt,_tmpCoverArtPath,_tmpDescription,_tmpSortOrder,_tmpIsSystemPlaylist)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getPlaylistById(id: Long): Flow<PlaylistEntity?> {
    val _sql: String = "SELECT * FROM playlists WHERE id = ?"
    return createFlow(__db, false, arrayOf("playlists")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfType: Int = getColumnIndexOrThrow(_stmt, "type")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfCoverArtPath: Int = getColumnIndexOrThrow(_stmt, "coverArtPath")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfSortOrder: Int = getColumnIndexOrThrow(_stmt, "sortOrder")
        val _columnIndexOfIsSystemPlaylist: Int = getColumnIndexOrThrow(_stmt, "isSystemPlaylist")
        val _result: PlaylistEntity?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpType: PlaylistType
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfType)
          _tmpType = __converters.toPlaylistType(_tmp)
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          val _tmpUpdatedAt: Long
          _tmpUpdatedAt = _stmt.getLong(_columnIndexOfUpdatedAt)
          val _tmpCoverArtPath: String?
          if (_stmt.isNull(_columnIndexOfCoverArtPath)) {
            _tmpCoverArtPath = null
          } else {
            _tmpCoverArtPath = _stmt.getText(_columnIndexOfCoverArtPath)
          }
          val _tmpDescription: String
          _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          val _tmpSortOrder: Int
          _tmpSortOrder = _stmt.getLong(_columnIndexOfSortOrder).toInt()
          val _tmpIsSystemPlaylist: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsSystemPlaylist).toInt()
          _tmpIsSystemPlaylist = _tmp_1 != 0
          _result =
              PlaylistEntity(_tmpId,_tmpName,_tmpType,_tmpCreatedAt,_tmpUpdatedAt,_tmpCoverArtPath,_tmpDescription,_tmpSortOrder,_tmpIsSystemPlaylist)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getPlaylistByIdOnce(id: Long): PlaylistEntity? {
    val _sql: String = "SELECT * FROM playlists WHERE id = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfType: Int = getColumnIndexOrThrow(_stmt, "type")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfCoverArtPath: Int = getColumnIndexOrThrow(_stmt, "coverArtPath")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfSortOrder: Int = getColumnIndexOrThrow(_stmt, "sortOrder")
        val _columnIndexOfIsSystemPlaylist: Int = getColumnIndexOrThrow(_stmt, "isSystemPlaylist")
        val _result: PlaylistEntity?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpType: PlaylistType
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfType)
          _tmpType = __converters.toPlaylistType(_tmp)
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          val _tmpUpdatedAt: Long
          _tmpUpdatedAt = _stmt.getLong(_columnIndexOfUpdatedAt)
          val _tmpCoverArtPath: String?
          if (_stmt.isNull(_columnIndexOfCoverArtPath)) {
            _tmpCoverArtPath = null
          } else {
            _tmpCoverArtPath = _stmt.getText(_columnIndexOfCoverArtPath)
          }
          val _tmpDescription: String
          _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          val _tmpSortOrder: Int
          _tmpSortOrder = _stmt.getLong(_columnIndexOfSortOrder).toInt()
          val _tmpIsSystemPlaylist: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsSystemPlaylist).toInt()
          _tmpIsSystemPlaylist = _tmp_1 != 0
          _result =
              PlaylistEntity(_tmpId,_tmpName,_tmpType,_tmpCreatedAt,_tmpUpdatedAt,_tmpCoverArtPath,_tmpDescription,_tmpSortOrder,_tmpIsSystemPlaylist)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getSongsInPlaylist(playlistId: Long): Flow<List<PlaylistSongCrossRef>> {
    val _sql: String = "SELECT * FROM playlist_songs WHERE playlistId = ? ORDER BY position ASC"
    return createFlow(__db, false, arrayOf("playlist_songs")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, playlistId)
        val _columnIndexOfPlaylistId: Int = getColumnIndexOrThrow(_stmt, "playlistId")
        val _columnIndexOfSongId: Int = getColumnIndexOrThrow(_stmt, "songId")
        val _columnIndexOfPosition: Int = getColumnIndexOrThrow(_stmt, "position")
        val _columnIndexOfAddedAt: Int = getColumnIndexOrThrow(_stmt, "addedAt")
        val _result: MutableList<PlaylistSongCrossRef> = mutableListOf()
        while (_stmt.step()) {
          val _item: PlaylistSongCrossRef
          val _tmpPlaylistId: Long
          _tmpPlaylistId = _stmt.getLong(_columnIndexOfPlaylistId)
          val _tmpSongId: Long
          _tmpSongId = _stmt.getLong(_columnIndexOfSongId)
          val _tmpPosition: Int
          _tmpPosition = _stmt.getLong(_columnIndexOfPosition).toInt()
          val _tmpAddedAt: Long
          _tmpAddedAt = _stmt.getLong(_columnIndexOfAddedAt)
          _item = PlaylistSongCrossRef(_tmpPlaylistId,_tmpSongId,_tmpPosition,_tmpAddedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getVideosInPlaylist(playlistId: Long): Flow<List<PlaylistVideoCrossRef>> {
    val _sql: String = "SELECT * FROM playlist_videos WHERE playlistId = ? ORDER BY position ASC"
    return createFlow(__db, false, arrayOf("playlist_videos")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, playlistId)
        val _columnIndexOfPlaylistId: Int = getColumnIndexOrThrow(_stmt, "playlistId")
        val _columnIndexOfVideoId: Int = getColumnIndexOrThrow(_stmt, "videoId")
        val _columnIndexOfPosition: Int = getColumnIndexOrThrow(_stmt, "position")
        val _columnIndexOfAddedAt: Int = getColumnIndexOrThrow(_stmt, "addedAt")
        val _result: MutableList<PlaylistVideoCrossRef> = mutableListOf()
        while (_stmt.step()) {
          val _item: PlaylistVideoCrossRef
          val _tmpPlaylistId: Long
          _tmpPlaylistId = _stmt.getLong(_columnIndexOfPlaylistId)
          val _tmpVideoId: Long
          _tmpVideoId = _stmt.getLong(_columnIndexOfVideoId)
          val _tmpPosition: Int
          _tmpPosition = _stmt.getLong(_columnIndexOfPosition).toInt()
          val _tmpAddedAt: Long
          _tmpAddedAt = _stmt.getLong(_columnIndexOfAddedAt)
          _item = PlaylistVideoCrossRef(_tmpPlaylistId,_tmpVideoId,_tmpPosition,_tmpAddedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getSongEntitiesInPlaylist(playlistId: Long): Flow<List<SongEntity>> {
    val _sql: String =
        "SELECT songs.* FROM songs INNER JOIN playlist_songs ON songs.id = playlist_songs.songId WHERE playlist_songs.playlistId = ? ORDER BY playlist_songs.position ASC"
    return createFlow(__db, false, arrayOf("songs", "playlist_songs")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, playlistId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfArtist: Int = getColumnIndexOrThrow(_stmt, "artist")
        val _columnIndexOfAlbum: Int = getColumnIndexOrThrow(_stmt, "album")
        val _columnIndexOfAlbumArtist: Int = getColumnIndexOrThrow(_stmt, "albumArtist")
        val _columnIndexOfGenre: Int = getColumnIndexOrThrow(_stmt, "genre")
        val _columnIndexOfPath: Int = getColumnIndexOrThrow(_stmt, "path")
        val _columnIndexOfSize: Int = getColumnIndexOrThrow(_stmt, "size")
        val _columnIndexOfDuration: Int = getColumnIndexOrThrow(_stmt, "duration")
        val _columnIndexOfTrackNumber: Int = getColumnIndexOrThrow(_stmt, "trackNumber")
        val _columnIndexOfDiscNumber: Int = getColumnIndexOrThrow(_stmt, "discNumber")
        val _columnIndexOfYear: Int = getColumnIndexOrThrow(_stmt, "year")
        val _columnIndexOfDateAdded: Int = getColumnIndexOrThrow(_stmt, "dateAdded")
        val _columnIndexOfDateModified: Int = getColumnIndexOrThrow(_stmt, "dateModified")
        val _columnIndexOfAlbumId: Int = getColumnIndexOrThrow(_stmt, "albumId")
        val _columnIndexOfArtistId: Int = getColumnIndexOrThrow(_stmt, "artistId")
        val _columnIndexOfBitrate: Int = getColumnIndexOrThrow(_stmt, "bitrate")
        val _columnIndexOfSampleRate: Int = getColumnIndexOrThrow(_stmt, "sampleRate")
        val _columnIndexOfMimeType: Int = getColumnIndexOrThrow(_stmt, "mimeType")
        val _columnIndexOfLastPlayedAt: Int = getColumnIndexOrThrow(_stmt, "lastPlayedAt")
        val _columnIndexOfLastPlayedPosition: Int = getColumnIndexOrThrow(_stmt,
            "lastPlayedPosition")
        val _columnIndexOfPlayCount: Int = getColumnIndexOrThrow(_stmt, "playCount")
        val _columnIndexOfIsFavorite: Int = getColumnIndexOrThrow(_stmt, "isFavorite")
        val _columnIndexOfLyricsPath: Int = getColumnIndexOrThrow(_stmt, "lyricsPath")
        val _columnIndexOfEmbeddedLyrics: Int = getColumnIndexOrThrow(_stmt, "embeddedLyrics")
        val _result: MutableList<SongEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: SongEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpArtist: String
          _tmpArtist = _stmt.getText(_columnIndexOfArtist)
          val _tmpAlbum: String
          _tmpAlbum = _stmt.getText(_columnIndexOfAlbum)
          val _tmpAlbumArtist: String
          _tmpAlbumArtist = _stmt.getText(_columnIndexOfAlbumArtist)
          val _tmpGenre: String
          _tmpGenre = _stmt.getText(_columnIndexOfGenre)
          val _tmpPath: String
          _tmpPath = _stmt.getText(_columnIndexOfPath)
          val _tmpSize: Long
          _tmpSize = _stmt.getLong(_columnIndexOfSize)
          val _tmpDuration: Long
          _tmpDuration = _stmt.getLong(_columnIndexOfDuration)
          val _tmpTrackNumber: Int
          _tmpTrackNumber = _stmt.getLong(_columnIndexOfTrackNumber).toInt()
          val _tmpDiscNumber: Int
          _tmpDiscNumber = _stmt.getLong(_columnIndexOfDiscNumber).toInt()
          val _tmpYear: Int
          _tmpYear = _stmt.getLong(_columnIndexOfYear).toInt()
          val _tmpDateAdded: Long
          _tmpDateAdded = _stmt.getLong(_columnIndexOfDateAdded)
          val _tmpDateModified: Long
          _tmpDateModified = _stmt.getLong(_columnIndexOfDateModified)
          val _tmpAlbumId: Long
          _tmpAlbumId = _stmt.getLong(_columnIndexOfAlbumId)
          val _tmpArtistId: Long
          _tmpArtistId = _stmt.getLong(_columnIndexOfArtistId)
          val _tmpBitrate: Long
          _tmpBitrate = _stmt.getLong(_columnIndexOfBitrate)
          val _tmpSampleRate: Int
          _tmpSampleRate = _stmt.getLong(_columnIndexOfSampleRate).toInt()
          val _tmpMimeType: String
          _tmpMimeType = _stmt.getText(_columnIndexOfMimeType)
          val _tmpLastPlayedAt: Long
          _tmpLastPlayedAt = _stmt.getLong(_columnIndexOfLastPlayedAt)
          val _tmpLastPlayedPosition: Long
          _tmpLastPlayedPosition = _stmt.getLong(_columnIndexOfLastPlayedPosition)
          val _tmpPlayCount: Int
          _tmpPlayCount = _stmt.getLong(_columnIndexOfPlayCount).toInt()
          val _tmpIsFavorite: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsFavorite).toInt()
          _tmpIsFavorite = _tmp != 0
          val _tmpLyricsPath: String?
          if (_stmt.isNull(_columnIndexOfLyricsPath)) {
            _tmpLyricsPath = null
          } else {
            _tmpLyricsPath = _stmt.getText(_columnIndexOfLyricsPath)
          }
          val _tmpEmbeddedLyrics: String?
          if (_stmt.isNull(_columnIndexOfEmbeddedLyrics)) {
            _tmpEmbeddedLyrics = null
          } else {
            _tmpEmbeddedLyrics = _stmt.getText(_columnIndexOfEmbeddedLyrics)
          }
          _item =
              SongEntity(_tmpId,_tmpTitle,_tmpArtist,_tmpAlbum,_tmpAlbumArtist,_tmpGenre,_tmpPath,_tmpSize,_tmpDuration,_tmpTrackNumber,_tmpDiscNumber,_tmpYear,_tmpDateAdded,_tmpDateModified,_tmpAlbumId,_tmpArtistId,_tmpBitrate,_tmpSampleRate,_tmpMimeType,_tmpLastPlayedAt,_tmpLastPlayedPosition,_tmpPlayCount,_tmpIsFavorite,_tmpLyricsPath,_tmpEmbeddedLyrics)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getVideoEntitiesInPlaylist(playlistId: Long): Flow<List<VideoEntity>> {
    val _sql: String =
        "SELECT videos.* FROM videos INNER JOIN playlist_videos ON videos.id = playlist_videos.videoId WHERE playlist_videos.playlistId = ? ORDER BY playlist_videos.position ASC"
    return createFlow(__db, false, arrayOf("videos", "playlist_videos")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, playlistId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDisplayName: Int = getColumnIndexOrThrow(_stmt, "displayName")
        val _columnIndexOfPath: Int = getColumnIndexOrThrow(_stmt, "path")
        val _columnIndexOfSize: Int = getColumnIndexOrThrow(_stmt, "size")
        val _columnIndexOfDuration: Int = getColumnIndexOrThrow(_stmt, "duration")
        val _columnIndexOfWidth: Int = getColumnIndexOrThrow(_stmt, "width")
        val _columnIndexOfHeight: Int = getColumnIndexOrThrow(_stmt, "height")
        val _columnIndexOfMimeType: Int = getColumnIndexOrThrow(_stmt, "mimeType")
        val _columnIndexOfDateAdded: Int = getColumnIndexOrThrow(_stmt, "dateAdded")
        val _columnIndexOfDateModified: Int = getColumnIndexOrThrow(_stmt, "dateModified")
        val _columnIndexOfBucketId: Int = getColumnIndexOrThrow(_stmt, "bucketId")
        val _columnIndexOfBucketName: Int = getColumnIndexOrThrow(_stmt, "bucketName")
        val _columnIndexOfResolution: Int = getColumnIndexOrThrow(_stmt, "resolution")
        val _columnIndexOfFrameRate: Int = getColumnIndexOrThrow(_stmt, "frameRate")
        val _columnIndexOfBitrate: Int = getColumnIndexOrThrow(_stmt, "bitrate")
        val _columnIndexOfCodecName: Int = getColumnIndexOrThrow(_stmt, "codecName")
        val _columnIndexOfLastPlayedAt: Int = getColumnIndexOrThrow(_stmt, "lastPlayedAt")
        val _columnIndexOfLastPlayedPosition: Int = getColumnIndexOrThrow(_stmt,
            "lastPlayedPosition")
        val _columnIndexOfPlayCount: Int = getColumnIndexOrThrow(_stmt, "playCount")
        val _columnIndexOfIsFavorite: Int = getColumnIndexOrThrow(_stmt, "isFavorite")
        val _columnIndexOfUserRating: Int = getColumnIndexOrThrow(_stmt, "userRating")
        val _columnIndexOfCustomThumbnailPath: Int = getColumnIndexOrThrow(_stmt,
            "customThumbnailPath")
        val _columnIndexOfIsHidden: Int = getColumnIndexOrThrow(_stmt, "isHidden")
        val _columnIndexOfPrivateVaultId: Int = getColumnIndexOrThrow(_stmt, "privateVaultId")
        val _result: MutableList<VideoEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: VideoEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpDisplayName: String
          _tmpDisplayName = _stmt.getText(_columnIndexOfDisplayName)
          val _tmpPath: String
          _tmpPath = _stmt.getText(_columnIndexOfPath)
          val _tmpSize: Long
          _tmpSize = _stmt.getLong(_columnIndexOfSize)
          val _tmpDuration: Long
          _tmpDuration = _stmt.getLong(_columnIndexOfDuration)
          val _tmpWidth: Int
          _tmpWidth = _stmt.getLong(_columnIndexOfWidth).toInt()
          val _tmpHeight: Int
          _tmpHeight = _stmt.getLong(_columnIndexOfHeight).toInt()
          val _tmpMimeType: String
          _tmpMimeType = _stmt.getText(_columnIndexOfMimeType)
          val _tmpDateAdded: Long
          _tmpDateAdded = _stmt.getLong(_columnIndexOfDateAdded)
          val _tmpDateModified: Long
          _tmpDateModified = _stmt.getLong(_columnIndexOfDateModified)
          val _tmpBucketId: Long
          _tmpBucketId = _stmt.getLong(_columnIndexOfBucketId)
          val _tmpBucketName: String
          _tmpBucketName = _stmt.getText(_columnIndexOfBucketName)
          val _tmpResolution: String
          _tmpResolution = _stmt.getText(_columnIndexOfResolution)
          val _tmpFrameRate: Int
          _tmpFrameRate = _stmt.getLong(_columnIndexOfFrameRate).toInt()
          val _tmpBitrate: Long
          _tmpBitrate = _stmt.getLong(_columnIndexOfBitrate)
          val _tmpCodecName: String
          _tmpCodecName = _stmt.getText(_columnIndexOfCodecName)
          val _tmpLastPlayedAt: Long
          _tmpLastPlayedAt = _stmt.getLong(_columnIndexOfLastPlayedAt)
          val _tmpLastPlayedPosition: Long
          _tmpLastPlayedPosition = _stmt.getLong(_columnIndexOfLastPlayedPosition)
          val _tmpPlayCount: Int
          _tmpPlayCount = _stmt.getLong(_columnIndexOfPlayCount).toInt()
          val _tmpIsFavorite: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsFavorite).toInt()
          _tmpIsFavorite = _tmp != 0
          val _tmpUserRating: Int
          _tmpUserRating = _stmt.getLong(_columnIndexOfUserRating).toInt()
          val _tmpCustomThumbnailPath: String?
          if (_stmt.isNull(_columnIndexOfCustomThumbnailPath)) {
            _tmpCustomThumbnailPath = null
          } else {
            _tmpCustomThumbnailPath = _stmt.getText(_columnIndexOfCustomThumbnailPath)
          }
          val _tmpIsHidden: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsHidden).toInt()
          _tmpIsHidden = _tmp_1 != 0
          val _tmpPrivateVaultId: String?
          if (_stmt.isNull(_columnIndexOfPrivateVaultId)) {
            _tmpPrivateVaultId = null
          } else {
            _tmpPrivateVaultId = _stmt.getText(_columnIndexOfPrivateVaultId)
          }
          _item =
              VideoEntity(_tmpId,_tmpTitle,_tmpDisplayName,_tmpPath,_tmpSize,_tmpDuration,_tmpWidth,_tmpHeight,_tmpMimeType,_tmpDateAdded,_tmpDateModified,_tmpBucketId,_tmpBucketName,_tmpResolution,_tmpFrameRate,_tmpBitrate,_tmpCodecName,_tmpLastPlayedAt,_tmpLastPlayedPosition,_tmpPlayCount,_tmpIsFavorite,_tmpUserRating,_tmpCustomThumbnailPath,_tmpIsHidden,_tmpPrivateVaultId)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getSongCountInPlaylist(playlistId: Long): Flow<Int> {
    val _sql: String = "SELECT COUNT(*) FROM playlist_songs WHERE playlistId = ?"
    return createFlow(__db, false, arrayOf("playlist_songs")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, playlistId)
        val _result: Int
        if (_stmt.step()) {
          val _tmp: Int
          _tmp = _stmt.getLong(0).toInt()
          _result = _tmp
        } else {
          _result = 0
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getVideoCountInPlaylist(playlistId: Long): Flow<Int> {
    val _sql: String = "SELECT COUNT(*) FROM playlist_videos WHERE playlistId = ?"
    return createFlow(__db, false, arrayOf("playlist_videos")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, playlistId)
        val _result: Int
        if (_stmt.step()) {
          val _tmp: Int
          _tmp = _stmt.getLong(0).toInt()
          _result = _tmp
        } else {
          _result = 0
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getPlaylistsContainingSong(songId: Long): Flow<List<PlaylistEntity>> {
    val _sql: String =
        "SELECT DISTINCT p.* FROM playlists p INNER JOIN playlist_songs ps ON p.id = ps.playlistId WHERE ps.songId = ?"
    return createFlow(__db, false, arrayOf("playlists", "playlist_songs")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, songId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfType: Int = getColumnIndexOrThrow(_stmt, "type")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfCoverArtPath: Int = getColumnIndexOrThrow(_stmt, "coverArtPath")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfSortOrder: Int = getColumnIndexOrThrow(_stmt, "sortOrder")
        val _columnIndexOfIsSystemPlaylist: Int = getColumnIndexOrThrow(_stmt, "isSystemPlaylist")
        val _result: MutableList<PlaylistEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: PlaylistEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpType: PlaylistType
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfType)
          _tmpType = __converters.toPlaylistType(_tmp)
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          val _tmpUpdatedAt: Long
          _tmpUpdatedAt = _stmt.getLong(_columnIndexOfUpdatedAt)
          val _tmpCoverArtPath: String?
          if (_stmt.isNull(_columnIndexOfCoverArtPath)) {
            _tmpCoverArtPath = null
          } else {
            _tmpCoverArtPath = _stmt.getText(_columnIndexOfCoverArtPath)
          }
          val _tmpDescription: String
          _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          val _tmpSortOrder: Int
          _tmpSortOrder = _stmt.getLong(_columnIndexOfSortOrder).toInt()
          val _tmpIsSystemPlaylist: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsSystemPlaylist).toInt()
          _tmpIsSystemPlaylist = _tmp_1 != 0
          _item =
              PlaylistEntity(_tmpId,_tmpName,_tmpType,_tmpCreatedAt,_tmpUpdatedAt,_tmpCoverArtPath,_tmpDescription,_tmpSortOrder,_tmpIsSystemPlaylist)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getPlaylistsContainingVideo(videoId: Long): Flow<List<PlaylistEntity>> {
    val _sql: String =
        "SELECT DISTINCT p.* FROM playlists p INNER JOIN playlist_videos pv ON p.id = pv.playlistId WHERE pv.videoId = ?"
    return createFlow(__db, false, arrayOf("playlists", "playlist_videos")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, videoId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfType: Int = getColumnIndexOrThrow(_stmt, "type")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfCoverArtPath: Int = getColumnIndexOrThrow(_stmt, "coverArtPath")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfSortOrder: Int = getColumnIndexOrThrow(_stmt, "sortOrder")
        val _columnIndexOfIsSystemPlaylist: Int = getColumnIndexOrThrow(_stmt, "isSystemPlaylist")
        val _result: MutableList<PlaylistEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: PlaylistEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpType: PlaylistType
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfType)
          _tmpType = __converters.toPlaylistType(_tmp)
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          val _tmpUpdatedAt: Long
          _tmpUpdatedAt = _stmt.getLong(_columnIndexOfUpdatedAt)
          val _tmpCoverArtPath: String?
          if (_stmt.isNull(_columnIndexOfCoverArtPath)) {
            _tmpCoverArtPath = null
          } else {
            _tmpCoverArtPath = _stmt.getText(_columnIndexOfCoverArtPath)
          }
          val _tmpDescription: String
          _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          val _tmpSortOrder: Int
          _tmpSortOrder = _stmt.getLong(_columnIndexOfSortOrder).toInt()
          val _tmpIsSystemPlaylist: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsSystemPlaylist).toInt()
          _tmpIsSystemPlaylist = _tmp_1 != 0
          _item =
              PlaylistEntity(_tmpId,_tmpName,_tmpType,_tmpCreatedAt,_tmpUpdatedAt,_tmpCoverArtPath,_tmpDescription,_tmpSortOrder,_tmpIsSystemPlaylist)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deletePlaylistById(id: Long) {
    val _sql: String = "DELETE FROM playlists WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, id)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun removeSongFromPlaylist(playlistId: Long, songId: Long) {
    val _sql: String = "DELETE FROM playlist_songs WHERE playlistId = ? AND songId = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, playlistId)
        _argIndex = 2
        _stmt.bindLong(_argIndex, songId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun removeVideoFromPlaylist(playlistId: Long, videoId: Long) {
    val _sql: String = "DELETE FROM playlist_videos WHERE playlistId = ? AND videoId = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, playlistId)
        _argIndex = 2
        _stmt.bindLong(_argIndex, videoId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun clearPlaylistSongs(playlistId: Long) {
    val _sql: String = "DELETE FROM playlist_songs WHERE playlistId = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, playlistId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun clearPlaylistVideos(playlistId: Long) {
    val _sql: String = "DELETE FROM playlist_videos WHERE playlistId = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, playlistId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun updateSongPosition(
    playlistId: Long,
    songId: Long,
    position: Int,
  ) {
    val _sql: String = "UPDATE playlist_songs SET position = ? WHERE playlistId = ? AND songId = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, position.toLong())
        _argIndex = 2
        _stmt.bindLong(_argIndex, playlistId)
        _argIndex = 3
        _stmt.bindLong(_argIndex, songId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
