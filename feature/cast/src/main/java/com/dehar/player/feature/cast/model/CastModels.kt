package com.dehar.player.feature.cast.model

/**
 * Cast device information
 */
data class CastDevice(
    val id: String,
    val name: String,
    val modelName: String? = null,
    val ipAddress: String? = null,
    val port: Int = 8009,
    val isConnected: Boolean = false,
    val isSelected: Boolean = false,
    val deviceType: CastDeviceType = CastDeviceType.GENERIC
)

/**
 * Cast device type enumeration
 */
enum class CastDeviceType {
    GENERIC,
    CHROMECAST,
    CHROMECAST_AUDIO,
    CHROMECAST_WITH_GOOGLE_TV,
    GOOGLE_HOME,
    GOOGLE_HOME_MINI,
    GOOGLE_HOME_MAX,
    SMART_DISPLAY,
    TV
}

/**
 * Cast session state
 */
enum class CastSessionState {
    IDLE,
    CONNECTING,
    CONNECTED,
    PLAYING,
    PAUSED,
    STOPPED,
    DISCONNECTING,
    ERROR
}

/**
 * Cast media information
 */
data class CastMediaInfo(
    val title: String,
    val subtitle: String? = null,
    val duration: Long = 0,
    val position: Long = 0,
    val url: String? = null,
    val mimeType: String? = null,
    val artworkUrl: String? = null
)

/**
 * Cast session information
 */
data class CastSession(
    val sessionId: String,
    val device: CastDevice,
    val state: CastSessionState = CastSessionState.IDLE,
    val mediaInfo: CastMediaInfo? = null,
    val volume: Float = 0.5f,
    val isMuted: Boolean = false
)

/**
 * Cast browser UI state
 */
data class CastBrowserUiState(
    val availableDevices: List<CastDevice> = emptyList(),
    val selectedDevice: CastDevice? = null,
    val castSession: CastSession? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val showDeviceSelector: Boolean = false,
    val scanningDevices: Boolean = false
)
