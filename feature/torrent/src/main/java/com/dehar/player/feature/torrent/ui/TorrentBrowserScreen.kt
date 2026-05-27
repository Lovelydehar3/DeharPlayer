package com.dehar.player.feature.torrent.ui

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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.dehar.player.feature.torrent.model.DownloadState
import com.dehar.player.feature.torrent.viewmodel.TorrentBrowserViewModel

/**
 * Main torrent browser screen
 */
@Composable
fun TorrentBrowserScreen(
    onNavigateBack: () -> Unit,
    onFileSelected: (String) -> Unit,
    viewModel: TorrentBrowserViewModel = hiltViewModel()
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
                title = { Text("Torrent Streaming") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
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
        if (uiState.torrentMetadata == null) {
            MagnetInputView(
                onMagnetSubmit = { viewModel.loadTorrentMetadata(it) },
                isLoading = uiState.isLoading,
                modifier = Modifier.padding(paddingValues)
            )
        } else {
            TorrentContentView(
                uiState = uiState,
                onFilePlay = { file ->
                    val fileIndex = uiState.torrentMetadata!!.files.indexOf(file)
                    viewModel.startStreaming(fileIndex)
                },
                onPlayPause = {
                    if (uiState.downloadState?.state == DownloadState.PAUSED) {
                        viewModel.resumeStreaming()
                    } else if (uiState.isStreaming) {
                        viewModel.pauseStreaming()
                    }
                },
                onStop = { viewModel.stopStreaming() },
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}

/**
 * Magnet URI input view
 */
@Composable
fun MagnetInputView(
    onMagnetSubmit: (String) -> Unit,
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    var magnetUri by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Stream from Torrent",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Enter a magnet link to start streaming torrent content",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        OutlinedTextField(
            value = magnetUri,
            onValueChange = { magnetUri = it },
            label = { Text("Magnet Link") },
            placeholder = { Text("magnet:?xt=urn:btih:...") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 3,
            enabled = !isLoading
        )

        if (isLoading) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = { onMagnetSubmit(magnetUri) },
                enabled = magnetUri.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Load Torrent")
            }
        }

        Text(
            text = "Where to find magnet links:\n• Open torrent sites in browser\n• Click or copy magnet link\n• Paste in this field",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/**
 * Torrent content view
 */
@Composable
fun TorrentContentView(
    uiState: com.dehar.player.feature.torrent.model.TorrentBrowserUiState,
    onFilePlay: (com.dehar.player.feature.torrent.model.TorrentFile) -> Unit,
    onPlayPause: () -> Unit,
    onStop: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Torrent info
        if (uiState.torrentMetadata != null) {
            TorrentMetadataCard(uiState.torrentMetadata)
        }

        // Progress card if streaming
        if (uiState.downloadState != null) {
            TorrentProgressCard(uiState.downloadState)

            // Playback controls
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onPlayPause,
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp)
                ) {
                    Icon(
                        imageVector = if (uiState.downloadState.state == DownloadState.PAUSED) {
                            Icons.Default.PlayArrow
                        } else {
                            Icons.Default.Pause
                        },
                        contentDescription = "Play/Pause"
                    )
                    Text(if (uiState.downloadState.state == DownloadState.PAUSED) "Resume" else "Pause")
                }

                Button(
                    onClick = onStop,
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp)
                ) {
                    Icon(Icons.Default.Stop, contentDescription = "Stop")
                    Text("Stop")
                }
            }
        }

        // Error display
        if (uiState.errorMessage != null) {
            TorrentErrorCard(uiState.errorMessage)
        }

        // File list
        if (uiState.torrentMetadata != null && uiState.torrentMetadata.files.isNotEmpty()) {
            Text(
                text = "Files (${uiState.torrentMetadata.files.size})",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            TorrentFileList(
                files = uiState.torrentMetadata.files,
                onPlayClick = onFilePlay
            )
        }

        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}
