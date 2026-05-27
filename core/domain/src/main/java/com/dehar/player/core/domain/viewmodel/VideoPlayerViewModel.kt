package com.dehar.player.core.domain.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dehar.player.core.domain.usecase.UpdateVideoPlaybackUseCase
import com.dehar.player.core.domain.usecase.VideoItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoPlayerViewModel @Inject constructor(
    private val updateVideoPlaybackUseCase: UpdateVideoPlaybackUseCase
) : ViewModel() {

    private val _currentVideo = MutableStateFlow<VideoItem?>(null)
    val currentVideo: StateFlow<VideoItem?> = _currentVideo.asStateFlow()

    private val _playbackPosition = MutableStateFlow(0L)
    val playbackPosition: StateFlow<Long> = _playbackPosition.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _duration = MutableStateFlow(0L)
    val duration: StateFlow<Long> = _duration.asStateFlow()

    private val _playbackSpeed = MutableStateFlow(1.0f)
    val playbackSpeed: StateFlow<Float> = _playbackSpeed.asStateFlow()

    private val _subtitle = MutableStateFlow<SubtitleState>(SubtitleState.None)
    val subtitle: StateFlow<SubtitleState> = _subtitle.asStateFlow()

    private val _controlsVisible = MutableStateFlow(true)
    val controlsVisible: StateFlow<Boolean> = _controlsVisible.asStateFlow()

    fun loadVideo(video: VideoItem) {
        _currentVideo.value = video
        _playbackPosition.value = video.lastPlayedPosition
        _duration.value = video.duration
    }

    fun play() {
        _isPlaying.value = true
    }

    fun pause() {
        _isPlaying.value = false
    }

    fun togglePlayPause() {
        _isPlaying.value = !_isPlaying.value
    }

    fun seekTo(positionMs: Long) {
        _playbackPosition.value = positionMs
    }

    fun setPlaybackSpeed(speed: Float) {
        _playbackSpeed.value = speed
    }

    fun loadSubtitle(path: String, format: String) {
        _subtitle.value = SubtitleState.Loaded(path, format)
    }

    fun disableSubtitle() {
        _subtitle.value = SubtitleState.None
    }

    fun toggleControls() {
        _controlsVisible.value = !_controlsVisible.value
    }

    fun updatePlaybackPosition(positionMs: Long) {
        _playbackPosition.value = positionMs
        currentVideo.value?.let { video ->
            viewModelScope.launch {
                updateVideoPlaybackUseCase(video.id, positionMs, _duration.value)
            }
        }
    }

    fun onVideoCompleted() {
        currentVideo.value?.let { video ->
            viewModelScope.launch {
                updateVideoPlaybackUseCase(video.id, 0L, video.duration)
            }
        }
    }
}

sealed class SubtitleState {
    object None : SubtitleState()
    data class Loaded(val path: String, val format: String) : SubtitleState()
    data class Error(val message: String) : SubtitleState()
}
