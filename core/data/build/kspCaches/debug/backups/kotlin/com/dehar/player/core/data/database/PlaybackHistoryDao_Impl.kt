package com.dehar.player.core.`data`.database

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.dehar.player.core.`data`.model.MediaType
import com.dehar.player.core.`data`.model.PlaybackHistoryEntity
import javax.`annotation`.processing.Generated
import kotlin.Float
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class PlaybackHistoryDao_Impl(
  __db: RoomDatabase,
) : PlaybackHistoryDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfPlaybackHistoryEntity: EntityInsertAdapter<PlaybackHistoryEntity>

  private val __converters: Converters = Converters()
  init {
    this.__db = __db
    this.__insertAdapterOfPlaybackHistoryEntity = object :
        EntityInsertAdapter<PlaybackHistoryEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `playback_history` (`id`,`mediaId`,`mediaType`,`playedAt`,`durationPlayedMs`,`completionPercent`) VALUES (nullif(?, 0),?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: PlaybackHistoryEntity) {
        statement.bindLong(1, entity.id)
        statement.bindLong(2, entity.mediaId)
        val _tmp: String = __converters.fromMediaType(entity.mediaType)
        statement.bindText(3, _tmp)
        statement.bindLong(4, entity.playedAt)
        statement.bindLong(5, entity.durationPlayedMs)
        statement.bindDouble(6, entity.completionPercent.toDouble())
      }
    }
  }

  public override suspend fun insertHistory(history: PlaybackHistoryEntity): Long =
      performSuspending(__db, false, true) { _connection ->
    val _result: Long = __insertAdapterOfPlaybackHistoryEntity.insertAndReturnId(_connection,
        history)
    _result
  }

  public override fun getRecentHistory(limit: Int): Flow<List<PlaybackHistoryEntity>> {
    val _sql: String = "SELECT * FROM playback_history ORDER BY playedAt DESC LIMIT ?"
    return createFlow(__db, false, arrayOf("playback_history")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, limit.toLong())
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfMediaId: Int = getColumnIndexOrThrow(_stmt, "mediaId")
        val _columnIndexOfMediaType: Int = getColumnIndexOrThrow(_stmt, "mediaType")
        val _columnIndexOfPlayedAt: Int = getColumnIndexOrThrow(_stmt, "playedAt")
        val _columnIndexOfDurationPlayedMs: Int = getColumnIndexOrThrow(_stmt, "durationPlayedMs")
        val _columnIndexOfCompletionPercent: Int = getColumnIndexOrThrow(_stmt, "completionPercent")
        val _result: MutableList<PlaybackHistoryEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: PlaybackHistoryEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpMediaId: Long
          _tmpMediaId = _stmt.getLong(_columnIndexOfMediaId)
          val _tmpMediaType: MediaType
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfMediaType)
          _tmpMediaType = __converters.toMediaType(_tmp)
          val _tmpPlayedAt: Long
          _tmpPlayedAt = _stmt.getLong(_columnIndexOfPlayedAt)
          val _tmpDurationPlayedMs: Long
          _tmpDurationPlayedMs = _stmt.getLong(_columnIndexOfDurationPlayedMs)
          val _tmpCompletionPercent: Float
          _tmpCompletionPercent = _stmt.getDouble(_columnIndexOfCompletionPercent).toFloat()
          _item =
              PlaybackHistoryEntity(_tmpId,_tmpMediaId,_tmpMediaType,_tmpPlayedAt,_tmpDurationPlayedMs,_tmpCompletionPercent)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getLatestHistoryForMedia(mediaId: Long, mediaType: MediaType):
      PlaybackHistoryEntity? {
    val _sql: String =
        "SELECT * FROM playback_history WHERE mediaId = ? AND mediaType = ? ORDER BY playedAt DESC LIMIT 1"
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
        val _columnIndexOfPlayedAt: Int = getColumnIndexOrThrow(_stmt, "playedAt")
        val _columnIndexOfDurationPlayedMs: Int = getColumnIndexOrThrow(_stmt, "durationPlayedMs")
        val _columnIndexOfCompletionPercent: Int = getColumnIndexOrThrow(_stmt, "completionPercent")
        val _result: PlaybackHistoryEntity?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpMediaId: Long
          _tmpMediaId = _stmt.getLong(_columnIndexOfMediaId)
          val _tmpMediaType: MediaType
          val _tmp_1: String
          _tmp_1 = _stmt.getText(_columnIndexOfMediaType)
          _tmpMediaType = __converters.toMediaType(_tmp_1)
          val _tmpPlayedAt: Long
          _tmpPlayedAt = _stmt.getLong(_columnIndexOfPlayedAt)
          val _tmpDurationPlayedMs: Long
          _tmpDurationPlayedMs = _stmt.getLong(_columnIndexOfDurationPlayedMs)
          val _tmpCompletionPercent: Float
          _tmpCompletionPercent = _stmt.getDouble(_columnIndexOfCompletionPercent).toFloat()
          _result =
              PlaybackHistoryEntity(_tmpId,_tmpMediaId,_tmpMediaType,_tmpPlayedAt,_tmpDurationPlayedMs,_tmpCompletionPercent)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getHistoryOlderThan(timestamp: Long): List<PlaybackHistoryEntity> {
    val _sql: String = "SELECT * FROM playback_history WHERE playedAt < ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, timestamp)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfMediaId: Int = getColumnIndexOrThrow(_stmt, "mediaId")
        val _columnIndexOfMediaType: Int = getColumnIndexOrThrow(_stmt, "mediaType")
        val _columnIndexOfPlayedAt: Int = getColumnIndexOrThrow(_stmt, "playedAt")
        val _columnIndexOfDurationPlayedMs: Int = getColumnIndexOrThrow(_stmt, "durationPlayedMs")
        val _columnIndexOfCompletionPercent: Int = getColumnIndexOrThrow(_stmt, "completionPercent")
        val _result: MutableList<PlaybackHistoryEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: PlaybackHistoryEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpMediaId: Long
          _tmpMediaId = _stmt.getLong(_columnIndexOfMediaId)
          val _tmpMediaType: MediaType
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfMediaType)
          _tmpMediaType = __converters.toMediaType(_tmp)
          val _tmpPlayedAt: Long
          _tmpPlayedAt = _stmt.getLong(_columnIndexOfPlayedAt)
          val _tmpDurationPlayedMs: Long
          _tmpDurationPlayedMs = _stmt.getLong(_columnIndexOfDurationPlayedMs)
          val _tmpCompletionPercent: Float
          _tmpCompletionPercent = _stmt.getDouble(_columnIndexOfCompletionPercent).toFloat()
          _item =
              PlaybackHistoryEntity(_tmpId,_tmpMediaId,_tmpMediaType,_tmpPlayedAt,_tmpDurationPlayedMs,_tmpCompletionPercent)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteHistoryOlderThan(timestamp: Long) {
    val _sql: String = "DELETE FROM playback_history WHERE playedAt < ?"
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

  public override suspend fun deleteAllHistory() {
    val _sql: String = "DELETE FROM playback_history"
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
