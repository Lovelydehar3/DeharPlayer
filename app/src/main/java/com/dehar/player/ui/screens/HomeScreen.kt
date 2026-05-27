package com.dehar.player.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.dehar.player.data.FolderData
import com.dehar.player.data.PreferencesManager
import com.dehar.player.data.SortOrder
import com.dehar.player.data.VideoRepository
import com.dehar.player.data.FolderLayoutSettings
import com.dehar.player.player.MusicPlaybackManager
import com.dehar.player.ui.components.FolderItemCard
import com.dehar.player.ui.components.FolderGridItemCard
import com.dehar.player.ui.components.VideoItemCard
import com.dehar.player.ui.components.VideoGridItemCard
import com.dehar.player.ui.components.LayoutAndSortDialog
import com.dehar.player.ui.components.MiniPlayer
import com.dehar.player.ui.navigation.Routes
import com.dehar.player.ui.theme.DeharAccent
import com.dehar.player.ui.theme.DeharBlue
import com.dehar.player.ui.theme.DeharUnplayedCyan
import kotlinx.coroutines.launch

import androidx.compose.material.icons.filled.*
import androidx.compose.foundation.lazy.LazyRow


import com.dehar.player.ui.components.VideoInfoDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    videoRepository: VideoRepository,
    preferencesManager: PreferencesManager,
    musicPlaybackManager: MusicPlaybackManager,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    
    var folders by remember { mutableStateOf<List<FolderData>>(emptyList()) }
    var allVideos by remember { mutableStateOf<List<com.dehar.player.data.VideoData>>(emptyList()) }
    var resumeVideos by remember { mutableStateOf<List<FolderResume>>(emptyList()) }

    var selectedVideoForInfo by remember { mutableStateOf<com.dehar.player.data.VideoData?>(null) }

    data class SmartCollection(val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector, val items: List<com.dehar.player.data.VideoData>)
    val smartCollections by remember(allVideos) {
        derivedStateOf {
            listOf(
                SmartCollection("4K Videos", Icons.Default.Hd, 
                    allVideos.filter { it.width >= 3840 }),
                SmartCollection("Long Videos", Icons.Default.Schedule, 
                    allVideos.filter { it.duration > 3600_000L }),
                SmartCollection("Unwatched", Icons.Default.FiberNew, 
                    allVideos.filter { it.playCount == 0 && it.duration > 120_000L }),
                SmartCollection("Recently Added", Icons.Default.NewReleases, 
                    allVideos.filter { it.dateAdded > (System.currentTimeMillis() / 1000) - 30L * 24 * 3600 }),
                SmartCollection("Favorites", Icons.Default.Favorite, 
                    allVideos.filter { it.isFavorite })
            ).filter { it.items.isNotEmpty() }
        }
    }
    var searchActive by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    
    var layoutSettings by remember { mutableStateOf(FolderLayoutSettings()) }
    var layoutSortDialogVisible by remember { mutableStateOf(false) }
    
    var storagePermissionGranted by remember { mutableStateOf(false) }

    val permissionToRequest = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_VIDEO
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        storagePermissionGranted = isGranted
        if (isGranted) {
            scope.launch {
                layoutSettings = preferencesManager.getLayoutSettings()
                folders = videoRepository.getVideoFolders(layoutSettings.sortOrder)
                allVideos = videoRepository.getAllVideos(layoutSettings.sortOrder)
                resumeVideos = loadResumeVideos(folders, preferencesManager)
            }
        } else {
            Toast.makeText(context, "Permission required to display videos!", Toast.LENGTH_LONG).show()
        }
    }

    LaunchedEffect(Unit) {
        val check = ContextCompat.checkSelfPermission(context, permissionToRequest)
        if (check == PackageManager.PERMISSION_GRANTED) {
            storagePermissionGranted = true
            layoutSettings = preferencesManager.getLayoutSettings()
            folders = videoRepository.getVideoFolders(layoutSettings.sortOrder)
            allVideos = videoRepository.getAllVideos(layoutSettings.sortOrder)
            resumeVideos = loadResumeVideos(folders, preferencesManager)
        } else {
            launcher.launch(permissionToRequest)
        }
    }

    LaunchedEffect(storagePermissionGranted, layoutSettings) {
        if (storagePermissionGranted) {
            folders = videoRepository.getVideoFolders(layoutSettings.sortOrder)
            allVideos = videoRepository.getAllVideos(layoutSettings.sortOrder)
            resumeVideos = loadResumeVideos(folders, preferencesManager)
        }
    }

    val filteredFolders = remember(folders, searchQuery) {
        if (searchQuery.isEmpty()) {
            folders
        } else {
            folders.mapNotNull { folder ->
                val matchingVideos = folder.videos.filter { video ->
                    video.displayName.contains(searchQuery, ignoreCase = true) ||
                    video.title.contains(searchQuery, ignoreCase = true)
                }
                if (matchingVideos.isNotEmpty()) {
                    folder.copy(videos = matchingVideos)
                } else {
                    null
                }
            }
        }
    }

    val filteredVideos = remember(allVideos, searchQuery) {
        if (searchQuery.isEmpty()) {
            allVideos
        } else {
            allVideos.filter {
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
                            placeholder = { Text("Search folders & videos...", color = Color.Gray) },
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
                            text = "Folders",
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            color = Color(0xFFE8EDF3)
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
                    
                    IconButton(onClick = { layoutSortDialogVisible = true }) {
                        Icon(
                            imageVector = if (layoutSettings.layoutType == "LIST") Icons.Default.GridView else Icons.AutoMirrored.Filled.List,
                            contentDescription = "Layout and Sort Options",
                            tint = Color.White
                        )
                    }

                    IconButton(onClick = {
                        scope.launch {
                            val isLockSet = preferencesManager.isPinSet()
                            if (isLockSet) {
                                preferencesManager.clearPin()
                                Toast.makeText(context, "Password Lock removed", Toast.LENGTH_SHORT).show()
                            } else {
                                navController.navigate(Routes.LOCK)
                            }
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile",
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
            if (folders.isNotEmpty()) {
                FloatingActionButton(
                    onClick = {
                        if (resumeVideos.isNotEmpty()) {
                            val item = resumeVideos.first()
                            navController.navigate(Routes.player(item.index, item.video.folderPath))
                        } else {
                            val firstFolder = folders.firstOrNull()
                            if (firstFolder != null && firstFolder.videos.isNotEmpty()) {
                                navController.navigate(Routes.player(0, firstFolder.path))
                            }
                        }
                    },
                    containerColor = DeharUnplayedCyan,
                    contentColor = Color.White,
                    shape = CircleShape
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = "Play/Resume")
                }
            }
        },
        modifier = modifier
    ) { innerPadding ->
        if (selectedVideoForInfo != null) {
            VideoInfoDialog(
                uri = selectedVideoForInfo!!.uri,
                context = context,
                onDismiss = { selectedVideoForInfo = null }
            )
        }

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
            if (!storagePermissionGranted) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Storage Permission Required",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "This app requires access to read video files from your device storage in order to browse and play them.",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = { launcher.launch(permissionToRequest) },
                        colors = ButtonDefaults.buttonColors(containerColor = DeharBlue)
                    ) {
                        Text("Grant Permission", color = Color.Black)
                    }
                }
            } else if (layoutSettings.viewMode == "FILES" && filteredVideos.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No videos found.",
                        color = Color.Gray,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                }
            } else if (layoutSettings.viewMode != "FILES" && filteredFolders.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No folders containing videos found.",
                        color = Color.Gray,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 96.dp, top = 4.dp)
                ) {
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 18.dp, vertical = 12.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            HomeChip(icon = Icons.Filled.MusicNote, text = "Music", onClick = { navController.navigate(Routes.MUSIC_LIBRARY) })
                            HomeChip(icon = Icons.Filled.PlayCircle, text = "Video")
                            HomeChip(icon = Icons.Filled.Lock, text = "Vault", onClick = { navController.navigate(Routes.PRIVATE_VAULT) })
                            HomeChip(icon = Icons.Filled.Delete, text = "Bin", onClick = { navController.navigate(Routes.mediaManager("recycle_bin")) })
                            }
                            }
                    if (smartCollections.isNotEmpty()) {
                        item {
                            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                                Text(
                                    "Collections",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 8.dp)
                                )
                                LazyRow(
                                    contentPadding = PaddingValues(horizontal = 18.dp),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    items(smartCollections) { collection ->
                                        Surface(
                                            onClick = { 
                                                if (collection.title == "Favorites") {
                                                    // TODO: Navigate to favorites
                                                } else {
                                                    // TODO: Navigate to filtered view
                                                }
                                            },
                                            color = Color(0xFF1B2B3A),
                                            shape = RoundedCornerShape(12.dp),
                                            modifier = Modifier.width(140.dp)
                                        ) {
                                            Column(modifier = Modifier.padding(16.dp)) {
                                                Icon(
                                                    collection.icon,
                                                    contentDescription = null,
                                                    tint = DeharAccent,
                                                    modifier = Modifier.size(28.dp)
                                                )
                                                Spacer(Modifier.height(12.dp))
                                                Text(
                                                    collection.title,
                                                    style = MaterialTheme.typography.labelLarge,
                                                    color = Color.White,
                                                    maxLines = 1
                                                )
                                                Text(
                                                    "${collection.items.size} videos",
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = Color.Gray
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (resumeVideos.isNotEmpty()) {
                        item {
                            ContinuePlayingRow(
                                item = resumeVideos.first(),
                                onClick = {
                                    navController.navigate(
                                        Routes.player(resumeVideos.first().index, resumeVideos.first().video.folderPath)
                                    )
                                }
                            )
                        }
                    }

                    if (layoutSettings.viewMode == "FILES") {
                        if (layoutSettings.layoutType == "GRID") {
                            val chunkedVideos = filteredVideos.chunked(2)
                            items(chunkedVideos) { rowItems ->
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    rowItems.forEach { video ->
                                        val hasPlayed = resumeVideos.firstOrNull()?.video?.path == video.path
                                        VideoGridItemCard(
                                            video = video,
                                            isPlayed = hasPlayed,
                                            onClick = {
                                                val originalIndex = allVideos.indexOfFirst { it.id == video.id }.coerceAtLeast(0)
                                                navController.navigate(Routes.player(originalIndex, video.folderPath))
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
                            items(filteredVideos) { video ->
                                val hasPlayed = resumeVideos.firstOrNull()?.video?.path == video.path
                                VideoItemCard(
                                    video = video,
                                    isPlayed = hasPlayed,
                                    onClick = {
                                        val originalIndex = allVideos.indexOfFirst { it.id == video.id }.coerceAtLeast(0)
                                        navController.navigate(Routes.player(originalIndex, video.folderPath))
                                    },
                                    onDeleteClick = {
                                        scope.launch {
                                            videoRepository.moveToRecycleBin(video)
                                            // Refresh lists
                                            allVideos = videoRepository.getAllVideos(layoutSettings.sortOrder)
                                            folders = videoRepository.getVideoFolders(layoutSettings.sortOrder)
                                            Toast.makeText(context, "Moved to Recycle Bin", Toast.LENGTH_SHORT).show()
                                        }
                                    },
                                    onInfoClick = {
                                        selectedVideoForInfo = video
                                    }
                                )
                            }
                        }
                    } else {
                        if (layoutSettings.layoutType == "GRID") {
                            val chunkedFolders = filteredFolders.chunked(2)
                            items(chunkedFolders) { rowItems ->
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    rowItems.forEach { folder ->
                                        FolderGridItemCard(
                                            folder = folder,
                                            onClick = {
                                                navController.navigate(Routes.folder(folder.path))
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
                            items(filteredFolders) { folder ->
                                FolderItemCard(
                                    folder = folder,
                                    onClick = {
                                        navController.navigate(Routes.folder(folder.path))
                                    }
                                )
                            }
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

private suspend fun loadResumeVideos(
    folders: List<FolderData>,
    preferencesManager: PreferencesManager
): List<FolderResume> {
    val allVideos = folders.flatMap { it.videos }
    val positions = preferencesManager.getLastPositions(allVideos.map { it.path })
    return folders.flatMap { folder ->
        folder.videos.mapIndexedNotNull { index, video ->
            val position = positions[video.path] ?: 0L
            if (position > 15_000L && video.duration > position + 10_000L) {
                FolderResume(video, index, position)
            } else {
                null
            }
        }
    }.sortedByDescending { it.position }.take(1)
}

private data class FolderResume(
    val video: com.dehar.player.data.VideoData,
    val index: Int,
    val position: Long
)

@Composable
private fun HomeChip(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    onClick: () -> Unit = {}
) {
    Surface(
        onClick = onClick,
        color = Color(0xFF263544),
        shape = RoundedCornerShape(22.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(icon, contentDescription = null, tint = Color(0xFFE8EDF3), modifier = Modifier.size(18.dp))
            Text(text, color = Color(0xFFE8EDF3), fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
    }
}

@Composable
private fun ContinuePlayingRow(
    item: FolderResume,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        color = Color(0xFF1B2B3A),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.PlayArrow, contentDescription = null, tint = DeharAccent, modifier = Modifier.size(30.dp))
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text("Continue playing", color = DeharAccent, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Text(item.video.displayName, color = Color.White, maxLines = 1, fontSize = 16.sp)
            }
        }
    }
}
