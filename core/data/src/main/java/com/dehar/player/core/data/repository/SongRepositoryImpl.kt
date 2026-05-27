package com.dehar.player.core.data.repository

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import com.dehar.player.core.data.database.SongDao
import com.dehar.player.core.data.model.SongEntity
import com.dehar.player.core.domain.model.SongItem
import com.dehar.player.core.domain.repository.SongRepository
import com.dehar.player.core.domain.usecase.SortOption
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SongRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val songDao: SongDao
) : SongRepository {

    override fun getSongs(sort: SortOption): Flow<List<SongItem>> = flow {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
                emit(emptyList())
                return@flow
            }
        } else {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
                emit(emptyList())
                return@flow
            }
        }

        emitAll(songDao.getAllSongs().map { entities ->
            entities.sortedWith(songSortComparator(sort)).map { it.toSongItem() }
        })
    }

    override fun getSongsByAlbum(albumId: Long): Flow<List<SongItem>> {
        return songDao.getSongsByAlbum(albumId).map { entities ->
            entities.map { it.toSongItem() }
        }
    }

    override fun getSongsByArtist(artistId: Long): Flow<List<SongItem>> {
        return songDao.getSongsByArtist(artistId).map { entities ->
            entities.map { it.toSongItem() }
        }
    }

    override fun getSongsByGenre(genre: String): Flow<List<SongItem>> {
        return songDao.getSongsByGenre(genre).map { entities ->
            entities.map { it.toSongItem() }
        }
    }

    override fun getFavoriteSongs(): Flow<List<SongItem>> {
        return songDao.getFavoriteSongs().map { entities ->
            entities.map { it.toSongItem() }
        }
    }

    override fun getRecentlyPlayedSongs(limit: Int): Flow<List<SongItem>> {
        return songDao.getRecentlyPlayedSongs(limit).map { entities ->
            entities.map { it.toSongItem() }
        }
    }

    override fun searchSongs(query: String): Flow<List<SongItem>> {
        return songDao.searchSongs(query).map { entities ->
            entities.map { it.toSongItem() }
        }
    }

    override suspend fun updateSongPlaybackPosition(songId: Long, position: Long) {
        songDao.updatePlaybackState(songId, System.currentTimeMillis(), position)
    }

    override suspend fun toggleFavoriteSong(songId: Long, isFavorite: Boolean) {
        songDao.updateFavoriteStatus(songId, isFavorite)
    }

    override suspend fun deleteSong(songId: Long) {
        songDao.deleteSongById(songId)
    }

    override fun getSongCount(): Flow<Int> {
        return songDao.getSongCount()
    }
}

private fun songSortComparator(sort: SortOption): Comparator<SongEntity> {
    return when (sort) {
        SortOption.TITLE_ASC -> compareBy { it.title.lowercase() }
        SortOption.TITLE_DESC -> compareByDescending { it.title.lowercase() }
        SortOption.DATE_DESC -> compareByDescending { it.dateAdded }
        SortOption.DATE_ASC -> compareBy { it.dateAdded }
        SortOption.SIZE_DESC -> compareByDescending { it.size }
        SortOption.SIZE_ASC -> compareBy { it.size }
        SortOption.DURATION_DESC -> compareByDescending { it.duration }
        SortOption.DURATION_ASC -> compareBy { it.duration }
        SortOption.PLAY_COUNT_DESC -> compareByDescending { it.playCount }
        SortOption.LAST_PLAYED_ASC -> compareBy { it.lastPlayedAt }
    }
}

fun SongEntity.toSongItem(): SongItem {
    return SongItem(
        id = id,
        title = title,
        artist = artist,
        album = album,
        path = path,
        duration = duration,
        trackNumber = trackNumber,
        discNumber = discNumber,
        year = year,
        bitrate = bitrate,
        sampleRate = sampleRate,
        mimeType = mimeType,
        playCount = playCount,
        isFavorite = isFavorite,
        lyricsPath = lyricsPath,
        embeddedLyrics = embeddedLyrics
    )
}
