package com.dehar.player.feature.ringtoneEditor.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.dehar.player.feature.ringtoneEditor.model.PlaybackState
import com.dehar.player.feature.ringtoneEditor.model.RingtoneAudio
import com.dehar.player.feature.ringtoneEditor.model.RingtoneEditorUiState
import com.dehar.player.feature.ringtoneEditor.model.RingtoneType
import com.dehar.player.feature.ringtoneEditor.model.TrimState
import com.dehar.player.feature.ringtoneEditor.repository.RingtoneEditorRepository

/**
 * ViewModel for ringtone editor feature
 */
@HiltViewModel
class RingtoneEditorViewModel @Inject constructor(
    private val repository: RingtoneEditorRepository,
    private val exoPlayer: ExoPlayer
) : ViewModel() {

    private val _uiState = MutableStateFlow(RingtoneEditorUiState())
    val uiState: StateFlow<RingtoneEditorUiState> = _uiState.asStateFlow()

    /**
     * Load audio file and extract waveform
     */
    fun loadAudioFile(audioPath: String, displayName: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val durationMs = repository.getAudioDuration(audioPath)
                val waveformData = repository.extractWaveform(audioPath)

                val audio = RingtoneAudio(
                    id = System.currentTimeMillis(),
                    name = displayName,
                    path = audioPath,
                    durationMs = durationMs
                )

                _uiState.value = _uiState.value.copy(
                    selectedAudio = audio,
                    waveformData = waveformData,
                    trimState = TrimState(
                        startMs = 0,
                        endMs = durationMs
                    ),
                    isLoading = false
                )

                // Load into player for preview
                exoPlayer.setMediaItem(MediaItem.fromUri(audioPath))
                exoPlayer.prepare()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Failed to load audio: ${e.message}"
                )
            }
        }
    }

    /**
     * Update trim start position
     */
    fun updateTrimStart(startMs: Long) {
        val currentTrimState = _uiState.value.trimState
        if (startMs < currentTrimState.endMs) {
            _uiState.value = _uiState.value.copy(
                trimState = currentTrimState.copy(startMs = startMs)
            )
            // Seek player to start position during preview
            exoPlayer.seekTo(startMs)
        }
    }

    /**
     * Update trim end position
     */
    fun updateTrimEnd(endMs: Long) {
        val currentTrimState = _uiState.value.trimState
        val maxDuration = _uiState.value.selectedAudio?.durationMs ?: 0
        
        if (endMs > currentTrimState.startMs && endMs <= maxDuration) {
            _uiState.value = _uiState.value.copy(
                trimState = currentTrimState.copy(endMs = endMs)
            )
        }
    }

    /**
     * Set dragging state for start position
     */
    fun setDraggingStart(isDragging: Boolean) {
        _uiState.value = _uiState.value.copy(
            trimState = _uiState.value.trimState.copy(isDraggingStart = isDragging)
        )
    }

    /**
     * Set dragging state for end position
     */
    fun setDraggingEnd(isDragging: Boolean) {
        _uiState.value = _uiState.value.copy(
            trimState = _uiState.value.trimState.copy(isDraggingEnd = isDragging)
        )
    }

    /**
     * Toggle playback within trimmed region
     */
    fun togglePlayback() {
        if (exoPlayer.isPlaying) {
            exoPlayer.pause()
            _uiState.value = _uiState.value.copy(
                playbackState = _uiState.value.playbackState.copy(isPlaying = false)
            )
        } else {
            val trimStart = _uiState.value.trimState.startMs
            exoPlayer.seekTo(trimStart)
            exoPlayer.play()
            _uiState.value = _uiState.value.copy(
                playbackState = _uiState.value.playbackState.copy(isPlaying = true)
            )
        }
    }

    /**
     * Update playback position
     */
    fun updatePlaybackPosition(positionMs: Long) {
        _uiState.value = _uiState.value.copy(
            playbackState = _uiState.value.playbackState.copy(currentPositionMs = positionMs)
        )
    }

    /**
     * Set volume level (0.0 - 1.0)
     */
    fun setVolume(volume: Float) {
        val clampedVolume = volume.coerceIn(0f, 1f)
        exoPlayer.volume = clampedVolume
        _uiState.value = _uiState.value.copy(
            playbackState = _uiState.value.playbackState.copy(volume = clampedVolume)
        )
    }

    /**
     * Set playback speed
     */
    fun setPlaybackSpeed(speed: Float) {
        exoPlayer.setPlaybackSpeed(speed)
        _uiState.value = _uiState.value.copy(
            playbackState = _uiState.value.playbackState.copy(playbackSpeed = speed)
        )
    }

    /**
     * Set ringtone type
     */
    fun setRingtoneType(ringtoneType: RingtoneType) {
        _uiState.value = _uiState.value.copy(ringtoneType = ringtoneType)
    }

    /**
     * Save as custom ringtone
     */
    fun saveAsCustomRingtone(displayName: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSaving = true)
            try {
                val selectedAudio = _uiState.value.selectedAudio ?: return@launch
                val trimState = _uiState.value.trimState

                // Trim audio if needed
                val trimmedPath = if (trimState.startMs > 0 || trimState.endMs < selectedAudio.durationMs) {
                    val tempFile = java.io.File.createTempFile("trimmed", ".m4a")
                    val trimmed = repository.trimAudio(
                        selectedAudio.path,
                        tempFile.absolutePath,
                        trimState.startMs,
                        trimState.endMs
                    )
                    if (trimmed) tempFile.absolutePath else selectedAudio.path
                } else {
                    selectedAudio.path
                }

                // Save to custom ringtones
                val ringtoneType = _uiState.value.ringtoneType
                val savedPath = repository.saveCustomRingtone(
                    trimmedPath,
                    ringtoneType,
                    displayName
                )

                if (savedPath != null) {
                    _uiState.value = _uiState.value.copy(
                        isSaving = false,
                        errorMessage = null
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isSaving = false,
                        errorMessage = "Failed to save ringtone"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isSaving = false,
                    errorMessage = "Error: ${e.message}"
                )
            }
        }
    }

    /**
     * Set as system ringtone
     */
    fun setAsSystemRingtone(displayName: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSaving = true)
            try {
                val selectedAudio = _uiState.value.selectedAudio ?: return@launch
                val trimState = _uiState.value.trimState
                val ringtoneType = _uiState.value.ringtoneType

                // Trim audio if needed
                val trimmedPath = if (trimState.startMs > 0 || trimState.endMs < selectedAudio.durationMs) {
                    val tempFile = java.io.File.createTempFile("trimmed", ".m4a")
                    val trimmed = repository.trimAudio(
                        selectedAudio.path,
                        tempFile.absolutePath,
                        trimState.startMs,
                        trimState.endMs
                    )
                    if (trimmed) tempFile.absolutePath else selectedAudio.path
                } else {
                    selectedAudio.path
                }

                // Save to custom location first
                val savedPath = repository.saveCustomRingtone(
                    trimmedPath,
                    ringtoneType,
                    displayName
                )

                if (savedPath != null) {
                    // Set as system ringtone
                    val success = repository.setAsRingtone(
                        savedPath,
                        ringtoneType,
                        displayName
                    )

                    _uiState.value = _uiState.value.copy(
                        isSaving = false,
                        errorMessage = if (success) null else "Failed to set as ringtone"
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isSaving = false,
                        errorMessage = "Failed to save ringtone"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isSaving = false,
                    errorMessage = "Error: ${e.message}"
                )
            }
        }
    }

    /**
     * Clear error message
     */
    fun clearErrorMessage() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    override fun onCleared() {
        super.onCleared()
        exoPlayer.release()
    }
}
