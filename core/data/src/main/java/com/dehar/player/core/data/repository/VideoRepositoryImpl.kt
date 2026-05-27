package com.dehar.player.core.data.repository

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import com.dehar.player.core.data.database.VideoDao
import com.dehar.player.core.data.model.VideoEntity
import com.dehar.player.core.domain.repository.VideoRepository
import com.dehar.player.core.domain.usecase.SortOption
import com.dehar.player.core.domain.usecase.VideoItem
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VideoRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val videoDao: VideoDao
) : VideoRepository {

    override fun getVideos(
        sortBy: SortOption,
        filterResolution: String?
    ): Flow<List<VideoItem>> = flow {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_VIDEO)
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

        emitAll(videoDao.getAllVideos().map { entities ->
            entities
                .filter { video ->
                    filterResolution == null || video.resolution.contains(filterResolution, ignoreCase = true)
                }
                .sortedWith(videoSortComparator(sortBy))
                .map { it.toVideoItem() }
        })
    }

    override fun getVideoById(id: Long): Flow<VideoItem?> {
        return videoDao.getVideoById(id).map { it?.toVideoItem() }
    }

    override fun getVideosByFolder(folderId: Long, sort: SortOption): Flow<List<VideoItem>> {
        return videoDao.getVideosInFolder(folderId).map { entities ->
            entities.sortedWith(videoSortComparator(sort)).map { it.toVideoItem() }
        }
    }

    override fun getFavoriteVideos(): Flow<List<VideoItem>> {
        return videoDao.getFavoriteVideos().map { entities ->
            entities.map { it.toVideoItem() }
        }
    }

    override fun getRecentlyPlayedVideos(limit: Int): Flow<List<VideoItem>> {
        return videoDao.getRecentlyPlayedVideos(limit).map { entities ->
            entities.map { it.toVideoItem() }
        }
    }

    override fun getUnwatchedVideos(): Flow<List<VideoItem>> {
        return videoDao.getUnwatchedVideos().map { entities ->
            entities.map { it.toVideoItem() }
        }
    }

    override suspend fun updatePlaybackPosition(videoId: Long, position: Long, duration: Long) {
        videoDao.updatePlaybackState(videoId, System.currentTimeMillis(), position)
    }

    override suspend fun toggleFavorite(videoId: Long, isFavorite: Boolean) {
        videoDao.updateFavoriteStatus(videoId, isFavorite)
    }

    override suspend fun deleteVideo(videoId: Long) {
        videoDao.deleteVideoById(videoId)
    }

    override suspend fun rescanLibrary() {
        // Library scan is triggered from the app media scanner layer.
    }

    override fun getVideoCount(): Flow<Int> {
        return videoDao.getVideoCount()
    }

    override fun getTotalVideoSize(): Flow<Long> {
        return videoDao.getTotalSize()
    }
}

private fun videoSortComparator(sort: SortOption): Comparator<VideoEntity> {
    return when (sort) {
        SortOption.TITLE_ASC -> compareBy { it.displayName.lowercase() }
        SortOption.TITLE_DESC -> compareByDescending { it.displayName.lowercase() }
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

fun VideoEntity.toVideoItem(): VideoItem {
    return VideoItem(
        id = id,
        title = title,
        path = path,
        duration = duration,
        size = size,
        thumbnail = customThumbnailPath,
        resolution = resolution,
        lastPlayedPosition = lastPlayedPosition,
        playCount = playCount,
        isFavorite = isFavorite
    )
}
