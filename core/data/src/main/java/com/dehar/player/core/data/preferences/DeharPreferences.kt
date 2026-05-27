package com.dehar.player.core.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "dehar_preferences")

@Singleton
class DeharPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val dataStore = context.dataStore

    companion object {
        // 3.1 Playback Preferences
        val KEY_RESUME_PLAYBACK = booleanPreferencesKey("resume_playback")
        val KEY_REMEMBER_LAST_POSITION = booleanPreferencesKey("remember_last_position")
        val KEY_DEFAULT_DECODER = stringPreferencesKey("default_decoder")
        val KEY_PREFER_HW_DECODER = booleanPreferencesKey("prefer_hw_decoder")
        val KEY_HW_PLUS_DECODER = booleanPreferencesKey("hw_plus_decoder")
        val KEY_DEFAULT_SPEED = floatPreferencesKey("default_speed")
        val KEY_SKIP_SILENCE = booleanPreferencesKey("skip_silence")
        val KEY_AUDIO_FOCUS = booleanPreferencesKey("audio_focus")
        val KEY_AUDIO_OFFLOAD = booleanPreferencesKey("audio_offload")
        val KEY_TUNNEL_PLAYBACK = booleanPreferencesKey("tunnel_playback")
        val KEY_SURFACE_TYPE = stringPreferencesKey("surface_type")
        val KEY_MAX_BUFFER_MS = intPreferencesKey("max_buffer_ms")
        val KEY_MIN_BUFFER_MS = intPreferencesKey("min_buffer_ms")
        val KEY_BUFFER_FOR_PLAYBACK_MS = intPreferencesKey("buffer_for_playback_ms")
        val KEY_BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_MS = intPreferencesKey("buffer_for_playback_after_rebuffer_ms")
        val KEY_VIDEO_SCALING_MODE = stringPreferencesKey("video_scaling_mode")
        val KEY_LOOP_MODE = stringPreferencesKey("loop_mode")
        val KEY_AUTOPLAY_NEXT = booleanPreferencesKey("autoplay_next")
        val KEY_AUTOPLAY_DELAY_MS = intPreferencesKey("autoplay_delay_ms")
        val KEY_BACKGROUND_PLAY_VIDEO = booleanPreferencesKey("background_play_video")
        val KEY_PIP_AUTO_ENTER = booleanPreferencesKey("pip_auto_enter")
        val KEY_SCREEN_BRIGHTNESS = floatPreferencesKey("screen_brightness")
        val KEY_KEEP_SCREEN_ON = booleanPreferencesKey("keep_screen_on")
        val KEY_SCREEN_ORIENTATION = stringPreferencesKey("screen_orientation")
        val KEY_DOUBLE_TAP_SEEK_MS = intPreferencesKey("double_tap_seek_ms")
        val KEY_LONG_PRESS_SPEED = floatPreferencesKey("long_press_speed")
        val KEY_SWIPE_SENSITIVITY = floatPreferencesKey("swipe_sensitivity")
        val KEY_GESTURE_SEEK_ENABLED = booleanPreferencesKey("gesture_seek_enabled")
        val KEY_GESTURE_VOLUME_ENABLED = booleanPreferencesKey("gesture_volume_enabled")
        val KEY_GESTURE_BRIGHTNESS_ENABLED = booleanPreferencesKey("gesture_brightness_enabled")
        val KEY_PINCH_ZOOM_ENABLED = booleanPreferencesKey("pinch_zoom_enabled")
        val KEY_VOLUME_BOOST_ENABLED = booleanPreferencesKey("volume_boost_enabled")
        val KEY_MAX_VOLUME_BOOST = intPreferencesKey("max_volume_boost")

        // 3.2 Subtitle Preferences
        val KEY_SUBTITLE_AUTO_LOAD = booleanPreferencesKey("subtitle_auto_load")
        val KEY_SUBTITLE_AUTO_SEARCH_ONLINE = booleanPreferencesKey("subtitle_auto_search_online")
        val KEY_SUBTITLE_DEFAULT_LANGUAGE = stringPreferencesKey("subtitle_default_language")
        val KEY_SUBTITLE_FONT_NAME = stringPreferencesKey("subtitle_font_name")
        val KEY_SUBTITLE_FONT_SIZE = intPreferencesKey("subtitle_font_size")
        val KEY_SUBTITLE_FONT_COLOR = stringPreferencesKey("subtitle_font_color")
        val KEY_SUBTITLE_BACKGROUND_COLOR = stringPreferencesKey("subtitle_background_color")
        val KEY_SUBTITLE_STROKE_COLOR = stringPreferencesKey("subtitle_stroke_color")
        val KEY_SUBTITLE_STROKE_WIDTH = floatPreferencesKey("subtitle_stroke_width")
        val KEY_SUBTITLE_BOLD = booleanPreferencesKey("subtitle_bold")
        val KEY_SUBTITLE_ITALIC = booleanPreferencesKey("subtitle_italic")
        val KEY_SUBTITLE_POSITION = floatPreferencesKey("subtitle_position")
        val KEY_SUBTITLE_ENCODING = stringPreferencesKey("subtitle_encoding")
        val KEY_SUBTITLE_SYNC_OFFSET_MS = longPreferencesKey("subtitle_sync_offset_ms")
        val KEY_SUBTITLE_VISIBLE = booleanPreferencesKey("subtitle_visible")
        val KEY_SUBTITLE_SCALE = floatPreferencesKey("subtitle_scale")
        val KEY_SSA_OVERRIDE_STYLE = booleanPreferencesKey("ssa_override_style")

        // 3.3 Audio Preferences
        val KEY_EQ_ENABLED = booleanPreferencesKey("eq_enabled")
        val KEY_ACTIVE_EQ_PRESET_ID = longPreferencesKey("active_eq_preset_id")
        val KEY_BASS_BOOST_ENABLED = booleanPreferencesKey("bass_boost_enabled")
        val KEY_BASS_BOOST_STRENGTH = intPreferencesKey("bass_boost_strength") // Short represented as Int
        val KEY_VIRTUALIZER_ENABLED = booleanPreferencesKey("virtualizer_enabled")
        val KEY_VIRTUALIZER_STRENGTH = intPreferencesKey("virtualizer_strength")
        val KEY_LOUDNESS_ENHANCER_ENABLED = booleanPreferencesKey("loudness_enhancer_enabled")
        val KEY_LOUDNESS_ENHANCER_GAIN_MDB = intPreferencesKey("loudness_enhancer_gain_mdb")
        val KEY_AUDIO_TRACK_DEFAULT = intPreferencesKey("audio_track_default")
        val KEY_PREFER_AUDIO_LANGUAGE = stringPreferencesKey("prefer_audio_language")
        val KEY_STEREO_MODE = stringPreferencesKey("stereo_mode")
        val KEY_SLEEP_TIMER_MINUTES = intPreferencesKey("sleep_timer_minutes")
        val KEY_SLEEP_TIMER_FINISH_CURRENT = booleanPreferencesKey("sleep_timer_finish_current")

        // 3.4 Library & Scanning Preferences
        val KEY_SCAN_INTERVAL_MINUTES = intPreferencesKey("scan_interval_minutes")
        val KEY_SCAN_ON_APP_START = booleanPreferencesKey("scan_on_app_start")
        val KEY_INCLUDE_HIDDEN_FOLDERS = booleanPreferencesKey("include_hidden_folders")
        val KEY_MIN_VIDEO_DURATION_SECS = intPreferencesKey("min_video_duration_secs")
        val KEY_MIN_VIDEO_SIZE_KB = intPreferencesKey("min_video_size_kb")
        val KEY_MIN_AUDIO_DURATION_SECS = intPreferencesKey("min_audio_duration_secs")
        val KEY_EXCLUDED_FOLDERS = stringSetPreferencesKey("excluded_folders")
        val KEY_INCLUDED_FOLDERS = stringSetPreferencesKey("included_folders")
        val KEY_VIDEO_FILE_EXTENSIONS = stringSetPreferencesKey("video_file_extensions")
        val KEY_AUDIO_FILE_EXTENSIONS = stringSetPreferencesKey("audio_file_extensions")
        val KEY_DEFAULT_VIDEO_SORT = stringPreferencesKey("default_video_sort")
        val KEY_DEFAULT_AUDIO_SORT = stringPreferencesKey("default_audio_sort")
        val KEY_DEFAULT_VIDEO_LAYOUT = stringPreferencesKey("default_video_layout")
        val KEY_DEFAULT_AUDIO_LAYOUT = stringPreferencesKey("default_audio_layout")
        val KEY_GROUP_BY_FOLDER = booleanPreferencesKey("group_by_folder")
        val KEY_SHOW_FILE_EXTENSION = booleanPreferencesKey("show_file_extension")
        val KEY_SHOW_DURATION_BADGE = booleanPreferencesKey("show_duration_badge")
        val KEY_SHOW_RESOLUTION_BADGE = booleanPreferencesKey("show_resolution_badge")
        val KEY_THUMBNAIL_QUALITY = stringPreferencesKey("thumbnail_quality")
        val KEY_THUMBNAIL_CACHE_SIZE_MB = intPreferencesKey("thumbnail_cache_size_mb")

        // 3.5 UI / Theme Preferences
        val KEY_THEME_MODE = stringPreferencesKey("theme_mode")
        val KEY_THEME_ACCENT = stringPreferencesKey("theme_accent")
        val KEY_DYNAMIC_COLOR = booleanPreferencesKey("dynamic_color")
        val KEY_FONT_SCALE = floatPreferencesKey("font_scale")
        val KEY_COMPACT_MODE = booleanPreferencesKey("compact_mode")
        val KEY_SHOW_BOTTOM_NAV_LABELS = booleanPreferencesKey("show_bottom_nav_labels")
        val KEY_SWIPE_BETWEEN_TABS = booleanPreferencesKey("swipe_between_tabs")
        val KEY_TRANSITION_ANIMATIONS = booleanPreferencesKey("transition_animations")
        val KEY_HAPTIC_FEEDBACK = booleanPreferencesKey("haptic_feedback")
        val KEY_STATUS_BAR_TRANSPARENT = booleanPreferencesKey("status_bar_transparent")
        val KEY_NAVIGATION_BAR_TRANSPARENT = booleanPreferencesKey("navigation_bar_transparent")
        val KEY_SHOW_NOW_PLAYING_BAR = booleanPreferencesKey("show_now_playing_bar")
        val KEY_SHOW_VIDEO_PREVIEW_SEEK = booleanPreferencesKey("show_video_preview_seek")
        val KEY_GRID_ROUNDED_CORNERS = booleanPreferencesKey("grid_rounded_corners")

        // 3.6 Privacy & Security Preferences
        val KEY_PRIVATE_VAULT_ENABLED = booleanPreferencesKey("private_vault_enabled")
        val KEY_PRIVATE_VAULT_LOCK_TYPE = stringPreferencesKey("private_vault_lock_type")
        val KEY_PRIVATE_VAULT_PIN_HASH = stringPreferencesKey("private_vault_pin_hash")
        val KEY_PRIVATE_VAULT_HIDE_FROM_RECENTS = booleanPreferencesKey("private_vault_hide_from_recents")
        val KEY_PRIVATE_VAULT_AUTO_LOCK_MINUTES = intPreferencesKey("private_vault_auto_lock_minutes")
        val KEY_PLAYBACK_HISTORY_ENABLED = booleanPreferencesKey("playback_history_enabled")
        val KEY_PLAYBACK_HISTORY_MAX_DAYS = intPreferencesKey("playback_history_max_days")
        val KEY_INCOGNITO_MODE = booleanPreferencesKey("incognito_mode")
        val KEY_APP_LOCK_ENABLED = booleanPreferencesKey("app_lock_enabled")
        val KEY_APP_LOCK_TYPE = stringPreferencesKey("app_lock_type")
        val KEY_SCREENSHOT_PREVENTION = booleanPreferencesKey("screenshot_prevention")
        val KEY_RECYCLE_BIN_ENABLED = booleanPreferencesKey("recycle_bin_enabled")
        val KEY_RECYCLE_BIN_RETENTION_DAYS = intPreferencesKey("recycle_bin_retention_days")

        // 3.7 Network & Cloud Preferences
        val KEY_STREAM_CACHE_ENABLED = booleanPreferencesKey("stream_cache_enabled")
        val KEY_STREAM_CACHE_SIZE_MB = intPreferencesKey("stream_cache_size_mb")
        val KEY_NETWORK_TIMEOUT_SECONDS = intPreferencesKey("network_timeout_seconds")
        val KEY_SMB_USERNAME = stringPreferencesKey("smb_username")
        val KEY_SMB_PASSWORD = stringPreferencesKey("smb_password")
        val KEY_SMB_DOMAIN = stringPreferencesKey("smb_domain")
        val KEY_SMB_AUTO_RECONNECT = booleanPreferencesKey("smb_auto_reconnect")
        val KEY_CAST_AUTO_RECONNECT = booleanPreferencesKey("cast_auto_reconnect")
        val KEY_CLOUD_BACKUP_ENABLED = booleanPreferencesKey("cloud_backup_enabled")
        val KEY_CLOUD_PROVIDER = stringPreferencesKey("cloud_provider")
        val KEY_CLOUD_BACKUP_PLAYLISTS = booleanPreferencesKey("cloud_backup_playlists")
        val KEY_CLOUD_BACKUP_HISTORY = booleanPreferencesKey("cloud_backup_history")
        val KEY_CLOUD_BACKUP_SUBTITLE_CACHE = booleanPreferencesKey("cloud_backup_subtitle_cache")
        val KEY_TORRENT_DOWNLOAD_DIR = stringPreferencesKey("torrent_download_dir")
        val KEY_TORRENT_MAX_CONNECTIONS = intPreferencesKey("torrent_max_connections")
        val KEY_TORRENT_SEQUENTIAL_DOWNLOAD = booleanPreferencesKey("torrent_sequential_download")
        val KEY_WIFI_ONLY_DOWNLOAD = booleanPreferencesKey("wifi_only_download")

        // 3.8 Advanced / Developer Preferences
        val KEY_LOG_LEVEL = stringPreferencesKey("log_level")
        val KEY_FFMPEG_LOG_LEVEL = stringPreferencesKey("ffmpeg_log_level")
        val KEY_PLAYER_DEBUG_OVERLAY = booleanPreferencesKey("player_debug_overlay")
        val KEY_SKIP_SILENCE_THRESHOLD_DB = floatPreferencesKey("skip_silence_threshold_db")
        val KEY_ANR_WATCHDOG_ENABLED = booleanPreferencesKey("anr_watchdog_enabled")
        val KEY_CRASH_REPORTING_ENABLED = booleanPreferencesKey("crash_reporting_enabled")
        val KEY_FORCE_SOFTWARE_DECODE_FORMATS = stringSetPreferencesKey("force_software_decode_formats")
        val KEY_DISABLE_FRAME_DROP = booleanPreferencesKey("disable_frame_drop")
        val KEY_EXPERIMENTAL_AV1_HW = booleanPreferencesKey("experimental_av1_hw")
    }

    // Default values defined in the spec
    val defaultVideoFileExtensions = setOf("mp4","mkv","avi","mov","wmv","flv","ts","m4v","3gp","rmvb","webm","ogv","divx")
    val defaultAudioFileExtensions = setOf("mp3","flac","ogg","wav","aac","m4a","opus","wma","ape","tta","mka","alac")

    val preferencesFlow: Flow<Preferences> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }

    // Example helper for reading a flow with default
    fun <T> getFlow(key: Preferences.Key<T>, defaultValue: T): Flow<T> = preferencesFlow.map { it[key] ?: defaultValue }

    // Read generic
    suspend fun <T> update(key: Preferences.Key<T>, value: T) {
        dataStore.edit { it[key] = value }
    }

    val resumePlayback: Flow<Boolean> = getFlow(KEY_RESUME_PLAYBACK, true)
    val defaultDecoder: Flow<String> = getFlow(KEY_DEFAULT_DECODER, "auto")
    val defaultPlaybackSpeed: Flow<Float> = getFlow(KEY_DEFAULT_SPEED, 1f)
    val skipSilence: Flow<Boolean> = getFlow(KEY_SKIP_SILENCE, false)
    val audioFocus: Flow<Boolean> = getFlow(KEY_AUDIO_FOCUS, true)
    val doubleTabSeekMs: Flow<Int> = getFlow(KEY_DOUBLE_TAP_SEEK_MS, 10_000)
    val longPressSpeed: Flow<Float> = getFlow(KEY_LONG_PRESS_SPEED, 2f)
    val volumeBoost: Flow<Boolean> = getFlow(KEY_VOLUME_BOOST_ENABLED, false)
    val pipAutoEnter: Flow<Boolean> = getFlow(KEY_PIP_AUTO_ENTER, true)
    val screenBrightness: Flow<Float> = getFlow(KEY_SCREEN_BRIGHTNESS, -1f)
    val keepScreenOn: Flow<Boolean> = getFlow(KEY_KEEP_SCREEN_ON, false)
    val screenOrientation: Flow<String> = getFlow(KEY_SCREEN_ORIENTATION, "auto")

    val subtitleAutoLoad: Flow<Boolean> = getFlow(KEY_SUBTITLE_AUTO_LOAD, true)
    val subtitleFontSize: Flow<Int> = getFlow(KEY_SUBTITLE_FONT_SIZE, 16)
    val subtitleFontColor: Flow<String> = getFlow(KEY_SUBTITLE_FONT_COLOR, "#FFFFFF")
    val subtitleBackgroundColor: Flow<String> = getFlow(KEY_SUBTITLE_BACKGROUND_COLOR, "#80000000")
    val subtitlePosition: Flow<Float> = getFlow(KEY_SUBTITLE_POSITION, 0.9f)
    val subtitleEncoding: Flow<String> = getFlow(KEY_SUBTITLE_ENCODING, "UTF-8")

    val eqEnabled: Flow<Boolean> = getFlow(KEY_EQ_ENABLED, false)
    val bassBoostEnabled: Flow<Boolean> = getFlow(KEY_BASS_BOOST_ENABLED, false)
    val virtualizerEnabled: Flow<Boolean> = getFlow(KEY_VIRTUALIZER_ENABLED, false)
    val audioTrackDefault: Flow<Int> = getFlow(KEY_AUDIO_TRACK_DEFAULT, 0)
    val preferAudioLanguage: Flow<String> = getFlow(KEY_PREFER_AUDIO_LANGUAGE, "")
    val sleepTimerMinutes: Flow<Int> = getFlow(KEY_SLEEP_TIMER_MINUTES, 0)

    val scanIntervalMinutes: Flow<Int> = getFlow(KEY_SCAN_INTERVAL_MINUTES, 60)
    val scanOnAppStart: Flow<Boolean> = getFlow(KEY_SCAN_ON_APP_START, true)
    val includeHiddenFolders: Flow<Boolean> = getFlow(KEY_INCLUDE_HIDDEN_FOLDERS, false)
    val minVideoDurationSecs: Flow<Int> = getFlow(KEY_MIN_VIDEO_DURATION_SECS, 0)
    val excludedFolders: Flow<Set<String>> = getFlow(KEY_EXCLUDED_FOLDERS, emptySet())
    val defaultVideoSort: Flow<String> = getFlow(KEY_DEFAULT_VIDEO_SORT, "date_desc")
    val defaultAudioSort: Flow<String> = getFlow(KEY_DEFAULT_AUDIO_SORT, "title_asc")
    val defaultVideoLayout: Flow<String> = getFlow(KEY_DEFAULT_VIDEO_LAYOUT, "grid")

    val themeMode: Flow<String> = getFlow(KEY_THEME_MODE, "system")
    val themeAccent: Flow<String> = getFlow(KEY_THEME_ACCENT, "blue")
    val dynamicColor: Flow<Boolean> = getFlow(KEY_DYNAMIC_COLOR, true)
    val fontScale: Flow<Float> = getFlow(KEY_FONT_SCALE, 1f)
    val compactMode: Flow<Boolean> = getFlow(KEY_COMPACT_MODE, false)
    val transitionAnimations: Flow<Boolean> = getFlow(KEY_TRANSITION_ANIMATIONS, true)
    val hapticFeedback: Flow<Boolean> = getFlow(KEY_HAPTIC_FEEDBACK, true)

    val privateVaultEnabled: Flow<Boolean> = getFlow(KEY_PRIVATE_VAULT_ENABLED, false)
    val privateVaultLockType: Flow<String> = getFlow(KEY_PRIVATE_VAULT_LOCK_TYPE, "biometric")
    val playbackHistoryEnabled: Flow<Boolean> = getFlow(KEY_PLAYBACK_HISTORY_ENABLED, true)
    val incognitoMode: Flow<Boolean> = getFlow(KEY_INCOGNITO_MODE, false)
    val recycleBinEnabled: Flow<Boolean> = getFlow(KEY_RECYCLE_BIN_ENABLED, true)
    val recycleBinRetentionDays: Flow<Int> = getFlow(KEY_RECYCLE_BIN_RETENTION_DAYS, 30)

    val streamCacheEnabled: Flow<Boolean> = getFlow(KEY_STREAM_CACHE_ENABLED, true)
    val networkTimeoutSeconds: Flow<Int> = getFlow(KEY_NETWORK_TIMEOUT_SECONDS, 30)
    val smbUsername: Flow<String> = getFlow(KEY_SMB_USERNAME, "")
    val smbPassword: Flow<String> = getFlow(KEY_SMB_PASSWORD, "")
    val wifiOnlyDownload: Flow<Boolean> = getFlow(KEY_WIFI_ONLY_DOWNLOAD, true)

    val logLevel: Flow<String> = getFlow(KEY_LOG_LEVEL, "info")
    val playerDebugOverlay: Flow<Boolean> = getFlow(KEY_PLAYER_DEBUG_OVERLAY, false)
    val crashReportingEnabled: Flow<Boolean> = getFlow(KEY_CRASH_REPORTING_ENABLED, true)

    suspend fun setThemeMode(mode: String) = update(KEY_THEME_MODE, mode)
    suspend fun setThemeAccent(accent: String) = update(KEY_THEME_ACCENT, accent)
    suspend fun setDynamicColor(enabled: Boolean) = update(KEY_DYNAMIC_COLOR, enabled)
    suspend fun setScreenOrientation(orientation: String) = update(KEY_SCREEN_ORIENTATION, orientation)
    suspend fun setDefaultDecoder(decoder: String) = update(KEY_DEFAULT_DECODER, decoder)
    suspend fun setResumePlayback(enabled: Boolean) = update(KEY_RESUME_PLAYBACK, enabled)
    suspend fun setSleepTimerMinutes(minutes: Int) = update(KEY_SLEEP_TIMER_MINUTES, minutes)
    suspend fun setPlaybackHistoryMaxDays(days: Int) = update(KEY_PLAYBACK_HISTORY_MAX_DAYS, days)
    suspend fun setExcludedFolders(folders: Set<String>) = update(KEY_EXCLUDED_FOLDERS, folders)

    suspend fun addExcludedFolder(folderPath: String) {
        dataStore.edit { prefs ->
            val current = prefs[KEY_EXCLUDED_FOLDERS] ?: emptySet()
            prefs[KEY_EXCLUDED_FOLDERS] = current + folderPath
        }
    }

    suspend fun removeExcludedFolder(folderPath: String) {
        dataStore.edit { prefs ->
            val current = prefs[KEY_EXCLUDED_FOLDERS] ?: emptySet()
            prefs[KEY_EXCLUDED_FOLDERS] = current - folderPath
        }
    }

    suspend fun resetAllSettings() {
        dataStore.edit { it.clear() }
    }

    suspend fun clearPlaybackHistory() {
        // Playback history is stored in Room; no-op until history repository is wired.
    }
}
