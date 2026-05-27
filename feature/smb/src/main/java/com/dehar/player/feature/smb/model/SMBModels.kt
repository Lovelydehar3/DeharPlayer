package com.dehar.player.feature.smb.model

/**
 * SMB connection credentials
 */
data class SMBCredentials(
    val username: String,
    val password: String,
    val domain: String = ""
)

/**
 * SMB server/share information
 */
data class SMBServer(
    val id: String,
    val name: String,
    val address: String,
    val port: Int = 445,
    val isOnline: Boolean = false,
    val description: String? = null
)

/**
 * SMB network share
 */
data class SMBShare(
    val id: String,
    val name: String,
    val path: String,
    val type: ShareType = ShareType.DISK,
    val comment: String? = null,
    val isAccessible: Boolean = true
)

/**
 * SMB file/folder entry
 */
data class SMBFileEntry(
    val id: String,
    val name: String,
    val path: String,
    val isDirectory: Boolean,
    val size: Long = 0,
    val lastModified: Long = 0,
    val isHidden: Boolean = false,
    val canRead: Boolean = true,
    val canWrite: Boolean = false
)

/**
 * Share type enumeration
 */
enum class ShareType {
    DISK,
    PRINTER,
    COMMUNICATION,
    IPC
}

/**
 * Connection state
 */
enum class SMBConnectionState {
    IDLE,
    CONNECTING,
    CONNECTED,
    DISCONNECTED,
    ERROR
}

/**
 * Browser navigation state
 */
data class SMBBrowserState(
    val currentPath: String = "",
    val currentShare: SMBShare? = null,
    val navigationHistory: List<String> = emptyList(),
    val breadcrumbs: List<String> = emptyList()
) {
    fun canGoBack(): Boolean = navigationHistory.isNotEmpty()
}

/**
 * SMB browser UI state
 */
data class SMBBrowserUiState(
    val servers: List<SMBServer> = emptyList(),
    val shares: List<SMBShare> = emptyList(),
    val files: List<SMBFileEntry> = emptyList(),
    val connectionState: SMBConnectionState = SMBConnectionState.IDLE,
    val selectedServer: SMBServer? = null,
    val selectedShare: SMBShare? = null,
    val browserState: SMBBrowserState = SMBBrowserState(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val credentials: SMBCredentials? = null,
    val showCredentialsDialog: Boolean = false,
    val enableNetworkDiscovery: Boolean = false
)
