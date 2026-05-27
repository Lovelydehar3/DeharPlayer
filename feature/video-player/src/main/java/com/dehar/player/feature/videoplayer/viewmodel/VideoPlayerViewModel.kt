package com.dehar.player.feature.videoplayer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dehar.player.core.ui.components.VideoUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class VideoPlaybackState {
    IDLE, LOADING, PLAYING, PAUSED, STOPPED, BUFFERING
}

enum class VideoAspectRatio {
    FIT_TO_SCREEN, FILL_SCREEN, ORIGINAL, ZOOM
}

data class SubtitleTrack(
    val id: Long,
    val language: String,
    val format: String,
    val isSelected: Boolean = false
)

data class AudioTrack(
    val id: Long,
    val language: String,
    val channels: String,
    val bitrate: String,
    val isSelected: Boolean = false
)

data class VideoPlayerUiState(
    val currentVideo: VideoUiModel? = null,
    val playbackState: VideoPlaybackState = VideoPlaybackState.IDLE,
    val currentPositionMs: Long = 0,
    val durationMs: Long = 0,
    val isControlsVisible: Boolean = true,
    val isFullscreen: Boolean = false,
    val aspectRatio: VideoAspectRatio = VideoAspectRatio.FIT_TO_SCREEN,
    val playbackSpeed: Float = 1.0f,
    val brightness: Float = 1.0f,
    val volume: Float = 1.0f,
    val isMuted: Boolean = false,
    val subtitles: List<SubtitleTrack> = emptyList(),
    val audioTracks: List<AudioTrack> = emptyList(),
    val isLocked: Boolean = false,
    val isPictureInPictureMode: Boolean = false,
    val seekPosition: Long? = null,
    val abRepeatStart: Long? = null,
    val abRepeatEnd: Long? = null,
    val sleepTimerMinutes: Int = 0,
    val thumbnailPreviewUri: String? = null,
    val errorMessage: String? = null
)

@HiltViewModel
class VideoPlayerViewModel @Inject constructor(
    // TODO: Inject repositories and ExoPlayer manager
) : ViewModel() {

    private val _uiState = MutableStateFlow(VideoPlayerUiState())
    val uiState: StateFlow<VideoPlayerUiState> = _uiState.asStateFlow()

    init {
        loadMockData()
    }

    private fun loadMockData() {
        viewModelScope.launch {
            val mockVideo = VideoUiModel(
                id = 1,
                title = "Sample Video",
                durationSec = 3600,
                thumbnailUri = null,
                size = 524288000,
                resolution = "1080p",
                isWatched = false
            )

            val mockSubtitles = listOf(
                SubtitleTrack(1, "English", "SRT", true),
                SubtitleTrack(2, "Spanish", "SRT", false),
                SubtitleTrack(3, "French", "ASS", false)
            )

            val mockAudioTracks = listOf(
                AudioTrack(1, "English", "2.0", "128 kbps", true),
                AudioTrack(2, "Spanish", "5.1", "320 kbps", false),
                AudioTrack(3, "French", "2.0", "128 kbps", false)
            )

            _uiState.value = _uiState.value.copy(
                currentVideo = mockVideo,
                durationMs = mockVideo.durationSec * 1000,
                playbackState = VideoPlaybackState.PLAYING,
                subtitles = mockSubtitles,
                audioTracks = mockAudioTracks
            )
        }
    }

    fun togglePlayPause() {
        val currentState = _uiState.value.playbackState
        val newState = if (currentState == VideoPlaybackState.PLAYING) {
            VideoPlaybackState.PAUSED
        } else {
            VideoPlaybackState.PLAYING
        }
        _uiState.value = _uiState.value.copy(playbackState = newState)
    }

    fun seekTo(positionMs: Long) {
        _uiState.value = _uiState.value.copy(currentPositionMs = positionMs, seekPosition = positionMs)
    }

    fun updateProgress(currentPositionMs: Long) {
        _uiState.value = _uiState.value.copy(currentPositionMs = currentPositionMs, seekPosition = null)
    }

    fun toggleControlsVisibility() {
        _uiState.value = _uiState.value.copy(
            isControlsVisible = !_uiState.value.isControlsVisible
        )
    }

    fun toggleFullscreen() {
        _uiState.value = _uiState.value.copy(
            isFullscreen = !_uiState.value.isFullscreen
        )
    }

    fun toggleLockControls() {
        _uiState.value = _uiState.value.copy(
            isLocked = !_uiState.value.isLocked
        )
    }

    fun togglePictureInPicture() {
        _uiState.value = _uiState.value.copy(
            isPictureInPictureMode = !_uiState.value.isPictureInPictureMode
        )
    }

    fun setAspectRatio(aspectRatio: VideoAspectRatio) {
        _uiState.value = _uiState.value.copy(aspectRatio = aspectRatio)
    }

    fun setPlaybackSpeed(speed: Float) {
        _uiState.value = _uiState.value.copy(playbackSpeed = speed)
    }

    fun setBrightness(brightness: Float) {
        _uiState.value = _uiState.value.copy(brightness = brightness.coerceIn(0f, 1f))
    }

    fun setVolume(volume: Float) {
        _uiState.value = _uiState.value.copy(volume = volume.coerceIn(0f, 1f))
    }

    fun toggleMute() {
        _uiState.value = _uiState.value.copy(
            isMuted = !_uiState.value.isMuted
        )
    }

    fun selectSubtitle(trackId: Long) {
        val updatedSubtitles = _uiState.value.subtitles.map { track ->
            track.copy(isSelected = track.id == trackId)
        }
        _uiState.value = _uiState.value.copy(subtitles = updatedSubtitles)
    }

    fun selectAudioTrack(trackId: Long) {
        val updatedTracks = _uiState.value.audioTracks.map { track ->
            track.copy(isSelected = track.id == trackId)
        }
        _uiState.value = _uiState.value.copy(audioTracks = updatedTracks)
    }

    fun setABRepeatStart(positionMs: Long) {
        _uiState.value = _uiState.value.copy(abRepeatStart = positionMs)
    }

    fun setABRepeatEnd(positionMs: Long) {
        _uiState.value = _uiState.value.copy(abRepeatEnd = positionMs)
    }

    fun clearABRepeat() {
        _uiState.value = _uiState.value.copy(abRepeatStart = null, abRepeatEnd = null)
    }

    fun setSleepTimer(minutes: Int) {
        _uiState.value = _uiState.value.copy(sleepTimerMinutes = minutes)
    }

    fun decrementSleepTimer() {
        val current = _uiState.value.sleepTimerMinutes
        if (current > 0) {
            _uiState.value = _uiState.value.copy(sleepTimerMinutes = current - 1)
        }
    }
}
