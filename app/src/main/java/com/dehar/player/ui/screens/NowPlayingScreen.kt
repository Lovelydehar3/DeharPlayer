package com.dehar.player.ui.screens

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.Spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.dehar.player.data.PlaylistManager
import com.dehar.player.data.SongData
import com.dehar.player.player.MusicPlaybackManager
import com.dehar.player.ui.theme.DeharAccent
import com.dehar.player.core.common.TimeUtils
import kotlinx.coroutines.launch
import java.util.Collections

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NowPlayingScreen(
    navController: NavController,
    musicPlaybackManager: MusicPlaybackManager,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    
    val currentSong = musicPlaybackManager.currentSong
    val isPlaying = musicPlaybackManager.isPlaying
    val playlistManager = remember { PlaylistManager(context) }
    
    var isFavorite by remember { mutableStateOf(false) }
    
    // Bottom sheet dialog triggers
    var showQueueSheet by remember { mutableStateOf(false) }
    var showEQDialog by remember { mutableStateOf(false) }
    var showTimerDialog by remember { mutableStateOf(false) }
    var showSpeedDialog by remember { mutableStateOf(false) }
    var lyricsExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(currentSong) {
        currentSong?.let {
            isFavorite = playlistManager.isFavorite(it.id)
            if (lyricsExpanded) {
                musicPlaybackManager.loadLyrics(it)
            }
        }
    }

    if (currentSong == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            Text("No song loaded", color = Color.Gray)
        }
        return
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // 1. DYNAMIC BLURRED BACKDROP
        Box(
            modifier = Modifier
                .fillMaxSize()
                .blur(40.dp) // Dynamic high-fidelity blur
        ) {
            AsyncImage(
                model = currentSong.path,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                alpha = 0.28f, // Ambient low opacity back-layer
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.4f),
                                Color.Black.copy(alpha = 0.85f)
                            )
                        )
                    )
            )
        }

        // 2. MAIN SCROLL CONTAINER
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // TOP BAR (Back, Meta Details, Favorite, Menu)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(top = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "NOW PLAYING",
                        color = Color.Gray,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp
                    )
                    Text(
                        text = currentSong.album,
                        color = Color.LightGray,
                        fontSize = 14.sp,
                        maxLines = 1,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }

                Row {
                    // Favorite Heart Button
                    IconButton(
                        onClick = {
                            scope.launch {
                                val favoriteState = playlistManager.toggleFavorite(currentSong.id)
                                isFavorite = favoriteState
                                val msg = if (favoriteState) "Added to Favorites" else "Removed from Favorites"
                                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (isFavorite) DeharAccent else Color.White
                        )
                    }
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.MoreVert, contentDescription = "More", tint = Color.White)
                    }
                }
            }

            Spacer(modifier = Modifier.weight(0.12f))

            // ALBUM ARTWORK CARD
            Box(
                modifier = Modifier
                    .size(310.dp)
                    .shadow(16.dp, RoundedCornerShape(20.dp))
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.DarkGray)
            ) {
                AsyncImage(
                    model = currentSong.path,
                    contentDescription = "Album art",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.weight(0.12f))

            // SONG TITLE & ARTIST ROW
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = currentSong.title,
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = currentSong.artist,
                    color = Color.Gray,
                    fontSize = 15.sp,
                    maxLines = 1,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // ULTRA-THIN TIMELINE SEEKBAR ROW (Lark Player style!)
            Column(modifier = Modifier.fillMaxWidth()) {
                val duration = musicPlaybackManager.playbackDuration
                val currentPos = musicPlaybackManager.playbackPosition
                val buffered = musicPlaybackManager.playbackBuffered
                
                NowPlayingSeekbar(
                    value = if (duration > 0) currentPos.toFloat() / duration else 0f,
                    bufferedValue = if (duration > 0) buffered.toFloat() / duration else 0f,
                    onValueChange = { percent ->
                        val target = (percent * duration).toLong()
                        musicPlaybackManager.seekTo(target)
                    }
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = TimeUtils.formatDuration(currentPos),
                        color = Color.Gray,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = TimeUtils.formatDuration(duration),
                        color = Color.Gray,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.weight(0.08f))

            // CENTER AUDIO PLAY CONTROLS (Golden/Accent theme from screenshot 3!)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                // Previous
                IconButton(
                    onClick = { musicPlaybackManager.previous() },
                    modifier = Modifier.size(56.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.SkipPrevious,
                        contentDescription = "Previous",
                        tint = DeharAccent,
                        modifier = Modifier.size(36.dp)
                    )
                }
                
                Spacer(modifier = Modifier.width(36.dp))

                // Play / Pause Large center button
                Box(
                    modifier = Modifier
                        .size(76.dp)
                        .background(DeharAccent, CircleShape)
                        .clickable { musicPlaybackManager.togglePlayPause() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = "Play/Pause",
                        tint = Color.Black,
                        modifier = Modifier.size(44.dp)
                    )
                }

                Spacer(modifier = Modifier.width(36.dp))

                // Next
                IconButton(
                    onClick = { musicPlaybackManager.next() },
                    modifier = Modifier.size(56.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.SkipNext,
                        contentDescription = "Next",
                        tint = DeharAccent,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.weight(0.12f))

            // BOTTOM OPTIONS ROW (Equalizer, Lyrics Capsule, Shuffle/Repeat actions)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Left: Equalizer settings
                IconButton(onClick = { showEQDialog = true }) {
                    Icon(Icons.Default.Tune, contentDescription = "Equalizer", tint = Color.LightGray)
                }

                // Center: Lyrics Capsule button (Screenshot 3 style!)
                Surface(
                    onClick = {
                        lyricsExpanded = !lyricsExpanded
                        if (lyricsExpanded && musicPlaybackManager.lyricsLines.isEmpty()) {
                            musicPlaybackManager.loadLyrics(currentSong)
                        }
                    },
                    color = if (lyricsExpanded) DeharAccent.copy(alpha = 0.15f) else Color.White.copy(alpha = 0.08f),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.height(40.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 24.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val currentLyricText = if (musicPlaybackManager.currentLyricsLine >= 0) {
                            musicPlaybackManager.lyricsLines[musicPlaybackManager.currentLyricsLine].text
                        } else if (musicPlaybackManager.lyricsLoading) {
                            "Loading lyrics..."
                        } else {
                            "Lyrics"
                        }
                        Text(
                            text = currentLyricText,
                            color = DeharAccent,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                // Right: Play Queue trigger button
                IconButton(onClick = { showQueueSheet = true }) {
                    Icon(Icons.Default.QueueMusic, contentDescription = "Play Queue", tint = Color.LightGray)
                }
            }

            // LYRICS PANEL (Animated Visibility)
            AnimatedVisibility(
                visible = lyricsExpanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .padding(vertical = 12.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White.copy(alpha = 0.05f))
                ) {
                    when {
                        musicPlaybackManager.lyricsLoading -> {
                            CircularProgressIndicator(
                                modifier = Modifier.align(Alignment.Center),
                                color = DeharAccent
                            )
                        }
                        musicPlaybackManager.lyricsNotFound -> {
                            Column(
                                modifier = Modifier.align(Alignment.Center),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("No lyrics found", color = Color.Gray)
                                TextButton(onClick = { musicPlaybackManager.loadLyrics(currentSong) }) {
                                    Text("Retry", color = DeharAccent)
                                }
                            }
                        }
                        else -> {
                            val listState = rememberLazyListState()
                            
                            LaunchedEffect(musicPlaybackManager.currentLyricsLine) {
                                if (musicPlaybackManager.currentLyricsLine >= 0) {
                                    listState.animateScrollToItem(
                                        (musicPlaybackManager.currentLyricsLine - 2).coerceAtLeast(0)
                                    )
                                }
                            }
                            
                            LazyColumn(
                                state = listState,
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                itemsIndexed(musicPlaybackManager.lyricsLines) { index, line ->
                                    val isCurrent = musicPlaybackManager.currentLyricsLine == index
                                    Text(
                                        text = line.text,
                                        color = if (isCurrent) DeharAccent else Color.White.copy(
                                            alpha = if (index < musicPlaybackManager.currentLyricsLine) 0.4f else 0.7f
                                        ),
                                        fontSize = if (isCurrent) 18.sp else 15.sp,
                                        fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 6.dp)
                                            .clickable {
                                                musicPlaybackManager.seekTo(line.timestampMs)
                                            }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // EXTRA OPTIONS ROW (Shuffle, Repeat, Speed, Timer)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                // Shuffle Toggle
                IconButton(onClick = { musicPlaybackManager.toggleShuffle() }) {
                    Icon(
                        imageVector = Icons.Default.Shuffle,
                        contentDescription = "Shuffle",
                        tint = if (musicPlaybackManager.isShuffleEnabled) DeharAccent else Color.Gray
                    )
                }

                // Speed controls
                IconButton(onClick = { showSpeedDialog = true }) {
                    Icon(
                        imageVector = Icons.Default.Speed,
                        contentDescription = "Playback Speed",
                        tint = if (musicPlaybackManager.playbackSpeed != 1.0f) DeharAccent else Color.Gray
                    )
                }

                // Sleep timer countdown
                IconButton(onClick = { showTimerDialog = true }) {
                    Icon(
                        imageVector = Icons.Default.Timer,
                        contentDescription = "Sleep Timer",
                        tint = if (musicPlaybackManager.sleepTimerRemainingSec > 0) DeharAccent else Color.Gray
                    )
                }

                // Repeat Mode Toggle
                IconButton(onClick = { musicPlaybackManager.toggleRepeatMode() }) {
                    val icon = when (musicPlaybackManager.repeatMode) {
                        androidx.media3.common.Player.REPEAT_MODE_ONE -> Icons.Default.RepeatOne
                        androidx.media3.common.Player.REPEAT_MODE_ALL -> Icons.Default.Repeat
                        else -> Icons.Default.Repeat
                    }
                    Icon(
                        imageVector = icon,
                        contentDescription = "Repeat",
                        tint = if (musicPlaybackManager.repeatMode != androidx.media3.common.Player.REPEAT_MODE_OFF) DeharAccent else Color.Gray
                    )
                }
            }
        }
    }

    // --- PLAY QUEUE SHEET ---
    if (showQueueSheet) {
        ModalBottomSheet(
            onDismissRequest = { showQueueSheet = false },
            containerColor = Color(0xFF1E2833)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.6f)
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Play Queue", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text(
                        "${musicPlaybackManager.currentQueue.value.size} songs",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    itemsIndexed(musicPlaybackManager.currentQueue.value) { index, song ->
                        val isCurrent = musicPlaybackManager.currentSongIndex == index
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    if (isCurrent) DeharAccent.copy(alpha = 0.12f)
                                    else Color.Transparent,
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable {
                                    musicPlaybackManager.seekTo(0L)
                                    musicPlaybackManager.setQueue(musicPlaybackManager.currentQueue.value, index)
                                }
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = (index + 1).toString(),
                                color = if (isCurrent) DeharAccent else Color.Gray,
                                fontSize = 13.sp,
                                modifier = Modifier.width(28.dp)
                            )
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = song.title,
                                    color = if (isCurrent) DeharAccent else Color.White,
                                    fontSize = 15.sp,
                                    fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                                    maxLines = 1
                                )
                                Text(
                                    text = song.artist,
                                    color = Color.Gray,
                                    fontSize = 12.sp,
                                    maxLines = 1
                                )
                            }
                            IconButton(onClick = { musicPlaybackManager.removeTrack(index) }) {
                                Icon(Icons.Default.Close, contentDescription = "Remove", tint = Color.Gray)
                            }
                        }
                    }
                }
            }
        }
    }

    // --- EQUALIZER DIALOG ---
    if (showEQDialog) {
        AlertDialog(
            onDismissRequest = { showEQDialog = false },
            containerColor = Color.Black,
            titleContentColor = Color.White,
            textContentColor = Color.White,
            title = { Text("Equalizer", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Presets", color = Color.White)
                        var expanded by remember { mutableStateOf(false) }
                        Box {
                            Button(
                                onClick = { expanded = true },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.12f))
                            ) {
                                Text(musicPlaybackManager.eqPreset, color = Color.White)
                            }
                            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                                listOf("Normal", "Dance", "Classical", "Hip Hop", "Rock", "Pop").forEach { prs ->
                                    DropdownMenuItem(
                                        text = { Text(prs) },
                                        onClick = {
                                            expanded = false
                                            musicPlaybackManager.eqPreset = prs
                                            val factor = when(prs) {
                                                "Rock" -> 80f
                                                "Dance" -> 70f
                                                "Classical" -> 40f
                                                else -> 50f
                                            }
                                            musicPlaybackManager.eqBassBoost = factor * 0.7f
                                            musicPlaybackManager.eqTreble = factor * 0.8f
                                            for (i in 0..4) musicPlaybackManager.eqBands[i] = factor
                                        }
                                    )
                                }
                            }
                        }
                    }
                    
                    HorizontalDivider(color = Color.DarkGray)
                    
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Bass Boost", color = Color.Gray, fontSize = 13.sp)
                            Slider(
                                value = musicPlaybackManager.eqBassBoost,
                                onValueChange = { musicPlaybackManager.eqBassBoost = it },
                                valueRange = 0f..100f,
                                colors = SliderDefaults.colors(thumbColor = DeharAccent, activeTrackColor = DeharAccent)
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Treble Boost", color = Color.Gray, fontSize = 13.sp)
                            Slider(
                                value = musicPlaybackManager.eqTreble,
                                onValueChange = { musicPlaybackManager.eqTreble = it },
                                valueRange = 0f..100f,
                                colors = SliderDefaults.colors(thumbColor = DeharAccent, activeTrackColor = DeharAccent)
                            )
                        }
                    }
                    
                    HorizontalDivider(color = Color.DarkGray)
                    
                    Text("5-Band Equalizer", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    val freq = listOf("60 Hz", "230 Hz", "910 Hz", "4 kHz", "14 kHz")
                    freq.forEachIndexed { index, label ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = label, color = Color.Gray, fontSize = 12.sp, modifier = Modifier.width(54.dp))
                            Slider(
                                value = musicPlaybackManager.eqBands[index],
                                onValueChange = { musicPlaybackManager.eqBands[index] = it },
                                valueRange = 0f..100f,
                                colors = SliderDefaults.colors(thumbColor = DeharAccent, activeTrackColor = DeharAccent),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showEQDialog = false }) {
                    Text("Done", color = DeharAccent)
                }
            }
        )
    }

    // --- SLEEP TIMER DIALOG ---
    if (showTimerDialog) {
        AlertDialog(
            onDismissRequest = { showTimerDialog = false },
            containerColor = Color.Black,
            titleContentColor = Color.White,
            textContentColor = Color.White,
            title = { Text("Sleep Timer", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    val remaining = musicPlaybackManager.sleepTimerRemainingSec
                    if (remaining > 0) {
                        Text(
                            text = "Active Timer: ${remaining / 60}m ${remaining % 60}s left",
                            color = DeharAccent,
                            fontWeight = FontWeight.Bold
                        )
                        Button(
                            onClick = {
                                musicPlaybackManager.cancelSleepTimer()
                                showTimerDialog = false
                                Toast.makeText(context, "Timer cancelled", Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Cancel Timer", color = Color.White)
                        }
                    }
                    
                    // After current song — music specific
                    Button(
                        onClick = {
                            // Set a very long timer that will be cancelled when song ends
                            musicPlaybackManager.startSleepTimerAfterSong()
                            showTimerDialog = false
                            Toast.makeText(context, "Will stop after current song", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = DeharAccent.copy(alpha = 0.2f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("🎵 After current song ends", color = DeharAccent)
                    }

                    listOf(5, 15, 30, 60).forEach { mins ->
                        Button(
                            onClick = {
                                musicPlaybackManager.startSleepTimer(mins)
                                showTimerDialog = false
                                Toast.makeText(context, "Sleep Timer set to $mins minutes", Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.12f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("$mins minutes", color = Color.White)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showTimerDialog = false }) {
                    Text("Close", color = Color.Gray)
                }
            }
        )
    }

    // --- PLAYBACK SPEED DIALOG ---
    if (showSpeedDialog) {
        AlertDialog(
            onDismissRequest = { showSpeedDialog = false },
            containerColor = Color.Black,
            titleContentColor = Color.White,
            textContentColor = Color.White,
            title = { Text("Playback Speed", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Speed: ${musicPlaybackManager.playbackSpeed}x", color = Color.White)
                    Slider(
                        value = musicPlaybackManager.playbackSpeed,
                        onValueChange = { musicPlaybackManager.setSpeed(it) },
                        valueRange = 0.25f..3.0f,
                        colors = SliderDefaults.colors(thumbColor = DeharAccent, activeTrackColor = DeharAccent)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        TextButton(onClick = { musicPlaybackManager.setSpeed(1.0f) }) { Text("Normal", color = DeharAccent) }
                        TextButton(onClick = { musicPlaybackManager.setSpeed(1.5f) }) { Text("1.5x", color = Color.White) }
                        TextButton(onClick = { musicPlaybackManager.setSpeed(2.0f) }) { Text("2.0x", color = Color.White) }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showSpeedDialog = false }) {
                    Text("Done", color = DeharAccent)
                }
            }
        )
    }
}

@Composable
private fun NowPlayingSeekbar(
    value: Float,
    bufferedValue: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    var isDragging by remember { mutableStateOf(false) }
    var dragProgress by remember { mutableFloatStateOf(value) }
    
    val activeProgress = if (isDragging) dragProgress else value
    val thumbRadius by animateDpAsState(
        targetValue = if (isDragging) 6.dp else 4.dp,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "thumbRadius"
    )
    
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(32.dp)
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragStart = { offset ->
                        isDragging = true
                        val width = size.width
                        if (width > 0) {
                            dragProgress = (offset.x / width).coerceIn(0f, 1f)
                            onValueChange(dragProgress)
                        }
                    },
                    onDragEnd = { isDragging = false },
                    onDragCancel = { isDragging = false },
                    onHorizontalDrag = { change, dragAmount ->
                        change.consume()
                        val width = size.width
                        if (width > 0) {
                            dragProgress = (dragProgress + dragAmount / width).coerceIn(0f, 1f)
                            onValueChange(dragProgress)
                        }
                    }
                )
            }
    ) {
        val accentColor = DeharAccent
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .height(18.dp)
        ) {
            val width = size.width
            val height = size.height
            val centerY = height / 2f
            
            // Inactive Track
            drawLine(
                color = Color(0x22FFFFFF),
                start = Offset(0f, centerY),
                end = Offset(width, centerY),
                strokeWidth = 2.dp.toPx(),
                cap = StrokeCap.Round
            )
            
            // Buffered Track
            if (bufferedValue > 0f) {
                drawLine(
                    color = Color(0x11FFFFFF),
                    start = Offset(0f, centerY),
                    end = Offset(width * bufferedValue.coerceIn(0f, 1f), centerY),
                    strokeWidth = 2.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }
            
            // Active Track
            drawLine(
                color = accentColor,
                start = Offset(0f, centerY),
                end = Offset(width * activeProgress.coerceIn(0f, 1f), centerY),
                strokeWidth = 2.dp.toPx(),
                cap = StrokeCap.Round
            )
            
            // Scrubber Thumb
            drawCircle(
                color = accentColor,
                radius = thumbRadius.toPx(),
                center = Offset(width * activeProgress.coerceIn(0f, 1f), centerY)
            )
        }
    }
}
