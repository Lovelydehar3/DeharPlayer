package com.dehar.player.core.`data`.database

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.dehar.player.core.`data`.model.DownloadEntity
import com.dehar.player.core.`data`.model.DownloadStatus
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
public class DownloadDao_Impl(
  __db: RoomDatabase,
) : DownloadDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfDownloadEntity: EntityInsertAdapter<DownloadEntity>

  private val __converters: Converters = Converters()

  private val __deleteAdapterOfDownloadEntity: EntityDeleteOrUpdateAdapter<DownloadEntity>

  private val __updateAdapterOfDownloadEntity: EntityDeleteOrUpdateAdapter<DownloadEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfDownloadEntity = object : EntityInsertAdapter<DownloadEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `downloads` (`id`,`url`,`title`,`destinationPath`,`totalBytes`,`downloadedBytes`,`status`,`mediaType`,`startedAt`,`completedAt`,`errorMessage`) VALUES (?,?,?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: DownloadEntity) {
        statement.bindText(1, entity.id)
        statement.bindText(2, entity.url)
        statement.bindText(3, entity.title)
        statement.bindText(4, entity.destinationPath)
        statement.bindLong(5, entity.totalBytes)
        statement.bindLong(6, entity.downloadedBytes)
        val _tmp: String = __converters.fromDownloadStatus(entity.status)
        statement.bindText(7, _tmp)
        val _tmp_1: String = __converters.fromMediaType(entity.mediaType)
        statement.bindText(8, _tmp_1)
        statement.bindLong(9, entity.startedAt)
        val _tmpCompletedAt: Long? = entity.completedAt
        if (_tmpCompletedAt == null) {
          statement.bindNull(10)
        } else {
          statement.bindLong(10, _tmpCompletedAt)
        }
        val _tmpErrorMessage: String? = entity.errorMessage
        if (_tmpErrorMessage == null) {
          statement.bindNull(11)
        } else {
          statement.bindText(11, _tmpErrorMessage)
        }
      }
    }
    this.__deleteAdapterOfDownloadEntity = object : EntityDeleteOrUpdateAdapter<DownloadEntity>() {
      protected override fun createQuery(): String = "DELETE FROM `downloads` WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: DownloadEntity) {
        statement.bindText(1, entity.id)
      }
    }
    this.__updateAdapterOfDownloadEntity = object : EntityDeleteOrUpdateAdapter<DownloadEntity>() {
      protected override fun createQuery(): String =
          "UPDATE OR ABORT `downloads` SET `id` = ?,`url` = ?,`title` = ?,`destinationPath` = ?,`totalBytes` = ?,`downloadedBytes` = ?,`status` = ?,`mediaType` = ?,`startedAt` = ?,`completedAt` = ?,`errorMessage` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: DownloadEntity) {
        statement.bindText(1, entity.id)
        statement.bindText(2, entity.url)
        statement.bindText(3, entity.title)
        statement.bindText(4, entity.destinationPath)
        statement.bindLong(5, entity.totalBytes)
        statement.bindLong(6, entity.downloadedBytes)
        val _tmp: String = __converters.fromDownloadStatus(entity.status)
        statement.bindText(7, _tmp)
        val _tmp_1: String = __converters.fromMediaType(entity.mediaType)
        statement.bindText(8, _tmp_1)
        statement.bindLong(9, entity.startedAt)
        val _tmpCompletedAt: Long? = entity.completedAt
        if (_tmpCompletedAt == null) {
          statement.bindNull(10)
        } else {
          statement.bindLong(10, _tmpCompletedAt)
        }
        val _tmpErrorMessage: String? = entity.errorMessage
        if (_tmpErrorMessage == null) {
          statement.bindNull(11)
        } else {
          statement.bindText(11, _tmpErrorMessage)
        }
        statement.bindText(12, entity.id)
      }
    }
  }

  public override suspend fun insertDownload(download: DownloadEntity): Unit =
      performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfDownloadEntity.insert(_connection, download)
  }

  public override suspend fun deleteDownload(download: DownloadEntity): Unit =
      performSuspending(__db, false, true) { _connection ->
    __deleteAdapterOfDownloadEntity.handle(_connection, download)
  }

  public override suspend fun updateDownload(download: DownloadEntity): Unit =
      performSuspending(__db, false, true) { _connection ->
    __updateAdapterOfDownloadEntity.handle(_connection, download)
  }

  public override fun getAllDownloads(): Flow<List<DownloadEntity>> {
    val _sql: String = "SELECT * FROM downloads ORDER BY startedAt DESC"
    return createFlow(__db, false, arrayOf("downloads")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfUrl: Int = getColumnIndexOrThrow(_stmt, "url")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDestinationPath: Int = getColumnIndexOrThrow(_stmt, "destinationPath")
        val _columnIndexOfTotalBytes: Int = getColumnIndexOrThrow(_stmt, "totalBytes")
        val _columnIndexOfDownloadedBytes: Int = getColumnIndexOrThrow(_stmt, "downloadedBytes")
        val _columnIndexOfStatus: Int = getColumnIndexOrThrow(_stmt, "status")
        val _columnIndexOfMediaType: Int = getColumnIndexOrThrow(_stmt, "mediaType")
        val _columnIndexOfStartedAt: Int = getColumnIndexOrThrow(_stmt, "startedAt")
        val _columnIndexOfCompletedAt: Int = getColumnIndexOrThrow(_stmt, "completedAt")
        val _columnIndexOfErrorMessage: Int = getColumnIndexOrThrow(_stmt, "errorMessage")
        val _result: MutableList<DownloadEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: DownloadEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpUrl: String
          _tmpUrl = _stmt.getText(_columnIndexOfUrl)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpDestinationPath: String
          _tmpDestinationPath = _stmt.getText(_columnIndexOfDestinationPath)
          val _tmpTotalBytes: Long
          _tmpTotalBytes = _stmt.getLong(_columnIndexOfTotalBytes)
          val _tmpDownloadedBytes: Long
          _tmpDownloadedBytes = _stmt.getLong(_columnIndexOfDownloadedBytes)
          val _tmpStatus: DownloadStatus
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfStatus)
          _tmpStatus = __converters.toDownloadStatus(_tmp)
          val _tmpMediaType: MediaType
          val _tmp_1: String
          _tmp_1 = _stmt.getText(_columnIndexOfMediaType)
          _tmpMediaType = __converters.toMediaType(_tmp_1)
          val _tmpStartedAt: Long
          _tmpStartedAt = _stmt.getLong(_columnIndexOfStartedAt)
          val _tmpCompletedAt: Long?
          if (_stmt.isNull(_columnIndexOfCompletedAt)) {
            _tmpCompletedAt = null
          } else {
            _tmpCompletedAt = _stmt.getLong(_columnIndexOfCompletedAt)
          }
          val _tmpErrorMessage: String?
          if (_stmt.isNull(_columnIndexOfErrorMessage)) {
            _tmpErrorMessage = null
          } else {
            _tmpErrorMessage = _stmt.getText(_columnIndexOfErrorMessage)
          }
          _item =
              DownloadEntity(_tmpId,_tmpUrl,_tmpTitle,_tmpDestinationPath,_tmpTotalBytes,_tmpDownloadedBytes,_tmpStatus,_tmpMediaType,_tmpStartedAt,_tmpCompletedAt,_tmpErrorMessage)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getDownloadById(id: String): DownloadEntity? {
    val _sql: String = "SELECT * FROM downloads WHERE id = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfUrl: Int = getColumnIndexOrThrow(_stmt, "url")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDestinationPath: Int = getColumnIndexOrThrow(_stmt, "destinationPath")
        val _columnIndexOfTotalBytes: Int = getColumnIndexOrThrow(_stmt, "totalBytes")
        val _columnIndexOfDownloadedBytes: Int = getColumnIndexOrThrow(_stmt, "downloadedBytes")
        val _columnIndexOfStatus: Int = getColumnIndexOrThrow(_stmt, "status")
        val _columnIndexOfMediaType: Int = getColumnIndexOrThrow(_stmt, "mediaType")
        val _columnIndexOfStartedAt: Int = getColumnIndexOrThrow(_stmt, "startedAt")
        val _columnIndexOfCompletedAt: Int = getColumnIndexOrThrow(_stmt, "completedAt")
        val _columnIndexOfErrorMessage: Int = getColumnIndexOrThrow(_stmt, "errorMessage")
        val _result: DownloadEntity?
        if (_stmt.step()) {
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpUrl: String
          _tmpUrl = _stmt.getText(_columnIndexOfUrl)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpDestinationPath: String
          _tmpDestinationPath = _stmt.getText(_columnIndexOfDestinationPath)
          val _tmpTotalBytes: Long
          _tmpTotalBytes = _stmt.getLong(_columnIndexOfTotalBytes)
          val _tmpDownloadedBytes: Long
          _tmpDownloadedBytes = _stmt.getLong(_columnIndexOfDownloadedBytes)
          val _tmpStatus: DownloadStatus
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfStatus)
          _tmpStatus = __converters.toDownloadStatus(_tmp)
          val _tmpMediaType: MediaType
          val _tmp_1: String
          _tmp_1 = _stmt.getText(_columnIndexOfMediaType)
          _tmpMediaType = __converters.toMediaType(_tmp_1)
          val _tmpStartedAt: Long
          _tmpStartedAt = _stmt.getLong(_columnIndexOfStartedAt)
          val _tmpCompletedAt: Long?
          if (_stmt.isNull(_columnIndexOfCompletedAt)) {
            _tmpCompletedAt = null
          } else {
            _tmpCompletedAt = _stmt.getLong(_columnIndexOfCompletedAt)
          }
          val _tmpErrorMessage: String?
          if (_stmt.isNull(_columnIndexOfErrorMessage)) {
            _tmpErrorMessage = null
          } else {
            _tmpErrorMessage = _stmt.getText(_columnIndexOfErrorMessage)
          }
          _result =
              DownloadEntity(_tmpId,_tmpUrl,_tmpTitle,_tmpDestinationPath,_tmpTotalBytes,_tmpDownloadedBytes,_tmpStatus,_tmpMediaType,_tmpStartedAt,_tmpCompletedAt,_tmpErrorMessage)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getDownloadsByStatus(status: DownloadStatus): Flow<List<DownloadEntity>> {
    val _sql: String = "SELECT * FROM downloads WHERE status = ?"
    return createFlow(__db, false, arrayOf("downloads")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        val _tmp: String = __converters.fromDownloadStatus(status)
        _stmt.bindText(_argIndex, _tmp)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfUrl: Int = getColumnIndexOrThrow(_stmt, "url")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDestinationPath: Int = getColumnIndexOrThrow(_stmt, "destinationPath")
        val _columnIndexOfTotalBytes: Int = getColumnIndexOrThrow(_stmt, "totalBytes")
        val _columnIndexOfDownloadedBytes: Int = getColumnIndexOrThrow(_stmt, "downloadedBytes")
        val _columnIndexOfStatus: Int = getColumnIndexOrThrow(_stmt, "status")
        val _columnIndexOfMediaType: Int = getColumnIndexOrThrow(_stmt, "mediaType")
        val _columnIndexOfStartedAt: Int = getColumnIndexOrThrow(_stmt, "startedAt")
        val _columnIndexOfCompletedAt: Int = getColumnIndexOrThrow(_stmt, "completedAt")
        val _columnIndexOfErrorMessage: Int = getColumnIndexOrThrow(_stmt, "errorMessage")
        val _result: MutableList<DownloadEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: DownloadEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpUrl: String
          _tmpUrl = _stmt.getText(_columnIndexOfUrl)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpDestinationPath: String
          _tmpDestinationPath = _stmt.getText(_columnIndexOfDestinationPath)
          val _tmpTotalBytes: Long
          _tmpTotalBytes = _stmt.getLong(_columnIndexOfTotalBytes)
          val _tmpDownloadedBytes: Long
          _tmpDownloadedBytes = _stmt.getLong(_columnIndexOfDownloadedBytes)
          val _tmpStatus: DownloadStatus
          val _tmp_1: String
          _tmp_1 = _stmt.getText(_columnIndexOfStatus)
          _tmpStatus = __converters.toDownloadStatus(_tmp_1)
          val _tmpMediaType: MediaType
          val _tmp_2: String
          _tmp_2 = _stmt.getText(_columnIndexOfMediaType)
          _tmpMediaType = __converters.toMediaType(_tmp_2)
          val _tmpStartedAt: Long
          _tmpStartedAt = _stmt.getLong(_columnIndexOfStartedAt)
          val _tmpCompletedAt: Long?
          if (_stmt.isNull(_columnIndexOfCompletedAt)) {
            _tmpCompletedAt = null
          } else {
            _tmpCompletedAt = _stmt.getLong(_columnIndexOfCompletedAt)
          }
          val _tmpErrorMessage: String?
          if (_stmt.isNull(_columnIndexOfErrorMessage)) {
            _tmpErrorMessage = null
          } else {
            _tmpErrorMessage = _stmt.getText(_columnIndexOfErrorMessage)
          }
          _item =
              DownloadEntity(_tmpId,_tmpUrl,_tmpTitle,_tmpDestinationPath,_tmpTotalBytes,_tmpDownloadedBytes,_tmpStatus,_tmpMediaType,_tmpStartedAt,_tmpCompletedAt,_tmpErrorMessage)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun updateDownloadProgress(
    id: String,
    status: DownloadStatus,
    downloadedBytes: Long,
    totalBytes: Long,
  ) {
    val _sql: String =
        "UPDATE downloads SET status = ?, downloadedBytes = ?, totalBytes = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        val _tmp: String = __converters.fromDownloadStatus(status)
        _stmt.bindText(_argIndex, _tmp)
        _argIndex = 2
        _stmt.bindLong(_argIndex, downloadedBytes)
        _argIndex = 3
        _stmt.bindLong(_argIndex, totalBytes)
        _argIndex = 4
        _stmt.bindText(_argIndex, id)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteCompletedDownloads(status: DownloadStatus) {
    val _sql: String = "DELETE FROM downloads WHERE status = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        val _tmp: String = __converters.fromDownloadStatus(status)
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
