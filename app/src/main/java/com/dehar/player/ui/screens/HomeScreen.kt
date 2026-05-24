package com.dehar.player.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Search
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
import com.dehar.player.ui.components.FolderItemCard
import com.dehar.player.ui.navigation.Routes
import com.dehar.player.ui.theme.DeharBackground
import com.dehar.player.ui.theme.DeharOrange
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    videoRepository: VideoRepository,
    preferencesManager: PreferencesManager,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    
    var folders by remember { mutableStateOf<List<FolderData>>(emptyList()) }
    var searchActive by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var sortOrder by remember { mutableStateOf(SortOrder.NAME_ASC) }
    var sortMenuExpanded by remember { mutableStateOf(false) }
    
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
                sortOrder = preferencesManager.getSortOrder()
                folders = videoRepository.getVideoFolders(sortOrder)
            }
        } else {
            Toast.makeText(context, "Permission required to display videos!", Toast.LENGTH_LONG).show()
        }
    }

    LaunchedEffect(Unit) {
        val check = ContextCompat.checkSelfPermission(context, permissionToRequest)
        if (check == PackageManager.PERMISSION_GRANTED) {
            storagePermissionGranted = true
            sortOrder = preferencesManager.getSortOrder()
            folders = videoRepository.getVideoFolders(sortOrder)
        } else {
            launcher.launch(permissionToRequest)
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
                            text = "Dehar Player",
                            fontWeight = FontWeight.Bold,
                            fontSize = 26.sp,
                            color = DeharOrange
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
                    
                    IconButton(onClick = { sortMenuExpanded = true }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.List,
                            contentDescription = "Sort Options",
                            tint = Color.White
                        )
                        DropdownMenu(
                            expanded = sortMenuExpanded,
                            onDismissRequest = { sortMenuExpanded = false }
                        ) {
                            SortOrder.values().forEach { order ->
                                DropdownMenuItem(
                                    text = { Text(order.name.replace("_", " ")) },
                                    onClick = {
                                        sortMenuExpanded = false
                                        scope.launch {
                                            preferencesManager.setSortOrder(order)
                                            sortOrder = order
                                            folders = videoRepository.getVideoFolders(sortOrder)
                                        }
                                    }
                                )
                            }
                        }
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
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Lock",
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
                        colors = ButtonDefaults.buttonColors(containerColor = DeharOrange)
                    ) {
                        Text("Grant Permission", color = Color.Black)
                    }
                }
            } else if (filteredFolders.isEmpty()) {
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
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 14.dp),
                    contentPadding = PaddingValues(bottom = 20.dp, top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
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
}
