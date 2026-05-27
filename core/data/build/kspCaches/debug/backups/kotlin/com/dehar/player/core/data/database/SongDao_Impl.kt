package com.dehar.player.core.`data`.database

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.dehar.player.core.`data`.model.SongEntity
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
public class SongDao_Impl(
  __db: RoomDatabase,
) : SongDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfSongEntity: EntityInsertAdapter<SongEntity>

  private val __deleteAdapterOfSongEntity: EntityDeleteOrUpdateAdapter<SongEntity>

  private val __updateAdapterOfSongEntity: EntityDeleteOrUpdateAdapter<SongEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfSongEntity = object : EntityInsertAdapter<SongEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `songs` (`id`,`title`,`artist`,`album`,`albumArtist`,`genre`,`path`,`size`,`duration`,`trackNumber`,`discNumber`,`year`,`dateAdded`,`dateModified`,`albumId`,`artistId`,`bitrate`,`sampleRate`,`mimeType`,`lastPlayedAt`,`lastPlayedPosition`,`playCount`,`isFavorite`,`lyricsPath`,`embeddedLyrics`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: SongEntity) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.title)
        statement.bindText(3, entity.artist)
        statement.bindText(4, entity.album)
        statement.bindText(5, entity.albumArtist)
        statement.bindText(6, entity.genre)
        statement.bindText(7, entity.path)
        statement.bindLong(8, entity.size)
        statement.bindLong(9, entity.duration)
        statement.bindLong(10, entity.trackNumber.toLong())
        statement.bindLong(11, entity.discNumber.toLong())
        statement.bindLong(12, entity.year.toLong())
        statement.bindLong(13, entity.dateAdded)
        statement.bindLong(14, entity.dateModified)
        statement.bindLong(15, entity.albumId)
        statement.bindLong(16, entity.artistId)
        statement.bindLong(17, entity.bitrate)
        statement.bindLong(18, entity.sampleRate.toLong())
        statement.bindText(19, entity.mimeType)
        statement.bindLong(20, entity.lastPlayedAt)
        statement.bindLong(21, entity.lastPlayedPosition)
        statement.bindLong(22, entity.playCount.toLong())
        val _tmp: Int = if (entity.isFavorite) 1 else 0
        statement.bindLong(23, _tmp.toLong())
        val _tmpLyricsPath: String? = entity.lyricsPath
        if (_tmpLyricsPath == null) {
          statement.bindNull(24)
        } else {
          statement.bindText(24, _tmpLyricsPath)
        }
        val _tmpEmbeddedLyrics: String? = entity.embeddedLyrics
        if (_tmpEmbeddedLyrics == null) {
          statement.bindNull(25)
        } else {
          statement.bindText(25, _tmpEmbeddedLyrics)
        }
      }
    }
    this.__deleteAdapterOfSongEntity = object : EntityDeleteOrUpdateAdapter<SongEntity>() {
      protected override fun createQuery(): String = "DELETE FROM `songs` WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: SongEntity) {
        statement.bindLong(1, entity.id)
      }
    }
    this.__updateAdapterOfSongEntity = object : EntityDeleteOrUpdateAdapter<SongEntity>() {
      protected override fun createQuery(): String =
          "UPDATE OR ABORT `songs` SET `id` = ?,`title` = ?,`artist` = ?,`album` = ?,`albumArtist` = ?,`genre` = ?,`path` = ?,`size` = ?,`duration` = ?,`trackNumber` = ?,`discNumber` = ?,`year` = ?,`dateAdded` = ?,`dateModified` = ?,`albumId` = ?,`artistId` = ?,`bitrate` = ?,`sampleRate` = ?,`mimeType` = ?,`lastPlayedAt` = ?,`lastPlayedPosition` = ?,`playCount` = ?,`isFavorite` = ?,`lyricsPath` = ?,`embeddedLyrics` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: SongEntity) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.title)
        statement.bindText(3, entity.artist)
        statement.bindText(4, entity.album)
        statement.bindText(5, entity.albumArtist)
        statement.bindText(6, entity.genre)
        statement.bindText(7, entity.path)
        statement.bindLong(8, entity.size)
        statement.bindLong(9, entity.duration)
        statement.bindLong(10, entity.trackNumber.toLong())
        statement.bindLong(11, entity.discNumber.toLong())
        statement.bindLong(12, entity.year.toLong())
        statement.bindLong(13, entity.dateAdded)
        statement.bindLong(14, entity.dateModified)
        statement.bindLong(15, entity.albumId)
        statement.bindLong(16, entity.artistId)
        statement.bindLong(17, entity.bitrate)
        statement.bindLong(18, entity.sampleRate.toLong())
        statement.bindText(19, entity.mimeType)
        statement.bindLong(20, entity.lastPlayedAt)
        statement.bindLong(21, entity.lastPlayedPosition)
        statement.bindLong(22, entity.playCount.toLong())
        val _tmp: Int = if (entity.isFavorite) 1 else 0
        statement.bindLong(23, _tmp.toLong())
        val _tmpLyricsPath: String? = entity.lyricsPath
        if (_tmpLyricsPath == null) {
          statement.bindNull(24)
        } else {
          statement.bindText(24, _tmpLyricsPath)
        }
        val _tmpEmbeddedLyrics: String? = entity.embeddedLyrics
        if (_tmpEmbeddedLyrics == null) {
          statement.bindNull(25)
        } else {
          statement.bindText(25, _tmpEmbeddedLyrics)
        }
        statement.bindLong(26, entity.id)
      }
    }
  }

  public override suspend fun insertSong(song: SongEntity): Unit = performSuspending(__db, false,
      true) { _connection ->
    __insertAdapterOfSongEntity.insert(_connection, song)
  }

  public override suspend fun insertAllSongs(songs: List<SongEntity>): Unit =
      performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfSongEntity.insert(_connection, songs)
  }

  public override suspend fun deleteSong(song: SongEntity): Unit = performSuspending(__db, false,
      true) { _connection ->
    __deleteAdapterOfSongEntity.handle(_connection, song)
  }

  public override suspend fun updateSong(song: SongEntity): Unit = performSuspending(__db, false,
      true) { _connection ->
    __updateAdapterOfSongEntity.handle(_connection, song)
  }

  public override fun getAllSongs(): Flow<List<SongEntity>> {
    val _sql: String = "SELECT * FROM songs ORDER BY title ASC"
    return createFlow(__db, false, arrayOf("songs")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
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

  public override suspend fun getAllSongsOnce(): List<SongEntity> {
    val _sql: String = "SELECT * FROM songs ORDER BY title ASC"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
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

  public override fun getSongById(id: Long): Flow<SongEntity?> {
    val _sql: String = "SELECT * FROM songs WHERE id = ?"
    return createFlow(__db, false, arrayOf("songs")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, id)
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
        val _result: SongEntity?
        if (_stmt.step()) {
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
          _result =
              SongEntity(_tmpId,_tmpTitle,_tmpArtist,_tmpAlbum,_tmpAlbumArtist,_tmpGenre,_tmpPath,_tmpSize,_tmpDuration,_tmpTrackNumber,_tmpDiscNumber,_tmpYear,_tmpDateAdded,_tmpDateModified,_tmpAlbumId,_tmpArtistId,_tmpBitrate,_tmpSampleRate,_tmpMimeType,_tmpLastPlayedAt,_tmpLastPlayedPosition,_tmpPlayCount,_tmpIsFavorite,_tmpLyricsPath,_tmpEmbeddedLyrics)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getSongByIdOnce(id: Long): SongEntity? {
    val _sql: String = "SELECT * FROM songs WHERE id = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, id)
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
        val _result: SongEntity?
        if (_stmt.step()) {
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
          _result =
              SongEntity(_tmpId,_tmpTitle,_tmpArtist,_tmpAlbum,_tmpAlbumArtist,_tmpGenre,_tmpPath,_tmpSize,_tmpDuration,_tmpTrackNumber,_tmpDiscNumber,_tmpYear,_tmpDateAdded,_tmpDateModified,_tmpAlbumId,_tmpArtistId,_tmpBitrate,_tmpSampleRate,_tmpMimeType,_tmpLastPlayedAt,_tmpLastPlayedPosition,_tmpPlayCount,_tmpIsFavorite,_tmpLyricsPath,_tmpEmbeddedLyrics)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getSongsByAlbum(albumId: Long): Flow<List<SongEntity>> {
    val _sql: String = "SELECT * FROM songs WHERE albumId = ? ORDER BY trackNumber ASC"
    return createFlow(__db, false, arrayOf("songs")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, albumId)
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

  public override fun getSongsByArtist(artistId: Long): Flow<List<SongEntity>> {
    val _sql: String = "SELECT * FROM songs WHERE artistId = ? ORDER BY title ASC"
    return createFlow(__db, false, arrayOf("songs")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, artistId)
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

  public override fun getSongsByGenre(genre: String): Flow<List<SongEntity>> {
    val _sql: String = "SELECT * FROM songs WHERE genre = ? ORDER BY title ASC"
    return createFlow(__db, false, arrayOf("songs")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, genre)
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

  public override fun getFavoriteSongs(): Flow<List<SongEntity>> {
    val _sql: String = "SELECT * FROM songs WHERE isFavorite = 1 ORDER BY title ASC"
    return createFlow(__db, false, arrayOf("songs")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
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

  public override fun getRecentlyPlayedSongs(limit: Int): Flow<List<SongEntity>> {
    val _sql: String =
        "SELECT * FROM songs WHERE lastPlayedAt > 0 ORDER BY lastPlayedAt DESC LIMIT ?"
    return createFlow(__db, false, arrayOf("songs")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, limit.toLong())
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

  public override fun getMostPlayedSongs(minPlays: Int, limit: Int): Flow<List<SongEntity>> {
    val _sql: String = "SELECT * FROM songs WHERE playCount >= ? ORDER BY playCount DESC LIMIT ?"
    return createFlow(__db, false, arrayOf("songs")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, minPlays.toLong())
        _argIndex = 2
        _stmt.bindLong(_argIndex, limit.toLong())
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

  public override fun getRecentlyAddedSongs(timestamp: Long): Flow<List<SongEntity>> {
    val _sql: String = "SELECT * FROM songs WHERE dateAdded >= ? ORDER BY dateAdded DESC"
    return createFlow(__db, false, arrayOf("songs")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, timestamp)
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

  public override fun searchSongs(query: String): Flow<List<SongEntity>> {
    val _sql: String =
        "SELECT * FROM songs WHERE title LIKE '%' || ? || '%' OR artist LIKE '%' || ? || '%' OR album LIKE '%' || ? || '%'"
    return createFlow(__db, false, arrayOf("songs")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, query)
        _argIndex = 2
        _stmt.bindText(_argIndex, query)
        _argIndex = 3
        _stmt.bindText(_argIndex, query)
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

  public override suspend fun searchSongsOnce(query: String): List<SongEntity> {
    val _sql: String =
        "SELECT * FROM songs WHERE title LIKE '%' || ? || '%' OR artist LIKE '%' || ? || '%' OR album LIKE '%' || ? || '%'"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, query)
        _argIndex = 2
        _stmt.bindText(_argIndex, query)
        _argIndex = 3
        _stmt.bindText(_argIndex, query)
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

  public override fun getAllAlbums(): Flow<List<AlbumInfo>> {
    val _sql: String =
        "SELECT DISTINCT album, albumArtist FROM songs WHERE album != '' AND album != '<unknown>' ORDER BY album ASC"
    return createFlow(__db, false, arrayOf("songs")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfAlbum: Int = 0
        val _columnIndexOfAlbumArtist: Int = 1
        val _result: MutableList<AlbumInfo> = mutableListOf()
        while (_stmt.step()) {
          val _item: AlbumInfo
          val _tmpAlbum: String
          _tmpAlbum = _stmt.getText(_columnIndexOfAlbum)
          val _tmpAlbumArtist: String
          _tmpAlbumArtist = _stmt.getText(_columnIndexOfAlbumArtist)
          _item = AlbumInfo(_tmpAlbum,_tmpAlbumArtist)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getAllArtists(): Flow<List<ArtistInfo>> {
    val _sql: String =
        "SELECT DISTINCT artistId, artist FROM songs WHERE artist != '' AND artist != '<unknown>' ORDER BY artist ASC"
    return createFlow(__db, false, arrayOf("songs")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfArtistId: Int = 0
        val _columnIndexOfArtist: Int = 1
        val _result: MutableList<ArtistInfo> = mutableListOf()
        while (_stmt.step()) {
          val _item: ArtistInfo
          val _tmpArtistId: Long
          _tmpArtistId = _stmt.getLong(_columnIndexOfArtistId)
          val _tmpArtist: String
          _tmpArtist = _stmt.getText(_columnIndexOfArtist)
          _item = ArtistInfo(_tmpArtistId,_tmpArtist)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getAllGenres(): Flow<List<String>> {
    val _sql: String =
        "SELECT DISTINCT genre FROM songs WHERE genre != '' AND genre != '<unknown>' ORDER BY genre ASC"
    return createFlow(__db, false, arrayOf("songs")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _result: MutableList<String> = mutableListOf()
        while (_stmt.step()) {
          val _item: String
          _item = _stmt.getText(0)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getSongCount(): Flow<Int> {
    val _sql: String = "SELECT COUNT(*) FROM songs"
    return createFlow(__db, false, arrayOf("songs")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
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

  public override suspend fun updatePlaybackState(
    id: Long,
    timestamp: Long,
    position: Long,
  ) {
    val _sql: String =
        "UPDATE songs SET lastPlayedAt = ?, lastPlayedPosition = ?, playCount = playCount + 1 WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, timestamp)
        _argIndex = 2
        _stmt.bindLong(_argIndex, position)
        _argIndex = 3
        _stmt.bindLong(_argIndex, id)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun updateFavoriteStatus(id: Long, isFavorite: Boolean) {
    val _sql: String = "UPDATE songs SET isFavorite = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        val _tmp: Int = if (isFavorite) 1 else 0
        _stmt.bindLong(_argIndex, _tmp.toLong())
        _argIndex = 2
        _stmt.bindLong(_argIndex, id)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteSongById(id: Long) {
    val _sql: String = "DELETE FROM songs WHERE id = ?"
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

  public override suspend fun deleteAllSongs() {
    val _sql: String = "DELETE FROM songs"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
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
