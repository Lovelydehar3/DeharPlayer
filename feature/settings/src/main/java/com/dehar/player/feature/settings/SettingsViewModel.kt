package com.dehar.player.feature.settings

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dehar.player.core.data.preferences.DeharPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferences: DeharPreferences
) : ViewModel() {

    val preferencesManager = SettingsPreferencesManager(preferences)

    val resumePlayback = preferences.resumePlayback
    val defaultDecoder = preferences.defaultDecoder
    val defaultPlaybackSpeed = preferences.defaultPlaybackSpeed
    val skipSilence = preferences.skipSilence
    val audioFocus = preferences.audioFocus
    val doubletapSeekMs = preferences.doubleTabSeekMs
    val longPressSpeed = preferences.longPressSpeed
    val volumeBoost = preferences.volumeBoost
    val pipAutoEnter = preferences.pipAutoEnter
    val screenBrightness = preferences.screenBrightness
    val keepScreenOn = preferences.keepScreenOn
    val screenOrientation = preferences.screenOrientation

    val subtitleAutoLoad = preferences.subtitleAutoLoad
    val subtitleFontSize = preferences.subtitleFontSize
    val subtitleFontColor = preferences.subtitleFontColor
    val subtitleBackgroundColor = preferences.subtitleBackgroundColor
    val subtitlePosition = preferences.subtitlePosition
    val subtitleEncoding = preferences.subtitleEncoding

    val eqEnabled = preferences.eqEnabled
    val bassBoostEnabled = preferences.bassBoostEnabled
    val virtualizerEnabled = preferences.virtualizerEnabled
    val audioTrackDefault = preferences.audioTrackDefault
    val preferAudioLanguage = preferences.preferAudioLanguage
    val sleepTimerMinutes = preferences.sleepTimerMinutes

    val scanIntervalMinutes = preferences.scanIntervalMinutes
    val scanOnAppStart = preferences.scanOnAppStart
    val includeHiddenFolders = preferences.includeHiddenFolders
    val minVideoDurationSecs = preferences.minVideoDurationSecs
    val excludedFolders = preferences.excludedFolders
    val defaultVideoSort = preferences.defaultVideoSort
    val defaultAudioSort = preferences.defaultAudioSort
    val defaultVideoLayout = preferences.defaultVideoLayout

    val themeMode = preferences.themeMode
    val themeAccent = preferences.themeAccent
    val dynamicColor = preferences.dynamicColor
    val fontScale = preferences.fontScale
    val compactMode = preferences.compactMode
    val transitionAnimations = preferences.transitionAnimations
    val hapticFeedback = preferences.hapticFeedback

    val privateVaultEnabled = preferences.privateVaultEnabled
    val privateVaultLockType = preferences.privateVaultLockType
    val playbackHistoryEnabled = preferences.playbackHistoryEnabled
    val incognitoMode = preferences.incognitoMode
    val recycleBinEnabled = preferences.recycleBinEnabled
    val recycleBinRetentionDays = preferences.recycleBinRetentionDays

    val streamCacheEnabled = preferences.streamCacheEnabled
    val networkTimeoutSeconds = preferences.networkTimeoutSeconds
    val smbUsername = preferences.smbUsername
    val smbPassword = preferences.smbPassword
    val wifiOnlyDownload = preferences.wifiOnlyDownload

    val logLevel = preferences.logLevel
    val playerDebugOverlay = preferences.playerDebugOverlay
    val crashReportingEnabled = preferences.crashReportingEnabled

    fun updateThemeMode(mode: String) = viewModelScope.launch {
        preferences.setThemeMode(mode)
    }

    fun updateThemeAccent(accent: String) = viewModelScope.launch {
        preferences.setThemeAccent(accent)
    }

    fun updateDynamicColor(enabled: Boolean) = viewModelScope.launch {
        preferences.setDynamicColor(enabled)
    }

    fun updateScreenOrientation(orientation: String) = viewModelScope.launch {
        preferences.setScreenOrientation(orientation)
    }

    fun updateDefaultDecoder(decoder: String) = viewModelScope.launch {
        preferences.setDefaultDecoder(decoder)
    }

    fun updateResumePlayback(enabled: Boolean) = viewModelScope.launch {
        preferences.setResumePlayback(enabled)
    }

    fun updateSleepTimer(minutes: Int) = viewModelScope.launch {
        preferences.setSleepTimerMinutes(minutes)
    }

    fun updatePlaybackHistoryRetention(days: Int) = viewModelScope.launch {
        preferences.setPlaybackHistoryMaxDays(days)
    }

    fun resetAllSettings() = viewModelScope.launch {
        preferences.resetAllSettings()
    }

    fun clearPlaybackHistory() = viewModelScope.launch {
        preferences.clearPlaybackHistory()
    }

    fun addExcludedFolder(folderPath: String) = viewModelScope.launch {
        preferences.addExcludedFolder(folderPath)
    }

    fun removeExcludedFolder(folderPath: String) = viewModelScope.launch {
        preferences.removeExcludedFolder(folderPath)
    }
}

