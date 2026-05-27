package com.dehar.player.core.`data`.database

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.dehar.player.core.`data`.model.MediaType
import com.dehar.player.core.`data`.model.RecycleBinEntity
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
public class RecycleBinDao_Impl(
  __db: RoomDatabase,
) : RecycleBinDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfRecycleBinEntity: EntityInsertAdapter<RecycleBinEntity>

  private val __converters: Converters = Converters()

  private val __deleteAdapterOfRecycleBinEntity: EntityDeleteOrUpdateAdapter<RecycleBinEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfRecycleBinEntity = object : EntityInsertAdapter<RecycleBinEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `recycle_bin` (`id`,`originalPath`,`originalName`,`mediaType`,`size`,`deletedAt`,`thumbnailPath`,`restorePath`) VALUES (?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: RecycleBinEntity) {
        statement.bindText(1, entity.id)
        statement.bindText(2, entity.originalPath)
        statement.bindText(3, entity.originalName)
        val _tmp: String = __converters.fromMediaType(entity.mediaType)
        statement.bindText(4, _tmp)
        statement.bindLong(5, entity.size)
        statement.bindLong(6, entity.deletedAt)
        val _tmpThumbnailPath: String? = entity.thumbnailPath
        if (_tmpThumbnailPath == null) {
          statement.bindNull(7)
        } else {
          statement.bindText(7, _tmpThumbnailPath)
        }
        val _tmpRestorePath: String? = entity.restorePath
        if (_tmpRestorePath == null) {
          statement.bindNull(8)
        } else {
          statement.bindText(8, _tmpRestorePath)
        }
      }
    }
    this.__deleteAdapterOfRecycleBinEntity = object :
        EntityDeleteOrUpdateAdapter<RecycleBinEntity>() {
      protected override fun createQuery(): String = "DELETE FROM `recycle_bin` WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: RecycleBinEntity) {
        statement.bindText(1, entity.id)
      }
    }
  }

  public override suspend fun insertDeletedItem(item: RecycleBinEntity): Unit =
      performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfRecycleBinEntity.insert(_connection, item)
  }

  public override suspend fun deleteDeletedItem(item: RecycleBinEntity): Unit =
      performSuspending(__db, false, true) { _connection ->
    __deleteAdapterOfRecycleBinEntity.handle(_connection, item)
  }

  public override fun getAllDeletedItems(): Flow<List<RecycleBinEntity>> {
    val _sql: String = "SELECT * FROM recycle_bin ORDER BY deletedAt DESC"
    return createFlow(__db, false, arrayOf("recycle_bin")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfOriginalPath: Int = getColumnIndexOrThrow(_stmt, "originalPath")
        val _columnIndexOfOriginalName: Int = getColumnIndexOrThrow(_stmt, "originalName")
        val _columnIndexOfMediaType: Int = getColumnIndexOrThrow(_stmt, "mediaType")
        val _columnIndexOfSize: Int = getColumnIndexOrThrow(_stmt, "size")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _columnIndexOfThumbnailPath: Int = getColumnIndexOrThrow(_stmt, "thumbnailPath")
        val _columnIndexOfRestorePath: Int = getColumnIndexOrThrow(_stmt, "restorePath")
        val _result: MutableList<RecycleBinEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: RecycleBinEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpOriginalPath: String
          _tmpOriginalPath = _stmt.getText(_columnIndexOfOriginalPath)
          val _tmpOriginalName: String
          _tmpOriginalName = _stmt.getText(_columnIndexOfOriginalName)
          val _tmpMediaType: MediaType
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfMediaType)
          _tmpMediaType = __converters.toMediaType(_tmp)
          val _tmpSize: Long
          _tmpSize = _stmt.getLong(_columnIndexOfSize)
          val _tmpDeletedAt: Long
          _tmpDeletedAt = _stmt.getLong(_columnIndexOfDeletedAt)
          val _tmpThumbnailPath: String?
          if (_stmt.isNull(_columnIndexOfThumbnailPath)) {
            _tmpThumbnailPath = null
          } else {
            _tmpThumbnailPath = _stmt.getText(_columnIndexOfThumbnailPath)
          }
          val _tmpRestorePath: String?
          if (_stmt.isNull(_columnIndexOfRestorePath)) {
            _tmpRestorePath = null
          } else {
            _tmpRestorePath = _stmt.getText(_columnIndexOfRestorePath)
          }
          _item =
              RecycleBinEntity(_tmpId,_tmpOriginalPath,_tmpOriginalName,_tmpMediaType,_tmpSize,_tmpDeletedAt,_tmpThumbnailPath,_tmpRestorePath)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getDeletedItemById(id: String): RecycleBinEntity? {
    val _sql: String = "SELECT * FROM recycle_bin WHERE id = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfOriginalPath: Int = getColumnIndexOrThrow(_stmt, "originalPath")
        val _columnIndexOfOriginalName: Int = getColumnIndexOrThrow(_stmt, "originalName")
        val _columnIndexOfMediaType: Int = getColumnIndexOrThrow(_stmt, "mediaType")
        val _columnIndexOfSize: Int = getColumnIndexOrThrow(_stmt, "size")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _columnIndexOfThumbnailPath: Int = getColumnIndexOrThrow(_stmt, "thumbnailPath")
        val _columnIndexOfRestorePath: Int = getColumnIndexOrThrow(_stmt, "restorePath")
        val _result: RecycleBinEntity?
        if (_stmt.step()) {
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpOriginalPath: String
          _tmpOriginalPath = _stmt.getText(_columnIndexOfOriginalPath)
          val _tmpOriginalName: String
          _tmpOriginalName = _stmt.getText(_columnIndexOfOriginalName)
          val _tmpMediaType: MediaType
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfMediaType)
          _tmpMediaType = __converters.toMediaType(_tmp)
          val _tmpSize: Long
          _tmpSize = _stmt.getLong(_columnIndexOfSize)
          val _tmpDeletedAt: Long
          _tmpDeletedAt = _stmt.getLong(_columnIndexOfDeletedAt)
          val _tmpThumbnailPath: String?
          if (_stmt.isNull(_columnIndexOfThumbnailPath)) {
            _tmpThumbnailPath = null
          } else {
            _tmpThumbnailPath = _stmt.getText(_columnIndexOfThumbnailPath)
          }
          val _tmpRestorePath: String?
          if (_stmt.isNull(_columnIndexOfRestorePath)) {
            _tmpRestorePath = null
          } else {
            _tmpRestorePath = _stmt.getText(_columnIndexOfRestorePath)
          }
          _result =
              RecycleBinEntity(_tmpId,_tmpOriginalPath,_tmpOriginalName,_tmpMediaType,_tmpSize,_tmpDeletedAt,_tmpThumbnailPath,_tmpRestorePath)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getItemsOlderThan(timestamp: Long): List<RecycleBinEntity> {
    val _sql: String = "SELECT * FROM recycle_bin WHERE deletedAt < ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, timestamp)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfOriginalPath: Int = getColumnIndexOrThrow(_stmt, "originalPath")
        val _columnIndexOfOriginalName: Int = getColumnIndexOrThrow(_stmt, "originalName")
        val _columnIndexOfMediaType: Int = getColumnIndexOrThrow(_stmt, "mediaType")
        val _columnIndexOfSize: Int = getColumnIndexOrThrow(_stmt, "size")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _columnIndexOfThumbnailPath: Int = getColumnIndexOrThrow(_stmt, "thumbnailPath")
        val _columnIndexOfRestorePath: Int = getColumnIndexOrThrow(_stmt, "restorePath")
        val _result: MutableList<RecycleBinEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: RecycleBinEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpOriginalPath: String
          _tmpOriginalPath = _stmt.getText(_columnIndexOfOriginalPath)
          val _tmpOriginalName: String
          _tmpOriginalName = _stmt.getText(_columnIndexOfOriginalName)
          val _tmpMediaType: MediaType
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfMediaType)
          _tmpMediaType = __converters.toMediaType(_tmp)
          val _tmpSize: Long
          _tmpSize = _stmt.getLong(_columnIndexOfSize)
          val _tmpDeletedAt: Long
          _tmpDeletedAt = _stmt.getLong(_columnIndexOfDeletedAt)
          val _tmpThumbnailPath: String?
          if (_stmt.isNull(_columnIndexOfThumbnailPath)) {
            _tmpThumbnailPath = null
          } else {
            _tmpThumbnailPath = _stmt.getText(_columnIndexOfThumbnailPath)
          }
          val _tmpRestorePath: String?
          if (_stmt.isNull(_columnIndexOfRestorePath)) {
            _tmpRestorePath = null
          } else {
            _tmpRestorePath = _stmt.getText(_columnIndexOfRestorePath)
          }
          _item =
              RecycleBinEntity(_tmpId,_tmpOriginalPath,_tmpOriginalName,_tmpMediaType,_tmpSize,_tmpDeletedAt,_tmpThumbnailPath,_tmpRestorePath)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getItemCount(): Flow<Int> {
    val _sql: String = "SELECT COUNT(*) FROM recycle_bin"
    return createFlow(__db, false, arrayOf("recycle_bin")) { _connection ->
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

  public override suspend fun deleteById(id: String) {
    val _sql: String = "DELETE FROM recycle_bin WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, id)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteItemsOlderThan(timestamp: Long) {
    val _sql: String = "DELETE FROM recycle_bin WHERE deletedAt < ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, timestamp)
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
