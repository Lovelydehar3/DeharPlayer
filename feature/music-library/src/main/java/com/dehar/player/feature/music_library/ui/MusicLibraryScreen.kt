package com.dehar.player.feature.music_library.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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
import com.dehar.player.core.domain.model.SongItem
import com.dehar.player.core.domain.viewmodel.MusicLibraryUiState
import com.dehar.player.core.domain.viewmodel.MusicLibraryViewModel
import com.dehar.player.core.domain.viewmodel.MusicTab

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicLibraryScreen(
    onNavigateBack: () -> Unit,
    onSelectSong: (String) -> Unit,
    viewModel: MusicLibraryViewModel = hiltViewModel()
) {
    val currentTab by viewModel.currentTab.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    var localSearchQuery by remember(searchQuery) { mutableStateOf(searchQuery) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Music Library") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
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
                value = localSearchQuery,
                onValueChange = {
                    localSearchQuery = it
                    viewModel.search(it)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                placeholder = { Text("Search songs, albums, artists...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    if (localSearchQuery.isNotEmpty()) {
                        IconButton(onClick = {
                            localSearchQuery = ""
                            viewModel.search("")
                        }) {
                            Icon(Icons.Default.Close, contentDescription = "Clear")
                        }
                    }
                },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(4.dp))

            TabRow(selectedTabIndex = currentTab.ordinal) {
                Tab(
                    selected = currentTab == MusicTab.SONGS,
                    onClick = { viewModel.setTab(MusicTab.SONGS) },
                    text = { Text("Songs") }
                )
                Tab(
                    selected = currentTab == MusicTab.ALBUMS,
                    onClick = { viewModel.setTab(MusicTab.ALBUMS) },
                    text = { Text("Albums") }
                )
                Tab(
                    selected = currentTab == MusicTab.ARTISTS,
                    onClick = { viewModel.setTab(MusicTab.ARTISTS) },
                    text = { Text("Artists") }
                )
                Tab(
                    selected = currentTab == MusicTab.FAVORITES,
                    onClick = { viewModel.setTab(MusicTab.FAVORITES) },
                    text = { Text("Favorites") }
                )
            }

            when (val state = uiState) {
                is MusicLibraryUiState.Loading -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is MusicLibraryUiState.Error -> {
                    Text(
                        text = state.message,
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.error
                    )
                }
                is MusicLibraryUiState.Success -> {
                    when (currentTab) {
                        MusicTab.SONGS, MusicTab.FAVORITES -> SongsTab(state.songs, onSelectSong)
                        MusicTab.ALBUMS -> PlaceholderTab("Albums coming soon")
                        MusicTab.ARTISTS -> PlaceholderTab("Artists coming soon")
                    }
                }
            }
        }
    }
}

@Composable
private fun PlaceholderTab(message: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(message)
    }
}

@Composable
fun SongsTab(
    songs: List<SongItem>,
    onSelectSong: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (songs.isEmpty()) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("No songs found")
        }
        return
    }

    LazyColumn(modifier = modifier.fillMaxWidth()) {
        items(songs, key = { it.id }) { song ->
            ListItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelectSong(song.path) },
                headlineContent = {
                    Text(
                        text = song.title,
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                supportingContent = {
                    Text(song.artist, style = MaterialTheme.typography.labelSmall)
                },
                trailingContent = {
                    Text(
                        text = formatDuration(song.duration),
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            )
        }
    }
}

fun formatDuration(ms: Long): String {
    val totalSeconds = ms / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%d:%02d".format(minutes, seconds)
}