class SettingsPreferencesManager(
    private val preferences: DeharPreferences
) {
    private companion object {
        val KEY_DECODER_MODE = stringPreferencesKey("decoder_mode")
        val KEY_AUTOPLAY_NEXT = booleanPreferencesKey("autoplay_next")
        val KEY_PIP_AUTO_ENTER = booleanPreferencesKey("pip_auto_enter")
        val KEY_SEEK_PREVIEW_ENABLED = booleanPreferencesKey("seek_preview_enabled")
        val KEY_AUTOLOAD_LYRICS = booleanPreferencesKey("autoload_lyrics")
        val KEY_LYRICS_API_ENABLED = booleanPreferencesKey("lyrics_api_enabled")
        val KEY_VAULT_ENABLED = booleanPreferencesKey("vault_enabled")
        val KEY_RECYCLE_BIN_ENABLED = booleanPreferencesKey("recycle_bin_enabled")
        val KEY_RECYCLE_BIN_RETENTION_DAYS = intPreferencesKey("recycle_bin_retention_days")
    }

    private fun <T> read(key: Preferences.Key<T>, default: T): T = runBlocking {
        preferences.preferencesFlow.first()[key] ?: default
    }

    suspend fun <T> write(key: Preferences.Key<T>, value: T) {
        preferences.update(key, value)
    }

    val decoderMode: String
        get() = read(KEY_DECODER_MODE, "AUTO")

    suspend fun setDecoderMode(mode: String) {
        write(KEY_DECODER_MODE, mode)
    }

    val autoplayNext: Boolean
        get() = read(KEY_AUTOPLAY_NEXT, true)

    suspend fun setAutoplayNext(enabled: Boolean) {
        write(KEY_AUTOPLAY_NEXT, enabled)
    }

    val pipAutoEnter: Boolean
        get() = read(KEY_PIP_AUTO_ENTER, true)

    suspend fun setPipAutoEnter(enabled: Boolean) {
        write(KEY_PIP_AUTO_ENTER, enabled)
    }

    val seekPreviewEnabled: Boolean
        get() = read(KEY_SEEK_PREVIEW_ENABLED, true)

    suspend fun setSeekPreviewEnabled(enabled: Boolean) {
        write(KEY_SEEK_PREVIEW_ENABLED, enabled)
    }

    val autoLoadLyrics: Boolean
        get() = read(KEY_AUTOLOAD_LYRICS, true)

    suspend fun setAutoLoadLyrics(enabled: Boolean) {
        write(KEY_AUTOLOAD_LYRICS, enabled)
    }

    val lyricsApiEnabled: Boolean
        get() = read(KEY_LYRICS_API_ENABLED, true)

    suspend fun setLyricsApiEnabled(enabled: Boolean) {
        write(KEY_LYRICS_API_ENABLED, enabled)
    }

    val vaultEnabled: Boolean
        get() = read(KEY_VAULT_ENABLED, false)

    suspend fun setVaultEnabled(enabled: Boolean) {
        write(KEY_VAULT_ENABLED, enabled)
    }

    val recycleBinEnabled: Boolean
        get() = read(KEY_RECYCLE_BIN_ENABLED, true)

    suspend fun setRecycleBinEnabled(enabled: Boolean) {
        write(KEY_RECYCLE_BIN_ENABLED, enabled)
    }

    val recycleBinRetentionDays: Int
        get() = read(KEY_RECYCLE_BIN_RETENTION_DAYS, 30)

    suspend fun setRecycleBinRetentionDays(days: Int) {
        write(KEY_RECYCLE_BIN_RETENTION_DAYS, days)
    }
}
