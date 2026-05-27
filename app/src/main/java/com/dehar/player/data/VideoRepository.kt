package com.dehar.player.data

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import android.util.Log
import java.io.File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

import com.dehar.player.core.data.database.DeharDatabase
import com.dehar.player.core.data.model.RecycleBinEntity
import com.dehar.player.core.data.model.MediaType
import java.util.UUID

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat

class VideoRepository(private val context: Context) {
    private val db = DeharDatabase.getDatabase(context)
    private val recycleBinDao = db.recycleBinDao()

    private fun checkPermission(): Boolean {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_VIDEO
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }

    suspend fun moveToRecycleBin(video: VideoData) = withContext(Dispatchers.IO) {
        val originalFile = File(video.path)
        if (!originalFile.exists()) return@withContext
        
        val recycleBinDir = File(context.filesDir, ".recycle_bin").also { it.mkdirs() }
        val recycledId = UUID.randomUUID().toString()
        val recycledFile = File(recycleBinDir, recycledId)
        
        // Move file
        if (originalFile.renameTo(recycledFile)) {
            // DB entry
            recycleBinDao.insertDeletedItem(RecycleBinEntity(
                id = recycledId,
                originalPath = video.path,
                originalName = video.displayName,
                mediaType = MediaType.VIDEO,
                deletedAt = System.currentTimeMillis(),
                size = video.size,
                restorePath = originalFile.parent
            ))
            
            // Note: Since this app uses MediaStore, the file is physically moved.
            // MediaStore will eventually update, but we might want to trigger a scan.
        }
    }

    fun getVideoFolders(sortOrder: SortOrder = SortOrder.NAME_ASC): List<FolderData> {
        val videos = getAllVideos(sortOrder)
        val grouped = videos.groupBy { it.folderPath }
        
        return grouped.map { (path, videoList) ->
            val name = videoList.firstOrNull()?.folderName ?: "Internal Storage"
            FolderData(
                name = name,
                path = path,
                videos = videoList
            )
        }.sortedBy { it.name.lowercase() }
    }

    fun getVideosInFolder(folderPath: String, sortOrder: SortOrder = SortOrder.NAME_ASC): List<VideoData> {
        return getAllVideos(sortOrder).filter { it.folderPath == folderPath }
    }

    fun getAllVideos(sortOrder: SortOrder = SortOrder.NAME_ASC): List<VideoData> {
        if (!checkPermission()) return emptyList()
        val videosList = mutableListOf<VideoData>()
        
        val projection = arrayOf(
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.Media.TITLE,
            MediaStore.Video.Media.DATA,
            MediaStore.Video.Media.DURATION,
            MediaStore.Video.Media.SIZE,
            MediaStore.Video.Media.WIDTH,
            MediaStore.Video.Media.HEIGHT,
            MediaStore.Video.Media.DATE_ADDED
        )

        // Only query external contents
        val uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        val sortOrderString = "${MediaStore.Video.Media.DATE_ADDED} DESC"

        try {
            context.contentResolver.query(
                uri,
                projection,
                null,
                null,
                sortOrderString
            )?.use { cursor ->
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
                val displayNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
                val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE)
                val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
                val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)
                val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)
                val widthColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.WIDTH)
                val heightColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.HEIGHT)
                val dateAddedColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_ADDED)

                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idColumn)
                    val displayName = cursor.getString(displayNameColumn) ?: "Unknown"
                    val title = cursor.getString(titleColumn) ?: "Unknown"
                    val path = cursor.getString(dataColumn) ?: ""
                    val duration = cursor.getLong(durationColumn)
                    val size = cursor.getLong(sizeColumn)
                    val width = cursor.getInt(widthColumn)
                    val height = cursor.getInt(heightColumn)
                    val dateAdded = cursor.getLong(dateAddedColumn)

                    // Skip empty paths or invalid files
                    if (path.isEmpty()) continue

                    val file = File(path)
                    val folderPath = file.parent ?: "/"
                    val folderName = file.parentFile?.name ?: "Internal Storage"
                    
                    val contentUri = ContentUris.withAppendedId(
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                        id
                    )

                    videosList.add(
                        VideoData(
                            id = id,
                            title = title,
                            displayName = displayName,
                            path = path,
                            uri = contentUri,
                            duration = duration,
                            size = size,
                            width = width,
                            height = height,
                            dateAdded = dateAdded,
                            folderName = folderName,
                            folderPath = folderPath,
                            isFavorite = false, // Stub
                            playCount = 0 // Stub
                        )
                    )
                }
            }
        } catch (e: Exception) {
            Log.e("VideoRepository", "Error reading MediaStore", e)
        }

        return sortVideos(videosList, sortOrder)
    }

    fun getWhatsAppStatuses(): List<VideoData> {
        val statuses = mutableListOf<VideoData>()
        val paths = listOf(
            "/storage/emulated/0/Android/media/com.whatsapp/WhatsApp/Media/.Statuses",
            "/storage/emulated/0/WhatsApp/Media/.Statuses",
            "/storage/emulated/0/Android/media/com.whatsapp.w4b/WhatsApp Business/Media/.Statuses"
        )

        paths.forEach { path ->
            val dir = File(path)
            if (dir.exists() && dir.isDirectory) {
                dir.listFiles { file -> 
                    file.isFile && (file.extension.lowercase() == "mp4" || file.extension.lowercase() == "mkv")
                }?.forEach { file ->
                    // Since MediaStore might not scan hidden .Statuses folder, we create manual VideoData
                    val retriever = MediaMetadataRetriever()
                    try {
                        retriever.setDataSource(file.absolutePath)
                        val duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLongOrNull() ?: 0L
                        val width = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)?.toIntOrNull() ?: 0
                        val height = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)?.toIntOrNull() ?: 0
                        
                        statuses.add(VideoData(
                            id = file.hashCode().toLong(),
                            title = file.name,
                            displayName = "Status ${file.nameWithoutExtension}",
                            path = file.absolutePath,
                            uri = Uri.fromFile(file),
                            duration = duration,
                            size = file.length(),
                            width = width,
                            height = height,
                            dateAdded = file.lastModified() / 1000,
                            folderName = "WhatsApp Status",
                            folderPath = file.parent ?: ""
                        ))
                    } catch (e: Exception) {
                        e.printStackTrace()
                    } finally {
                        retriever.release()
                    }
                }
            }
        }
        return statuses.sortedByDescending { it.dateAdded }
    }

    private fun sortVideos(videos: List<VideoData>, order: SortOrder): List<VideoData> {
        return when (order) {
            SortOrder.NAME_ASC -> videos.sortedBy { it.displayName.lowercase() }
            SortOrder.NAME_DESC -> videos.sortedByDescending { it.displayName.lowercase() }
            SortOrder.DATE_ASC -> videos.sortedBy { it.dateAdded }
            SortOrder.DATE_DESC -> videos.sortedByDescending { it.dateAdded }
            SortOrder.SIZE_ASC -> videos.sortedBy { it.size }
            SortOrder.SIZE_DESC -> videos.sortedByDescending { it.size }
            SortOrder.DURATION_ASC -> videos.sortedBy { it.duration }
            SortOrder.DURATION_DESC -> videos.sortedByDescending { it.duration }
        }
    }
}
