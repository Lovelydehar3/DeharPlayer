package com.dehar.player.feature.ringtoneEditor.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dehar.player.feature.ringtoneEditor.model.RingtoneType
import com.dehar.player.feature.ringtoneEditor.viewmodel.RingtoneEditorViewModel

/**
 * Main ringtone editor screen
 */
@Composable
fun RingtoneEditorScreen(
    audioPath: String? = null,
    displayName: String? = null,
    onNavigateBack: () -> Unit,
    viewModel: RingtoneEditorViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Load audio if provided
    LaunchedEffect(audioPath) {
        if (audioPath != null && displayName != null) {
            viewModel.loadAudioFile(audioPath, displayName)
        }
    }

    // Show error messages
    LaunchedEffect(uiState.errorMessage) {
        if (uiState.errorMessage != null) {
            snackbarHostState.showSnackbar(uiState.errorMessage!!)
            viewModel.clearErrorMessage()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ringtone Editor") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.Close, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        if (uiState.selectedAudio == null && !uiState.isLoading) {
            // No audio loaded state
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        "Select an audio file to edit",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Load a music file and trim it to create a custom ringtone",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else if (uiState.isLoading) {
            // Loading state
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            // Loaded state with editor
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    // Audio file info
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            uiState.selectedAudio?.name ?: "Unknown",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Total duration: ${formatTime(uiState.selectedAudio?.durationMs ?: 0)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                item {
                    // Waveform display
                    WaveformDisplay(
                        waveformData = uiState.waveformData,
                        trimState = uiState.trimState,
                        onTrimStartChanged = { viewModel.updateTrimStart(it) },
                        onTrimEndChanged = { viewModel.updateTrimEnd(it) },
                        onDraggingStartChanged = { viewModel.setDraggingStart(it) },
                        onDraggingEndChanged = { viewModel.setDraggingEnd(it) },
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }

                item {
                    // Trim time display
                    TrimTimeDisplay(
                        trimState = uiState.trimState,
                        totalDuration = uiState.selectedAudio?.durationMs ?: 0
                    )
                }

                item {
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                }

                item {
                    // Playback controls
                    PlaybackControls(
                        playbackState = uiState.playbackState,
                        totalDuration = uiState.selectedAudio?.durationMs ?: 0,
                        onPlayPauseClick = { viewModel.togglePlayback() },
                        onVolumeChanged = { viewModel.setVolume(it) },
                        onSpeedChanged = { viewModel.setPlaybackSpeed(it) }
                    )
                }

                item {
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                }

                item {
                    // Ringtone type selection
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            "Ringtone Type",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold
                        )

                        RingtoneType.values().forEach { type ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                RadioButton(
                                    selected = uiState.ringtoneType == type,
                                    onClick = { viewModel.setRingtoneType(type) }
                                )
                                Text(
                                    type.name.replace("_", " "),
                                    modifier = Modifier.padding(start = 8.dp),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }

                item {
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                }

                item {
                    // Save actions
                    SaveActionButtons(
                        onSaveCustom = {
                            viewModel.saveAsCustomRingtone(
                                uiState.selectedAudio?.name ?: "Ringtone"
                            )
                        },
                        onSetAsRingtone = {
                            viewModel.setAsSystemRingtone(
                                uiState.selectedAudio?.name ?: "Ringtone"
                            )
                        },
                        isSaving = uiState.isSaving
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

/**
 * Ringtone picker/selector screen
 */
@Composable
fun RingtonePickerScreen(
    onRingtoneSelected: (String) -> Unit,
    viewModel: RingtoneEditorViewModel = hiltViewModel()
) {
    var selectedType by remember { mutableStateOf(RingtoneType.CUSTOM) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBar(
            title = { Text("Select Ringtone") },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        )

        // Ringtone type filter
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            RingtoneType.values().forEach { type ->
                OutlinedButton(
                    onClick = { selectedType = type },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        type.name.replace("_", " "),
                        fontSize = 10.sp
                    )
                }
            }
        }

        Divider()

        // Ringtone list placeholder
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "No ringtones available",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
