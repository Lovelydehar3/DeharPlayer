package com.dehar.player.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.dehar.player.ui.components.VideoItemCard
import com.dehar.player.ui.navigation.Routes
import com.dehar.player.ui.theme.DeharBackground
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FolderScreen(
    folderPath: String,
    navController: NavController,
    videoRepository: VideoRepository,
    preferencesManager: PreferencesManager,
    modifier: Modifier = Modifier
) {
    var videos by remember { mutableStateOf<List<VideoData>>(emptyList()) }
    var sortOrder by remember { mutableStateOf(SortOrder.NAME_ASC) }
    var searchActive by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    
    val folderName = remember(folderPath) {
        File(folderPath).name.ifEmpty { "Videos" }
    }

    LaunchedEffect(folderPath) {
        sortOrder = preferencesManager.getSortOrder()
        videos = videoRepository.getVideosInFolder(folderPath, sortOrder)
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
                            color = Color.White
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
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
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DeharBackground
                )
            )
        },
        modifier = modifier
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(DeharBackground)
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
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    itemsIndexed(filteredVideos) { _, video ->
                        VideoItemCard(
                            video = video,
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
    }
}
