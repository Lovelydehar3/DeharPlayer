package com.dehar.player.feature.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.dehar.player.feature.settings.ui.AdvancedSettingsScreen
import com.dehar.player.feature.settings.ui.AudioSettingsScreen
import com.dehar.player.feature.settings.ui.LibrarySettingsScreen
import com.dehar.player.feature.settings.ui.NavigationPreference
import com.dehar.player.feature.settings.ui.NetworkSettingsScreen
import com.dehar.player.feature.settings.ui.PlaybackSettingsScreen
import com.dehar.player.feature.settings.ui.PrivacySettingsScreen
import com.dehar.player.feature.settings.ui.SubtitleSettingsScreen
import com.dehar.player.feature.settings.ui.UISettingsScreen

data class SettingCategory(
    val id: String,
    val title: String,
    val description: String,
    val icon: String
)

enum class SettingsScreenState {
    MAIN, PLAYBACK, SUBTITLE, AUDIO, LIBRARY, UI, PRIVACY, NETWORK, ADVANCED
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    var currentScreen by remember { mutableStateOf(SettingsScreenState.MAIN) }

    when (currentScreen) {
        SettingsScreenState.MAIN -> MainSettingsScreen(
            onCategoryClick = { currentScreen = it }
        )
        SettingsScreenState.PLAYBACK -> PlaybackSettingsScreen(
            onBack = { currentScreen = SettingsScreenState.MAIN }
        )
        SettingsScreenState.SUBTITLE -> SubtitleSettingsScreen(
            onBack = { currentScreen = SettingsScreenState.MAIN }
        )
        SettingsScreenState.AUDIO -> AudioSettingsScreen(
            onBack = { currentScreen = SettingsScreenState.MAIN }
        )
        SettingsScreenState.LIBRARY -> LibrarySettingsScreen(
            onBack = { currentScreen = SettingsScreenState.MAIN }
        )
        SettingsScreenState.UI -> UISettingsScreen(
            onBack = { currentScreen = SettingsScreenState.MAIN }
        )
        SettingsScreenState.PRIVACY -> PrivacySettingsScreen(
            onBack = { currentScreen = SettingsScreenState.MAIN }
        )
        SettingsScreenState.NETWORK -> NetworkSettingsScreen(
            onBack = { currentScreen = SettingsScreenState.MAIN }
        )
        SettingsScreenState.ADVANCED -> AdvancedSettingsScreen(
            onBack = { currentScreen = SettingsScreenState.MAIN }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainSettingsScreen(
    onCategoryClick: (SettingsScreenState) -> Unit
) {
    val categories = listOf(
        SettingCategory("playback", "Playback", "Video and audio playback options", "▶️"),
        SettingCategory("subtitle", "Subtitles", "Subtitle display and formatting", "📝"),
        SettingCategory("audio", "Audio", "Audio equalizer and effects", "🔊"),
        SettingCategory("library", "Library", "Media scanning and organization", "📚"),
        SettingCategory("ui", "User Interface", "Theme and display settings", "🎨"),
        SettingCategory("privacy", "Privacy & Security", "Vault and privacy controls", "🔒"),
        SettingCategory("network", "Network", "Streaming and connectivity options", "🌐"),
        SettingCategory("advanced", "Advanced", "Performance and debug options", "⚙️")
    )

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Settings") }
        )

        LazyColumn {
            items(
                items = categories,
                key = { it.id }
            ) { category ->
                NavigationPreference(
                    title = "${category.icon} ${category.title}",
                    description = category.description,
                    onClick = {
                        when (category.id) {
                            "playback" -> onCategoryClick(SettingsScreenState.PLAYBACK)
                            "subtitle" -> onCategoryClick(SettingsScreenState.SUBTITLE)
                            "audio" -> onCategoryClick(SettingsScreenState.AUDIO)
                            "library" -> onCategoryClick(SettingsScreenState.LIBRARY)
                            "ui" -> onCategoryClick(SettingsScreenState.UI)
                            "privacy" -> onCategoryClick(SettingsScreenState.PRIVACY)
                            "network" -> onCategoryClick(SettingsScreenState.NETWORK)
                            "advanced" -> onCategoryClick(SettingsScreenState.ADVANCED)
                        }
                    }
                )
            }
        }
    }
}
