package com.dehar.player.feature.usb.model

/**
 * USB storage device information
 */
data class USBDevice(
    val id: String,
    val name: String,
    val path: String,
    val totalSpace: Long = 0,
    val availableSpace: Long = 0,
    val isReadable: Boolean = true,
    val isWritable: Boolean = false,
    val isEjectable: Boolean = true
) {
    val usedSpace: Long get() = totalSpace - availableSpace
    
    fun getUsagePercentage(): Float {
        return if (totalSpace > 0) {
            (usedSpace.toFloat() / totalSpace.toFloat()) * 100f
        } else {
            0f
        }
    }
}

/**
 * USB file/folder entry
 */
data class USBFileEntry(
    val id: String,
    val name: String,
    val path: String,
    val isDirectory: Boolean,
    val size: Long = 0,
    val lastModified: Long = 0,
    val isHidden: Boolean = false,
    val canRead: Boolean = true,
    val canWrite: Boolean = false,
    val mimeType: String? = null
)

/**
 * USB permission state
 */
enum class USBPermissionState {
    NOT_REQUESTED,
    PENDING,
    GRANTED,
    DENIED
}

/**
 * Browser navigation state
 */
data class USBBrowserState(
    val currentPath: String = "",
    val currentDevice: USBDevice? = null,
    val navigationHistory: List<String> = emptyList(),
    val breadcrumbs: List<String> = emptyList()
) {
    fun canGoBack(): Boolean = navigationHistory.isNotEmpty()
}

/**
 * USB browser UI state
 */
data class USBBrowserUiState(
    val devices: List<USBDevice> = emptyList(),
    val files: List<USBFileEntry> = emptyList(),
    val selectedDevice: USBDevice? = null,
    val browserState: USBBrowserState = USBBrowserState(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val permissionState: USBPermissionState = USBPermissionState.NOT_REQUESTED,
    val showPermissionRequest: Boolean = false
)
