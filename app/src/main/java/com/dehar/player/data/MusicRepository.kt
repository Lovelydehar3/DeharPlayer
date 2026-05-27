package com.dehar.player.data

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File

class MusicRepository(private val context: Context) {

    fun getGenres(): Flow<List<GenreItem>> = flow {
        val genres = mutableMapOf<String, Int>()
        val cursor = context.contentResolver.query(
            MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI,
            arrayOf(MediaStore.Audio.Genres._ID, MediaStore.Audio.Genres.NAME),
            null, null, MediaStore.Audio.Genres.NAME + " ASC"
        )
        cursor?.use {
            while (it.moveToNext()) {
                val name = it.getString(it.getColumnIndexOrThrow(MediaStore.Audio.Genres.NAME)) ?: continue
                val id = it.getLong(it.getColumnIndexOrThrow(MediaStore.Audio.Genres._ID))
                // Count songs for this genre
                val countCursor = context.contentResolver.query(
                    MediaStore.Audio.Genres.Members.getContentUri("external", id),
                    arrayOf(MediaStore.Audio.Media._ID), null, null, null
                )
                genres[name] = countCursor?.count ?: 0
                countCursor?.close()
            }
        }
        emit(genres.map { (name, count) -> GenreItem(name, count) })
    }

    suspend fun getSongs(sortOrder: SortOrder = SortOrder.NAME_ASC): List<SongData> = withContext(Dispatchers.IO) {
        val songs = mutableListOf<SongData>()
        val uri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.SIZE,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.DATE_ADDED,
            MediaStore.Audio.Media.TRACK
        )

        // Broader query to include all standard audio tracks (Downloaded tracks, WhatsApp/Telegram audio, etc.) and filter out short system tones
        val selection = "${MediaStore.Audio.Media.DURATION} >= 10000 AND ${MediaStore.Audio.Media.IS_NOTIFICATION} = 0 AND ${MediaStore.Audio.Media.IS_ALARM} = 0 AND ${MediaStore.Audio.Media.IS_RINGTONE} = 0"
        
        val sortOrderString = when (sortOrder) {
            SortOrder.NAME_ASC -> "${MediaStore.Audio.Media.TITLE} ASC"
            SortOrder.NAME_DESC -> "${MediaStore.Audio.Media.TITLE} DESC"
            SortOrder.DATE_ASC -> "${MediaStore.Audio.Media.DATE_ADDED} ASC"
            SortOrder.DATE_DESC -> "${MediaStore.Audio.Media.DATE_ADDED} DESC"
            SortOrder.SIZE_ASC -> "${MediaStore.Audio.Media.SIZE} ASC"
            SortOrder.SIZE_DESC -> "${MediaStore.Audio.Media.SIZE} DESC"
            SortOrder.DURATION_ASC -> "${MediaStore.Audio.Media.DURATION} ASC"
            SortOrder.DURATION_DESC -> "${MediaStore.Audio.Media.DURATION} DESC"
        }

        try {
            context.contentResolver.query(
                uri,
                projection,
                selection,
                null,
                sortOrderString
            )?.use { cursor ->
                val idCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
                val titleCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
                val artistCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
                val albumCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
                val durCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
                val sizeCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)
                val dataCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
                val dateCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_ADDED)
                val trackCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TRACK)

                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idCol)
                    val title = cursor.getString(titleCol) ?: "Unknown"
                    val artist = cursor.getString(artistCol) ?: MediaStore.UNKNOWN_STRING
                    val album = cursor.getString(albumCol) ?: MediaStore.UNKNOWN_STRING
                    val duration = cursor.getLong(durCol)
                    val size = cursor.getLong(sizeCol)
                    val path = cursor.getString(dataCol) ?: ""
                    val dateAdded = cursor.getLong(dateCol)
                    val trackNumber = cursor.getInt(trackCol)

                    // Safe verification for scoped storage (File.exists() fails on Android 10+ for media paths, so we trust MediaStore database entry)
                    if (path.isNotEmpty()) {
                        val trackUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id).toString()
                        songs.add(
                            SongData(
                                id = id,
                                uri = trackUri,
                                title = title,
                                artist = if (artist == MediaStore.UNKNOWN_STRING) "Unknown Artist" else artist,
                                album = if (album == MediaStore.UNKNOWN_STRING) "Unknown Album" else album,
                                duration = duration,
                                size = size,
                                path = path,
                                dateAdded = dateAdded,
                                trackNumber = trackNumber,
                                isFavorite = false
                            )
                        )
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return@withContext songs
    }
}
