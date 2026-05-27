package com.dehar.player.core.`data`.database

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.dehar.player.core.`data`.model.EqPresetEntity
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
public class EqPresetDao_Impl(
  __db: RoomDatabase,
) : EqPresetDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfEqPresetEntity: EntityInsertAdapter<EqPresetEntity>

  private val __deleteAdapterOfEqPresetEntity: EntityDeleteOrUpdateAdapter<EqPresetEntity>

  private val __updateAdapterOfEqPresetEntity: EntityDeleteOrUpdateAdapter<EqPresetEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfEqPresetEntity = object : EntityInsertAdapter<EqPresetEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `eq_presets` (`id`,`name`,`bands`,`bassBoost`,`virtualizer`,`loudnessEnhancer`,`isSystem`,`isActive`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: EqPresetEntity) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.name)
        statement.bindText(3, entity.bands)
        statement.bindLong(4, entity.bassBoost.toLong())
        statement.bindLong(5, entity.virtualizer.toLong())
        statement.bindLong(6, entity.loudnessEnhancer.toLong())
        val _tmp: Int = if (entity.isSystem) 1 else 0
        statement.bindLong(7, _tmp.toLong())
        val _tmp_1: Int = if (entity.isActive) 1 else 0
        statement.bindLong(8, _tmp_1.toLong())
      }
    }
    this.__deleteAdapterOfEqPresetEntity = object : EntityDeleteOrUpdateAdapter<EqPresetEntity>() {
      protected override fun createQuery(): String = "DELETE FROM `eq_presets` WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: EqPresetEntity) {
        statement.bindLong(1, entity.id)
      }
    }
    this.__updateAdapterOfEqPresetEntity = object : EntityDeleteOrUpdateAdapter<EqPresetEntity>() {
      protected override fun createQuery(): String =
          "UPDATE OR ABORT `eq_presets` SET `id` = ?,`name` = ?,`bands` = ?,`bassBoost` = ?,`virtualizer` = ?,`loudnessEnhancer` = ?,`isSystem` = ?,`isActive` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: EqPresetEntity) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.name)
        statement.bindText(3, entity.bands)
        statement.bindLong(4, entity.bassBoost.toLong())
        statement.bindLong(5, entity.virtualizer.toLong())
        statement.bindLong(6, entity.loudnessEnhancer.toLong())
        val _tmp: Int = if (entity.isSystem) 1 else 0
        statement.bindLong(7, _tmp.toLong())
        val _tmp_1: Int = if (entity.isActive) 1 else 0
        statement.bindLong(8, _tmp_1.toLong())
        statement.bindLong(9, entity.id)
      }
    }
  }

  public override suspend fun insertPreset(preset: EqPresetEntity): Long = performSuspending(__db,
      false, true) { _connection ->
    val _result: Long = __insertAdapterOfEqPresetEntity.insertAndReturnId(_connection, preset)
    _result
  }

  public override suspend fun deletePreset(preset: EqPresetEntity): Unit = performSuspending(__db,
      false, true) { _connection ->
    __deleteAdapterOfEqPresetEntity.handle(_connection, preset)
  }

  public override suspend fun updatePreset(preset: EqPresetEntity): Unit = performSuspending(__db,
      false, true) { _connection ->
    __updateAdapterOfEqPresetEntity.handle(_connection, preset)
  }

  public override fun getCustomPresets(): Flow<List<EqPresetEntity>> {
    val _sql: String = "SELECT * FROM eq_presets WHERE isSystem = 0 ORDER BY name ASC"
    return createFlow(__db, false, arrayOf("eq_presets")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfBands: Int = getColumnIndexOrThrow(_stmt, "bands")
        val _columnIndexOfBassBoost: Int = getColumnIndexOrThrow(_stmt, "bassBoost")
        val _columnIndexOfVirtualizer: Int = getColumnIndexOrThrow(_stmt, "virtualizer")
        val _columnIndexOfLoudnessEnhancer: Int = getColumnIndexOrThrow(_stmt, "loudnessEnhancer")
        val _columnIndexOfIsSystem: Int = getColumnIndexOrThrow(_stmt, "isSystem")
        val _columnIndexOfIsActive: Int = getColumnIndexOrThrow(_stmt, "isActive")
        val _result: MutableList<EqPresetEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: EqPresetEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpBands: String
          _tmpBands = _stmt.getText(_columnIndexOfBands)
          val _tmpBassBoost: Int
          _tmpBassBoost = _stmt.getLong(_columnIndexOfBassBoost).toInt()
          val _tmpVirtualizer: Int
          _tmpVirtualizer = _stmt.getLong(_columnIndexOfVirtualizer).toInt()
          val _tmpLoudnessEnhancer: Int
          _tmpLoudnessEnhancer = _stmt.getLong(_columnIndexOfLoudnessEnhancer).toInt()
          val _tmpIsSystem: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsSystem).toInt()
          _tmpIsSystem = _tmp != 0
          val _tmpIsActive: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsActive).toInt()
          _tmpIsActive = _tmp_1 != 0
          _item =
              EqPresetEntity(_tmpId,_tmpName,_tmpBands,_tmpBassBoost,_tmpVirtualizer,_tmpLoudnessEnhancer,_tmpIsSystem,_tmpIsActive)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getSystemPresets(): Flow<List<EqPresetEntity>> {
    val _sql: String = "SELECT * FROM eq_presets WHERE isSystem = 1 ORDER BY name ASC"
    return createFlow(__db, false, arrayOf("eq_presets")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfBands: Int = getColumnIndexOrThrow(_stmt, "bands")
        val _columnIndexOfBassBoost: Int = getColumnIndexOrThrow(_stmt, "bassBoost")
        val _columnIndexOfVirtualizer: Int = getColumnIndexOrThrow(_stmt, "virtualizer")
        val _columnIndexOfLoudnessEnhancer: Int = getColumnIndexOrThrow(_stmt, "loudnessEnhancer")
        val _columnIndexOfIsSystem: Int = getColumnIndexOrThrow(_stmt, "isSystem")
        val _columnIndexOfIsActive: Int = getColumnIndexOrThrow(_stmt, "isActive")
        val _result: MutableList<EqPresetEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: EqPresetEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpBands: String
          _tmpBands = _stmt.getText(_columnIndexOfBands)
          val _tmpBassBoost: Int
          _tmpBassBoost = _stmt.getLong(_columnIndexOfBassBoost).toInt()
          val _tmpVirtualizer: Int
          _tmpVirtualizer = _stmt.getLong(_columnIndexOfVirtualizer).toInt()
          val _tmpLoudnessEnhancer: Int
          _tmpLoudnessEnhancer = _stmt.getLong(_columnIndexOfLoudnessEnhancer).toInt()
          val _tmpIsSystem: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsSystem).toInt()
          _tmpIsSystem = _tmp != 0
          val _tmpIsActive: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsActive).toInt()
          _tmpIsActive = _tmp_1 != 0
          _item =
              EqPresetEntity(_tmpId,_tmpName,_tmpBands,_tmpBassBoost,_tmpVirtualizer,_tmpLoudnessEnhancer,_tmpIsSystem,_tmpIsActive)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getActivePreset(): Flow<EqPresetEntity?> {
    val _sql: String = "SELECT * FROM eq_presets WHERE isActive = 1 LIMIT 1"
    return createFlow(__db, false, arrayOf("eq_presets")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfBands: Int = getColumnIndexOrThrow(_stmt, "bands")
        val _columnIndexOfBassBoost: Int = getColumnIndexOrThrow(_stmt, "bassBoost")
        val _columnIndexOfVirtualizer: Int = getColumnIndexOrThrow(_stmt, "virtualizer")
        val _columnIndexOfLoudnessEnhancer: Int = getColumnIndexOrThrow(_stmt, "loudnessEnhancer")
        val _columnIndexOfIsSystem: Int = getColumnIndexOrThrow(_stmt, "isSystem")
        val _columnIndexOfIsActive: Int = getColumnIndexOrThrow(_stmt, "isActive")
        val _result: EqPresetEntity?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpBands: String
          _tmpBands = _stmt.getText(_columnIndexOfBands)
          val _tmpBassBoost: Int
          _tmpBassBoost = _stmt.getLong(_columnIndexOfBassBoost).toInt()
          val _tmpVirtualizer: Int
          _tmpVirtualizer = _stmt.getLong(_columnIndexOfVirtualizer).toInt()
          val _tmpLoudnessEnhancer: Int
          _tmpLoudnessEnhancer = _stmt.getLong(_columnIndexOfLoudnessEnhancer).toInt()
          val _tmpIsSystem: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsSystem).toInt()
          _tmpIsSystem = _tmp != 0
          val _tmpIsActive: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsActive).toInt()
          _tmpIsActive = _tmp_1 != 0
          _result =
              EqPresetEntity(_tmpId,_tmpName,_tmpBands,_tmpBassBoost,_tmpVirtualizer,_tmpLoudnessEnhancer,_tmpIsSystem,_tmpIsActive)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getPresetById(id: Long): EqPresetEntity? {
    val _sql: String = "SELECT * FROM eq_presets WHERE id = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfBands: Int = getColumnIndexOrThrow(_stmt, "bands")
        val _columnIndexOfBassBoost: Int = getColumnIndexOrThrow(_stmt, "bassBoost")
        val _columnIndexOfVirtualizer: Int = getColumnIndexOrThrow(_stmt, "virtualizer")
        val _columnIndexOfLoudnessEnhancer: Int = getColumnIndexOrThrow(_stmt, "loudnessEnhancer")
        val _columnIndexOfIsSystem: Int = getColumnIndexOrThrow(_stmt, "isSystem")
        val _columnIndexOfIsActive: Int = getColumnIndexOrThrow(_stmt, "isActive")
        val _result: EqPresetEntity?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpBands: String
          _tmpBands = _stmt.getText(_columnIndexOfBands)
          val _tmpBassBoost: Int
          _tmpBassBoost = _stmt.getLong(_columnIndexOfBassBoost).toInt()
          val _tmpVirtualizer: Int
          _tmpVirtualizer = _stmt.getLong(_columnIndexOfVirtualizer).toInt()
          val _tmpLoudnessEnhancer: Int
          _tmpLoudnessEnhancer = _stmt.getLong(_columnIndexOfLoudnessEnhancer).toInt()
          val _tmpIsSystem: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsSystem).toInt()
          _tmpIsSystem = _tmp != 0
          val _tmpIsActive: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsActive).toInt()
          _tmpIsActive = _tmp_1 != 0
          _result =
              EqPresetEntity(_tmpId,_tmpName,_tmpBands,_tmpBassBoost,_tmpVirtualizer,_tmpLoudnessEnhancer,_tmpIsSystem,_tmpIsActive)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun setActivePreset(id: Long) {
    val _sql: String = "UPDATE eq_presets SET isActive = CASE WHEN id = ? THEN 1 ELSE 0 END"
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

  public override suspend fun clearActivePreset() {
    val _sql: String = "UPDATE eq_presets SET isActive = 0"
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
