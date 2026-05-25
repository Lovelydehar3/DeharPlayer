package com.dehar.player.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material3.*
import androidx.compose.runtime.*
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.dehar.player.data.PreferencesManager
import com.dehar.player.data.SortOrder
import com.dehar.player.data.VideoData
import com.dehar.player.data.VideoRepository
import com.dehar.player.data.FolderLayoutSettings
import com.dehar.player.player.MusicPlaybackManager
import com.dehar.player.ui.components.VideoItemCard
import com.dehar.player.ui.components.VideoGridItemCard
import com.dehar.player.ui.components.LayoutAndSortDialog
import com.dehar.player.ui.components.MiniPlayer
import com.dehar.player.ui.navigation.Routes
import com.dehar.player.ui.theme.DeharUnplayedCyan
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FolderScreen(
    folderPath: String,
    navController: NavController,
    videoRepository: VideoRepository,
    preferencesManager: PreferencesManager,
    musicPlaybackManager: MusicPlaybackManager,
    modifier: Modifier = Modifier
) {
    var videos by remember { mutableStateOf<List<VideoData>>(emptyList()) }
    var lastPositions by remember { mutableStateOf<Map<String, Long>>(emptyMap()) }
    var layoutSettings by remember { mutableStateOf(FolderLayoutSettings()) }
    var layoutSortDialogVisible by remember { mutableStateOf(false) }
    var searchActive by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    
    val folderName = remember(folderPath) {
        File(folderPath).name.ifEmpty { "Videos" }
    }

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        layoutSettings = preferencesManager.getLayoutSettings()
    }

    LaunchedEffect(folderPath, layoutSettings.sortOrder) {
        val list = videoRepository.getVideosInFolder(folderPath, layoutSettings.sortOrder)
        videos = list
        lastPositions = preferencesManager.getLastPositions(list.map { it.path })
    }

    val filteredVideos = remember(videos, searchQuery) {
        if (searchQuery.isEmpty()) {
            videos
        } else {
            videos.filter {
                it.displayName.contains(searchQuery, ignoreCase = true) ||
                it.title.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (searchActive) {
                        TextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("Search videos in this folder...", color = Color.Gray) },
                            singleLine = true,
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        Text(
                            text = folderName,
                            fontWeight = FontWeight.Bold,
                            fontSize = 23.sp,
                            color = Color(0xFFE8EDF3)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFFE8EDF3)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        searchActive = !searchActive
                        if (!searchActive) searchQuery = ""
                    }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = Color(0xFFE8EDF3)
                        )
                    }
                    IconButton(onClick = { layoutSortDialogVisible = true }) {
                        Icon(
                            imageVector = if (layoutSettings.layoutType == "LIST") Icons.Default.GridView else Icons.AutoMirrored.Filled.List,
                            contentDescription = "Layout and Sort Options",
                            tint = Color(0xFFE8EDF3)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        floatingActionButton = {
            if (videos.isNotEmpty()) {
                FloatingActionButton(
                    onClick = {
                        navController.navigate(Routes.player(0, folderPath))
                    },
                    containerColor = DeharUnplayedCyan,
                    contentColor = Color.White,
                    shape = CircleShape
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = "Play Folder")
                }
            }
        },
        modifier = modifier
    ) { innerPadding ->
        if (layoutSortDialogVisible) {
            LayoutAndSortDialog(
                initialSettings = layoutSettings,
                onDismiss = { layoutSortDialogVisible = false },
                onConfirm = { newSettings ->
                    layoutSortDialogVisible = false
                    layoutSettings = newSettings
                    scope.launch {
                        preferencesManager.setLayoutSettings(newSettings)
                    }
                }
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
        ) {
            if (filteredVideos.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No videos found in this folder.",
                        color = Color.Gray,
                        fontSize = 16.sp
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 96.dp)
                ) {
                    if (layoutSettings.layoutType == "GRID") {
                        val chunkedVideos = filteredVideos.chunked(2)
                        itemsIndexed(chunkedVideos) { _, rowItems ->
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                rowItems.forEach { video ->
                                    val hasPlayed = (lastPositions[video.path] ?: 0L) > 0L
                                    VideoGridItemCard(
                                        video = video,
                                        isPlayed = hasPlayed,
                                        onClick = {
                                            val originalIndex = videos.indexOfFirst { it.id == video.id }.coerceAtLeast(0)
                                            navController.navigate(
                                                Routes.player(originalIndex, folderPath)
                                            )
                                        },
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                                if (rowItems.size < 2) {
                                    repeat(2 - rowItems.size) {
                                        Spacer(modifier = Modifier.weight(1f))
                                    }
                                }
                            }
                        }
                    } else {
                        itemsIndexed(filteredVideos) { _, video ->
                            val hasPlayed = (lastPositions[video.path] ?: 0L) > 0L
                            VideoItemCard(
                                video = video,
                                isPlayed = hasPlayed,
                                onClick = {
                                    val originalIndex = videos.indexOfFirst { it.id == video.id }.coerceAtLeast(0)
                                    navController.navigate(
                                        Routes.player(originalIndex, folderPath)
                                    )
                                }
                            )
                        }
                    }
                }
            }

            // Synced bottom MiniPlayer overlay
            if (musicPlaybackManager.currentSong != null) {
                MiniPlayer(
                    musicPlaybackManager = musicPlaybackManager,
                    onClick = { navController.navigate(Routes.NOW_PLAYING) },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 12.dp, start = 12.dp, end = 12.dp)
                )
            }
        }
    }
}
