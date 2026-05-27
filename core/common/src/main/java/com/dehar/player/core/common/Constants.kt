package com.dehar.player.core.common

/**
 * Application-wide constants for Dehar Player
 */
object Constants {
    // File & Folder
    const val APP_NAME = "Dehar Player"
    const val PREFS_NAME = "dehar_prefs"
    const val DB_NAME = "dehar_database"
    
    // Paths
    const val PRIVATE_VAULT_DIR = "private_vault"
    const val RECYCLE_BIN_DIR = ".dehar_recycle_bin"
    const val THUMBNAIL_CACHE_DIR = "thumbnails"
    const val SUBTITLE_CACHE_DIR = "subtitles"
    const val RINGTONE_OUTPUT_DIR = "Ringtones/DeharPlayer"
    const val TRANSFER_DIR = "Dehar Transfer"
    const val TORRENT_TEMP_DIR = "torrent_temp"
    
    // Notification Channels
    const val PLAYBACK_NOTIFICATION_CHANNEL = "playback_notification"
    const val DOWNLOAD_NOTIFICATION_CHANNEL = "download_notification"
    const val TRANSFER_NOTIFICATION_CHANNEL = "transfer_notification"
    const val TORRENT_NOTIFICATION_CHANNEL = "torrent_notification"
    const val FOREGROUND_SERVICE_CHANNEL = "foreground_service"
    
    // Notification IDs
    const val PLAYBACK_NOTIFICATION_ID = 1001
    const val DOWNLOAD_NOTIFICATION_ID = 1002
    const val TRANSFER_NOTIFICATION_ID = 1003
    const val TORRENT_NOTIFICATION_ID = 1004
    
    // Request Codes
    const val REQUEST_STORAGE_PERMISSION = 1001
    const val REQUEST_NOTIFICATION_PERMISSION = 1002
    const val REQUEST_BIOMETRIC_PERMISSION = 1003
    const val REQUEST_RINGTONE_PERMISSION = 1004
    const val REQUEST_SYSTEM_ALERT_PERMISSION = 1005
    
    // Media Extensions
    val VIDEO_EXTENSIONS = setOf(
        "mp4", "mkv", "avi", "mov", "wmv", "flv", "ts", "m4v", "3gp", 
        "rmvb", "webm", "ogv", "divx", "vob", "m2ts", "mts", "mxf",
        "asf", "f4v", "m2v", "m4p", "mpe", "mpg", "mpv", "nsv",
        "ogg", "qt", "rm", "svi", "yuv"
    )
    
    val AUDIO_EXTENSIONS = setOf(
        "mp3", "flac", "ogg", "wav", "aac", "m4a", "opus", "wma", 
        "ape", "tta", "mka", "alac", "ac3", "dts", "amr", "awb",
        "mid", "midi", "midi", "mxmf", "xmf", "imy", "rtttl", "ota"
    )
    
    val SUBTITLE_EXTENSIONS = setOf(
        "srt", "ass", "ssa", "sub", "idx", "smi", "vtt", "mpl", 
        "txt", "psb", "pjs", "rt", "ss a", "stl", "usf", "dks",
        "scc", "sami", "smil", "aqt", "cdg", "subviewer", "tt"
    )
    
    // Video Resolutions
    const val RESOLUTION_4K_WIDTH = 3840
    const val RESOLUTION_FHD_WIDTH = 1920
    const val RESOLUTION_HD_WIDTH = 1280
    const val RESOLUTION_SD_WIDTH = 720
    
    // Duration Thresholds (in milliseconds)
    const val MIN_VIDEO_DURATION = 10_000L // 10 seconds
    const val MIN_AUDIO_DURATION = 30_000L // 30 seconds
    const val SHORT_VIDEO_DURATION = 30_000L // 30 seconds
    const val LONG_VIDEO_DURATION = 3_600_000L // 1 hour
    
    // File Size Thresholds (in bytes)
    const val MIN_VIDEO_SIZE = 500 * 1024L // 500 KB
    const val LARGE_FILE_THRESHOLD = 500 * 1024 * 1024L // 500 MB
    
    // Player Defaults
    const val DEFAULT_PLAYBACK_SPEED = 1.0f
    const val DEFAULT_DOUBLE_TAP_SEEK_MS = 10_000L
    const val DEFAULT_LONG_PRESS_SPEED = 2.0f
    const val DEFAULT_SUBTITLE_SIZE = 18
    const val DEFAULT_SUBTITLE_POSITION = 0.85f
    
    // Buffer Defaults (in milliseconds)
    const val DEFAULT_MIN_BUFFER_MS = 15_000
    const val DEFAULT_MAX_BUFFER_MS = 50_000
    const val DEFAULT_BUFFER_FOR_PLAYBACK_MS = 2_500
    const val DEFAULT_BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_MS = 5_000
    
    // Network
    const val DEFAULT_NETWORK_TIMEOUT_SECONDS = 30
    const val DEFAULT_STREAM_CACHE_SIZE_MB = 512
    const val DEFAULT_TORRENT_MAX_CONNECTIONS = 200
    const val DEFAULT_TRANSFER_PORT = 8888
    
    // Scan Defaults
    const val DEFAULT_SCAN_INTERVAL_MINUTES = 30
    const val DEFAULT_THUMBNAIL_CACHE_SIZE_MB = 256
    
    // Privacy
    const val DEFAULT_VAULT_AUTO_LOCK_MINUTES = 5
    const val DEFAULT_HISTORY_RETENTION_DAYS = 90
    const val DEFAULT_RECYCLE_BIN_RETENTION_DAYS = 30
    
    // UI
    const val ANIMATION_DURATION_MS = 300
    const val CONTROLS_AUTO_HIDE_DELAY_MS = 3_500
    const val RESUME_PROMPT_THRESHOLD_MS = 15_000L
    
    // EQ Bands (frequencies in Hz)
    val EQ_BAND_FREQUENCIES = floatArrayOf(60f, 230f, 910f, 3000f, 14000f)
    const val EQ_MIN_DB = -15f
    const val EQ_MAX_DB = 15f
    
    // Biometric
    const val BIOMETRIC_ERROR_TIMEOUT_MS = 3_000L
    
    // Work Manager
    const val THUMBNAIL_GENERATION_WORK_NAME = "thumbnail_generation"
    const val MEDIA_SCAN_WORK_NAME = "media_scan"
    const val RECYCLE_BIN_CLEANUP_WORK_NAME = "recycle_bin_cleanup"
    const val CLOUD_BACKUP_WORK_NAME = "cloud_backup"
}