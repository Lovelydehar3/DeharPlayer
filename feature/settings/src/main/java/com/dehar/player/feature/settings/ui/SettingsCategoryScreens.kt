package com.dehar.player.feature.settings.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dehar.player.feature.settings.SettingsViewModel
import kotlinx.coroutines.launch

/**
 * Playback Settings Screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaybackSettingsScreen(
    onBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Playback Settings") },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        )

        LazyColumn {
            item { PreferenceCategoryHeader("GENERAL") }
            item {
                TogglePreference(
                    title = "Resume Playback",
                    description = "Continue from last position",
                    isChecked = true,
                    onCheckedChange = { }
                )
            }
            item {
                TogglePreference(
                    title = "Autoplay Next Video",
                    description = "Play next video in folder automatically",
                    isChecked = viewModel.preferencesManager.autoplayNext,
                    onCheckedChange = { scope.launch { viewModel.preferencesManager.setAutoplayNext(it) } }
                )
            }
            item {
                NavigationPreference(
                    title = "Default Decoder",
                    description = "Current: ${viewModel.preferencesManager.decoderMode}",
                    onClick = { /* Show decoder dialog */ }
                )
            }

            item { PreferenceCategoryHeader("SPEED & TIMING") }
            item {
                SliderPreference(
                    title = "Default Playback Speed",
                    description = "Initial speed for new videos",
                    value = 1.0f,
                    onValueChange = { },
                    valueRange = 0.25f..2.0f,
                    steps = 6,
                    valueFormatter = { String.format("%.2f", it) + "x" }
                )
            }
            item {
                SliderPreference(
                    title = "Double-Tap Skip Duration",
                    description = "Seconds to skip on double tap",
                    value = 10f,
                    onValueChange = { },
                    valueRange = 5f..30f,
                    steps = 4,
                    valueFormatter = { "${it.toInt()}s" }
                )
            }

            item { PreferenceCategoryHeader("AUDIO FOCUS") }
            item {
                TogglePreference(
                    title = "Skip Silence",
                    description = "Automatically skip silent parts",
                    isChecked = false,
                    onCheckedChange = { }
                )
            }
            item {
                TogglePreference(
                    title = "Audio Focus",
                    description = "Pause on incoming calls",
                    isChecked = true,
                    onCheckedChange = { }
                )
            }

            item { PreferenceCategoryHeader("PIP & DISPLAY") }
            item {
                TogglePreference(
                    title = "Picture-in-Picture",
                    description = "Auto-enter PiP on app switch",
                    isChecked = viewModel.preferencesManager.pipAutoEnter,
                    onCheckedChange = { scope.launch { viewModel.preferencesManager.setPipAutoEnter(it) } }
                )
            }
            item {
                TogglePreference(
                    title = "Seekbar Frame Preview",
                    description = "Show frame thumbnail while seeking",
                    isChecked = viewModel.preferencesManager.seekPreviewEnabled,
                    onCheckedChange = { scope.launch { viewModel.preferencesManager.setSeekPreviewEnabled(it) } }
                )
            }
            item {
                TogglePreference(
                    title = "Keep Screen On",
                    description = "Prevent screen from sleeping",
                    isChecked = true,
                    onCheckedChange = { }
                )
            }
        }
    }
}

