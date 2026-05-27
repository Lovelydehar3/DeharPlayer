package com.dehar.player.core.`data`.database

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.dehar.player.core.`data`.model.BookmarkEntity
import com.dehar.player.core.`data`.model.MediaType
import javax.`annotation`.processing.Generated
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
public class BookmarkDao_Impl(
  __db: RoomDatabase,
) : BookmarkDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfBookmarkEntity: EntityInsertAdapter<BookmarkEntity>

  private val __converters: Converters = Converters()

  private val __deleteAdapterOfBookmarkEntity: EntityDeleteOrUpdateAdapter<BookmarkEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfBookmarkEntity = object : EntityInsertAdapter<BookmarkEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `bookmarks` (`id`,`mediaId`,`mediaType`,`positionMs`,`label`,`createdAt`,`thumbnailPath`) VALUES (nullif(?, 0),?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: BookmarkEntity) {
        statement.bindLong(1, entity.id)
        statement.bindLong(2, entity.mediaId)
        val _tmp: String = __converters.fromMediaType(entity.mediaType)
        statement.bindText(3, _tmp)
        statement.bindLong(4, entity.positionMs)
        statement.bindText(5, entity.label)
        statement.bindLong(6, entity.createdAt)
        val _tmpThumbnailPath: String? = entity.thumbnailPath
        if (_tmpThumbnailPath == null) {
          statement.bindNull(7)
        } else {
          statement.bindText(7, _tmpThumbnailPath)
        }
      }
    }
    this.__deleteAdapterOfBookmarkEntity = object : EntityDeleteOrUpdateAdapter<BookmarkEntity>() {
      protected override fun createQuery(): String = "DELETE FROM `bookmarks` WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: BookmarkEntity) {
        statement.bindLong(1, entity.id)
      }
    }
  }

  public override suspend fun insertBookmark(bookmark: BookmarkEntity): Long =
      performSuspending(__db, false, true) { _connection ->
    val _result: Long = __insertAdapterOfBookmarkEntity.insertAndReturnId(_connection, bookmark)
    _result
  }

  public override suspend fun deleteBookmark(bookmark: BookmarkEntity): Unit =
      performSuspending(__db, false, true) { _connection ->
    __deleteAdapterOfBookmarkEntity.handle(_connection, bookmark)
  }

  public override fun getBookmarksForMedia(mediaId: Long, mediaType: MediaType):
      Flow<List<BookmarkEntity>> {
    val _sql: String =
        "SELECT * FROM bookmarks WHERE mediaId = ? AND mediaType = ? ORDER BY positionMs ASC"
    return createFlow(__db, false, arrayOf("bookmarks")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, mediaId)
        _argIndex = 2
        val _tmp: String = __converters.fromMediaType(mediaType)
        _stmt.bindText(_argIndex, _tmp)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfMediaId: Int = getColumnIndexOrThrow(_stmt, "mediaId")
        val _columnIndexOfMediaType: Int = getColumnIndexOrThrow(_stmt, "mediaType")
        val _columnIndexOfPositionMs: Int = getColumnIndexOrThrow(_stmt, "positionMs")
        val _columnIndexOfLabel: Int = getColumnIndexOrThrow(_stmt, "label")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfThumbnailPath: Int = getColumnIndexOrThrow(_stmt, "thumbnailPath")
        val _result: MutableList<BookmarkEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: BookmarkEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpMediaId: Long
          _tmpMediaId = _stmt.getLong(_columnIndexOfMediaId)
          val _tmpMediaType: MediaType
          val _tmp_1: String
          _tmp_1 = _stmt.getText(_columnIndexOfMediaType)
          _tmpMediaType = __converters.toMediaType(_tmp_1)
          val _tmpPositionMs: Long
          _tmpPositionMs = _stmt.getLong(_columnIndexOfPositionMs)
          val _tmpLabel: String
          _tmpLabel = _stmt.getText(_columnIndexOfLabel)
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          val _tmpThumbnailPath: String?
          if (_stmt.isNull(_columnIndexOfThumbnailPath)) {
            _tmpThumbnailPath = null
          } else {
            _tmpThumbnailPath = _stmt.getText(_columnIndexOfThumbnailPath)
          }
          _item =
              BookmarkEntity(_tmpId,_tmpMediaId,_tmpMediaType,_tmpPositionMs,_tmpLabel,_tmpCreatedAt,_tmpThumbnailPath)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getBookmarksForMediaOnce(mediaId: Long, mediaType: MediaType):
      List<BookmarkEntity> {
    val _sql: String =
        "SELECT * FROM bookmarks WHERE mediaId = ? AND mediaType = ? ORDER BY positionMs ASC"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, mediaId)
        _argIndex = 2
        val _tmp: String = __converters.fromMediaType(mediaType)
        _stmt.bindText(_argIndex, _tmp)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfMediaId: Int = getColumnIndexOrThrow(_stmt, "mediaId")
        val _columnIndexOfMediaType: Int = getColumnIndexOrThrow(_stmt, "mediaType")
        val _columnIndexOfPositionMs: Int = getColumnIndexOrThrow(_stmt, "positionMs")
        val _columnIndexOfLabel: Int = getColumnIndexOrThrow(_stmt, "label")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfThumbnailPath: Int = getColumnIndexOrThrow(_stmt, "thumbnailPath")
        val _result: MutableList<BookmarkEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: BookmarkEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpMediaId: Long
          _tmpMediaId = _stmt.getLong(_columnIndexOfMediaId)
          val _tmpMediaType: MediaType
          val _tmp_1: String
          _tmp_1 = _stmt.getText(_columnIndexOfMediaType)
          _tmpMediaType = __converters.toMediaType(_tmp_1)
          val _tmpPositionMs: Long
          _tmpPositionMs = _stmt.getLong(_columnIndexOfPositionMs)
          val _tmpLabel: String
          _tmpLabel = _stmt.getText(_columnIndexOfLabel)
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          val _tmpThumbnailPath: String?
          if (_stmt.isNull(_columnIndexOfThumbnailPath)) {
            _tmpThumbnailPath = null
          } else {
            _tmpThumbnailPath = _stmt.getText(_columnIndexOfThumbnailPath)
          }
          _item =
              BookmarkEntity(_tmpId,_tmpMediaId,_tmpMediaType,_tmpPositionMs,_tmpLabel,_tmpCreatedAt,_tmpThumbnailPath)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getBookmarkById(id: Long): BookmarkEntity? {
    val _sql: String = "SELECT * FROM bookmarks WHERE id = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfMediaId: Int = getColumnIndexOrThrow(_stmt, "mediaId")
        val _columnIndexOfMediaType: Int = getColumnIndexOrThrow(_stmt, "mediaType")
        val _columnIndexOfPositionMs: Int = getColumnIndexOrThrow(_stmt, "positionMs")
        val _columnIndexOfLabel: Int = getColumnIndexOrThrow(_stmt, "label")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfThumbnailPath: Int = getColumnIndexOrThrow(_stmt, "thumbnailPath")
        val _result: BookmarkEntity?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpMediaId: Long
          _tmpMediaId = _stmt.getLong(_columnIndexOfMediaId)
          val _tmpMediaType: MediaType
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfMediaType)
          _tmpMediaType = __converters.toMediaType(_tmp)
          val _tmpPositionMs: Long
          _tmpPositionMs = _stmt.getLong(_columnIndexOfPositionMs)
          val _tmpLabel: String
          _tmpLabel = _stmt.getText(_columnIndexOfLabel)
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          val _tmpThumbnailPath: String?
          if (_stmt.isNull(_columnIndexOfThumbnailPath)) {
            _tmpThumbnailPath = null
          } else {
            _tmpThumbnailPath = _stmt.getText(_columnIndexOfThumbnailPath)
          }
          _result =
              BookmarkEntity(_tmpId,_tmpMediaId,_tmpMediaType,_tmpPositionMs,_tmpLabel,_tmpCreatedAt,_tmpThumbnailPath)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteAllBookmarksForMedia(mediaId: Long, mediaType: MediaType) {
    val _sql: String = "DELETE FROM bookmarks WHERE mediaId = ? AND mediaType = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, mediaId)
        _argIndex = 2
        val _tmp: String = __converters.fromMediaType(mediaType)
        _stmt.bindText(_argIndex, _tmp)
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
