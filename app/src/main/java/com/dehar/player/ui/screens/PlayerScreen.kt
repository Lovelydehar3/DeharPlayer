package com.dehar.player.ui.screens

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.media.AudioManager
import android.net.Uri
import android.view.WindowManager
import androidx.annotation.OptIn
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController
import com.dehar.player.data.PreferencesManager
import com.dehar.player.data.VideoData
import com.dehar.player.data.VideoRepository
import com.dehar.player.player.PlayerManager
import com.dehar.player.player.MediaTrackOption
import com.dehar.player.ui.theme.DeharOrange
import com.dehar.player.utils.TimeUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(UnstableApi::class)
@Composable
fun PlayerScreen(
    videoIndex: Int,
    folderPath: String,
    navController: NavController,
    videoRepository: VideoRepository,
    preferencesManager: PreferencesManager,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val activity = context as? Activity
    val scope = rememberCoroutineScope()

    var videos by remember { mutableStateOf<List<VideoData>>(emptyList()) }
    var currentIndex by remember { mutableIntStateOf(videoIndex) }
    
    val playerManager = remember { PlayerManager(context).apply { initialize() } }
    var showControls by remember { mutableStateOf(true) }
    var isControlsLocked by remember { mutableStateOf(false) }

    // Gesture indicator states
    var gestureIndicatorText by remember { mutableStateOf<String?>(null) }
    var gestureIndicatorIcon by remember { mutableIntStateOf(0) } // 1 = Vol, 2 = Bright, 3 = Seek

    // Speed & Aspect Ratio
    var speedMenuExpanded by remember { mutableStateOf(false) }
    var audioDialogVisible by remember { mutableStateOf(false) }
    var subtitleDialogVisible by remember { mutableStateOf(false) }
    var decoderDialogVisible by remember { mutableStateOf(false) }
    var currentSpeed by remember { mutableFloatStateOf(1.0f) }
    var currentResizeMode by remember { mutableIntStateOf(AspectRatioFrameLayout.RESIZE_MODE_FIT) }
    var decoderMode by remember { mutableStateOf("HW decoder") }

    // Position tracking
    var currentPos by remember { mutableLongStateOf(0L) }
    var duration by remember { mutableLongStateOf(0L) }
    var isPlaying by remember { mutableStateOf(false) }

    // Brightness/Volume tracking
    val audioManager = remember { context.getSystemService(Context.AUDIO_SERVICE) as AudioManager }
    val maxVolume = remember { audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) }

    DisposableEffect(lifecycleOwner, playerManager) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE, Lifecycle.Event.ON_STOP -> playerManager.pause()
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    // Immersive screen setup
    LaunchedEffect(Unit) {
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        
        // Hide System bars
        activity?.window?.let { window ->
            val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
            windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
        }

        // Get videos
        val sortOrder = preferencesManager.getSortOrder()
        videos = videoRepository.getVideosInFolder(folderPath, sortOrder)
    }

    // Release player on back
    DisposableEffect(Unit) {
        onDispose {
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            
            // Show System bars
            activity?.window?.let { window ->
                val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
                windowInsetsController.show(WindowInsetsCompat.Type.systemBars())
            }

            scope.launch {
                val currentVid = playerManager.getCurrentVideo()
                if (currentVid != null) {
                    preferencesManager.setLastPosition(currentVid.path, playerManager.getCurrentPosition())
                }
                playerManager.release()
            }
        }
    }

    // Setup playlist once videos are loaded
    LaunchedEffect(videos) {
        if (videos.isNotEmpty() && currentIndex in videos.indices) {
            playerManager.setPlaylist(videos, currentIndex)
            
            // Retrieve last position
            val lastPos = preferencesManager.getLastPosition(videos[currentIndex].path)
            playerManager.playAt(currentIndex, lastPos)
            
            // Track playback position
            while (true) {
                currentPos = playerManager.getCurrentPosition()
                duration = playerManager.getDuration()
                isPlaying = playerManager.isPlaying()
                delay(500)
            }
        }
    }

    // Auto-hide controls
    LaunchedEffect(showControls, isControlsLocked) {
        if (showControls && !isControlsLocked) {
            delay(3500)
            showControls = false
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Video View
        AndroidView(
            factory = { ctx ->
                PlayerView(ctx).apply {
                    useController = false
                    resizeMode = currentResizeMode
                    player = playerManager.exoPlayer
                }
            },
            update = { view ->
                view.player = playerManager.exoPlayer
                view.resizeMode = currentResizeMode
            },
            modifier = Modifier.fillMaxSize()
        )

        // Transparent Gesture Capture Overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(isControlsLocked) {
                    detectTapGestures(
                        onDoubleTap = { offset ->
                            if (!isControlsLocked) {
                                val isLeftHalf = offset.x < size.width / 2
                                if (isLeftHalf) {
                                    playerManager.seekBackward(10000L)
                                    gestureIndicatorText = "-10s"
                                    gestureIndicatorIcon = 3
                                } else {
                                    playerManager.seekForward(10000L)
                                    gestureIndicatorText = "+10s"
                                    gestureIndicatorIcon = 3
                                }
                                scope.launch {
                                    delay(1000)
                                    gestureIndicatorText = null
                                }
                            }
                        },
                        onTap = {
                            showControls = !showControls
                        }
                    )
                }
                .pointerInput(isControlsLocked) {
                    if (isControlsLocked) return@pointerInput
                    detectVerticalDragGestures { change, dragAmount ->
                        val isLeftHalf = change.position.x < size.width / 2
                        if (isLeftHalf) {
                            // Adjust Brightness
                            val lp = activity?.window?.attributes
                            val currentBrightness = lp?.screenBrightness ?: 0.5f
                            val newBrightness = (currentBrightness - (dragAmount / 500f)).coerceIn(0.01f, 1.0f)
                            lp?.screenBrightness = newBrightness
                            activity?.window?.attributes = lp
                            
                            gestureIndicatorText = "Brightness: ${(newBrightness * 100).toInt()}%"
                            gestureIndicatorIcon = 2
                        } else {
                            // Adjust Volume
                            val currentVol = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
                            val volDiff = if (dragAmount > 0) -1 else 1
                            val newVol = (currentVol + volDiff).coerceIn(0, maxVolume)
                            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, newVol, 0)
                            
                            gestureIndicatorText = "Volume: ${(newVol * 100 / maxVolume)}%"
                            gestureIndicatorIcon = 1
                        }
                        
                        scope.launch {
                            delay(1500)
                            gestureIndicatorText = null
                        }
                    }
                }
                .pointerInput(isControlsLocked, duration) {
                    if (isControlsLocked) return@pointerInput
                    detectHorizontalDragGestures(
                        onHorizontalDrag = { _, dragAmount ->
                            if (duration <= 0L) return@detectHorizontalDragGestures
                            val deltaMs = ((dragAmount / size.width) * 90_000L).toLong()
                            val target = (playerManager.getCurrentPosition() + deltaMs).coerceIn(0L, duration)
                            playerManager.exoPlayer?.seekTo(target)
                            gestureIndicatorText = TimeUtils.formatDuration(target)
                            gestureIndicatorIcon = 3
                        },
                        onDragEnd = {
                            scope.launch {
                                delay(1000)
                                gestureIndicatorText = null
                            }
                        },
                        onDragCancel = {
                            gestureIndicatorText = null
                        }
                    )
                }
        )

        // Custom Overlay Controls
        AnimatedVisibility(
            visible = showControls,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
            ) {
                if (isControlsLocked) {
                    // Lock control only
                    IconButton(
                        onClick = { isControlsLocked = false },
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(24.dp)
                            .size(64.dp)
                            .background(Color.Black.copy(alpha = 0.6f), shape = CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Unlock Controls",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                } else {
                    // Full controls overlay
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Top bar
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(onClick = { navController.popBackStack() }) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Back",
                                        tint = Color.White
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = videos.getOrNull(currentIndex)?.displayName ?: "",
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1
                                )
                            }

                            Row {
                                PlayerTextButton(
                                    text = "AUD",
                                    onClick = { audioDialogVisible = true }
                                )

                                PlayerTextButton(
                                    text = "SUB",
                                    onClick = { subtitleDialogVisible = true }
                                )

                                PlayerTextButton(
                                    text = decoderMode.substringBefore(" "),
                                    onClick = { decoderDialogVisible = true }
                                )

                                IconButton(onClick = {
                                    currentResizeMode = when (currentResizeMode) {
                                        AspectRatioFrameLayout.RESIZE_MODE_FIT -> AspectRatioFrameLayout.RESIZE_MODE_FILL
                                        AspectRatioFrameLayout.RESIZE_MODE_FILL -> AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                                        else -> AspectRatioFrameLayout.RESIZE_MODE_FIT
                                    }
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Settings,
                                        contentDescription = "Aspect Ratio",
                                        tint = Color.White
                                    )
                                }

                                IconButton(onClick = { speedMenuExpanded = true }) {
                                    Text(
                                        text = "${currentSpeed.cleanSpeed()}x",
                                        color = Color.White,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    DropdownMenu(
                                        expanded = speedMenuExpanded,
                                        onDismissRequest = { speedMenuExpanded = false }
                                    ) {
                                        listOf(0.5f, 0.75f, 1.0f, 1.25f, 1.5f, 2.0f).forEach { spd ->
                                            DropdownMenuItem(
                                                text = { Text("${spd}x") },
                                                onClick = {
                                                    speedMenuExpanded = false
                                                    currentSpeed = spd
                                                    playerManager.setPlaybackSpeed(spd)
                                                }
                                            )
                                        }
                                    }
                                }

                                IconButton(onClick = { isControlsLocked = true }) {
                                    Icon(
                                        imageVector = Icons.Default.Lock,
                                        contentDescription = "Lock controls",
                                        tint = Color.White
                                    )
                                }
                            }
                        }

                        // Center Controls (Play/Pause, Prev, Next)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.CenterHorizontally),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = {
                                    if (playerManager.playPrevious()) {
                                        currentIndex--
                                        playerManager.updateIndex(currentIndex)
                                    }
                                },
                                modifier = Modifier.size(56.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                    contentDescription = "Previous",
                                    tint = Color.White,
                                    modifier = Modifier.size(36.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(32.dp))

                            IconButton(
                                onClick = { playerManager.togglePlayPause() },
                                modifier = Modifier
                                    .size(72.dp)
                                    .background(Color.Black.copy(alpha = 0.5f), shape = CircleShape)
                            ) {
                                if (isPlaying) {
                                    Text(
                                        text = "II",
                                        color = Color.White,
                                        fontSize = 30.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.PlayArrow,
                                        contentDescription = "Play",
                                        tint = Color.White,
                                        modifier = Modifier.size(48.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(32.dp))

                            IconButton(
                                onClick = {
                                    if (playerManager.playNext()) {
                                        currentIndex++
                                        playerManager.updateIndex(currentIndex)
                                    }
                                },
                                modifier = Modifier.size(56.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                    contentDescription = "Next",
                                    tint = Color.White,
                                    modifier = Modifier.size(36.dp)
                                )
                            }
                        }

                        // Bottom bar (Time, seek slider)
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.Black.copy(alpha = 0.6f))
                                .padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = TimeUtils.formatDuration(currentPos),
                                    color = Color.White,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = TimeUtils.formatDuration(duration),
                                    color = Color.White,
                                    fontSize = 14.sp
                                )
                            }
                            
                            Slider(
                                value = if (duration > 0) currentPos.toFloat() / duration else 0f,
                                onValueChange = { percent ->
                                    val newPos = (percent * duration).toLong()
                                    playerManager.exoPlayer?.seekTo(newPos)
                                },
                                colors = SliderDefaults.colors(
                                    thumbColor = DeharOrange,
                                    activeTrackColor = DeharOrange,
                                    inactiveTrackColor = Color.DarkGray
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }

        if (audioDialogVisible) {
            TrackChoiceDialog(
                title = "Audio Track",
                options = playerManager.getAudioTracks(),
                emptyText = "No extra audio tracks found",
                disabledText = "Disable audio",
                onSelect = {
                    playerManager.selectAudioTrack(it)
                    audioDialogVisible = false
                },
                onDismiss = { audioDialogVisible = false }
            )
        }

        if (subtitleDialogVisible) {
            TrackChoiceDialog(
                title = "Subtitle",
                options = playerManager.getSubtitleTracks(),
                emptyText = "No embedded subtitles found",
                disabledText = "Disable subtitles",
                onSelect = {
                    playerManager.selectSubtitleTrack(it)
                    subtitleDialogVisible = false
                },
                onDismiss = { subtitleDialogVisible = false }
            )
        }

        if (decoderDialogVisible) {
            DecoderChoiceDialog(
                selected = decoderMode,
                onSelect = {
                    decoderMode = it
                    decoderDialogVisible = false
                },
                onDismiss = { decoderDialogVisible = false }
            )
        }

        // Gesture Overlay Visual Indicators
        AnimatedVisibility(
            visible = gestureIndicatorText != null,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.Center)
        ) {
            Box(
                modifier = Modifier
                    .background(Color.Black.copy(alpha = 0.8f), shape = MaterialTheme.shapes.medium)
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val icon = when (gestureIndicatorIcon) {
                        1 -> Icons.Default.Settings
                        2 -> Icons.Default.Settings
                        else -> Icons.AutoMirrored.Filled.KeyboardArrowRight
                    }
                    Icon(
                        imageVector = icon,
                        contentDescription = "Indicator",
                        tint = DeharOrange,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = gestureIndicatorText ?: "",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@OptIn(UnstableApi::class)
@Composable
fun ExternalPlayerScreen(
    uri: Uri,
    displayName: String,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val activity = context as? Activity
    val playerManager = remember { PlayerManager(context).apply { initialize() } }

    var showControls by remember { mutableStateOf(true) }
    var currentPos by remember { mutableLongStateOf(0L) }
    var duration by remember { mutableLongStateOf(0L) }
    var isPlaying by remember { mutableStateOf(false) }

    DisposableEffect(lifecycleOwner, playerManager) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE, Lifecycle.Event.ON_STOP -> playerManager.pause()
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    LaunchedEffect(uri) {
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        activity?.window?.let { window ->
            val controller = WindowCompat.getInsetsController(window, window.decorView)
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            controller.hide(WindowInsetsCompat.Type.systemBars())
        }

        playerManager.setSingleMedia(uri)

        while (true) {
            currentPos = playerManager.getCurrentPosition()
            duration = playerManager.getDuration()
            isPlaying = playerManager.isPlaying()
            delay(500)
        }
    }

    LaunchedEffect(showControls) {
        if (showControls) {
            delay(3500)
            showControls = false
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            activity?.window?.let { window ->
                WindowCompat.getInsetsController(window, window.decorView)
                    .show(WindowInsetsCompat.Type.systemBars())
            }
            playerManager.release()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .pointerInput(Unit) {
                detectTapGestures(onTap = { showControls = !showControls })
            }
    ) {
        AndroidView(
            factory = { ctx ->
                PlayerView(ctx).apply {
                    useController = false
                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                    player = playerManager.exoPlayer
                }
            },
            update = { view -> view.player = playerManager.exoPlayer },
            modifier = Modifier.fillMaxSize()
        )

        AnimatedVisibility(
            visible = showControls,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .align(Alignment.TopCenter),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        if (!navController.popBackStack()) {
                            activity?.finish()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                    Text(
                        text = displayName,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                }

                IconButton(
                    onClick = { playerManager.togglePlayPause() },
                    modifier = Modifier
                        .size(72.dp)
                        .align(Alignment.Center)
                        .background(Color.Black.copy(alpha = 0.5f), shape = CircleShape)
                ) {
                    if (isPlaying) {
                        Text(
                            text = "II",
                            color = Color.White,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Play",
                            tint = Color.White,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .background(Color.Black.copy(alpha = 0.6f))
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(TimeUtils.formatDuration(currentPos), color = Color.White, fontSize = 14.sp)
                        Text(TimeUtils.formatDuration(duration), color = Color.White, fontSize = 14.sp)
                    }

                    Slider(
                        value = if (duration > 0) currentPos.toFloat() / duration else 0f,
                        onValueChange = { percent ->
                            playerManager.exoPlayer?.seekTo((percent * duration).toLong())
                        },
                        colors = SliderDefaults.colors(
                            thumbColor = DeharOrange,
                            activeTrackColor = DeharOrange,
                            inactiveTrackColor = Color.DarkGray
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
private fun PlayerTextButton(
    text: String,
    onClick: () -> Unit
) {
    TextButton(
        onClick = onClick,
        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun TrackChoiceDialog(
    title: String,
    options: List<MediaTrackOption>,
    emptyText: String,
    disabledText: String,
    onSelect: (MediaTrackOption?) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.Black,
        titleContentColor = Color.White,
        textContentColor = Color.White,
        title = {
            Text(
                text = title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                if (options.isEmpty()) {
                    Text(emptyText, color = Color.Gray, fontSize = 16.sp)
                } else {
                    options.forEach { option ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = option.selected,
                                onClick = { onSelect(option) },
                                enabled = option.supported,
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = Color(0xFF4DB1FF),
                                    unselectedColor = Color.LightGray,
                                    disabledUnselectedColor = Color.DarkGray
                                )
                            )
                            Text(
                                text = option.label,
                                color = if (option.supported) Color.White else Color.Gray,
                                fontSize = 18.sp,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                }

                Divider(color = Color.DarkGray)

                TextButton(onClick = { onSelect(null) }) {
                    Text(disabledText, color = Color.White, fontSize = 16.sp)
                }
            }
        },
        confirmButton = {}
    )
}

@Composable
private fun DecoderChoiceDialog(
    selected: String,
    onSelect: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val choices = listOf("HW decoder", "HW+ decoder", "SW decoder")
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.Black,
        titleContentColor = Color.White,
        textContentColor = Color.White,
        title = {
            Text(
                text = "Select decoder",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
                choices.forEach { choice ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selected == choice,
                            onClick = { onSelect(choice) },
                            colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF4DB1FF))
                        )
                        Text(
                            text = choice,
                            color = Color.White,
                            fontSize = 20.sp,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
                Text(
                    text = "Decoder selection is saved as a playback preference. Android Media3 chooses the safest available hardware path for each file.",
                    color = Color.Gray,
                    fontSize = 13.sp
                )
            }
        },
        confirmButton = {}
    )
}

private fun Float.cleanSpeed(): String {
    return if (this % 1f == 0f) this.toInt().toString() else this.toString()
}