/**
 * Subtitle Settings Screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubtitleSettingsScreen(
    onBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Subtitle Settings") },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        )

        LazyColumn {
            item { PreferenceCategoryHeader("AUTO-LOADING") }
            item {
                TogglePreference(
                    title = "Auto-Load Subtitles",
                    description = "Automatically find matching subtitle files",
                    isChecked = true,
                    onCheckedChange = { }
                )
            }

            item { PreferenceCategoryHeader("APPEARANCE") }
            item {
                SliderPreference(
                    title = "Font Size",
                    description = "Subtitle text size",
                    value = 18f,
                    onValueChange = { },
                    valueRange = 10f..40f,
                    steps = 5,
                    valueFormatter = { "${it.toInt()}px" }
                )
            }
            item {
                NavigationPreference(
                    title = "Font Color",
                    description = "Change text color",
                    onClick = { }
                )
            }
            item {
                NavigationPreference(
                    title = "Background Color",
                    description = "Change background color",
                    onClick = { }
                )
            }

            item { PreferenceCategoryHeader("POSITION & ENCODING") }
            item {
                NavigationPreference(
                    title = "Position",
                    description = "Top, middle, or bottom",
                    onClick = { }
                )
            }
            item {
                NavigationPreference(
                    title = "Encoding",
                    description = "Subtitle text encoding",
                    onClick = { }
                )
            }
        }
    }
}

/**
 * Audio Settings Screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AudioSettingsScreen(
    onBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Audio Settings") },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        )

        LazyColumn {
            item { PreferenceCategoryHeader("EQUALIZER") }
            item {
                TogglePreference(
                    title = "Equalizer",
                    description = "Enable audio equalizer",
                    isChecked = false,
                    onCheckedChange = { }
                )
            }
            item {
                TogglePreference(
                    title = "Bass Boost",
                    description = "Enhance low frequencies",
                    isChecked = false,
                    onCheckedChange = { }
                )
            }
            item {
                TogglePreference(
                    title = "Virtualizer",
                    description = "Surround sound effect",
                    isChecked = false,
                    onCheckedChange = { }
                )
            }

            item { PreferenceCategoryHeader("LYRICS") }
            item {
                TogglePreference(
                    title = "Auto-Load Lyrics",
                    description = "Automatically find .lrc files",
                    isChecked = viewModel.preferencesManager.autoLoadLyrics,
                    onCheckedChange = { scope.launch { viewModel.preferencesManager.setAutoLoadLyrics(it) } }
                )
            }
            item {
                TogglePreference(
                    title = "Online Lyrics Search",
                    description = "Search lyrics from lrclib.net",
                    isChecked = viewModel.preferencesManager.lyricsApiEnabled,
                    onCheckedChange = { scope.launch { viewModel.preferencesManager.setLyricsApiEnabled(it) } }
                )
            }

            item { PreferenceCategoryHeader("AUDIO TRACKS") }
            item {
                NavigationPreference(
                    title = "Default Audio Track",
                    description = "Select preferred audio",
                    onClick = { }
                )
            }
            item {
                NavigationPreference(
                    title = "Preferred Language",
                    description = "Auto-select this language if available",
                    onClick = { }
                )
            }

            item { PreferenceCategoryHeader("VOLUME") }
            item {
                SliderPreference(
                    title = "Volume Boost",
                    description = "Increase maximum volume",
                    value = 1.0f,
                    onValueChange = { },
                    valueRange = 0.8f..1.5f,
                    steps = 6,
                    valueFormatter = { String.format("%.1f", it) + "x" }
                )
            }

            item { PreferenceCategoryHeader("SLEEP TIMER") }
            item {
                NavigationPreference(
                    title = "Sleep Timer",
                    description = "Set auto-stop duration",
                    onClick = { }
                )
            }
        }
    }
}

/**
 * Library Settings Screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibrarySettingsScreen(
    onBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Library Settings") },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        )

        LazyColumn {
            item { PreferenceCategoryHeader("SCANNING") }
            item {
                SliderPreference(
                    title = "Scan Interval",
                    description = "How often to scan media",
                    value = 24f,
                    onValueChange = { },
                    valueRange = 1f..168f,
                    steps = 10,
                    valueFormatter = { "${it.toInt()}h" }
                )
            }
            item {
                TogglePreference(
                    title = "Scan on App Start",
                    description = "Automatically scan when opening app",
                    isChecked = true,
                    onCheckedChange = { }
                )
            }

            item { PreferenceCategoryHeader("FILTERS") }
            item {
                TogglePreference(
                    title = "Include Hidden Folders",
                    description = "Show folders starting with .",
                    isChecked = false,
                    onCheckedChange = { }
                )
            }
            item {
                SliderPreference(
                    title = "Minimum Video Duration",
                    description = "Ignore videos shorter than this",
                    value = 30f,
                    onValueChange = { },
                    valueRange = 0f..300f,
                    steps = 9,
                    valueFormatter = { "${it.toInt()}s" }
                )
            }

            item { PreferenceCategoryHeader("DEFAULTS") }
            item {
                NavigationPreference(
                    title = "Default Video Sort",
                    description = "Name, date, size, duration",
                    onClick = { }
                )
            }
            item {
                NavigationPreference(
                    title = "Default Audio Sort",
                    description = "Name, artist, album",
                    onClick = { }
                )
            }
            item {
                NavigationPreference(
                    title = "Default Video Layout",
                    description = "List or grid view",
                    onClick = { }
                )
            }

            item { PreferenceCategoryHeader("MAINTENANCE") }
            item {
                NavigationPreference(
                    title = "Excluded Folders",
                    description = "Manage ignored folders",
                    onClick = { }
                )
            }
            item {
                NavigationPreference(
                    title = "Clear Cache",
                    description = "Remove temporary files",
                    onClick = { }
                )
            }
        }
    }
}

/**
 * UI Settings Screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UISettingsScreen(
    onBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("UI Settings") },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        )

        LazyColumn {
            item { PreferenceCategoryHeader("THEME") }
            item {
                NavigationPreference(
                    title = "Theme Mode",
                    description = "Light, dark, or system default",
                    onClick = { }
                )
            }
            item {
                TogglePreference(
                    title = "AMOLED Mode",
                    description = "Pure black background on OLED",
                    isChecked = false,
                    onCheckedChange = { }
                )
            }

            item { PreferenceCategoryHeader("DISPLAY") }
            item {
                NavigationPreference(
                    title = "Screen Brightness",
                    description = "Default brightness level",
                    onClick = { }
                )
            }
            item {
                NavigationPreference(
                    title = "Screen Orientation",
                    description = "Portrait or auto-rotate",
                    onClick = { }
                )
            }
            item {
                TogglePreference(
                    title = "Gesture Hints",
                    description = "Show gesture control hints",
                    isChecked = true,
                    onCheckedChange = { }
                )
            }

            item { PreferenceCategoryHeader("PLAYER CONTROLS") }
            item {
                TogglePreference(
                    title = "Show Debug Overlay",
                    description = "Display frame rate and stats",
                    isChecked = false,
                    onCheckedChange = { }
                )
            }
            item {
                SliderPreference(
                    title = "Controls Auto-Hide Delay",
                    description = "Hide after inactivity",
                    value = 3.5f,
                    onValueChange = { },
                    valueRange = 1f..10f,
                    steps = 8,
                    valueFormatter = { String.format("%.1f", it) + "s" }
                )
            }
        }
    }
}

/**
 * Privacy Settings Screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacySettingsScreen(
    onBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Privacy & Security") },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        )

        LazyColumn {
            item { PreferenceCategoryHeader("PRIVACY VAULT") }
            item {
                TogglePreference(
                    title = "Private Vault",
                    description = "Password-protect sensitive files",
                    isChecked = viewModel.preferencesManager.vaultEnabled,
                    onCheckedChange = { scope.launch { viewModel.preferencesManager.setVaultEnabled(it) } }
                )
            }
            item {
                NavigationPreference(
                    title = "Vault Password",
                    description = "Set or change password",
                    onClick = { }
                )
            }
            item {
                TogglePreference(
                    title = "Biometric Lock",
                    description = "Use fingerprint/face recognition",
                    isChecked = true,
                    onCheckedChange = { }
                )
            }

            item { PreferenceCategoryHeader("RECYCLE BIN") }
            item {
                TogglePreference(
                    title = "Recycle Bin",
                    description = "Move deleted files to bin instead of permanent delete",
                    isChecked = viewModel.preferencesManager.recycleBinEnabled,
                    onCheckedChange = { scope.launch { viewModel.preferencesManager.setRecycleBinEnabled(it) } }
                )
            }
            item {
                SliderPreference(
                    title = "Retention Period",
                    description = "Days before permanent deletion",
                    value = viewModel.preferencesManager.recycleBinRetentionDays.toFloat(),
                    onValueChange = { scope.launch { viewModel.preferencesManager.setRecycleBinRetentionDays(it.toInt()) } },
                    valueRange = 7f..90f,
                    steps = 3,
                    valueFormatter = { "${it.toInt()} days" }
                )
            }

            item { PreferenceCategoryHeader("TRACKING & ANALYTICS") }
            item {
                Text(
                    text = "✓ No analytics or tracking SDKs",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(16.dp)
                )
            }
            item {
                Text(
                    text = "✓ No ads or ad networks",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }
            item {
                Text(
                    text = "✓ No telemetry collection",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }
        }
    }
}

/**
 * Network Settings Screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NetworkSettingsScreen(
    onBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Network Settings") },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        )

        LazyColumn {
            item { PreferenceCategoryHeader("STREAMING") }
            item {
                SliderPreference(
                    title = "Network Timeout",
                    description = "Connection timeout duration",
                    value = 30f,
                    onValueChange = { },
                    valueRange = 10f..120f,
                    steps = 10,
                    valueFormatter = { "${it.toInt()}s" }
                )
            }
            item {
                SliderPreference(
                    title = "Buffer Size",
                    description = "Pre-buffer before playback",
                    value = 2.5f,
                    onValueChange = { },
                    valueRange = 1f..10f,
                    steps = 8,
                    valueFormatter = { String.format("%.1f", it) + "s" }
                )
            }

            item { PreferenceCategoryHeader("SMB / NAS") }
            item {
                TogglePreference(
                    title = "SMB Browsing",
                    description = "Enable network file access",
                    isChecked = true,
                    onCheckedChange = { }
                )
            }

            item { PreferenceCategoryHeader("CASTING") }
            item {
                TogglePreference(
                    title = "Chromecast Support",
                    description = "Cast to Chromecast devices",
                    isChecked = true,
                    onCheckedChange = { }
                )
            }

            item { PreferenceCategoryHeader("TORRENT") }
            item {
                SliderPreference(
                    title = "Max Connections",
                    description = "Torrent connection limit",
                    value = 200f,
                    onValueChange = { },
                    valueRange = 50f..500f,
                    steps = 8,
                    valueFormatter = { "${it.toInt()}" }
                )
            }
        }
    }
}

/**
 * Advanced Settings Screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdvancedSettingsScreen(
    onBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Advanced Settings") },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        )

        LazyColumn {
            item { PreferenceCategoryHeader("DEVELOPER") }
            item {
                TogglePreference(
                    title = "Debug Overlay",
                    description = "Show technical information",
                    isChecked = false,
                    onCheckedChange = { }
                )
            }
            item {
                TogglePreference(
                    title = "Hardware Acceleration",
                    description = "Use GPU for video decoding",
                    isChecked = true,
                    onCheckedChange = { }
                )
            }

            item { PreferenceCategoryHeader("PERFORMANCE") }
            item {
                NavigationPreference(
                    title = "Baseline Profile",
                    description = "Enable startup optimization",
                    onClick = { }
                )
            }
            item {
                TogglePreference(
                    title = "Aggressive Caching",
                    description = "Use more memory for performance",
                    isChecked = false,
                    onCheckedChange = { }
                )
            }

            item { PreferenceCategoryHeader("DATA & STORAGE") }
            item {
                NavigationPreference(
                    title = "Cache Size",
                    description = "Temporary file storage",
                    onClick = { }
                )
            }
            item {
                NavigationPreference(
                    title = "Export Settings",
                    description = "Backup all preferences",
                    onClick = { }
                )
            }
            item {
                NavigationPreference(
                    title = "Import Settings",
                    description = "Restore from backup",
                    onClick = { }
                )
            }
            item {
                NavigationPreference(
                    title = "Reset to Default",
                    description = "Restore original settings",
                    onClick = { }
                )
            }

            item { PreferenceCategoryHeader("ABOUT") }
            item {
                PreferenceItem(
                    title = "App Version",
                    description = "1.0.0 (Build 1)",
                    onClick = { }
                )
            }
            item {
                PreferenceItem(
                    title = "Build Info",
                    description = "May 26, 2026",
                    onClick = { }
                )
            }
        }
    }
}
