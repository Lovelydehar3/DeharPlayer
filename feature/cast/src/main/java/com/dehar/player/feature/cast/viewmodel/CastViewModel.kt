package com.dehar.player.feature.cast.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.dehar.player.feature.cast.model.CastBrowserUiState
import com.dehar.player.feature.cast.model.CastDevice
import com.dehar.player.feature.cast.repository.CastRepository

/**
 * ViewModel for Cast integration feature
 */
@HiltViewModel
class CastViewModel @Inject constructor(
    private val repository: CastRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CastBrowserUiState())
    val uiState: StateFlow<CastBrowserUiState> = _uiState.asStateFlow()

    /**
     * Scan for available Cast devices
     */
    fun scanForDevices() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(scanningDevices = true)
            try {
                val devices = repository.scanForDevices()
                _uiState.value = _uiState.value.copy(
                    availableDevices = devices,
                    scanningDevices = false,
                    errorMessage = if (devices.isEmpty()) {
                        "No Cast devices found"
                    } else {
                        null
                    }
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    scanningDevices = false,
                    errorMessage = "Failed to scan devices: ${e.message}"
                )
            }
        }
    }

    /**
     * Connect to Cast device
     */
    fun connectToDevice(device: CastDevice) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                selectedDevice = device
            )
            try {
                val success = repository.connectToDevice(device)
                
                if (success) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        castSession = repository.getCurrentSession(),
                        showDeviceSelector = false,
                        errorMessage = null
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Failed to connect to device"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Connection error: ${e.message}"
                )
            }
        }
    }

    /**
     * Disconnect from Cast device
     */
    fun disconnectFromDevice() {
        viewModelScope.launch {
            try {
                repository.disconnectFromDevice()
                _uiState.value = CastBrowserUiState()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Disconnect error: ${e.message}"
                )
            }
        }
    }

    /**
     * Cast media to device
     */
    fun castMedia(
        mediaUrl: String,
        title: String,
        subtitle: String? = null,
        mimeType: String = "video/mp4",
        artworkUrl: String? = null,
        duration: Long = 0
    ) {
        viewModelScope.launch {
            try {
                val success = repository.castMedia(
                    mediaUrl, title, subtitle, mimeType, artworkUrl, duration
                )
                
                if (success) {
                    _uiState.value = _uiState.value.copy(
                        castSession = repository.getCurrentSession(),
                        errorMessage = null
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "Failed to cast media"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Cast error: ${e.message}"
                )
            }
        }
    }

    /**
     * Play media on Cast device
     */
    fun play() {
        viewModelScope.launch {
            try {
                repository.play()
                _uiState.value = _uiState.value.copy(
                    castSession = repository.getCurrentSession()
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Play error: ${e.message}"
                )
            }
        }
    }

    /**
     * Pause media on Cast device
     */
    fun pause() {
        viewModelScope.launch {
            try {
                repository.pause()
                _uiState.value = _uiState.value.copy(
                    castSession = repository.getCurrentSession()
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Pause error: ${e.message}"
                )
            }
        }
    }

    /**
     * Stop casting
     */
    fun stop() {
        viewModelScope.launch {
            try {
                repository.stopCasting()
                _uiState.value = _uiState.value.copy(
                    castSession = repository.getCurrentSession()
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Stop error: ${e.message}"
                )
            }
        }
    }

    /**
     * Seek to position
     */
    fun seekTo(positionMs: Long) {
        viewModelScope.launch {
            try {
                repository.seekTo(positionMs)
                _uiState.value = _uiState.value.copy(
                    castSession = repository.getCurrentSession()
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Seek error: ${e.message}"
                )
            }
        }
    }

    /**
     * Set volume
     */
    fun setVolume(volume: Float) {
        viewModelScope.launch {
            try {
                repository.setVolume(volume)
                _uiState.value = _uiState.value.copy(
                    castSession = repository.getCurrentSession()
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Volume error: ${e.message}"
                )
            }
        }
    }

    /**
     * Toggle mute
     */
    fun toggleMute() {
        viewModelScope.launch {
            try {
                repository.toggleMute()
                _uiState.value = _uiState.value.copy(
                    castSession = repository.getCurrentSession()
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Mute error: ${e.message}"
                )
            }
        }
    }

    /**
     * Show device selector
     */
    fun showDeviceSelector() {
        _uiState.value = _uiState.value.copy(showDeviceSelector = true)
    }

    /**
     * Hide device selector
     */
    fun hideDeviceSelector() {
        _uiState.value = _uiState.value.copy(showDeviceSelector = false)
    }

    /**
     * Clear error message
     */
    fun clearErrorMessage() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    override fun onCleared() {
        super.onCleared()
        try {
            viewModelScope.launch {
                if (repository.isDeviceConnected()) {
                    disconnectFromDevice()
                }
            }
        } catch (e: Exception) {
            // Ignore cleanup errors
        }
    }
}
