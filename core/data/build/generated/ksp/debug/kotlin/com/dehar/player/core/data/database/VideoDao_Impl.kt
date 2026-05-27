package com.dehar.player.core.`data`.database

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performInTransactionSuspending
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
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
public class VideoDao_Impl(
  __db: RoomDatabase,
) : VideoDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfVideoEntity: EntityInsertAdapter<VideoEntity>

  private val __deleteAdapterOfVideoEntity: EntityDeleteOrUpdateAdapter<VideoEntity>

  private val __updateAdapterOfVideoEntity: EntityDeleteOrUpdateAdapter<VideoEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfVideoEntity = object : EntityInsertAdapter<VideoEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `videos` (`id`,`title`,`displayName`,`path`,`size`,`duration`,`width`,`height`,`mimeType`,`dateAdded`,`dateModified`,`bucketId`,`bucketName`,`resolution`,`frameRate`,`bitrate`,`codecName`,`lastPlayedAt`,`lastPlayedPosition`,`playCount`,`isFavorite`,`userRating`,`customThumbnailPath`,`isHidden`,`privateVaultId`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: VideoEntity) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.title)
        statement.bindText(3, entity.displayName)
        statement.bindText(4, entity.path)
        statement.bindLong(5, entity.size)
        statement.bindLong(6, entity.duration)
        statement.bindLong(7, entity.width.toLong())
        statement.bindLong(8, entity.height.toLong())
        statement.bindText(9, entity.mimeType)
        statement.bindLong(10, entity.dateAdded)
        statement.bindLong(11, entity.dateModified)
        statement.bindLong(12, entity.bucketId)
        statement.bindText(13, entity.bucketName)
        statement.bindText(14, entity.resolution)
        statement.bindLong(15, entity.frameRate.toLong())
        statement.bindLong(16, entity.bitrate)
        statement.bindText(17, entity.codecName)
        statement.bindLong(18, entity.lastPlayedAt)
        statement.bindLong(19, entity.lastPlayedPosition)
        statement.bindLong(20, entity.playCount.toLong())
        val _tmp: Int = if (entity.isFavorite) 1 else 0
        statement.bindLong(21, _tmp.toLong())
        statement.bindLong(22, entity.userRating.toLong())
        val _tmpCustomThumbnailPath: String? = entity.customThumbnailPath
        if (_tmpCustomThumbnailPath == null) {
          statement.bindNull(23)
        } else {
          statement.bindText(23, _tmpCustomThumbnailPath)
        }
        val _tmp_1: Int = if (entity.isHidden) 1 else 0
        statement.bindLong(24, _tmp_1.toLong())
        val _tmpPrivateVaultId: String? = entity.privateVaultId
        if (_tmpPrivateVaultId == null) {
          statement.bindNull(25)
        } else {
          statement.bindText(25, _tmpPrivateVaultId)
        }
      }
    }
    this.__deleteAdapterOfVideoEntity = object : EntityDeleteOrUpdateAdapter<VideoEntity>() {
      protected override fun createQuery(): String = "DELETE FROM `videos` WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: VideoEntity) {
        statement.bindLong(1, entity.id)
      }
    }
    this.__updateAdapterOfVideoEntity = object : EntityDeleteOrUpdateAdapter<VideoEntity>() {
      protected override fun createQuery(): String =
          "UPDATE OR ABORT `videos` SET `id` = ?,`title` = ?,`displayName` = ?,`path` = ?,`size` = ?,`duration` = ?,`width` = ?,`height` = ?,`mimeType` = ?,`dateAdded` = ?,`dateModified` = ?,`bucketId` = ?,`bucketName` = ?,`resolution` = ?,`frameRate` = ?,`bitrate` = ?,`codecName` = ?,`lastPlayedAt` = ?,`lastPlayedPosition` = ?,`playCount` = ?,`isFavorite` = ?,`userRating` = ?,`customThumbnailPath` = ?,`isHidden` = ?,`privateVaultId` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: VideoEntity) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.title)
        statement.bindText(3, entity.displayName)
        statement.bindText(4, entity.path)
        statement.bindLong(5, entity.size)
        statement.bindLong(6, entity.duration)
        statement.bindLong(7, entity.width.toLong())
        statement.bindLong(8, entity.height.toLong())
        statement.bindText(9, entity.mimeType)
        statement.bindLong(10, entity.dateAdded)
        statement.bindLong(11, entity.dateModified)
        statement.bindLong(12, entity.bucketId)
        statement.bindText(13, entity.bucketName)
        statement.bindText(14, entity.resolution)
        statement.bindLong(15, entity.frameRate.toLong())
        statement.bindLong(16, entity.bitrate)
        statement.bindText(17, entity.codecName)
        statement.bindLong(18, entity.lastPlayedAt)
        statement.bindLong(19, entity.lastPlayedPosition)
        statement.bindLong(20, entity.playCount.toLong())
        val _tmp: Int = if (entity.isFavorite) 1 else 0
        statement.bindLong(21, _tmp.toLong())
        statement.bindLong(22, entity.userRating.toLong())
        val _tmpCustomThumbnailPath: String? = entity.customThumbnailPath
        if (_tmpCustomThumbnailPath == null) {
          statement.bindNull(23)
        } else {
          statement.bindText(23, _tmpCustomThumbnailPath)
        }
        val _tmp_1: Int = if (entity.isHidden) 1 else 0
        statement.bindLong(24, _tmp_1.toLong())
        val _tmpPrivateVaultId: String? = entity.privateVaultId
        if (_tmpPrivateVaultId == null) {
          statement.bindNull(25)
        } else {
          statement.bindText(25, _tmpPrivateVaultId)
        }
        statement.bindLong(26, entity.id)
      }
    }
  }

  public override suspend fun insertVideo(video: VideoEntity): Unit = performSuspending(__db, false,
      true) { _connection ->
    __insertAdapterOfVideoEntity.insert(_connection, video)
  }

  public override suspend fun insertAllVideos(videos: List<VideoEntity>): Unit =
      performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfVideoEntity.insert(_connection, videos)
  }

  public override suspend fun deleteVideo(video: VideoEntity): Unit = performSuspending(__db, false,
      true) { _connection ->
    __deleteAdapterOfVideoEntity.handle(_connection, video)
  }

  public override suspend fun updateVideo(video: VideoEntity): Unit = performSuspending(__db, false,
      true) { _connection ->
    __updateAdapterOfVideoEntity.handle(_connection, video)
  }

  public override suspend fun upsertVideos(videos: List<VideoEntity>): Unit =
      performInTransactionSuspending(__db) {
    super@VideoDao_Impl.upsertVideos(videos)
  }

  public override fun getAllVideos(): Flow<List<VideoEntity>> {
    val _sql: String =
        "SELECT * FROM videos WHERE isHidden = 0 AND privateVaultId IS NULL ORDER BY dateAdded DESC"
    return createFlow(__db, false, arrayOf("videos")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
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

  public override suspend fun getAllVideosOnce(): List<VideoEntity> {
    val _sql: String =
        "SELECT * FROM videos WHERE isHidden = 0 AND privateVaultId IS NULL ORDER BY dateAdded DESC"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
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

  public override fun getVideosInFolder(folderId: Long): Flow<List<VideoEntity>> {
    val _sql: String =
        "SELECT * FROM videos WHERE bucketId = ? AND isHidden = 0 ORDER BY displayName ASC"
    return createFlow(__db, false, arrayOf("videos")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, folderId)
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

  public override fun getVideoById(id: Long): Flow<VideoEntity?> {
    val _sql: String = "SELECT * FROM videos WHERE id = ?"
    return createFlow(__db, false, arrayOf("videos")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, id)
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
        val _result: VideoEntity?
        if (_stmt.step()) {
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
          _result =
              VideoEntity(_tmpId,_tmpTitle,_tmpDisplayName,_tmpPath,_tmpSize,_tmpDuration,_tmpWidth,_tmpHeight,_tmpMimeType,_tmpDateAdded,_tmpDateModified,_tmpBucketId,_tmpBucketName,_tmpResolution,_tmpFrameRate,_tmpBitrate,_tmpCodecName,_tmpLastPlayedAt,_tmpLastPlayedPosition,_tmpPlayCount,_tmpIsFavorite,_tmpUserRating,_tmpCustomThumbnailPath,_tmpIsHidden,_tmpPrivateVaultId)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getVideoByIdOnce(id: Long): VideoEntity? {
    val _sql: String = "SELECT * FROM videos WHERE id = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, id)
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
        val _result: VideoEntity?
        if (_stmt.step()) {
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
          _result =
              VideoEntity(_tmpId,_tmpTitle,_tmpDisplayName,_tmpPath,_tmpSize,_tmpDuration,_tmpWidth,_tmpHeight,_tmpMimeType,_tmpDateAdded,_tmpDateModified,_tmpBucketId,_tmpBucketName,_tmpResolution,_tmpFrameRate,_tmpBitrate,_tmpCodecName,_tmpLastPlayedAt,_tmpLastPlayedPosition,_tmpPlayCount,_tmpIsFavorite,_tmpUserRating,_tmpCustomThumbnailPath,_tmpIsHidden,_tmpPrivateVaultId)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getVideoByPath(path: String): VideoEntity? {
    val _sql: String = "SELECT * FROM videos WHERE path = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, path)
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
        val _result: VideoEntity?
        if (_stmt.step()) {
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
          _result =
              VideoEntity(_tmpId,_tmpTitle,_tmpDisplayName,_tmpPath,_tmpSize,_tmpDuration,_tmpWidth,_tmpHeight,_tmpMimeType,_tmpDateAdded,_tmpDateModified,_tmpBucketId,_tmpBucketName,_tmpResolution,_tmpFrameRate,_tmpBitrate,_tmpCodecName,_tmpLastPlayedAt,_tmpLastPlayedPosition,_tmpPlayCount,_tmpIsFavorite,_tmpUserRating,_tmpCustomThumbnailPath,_tmpIsHidden,_tmpPrivateVaultId)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getVideoFolders(): Flow<List<FolderInfo>> {
    val _sql: String =
        "SELECT DISTINCT bucketId, bucketName FROM videos WHERE isHidden = 0 AND privateVaultId IS NULL ORDER BY bucketName ASC"
    return createFlow(__db, false, arrayOf("videos")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfBucketId: Int = 0
        val _columnIndexOfBucketName: Int = 1
        val _result: MutableList<FolderInfo> = mutableListOf()
        while (_stmt.step()) {
          val _item: FolderInfo
          val _tmpBucketId: Long
          _tmpBucketId = _stmt.getLong(_columnIndexOfBucketId)
          val _tmpBucketName: String
          _tmpBucketName = _stmt.getText(_columnIndexOfBucketName)
          _item = FolderInfo(_tmpBucketId,_tmpBucketName)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getFavoriteVideos(): Flow<List<VideoEntity>> {
    val _sql: String =
        "SELECT * FROM videos WHERE isFavorite = 1 AND isHidden = 0 ORDER BY lastPlayedAt DESC"
    return createFlow(__db, false, arrayOf("videos")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
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

  public override fun getRecentlyPlayedVideos(limit: Int): Flow<List<VideoEntity>> {
    val _sql: String =
        "SELECT * FROM videos WHERE lastPlayedAt > 0 AND isHidden = 0 ORDER BY lastPlayedAt DESC LIMIT ?"
    return createFlow(__db, false, arrayOf("videos")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, limit.toLong())
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

  public override fun get4KVideos(): Flow<List<VideoEntity>> {
    val _sql: String =
        "SELECT * FROM videos WHERE width >= 3840 AND isHidden = 0 ORDER BY dateAdded DESC"
    return createFlow(__db, false, arrayOf("videos")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
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

  public override fun getLongVideos(minDuration: Long): Flow<List<VideoEntity>> {
    val _sql: String =
        "SELECT * FROM videos WHERE duration >= ? AND isHidden = 0 ORDER BY dateAdded DESC"
    return createFlow(__db, false, arrayOf("videos")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, minDuration)
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

  public override fun getUnwatchedVideos(): Flow<List<VideoEntity>> {
    val _sql: String =
        "SELECT * FROM videos WHERE playCount = 0 AND duration > 120000 AND isHidden = 0 ORDER BY dateAdded DESC"
    return createFlow(__db, false, arrayOf("videos")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
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

  public override fun getRecentlyAddedVideos(timestamp: Long): Flow<List<VideoEntity>> {
    val _sql: String =
        "SELECT * FROM videos WHERE dateAdded >= ? AND isHidden = 0 ORDER BY dateAdded DESC"
    return createFlow(__db, false, arrayOf("videos")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, timestamp)
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

  public override fun searchVideos(query: String): Flow<List<VideoEntity>> {
    val _sql: String =
        "SELECT * FROM videos WHERE displayName LIKE '%' || ? || '%' OR title LIKE '%' || ? || '%' AND isHidden = 0"
    return createFlow(__db, false, arrayOf("videos")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, query)
        _argIndex = 2
        _stmt.bindText(_argIndex, query)
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

  public override suspend fun searchVideosOnce(query: String): List<VideoEntity> {
    val _sql: String =
        "SELECT * FROM videos WHERE displayName LIKE '%' || ? || '%' OR title LIKE '%' || ? || '%' AND isHidden = 0"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, query)
        _argIndex = 2
        _stmt.bindText(_argIndex, query)
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

  public override fun getVideoCount(): Flow<Int> {
    val _sql: String = "SELECT COUNT(*) FROM videos WHERE isHidden = 0 AND privateVaultId IS NULL"
    return createFlow(__db, false, arrayOf("videos")) { _connection ->
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

  public override fun getTotalSize(): Flow<Long> {
    val _sql: String =
        "SELECT COALESCE(SUM(size), 0) FROM videos WHERE isHidden = 0 AND privateVaultId IS NULL"
    return createFlow(__db, false, arrayOf("videos")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _result: Long
        if (_stmt.step()) {
          val _tmp: Long
          _tmp = _stmt.getLong(0)
          _result = _tmp
        } else {
          _result = 0L
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteVideoById(id: Long) {
    val _sql: String = "DELETE FROM videos WHERE id = ?"
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

  public override suspend fun updatePlaybackState(
    id: Long,
    timestamp: Long,
    position: Long,
  ) {
    val _sql: String =
        "UPDATE videos SET lastPlayedAt = ?, lastPlayedPosition = ?, playCount = playCount + 1 WHERE id = ?"
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
    val _sql: String = "UPDATE videos SET isFavorite = ? WHERE id = ?"
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

  public override suspend fun updateHiddenStatus(id: Long, isHidden: Boolean) {
    val _sql: String = "UPDATE videos SET isHidden = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        val _tmp: Int = if (isHidden) 1 else 0
        _stmt.bindLong(_argIndex, _tmp.toLong())
        _argIndex = 2
        _stmt.bindLong(_argIndex, id)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun updateRating(id: Long, rating: Int) {
    val _sql: String = "UPDATE videos SET userRating = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, rating.toLong())
        _argIndex = 2
        _stmt.bindLong(_argIndex, id)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteAllVideos() {
    val _sql: String = "DELETE FROM videos"
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
