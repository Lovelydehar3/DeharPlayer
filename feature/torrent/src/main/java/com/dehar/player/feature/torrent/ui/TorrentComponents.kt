package com.dehar.player.feature.torrent.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
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
 * Torrent metadata display
 */
@Composable
fun TorrentMetadataCard(
    metadata: com.dehar.player.feature.torrent.model.TorrentMetadata,
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
        Text(
            text = metadata.name,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Total Size", style = MaterialTheme.typography.labelSmall)
                Text(
                    formatFileSize(metadata.totalSize),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Files", style = MaterialTheme.typography.labelSmall)
                Text(
                    metadata.files.size.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        if (!metadata.comment.isNullOrEmpty()) {
            Text(
                text = metadata.comment,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * Torrent file item
 */
@Composable
fun TorrentFileItem(
    file: com.dehar.player.feature.torrent.model.TorrentFile,
    onPlayClick: (com.dehar.player.feature.torrent.model.TorrentFile) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.CloudDownload,
            contentDescription = "File",
            modifier = Modifier.size(32.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = file.name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = formatFileSize(file.size),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        IconButton(onClick = { onPlayClick(file) }) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "Stream",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

/**
 * Torrent download progress card
 */
@Composable
fun TorrentProgressCard(
    downloadState: com.dehar.player.feature.torrent.model.TorrentDownloadState,
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
        // Progress bar
        LinearProgressIndicator(
            progress = downloadState.progress,
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )

        // Progress text
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "${downloadState.getProgressPercentage().roundToInt()}%",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "${formatFileSize(downloadState.downloadedBytes)} / ${formatFileSize(downloadState.totalBytes)}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Speed and peers
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Download", style = MaterialTheme.typography.labelSmall)
                Text(
                    formatSpeed(downloadState.downloadSpeed),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Upload", style = MaterialTheme.typography.labelSmall)
                Text(
                    formatSpeed(downloadState.uploadSpeed),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Peers", style = MaterialTheme.typography.labelSmall)
                Text(
                    "${downloadState.peers}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // ETA
        if (downloadState.eta > 0) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(4.dp))
                    .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f))
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Time Remaining",
                    style = MaterialTheme.typography.labelSmall
                )
                Text(
                    text = formatDuration(downloadState.eta),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

/**
 * Torrent error display
 */
@Composable
fun TorrentErrorCard(
    errorMessage: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFEF5350).copy(alpha = 0.1f))
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.ErrorOutline,
            contentDescription = "Error",
            modifier = Modifier.size(24.dp),
            tint = Color(0xFFEF5350)
        )
        
        Text(
            text = errorMessage,
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFFEF5350),
            modifier = Modifier.weight(1f)
        )
    }
}

/**
 * Torrent file list
 */
@Composable
fun TorrentFileList(
    files: List<com.dehar.player.feature.torrent.model.TorrentFile>,
    onPlayClick: (com.dehar.player.feature.torrent.model.TorrentFile) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(files) { file ->
            TorrentFileItem(
                file = file,
                onPlayClick = onPlayClick
            )
        }
    }
}

/**
 * Format file size for display
 */
fun formatFileSize(bytes: Long): String {
    return when {
        bytes < 1024 -> "$bytes B"
        bytes < 1024 * 1024 -> "${(bytes / 1024.0).roundToInt()} KB"
        bytes < 1024 * 1024 * 1024 -> "${(bytes / (1024.0 * 1024)).roundToInt()} MB"
        else -> "${(bytes / (1024.0 * 1024 * 1024)).roundToInt()} GB"
    }
}

/**
 * Format speed for display
 */
fun formatSpeed(bytesPerSecond: Long): String {
    return when {
        bytesPerSecond < 1024 -> "$bytesPerSecond B/s"
        bytesPerSecond < 1024 * 1024 -> "${(bytesPerSecond / 1024.0).roundToInt()} KB/s"
        else -> "${(bytesPerSecond / (1024.0 * 1024)).roundToInt()} MB/s"
    }
}

/**
 * Format duration for display
 */
fun formatDuration(milliseconds: Long): String {
    val seconds = milliseconds / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    
    return when {
        hours > 0 -> "${hours}h ${minutes % 60}m"
        minutes > 0 -> "${minutes}m ${seconds % 60}s"
        else -> "${seconds}s"
    }
}
