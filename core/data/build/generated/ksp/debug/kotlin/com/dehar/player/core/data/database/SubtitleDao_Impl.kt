package com.dehar.player.core.`data`.database

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.dehar.player.core.`data`.model.SubtitleFormat
import com.dehar.player.core.`data`.model.SubtitleTrackEntity
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
public class SubtitleDao_Impl(
  __db: RoomDatabase,
) : SubtitleDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfSubtitleTrackEntity: EntityInsertAdapter<SubtitleTrackEntity>

  private val __converters: Converters = Converters()

  private val __deleteAdapterOfSubtitleTrackEntity: EntityDeleteOrUpdateAdapter<SubtitleTrackEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfSubtitleTrackEntity = object : EntityInsertAdapter<SubtitleTrackEntity>()
        {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `subtitle_tracks` (`id`,`videoId`,`language`,`label`,`path`,`format`,`encoding`,`isDefault`,`offsetMs`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: SubtitleTrackEntity) {
        statement.bindLong(1, entity.id)
        statement.bindLong(2, entity.videoId)
        statement.bindText(3, entity.language)
        statement.bindText(4, entity.label)
        statement.bindText(5, entity.path)
        val _tmp: String = __converters.fromSubtitleFormat(entity.format)
        statement.bindText(6, _tmp)
        statement.bindText(7, entity.encoding)
        val _tmp_1: Int = if (entity.isDefault) 1 else 0
        statement.bindLong(8, _tmp_1.toLong())
        statement.bindLong(9, entity.offsetMs)
      }
    }
    this.__deleteAdapterOfSubtitleTrackEntity = object :
        EntityDeleteOrUpdateAdapter<SubtitleTrackEntity>() {
      protected override fun createQuery(): String = "DELETE FROM `subtitle_tracks` WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: SubtitleTrackEntity) {
        statement.bindLong(1, entity.id)
      }
    }
  }

  public override suspend fun insertSubtitleTrack(track: SubtitleTrackEntity): Long =
      performSuspending(__db, false, true) { _connection ->
    val _result: Long = __insertAdapterOfSubtitleTrackEntity.insertAndReturnId(_connection, track)
    _result
  }

  public override suspend fun insertAllSubtitleTracks(tracks: List<SubtitleTrackEntity>): Unit =
      performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfSubtitleTrackEntity.insert(_connection, tracks)
  }

  public override suspend fun deleteSubtitleTrack(track: SubtitleTrackEntity): Unit =
      performSuspending(__db, false, true) { _connection ->
    __deleteAdapterOfSubtitleTrackEntity.handle(_connection, track)
  }

  public override fun getSubtitleTracksForVideo(videoId: Long): Flow<List<SubtitleTrackEntity>> {
    val _sql: String =
        "SELECT * FROM subtitle_tracks WHERE videoId = ? ORDER BY isDefault DESC, language ASC"
    return createFlow(__db, false, arrayOf("subtitle_tracks")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, videoId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfVideoId: Int = getColumnIndexOrThrow(_stmt, "videoId")
        val _columnIndexOfLanguage: Int = getColumnIndexOrThrow(_stmt, "language")
        val _columnIndexOfLabel: Int = getColumnIndexOrThrow(_stmt, "label")
        val _columnIndexOfPath: Int = getColumnIndexOrThrow(_stmt, "path")
        val _columnIndexOfFormat: Int = getColumnIndexOrThrow(_stmt, "format")
        val _columnIndexOfEncoding: Int = getColumnIndexOrThrow(_stmt, "encoding")
        val _columnIndexOfIsDefault: Int = getColumnIndexOrThrow(_stmt, "isDefault")
        val _columnIndexOfOffsetMs: Int = getColumnIndexOrThrow(_stmt, "offsetMs")
        val _result: MutableList<SubtitleTrackEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: SubtitleTrackEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpVideoId: Long
          _tmpVideoId = _stmt.getLong(_columnIndexOfVideoId)
          val _tmpLanguage: String
          _tmpLanguage = _stmt.getText(_columnIndexOfLanguage)
          val _tmpLabel: String
          _tmpLabel = _stmt.getText(_columnIndexOfLabel)
          val _tmpPath: String
          _tmpPath = _stmt.getText(_columnIndexOfPath)
          val _tmpFormat: SubtitleFormat
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfFormat)
          _tmpFormat = __converters.toSubtitleFormat(_tmp)
          val _tmpEncoding: String
          _tmpEncoding = _stmt.getText(_columnIndexOfEncoding)
          val _tmpIsDefault: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsDefault).toInt()
          _tmpIsDefault = _tmp_1 != 0
          val _tmpOffsetMs: Long
          _tmpOffsetMs = _stmt.getLong(_columnIndexOfOffsetMs)
          _item =
              SubtitleTrackEntity(_tmpId,_tmpVideoId,_tmpLanguage,_tmpLabel,_tmpPath,_tmpFormat,_tmpEncoding,_tmpIsDefault,_tmpOffsetMs)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getSubtitleTracksForVideoOnce(videoId: Long):
      List<SubtitleTrackEntity> {
    val _sql: String =
        "SELECT * FROM subtitle_tracks WHERE videoId = ? ORDER BY isDefault DESC, language ASC"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, videoId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfVideoId: Int = getColumnIndexOrThrow(_stmt, "videoId")
        val _columnIndexOfLanguage: Int = getColumnIndexOrThrow(_stmt, "language")
        val _columnIndexOfLabel: Int = getColumnIndexOrThrow(_stmt, "label")
        val _columnIndexOfPath: Int = getColumnIndexOrThrow(_stmt, "path")
        val _columnIndexOfFormat: Int = getColumnIndexOrThrow(_stmt, "format")
        val _columnIndexOfEncoding: Int = getColumnIndexOrThrow(_stmt, "encoding")
        val _columnIndexOfIsDefault: Int = getColumnIndexOrThrow(_stmt, "isDefault")
        val _columnIndexOfOffsetMs: Int = getColumnIndexOrThrow(_stmt, "offsetMs")
        val _result: MutableList<SubtitleTrackEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: SubtitleTrackEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpVideoId: Long
          _tmpVideoId = _stmt.getLong(_columnIndexOfVideoId)
          val _tmpLanguage: String
          _tmpLanguage = _stmt.getText(_columnIndexOfLanguage)
          val _tmpLabel: String
          _tmpLabel = _stmt.getText(_columnIndexOfLabel)
          val _tmpPath: String
          _tmpPath = _stmt.getText(_columnIndexOfPath)
          val _tmpFormat: SubtitleFormat
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfFormat)
          _tmpFormat = __converters.toSubtitleFormat(_tmp)
          val _tmpEncoding: String
          _tmpEncoding = _stmt.getText(_columnIndexOfEncoding)
          val _tmpIsDefault: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsDefault).toInt()
          _tmpIsDefault = _tmp_1 != 0
          val _tmpOffsetMs: Long
          _tmpOffsetMs = _stmt.getLong(_columnIndexOfOffsetMs)
          _item =
              SubtitleTrackEntity(_tmpId,_tmpVideoId,_tmpLanguage,_tmpLabel,_tmpPath,_tmpFormat,_tmpEncoding,_tmpIsDefault,_tmpOffsetMs)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteAllSubtitleTracksForVideo(videoId: Long) {
    val _sql: String = "DELETE FROM subtitle_tracks WHERE videoId = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, videoId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun setDefaultTrack(videoId: Long, trackId: Long) {
    val _sql: String =
        "UPDATE subtitle_tracks SET isDefault = CASE WHEN id = ? THEN 1 ELSE 0 END WHERE videoId = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, trackId)
        _argIndex = 2
        _stmt.bindLong(_argIndex, videoId)
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
