package com.dehar.player.feature.whatsappstatus.model

/**
 * WhatsApp status file information
 */
data class StatusFile(
    val id: String,
    val name: String,
    val path: String,
    val size: Long = 0,
    val lastModified: Long = 0,
    val type: StatusFileType = StatusFileType.UNKNOWN,
    val mimeType: String? = null,
    val duration: Long? = null,
    val thumbnailPath: String? = null,
    val isDownloaded: Boolean = false,
    val downloadPath: String? = null
)

/**
 * Status file type enumeration
 */
enum class StatusFileType {
    IMAGE,
    VIDEO,
    UNKNOWN
}

/**
 * Contact for WhatsApp status
 */
data class StatusContact(
    val id: String,
    val name: String,
    val displayName: String? = null,
    val statusCount: Int = 0,
    val lastStatusTime: Long = 0,
    val profilePhotoPath: String? = null
)

/**
 * Download progress information
 */
data class DownloadProgress(
    val fileId: String,
    val totalBytes: Long = 0,
    val downloadedBytes: Long = 0,
    val isDownloading: Boolean = false,
    val error: String? = null
) {
    fun getProgressPercentage(): Float {
        return if (totalBytes > 0) {
            (downloadedBytes.toFloat() / totalBytes.toFloat()) * 100f
        } else {
            0f
        }
    }
}

/**
 * Monitor state for status watching
 */
enum class MonitorState {
    IDLE,
    MONITORING,
    PAUSED,
    ERROR
}

/**
 * WhatsApp Status browser UI state
 */
data class StatusBrowserUiState(
    val statuses: List<StatusFile> = emptyList(),
    val contacts: List<StatusContact> = emptyList(),
    val selectedContact: StatusContact? = null,
    val selectedStatus: StatusFile? = null,
    val monitorState: MonitorState = MonitorState.IDLE,
    val downloadProgress: Map<String, DownloadProgress> = emptyMap(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val showMonitorDialog: Boolean = false,
    val totalStatusesFound: Int = 0,
    val downloadedCount: Int = 0,
    val downloadFolder: String = ""
)
