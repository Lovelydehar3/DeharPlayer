package com.dehar.player.feature.torrent.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.dehar.player.feature.torrent.model.DownloadState
import com.dehar.player.feature.torrent.model.TorrentBrowserUiState
import com.dehar.player.feature.torrent.model.TorrentDownloadState
import com.dehar.player.feature.torrent.repository.TorrentRepository

/**
 * ViewModel for torrent browser feature
 */
@HiltViewModel
class TorrentBrowserViewModel @Inject constructor(
    private val repository: TorrentRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TorrentBrowserUiState())
    val uiState: StateFlow<TorrentBrowserUiState> = _uiState.asStateFlow()

    /**
     * Load torrent metadata from magnet URI
     */
    fun loadTorrentMetadata(magnetUri: String) {
        // Validate magnet URI
        if (!repository.isValidMagnetUri(magnetUri)) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Invalid magnet URI format"
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                magnetUri = magnetUri,
                errorMessage = null
            )
            try {
                val metadata = repository.loadTorrentMetadata(magnetUri)
                if (metadata != null) {
                    _uiState.value = _uiState.value.copy(
                        torrentMetadata = metadata,
                        isLoading = false,
                        showMagnetDialog = false
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Failed to load torrent metadata"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Error loading torrent: ${e.message}"
                )
            }
        }
    }

    /**
     * Start streaming torrent file
     */
    fun startStreaming(fileIndex: Int) {
        val metadata = _uiState.value.torrentMetadata ?: return
        val file = metadata.files.getOrNull(fileIndex) ?: return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                selectedFile = file
            )
            try {
                val streamUrl = repository.startStreaming(
                    _uiState.value.magnetUri,
                    fileIndex
                )

                if (streamUrl != null) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isStreaming = true,
                        downloadState = TorrentDownloadState(
                            infohash = metadata.infohash,
                            state = DownloadState.STREAMING
                        )
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Failed to start streaming"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Streaming error: ${e.message}"
                )
            }
        }
    }

    /**
     * Pause streaming
     */
    fun pauseStreaming() {
        val infohash = _uiState.value.torrentMetadata?.infohash ?: return
        
        viewModelScope.launch {
            try {
                repository.pauseStreaming(infohash)
                val currentState = _uiState.value.downloadState
                if (currentState != null) {
                    _uiState.value = _uiState.value.copy(
                        downloadState = currentState.copy(state = DownloadState.PAUSED)
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Pause error: ${e.message}"
                )
            }
        }
    }

    /**
     * Resume streaming
     */
    fun resumeStreaming() {
        val infohash = _uiState.value.torrentMetadata?.infohash ?: return

        viewModelScope.launch {
            try {
                repository.resumeStreaming(infohash)
                val currentState = _uiState.value.downloadState
                if (currentState != null) {
                    _uiState.value = _uiState.value.copy(
                        downloadState = currentState.copy(state = DownloadState.STREAMING)
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Resume error: ${e.message}"
                )
            }
        }
    }

    /**
     * Stop streaming and clean up
     */
    fun stopStreaming() {
        val infohash = _uiState.value.torrentMetadata?.infohash ?: return

        viewModelScope.launch {
            try {
                repository.stopStreaming(infohash)
                _uiState.value = TorrentBrowserUiState()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Stop error: ${e.message}"
                )
            }
        }
    }

    /**
     * Load available peers for torrent
     */
    fun loadAvailablePeers() {
        val infohash = _uiState.value.torrentMetadata?.infohash ?: return

        viewModelScope.launch {
            try {
                val peers = repository.getAvailablePeers(infohash)
                _uiState.value = _uiState.value.copy(
                    availablePeers = peers
                )
            } catch (e: Exception) {
                // Silently fail for peer loading
            }
        }
    }

    /**
     * Show magnet input dialog
     */
    fun showMagnetDialog() {
        _uiState.value = _uiState.value.copy(showMagnetDialog = true)
    }

    /**
     * Hide magnet input dialog
     */
    fun hideMagnetDialog() {
        _uiState.value = _uiState.value.copy(showMagnetDialog = false)
    }

    /**
     * Clear error message
     */
    fun clearErrorMessage() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    /**
     * Reset to initial state
     */
    fun reset() {
        _uiState.value = TorrentBrowserUiState()
    }

    override fun onCleared() {
        super.onCleared()
        stopStreaming()
    }
}
