package com.dehar.player.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dehar.player.core.data.preferences.DeharPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferences: DeharPreferences
) : ViewModel() {

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
