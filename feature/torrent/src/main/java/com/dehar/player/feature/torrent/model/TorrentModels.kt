package com.dehar.player.feature.torrent.model

/**
 * Torrent file information
 */
data class TorrentFile(
    val id: String,
    val name: String,
    val path: String,
    val size: Long,
    val mimeType: String? = null
)

/**
 * Torrent metadata
 */
data class TorrentMetadata(
    val infohash: String,
    val name: String,
    val totalSize: Long,
    val files: List<TorrentFile> = emptyList(),
    val createdDate: Long = 0,
    val comment: String? = null,
    val createdBy: String? = null
)

/**
 * Torrent peer information
 */
data class TorrentPeer(
    val ip: String,
    val port: Int,
    val country: String? = null,
    val uploadSpeed: Long = 0,
    val downloadSpeed: Long = 0
)

/**
 * Download state enumeration
 */
enum class DownloadState {
    IDLE,
    METADATA_LOADING,
    DOWNLOADING,
    STREAMING,
    PAUSED,
    COMPLETED,
    ERROR
}

/**
 * Torrent download state
 */
data class TorrentDownloadState(
    val infohash: String,
    val state: DownloadState = DownloadState.IDLE,
    val progress: Float = 0f,
    val downloadedBytes: Long = 0,
    val totalBytes: Long = 0,
    val downloadSpeed: Long = 0,
    val uploadSpeed: Long = 0,
    val peers: Int = 0,
    val seeders: Int = 0,
    val leechers: Int = 0,
    val eta: Long = 0,
    val errorMessage: String? = null
) {
    fun getProgressPercentage(): Float = (downloadedBytes.toFloat() / totalBytes) * 100f
}

/**
 * Torrent browser UI state
 */
data class TorrentBrowserUiState(
    val magnetUri: String = "",
    val torrentMetadata: TorrentMetadata? = null,
    val selectedFile: TorrentFile? = null,
    val downloadState: TorrentDownloadState? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val showMagnetDialog: Boolean = false,
    val availablePeers: List<TorrentPeer> = emptyList(),
    val isStreaming: Boolean = false
)
