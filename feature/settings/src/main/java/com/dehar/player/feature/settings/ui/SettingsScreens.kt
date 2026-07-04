package com.dehar.player.feature.settings.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dehar.player.feature.settings.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsRootScreen(
    onNavigateBack: () -> Unit,
    onNavigateTo: (String) -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
        ) {
            item {
                SettingsCategory(
                    title = "🎬 Video Playback",
                    onClick = { onNavigateTo("settings_playback") }
                )
            }
            item {
                SettingsCategory(
                    title = "📝 Subtitles",
                    onClick = { onNavigateTo("settings_subtitle") }
                )
            }
            item {
                SettingsCategory(
                    title = "🎵 Audio",
                    onClick = { onNavigateTo("settings_audio") }
                )
            }
            item {
                SettingsCategory(
                    title = "📚 Library & Scanning",
                    onClick = { onNavigateTo("settings_library") }
                )
            }
            item {
                SettingsCategory(
                    title = "🎨 Appearance",
                    onClick = { onNavigateTo("settings_ui") }
                )
            }
            item {
                SettingsCategory(
                    title = "🔒 Privacy & Security",
                    onClick = { onNavigateTo("settings_privacy") }
                )
            }
            item {
                SettingsCategory(
                    title = "🌐 Network & Cloud",
                    onClick = { onNavigateTo("settings_network") }
                )
            }
            item {
                SettingsCategory(
                    title = "🔬 Advanced",
                    onClick = { onNavigateTo("settings_advanced") }
                )
            }
            item {
                SettingsCategory(
                    title = "ℹ️ About",
                    onClick = { onNavigateTo("settings_about") }
                )
            }
        }
    }
}

@Composable
private fun SettingsCategory(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f)
            )
            Icon(
                Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LegacyPlaybackSettingsScreen(
    onNavigateBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Video Playback") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
        ) {
            item {
                SettingsPref(
                    title = "Default Decoder",
                    summary = "Choose decoder: Auto, Hardware, Software, FFmpeg",
                    onClick = { /* Open decoder picker */ }
                )
            }
            item {
                SettingsPref(
                    title = "Resume Playback",
                    summary = "Auto-resume last position",
                    onClick = { /* Toggle */ }
                )
            }
            item {
                SettingsPref(
                    title = "Autoplay Next",
                    summary = "Automatically play next file in folder",
                    onClick = { /* Toggle */ }
                )
            }
            item {
                SettingsPref(
                    title = "Double-Tap Seek",
                    summary = "Seek duration: 10 seconds",
                    onClick = { /* Open picker */ }
                )
            }
            item {
                SettingsPref(
                    title = "Screen Orientation",
                    summary = "Sensor (auto-rotate)",
                    onClick = { /* Open picker */ }
                )
            }
            item {
                SettingsPref(
                    title = "Keep Screen On",
                    summary = "Prevent screen from turning off during playback",
                    onClick = { /* Toggle */ }
                )
            }
            item {
                SettingsPref(
                    title = "PiP Auto-Enter",
                    summary = "Auto-enter Picture-in-Picture on Home press",
                    onClick = { /* Toggle */ }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LegacyAudioSettingsScreen(
    onNavigateBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Audio Settings") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
        ) {
            item {
                SettingsPref(
                    title = "Equalizer",
                    summary = "Open 5-band EQ and effects",
                    onClick = { /* Navigate to EQ */ }
                )
            }
            item {
                SettingsPref(
                    title = "Bass Boost",
                    summary = "Enable low-frequency boost",
                    onClick = { /* Toggle */ }
                )
            }
            item {
                SettingsPref(
                    title = "Virtualizer / 3D Audio",
                    summary = "Surround sound enhancement",
                    onClick = { /* Toggle */ }
                )
            }
            item {
                SettingsPref(
                    title = "Volume Normalization",
                    summary = "Normalize volume across tracks",
                    onClick = { /* Toggle */ }
                )
            }
            item {
                SettingsPref(
                    title = "Preferred Audio Language",
                    summary = "e.g., en, hi, es",
                    onClick = { /* Open text input */ }
                )
            }
            item {
                SettingsPref(
                    title = "Skip Silence",
                    summary = "Skip silent sections in audio",
                    onClick = { /* Toggle */ }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LegacyUISettingsScreen(
    onNavigateBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Appearance") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
        ) {
            item {
                SettingsPref(
                    title = "Theme",
                    summary = "System (Light/Dark/AMOLED)",
                    onClick = { /* Open picker */ }
                )
            }
            item {
                SettingsPref(
                    title = "Accent Color",
                    summary = "Blue (Material You Dynamic)",
                    onClick = { /* Open color picker */ }
                )
            }
            item {
                SettingsPref(
                    title = "Dynamic Color",
                    summary = "Use Material You colors (API 31+)",
                    onClick = { /* Toggle */ }
                )
            }
            item {
                SettingsPref(
                    title = "Font Size",
                    summary = "Normal",
                    onClick = { /* Open slider */ }
                )
            }
            item {
                SettingsPref(
                    title = "Compact Mode",
                    summary = "Denser list layout",
                    onClick = { /* Toggle */ }
                )
            }
            item {
                SettingsPref(
                    title = "Transition Animations",
                    summary = "Enable smooth transitions",
                    onClick = { /* Toggle */ }
                )
            }
            item {
                SettingsPref(
                    title = "Haptic Feedback",
                    summary = "Vibration on interactions",
                    onClick = { /* Toggle */ }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LegacyPrivacySettingsScreen(
    onNavigateBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Privacy & Security") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
        ) {
            item {
                SettingsPref(
                    title = "Private Vault",
                    summary = "Enable encrypted file vault",
                    onClick = { /* Toggle and setup */ }
                )
            }
            item {
                SettingsPref(
                    title = "App Lock",
                    summary = "Lock app with biometric/PIN",
                    onClick = { /* Setup */ }
                )
            }
            item {
                SettingsPref(
                    title = "Playback History",
                    summary = "Save and sync playback history",
                    onClick = { /* Toggle */ }
                )
            }
            item {
                SettingsPref(
                    title = "Incognito Mode",
                    summary = "No history, no position save",
                    onClick = { /* Toggle */ }
                )
            }
            item {
                SettingsPref(
                    title = "Recycle Bin",
                    summary = "Keep deleted files 30 days",
                    onClick = { /* Toggle */ }
                )
            }
            item {
                SettingsPref(
                    title = "Clear History",
                    summary = "Remove all playback history",
                    onClick = { /* Confirm and clear */ },
                    isDestructive = true
                )
            }
        }
    }
}

@Composable
private fun SettingsPref(
    title: String,
    summary: String,
    onClick: () -> Unit,
    isDestructive: Boolean = false,
    modifier: Modifier = Modifier
) {
    ListItem(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        headlineContent = { Text(title) },
        supportingContent = { Text(summary) }
    )
}
