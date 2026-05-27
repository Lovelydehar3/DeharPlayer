package com.dehar.player.core.common

/**
 * Global constants for Dehar Player
 */
object DeharConstants {
    // Database
    const val DB_NAME = "dehar_player.db"
    
    // Preferences
    const val PREFERENCES_NAME = "dehar_preferences"
    
    // MMKV
    const val MMKV_ID = "dehar_player"
    
    // Notification Channels
    const val MUSIC_SERVICE_CHANNEL_ID = "music_playback"
    const val VIDEO_SERVICE_CHANNEL_ID = "video_playback"
    const val DOWNLOADS_CHANNEL_ID = "downloads"
    const val TORRENT_CHANNEL_ID = "torrent"
    const val TRANSFER_CHANNEL_ID = "file_transfer"
    
    // File Paths
    const val RECYCLE_BIN_FOLDER = ".dehar_recycle_bin"
    const val PRIVATE_VAULT_FOLDER = ".dehar_vault"
    const val TEMP_FILES_FOLDER = ".dehar_temp"
    const val THUMBNAIL_CACHE_FOLDER = ".dehar_thumbnails"
    
    // Media Scanning
    const val DEFAULT_SCAN_INTERVAL_MINUTES = 30
    const val MIN_VIDEO_DURATION_SECONDS = 10
    const val MIN_VIDEO_SIZE_KB = 500
    const val MIN_AUDIO_DURATION_SECONDS = 30
    
    // Playback
    const val DEFAULT_BUFFER_MS = 50000
    const val MIN_BUFFER_MS = 15000
    const val BUFFER_FOR_PLAYBACK_MS = 2500
    const val BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_MS = 5000
    
    // UI
    const val CONTROLS_AUTO_HIDE_DELAY_MS = 3500L
    const val GESTURE_DEBOUNCE_MS = 300L
    const val THUMBNAIL_PREVIEW_DEBOUNCE_MS = 200L
    
    // Networking
    const val NETWORK_TIMEOUT_SECONDS = 30
    const val HTTP_TRANSFER_PORT = 8888
    const val TORRENT_MAX_CONNECTIONS = 200
    
    // Video/Audio Settings
    const val AUTOPLAY_COUNTDOWN_DELAY_MS = 3000
    const val DOUBLE_TAP_SEEK_MS = 10000L
    const val LONG_PRESS_SPEED_FACTOR = 2.0f
    
    // Recycle Bin
    const val RECYCLE_BIN_RETENTION_DAYS = 30
    
    // Encryption
    const val VAULT_KEY_ALIAS = "dehar_vault_key"
    
    // API Keys
    const val OPENSUBTITLES_API_VERSION = "v1"
    const val OPENSUBTITLES_BASE_URL = "https://api.opensubtitles.com/"
}
