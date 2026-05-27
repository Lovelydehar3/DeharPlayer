package com.dehar.player.core.`data`.database

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.dehar.player.core.`data`.model.MediaType
import com.dehar.player.core.`data`.model.PrivateVaultItemEntity
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
public class PrivateVaultDao_Impl(
  __db: RoomDatabase,
) : PrivateVaultDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfPrivateVaultItemEntity: EntityInsertAdapter<PrivateVaultItemEntity>

  private val __converters: Converters = Converters()

  private val __deleteAdapterOfPrivateVaultItemEntity:
      EntityDeleteOrUpdateAdapter<PrivateVaultItemEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfPrivateVaultItemEntity = object :
        EntityInsertAdapter<PrivateVaultItemEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `private_vault` (`id`,`originalPath`,`encryptedPath`,`mediaType`,`addedAt`,`thumbnailPath`,`title`,`duration`) VALUES (?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: PrivateVaultItemEntity) {
        statement.bindText(1, entity.id)
        statement.bindText(2, entity.originalPath)
        statement.bindText(3, entity.encryptedPath)
        val _tmp: String = __converters.fromMediaType(entity.mediaType)
        statement.bindText(4, _tmp)
        statement.bindLong(5, entity.addedAt)
        val _tmpThumbnailPath: String? = entity.thumbnailPath
        if (_tmpThumbnailPath == null) {
          statement.bindNull(6)
        } else {
          statement.bindText(6, _tmpThumbnailPath)
        }
        statement.bindText(7, entity.title)
        statement.bindLong(8, entity.duration)
      }
    }
    this.__deleteAdapterOfPrivateVaultItemEntity = object :
        EntityDeleteOrUpdateAdapter<PrivateVaultItemEntity>() {
      protected override fun createQuery(): String = "DELETE FROM `private_vault` WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: PrivateVaultItemEntity) {
        statement.bindText(1, entity.id)
      }
    }
  }

  public override suspend fun insertVaultItem(item: PrivateVaultItemEntity): Unit =
      performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfPrivateVaultItemEntity.insert(_connection, item)
  }

  public override suspend fun deleteVaultItem(item: PrivateVaultItemEntity): Unit =
      performSuspending(__db, false, true) { _connection ->
    __deleteAdapterOfPrivateVaultItemEntity.handle(_connection, item)
  }

  public override fun getAllVaultItems(): Flow<List<PrivateVaultItemEntity>> {
    val _sql: String = "SELECT * FROM private_vault ORDER BY addedAt DESC"
    return createFlow(__db, false, arrayOf("private_vault")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfOriginalPath: Int = getColumnIndexOrThrow(_stmt, "originalPath")
        val _columnIndexOfEncryptedPath: Int = getColumnIndexOrThrow(_stmt, "encryptedPath")
        val _columnIndexOfMediaType: Int = getColumnIndexOrThrow(_stmt, "mediaType")
        val _columnIndexOfAddedAt: Int = getColumnIndexOrThrow(_stmt, "addedAt")
        val _columnIndexOfThumbnailPath: Int = getColumnIndexOrThrow(_stmt, "thumbnailPath")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDuration: Int = getColumnIndexOrThrow(_stmt, "duration")
        val _result: MutableList<PrivateVaultItemEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: PrivateVaultItemEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpOriginalPath: String
          _tmpOriginalPath = _stmt.getText(_columnIndexOfOriginalPath)
          val _tmpEncryptedPath: String
          _tmpEncryptedPath = _stmt.getText(_columnIndexOfEncryptedPath)
          val _tmpMediaType: MediaType
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfMediaType)
          _tmpMediaType = __converters.toMediaType(_tmp)
          val _tmpAddedAt: Long
          _tmpAddedAt = _stmt.getLong(_columnIndexOfAddedAt)
          val _tmpThumbnailPath: String?
          if (_stmt.isNull(_columnIndexOfThumbnailPath)) {
            _tmpThumbnailPath = null
          } else {
            _tmpThumbnailPath = _stmt.getText(_columnIndexOfThumbnailPath)
          }
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpDuration: Long
          _tmpDuration = _stmt.getLong(_columnIndexOfDuration)
          _item =
              PrivateVaultItemEntity(_tmpId,_tmpOriginalPath,_tmpEncryptedPath,_tmpMediaType,_tmpAddedAt,_tmpThumbnailPath,_tmpTitle,_tmpDuration)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getVaultItemById(id: String): PrivateVaultItemEntity? {
    val _sql: String = "SELECT * FROM private_vault WHERE id = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfOriginalPath: Int = getColumnIndexOrThrow(_stmt, "originalPath")
        val _columnIndexOfEncryptedPath: Int = getColumnIndexOrThrow(_stmt, "encryptedPath")
        val _columnIndexOfMediaType: Int = getColumnIndexOrThrow(_stmt, "mediaType")
        val _columnIndexOfAddedAt: Int = getColumnIndexOrThrow(_stmt, "addedAt")
        val _columnIndexOfThumbnailPath: Int = getColumnIndexOrThrow(_stmt, "thumbnailPath")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDuration: Int = getColumnIndexOrThrow(_stmt, "duration")
        val _result: PrivateVaultItemEntity?
        if (_stmt.step()) {
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpOriginalPath: String
          _tmpOriginalPath = _stmt.getText(_columnIndexOfOriginalPath)
          val _tmpEncryptedPath: String
          _tmpEncryptedPath = _stmt.getText(_columnIndexOfEncryptedPath)
          val _tmpMediaType: MediaType
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfMediaType)
          _tmpMediaType = __converters.toMediaType(_tmp)
          val _tmpAddedAt: Long
          _tmpAddedAt = _stmt.getLong(_columnIndexOfAddedAt)
          val _tmpThumbnailPath: String?
          if (_stmt.isNull(_columnIndexOfThumbnailPath)) {
            _tmpThumbnailPath = null
          } else {
            _tmpThumbnailPath = _stmt.getText(_columnIndexOfThumbnailPath)
          }
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpDuration: Long
          _tmpDuration = _stmt.getLong(_columnIndexOfDuration)
          _result =
              PrivateVaultItemEntity(_tmpId,_tmpOriginalPath,_tmpEncryptedPath,_tmpMediaType,_tmpAddedAt,_tmpThumbnailPath,_tmpTitle,_tmpDuration)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getVaultItemsByType(mediaType: MediaType):
      Flow<List<PrivateVaultItemEntity>> {
    val _sql: String = "SELECT * FROM private_vault WHERE mediaType = ? ORDER BY addedAt DESC"
    return createFlow(__db, false, arrayOf("private_vault")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        val _tmp: String = __converters.fromMediaType(mediaType)
        _stmt.bindText(_argIndex, _tmp)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfOriginalPath: Int = getColumnIndexOrThrow(_stmt, "originalPath")
        val _columnIndexOfEncryptedPath: Int = getColumnIndexOrThrow(_stmt, "encryptedPath")
        val _columnIndexOfMediaType: Int = getColumnIndexOrThrow(_stmt, "mediaType")
        val _columnIndexOfAddedAt: Int = getColumnIndexOrThrow(_stmt, "addedAt")
        val _columnIndexOfThumbnailPath: Int = getColumnIndexOrThrow(_stmt, "thumbnailPath")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDuration: Int = getColumnIndexOrThrow(_stmt, "duration")
        val _result: MutableList<PrivateVaultItemEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: PrivateVaultItemEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpOriginalPath: String
          _tmpOriginalPath = _stmt.getText(_columnIndexOfOriginalPath)
          val _tmpEncryptedPath: String
          _tmpEncryptedPath = _stmt.getText(_columnIndexOfEncryptedPath)
          val _tmpMediaType: MediaType
          val _tmp_1: String
          _tmp_1 = _stmt.getText(_columnIndexOfMediaType)
          _tmpMediaType = __converters.toMediaType(_tmp_1)
          val _tmpAddedAt: Long
          _tmpAddedAt = _stmt.getLong(_columnIndexOfAddedAt)
          val _tmpThumbnailPath: String?
          if (_stmt.isNull(_columnIndexOfThumbnailPath)) {
            _tmpThumbnailPath = null
          } else {
            _tmpThumbnailPath = _stmt.getText(_columnIndexOfThumbnailPath)
          }
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpDuration: Long
          _tmpDuration = _stmt.getLong(_columnIndexOfDuration)
          _item =
              PrivateVaultItemEntity(_tmpId,_tmpOriginalPath,_tmpEncryptedPath,_tmpMediaType,_tmpAddedAt,_tmpThumbnailPath,_tmpTitle,_tmpDuration)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getVaultItemCount(): Flow<Int> {
    val _sql: String = "SELECT COUNT(*) FROM private_vault"
    return createFlow(__db, false, arrayOf("private_vault")) { _connection ->
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

  public override suspend fun deleteVaultItemById(id: String) {
    val _sql: String = "DELETE FROM private_vault WHERE id = ?"
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

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
