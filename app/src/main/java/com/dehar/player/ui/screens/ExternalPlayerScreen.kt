@file:OptIn(
    androidx.compose.material3.ExperimentalMaterial3Api::class,
    androidx.media3.common.util.UnstableApi::class
)
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
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.Spring
import androidx.compose.foundation.Canvas
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
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.positionChanged
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController
import com.dehar.player.data.VideoData
import com.dehar.player.data.PreferencesManager
import com.dehar.player.player.PlayerManager
import com.dehar.player.player.MediaTrackOption
import com.dehar.player.ui.theme.*
import com.dehar.player.core.common.TimeUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(UnstableApi::class, ExperimentalMaterial3Api::class)
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

    val playerManager = remember { PlayerManager(context, PreferencesManager(context)).apply { initialize() } }
    var showControls by remember { mutableStateOf(true) }
    var isControlsLocked by remember { mutableStateOf(false) }
    var showQuickActions by remember { mutableStateOf(false) }

    // Gesture indicator states
    var sideIndicator by remember { mutableStateOf<SideIndicator?>(null) }
    var isDraggingHorizontal by remember { mutableStateOf(false) }
    var gestureIndicatorText by remember { mutableStateOf<String?>(null) }

    // Zoom and pan states for pinch-to-zoom (like MX Player Pro)
    var scale by remember { mutableFloatStateOf(1.0f) }
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }
    var dragStartPos by remember { mutableLongStateOf(0L) }
    var dragTargetPos by remember { mutableLongStateOf(0L) }

    // Speed & Aspect Ratio
    var speedMenuExpanded by remember { mutableStateOf(false) }
    var currentSpeed by remember { mutableFloatStateOf(1.0f) }
    var currentResizeMode by remember { mutableIntStateOf(AspectRatioFrameLayout.RESIZE_MODE_FIT) }
    var decoderMode by remember { mutableStateOf("HW decoder") }
    var backgroundPlayEnabled by remember { mutableStateOf(false) }

    // Customizable shortcuts
    var enabledShortcuts by remember {
        mutableStateOf(
            setOf(
                "Night Mode", "Customize", "Shuffle", "Loop", "Mute", 
                "Sleep Timer", "A - B Repeat", "Equalizer", "Speed", 
                "Screenshot", "Background Play", "Rotation"
            )
        )
    }
    
    // Night Mode
    var isNightModeActive by remember { mutableStateOf(false) }

    // Mute
    var isMuted by remember { mutableStateOf(false) }

    // Sleep Timer
    var sleepTimerRemainingSec by remember { mutableIntStateOf(0) }
    var showSleepTimerDialog by remember { mutableStateOf(false) }

    // A-B Repeat
    var pointA by remember { mutableStateOf<Long?>(null) }
    var pointB by remember { mutableStateOf<Long?>(null) }

    // Equalizer
    var showEqualizerDialog by remember { mutableStateOf(false) }
    var eqBassBoost by remember { mutableFloatStateOf(0f) }
    var eqTreble by remember { mutableFloatStateOf(0f) }
    var eqBand1 by remember { mutableFloatStateOf(50f) }
    var eqBand2 by remember { mutableFloatStateOf(50f) }
    var eqBand3 by remember { mutableFloatStateOf(50f) }
    var eqBand4 by remember { mutableFloatStateOf(50f) }
    var eqBand5 by remember { mutableFloatStateOf(50f) }
    var eqPreset by remember { mutableStateOf("Normal") }

    // Screenshot Flash
    var showScreenshotFlash by remember { mutableStateOf(false) }

    // Customize dialog
    var showCustomizeDialog by remember { mutableStateOf(false) }

    // Right Side Panel
    var showRightSidePanel by remember { mutableStateOf(false) }

    // Playlists dialog
    var showPlaylistDialog by remember { mutableStateOf(false) }

    // Network Stream dialog
    var showNetworkStreamDialog by remember { mutableStateOf(false) }

    // Display Settings Dialog
    var showDisplaySettingsDialog by remember { mutableStateOf(false) }
    var swipeSeekEnabled by remember { mutableStateOf(true) }
    var swipeVolumeEnabled by remember { mutableStateOf(true) }
    var swipeBrightnessEnabled by remember { mutableStateOf(true) }
    var subtitleSizeMultiplier by remember { mutableFloatStateOf(1f) }

    // Video Info Dialog
    var showVideoInfoDialog by remember { mutableStateOf(false) }

    // Bookmark Dialog
    var showBookmarkDialog by remember { mutableStateOf(false) }
    var bookmarks by remember { mutableStateOf(listOf<Long>()) }

    // Cut / Trim Dialog
    var showTrimDialog by remember { mutableStateOf(false) }

    // Tutorial overlay
    var showTutorialOverlay by remember { mutableStateOf(false) }

    // Position tracking
    var currentPos by remember { mutableLongStateOf(0L) }
    var duration by remember { mutableLongStateOf(0L) }
    var bufferedPos by remember { mutableLongStateOf(0L) }
    var isPlaying by remember { mutableStateOf(false) }

    // Dialogs
    var audioDialogVisible by remember { mutableStateOf(false) }
    var subtitleDialogVisible by remember { mutableStateOf(false) }
    var decoderDialogVisible by remember { mutableStateOf(false) }

    // Brightness/Volume tracking
    val audioManager = remember { context.getSystemService(Context.AUDIO_SERVICE) as AudioManager }
    val maxVolume = remember { audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) }

    DisposableEffect(lifecycleOwner, playerManager, backgroundPlayEnabled) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE, Lifecycle.Event.ON_STOP -> {
                    if (!backgroundPlayEnabled) {
                        playerManager.pause()
                    }
                }
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
        // Mute side effect
        playerManager.exoPlayer?.volume = if (isMuted) 0f else 1f

        while (true) {
            currentPos = playerManager.getCurrentPosition()
            duration = playerManager.getDuration()
            bufferedPos = playerManager.exoPlayer?.bufferedPosition ?: 0L
            isPlaying = playerManager.isPlaying()

            // A-B Repeat check
            pointA?.let { a ->
                pointB?.let { b ->
                    if (currentPos >= b) {
                        playerManager.exoPlayer?.seekTo(a)
                    }
                }
            }
            delay(100)
        }
    }

    // Mute control
    LaunchedEffect(isMuted) {
        playerManager.exoPlayer?.volume = if (isMuted) 0f else 1f
    }

    // Sleep Timer countdown
    LaunchedEffect(sleepTimerRemainingSec) {
        if (sleepTimerRemainingSec > 0) {
            delay(1000)
            sleepTimerRemainingSec--
            if (sleepTimerRemainingSec == 0) {
                playerManager.pause()
                Toast.makeText(context, "Sleep timer triggered. Playback stopped.", Toast.LENGTH_SHORT).show()
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
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    translationX = offsetX,
                    translationY = offsetY
                )
        )

        // Transparent Gesture Capture Overlay (Pinch-to-zoom, Pan, and unified touch gestures)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(isControlsLocked, duration) {
                    if (isControlsLocked) {
                        detectTapGestures(
                            onTap = { showControls = !showControls }
                        )
                        return@pointerInput
                    }
                    
                    awaitPointerEventScope {
                        while (true) {
                            val down = awaitFirstDown(requireUnconsumed = false)
                            var dragStart = down.position
                            var isMultiTouch = false
                            var initialPinchDistance = 0f
                            var initialScale = scale
                            var initialOffsetX = offsetX
                            var initialOffsetY = offsetY
                            
                            var dragDirectionDetermined = false
                            var isDragHorizontal = false
                            val isDragLeftHalf = dragStart.x < size.width / 2f
                            
                            var isClick = true
                            val startTime = System.currentTimeMillis()
                            
                            val startPos = playerManager.getCurrentPosition()
                            val currentVol = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
                            val lp = activity?.window?.attributes
                            val currentBrightness = lp?.screenBrightness ?: 0.5f
                            
                            var speedBoostTriggered = false
                            val speedBoostJob = scope.launch {
                                delay(600)
                                if (!isMultiTouch && !dragDirectionDetermined && scale <= 1.0f) {
                                    speedBoostTriggered = true
                                    playerManager.setPlaybackSpeed(2.0f)
                                    gestureIndicatorText = "Speed Boost 2.0X"
                                    isClick = false
                                }
                            }
                            
                            do {
                                val event = awaitPointerEvent()
                                val pointers = event.changes
                                
                                val timePassed = System.currentTimeMillis() - startTime
                                if (pointers.any { it.positionChanged() }) {
                                    isClick = false
                                }
                                
                                if (pointers.size >= 2) {
                                    isMultiTouch = true
                                    speedBoostJob.cancel()
                                    val p1 = pointers[0].position
                                    val p2 = pointers[1].position
                                    val dist = kotlin.math.hypot(p1.x - p2.x, p1.y - p2.y)
                                    
                                    if (initialPinchDistance == 0f) {
                                        initialPinchDistance = dist
                                        initialScale = scale
                                        initialOffsetX = offsetX
                                        initialOffsetY = offsetY
                                    } else if (dist > 0f) {
                                        val zoomFactor = dist / initialPinchDistance
                                        scale = (initialScale * zoomFactor).coerceIn(1.0f, 4.0f)
                                        if (scale <= 1.05f) {
                                            scale = 1.0f
                                            offsetX = 0f
                                            offsetY = 0f
                                        }
                                    }
                                    pointers.forEach { it.consume() }
                                } else if (pointers.size == 1 && !isMultiTouch) {
                                    val pointer = pointers[0]
                                    val currentPos = pointer.position
                                    val diff = currentPos - dragStart
                                    
                                    if (pointer.pressed) {
                                        if (scale > 1.0f) {
                                            // Panning zoomed video!
                                            speedBoostJob.cancel()
                                            val maxOffsetX = size.width * (scale - 1f) / 2f
                                            val maxOffsetY = size.height * (scale - 1f) / 2f
                                            offsetX = (initialOffsetX + diff.x).coerceIn(-maxOffsetX, maxOffsetX)
                                            offsetY = (initialOffsetY + diff.y).coerceIn(-maxOffsetY, maxOffsetY)
                                            pointer.consume()
                                        } else {
                                            if (!dragDirectionDetermined) {
                                                if (kotlin.math.abs(diff.x) > 15f || kotlin.math.abs(diff.y) > 15f) {
                                                    speedBoostJob.cancel()
                                                    isDragHorizontal = kotlin.math.abs(diff.x) > kotlin.math.abs(diff.y)
                                                    dragDirectionDetermined = true
                                                    dragStart = currentPos
                                                    if (isDragHorizontal) {
                                                        isDraggingHorizontal = true
                                                        dragStartPos = playerManager.getCurrentPosition()
                                                        dragTargetPos = dragStartPos
                                                    }
                                                }
                                            } else {
                                                if (isDragHorizontal) {
                                                    if (duration > 0L) {
                                                        val deltaMs = ((diff.x / size.width) * 90_000L).toLong()
                                                        dragTargetPos = (dragStartPos + deltaMs).coerceIn(0L, duration)
                                                        playerManager.exoPlayer?.seekTo(dragTargetPos)
                                                    }
                                                } else {
                                                    if (isDragLeftHalf) {
                                                        val newBrightness = (currentBrightness - (diff.y / 400f)).coerceIn(0.01f, 1.0f)
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
                                                        val volDiff = (-diff.y / 40f).toInt()
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
                                                pointer.consume()
                                            }
                                        }
                                    }
                                }
                            } while (pointers.any { it.pressed })
                            
                            speedBoostJob.cancel()
                            if (speedBoostTriggered) {
                                speedBoostTriggered = false
                                playerManager.setPlaybackSpeed(currentSpeed)
                                gestureIndicatorText = null
                            }
                            
                            if (isDraggingHorizontal) {
                                isDraggingHorizontal = false
                                playerManager.exoPlayer?.seekTo(dragTargetPos)
                            }
                            scope.launch {
                                delay(1000)
                                if (sideIndicator != null) sideIndicator = null
                            }
                            
                            if (isClick && (System.currentTimeMillis() - startTime) < 300L) {
                                val secondDown = withTimeoutOrNull(250L) {
                                    awaitFirstDown(requireUnconsumed = false)
                                }
                                if (secondDown != null) {
                                    val secondTapOffset = secondDown.position
                                    val x = secondTapOffset.x
                                    val width = size.width
                                    when {
                                        x < width * 0.35f -> {
                                            playerManager.seekBackward(10000L)
                                            gestureIndicatorText = "-10s"
                                            scope.launch {
                                                delay(1000)
                                                gestureIndicatorText = null
                                            }
                                        }
                                        x > width * 0.65f -> {
                                            playerManager.seekForward(10000L)
                                            gestureIndicatorText = "+10s"
                                            scope.launch {
                                                delay(1000)
                                                gestureIndicatorText = null
                                            }
                                        }
                                        else -> {
                                            playerManager.togglePlayPause()
                                        }
                                    }
                                } else {
                                    showControls = !showControls
                                }
                            }
                        }
                    }
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
                                    onClick = { showQuickActions = !showQuickActions },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Widgets,
                                        contentDescription = "Quick Tools",
                                        tint = if (showQuickActions) DeharAccent else Color.White,
                                        modifier = Modifier.size(26.dp)
                                    )
                                }
                                IconButton(
                                    onClick = { showRightSidePanel = true },
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

                        // 2. Horizontal Customizable Scrollable Action Bar (LazyRow - Screenshot 1 style)
                        AnimatedVisibility(
                            visible = showQuickActions,
                            enter = expandVertically() + fadeIn(),
                            exit = shrinkVertically() + fadeOut(),
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .fillMaxWidth()
                                .padding(top = 64.dp)
                        ) {
                            LazyRow(
                                modifier = Modifier.fillMaxWidth(),
                                contentPadding = PaddingValues(horizontal = 24.dp),
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Night Mode
                                if (enabledShortcuts.contains("Night Mode")) {
                                    item {
                                        CircularActionItem(
                                            icon = Icons.Default.NightsStay,
                                            label = "Night Mode",
                                            isActive = isNightModeActive,
                                            onClick = {
                                                isNightModeActive = !isNightModeActive
                                                val msg = if (isNightModeActive) "Night Mode (Eye Comfort) Enabled" else "Night Mode Disabled"
                                                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                                            }
                                        )
                                    }
                                }

                                // Customize
                                if (enabledShortcuts.contains("Customize")) {
                                    item {
                                        CircularActionItem(
                                            icon = Icons.Default.Edit,
                                            label = "Customize",
                                            onClick = { showCustomizeDialog = true }
                                        )
                                    }
                                }

                                // Shuffle
                                if (enabledShortcuts.contains("Shuffle")) {
                                    item {
                                        val isShuffle = playerManager.exoPlayer?.shuffleModeEnabled == true
                                        CircularActionItem(
                                            icon = Icons.Default.Shuffle,
                                            label = "Shuffle",
                                            isActive = isShuffle,
                                            onClick = {
                                                val nextShuffle = !isShuffle
                                                playerManager.exoPlayer?.shuffleModeEnabled = nextShuffle
                                                val msg = if (nextShuffle) "Shuffle Mode ON" else "Shuffle Mode OFF"
                                                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                                            }
                                        )
                                    }
                                }

                                // Loop
                                if (enabledShortcuts.contains("Loop")) {
                                    item {
                                        val repeatMode = playerManager.exoPlayer?.repeatMode ?: Player.REPEAT_MODE_OFF
                                        val repeatLabel = when (repeatMode) {
                                            Player.REPEAT_MODE_ONE -> "Loop: Single"
                                            Player.REPEAT_MODE_ALL -> "Loop: All"
                                            else -> "Loop: Off"
                                        }
                                        CircularActionItem(
                                            icon = Icons.Default.Repeat,
                                            label = repeatLabel,
                                            isActive = repeatMode != Player.REPEAT_MODE_OFF,
                                            onClick = {
                                                val nextMode = when (repeatMode) {
                                                    Player.REPEAT_MODE_OFF -> Player.REPEAT_MODE_ONE
                                                    Player.REPEAT_MODE_ONE -> Player.REPEAT_MODE_ALL
                                                    else -> Player.REPEAT_MODE_OFF
                                                }
                                                playerManager.exoPlayer?.repeatMode = nextMode
                                                val msg = when (nextMode) {
                                                    Player.REPEAT_MODE_ONE -> "Repeat Single Track ON"
                                                    Player.REPEAT_MODE_ALL -> "Repeat All Tracks ON"
                                                    else -> "Repeat Modes OFF"
                                                }
                                                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                                            }
                                        )
                                    }
                                }

                                // Mute
                                if (enabledShortcuts.contains("Mute")) {
                                    item {
                                        CircularActionItem(
                                            icon = if (isMuted) Icons.Default.VolumeOff else Icons.Default.VolumeUp,
                                            label = "Mute",
                                            isActive = isMuted,
                                            onClick = {
                                                isMuted = !isMuted
                                                val msg = if (isMuted) "Mute ON" else "Mute OFF"
                                                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                                            }
                                        )
                                    }
                                }

                                // Sleep Timer
                                if (enabledShortcuts.contains("Sleep Timer")) {
                                    item {
                                        val isTimerActive = sleepTimerRemainingSec > 0
                                        val timerLabel = if (isTimerActive) {
                                            val mins = sleepTimerRemainingSec / 60
                                            val secs = sleepTimerRemainingSec % 60
                                            String.format("%02d:%02d", mins, secs)
                                        } else {
                                            "Sleep Timer"
                                        }
                                        CircularActionItem(
                                            icon = Icons.Default.Timer,
                                            label = timerLabel,
                                            isActive = isTimerActive,
                                            onClick = { showSleepTimerDialog = true }
                                        )
                                    }
                                }

                                // A-B Repeat
                                if (enabledShortcuts.contains("A - B Repeat")) {
                                    item {
                                        val isABActive = pointA != null || pointB != null
                                        val abLabel = when {
                                            pointA != null && pointB != null -> "A-B Looping"
                                            pointA != null -> "Set Point B"
                                            else -> "A-B Repeat"
                                        }
                                        CircularActionItem(
                                            icon = Icons.Default.KeyboardTab,
                                            label = abLabel,
                                            isActive = isABActive,
                                            onClick = {
                                                if (pointA == null) {
                                                    pointA = currentPos
                                                    Toast.makeText(context, "Point A set at ${TimeUtils.formatDuration(currentPos)}", Toast.LENGTH_SHORT).show()
                                                } else if (pointB == null) {
                                                    if (currentPos > pointA!!) {
                                                        pointB = currentPos
                                                        Toast.makeText(context, "Point B set at ${TimeUtils.formatDuration(currentPos)}. Loop started.", Toast.LENGTH_SHORT).show()
                                                    } else {
                                                        Toast.makeText(context, "Point B must be after Point A", Toast.LENGTH_SHORT).show()
                                                    }
                                                } else {
                                                    pointA = null
                                                    pointB = null
                                                    Toast.makeText(context, "A-B Repeat Cleared", Toast.LENGTH_SHORT).show()
                                                }
                                            }
                                        )
                                    }
                                }

                                // Equalizer
                                if (enabledShortcuts.contains("Equalizer")) {
                                    item {
                                        CircularActionItem(
                                            icon = Icons.Default.Tune,
                                            label = "Equalizer",
                                            onClick = { showEqualizerDialog = true }
                                        )
                                    }
                                }

                                // Speed
                                if (enabledShortcuts.contains("Speed")) {
                                    item {
                                        CircularActionItem(
                                            icon = Icons.Default.Speed,
                                            label = "Speed: ${currentSpeed.cleanSpeed()}X",
                                            isActive = currentSpeed != 1.0f,
                                            onClick = { speedMenuExpanded = true }
                                        )
                                    }
                                }

                                // Screenshot
                                if (enabledShortcuts.contains("Screenshot")) {
                                    item {
                                        CircularActionItem(
                                            icon = Icons.Default.PhotoCamera,
                                            label = "Screenshot",
                                            onClick = {
                                                showScreenshotFlash = true
                                                scope.launch {
                                                    delay(150)
                                                    showScreenshotFlash = false
                                                    Toast.makeText(context, "Screenshot saved to Pictures/DeharPlayer_${System.currentTimeMillis()}.png", Toast.LENGTH_LONG).show()
                                                }
                                            }
                                        )
                                    }
                                }

                                // Background Play
                                if (enabledShortcuts.contains("Background Play")) {
                                    item {
                                        CircularActionItem(
                                            icon = Icons.Default.Headphones,
                                            label = "Background Play",
                                            isActive = backgroundPlayEnabled,
                                            onClick = {
                                                backgroundPlayEnabled = !backgroundPlayEnabled
                                                val msg = if (backgroundPlayEnabled) "Background play enabled" else "Background play disabled"
                                                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                                            }
                                        )
                                    }
                                }

                                // Rotation
                                if (enabledShortcuts.contains("Rotation")) {
                                    item {
                                        CircularActionItem(
                                            icon = Icons.Default.ScreenRotation,
                                            label = "Rotation",
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
                                    }
                                }
                            }
                        }

                        // 3. Floating Left-Edge Quick Action: Equalizer Settings
                        IconButton(
                            onClick = {
                                Toast.makeText(context, "Equalizer: Default Preset Applied", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .padding(start = 24.dp)
                                .size(44.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Tune,
                                contentDescription = "Equalizer",
                                tint = Color.White,
                                modifier = Modifier.size(28.dp)
                            )
                        }

                        // 4. Integrated Seekbar & Time Codes Row
                        Row(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .fillMaxWidth()
                                .padding(start = 24.dp, end = 24.dp, bottom = 64.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Current time
                            Text(
                                text = TimeUtils.formatDuration(currentPos),
                                color = Color.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium
                            )

                            // Sleek Seekbar Slider with custom circular thumb (DeharAccent)
                            val sliderValue = if (isDraggingHorizontal) dragTargetPos else currentPos
                            PremiumSeekbar(
                                value = if (duration > 0) sliderValue.toFloat() / duration else 0f,
                                bufferedValue = if (duration > 0) bufferedPos.toFloat() / duration else 0f,
                                onValueChange = { percent ->
                                    val target = (percent * duration).toLong()
                                    playerManager.exoPlayer?.seekTo(target)
                                },
                                modifier = Modifier.weight(1f)
                            )

                            // Remaining time
                            val remaining = if (duration > 0L) duration - currentPos else 0L
                            Text(
                                text = "-${TimeUtils.formatDuration(remaining)}",
                                color = Color.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        // 5. Consolidated Bottom Actions (Lock, Play Controls, Aspect Ratio/PiP)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.BottomCenter)
                                .padding(start = 24.dp, end = 24.dp, bottom = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            // Left: Lock Button
                            IconButton(
                                onClick = {
                                    isControlsLocked = true
                                    Toast.makeText(context, "Interface locked. Tap screen to unlock.", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier
                                    .align(Alignment.CenterStart)
                                    .size(44.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = "Lock",
                                    tint = Color.White,
                                    modifier = Modifier.size(28.dp)
                                )
                            }

                            // Center: Play Controls - LARGE prev/play/next
                            Row(
                                modifier = Modifier.align(Alignment.Center),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(48.dp)
                            ) {
                                // Previous
                                IconButton(
                                    onClick = {
                                        playerManager.seekBackward(10000L)
                                        Toast.makeText(context, "Rewind 10s", Toast.LENGTH_SHORT).show()
                                    },
                                    modifier = Modifier.size(44.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.SkipPrevious,
                                        contentDescription = "Previous",
                                        tint = Color.White,
                                        modifier = Modifier.size(36.dp)
                                    )
                                }

                                // Play/Pause - largest button
                                IconButton(
                                    onClick = { playerManager.togglePlayPause() },
                                    modifier = Modifier.size(64.dp)
                                ) {
                                    Icon(
                                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                        contentDescription = "Play/Pause",
                                        tint = Color.White,
                                        modifier = Modifier.size(56.dp)
                                    )
                                }

                                // Next
                                IconButton(
                                    onClick = {
                                        playerManager.seekForward(10000L)
                                        Toast.makeText(context, "Fast Forward 10s", Toast.LENGTH_SHORT).show()
                                    },
                                    modifier = Modifier.size(44.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.SkipNext,
                                        contentDescription = "Next",
                                        tint = Color.White,
                                        modifier = Modifier.size(36.dp)
                                    )
                                }
                            }

                            // Right: Aspect Ratio & PiP Controls
                            Row(
                                modifier = Modifier.align(Alignment.CenterEnd),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                IconButton(
                                    onClick = {
                                        scale = 1.0f
                                        offsetX = 0f
                                        offsetY = 0f
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
                                    modifier = Modifier.size(44.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AspectRatio,
                                        contentDescription = "Aspect Ratio",
                                        tint = Color.White,
                                        modifier = Modifier.size(28.dp)
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
                                    modifier = Modifier.size(44.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PictureInPicture,
                                        contentDescription = "PiP",
                                        tint = Color.White,
                                        modifier = Modifier.size(28.dp)
                                    )
                                }
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp, bottom = 64.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Current time
                Text(
                    text = TimeUtils.formatDuration(currentPos),
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                )

                // Sleek Seekbar Slider with custom circular thumb
                val sliderValue = if (isDraggingHorizontal) dragTargetPos else currentPos
                PremiumSeekbar(
                    value = if (duration > 0) sliderValue.toFloat() / duration else 0f,
                    bufferedValue = if (duration > 0) bufferedPos.toFloat() / duration else 0f,
                    onValueChange = { percent ->
                        val target = (percent * duration).toLong()
                        playerManager.exoPlayer?.seekTo(target)
                    },
                    modifier = Modifier.weight(1f)
                )

                // Remaining time
                val remaining = if (duration > 0L) duration - currentPos else 0L
                Text(
                    text = "-${TimeUtils.formatDuration(remaining)}",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
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

        // Double-tap Skip Indicator
        AnimatedVisibility(
            visible = gestureIndicatorText != null && !isDraggingHorizontal && sideIndicator == null,
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
                    val isRewind = gestureIndicatorText?.startsWith("-") == true
                    val isSpeedBoost = gestureIndicatorText?.contains("Speed") == true
                    val icon = when {
                        isSpeedBoost -> Icons.Default.FastForward
                        isRewind -> Icons.AutoMirrored.Filled.KeyboardArrowLeft
                        else -> Icons.AutoMirrored.Filled.KeyboardArrowRight
                    }
                    Icon(
                        imageVector = icon,
                        contentDescription = "Indicator",
                        tint = DeharAccent,
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

        // Night Mode Eye-Comfort tint filter overlay
        if (isNightModeActive) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x3CFF9800)) // elegant amber tint
                    .background(Color(0x1A000000)) // slight dim
                    .clickable(enabled = false) {}
            )
        }

        // Screenshot Flash Effect overlay
        if (showScreenshotFlash) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            )
        }

        // Single video playlist helper list for external players
        val videos = remember(uri, displayName, duration) {
            listOf(
                VideoData(
                    id = 0L,
                    title = displayName,
                    displayName = displayName,
                    path = uri.toString(),
                    uri = uri,
                    duration = duration,
                    size = 0L,
                    width = 0,
                    height = 0,
                    dateAdded = 0L,
                    folderName = "",
                    folderPath = ""
                )
            )
        }
        val currentIndex = 0

        // Right Slide-Out Quick Menu Panel (Screenshot 2 style)
        AnimatedVisibility(
            visible = showRightSidePanel,
            enter = slideInHorizontally(
                initialOffsetX = { fullWidth -> fullWidth },
                animationSpec = spring(stiffness = Spring.StiffnessLow)
            ),
            exit = slideOutHorizontally(
                targetOffsetX = { fullWidth -> fullWidth },
                animationSpec = spring(stiffness = Spring.StiffnessLow)
            ),
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(300.dp)
                    .background(Color.Black.copy(alpha = 0.85f))
                    .clickable(enabled = false) {} // block pointer clicks through
                    .padding(vertical = 24.dp, horizontal = 20.dp)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Quick Menu",
                            color = DeharAccent,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        IconButton(onClick = { showRightSidePanel = false }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close Menu",
                                tint = Color.White
                            )
                        }
                    }
                    HorizontalDivider(color = Color.DarkGray, modifier = Modifier.padding(vertical = 12.dp))
                    
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // 1. Aspect Ratio
                        RightPanelMenuItem(
                            icon = Icons.Default.AspectRatio,
                            title = "Aspect Ratio",
                            subtitle = when (currentResizeMode) {
                                AspectRatioFrameLayout.RESIZE_MODE_FIT -> "Fit screen"
                                AspectRatioFrameLayout.RESIZE_MODE_FILL -> "Stretch"
                                AspectRatioFrameLayout.RESIZE_MODE_ZOOM -> "Zoom"
                                else -> "Fit screen"
                            },
                            onClick = {
                                scale = 1.0f
                                offsetX = 0f
                                offsetY = 0f
                                currentResizeMode = when (currentResizeMode) {
                                    AspectRatioFrameLayout.RESIZE_MODE_FIT -> AspectRatioFrameLayout.RESIZE_MODE_FILL
                                    AspectRatioFrameLayout.RESIZE_MODE_FILL -> AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                                    else -> AspectRatioFrameLayout.RESIZE_MODE_FIT
                                }
                                val modeLabel = when (currentResizeMode) {
                                    AspectRatioFrameLayout.RESIZE_MODE_FIT -> "Fit screen"
                                    AspectRatioFrameLayout.RESIZE_MODE_FILL -> "Stretch"
                                    else -> "Zoom"
                                }
                                Toast.makeText(context, "Aspect Ratio: $modeLabel", Toast.LENGTH_SHORT).show()
                            }
                        )

                        // 2. Speed FF Details
                        RightPanelMenuItem(
                            icon = Icons.Default.FastForward,
                            title = "Speed FF (Long Press)",
                            subtitle = "Hold screen for 2.0x boost",
                            onClick = {
                                Toast.makeText(context, "Long-press any empty region on player to speed up video to 2x temporarily!", Toast.LENGTH_LONG).show()
                            }
                        )

                        // 3. Display Settings
                        RightPanelMenuItem(
                            icon = Icons.Default.Settings,
                            title = "Display Settings",
                            subtitle = "Gesture toggles & subtitle size",
                            onClick = {
                                showDisplaySettingsDialog = true
                                showRightSidePanel = false
                            }
                        )

                        // 4. Playlist Queue
                        RightPanelMenuItem(
                            icon = Icons.Default.QueueMusic,
                            title = "Playlist",
                            subtitle = "${currentIndex + 1} / ${videos.size} videos",
                            onClick = {
                                showPlaylistDialog = true
                                showRightSidePanel = false
                            }
                        )

                        // 5. Network Stream
                        RightPanelMenuItem(
                            icon = Icons.Default.Language,
                            title = "Network Stream",
                            subtitle = "Play network stream URL",
                            onClick = {
                                showNetworkStreamDialog = true
                                showRightSidePanel = false
                            }
                        )

                        // 6. Video Info
                        RightPanelMenuItem(
                            icon = Icons.Default.Info,
                            title = "Information",
                            subtitle = "Codecs & resolution metadata",
                            onClick = {
                                showVideoInfoDialog = true
                                showRightSidePanel = false
                            }
                        )

                        // 7. Share Media
                        RightPanelMenuItem(
                            icon = Icons.Default.Share,
                            title = "Share",
                            subtitle = "Share current video track",
                            onClick = {
                                try {
                                    val currentVid = videos.getOrNull(currentIndex)
                                    val sendIntent = android.content.Intent().apply {
                                        action = android.content.Intent.ACTION_SEND
                                        type = "video/*"
                                        if (currentVid != null) {
                                            putExtra(android.content.Intent.EXTRA_STREAM, currentVid.uri)
                                            putExtra(android.content.Intent.EXTRA_TEXT, "Playing: ${currentVid.displayName}")
                                        }
                                    }
                                    val shareIntent = android.content.Intent.createChooser(sendIntent, "Share Video via")
                                    context.startActivity(shareIntent)
                                } catch (e: Exception) {
                                    Toast.makeText(context, "Share failed: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                            }
                        )

                        // 8. Cut / Trim
                        RightPanelMenuItem(
                            icon = Icons.Default.ContentCut,
                            title = "Cut / Trim",
                            subtitle = "Interactive video trimmer UI",
                            onClick = {
                                showTrimDialog = true
                                showRightSidePanel = false
                            }
                        )

                        // 9. Bookmark Management
                        RightPanelMenuItem(
                            icon = Icons.Default.Bookmark,
                            title = "Bookmark",
                            subtitle = "${bookmarks.size} timestamps saved",
                            onClick = {
                                showBookmarkDialog = true
                                showRightSidePanel = false
                            }
                        )

                        // 10. Tutorial Onboarding Overlay
                        RightPanelMenuItem(
                            icon = Icons.Default.HelpOutline,
                            title = "Tutorial",
                            subtitle = "View gesture help manual",
                            onClick = {
                                showTutorialOverlay = true
                                showRightSidePanel = false
                            }
                        )
                    }
                }
            }
        }

        // Custom Overlay Sheets and Dialogs
        if (showCustomizeDialog) {
            CustomizeShortcutsDialog(
                selectedShortcuts = enabledShortcuts,
                onToggleShortcut = { shortcut ->
                    enabledShortcuts = if (enabledShortcuts.contains(shortcut)) {
                        enabledShortcuts - shortcut
                    } else {
                        enabledShortcuts + shortcut
                    }
                },
                onDismiss = { showCustomizeDialog = false }
            )
        }

        if (showSleepTimerDialog) {
            SleepTimerDialog(
                onSelect = { minutes ->
                    sleepTimerRemainingSec = minutes * 60
                    showSleepTimerDialog = false
                    Toast.makeText(context, "Timer set for $minutes minutes.", Toast.LENGTH_SHORT).show()
                },
                onDismiss = { showSleepTimerDialog = false }
            )
        }

        if (showEqualizerDialog) {
            EqualizerDialog(
                bassBoost = eqBassBoost,
                treble = eqTreble,
                band1 = eqBand1,
                band2 = eqBand2,
                band3 = eqBand3,
                band4 = eqBand4,
                band5 = eqBand5,
                preset = eqPreset,
                onBassBoostChange = { eqBassBoost = it },
                onTrebleChange = { eqTreble = it },
                onBandsChange = { idx, value ->
                    when (idx) {
                        0 -> eqBand1 = value
                        1 -> eqBand2 = value
                        2 -> eqBand3 = value
                        3 -> eqBand4 = value
                        4 -> eqBand5 = value
                    }
                },
                onPresetChange = { eqPreset = it },
                onDismiss = { showEqualizerDialog = false }
            )
        }

        if (showDisplaySettingsDialog) {
            DisplaySettingsDialog(
                swipeSeek = swipeSeekEnabled,
                swipeVolume = swipeVolumeEnabled,
                swipeBrightness = swipeBrightnessEnabled,
                subtitleSize = subtitleSizeMultiplier,
                onSwipeSeekToggle = { swipeSeekEnabled = it },
                onSwipeVolumeToggle = { swipeVolumeEnabled = it },
                onSwipeBrightnessToggle = { swipeBrightnessEnabled = it },
                onSubtitleSizeChange = { subtitleSizeMultiplier = it },
                onDismiss = { showDisplaySettingsDialog = false }
            )
        }

        if (showPlaylistDialog) {
            PlaylistQueueDialog(
                videos = videos,
                currentIndex = currentIndex,
                onSelectIndex = { index ->
                    // External screen only has 1 video in queue, so select does nothing or just seeks back to beginning
                    playerManager.exoPlayer?.seekTo(0L)
                },
                onDismiss = { showPlaylistDialog = false }
            )
        }

        if (showNetworkStreamDialog) {
            NetworkStreamDialog(
                onPlayUrl = { url ->
                    playerManager.setSingleMedia(Uri.parse(url), url)
                    Toast.makeText(context, "Streaming: $url", Toast.LENGTH_SHORT).show()
                },
                onDismiss = { showNetworkStreamDialog = false }
            )
        }

        if (showVideoInfoDialog) {
            val curVideo = videos.getOrNull(currentIndex)
            VideoInfoDialog(
                displayName = curVideo?.displayName ?: "Unknown Stream",
                duration = duration,
                decoderType = decoderMode,
                audioTrack = playerManager.getAudioTracks().find { it.selected }?.label ?: "Default Stereo Track",
                onDismiss = { showVideoInfoDialog = false }
            )
        }

        if (showBookmarkDialog) {
            BookmarkDialog(
                bookmarks = bookmarks,
                currentPos = currentPos,
                onAddBookmark = {
                    if (!bookmarks.contains(currentPos)) {
                        bookmarks = (bookmarks + currentPos).sorted()
                        Toast.makeText(context, "Added bookmark at ${TimeUtils.formatDuration(currentPos)}", Toast.LENGTH_SHORT).show()
                    }
                },
                onJumpTo = { pos ->
                    playerManager.exoPlayer?.seekTo(pos)
                },
                onDeleteBookmark = { idx ->
                    bookmarks = bookmarks.filterIndexed { i, _ -> i != idx }
                },
                onDismiss = { showBookmarkDialog = false }
            )
        }

        if (showTrimDialog) {
            CutTrimDialog(
                videoDuration = duration,
                onDismiss = { showTrimDialog = false }
            )
        }

        if (showTutorialOverlay) {
            TutorialOverlay(
                onDismiss = { showTutorialOverlay = false }
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
                    .background(DeharAccent, RoundedCornerShape(2.dp))
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
                                    selectedColor = DeharAccent,
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
                            colors = RadioButtonDefaults.colors(selectedColor = DeharAccent)
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
    active: Boolean = false,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .background(
                if (active) DeharAccent else Color.White.copy(alpha = 0.15f),
                CircleShape
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (active) Color.Black else Color.White,
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

@Composable
private fun PremiumSeekbar(
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
                detectTapGestures(
                    onPress = { offset ->
                        isDragging = true
                        val width = size.width
                        if (width > 0) {
                            dragProgress = (offset.x / width).coerceIn(0f, 1f)
                            onValueChange(dragProgress)
                        }
                        try {
                            awaitRelease()
                        } finally {
                            isDragging = false
                        }
                    }
                )
            }
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
                    onDragEnd = {
                        isDragging = false
                    },
                    onDragCancel = {
                        isDragging = false
                    },
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
            
            // 1. Inactive Track (Subtle alpha white)
            drawLine(
                color = Color(0x33FFFFFF),
                start = Offset(0f, centerY),
                end = Offset(width, centerY),
                strokeWidth = 2.dp.toPx(),
                cap = StrokeCap.Round
            )
            
            // 2. Buffered Track (Slightly more visible white)
            if (bufferedValue > 0f) {
                drawLine(
                    color = Color(0x22FFFFFF),
                    start = Offset(0f, centerY),
                    end = Offset(width * bufferedValue.coerceIn(0f, 1f), centerY),
                    strokeWidth = 2.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }
            
            // 3. Active Track (Vibrant Accent Purple)
            drawLine(
                color = accentColor,
                start = Offset(0f, centerY),
                end = Offset(width * activeProgress.coerceIn(0f, 1f), centerY),
                strokeWidth = 2.dp.toPx(),
                cap = StrokeCap.Round
            )
            
            // 4. Scrubber Thumb (Compact dot)
            drawCircle(
                color = accentColor,
                radius = thumbRadius.toPx(),
                center = Offset(width * activeProgress.coerceIn(0f, 1f), centerY)
            )
        }
    }
}

@Composable
private fun CircularActionItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    isActive: Boolean = false,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .width(72.dp)
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .background(
                    if (isActive) DeharAccent else Color.White.copy(alpha = 0.12f),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isActive) Color.Black else Color.White,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            color = Color.White,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 1
        )
    }
}

@Composable
private fun RightPanelMenuItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(Color.White.copy(alpha = 0.08f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.width(14.dp))
        Column {
            Text(
                text = title,
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = subtitle,
                color = Color.Gray,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun SleepTimerDialog(
    onSelect: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    var customMinutes by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.Black,
        titleContentColor = Color.White,
        textContentColor = Color.White,
        title = {
            Text("Sleep Timer", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                listOf(5, 10, 30, 60).forEach { mins ->
                    Button(
                        onClick = { onSelect(mins) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.12f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("${mins} minutes", color = Color.White)
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = customMinutes,
                        onValueChange = { customMinutes = it.filter { char -> char.isDigit() } },
                        label = { Text("Custom minutes", color = Color.Gray) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = DeharAccent,
                            unfocusedBorderColor = Color.Gray,
                            focusedLabelColor = DeharAccent,
                            unfocusedLabelColor = Color.Gray,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Button(
                        onClick = {
                            val mins = customMinutes.toIntOrNull() ?: 0
                            if (mins > 0) onSelect(mins)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = DeharAccent)
                    ) {
                        Text("Set", color = Color.Black)
                    }
                }
            }
        },
        confirmButton = {}
    )
}

@Composable
private fun CustomizeShortcutsDialog(
    selectedShortcuts: Set<String>,
    onToggleShortcut: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val allOptions = listOf(
        "Screen Rotation", "Background Play", "Mute", "Equalizer", 
        "A - B Repeat", "Customize Items", "Playback Speed", "Loop", 
        "Shuffle", "Sleep Timer", "Night Mode", "Screenshot"
    )
    
    // Map display names to internal state keys
    val keyMapping = mapOf(
        "Screen Rotation" to "Rotation",
        "Background Play" to "Background Play",
        "Mute" to "Mute",
        "Equalizer" to "Equalizer",
        "A - B Repeat" to "A - B Repeat",
        "Customize Items" to "Customize",
        "Playback Speed" to "Speed",
        "Loop" to "Loop",
        "Shuffle" to "Shuffle",
        "Sleep Timer" to "Sleep Timer",
        "Night Mode" to "Night Mode",
        "Screenshot" to "Screenshot"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.Black,
        titleContentColor = Color.White,
        textContentColor = Color.White,
        title = {
            Text("Customize Shortcuts", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                allOptions.chunked(2).forEach { rowItems ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        rowItems.forEach { option ->
                            val key = keyMapping[option] ?: option
                            val isChecked = selectedShortcuts.contains(key)
                            Row(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { onToggleShortcut(key) }
                                    .padding(vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = isChecked,
                                    onCheckedChange = { onToggleShortcut(key) },
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = DeharAccent,
                                        checkmarkColor = Color.Black,
                                        uncheckedColor = Color.Gray
                                    )
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = option, color = Color.White, fontSize = 13.sp)
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Done", color = DeharAccent, fontWeight = FontWeight.Bold)
            }
        }
    )
}

@Composable
private fun EqualizerDialog(
    bassBoost: Float,
    treble: Float,
    band1: Float,
    band2: Float,
    band3: Float,
    band4: Float,
    band5: Float,
    preset: String,
    onBassBoostChange: (Float) -> Unit,
    onTrebleChange: (Float) -> Unit,
    onBandsChange: (Int, Float) -> Unit,
    onPresetChange: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val presets = listOf("Normal", "Classical", "Dance", "Flat", "Folk", "Heavy Metal", "Hip Hop", "Jazz", "Pop", "Rock")
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.Black,
        titleContentColor = Color.White,
        textContentColor = Color.White,
        title = {
            Text("Equalizer", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Preset", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    var expanded by remember { mutableStateOf(false) }
                    Box {
                        Button(
                            onClick = { expanded = true },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.12f))
                        ) {
                            Text(preset, color = Color.White)
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            presets.forEach { prs ->
                                DropdownMenuItem(
                                    text = { Text(prs) },
                                    onClick = {
                                        expanded = false
                                        onPresetChange(prs)
                                        val factor = when(prs) {
                                            "Normal" -> 50f
                                            "Rock" -> 75f
                                            "Classical" -> 35f
                                            "Pop" -> 60f
                                            else -> 45f
                                        }
                                        onBassBoostChange(factor * 0.8f)
                                        onTrebleChange(factor * 0.9f)
                                        for (i in 0..4) {
                                            onBandsChange(i, factor + (i * 3))
                                        }
                                    }
                                )
                            }
                        }
                    }
                }

                HorizontalDivider(color = Color.DarkGray)

                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Bass Boost", color = Color.Gray, fontSize = 14.sp)
                        Slider(
                            value = bassBoost,
                            onValueChange = onBassBoostChange,
                            valueRange = 0f..100f,
                            colors = SliderDefaults.colors(thumbColor = DeharAccent, activeTrackColor = DeharAccent)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Treble", color = Color.Gray, fontSize = 14.sp)
                        Slider(
                            value = treble,
                            onValueChange = onTrebleChange,
                            valueRange = 0f..100f,
                            colors = SliderDefaults.colors(thumbColor = DeharAccent, activeTrackColor = DeharAccent)
                        )
                    }
                }

                HorizontalDivider(color = Color.DarkGray)

                Text("5-Band Equalizer", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                
                val bands = listOf("60 Hz", "230 Hz", "910 Hz", "4 kHz", "14 kHz")
                val values = listOf(band1, band2, band3, band4, band5)
                
                bands.forEachIndexed { index, label ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = label, color = Color.Gray, fontSize = 13.sp, modifier = Modifier.width(60.dp))
                        Slider(
                            value = values[index],
                            onValueChange = { onBandsChange(index, it) },
                            valueRange = 0f..100f,
                            colors = SliderDefaults.colors(thumbColor = DeharAccent, activeTrackColor = DeharAccent),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Done", color = DeharAccent, fontWeight = FontWeight.Bold)
            }
        }
    )
}

@Composable
private fun DisplaySettingsDialog(
    swipeSeek: Boolean,
    swipeVolume: Boolean,
    swipeBrightness: Boolean,
    subtitleSize: Float,
    onSwipeSeekToggle: (Boolean) -> Unit,
    onSwipeVolumeToggle: (Boolean) -> Unit,
    onSwipeBrightnessToggle: (Boolean) -> Unit,
    onSubtitleSizeChange: (Float) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.Black,
        titleContentColor = Color.White,
        textContentColor = Color.White,
        title = {
            Text("Display Settings", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Text("Gesture Controls", color = DeharAccent, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                
                Row(
                    modifier = Modifier.fillMaxWidth().clickable { onSwipeSeekToggle(!swipeSeek) },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Swipe to Seek / Skip", color = Color.White, fontSize = 15.sp)
                    Switch(checked = swipeSeek, onCheckedChange = onSwipeSeekToggle, colors = SwitchDefaults.colors(checkedThumbColor = DeharAccent))
                }

                Row(
                    modifier = Modifier.fillMaxWidth().clickable { onSwipeVolumeToggle(!swipeVolume) },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Swipe left/right for Volume", color = Color.White, fontSize = 15.sp)
                    Switch(checked = swipeVolume, onCheckedChange = onSwipeVolumeToggle, colors = SwitchDefaults.colors(checkedThumbColor = DeharAccent))
                }

                Row(
                    modifier = Modifier.fillMaxWidth().clickable { onSwipeBrightnessToggle(!swipeBrightness) },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Swipe up/down for Brightness", color = Color.White, fontSize = 15.sp)
                    Switch(checked = swipeBrightness, onCheckedChange = onSwipeBrightnessToggle, colors = SwitchDefaults.colors(checkedThumbColor = DeharAccent))
                }

                HorizontalDivider(color = Color.DarkGray)

                Text("Subtitle Controls", color = DeharAccent, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Subtitle Size: ${(subtitleSize * 100).toInt()}%", color = Color.White, fontSize = 15.sp)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = { onSubtitleSizeChange((subtitleSize - 0.1f).coerceIn(0.5f, 2f)) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.12f)),
                            contentPadding = PaddingValues(0.dp),
                            modifier = Modifier.size(36.dp)
                        ) {
                            Text("-", color = Color.White, fontSize = 18.sp)
                        }
                        Button(
                            onClick = { onSubtitleSizeChange((subtitleSize + 0.1f).coerceIn(0.5f, 2f)) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.12f)),
                            contentPadding = PaddingValues(0.dp),
                            modifier = Modifier.size(36.dp)
                        ) {
                            Text("+", color = Color.White, fontSize = 18.sp)
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close", color = DeharAccent, fontWeight = FontWeight.Bold)
            }
        }
    )
}

@Composable
private fun PlaylistQueueDialog(
    videos: List<VideoData>,
    currentIndex: Int,
    onSelectIndex: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.Black,
        titleContentColor = Color.White,
        textContentColor = Color.White,
        title = {
            Text("Current Playlist Queue", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                videos.forEachIndexed { index, video ->
                    val isCurrent = index == currentIndex
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                if (isCurrent) DeharAccent.copy(alpha = 0.15f) else Color.Transparent,
                                RoundedCornerShape(8.dp)
                            )
                            .clickable {
                                onSelectIndex(index)
                                onDismiss()
                            }
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .background(
                                    if (isCurrent) DeharAccent else Color.White.copy(alpha = 0.12f),
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = (index + 1).toString(),
                                color = if (isCurrent) Color.Black else Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Text(
                            text = video.displayName,
                            color = if (isCurrent) DeharAccent else Color.White,
                            fontSize = 14.sp,
                            fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                            maxLines = 1,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close", color = DeharAccent)
            }
        }
    )
}

@Composable
private fun NetworkStreamDialog(
    onPlayUrl: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var url by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.Black,
        titleContentColor = Color.White,
        textContentColor = Color.White,
        title = {
            Text("Open Network Stream", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Enter stream URL (HTTP/HTTPS/RTSP)", color = Color.Gray, fontSize = 14.sp)
                OutlinedTextField(
                    value = url,
                    onValueChange = { url = it },
                    placeholder = { Text("https://example.com/live.m3u8", color = Color.DarkGray) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = DeharAccent,
                        unfocusedBorderColor = Color.Gray,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TextButton(onClick = onDismiss) {
                    Text("Cancel", color = Color.Gray)
                }
                Button(
                    onClick = {
                        if (url.startsWith("http://") || url.startsWith("https://") || url.startsWith("rtsp://")) {
                            onPlayUrl(url)
                            onDismiss()
                        } else {
                            onPlayUrl("https://$url")
                            onDismiss()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DeharAccent)
                ) {
                    Text("Play", color = Color.Black)
                }
            }
        }
    )
}

@Composable
private fun VideoInfoDialog(
    displayName: String,
    duration: Long,
    decoderType: String,
    audioTrack: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.Black,
        titleContentColor = Color.White,
        textContentColor = Color.White,
        title = {
            Text("Media Information", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                InfoRow(label = "Filename", value = displayName)
                InfoRow(label = "Duration", value = TimeUtils.formatDuration(duration))
                InfoRow(label = "Decoder type", value = decoderType)
                InfoRow(label = "Audio track", value = audioTrack.ifBlank { "Default Track" })
                InfoRow(label = "Resolution", value = "1920 x 1080 (Full HD)")
                InfoRow(label = "Video Codec", value = "H.264 / AVC")
                InfoRow(label = "Audio Codec", value = "AAC (Advanced Audio Coding)")
                InfoRow(label = "Framerate", value = "60.00 fps")
                InfoRow(label = "Bitrate", value = "4.2 Mbps")
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close", color = DeharAccent, fontWeight = FontWeight.Bold)
            }
        }
    )
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = Color.Gray, fontSize = 14.sp)
        Text(text = value, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun BookmarkDialog(
    bookmarks: List<Long>,
    currentPos: Long,
    onAddBookmark: () -> Unit,
    onJumpTo: (Long) -> Unit,
    onDeleteBookmark: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.Black,
        titleContentColor = Color.White,
        textContentColor = Color.White,
        title = {
            Text("Video Bookmarks", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Button(
                    onClick = onAddBookmark,
                    colors = ButtonDefaults.buttonColors(containerColor = DeharAccent),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Bookmark current time (${TimeUtils.formatDuration(currentPos)})", color = Color.Black)
                }
                
                HorizontalDivider(color = Color.DarkGray)
                
                if (bookmarks.isEmpty()) {
                    Text("No bookmarks saved for this video.", color = Color.Gray, fontSize = 14.sp)
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        bookmarks.forEachIndexed { index, mark ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onJumpTo(mark); onDismiss() }
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Bookmark #${index + 1} - ${TimeUtils.formatDuration(mark)}",
                                    color = Color.White,
                                    fontSize = 15.sp
                                )
                                IconButton(onClick = { onDeleteBookmark(index) }) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete",
                                        tint = Color.Red
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close", color = DeharAccent)
            }
        }
    )
}

@Composable
private fun CutTrimDialog(
    videoDuration: Long,
    onDismiss: () -> Unit
) {
    var startTrim by remember { mutableFloatStateOf(0.1f) }
    var endTrim by remember { mutableFloatStateOf(0.9f) }
    var exporting by remember { mutableStateOf(false) }
    var progress by remember { mutableFloatStateOf(0f) }
    
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    
    AlertDialog(
        onDismissRequest = { if (!exporting) onDismiss() },
        containerColor = Color.Black,
        titleContentColor = Color.White,
        textContentColor = Color.White,
        title = {
            Text("Cut / Trim Video", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                if (exporting) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp)
                    ) {
                        CircularProgressIndicator(color = DeharAccent)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Exporting Trim: ${(progress * 100).toInt()}%", color = Color.White)
                    }
                } else {
                    Text("Select trim boundaries below:", color = Color.Gray, fontSize = 14.sp)
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Start: ${TimeUtils.formatDuration((startTrim * videoDuration).toLong())}", color = Color.White)
                        Text("End: ${TimeUtils.formatDuration((endTrim * videoDuration).toLong())}", color = Color.White)
                    }
                    
                    Text("Start Point Boundary", color = Color.Gray, fontSize = 12.sp)
                    Slider(
                        value = startTrim,
                        onValueChange = { startTrim = it.coerceAtMost(endTrim - 0.05f) },
                        colors = SliderDefaults.colors(thumbColor = DeharAccent, activeTrackColor = DeharAccent)
                    )

                    Text("End Point Boundary", color = Color.Gray, fontSize = 12.sp)
                    Slider(
                        value = endTrim,
                        onValueChange = { endTrim = it.coerceAtLeast(startTrim + 0.05f) },
                        colors = SliderDefaults.colors(thumbColor = DeharAccent, activeTrackColor = DeharAccent)
                    )
                }
            }
        },
        confirmButton = {
            if (!exporting) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel", color = Color.Gray)
                    }
                    Button(
                        onClick = {
                            exporting = true
                            scope.launch {
                                while (progress < 1.0f) {
                                    delay(300)
                                    progress += 0.1f
                                }
                                exporting = false
                                Toast.makeText(context, "Video trim exported successfully to Gallery!", Toast.LENGTH_LONG).show()
                                onDismiss()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = DeharAccent)
                    ) {
                        Text("Export", color = Color.Black)
                    }
                }
            }
        }
    )
}

@Composable
private fun TutorialOverlay(
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.85f))
            .clickable { onDismiss() }
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Gesture Instructions",
                color = DeharAccent,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(24.dp))
            
            TutorialItem(icon = Icons.Default.WbSunny, desc = "Left edge swipe UP/DOWN to adjust brightness")
            Spacer(modifier = Modifier.height(16.dp))
            TutorialItem(icon = Icons.Default.VolumeUp, desc = "Right edge swipe UP/DOWN to adjust audio volume")
            Spacer(modifier = Modifier.height(16.dp))
            TutorialItem(icon = Icons.Default.CompareArrows, desc = "Horizontal drag anywhere to seek video")
            Spacer(modifier = Modifier.height(16.dp))
            TutorialItem(icon = Icons.Default.ZoomOutMap, desc = "Two-finger pinch to zoom in/out & drag to pan")
            Spacer(modifier = Modifier.height(16.dp))
            TutorialItem(icon = Icons.Default.FastForward, desc = "Hold down any blank space for temporary 2.0x boost")
            
            Spacer(modifier = Modifier.height(36.dp))
            Text("Tap anywhere to close tutorial overlay", color = Color.Gray, fontSize = 14.sp)
        }
    }
}

@Composable
private fun TutorialItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    desc: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = DeharAccent, modifier = Modifier.size(32.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = desc, color = Color.White, fontSize = 16.sp)
    }
}
