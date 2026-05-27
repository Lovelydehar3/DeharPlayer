package com.dehar.player.feature.musicplayer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dehar.player.core.ui.components.SongUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class PlaybackState {
    IDLE, LOADING, PLAYING, PAUSED, STOPPED
}

data class MusicPlayerUiState(
    val currentSong: SongUiModel? = null,
    val playbackState: PlaybackState = PlaybackState.IDLE,
    val currentPositionMs: Long = 0,
    val queue: List<SongUiModel> = emptyList(),
    val currentQueueIndex: Int = 0,
    val isShuffleEnabled: Boolean = false,
    val repeatMode: RepeatMode = RepeatMode.NONE,
    val playbackSpeed: Float = 1.0f,
    val volume: Float = 1.0f,
    val isMuted: Boolean = false,
    val lyrics: List<LyricLine> = emptyList(),
    val showQueue: Boolean = false,
    val errorMessage: String? = null
)

enum class RepeatMode {
    NONE, ONE, ALL
}

data class LyricLine(
    val timeMs: Long,
    val text: String,
    val isSynced: Boolean = true
)

@HiltViewModel
class MusicPlayerViewModel @Inject constructor(
    // TODO: Inject repositories and ExoPlayer manager
) : ViewModel() {

    private val _uiState = MutableStateFlow(MusicPlayerUiState())
    val uiState: StateFlow<MusicPlayerUiState> = _uiState.asStateFlow()

    init {
        loadMockData()
    }

    private fun loadMockData() {
        viewModelScope.launch {
            val mockQueue = listOf(
                SongUiModel(
                    id = 1,
                    title = "Beautiful Song",
                    artist = "Artist Name",
                    album = "Album Title",
                    durationMs = 300000,
                    hasLyrics = true
                ),
                SongUiModel(
                    id = 2,
                    title = "Another Track",
                    artist = "Different Artist",
                    album = "Another Album",
                    durationMs = 240000,
                    hasLyrics = false
                ),
                SongUiModel(
                    id = 3,
                    title = "Third Song",
                    artist = "Artist Name",
                    album = "Album Title",
                    durationMs = 280000,
                    hasLyrics = true
                )
            )

            val mockLyrics = listOf(
                LyricLine(0, "Line 1 of lyrics"),
                LyricLine(3000, "Line 2 of lyrics"),
                LyricLine(6000, "Line 3 of lyrics"),
                LyricLine(9000, "Beautiful song lyrics continue..."),
                LyricLine(12000, "More lyrics here"),
                LyricLine(15000, "And here...")
            )

            _uiState.value = _uiState.value.copy(
                currentSong = mockQueue.first(),
                queue = mockQueue,
                playbackState = PlaybackState.PLAYING,
                lyrics = mockLyrics
            )
        }
    }

    fun togglePlayPause() {
        val currentState = _uiState.value.playbackState
        val newState = if (currentState == PlaybackState.PLAYING) {
            PlaybackState.PAUSED
        } else {
            PlaybackState.PLAYING
        }
        _uiState.value = _uiState.value.copy(playbackState = newState)
    }

    fun seekTo(positionMs: Long) {
        _uiState.value = _uiState.value.copy(currentPositionMs = positionMs)
    }

    fun playNext() {
        val queue = _uiState.value.queue
        val currentIndex = _uiState.value.currentQueueIndex
        val nextIndex = if (currentIndex < queue.size - 1) currentIndex + 1 else 0
        
        if (nextIndex < queue.size) {
            _uiState.value = _uiState.value.copy(
                currentSong = queue[nextIndex],
                currentQueueIndex = nextIndex,
                currentPositionMs = 0
            )
        }
    }

    fun playPrevious() {
        val queue = _uiState.value.queue
        val currentIndex = _uiState.value.currentQueueIndex
        val prevIndex = if (currentIndex > 0) currentIndex - 1 else queue.size - 1
        
        if (prevIndex >= 0 && prevIndex < queue.size) {
            _uiState.value = _uiState.value.copy(
                currentSong = queue[prevIndex],
                currentQueueIndex = prevIndex,
                currentPositionMs = 0
            )
        }
    }

    fun toggleShuffle() {
        _uiState.value = _uiState.value.copy(
            isShuffleEnabled = !_uiState.value.isShuffleEnabled
        )
    }

    fun toggleRepeat() {
        val nextMode = when (_uiState.value.repeatMode) {
            RepeatMode.NONE -> RepeatMode.ALL
            RepeatMode.ALL -> RepeatMode.ONE
            RepeatMode.ONE -> RepeatMode.NONE
        }
        _uiState.value = _uiState.value.copy(repeatMode = nextMode)
    }

    fun setPlaybackSpeed(speed: Float) {
        _uiState.value = _uiState.value.copy(playbackSpeed = speed)
    }

    fun setVolume(volume: Float) {
        _uiState.value = _uiState.value.copy(volume = volume.coerceIn(0f, 1f))
    }

    fun toggleMute() {
        _uiState.value = _uiState.value.copy(
            isMuted = !_uiState.value.isMuted
        )
    }

    fun toggleQueueDisplay() {
        _uiState.value = _uiState.value.copy(
            showQueue = !_uiState.value.showQueue
        )
    }

    fun updatePlaybackProgress(currentPositionMs: Long) {
        _uiState.value = _uiState.value.copy(currentPositionMs = currentPositionMs)
    }
}
