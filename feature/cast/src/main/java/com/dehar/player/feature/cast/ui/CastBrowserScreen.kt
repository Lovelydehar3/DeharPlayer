package com.dehar.player.feature.cast.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cast
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.dehar.player.feature.cast.viewmodel.CastViewModel

/**
 * Cast device selector dialog
 */
@Composable
fun CastDeviceSelectorDialog(
    uiState: com.dehar.player.feature.cast.model.CastBrowserUiState,
    onDeviceSelected: (com.dehar.player.feature.cast.model.CastDevice) -> Unit,
    onDismiss: () -> Unit,
    onRefresh: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = true
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Select Cast Device",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )

                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                // Device list or loading
                if (uiState.scanningDevices) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else if (uiState.availableDevices.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "No Cast devices found",
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Button(onClick = onRefresh) {
                            Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                            Text("Scan Again")
                        }
                    }
                } else {
                    CastDeviceList(
                        devices = uiState.availableDevices,
                        selectedDeviceId = uiState.selectedDevice?.id,
                        onDeviceClick = onDeviceSelected,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 400.dp)
                            .verticalScroll(rememberScrollState())
                    )
                }
            }
        }
    }
}

/**
 * Main Cast browser screen
 */
@Composable
fun CastBrowserScreen(
    onNavigateBack: () -> Unit,
    onFileSelected: (String) -> Unit,
    viewModel: CastViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Show error snackbar
    LaunchedEffect(uiState.errorMessage) {
        if (uiState.errorMessage != null) {
            snackbarHostState.showSnackbar(uiState.errorMessage!!)
            viewModel.clearErrorMessage()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cast to Device") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.Cast, contentDescription = "Back")
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
        if (uiState.castSession != null) {
            // Casting view
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CastSessionCard(uiState.castSession)

                CastControlButtons(
                    state = uiState.castSession.state,
                    onPlay = { viewModel.play() },
                    onPause = { viewModel.pause() },
                    onStop = { viewModel.stop() }
                )

                CastVolumeControl(
                    volume = uiState.castSession.volume,
                    isMuted = uiState.castSession.isMuted,
                    onVolumeChanged = { viewModel.setVolume(it) },
                    onMuteToggle = { viewModel.toggleMute() }
                )

                Button(
                    onClick = { viewModel.disconnectFromDevice() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Disconnect")
                }
            }
        } else {
            // Device selection view
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Available Cast Devices",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                if (uiState.isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else if (uiState.availableDevices.isEmpty()) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        content = {
                            Text(
                                text = "No Cast devices found",
                                style = MaterialTheme.typography.bodyMedium
                            )

                            Button(onClick = { viewModel.scanForDevices() }) {
                                Icon(Icons.Default.Refresh, contentDescription = "Scan")
                                Text("Scan for Devices")
                            }
                        }
                    )
                } else {
                    CastDeviceList(
                        devices = uiState.availableDevices,
                        onDeviceClick = { viewModel.connectToDevice(it) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    )
                }
            }
        }
    }

    // Device selector dialog
    if (uiState.showDeviceSelector) {
        CastDeviceSelectorDialog(
            uiState = uiState,
            onDeviceSelected = { viewModel.connectToDevice(it) },
            onDismiss = { viewModel.hideDeviceSelector() },
            onRefresh = { viewModel.scanForDevices() }
        )
    }
}

// Helper function for heightIn
@Composable
fun Modifier.heightIn(min: androidx.compose.ui.unit.Dp = 0.dp, max: androidx.compose.ui.unit.Dp = androidx.compose.ui.unit.Dp.Unspecified): Modifier =
    this.then(
        androidx.compose.foundation.layout.heightIn(
            min = min,
            max = max
        )
    )
