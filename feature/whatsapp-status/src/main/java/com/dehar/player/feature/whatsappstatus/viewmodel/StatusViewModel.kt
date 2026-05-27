package com.dehar.player.feature.whatsappstatus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dehar.player.feature.whatsappstatus.model.DownloadProgress
import com.dehar.player.feature.whatsappstatus.model.MonitorState
import com.dehar.player.feature.whatsappstatus.model.StatusBrowserUiState
import com.dehar.player.feature.whatsappstatus.model.StatusContact
import com.dehar.player.feature.whatsappstatus.model.StatusFile
import com.dehar.player.feature.whatsappstatus.repository.StatusRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatusViewModel @Inject constructor(
    private val repository: StatusRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        StatusBrowserUiState(
            statuses = emptyList(),
            contacts = emptyList(),
            downloadProgress = emptyMap(),
            monitorState = MonitorState.IDLE,
            isLoading = false,
            error = null,
            selectedStatus = null,
            downloadedStatuses = emptyList()
        )
    )
    val uiState: StateFlow<StatusBrowserUiState> = _uiState.asStateFlow()

    init {
        scanForStatuses()
        loadDownloadedStatuses()
    }

    /**
     * Scan for WhatsApp status files
     */
    fun scanForStatuses() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, error = null)

                val statuses = repository.scanForStatuses()
                val contacts = groupStatusesByContact(statuses)

                _uiState.value = _uiState.value.copy(
                    statuses = statuses,
                    contacts = contacts,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to scan statuses"
                )
            }
        }
    }

    /**
     * Select a status for viewing
     */
    fun selectStatus(statusFile: StatusFile) {
        _uiState.value = _uiState.value.copy(selectedStatus = statusFile)
    }

    /**
     * Clear status selection
     */
    fun clearStatusSelection() {
        _uiState.value = _uiState.value.copy(selectedStatus = null)
    }

    /**
     * Download single status file
     */
    fun downloadStatus(statusFile: StatusFile) {
        viewModelScope.launch {
            try {
                // Track progress
                val currentProgress = _uiState.value.downloadProgress.toMutableMap()
                currentProgress[statusFile.id] = DownloadProgress(
                    statusId = statusFile.id,
                    totalBytes = statusFile.size,
                    downloadedBytes = 0,
                    startTime = System.currentTimeMillis()
                )
                _uiState.value = _uiState.value.copy(downloadProgress = currentProgress)

                // Download file
                val success = repository.downloadStatus(statusFile)

                if (success) {
                    currentProgress.remove(statusFile.id)
                    _uiState.value = _uiState.value.copy(downloadProgress = currentProgress)
                    
                    // Refresh downloaded list
                    loadDownloadedStatuses()
                } else {
                    _uiState.value = _uiState.value.copy(
                        error = "Failed to download ${statusFile.name}"
                    )
                    currentProgress.remove(statusFile.id)
                    _uiState.value = _uiState.value.copy(downloadProgress = currentProgress)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Download failed"
                )
            }
        }
    }

    /**
     * Download multiple status files
     */
    fun downloadMultipleStatuses(statusFiles: List<StatusFile>) {
        viewModelScope.launch {
            try {
                // Initialize progress tracking
                val currentProgress = _uiState.value.downloadProgress.toMutableMap()
                statusFiles.forEach { status ->
                    currentProgress[status.id] = DownloadProgress(
                        statusId = status.id,
                        totalBytes = status.size,
                        downloadedBytes = 0,
                        startTime = System.currentTimeMillis()
                    )
                }
                _uiState.value = _uiState.value.copy(downloadProgress = currentProgress)

                // Download all files
                repository.downloadMultipleStatuses(statusFiles) { current, total ->
                    // Update progress
                    currentProgress.forEach { (statusId, progress) ->
                        val downloadedBytes = (current.toLong() * progress.totalBytes) / total
                        currentProgress[statusId] = progress.copy(
                            downloadedBytes = downloadedBytes
                        )
                    }
                    _uiState.value = _uiState.value.copy(downloadProgress = currentProgress)
                }

                // Clear progress
                _uiState.value = _uiState.value.copy(downloadProgress = emptyMap())
                
                // Refresh downloaded list
                loadDownloadedStatuses()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Batch download failed"
                )
            }
        }
    }

    /**
     * Toggle monitoring state
     */
    fun toggleMonitoring(enabled: Boolean) {
        viewModelScope.launch {
            try {
                if (enabled) {
                    _uiState.value = _uiState.value.copy(monitorState = MonitorState.MONITORING)
                    
                    val success = repository.monitorStatusFolder { newStatus ->
                        // Add newly detected status to the list
                        val currentStatuses = _uiState.value.statuses.toMutableList()
                        currentStatuses.add(0, newStatus)
                        _uiState.value = _uiState.value.copy(
                            statuses = currentStatuses,
                            contacts = groupStatusesByContact(currentStatuses)
                        )
                    }

                    if (!success) {
                        _uiState.value = _uiState.value.copy(
                            monitorState = MonitorState.ERROR,
                            error = "Failed to start monitoring"
                        )
                    }
                } else {
                    _uiState.value = _uiState.value.copy(monitorState = MonitorState.IDLE)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    monitorState = MonitorState.ERROR,
                    error = e.message ?: "Monitoring toggle failed"
                )
            }
        }
    }

    /**
     * Delete downloaded status
     */
    fun deleteDownloadedStatus(filePath: String) {
        viewModelScope.launch {
            try {
                val success = repository.deleteDownloadedStatus(filePath)
                if (success) {
                    loadDownloadedStatuses()
                } else {
                    _uiState.value = _uiState.value.copy(
                        error = "Failed to delete file"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Deletion failed"
                )
            }
        }
    }

    /**
     * Load downloaded statuses
     */
    private fun loadDownloadedStatuses() {
        viewModelScope.launch {
            try {
                val downloaded = repository.getDownloadedStatuses()
                _uiState.value = _uiState.value.copy(downloadedStatuses = downloaded)
            } catch (e: Exception) {
                // Silently handle error for background load
            }
        }
    }

    /**
     * Get available storage space
     */
    fun getAvailableStorage(): Long {
        return repository.getAvailableStorageSpace()
    }

    /**
     * Get download folder size
     */
    fun getDownloadFolderSize() {
        viewModelScope.launch {
            try {
                val size = repository.getDownloadFolderSize()
                // This can be displayed in UI state if needed
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    /**
     * Check if status folder is accessible
     */
    fun isStatusFolderAccessible(): Boolean {
        return repository.isStatusFolderAccessible()
    }

    /**
     * Clear error message
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    /**
     * Group statuses by contact (based on file timestamp or prefix)
     */
    private fun groupStatusesByContact(statuses: List<StatusFile>): List<StatusContact> {
        return statuses
            .groupBy { it.name.substringBefore("_") }
            .map { (contact, files) ->
                StatusContact(
                    id = contact.hashCode().toString(),
                    name = contact,
                    statusCount = files.size,
                    lastStatusTime = files.maxOfOrNull { it.lastModified } ?: 0L,
                    profileImagePath = null
                )
            }
            .sortedByDescending { it.lastStatusTime }
    }
}
