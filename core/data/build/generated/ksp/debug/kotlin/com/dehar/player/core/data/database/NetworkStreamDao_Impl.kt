package com.dehar.player.core.`data`.database

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.dehar.player.core.`data`.model.MediaType
import com.dehar.player.core.`data`.model.NetworkStreamEntity
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
public class NetworkStreamDao_Impl(
  __db: RoomDatabase,
) : NetworkStreamDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfNetworkStreamEntity: EntityInsertAdapter<NetworkStreamEntity>

  private val __converters: Converters = Converters()

  private val __deleteAdapterOfNetworkStreamEntity: EntityDeleteOrUpdateAdapter<NetworkStreamEntity>

  private val __updateAdapterOfNetworkStreamEntity: EntityDeleteOrUpdateAdapter<NetworkStreamEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfNetworkStreamEntity = object : EntityInsertAdapter<NetworkStreamEntity>()
        {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `network_streams` (`id`,`url`,`title`,`addedAt`,`lastPlayedAt`,`mediaType`,`headers`) VALUES (nullif(?, 0),?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: NetworkStreamEntity) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.url)
        statement.bindText(3, entity.title)
        statement.bindLong(4, entity.addedAt)
        val _tmpLastPlayedAt: Long? = entity.lastPlayedAt
        if (_tmpLastPlayedAt == null) {
          statement.bindNull(5)
        } else {
          statement.bindLong(5, _tmpLastPlayedAt)
        }
        val _tmp: String = __converters.fromMediaType(entity.mediaType)
        statement.bindText(6, _tmp)
        statement.bindText(7, entity.headers)
      }
    }
    this.__deleteAdapterOfNetworkStreamEntity = object :
        EntityDeleteOrUpdateAdapter<NetworkStreamEntity>() {
      protected override fun createQuery(): String = "DELETE FROM `network_streams` WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: NetworkStreamEntity) {
        statement.bindLong(1, entity.id)
      }
    }
    this.__updateAdapterOfNetworkStreamEntity = object :
        EntityDeleteOrUpdateAdapter<NetworkStreamEntity>() {
      protected override fun createQuery(): String =
          "UPDATE OR ABORT `network_streams` SET `id` = ?,`url` = ?,`title` = ?,`addedAt` = ?,`lastPlayedAt` = ?,`mediaType` = ?,`headers` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: NetworkStreamEntity) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.url)
        statement.bindText(3, entity.title)
        statement.bindLong(4, entity.addedAt)
        val _tmpLastPlayedAt: Long? = entity.lastPlayedAt
        if (_tmpLastPlayedAt == null) {
          statement.bindNull(5)
        } else {
          statement.bindLong(5, _tmpLastPlayedAt)
        }
        val _tmp: String = __converters.fromMediaType(entity.mediaType)
        statement.bindText(6, _tmp)
        statement.bindText(7, entity.headers)
        statement.bindLong(8, entity.id)
      }
    }
  }

  public override suspend fun insertStream(stream: NetworkStreamEntity): Long =
      performSuspending(__db, false, true) { _connection ->
    val _result: Long = __insertAdapterOfNetworkStreamEntity.insertAndReturnId(_connection, stream)
    _result
  }

  public override suspend fun deleteStream(stream: NetworkStreamEntity): Unit =
      performSuspending(__db, false, true) { _connection ->
    __deleteAdapterOfNetworkStreamEntity.handle(_connection, stream)
  }

  public override suspend fun updateStream(stream: NetworkStreamEntity): Unit =
      performSuspending(__db, false, true) { _connection ->
    __updateAdapterOfNetworkStreamEntity.handle(_connection, stream)
  }

  public override fun getAllStreams(): Flow<List<NetworkStreamEntity>> {
    val _sql: String = "SELECT * FROM network_streams ORDER BY addedAt DESC"
    return createFlow(__db, false, arrayOf("network_streams")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfUrl: Int = getColumnIndexOrThrow(_stmt, "url")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfAddedAt: Int = getColumnIndexOrThrow(_stmt, "addedAt")
        val _columnIndexOfLastPlayedAt: Int = getColumnIndexOrThrow(_stmt, "lastPlayedAt")
        val _columnIndexOfMediaType: Int = getColumnIndexOrThrow(_stmt, "mediaType")
        val _columnIndexOfHeaders: Int = getColumnIndexOrThrow(_stmt, "headers")
        val _result: MutableList<NetworkStreamEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: NetworkStreamEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpUrl: String
          _tmpUrl = _stmt.getText(_columnIndexOfUrl)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpAddedAt: Long
          _tmpAddedAt = _stmt.getLong(_columnIndexOfAddedAt)
          val _tmpLastPlayedAt: Long?
          if (_stmt.isNull(_columnIndexOfLastPlayedAt)) {
            _tmpLastPlayedAt = null
          } else {
            _tmpLastPlayedAt = _stmt.getLong(_columnIndexOfLastPlayedAt)
          }
          val _tmpMediaType: MediaType
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfMediaType)
          _tmpMediaType = __converters.toMediaType(_tmp)
          val _tmpHeaders: String
          _tmpHeaders = _stmt.getText(_columnIndexOfHeaders)
          _item =
              NetworkStreamEntity(_tmpId,_tmpUrl,_tmpTitle,_tmpAddedAt,_tmpLastPlayedAt,_tmpMediaType,_tmpHeaders)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getStreamById(id: Long): NetworkStreamEntity? {
    val _sql: String = "SELECT * FROM network_streams WHERE id = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfUrl: Int = getColumnIndexOrThrow(_stmt, "url")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfAddedAt: Int = getColumnIndexOrThrow(_stmt, "addedAt")
        val _columnIndexOfLastPlayedAt: Int = getColumnIndexOrThrow(_stmt, "lastPlayedAt")
        val _columnIndexOfMediaType: Int = getColumnIndexOrThrow(_stmt, "mediaType")
        val _columnIndexOfHeaders: Int = getColumnIndexOrThrow(_stmt, "headers")
        val _result: NetworkStreamEntity?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpUrl: String
          _tmpUrl = _stmt.getText(_columnIndexOfUrl)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpAddedAt: Long
          _tmpAddedAt = _stmt.getLong(_columnIndexOfAddedAt)
          val _tmpLastPlayedAt: Long?
          if (_stmt.isNull(_columnIndexOfLastPlayedAt)) {
            _tmpLastPlayedAt = null
          } else {
            _tmpLastPlayedAt = _stmt.getLong(_columnIndexOfLastPlayedAt)
          }
          val _tmpMediaType: MediaType
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfMediaType)
          _tmpMediaType = __converters.toMediaType(_tmp)
          val _tmpHeaders: String
          _tmpHeaders = _stmt.getText(_columnIndexOfHeaders)
          _result =
              NetworkStreamEntity(_tmpId,_tmpUrl,_tmpTitle,_tmpAddedAt,_tmpLastPlayedAt,_tmpMediaType,_tmpHeaders)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun updateLastPlayed(id: Long, timestamp: Long) {
    val _sql: String = "UPDATE network_streams SET lastPlayedAt = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, timestamp)
        _argIndex = 2
        _stmt.bindLong(_argIndex, id)
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
