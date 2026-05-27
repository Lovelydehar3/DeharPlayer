package com.dehar.player.feature.home.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dehar.player.core.domain.viewmodel.HomeViewModel
import com.dehar.player.core.domain.viewmodel.HomeUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToSettings: () -> Unit,
    onNavigateToPlayer: () -> Unit,
    onNavigateToMusicLibrary: () -> Unit,
    onNavigateToVideoPlayer: (String) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dehar Player") },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text("Search videos, music...") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "Search")
                },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            when (uiState) {
                is HomeUiState.Loading -> {
                    Text("Loading...", modifier = Modifier.padding(16.dp))
                }
                is HomeUiState.Success -> {
                    val state = (uiState as HomeUiState.Success)
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp)
                    ) {
                        item {
                            Text(
                                "Recently Played",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(16.dp, 8.dp)
                            )
                        }

                        items(state.recentlyPlayed.take(5)) { video ->
                            VideoItemCard(
                                title = video.title,
                                thumbnail = null,
                                duration = video.duration,
                                onClick = { onNavigateToVideoPlayer(video.path) }
                            )
                        }

                        item {
                            Text(
                                "Favorite Videos",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(16.dp, 24.dp, 16.dp, 8.dp)
                            )
                        }

                        items(state.favorites.take(5)) { video ->
                            VideoItemCard(
                                title = video.title,
                                thumbnail = null,
                                duration = video.duration,
                                onClick = { onNavigateToVideoPlayer(video.path) }
                            )
                        }

                        item {
                            Text(
                                "Continue Watching",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(16.dp, 24.dp, 16.dp, 8.dp)
                            )
                        }

                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
                is HomeUiState.Error -> {
                    Text(
                        "Error: ${(uiState as HomeUiState.Error).message}",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun VideoItemCard(
    title: String,
    thumbnail: String?,
    duration: Long,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    androidx.compose.material3.Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        onClick = onClick
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Thumbnail placeholder
            androidx.compose.material3.Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                // TODO: Load thumbnail with Coil
            }

            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2
                )
                Text(
                    text = formatTime(duration),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

fun formatTime(ms: Long): String {
    val totalSeconds = ms / 1000
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60

    return if (hours > 0) {
        "%d:%02d:%02d".format(hours, minutes, seconds)
    } else {
        "%d:%02d".format(minutes, seconds)
    }
}
