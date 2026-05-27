package com.dehar.player.feature.cast.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cast
import androidx.compose.material.icons.filled.CastConnected
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.overflow.TextOverflow
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

/**
 * Cast device item
 */
@Composable
fun CastDeviceItem(
    device: com.dehar.player.feature.cast.model.CastDevice,
    isSelected: Boolean = false,
    onDeviceClick: (com.dehar.player.feature.cast.model.CastDevice) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(
                color = if (isSelected) {
                    MaterialTheme.colorScheme.primaryContainer
                } else {
                    MaterialTheme.colorScheme.surfaceContainer
                }
            )
            .clickable { onDeviceClick(device) }
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if (device.isConnected) Icons.Default.CastConnected else Icons.Default.Cast,
            contentDescription = "Cast Device",
            modifier = Modifier.size(32.dp),
            tint = if (device.isConnected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            }
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = device.name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (!device.modelName.isNullOrEmpty()) {
                Text(
                    text = device.modelName,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        if (device.isConnected) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(4.dp)
                    )
            )
        }
    }
}

/**
 * Cast session info card
 */
@Composable
fun CastSessionCard(
    session: com.dehar.player.feature.cast.model.CastSession,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Device info
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.CastConnected,
                contentDescription = "Connected",
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Casting to ${session.device.name}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
        }

        // Media info
        if (session.mediaInfo != null) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = session.mediaInfo.title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (!session.mediaInfo.subtitle.isNullOrEmpty()) {
                    Text(
                        text = session.mediaInfo.subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // Progress bar
            if (session.mediaInfo.duration > 0) {
                LinearProgressIndicator(
                    progress = session.mediaInfo.position.toFloat() / session.mediaInfo.duration.toFloat(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

/**
 * Cast control buttons
 */
@Composable
fun CastControlButtons(
    state: com.dehar.player.feature.cast.model.CastSessionState,
    onPlay: () -> Unit,
    onPause: () -> Unit,
    onStop: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        IconButton(
            onClick = onPlay,
            modifier = Modifier
                .weight(1f)
                .padding(4.dp)
        ) {
            Icon(Icons.Default.PlayArrow, contentDescription = "Play")
        }

        IconButton(
            onClick = onPause,
            modifier = Modifier
                .weight(1f)
                .padding(4.dp)
        ) {
            Icon(Icons.Default.Pause, contentDescription = "Pause")
        }

        IconButton(
            onClick = onStop,
            modifier = Modifier
                .weight(1f)
                .padding(4.dp)
        ) {
            Icon(Icons.Default.Stop, contentDescription = "Stop")
        }
    }
}

/**
 * Cast volume control
 */
@Composable
fun CastVolumeControl(
    volume: Float,
    isMuted: Boolean,
    onVolumeChanged: (Float) -> Unit,
    onMuteToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        IconButton(
            onClick = onMuteToggle,
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                imageVector = Icons.Default.VolumeUp,
                contentDescription = "Mute",
                modifier = Modifier.size(20.dp),
                tint = if (isMuted) {
                    MaterialTheme.colorScheme.onSurfaceVariant
                } else {
                    MaterialTheme.colorScheme.primary
                }
            )
        }

        Slider(
            value = if (isMuted) 0f else volume,
            onValueChange = onVolumeChanged,
            valueRange = 0f..1f,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = "${(volume * 100).roundToInt()}%",
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.width(35.dp)
        )
    }
}

/**
 * Cast device list
 */
@Composable
fun CastDeviceList(
    devices: List<com.dehar.player.feature.cast.model.CastDevice>,
    selectedDeviceId: String? = null,
    onDeviceClick: (com.dehar.player.feature.cast.model.CastDevice) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(devices) { device ->
            CastDeviceItem(
                device = device,
                isSelected = device.id == selectedDeviceId,
                onDeviceClick = onDeviceClick
            )
        }
    }
}

/**
 * Padding helper
 */
@Composable
fun Modifier.width(width: androidx.compose.ui.unit.Dp): Modifier =
    this.then(androidx.compose.foundation.layout.width(width))
