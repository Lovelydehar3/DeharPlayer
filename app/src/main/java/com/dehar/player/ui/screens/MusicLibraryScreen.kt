package com.dehar.player.ui.screens

import android.app.Activity
import android.widget.Toast
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.animation.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.PlaylistPlay
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.dehar.player.data.*
import com.dehar.player.player.MusicPlaybackManager
import com.dehar.player.ui.components.MiniPlayer
import com.dehar.player.ui.navigation.Routes
import com.dehar.player.ui.theme.DeharAccent
import com.dehar.player.ui.theme.DeharUnplayedCyan
import kotlinx.coroutines.launch
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicLibraryScreen(
    navController: NavController,
    musicPlaybackManager: MusicPlaybackManager,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    
    val musicRepository = remember { MusicRepository(context) }
    val playlistManager = remember { PlaylistManager(context) }
    
    var songs by remember { mutableStateOf<List<SongData>>(emptyList()) }
    var playlists by remember { mutableStateOf<List<Playlist>>(emptyList()) }
    var favoriteSongIds by remember { mutableStateOf<Set<Long>>(emptySet()) }
    
    var selectedTab by remember { mutableIntStateOf(1) } // 0 = Videos, 1 = Songs, 2 = Playlists, 3 = Folders, 4 = Artists, 5 = Albums, 6 = Genres
    val tabs = listOf("Videos", "Songs", "Playlists", "Folders", "Artists", "Albums", "Genres")
    
    var searchActive by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    
    // Multi-select states (Screenshot 2)
    var isMultiSelectMode by remember { mutableStateOf(false) }
    var selectedSongs by remember { mutableStateOf(setOf<SongData>()) }
    
    // Dialog triggers
    var showCreatePlaylistDialog by remember { mutableStateOf(false) }
    var showPlaylistPickerDialog by remember { mutableStateOf(false) }
    
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.values.any { it }) {
            scope.launch { songs = musicRepository.getSongs() }
        }
    }

    LaunchedEffect(Unit) {
        val perms = if (Build.VERSION.SDK_INT >= 33)
            arrayOf(Manifest.permission.READ_MEDIA_AUDIO)
        else
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
            
        if (perms.any { ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED }) {
            permissionLauncher.launch(perms)
        } else {
            songs = musicRepository.getSongs()
        }
        playlists = playlistManager.getPlaylists()
        favoriteSongIds = playlistManager.getFavoriteSongIds()
    }
    
    // Refresh content helpers
    fun refreshPlaylists() {
        scope.launch { playlists = playlistManager.getPlaylists() }
    }
    
    // tab sorting and groupings
    val foldersGroup = remember(songs) {
        songs.groupBy { File(it.path).parent ?: "Internal Storage" }
    }
    val artistsGroup = remember(songs) {
        songs.groupBy { it.artist }
    }
    val albumsGroup = remember(songs) {
        songs.groupBy { it.album }
    }
    
    // Fuzzy Filtered lists
    val filteredSongs = remember(songs, searchQuery) {
        if (searchQuery.isEmpty()) songs
        else songs.filter {
            it.title.contains(searchQuery, ignoreCase = true) ||
            it.artist.contains(searchQuery, ignoreCase = true)
        }
    }
    
    val filteredPlaylists = remember(playlists, searchQuery) {
        if (searchQuery.isEmpty()) playlists
        else playlists.filter { it.name.contains(searchQuery, ignoreCase = true) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (isMultiSelectMode) {
                        Text(
                            text = "${selectedSongs.size} Selected",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = Color.White
                        )
                    } else if (searchActive) {
                        TextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("Search songs, artists...", color = Color.Gray) },
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
                            text = "Music Library",
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            color = Color(0xFFE8EDF3)
                        )
                    }
                },
                navigationIcon = {
                    if (isMultiSelectMode) {
                        IconButton(onClick = { 
                            isMultiSelectMode = false
                            selectedSongs = emptySet()
                        }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                        }
                    } else {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                        }
                    }
                },
                actions = {
                    if (!isMultiSelectMode) {
                        IconButton(onClick = {
                            searchActive = !searchActive
                            if (!searchActive) searchQuery = ""
                        }) {
                            Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.White)
                        }
                        IconButton(onClick = {
                            Toast.makeText(context, "Scanning local storage for fresh songs...", Toast.LENGTH_SHORT).show()
                            scope.launch { songs = musicRepository.getSongs() }
                        }) {
                            Icon(Icons.Default.Refresh, contentDescription = "Refresh", tint = Color.White)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        modifier = modifier
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // 1. Horizontal tab navigation row (Songs, Playlists, Folders, etc.)
                ScrollableTabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = Color.Transparent,
                    contentColor = DeharAccent,
                    edgePadding = 18.dp,
                    divider = {},
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            color = DeharAccent
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = {
                                if (index == 0) {
                                    // Videos tab -> Go back to Video HomeScreen
                                    navController.navigate(Routes.HOME) {
                                        popUpTo(Routes.HOME) { inclusive = true }
                                    }
                                } else {
                                    selectedTab = index
                                    isMultiSelectMode = false
                                    selectedSongs = emptySet()
                                }
                            },
                            text = {
                                Text(
                                    text = title,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                            },
                            selectedContentColor = DeharAccent,
                            unselectedContentColor = Color.Gray
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))

                // 2. Active Tab contents list
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    when (selectedTab) {
                        1 -> { // SONGS Tab
                            if (filteredSongs.isEmpty()) {
                                EmptyLibraryMessage(msg = "No local music files found.\nPut audio files in your storage to scan.")
                            } else {
                                LazyColumn(
                                    modifier = Modifier.fillMaxSize(),
                                    contentPadding = PaddingValues(bottom = 96.dp)
                                ) {
                                    itemsIndexed(filteredSongs) { idx, song ->
                                        val isCurrent = musicPlaybackManager.currentSong?.id == song.id
                                        val isSelected = selectedSongs.contains(song)
                                        SongListItem(
                                            song = song,
                                            isCurrent = isCurrent,
                                            isSelected = isSelected,
                                            isMultiSelectMode = isMultiSelectMode,
                                            onItemClick = {
                                                if (isMultiSelectMode) {
                                                    selectedSongs = if (isSelected) selectedSongs - song else selectedSongs + song
                                                    if (selectedSongs.isEmpty()) isMultiSelectMode = false
                                                } else {
                                                    musicPlaybackManager.setQueue(filteredSongs, idx)
                                                    navController.navigate(Routes.NOW_PLAYING)
                                                }
                                            },
                                            onLongClick = {
                                                if (!isMultiSelectMode) {
                                                    isMultiSelectMode = true
                                                    selectedSongs = setOf(song)
                                                }
                                            }
                                        )
                                    }
                                }
                            }
                        }
                        2 -> { // PLAYLISTS Tab
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(bottom = 96.dp)
                            ) {
                                // Add New playlist item
                                item {
                                    PlaylistsAddNewCard(onClick = { showCreatePlaylistDialog = true })
                                }
                                
                                itemsIndexed(filteredPlaylists) { _, playlist ->
                                    PlaylistCardItem(
                                        playlist = playlist,
                                        onSelect = {
                                            // Extract playlist tracks from standard query map
                                            val pSongs = songs.filter { playlist.songIds.contains(it.id) }
                                            if (pSongs.isNotEmpty()) {
                                                musicPlaybackManager.setQueue(pSongs, 0)
                                                navController.navigate(Routes.NOW_PLAYING)
                                            } else {
                                                Toast.makeText(context, "Playlist is empty! Long-press songs to add them.", Toast.LENGTH_LONG).show()
                                            }
                                        },
                                        onDelete = {
                                            scope.launch {
                                                playlistManager.deletePlaylist(playlist.name)
                                                refreshPlaylists()
                                                Toast.makeText(context, "Deleted Playlist", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    )
                                }
                            }
                        }
                        3 -> { // FOLDERS Tab
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(bottom = 96.dp)
                            ) {
                                foldersGroup.forEach { (parentPath, folderSongs) ->
                                    item {
                                        GroupedHeaderItem(
                                            icon = Icons.Default.Folder,
                                            title = File(parentPath).name.ifEmpty { "Root Storage" },
                                            subtitle = "${folderSongs.size} songs - $parentPath",
                                            onClick = {
                                                musicPlaybackManager.setQueue(folderSongs, 0)
                                                navController.navigate(Routes.NOW_PLAYING)
                                            }
                                        )
                                    }
                                }
                            }
                        }
                        4 -> { // ARTISTS Tab
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(bottom = 96.dp)
                            ) {
                                artistsGroup.forEach { (artist, artistSongs) ->
                                    item {
                                        GroupedHeaderItem(
                                            icon = Icons.Default.Person,
                                            title = artist,
                                            subtitle = "${artistSongs.size} tracks",
                                            onClick = {
                                                musicPlaybackManager.setQueue(artistSongs, 0)
                                                navController.navigate(Routes.NOW_PLAYING)
                                            }
                                        )
                                    }
                                }
                            }
                        }
                        5 -> { // ALBUMS Tab
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(bottom = 96.dp)
                            ) {
                                albumsGroup.forEach { (album, albumSongs) ->
                                    item {
                                        GroupedHeaderItem(
                                            icon = Icons.Default.Album,
                                            title = album,
                                            subtitle = "${albumSongs.size} songs",
                                            onClick = {
                                                musicPlaybackManager.setQueue(albumSongs, 0)
                                                navController.navigate(Routes.NOW_PLAYING)
                                            }
                                        )
                                    }
                                }
                            }
                        }
                        6 -> { // GENRES Tab
                            var genres by remember { mutableStateOf<List<com.dehar.player.data.GenreItem>>(emptyList()) }
                            LaunchedEffect(Unit) {
                                musicRepository.getGenres().collect { genres = it }
                            }
                            GenresTab(genres = genres) { genreName ->
                                // Optional: navigate to specific genre list
                            }
                        }
                    }
                }
            }

            // 3. Floating Bottom Multi-Select Actions Bar (Screenshot 2 style)
            AnimatedVisibility(
                visible = isMultiSelectMode,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it }),
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                Surface(
                    color = Color(0xFF1E2833),
                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        MultiSelectActionButton(icon = Icons.Default.PlaylistAdd, label = "Add to") {
                            showPlaylistPickerDialog = true
                        }
                        MultiSelectActionButton(icon = Icons.AutoMirrored.Filled.PlaylistPlay, label = "Play next") {
                            selectedSongs.forEach { musicPlaybackManager.playNext(it) }
                            isMultiSelectMode = false
                            selectedSongs = emptySet()
                            Toast.makeText(context, "Added items to Play Next queue", Toast.LENGTH_SHORT).show()
                        }
                        MultiSelectActionButton(icon = Icons.Default.Share, label = "Share") {
                            try {
                                val uris = arrayListOf<android.net.Uri>()
                                selectedSongs.forEach { uris.add(android.net.Uri.parse(it.uri)) }
                                val intent = android.content.Intent().apply {
                                    action = android.content.Intent.ACTION_SEND_MULTIPLE
                                    type = "audio/*"
                                    putParcelableArrayListExtra(android.content.Intent.EXTRA_STREAM, uris)
                                }
                                context.startActivity(android.content.Intent.createChooser(intent, "Share Audio Tracks"))
                            } catch (e: Exception) {
                                Toast.makeText(context, "Share error: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                            isMultiSelectMode = false
                            selectedSongs = emptySet()
                        }
                        MultiSelectActionButton(icon = Icons.Default.Delete, label = "Delete") {
                            // physically mock remove
                            songs = songs - selectedSongs
                            isMultiSelectMode = false
                            selectedSongs = emptySet()
                            Toast.makeText(context, "Removed items from active view", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

            // 4. Floating bottom MiniPlayer overlay (when not in multi-select mode)
            if (musicPlaybackManager.currentSong != null && !isMultiSelectMode) {
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

    // --- CREATE PLAYLIST DIALOG ---
    if (showCreatePlaylistDialog) {
        var playlistName by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showCreatePlaylistDialog = false },
            containerColor = Color.Black,
            titleContentColor = Color.White,
            textContentColor = Color.White,
            title = { Text("Create New Playlist", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
            text = {
                OutlinedTextField(
                    value = playlistName,
                    onValueChange = { playlistName = it },
                    label = { Text("Playlist name", color = Color.Gray) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = DeharAccent,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TextButton(onClick = { showCreatePlaylistDialog = false }) {
                        Text("Cancel", color = Color.Gray)
                    }
                    Button(
                        onClick = {
                            scope.launch {
                                val success = playlistManager.createPlaylist(playlistName)
                                showCreatePlaylistDialog = false
                                if (success) {
                                    refreshPlaylists()
                                    Toast.makeText(context, "Playlist created successfully!", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(context, "Playlist name already exists or is blank", Toast.LENGTH_SHORT).show()
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = DeharAccent)
                    ) {
                        Text("Create", color = Color.Black)
                    }
                }
            }
        )
    }

    // --- PLAYLIST PICKER DIALOG ---
    if (showPlaylistPickerDialog) {
        AlertDialog(
            onDismissRequest = { showPlaylistPickerDialog = false },
            containerColor = Color.Black,
            titleContentColor = Color.White,
            textContentColor = Color.White,
            title = { Text("Select Target Playlist", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(Color.Transparent)
                ) {
                    if (playlists.isEmpty()) {
                        Text("No playlists found. Create one first!", color = Color.Gray, modifier = Modifier.padding(16.dp))
                    } else {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            itemsIndexed(playlists) { _, playlist ->
                                TextButton(
                                    onClick = {
                                        scope.launch {
                                            playlistManager.addSongsToPlaylist(
                                                playlist.name,
                                                selectedSongs.map { it.id }
                                            )
                                            showPlaylistPickerDialog = false
                                            isMultiSelectMode = false
                                            selectedSongs = emptySet()
                                            refreshPlaylists()
                                            Toast.makeText(context, "Added songs to ${playlist.name}!", Toast.LENGTH_SHORT).show()
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = playlist.name,
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Start
                                    )
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showPlaylistPickerDialog = false }) {
                    Text("Close", color = Color.Gray)
                }
            }
        )
    }
}

@Composable
fun GenresTab(genres: List<com.dehar.player.data.GenreItem>, onGenreClick: (String) -> Unit) {
    if (genres.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No genres found", color = Color.Gray)
        }
    } else {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(genres) { genre ->
                ListItem(
                    headlineContent = { Text(genre.name, color = Color.White) },
                    supportingContent = { Text("${genre.songCount} songs", color = Color.Gray) },
                    leadingContent = {
                        Icon(Icons.Default.MusicNote, null, tint = DeharAccent)
                    },
                    modifier = Modifier.clickable { onGenreClick(genre.name) },
                    colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                )
                HorizontalDivider(color = Color.White.copy(alpha = 0.1f))
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun SongListItem(
    song: SongData,
    isCurrent: Boolean,
    isSelected: Boolean,
    isMultiSelectMode: Boolean,
    onItemClick: () -> Unit,
    onLongClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                if (isSelected) DeharAccent.copy(alpha = 0.25f)
                else if (isCurrent) DeharAccent.copy(alpha = 0.08f)
                else Color.Transparent
            )
            .combinedClickable(
                onClick = onItemClick,
                onLongClick = onLongClick
            )
            .padding(horizontal = 18.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 1. Multi-select check circles (Screenshot 2 style)
        if (isMultiSelectMode) {
            Icon(
                imageVector = if (isSelected) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                contentDescription = "Selection State",
                tint = if (isSelected) DeharAccent else Color.Gray,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
        }

        // 2. Artwork image or cover stub on the left
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White.copy(alpha = 0.08f)),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = song.path,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        // 3. Track Details: Title, Lyrics badge, Artist, Album
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = song.title,
                color = if (isCurrent) DeharAccent else Color.White,
                fontSize = 15.sp,
                fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Medium,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(2.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // "LYRICS" small badge (Screenshot 2 style!)
                Surface(
                    color = Color.White.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(3.dp),
                    modifier = Modifier.height(14.dp)
                ) {
                    Text(
                        text = "LYRICS",
                        color = Color.LightGray,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 4.dp),
                        textAlign = TextAlign.Center
                    )
                }
                
                Text(
                    text = "${song.artist} - ${song.album}",
                    color = Color.Gray,
                    fontSize = 12.sp,
                    maxLines = 1
                )
            }
        }

        // 4. Edit Pen icon (Screenshot 2 style) or dropdown vertical dots
        IconButton(onClick = {}) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit metadata",
                tint = Color.Gray,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun PlaylistsAddNewCard(
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E2833)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 8.dp)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White.copy(alpha = 0.08f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add playlist", tint = DeharAccent, modifier = Modifier.size(28.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Add new playlist",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun PlaylistCardItem(
    playlist: Playlist,
    onSelect: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1B2633)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 6.dp)
            .clickable { onSelect() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White.copy(alpha = 0.08f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.QueueMusic, contentDescription = null, tint = DeharAccent, modifier = Modifier.size(26.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = playlist.name,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${playlist.songIds.size} songs",
                    color = Color.Gray,
                    fontSize = 13.sp
                )
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red.copy(alpha = 0.8f))
            }
        }
    }
}

@Composable
private fun GroupedHeaderItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 18.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.08f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = DeharAccent, modifier = Modifier.size(22.dp))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
            Text(
                text = subtitle,
                color = Color.Gray,
                fontSize = 13.sp,
                maxLines = 1
            )
        }
        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = Color.Gray)
    }
}

@Composable
private fun MultiSelectActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Icon(imageVector = icon, contentDescription = label, tint = Color.White, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = label, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun EmptyLibraryMessage(msg: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = msg,
            color = Color.Gray,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp
        )
    }
}
