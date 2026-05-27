package com.dehar.player.core.domain.usecase

import androidx.compose.runtime.Stable
import com.dehar.player.core.domain.repository.VideoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@Stable
data class VideoItem(
    val id: Long,
    val title: String,
    val path: String,
    val duration: Long,
    val size: Long,
    val thumbnail: String? = null,
    val resolution: String = "Unknown",
    val lastPlayedPosition: Long = 0L,
    val playCount: Int = 0,
    val isFavorite: Boolean = false
)

class GetVideosUseCase @Inject constructor(
    private val videoRepository: VideoRepository
) {
    operator fun invoke(
        sortBy: SortOption = SortOption.DATE_DESC,
        filterResolution: String? = null
    ): Flow<List<VideoItem>> {
        return videoRepository.getVideos(sortBy, filterResolution)
    }
}

class GetVideoDetailUseCase @Inject constructor(
    private val videoRepository: VideoRepository
) {
    operator fun invoke(videoId: Long): Flow<VideoItem?> {
        return videoRepository.getVideoById(videoId)
    }
}

class UpdateVideoPlaybackUseCase @Inject constructor(
    private val videoRepository: VideoRepository
) {
    suspend operator fun invoke(videoId: Long, position: Long, duration: Long) {
        videoRepository.updatePlaybackPosition(videoId, position, duration)
    }
}

enum class SortOption {
    TITLE_ASC, TITLE_DESC,
    DATE_DESC, DATE_ASC,
    SIZE_DESC, SIZE_ASC,
    DURATION_DESC, DURATION_ASC,
    PLAY_COUNT_DESC,
    LAST_PLAYED_ASC
}
