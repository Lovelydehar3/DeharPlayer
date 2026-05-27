package com.dehar.player.core.domain.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dehar.player.core.domain.model.SongItem
import com.dehar.player.core.domain.usecase.UpdateSongPlaybackUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NowPlayingViewModel @Inject constructor(
    private val updateSongPlaybackUseCase: UpdateSongPlaybackUseCase
) : ViewModel() {

    private val _currentSong = MutableStateFlow<SongItem?>(null)
    val currentSong: StateFlow<SongItem?> = _currentSong.asStateFlow()

    private val _playlist = MutableStateFlow<List<SongItem>>(emptyList())
    val playlist: StateFlow<List<SongItem>> = _playlist.asStateFlow()

    private val _currentIndex = MutableStateFlow(0)
    val currentIndex: StateFlow<Int> = _currentIndex.asStateFlow()

    private val _playbackPosition = MutableStateFlow(0L)
    val playbackPosition: StateFlow<Long> = _playbackPosition.asStateFlow()

    private val _duration = MutableStateFlow(0L)
    val duration: StateFlow<Long> = _duration.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _repeatMode = MutableStateFlow(RepeatMode.NONE)
    val repeatMode: StateFlow<RepeatMode> = _repeatMode.asStateFlow()

    private val _isShuffled = MutableStateFlow(false)
    val isShuffled: StateFlow<Boolean> = _isShuffled.asStateFlow()

    private val _playbackSpeed = MutableStateFlow(1.0f)
    val playbackSpeed: StateFlow<Float> = _playbackSpeed.asStateFlow()

    fun loadPlaylist(songs: List<SongItem>, startIndex: Int = 0) {
        _playlist.value = songs
        if (songs.isNotEmpty()) {
            _currentIndex.value = startIndex.coerceIn(0, songs.size - 1)
            _currentSong.value = songs[_currentIndex.value]
            _duration.value = songs[_currentIndex.value].duration
        }
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

    fun next() {
        val playlist = _playlist.value
        if (playlist.isEmpty()) return

        val nextIndex = when (_repeatMode.value) {
            RepeatMode.ONE -> _currentIndex.value
            else -> {
                val newIndex = _currentIndex.value + 1
                if (newIndex >= playlist.size) {
                    if (_repeatMode.value == RepeatMode.ALL) 0 else -1
                } else {
                    newIndex
                }
            }
        }

        if (nextIndex >= 0 && nextIndex < playlist.size) {
            _currentIndex.value = nextIndex
            _currentSong.value = playlist[nextIndex]
            _playbackPosition.value = 0L
            _duration.value = playlist[nextIndex].duration
        } else {
            // Playlist ended
            pause()
        }
    }

    fun previous() {
        val playlist = _playlist.value
        if (playlist.isEmpty()) return

        val prevIndex = if (_playbackPosition.value > 3000) {
            // Restart current if more than 3 seconds in
            _currentIndex.value
        } else {
            (_currentIndex.value - 1).coerceAtLeast(0)
        }

        _currentIndex.value = prevIndex
        _currentSong.value = playlist[prevIndex]
        _playbackPosition.value = 0L
        _duration.value = playlist[prevIndex].duration
    }

    fun seekTo(positionMs: Long) {
        _playbackPosition.value = positionMs
    }

    fun cycleRepeatMode() {
        _repeatMode.value = when (_repeatMode.value) {
            RepeatMode.NONE -> RepeatMode.ALL
            RepeatMode.ALL -> RepeatMode.ONE
            RepeatMode.ONE -> RepeatMode.NONE
        }
    }

    fun toggleShuffle() {
        _isShuffled.value = !_isShuffled.value
    }

    fun setPlaybackSpeed(speed: Float) {
        _playbackSpeed.value = speed
    }

    fun updatePlaybackPosition(positionMs: Long) {
        _playbackPosition.value = positionMs
        currentSong.value?.let { song ->
            viewModelScope.launch {
                updateSongPlaybackUseCase(song.id, positionMs)
            }
        }
    }

    fun onSongCompleted() {
        next()
        if (_isPlaying.value) {
            play()
        }
    }
}

enum class RepeatMode {
    NONE, ALL, ONE
}
