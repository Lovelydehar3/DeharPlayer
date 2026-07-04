package com.dehar.player.feature.ringtoneeditor.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dehar.player.feature.ringtoneeditor.model.PlaybackState
import com.dehar.player.feature.ringtoneeditor.model.TrimState
import com.dehar.player.feature.ringtoneeditor.model.WaveformData
import kotlin.math.roundToInt

/**
 * Waveform display with trim handles
 */
@Composable
fun WaveformDisplay(
    waveformData: WaveformData?,
    trimState: TrimState,
    onTrimStartChanged: (Long) -> Unit,
    onTrimEndChanged: (Long) -> Unit,
    onDraggingStartChanged: (Boolean) -> Unit,
    onDraggingEndChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    if (waveformData == null) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(MaterialTheme.colorScheme.surfaceContainer),
            contentAlignment = Alignment.Center
        ) {
            Text("No waveform data")
        }
        return
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .clip(RoundedCornerShape(12.dp))
    ) {
        // Waveform bars
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(vertical = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(1.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val sampleCount = waveformData.samples.size
            for (i in waveformData.samples.indices) {
                val amplitude = waveformData.samples[i]
                val heightFraction = amplitude / 100f
                
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height((160 * heightFraction).dp)
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(2.dp)
                        )
                )
            }
        }

        // Trim region highlight
        val totalWidth = waveformData.durationMs
        val trimStartPct = (trimState.startMs.toFloat() / totalWidth).coerceIn(0f, 1f)
        val trimEndPct = (trimState.endMs.toFloat() / totalWidth).coerceIn(0f, 1f)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(
                    color = Color.Black.copy(alpha = 0.3f)
                )
                .pointerInput(Unit) {
                    detectHorizontalDragGestures { change, dragAmount ->
                        change.consume()
                        // Drag logic handled by trim handles
                    }
                }
        )

        // Start handle
        TrimHandle(
            position = trimStartPct,
            isDragging = trimState.isDraggingStart,
            onDraggingChanged = { isDragging ->
                onDraggingStartChanged(isDragging)
            },
            onPositionChanged = { newPct ->
                val newMs = (newPct * totalWidth).toLong()
                onTrimStartChanged(newMs)
            },
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = (trimStartPct * 100).dp - 6.dp)
        )

        // End handle
        TrimHandle(
            position = trimEndPct,
            isDragging = trimState.isDraggingEnd,
            onDraggingChanged = { isDragging ->
                onDraggingEndChanged(isDragging)
            },
            onPositionChanged = { newPct ->
                val newMs = (newPct * totalWidth).toLong()
                onTrimEndChanged(newMs)
            },
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = (100 - (trimEndPct * 100)).dp - 6.dp),
            color = MaterialTheme.colorScheme.tertiary
        )
    }
}

/**
 * Trim handle for dragging
 */
@Composable
fun TrimHandle(
    position: Float,
    isDragging: Boolean,
    onDraggingChanged: (Boolean) -> Unit,
    onPositionChanged: (Float) -> Unit,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary
) {
    Box(
        modifier = modifier
            .size(12.dp)
            .background(color = color, shape = CircleShape)
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragStart = { onDraggingChanged(true) },
                    onDragEnd = { onDraggingChanged(false) },
                    onHorizontalDrag = { change, dragAmount ->
                        change.consume()
                        val newPosition = (position + dragAmount / size.width).coerceIn(0f, 1f)
                        onPositionChanged(newPosition)
                    }
                )
            }
    )
}

/**
 * Time display showing current trim positions
 */
@Composable
fun TrimTimeDisplay(
    trimState: TrimState,
    totalDuration: Long,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text("Start", style = MaterialTheme.typography.labelSmall)
            Text(
                formatTime(trimState.startMs),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Duration", style = MaterialTheme.typography.labelSmall)
            Text(
                formatTime(trimState.getTrimmedDuration()),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Column(horizontalAlignment = Alignment.End) {
            Text("End", style = MaterialTheme.typography.labelSmall)
            Text(
                formatTime(trimState.endMs),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

/**
 * Playback controls for preview
 */
@Composable
fun PlaybackControls(
    playbackState: PlaybackState,
    totalDuration: Long,
    onPlayPauseClick: () -> Unit,
    onVolumeChanged: (Float) -> Unit,
    onSpeedChanged: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Play/Pause button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            IconButton(
                onClick = onPlayPauseClick,
                modifier = Modifier
                    .size(56.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = CircleShape
                    )
            ) {
                Icon(
                    imageVector = if (playbackState.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = if (playbackState.isPlaying) "Pause" else "Play",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(28.dp)
                )
            }
        }

        // Volume control
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.VolumeUp,
                contentDescription = "Volume",
                modifier = Modifier.size(24.dp)
            )
            Slider(
                value = playbackState.volume,
                onValueChange = onVolumeChanged,
                valueRange = 0f..1f,
                modifier = Modifier.weight(1f)
            )
            Text(
                "${(playbackState.volume * 100).roundToInt()}%",
                style = MaterialTheme.typography.labelSmall
            )
        }

        // Speed control
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Speed", style = MaterialTheme.typography.labelSmall, modifier = Modifier.width(50.dp))
            Slider(
                value = playbackState.playbackSpeed,
                onValueChange = onSpeedChanged,
                valueRange = 0.5f..2f,
                modifier = Modifier.weight(1f)
            )
            Text(
                String.format("%.1fx", playbackState.playbackSpeed),
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

/**
 * Save action buttons
 */
@Composable
fun SaveActionButtons(
    onSaveCustom: () -> Unit,
    onSetAsRingtone: () -> Unit,
    isSaving: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(
            onClick = onSaveCustom,
            enabled = !isSaving,
            modifier = Modifier.weight(1f)
        ) {
            Text("Save Ringtone")
        }

        Button(
            onClick = onSetAsRingtone,
            enabled = !isSaving,
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = Icons.Default.Download,
                contentDescription = "Set as default"
            )
        }
    }
}

/**
 * Format milliseconds to MM:SS
 */
fun formatTime(ms: Long): String {
    val totalSeconds = ms / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%02d:%02d", minutes, seconds)
}
