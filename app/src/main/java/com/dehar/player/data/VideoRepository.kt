package com.dehar.player.data

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import android.util.Log
import java.io.File

class VideoRepository(private val context: Context) {

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
                            folderPath = folderPath
                        )
                    )
                }
            }
        } catch (e: Exception) {
            Log.e("VideoRepository", "Error reading MediaStore", e)
        }

        return sortVideos(videosList, sortOrder)
    }

    fun searchVideos(query: String): List<VideoData> {
        val all = getAllVideos(SortOrder.NAME_ASC)
        if (query.isEmpty()) return all
        return all.filter { 
            it.displayName.contains(query, ignoreCase = true) || 
            it.title.contains(query, ignoreCase = true) 
        }
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
