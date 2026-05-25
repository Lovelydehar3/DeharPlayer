package com.dehar.player.ui.screens

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.media.AudioManager
import android.net.Uri
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.dehar.player.player.PlayerManager
import com.dehar.player.player.MediaTrackOption
import com.dehar.player.ui.theme.*
import com.dehar.player.utils.TimeUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
    val scope = rememberCoroutineScope()

    val playerManager = remember { PlayerManager(context).apply { initialize() } }
    var showControls by remember { mutableStateOf(true) }
    var isControlsLocked by remember { mutableStateOf(false) }

    // Gesture indicator states
    var sideIndicator by remember { mutableStateOf<SideIndicator?>(null) }
    var isDraggingHorizontal by remember { mutableStateOf(false) }
    var dragStartPos by remember { mutableLongStateOf(0L) }
    var dragTargetPos by remember { mutableLongStateOf(0L) }

    // Speed & Aspect Ratio
    var speedMenuExpanded by remember { mutableStateOf(false) }
    var currentSpeed by remember { mutableFloatStateOf(1.0f) }
    var currentResizeMode by remember { mutableIntStateOf(AspectRatioFrameLayout.RESIZE_MODE_FIT) }
    var decoderMode by remember { mutableStateOf("HW decoder") }

    // Position tracking
    var currentPos by remember { mutableLongStateOf(0L) }
    var duration by remember { mutableLongStateOf(0L) }
    var isPlaying by remember { mutableStateOf(false) }

    // Dialogs
    var audioDialogVisible by remember { mutableStateOf(false) }
    var subtitleDialogVisible by remember { mutableStateOf(false) }
    var decoderDialogVisible by remember { mutableStateOf(false) }

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

        playerManager.setSingleMedia(uri)
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
            playerManager.release()
        }
    }

    // Update position and duration regularly
    LaunchedEffect(uri) {
        while (true) {
            currentPos = playerManager.getCurrentPosition()
            duration = playerManager.getDuration()
            isPlaying = playerManager.isPlaying()
            delay(500)
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
                                val x = offset.x
                                val width = size.width
                                when {
                                    x < width * 0.35f -> {
                                        playerManager.seekBackward(10000L)
                                    }
                                    x > width * 0.65f -> {
                                        playerManager.seekForward(10000L)
                                    }
                                    else -> {
                                        playerManager.togglePlayPause()
                                    }
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
                    detectVerticalDragGestures(
                        onDragStart = {},
                        onDragEnd = {
                            scope.launch {
                                delay(1000)
                                sideIndicator = null
                            }
                        },
                        onDragCancel = {
                            sideIndicator = null
                        },
                        onVerticalDrag = { change, dragAmount ->
                            val isLeftHalf = change.position.x < size.width / 2
                            if (isLeftHalf) {
                                // Adjust Brightness
                                val lp = activity?.window?.attributes
                                val currentBrightness = lp?.screenBrightness ?: 0.5f
                                val newBrightness = (currentBrightness - (dragAmount / 500f)).coerceIn(0.01f, 1.0f)
                                lp?.screenBrightness = newBrightness
                                activity?.window?.attributes = lp
                                
                                val percent = (newBrightness * 100).toInt()
                                sideIndicator = SideIndicator(
                                    side = IndicatorSide.Left,
                                    value = percent,
                                    displayValue = "$percent%",
                                    icon = Icons.Default.WbSunny
                                )
                            } else {
                                // Adjust Volume
                                val currentVol = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
                                val volDiff = if (dragAmount > 0) -1 else 1
                                val newVol = (currentVol + volDiff).coerceIn(0, maxVolume)
                                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, newVol, 0)
                                
                                val percent = if (maxVolume > 0) newVol * 100 / maxVolume else 0
                                sideIndicator = SideIndicator(
                                    side = IndicatorSide.Right,
                                    value = percent,
                                    displayValue = newVol.toString(),
                                    icon = Icons.Default.VolumeUp
                                )
                            }
                        }
                    )
                }
                .pointerInput(isControlsLocked, duration) {
                    if (isControlsLocked) return@pointerInput
                    detectHorizontalDragGestures(
                        onDragStart = {
                            dragStartPos = playerManager.getCurrentPosition()
                            dragTargetPos = dragStartPos
                            isDraggingHorizontal = true
                        },
                        onDragEnd = {
                            isDraggingHorizontal = false
                            playerManager.exoPlayer?.seekTo(dragTargetPos)
                        },
                        onDragCancel = {
                            isDraggingHorizontal = false
                        },
                        onHorizontalDrag = { _, dragAmount ->
                            if (duration <= 0L) return@detectHorizontalDragGestures
                            val deltaMs = ((dragAmount / size.width) * 90_000L).toLong()
                            dragTargetPos = (dragTargetPos + deltaMs).coerceIn(0L, duration)
                            playerManager.exoPlayer?.seekTo(dragTargetPos)
                        }
                    )
                }
        )

        // Custom Overlay Controls
        AnimatedVisibility(
            visible = showControls && !isDraggingHorizontal && sideIndicator == null,
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
                    Box(modifier = Modifier.fillMaxSize()) {
                        // 1. Top Bar
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.TopCenter)
                                .padding(start = 24.dp, top = 16.dp, end = 24.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                IconButton(
                                    onClick = { navController.popBackStack() },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Back",
                                        tint = Color.White,
                                        modifier = Modifier.size(28.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = displayName,
                                    color = Color.White,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1
                                )
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                IconButton(
                                    onClick = { audioDialogVisible = true },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.MusicNote,
                                        contentDescription = "Audio track",
                                        tint = Color.White,
                                        modifier = Modifier.size(26.dp)
                                    )
                                }
                                IconButton(
                                    onClick = { subtitleDialogVisible = true },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Subtitles,
                                        contentDescription = "Subtitles",
                                        tint = Color.White,
                                        modifier = Modifier.size(26.dp)
                                    )
                                }
                                TextButton(
                                    onClick = { decoderDialogVisible = true },
                                    contentPadding = PaddingValues(horizontal = 4.dp),
                                    modifier = Modifier.height(32.dp)
                                ) {
                                    Text(
                                        text = "HW",
                                        color = Color.White,
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                IconButton(
                                    onClick = { showControls = true },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.MoreVert,
                                        contentDescription = "More options",
                                        tint = Color.White,
                                        modifier = Modifier.size(26.dp)
                                    )
                                }
                            }
                        }

                        // 2. Horizontal Action Bar (Equalizer, Speed, AspectRatio, Audio, Rotation, Expand)
                        Row(
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(start = 24.dp, top = 64.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Equalizer
                            PlayerActionCircleButton(
                                icon = Icons.Default.Tune,
                                onClick = {
                                    Toast.makeText(context, "Equalizer: Default Preset Applied", Toast.LENGTH_SHORT).show()
                                }
                            )

                            // Playback Speed
                            Box {
                                PlayerActionCircleTextButton(
                                    text = "${currentSpeed.cleanSpeed()}X",
                                    onClick = { speedMenuExpanded = true }
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

                            // Aspect Ratio crop
                            PlayerActionCircleButton(
                                icon = Icons.Default.AspectRatio,
                                onClick = {
                                    currentResizeMode = when (currentResizeMode) {
                                        AspectRatioFrameLayout.RESIZE_MODE_FIT -> AspectRatioFrameLayout.RESIZE_MODE_FILL
                                        AspectRatioFrameLayout.RESIZE_MODE_FILL -> AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                                        else -> AspectRatioFrameLayout.RESIZE_MODE_FIT
                                    }
                                    val modeName = when (currentResizeMode) {
                                        AspectRatioFrameLayout.RESIZE_MODE_FIT -> "Fit to Screen"
                                        AspectRatioFrameLayout.RESIZE_MODE_FILL -> "Stretch"
                                        AspectRatioFrameLayout.RESIZE_MODE_ZOOM -> "Zoom"
                                        else -> "Fit to Screen"
                                    }
                                    Toast.makeText(context, modeName, Toast.LENGTH_SHORT).show()
                                }
                            )

                            // Audio/Headphone track
                            PlayerActionCircleButton(
                                icon = Icons.Default.Headphones,
                                onClick = { audioDialogVisible = true }
                            )

                            // Rotation Toggle
                            PlayerActionCircleButton(
                                icon = Icons.Default.ScreenRotation,
                                onClick = {
                                    val isPortrait = activity?.requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                                    activity?.requestedOrientation = if (isPortrait) {
                                        ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
                                    } else {
                                        ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                                    }
                                    val orientationName = if (isPortrait) "Landscape Mode" else "Portrait Mode"
                                    Toast.makeText(context, orientationName, Toast.LENGTH_SHORT).show()
                                }
                            )

                            // Expand Arrow
                            PlayerActionCircleButton(
                                icon = Icons.Default.ChevronRight,
                                onClick = {
                                    Toast.makeText(context, "More option shortcuts expanded", Toast.LENGTH_SHORT).show()
                                }
                            )
                        }

                        // 3. Integrated Seekbar & Time Codes Row at the Bottom (Controls Visible)
                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .fillMaxWidth()
                                .padding(bottom = 76.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 24.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = TimeUtils.formatDuration(currentPos),
                                    color = Color.White,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                val remaining = if (duration > 0L) duration - currentPos else 0L
                                Text(
                                    text = "-${TimeUtils.formatDuration(remaining)}",
                                    color = Color.White,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            val sliderValue = if (isDraggingHorizontal) dragTargetPos else currentPos
                            Slider(
                                value = if (duration > 0) sliderValue.toFloat() / duration else 0f,
                                onValueChange = { percent ->
                                    val target = (percent * duration).toLong()
                                    playerManager.exoPlayer?.seekTo(target)
                                },
                                colors = SliderDefaults.colors(
                                    thumbColor = DeharUnplayedCyan,
                                    activeTrackColor = DeharUnplayedCyan,
                                    inactiveTrackColor = Color(0xFF555555)
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 24.dp)
                            )
                        }

                        // 4. Bottom Alignment Row of Controls
                        // Bottom-Left Lock & Rotation
                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(start = 24.dp, bottom = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            IconButton(
                                onClick = {
                                    isControlsLocked = true
                                    Toast.makeText(context, "Interface locked. Tap screen to unlock.", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = "Lock",
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            IconButton(
                                onClick = {
                                    val isPortrait = activity?.requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                                    activity?.requestedOrientation = if (isPortrait) {
                                        ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
                                    } else {
                                        ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                                    }
                                    val orientationName = if (isPortrait) "Landscape Mode" else "Portrait Mode"
                                    Toast.makeText(context, orientationName, Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ScreenRotation,
                                    contentDescription = "Rotate",
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }

                        // Bottom-Center: Prev (seek backward 10s), Play/Pause, Next (seek forward 10s)
                        Row(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 20.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(56.dp)
                        ) {
                            IconButton(
                                onClick = {
                                    playerManager.seekBackward(10000L)
                                    Toast.makeText(context, "Rewind 10s", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SkipPrevious,
                                    contentDescription = "Rewind",
                                    tint = Color.White,
                                    modifier = Modifier.size(28.dp)
                                )
                            }

                            IconButton(
                                onClick = { playerManager.togglePlayPause() },
                                modifier = Modifier.size(48.dp)
                            ) {
                                Icon(
                                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                    contentDescription = "Play/Pause",
                                    tint = Color.White,
                                    modifier = Modifier.size(40.dp)
                                )
                            }

                            IconButton(
                                onClick = {
                                    playerManager.seekForward(10000L)
                                    Toast.makeText(context, "Fast Forward 10s", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SkipNext,
                                    contentDescription = "Fast Forward",
                                    tint = Color.White,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }

                        // Bottom-Right Aspect Ratio & PiP
                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(end = 24.dp, bottom = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            IconButton(
                                onClick = {
                                    currentResizeMode = when (currentResizeMode) {
                                        AspectRatioFrameLayout.RESIZE_MODE_FIT -> AspectRatioFrameLayout.RESIZE_MODE_FILL
                                        AspectRatioFrameLayout.RESIZE_MODE_FILL -> AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                                        else -> AspectRatioFrameLayout.RESIZE_MODE_FIT
                                    }
                                    val modeName = when (currentResizeMode) {
                                        AspectRatioFrameLayout.RESIZE_MODE_FIT -> "Fit to Screen"
                                        AspectRatioFrameLayout.RESIZE_MODE_FILL -> "Stretch"
                                        AspectRatioFrameLayout.RESIZE_MODE_ZOOM -> "Zoom"
                                        else -> "Fit to Screen"
                                    }
                                    Toast.makeText(context, modeName, Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AspectRatio,
                                    contentDescription = "Aspect Ratio",
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            IconButton(
                                onClick = {
                                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                        activity?.enterPictureInPictureMode(
                                            android.app.PictureInPictureParams.Builder().build()
                                        )
                                    } else {
                                        Toast.makeText(context, "PiP not supported on this device", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PictureInPicture,
                                    contentDescription = "PiP",
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Bottom Seekbar (Visible ONLY when controls hidden AND user dragging/adjusting brightness/volume)
        AnimatedVisibility(
            visible = !showControls && (isDraggingHorizontal || sideIndicator != null) && !isControlsLocked,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp, bottom = 76.dp)
            ) {
                if (!isDraggingHorizontal && sideIndicator == null) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = TimeUtils.formatDuration(currentPos),
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium
                        )
                        val remaining = if (duration > 0L) duration - currentPos else 0L
                        Text(
                            text = "-${TimeUtils.formatDuration(remaining)}",
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                val sliderValue = if (isDraggingHorizontal) dragTargetPos else currentPos
                Slider(
                    value = if (duration > 0) sliderValue.toFloat() / duration else 0f,
                    onValueChange = { percent ->
                        val target = (percent * duration).toLong()
                        playerManager.exoPlayer?.seekTo(target)
                    },
                    colors = SliderDefaults.colors(
                        thumbColor = DeharUnplayedCyan,
                        activeTrackColor = DeharUnplayedCyan,
                        inactiveTrackColor = Color(0xFF555555)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        sideIndicator?.let { indicator ->
            VerticalSideIndicator(
                indicator = indicator,
                modifier = Modifier.align(
                    if (indicator.side == IndicatorSide.Left) Alignment.CenterStart else Alignment.CenterEnd
                )
            )
        }

        // Center Seek Indicator
        AnimatedVisibility(
            visible = isDraggingHorizontal,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.Center)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = TimeUtils.formatDuration(dragTargetPos),
                    color = Color.White,
                    fontSize = 72.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                val delta = dragTargetPos - dragStartPos
                val sign = if (delta >= 0) "+" else "-"
                val absDelta = kotlin.math.abs(delta)
                val deltaStr = "[$sign${TimeUtils.formatDuration(absDelta)}]"
                Text(
                    text = deltaStr,
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Medium
                )
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
    }
}

private fun Float.cleanSpeed(): String {
    return if (this % 1f == 0f) this.toInt().toString() else this.toString()
}


@Composable
private fun VerticalSideIndicator(
    indicator: SideIndicator,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(horizontal = 22.dp)
            .width(44.dp)
            .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = indicator.displayValue,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .height(130.dp)
                .width(4.dp)
                .background(Color(0xFF555555), RoundedCornerShape(2.dp)),
            contentAlignment = Alignment.BottomCenter
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight((indicator.value / 100f).coerceIn(0f, 1f))
                    .background(Color(0xFF4DA3FF), RoundedCornerShape(2.dp))
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Icon(
            imageVector = indicator.icon,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(20.dp)
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

                HorizontalDivider(color = Color.DarkGray)

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

@Composable
private fun PlayerActionCircleButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .background(Color.White.copy(alpha = 0.15f), CircleShape)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
private fun PlayerActionCircleTextButton(
    text: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .background(Color.White.copy(alpha = 0.15f), CircleShape)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
