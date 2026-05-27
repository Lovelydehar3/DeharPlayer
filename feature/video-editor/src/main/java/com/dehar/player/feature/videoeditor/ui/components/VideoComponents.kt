package com.dehar.player.feature.videoeditor.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Filter
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dehar.player.feature.videoeditor.model.EncodingProgress
import com.dehar.player.feature.videoeditor.model.ExportQuality
import com.dehar.player.feature.videoeditor.model.FilterType
import com.dehar.player.feature.videoeditor.model.VideoClip
import com.dehar.player.feature.videoeditor.model.VideoEditProject
import com.dehar.player.feature.videoeditor.model.VideoFilter

/**
 * Video info card displaying project metadata
 */
@Composable
fun VideoInfoCard(
    project: VideoEditProject,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = project.projectName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                InfoItem("Duration", formatDuration(project.videoDuration))
                InfoItem("Resolution", "${project.videoWidth}x${project.videoHeight}")
                InfoItem("FPS", project.frameRate.toString())
            }
        }
    }
}

/**
 * Timeline trim slider
 */
@Composable
fun TimelineSlider(
    duration: Long,
    startMs: Long,
    endMs: Long,
    onStartChange: (Long) -> Unit,
    onEndChange: (Long) -> Unit,
    currentTimeMs: Long = 0L,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Trim Video",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Medium
        )

        // Start time slider
        Column {
            Text(
                text = "Start: ${formatDuration(startMs)}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Slider(
                value = startMs.toFloat(),
                onValueChange = { onStartChange(it.toLong()) },
                valueRange = 0f..duration.toFloat(),
                modifier = Modifier.fillMaxWidth()
            )
        }

        // End time slider
        Column {
            Text(
                text = "End: ${formatDuration(endMs)}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Slider(
                value = endMs.toFloat(),
                onValueChange = { onEndChange(it.toLong()) },
                valueRange = startMs.toFloat()..duration.toFloat(),
                modifier = Modifier.fillMaxWidth()
            )
        }

        Text(
            text = "Duration: ${formatDuration(endMs - startMs)}",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

/**
 * Filter selector with chips
 */
@Composable
fun FilterSelector(
    selectedFilters: List<VideoFilter>,
    onFilterAdd: (VideoFilter) -> Unit,
    onFilterRemove: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Filters",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Medium
        )

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(FilterType.values()) { filterType ->
                FilterChip(
                    filterType = filterType,
                    isSelected = selectedFilters.any { it.type == filterType },
                    onToggle = { isSelected ->
                        if (isSelected) {
                            onFilterAdd(
                                VideoFilter(
                                    id = "${filterType.name}_${System.currentTimeMillis()}",
                                    type = filterType,
                                    value = when (filterType) {
                                        FilterType.BRIGHTNESS -> 1.0f
                                        FilterType.CONTRAST -> 1.0f
                                        FilterType.SATURATION -> 1.0f
                                        FilterType.HUE -> 0.5f
                                        FilterType.BLUR -> 1.0f
                                        FilterType.SHARPEN -> 1.0f
                                        else -> 1.0f
                                    }
                                )
                            )
                        } else {
                            selectedFilters.find { it.type == filterType }
                                ?.let { onFilterRemove(it.id) }
                        }
                    }
                )
            }
        }
    }
}

/**
 * Individual filter chip
 */
@Composable
fun FilterChip(
    filterType: FilterType,
    isSelected: Boolean,
    onToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(
                if (isSelected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.surface
            )
            .clickable { onToggle(!isSelected) }
            .padding(12.dp, 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = filterType.name.replace("_", " "),
            style = MaterialTheme.typography.labelSmall,
            fontSize = 11.sp,
            color = if (isSelected) MaterialTheme.colorScheme.onPrimary
            else MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Medium
        )
    }
}

/**
 * Encoding progress display
 */
@Composable
fun EncodingProgressCard(
    progress: EncodingProgress,
    fileName: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Encoding: $fileName",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )

            LinearProgressIndicator(
                progress = progress.getProgressPercentage() / 100f,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${progress.getProgressPercentage()}%",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "ETA: ${formatDuration(progress.getEstimatedTimeRemaining())}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Text(
                text = "${progress.fps.toInt()} fps • Size: ${formatFileSize(progress.outputSize)}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * Export quality selector
 */
@Composable
fun QualitySelector(
    selectedQuality: ExportQuality,
    onQualitySelect: (ExportQuality) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Export Quality",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Medium
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ExportQuality.values().forEach { quality ->
                Button(
                    onClick = { onQualitySelect(quality) },
                    modifier = Modifier.weight(1f),
                    enabled = true
                ) {
                    Text(
                        text = quality.name,
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 10.sp
                    )
                }
            }
        }
    }
}

/**
 * Clip item display
 */
@Composable
fun ClipItem(
    clip: VideoClip,
    isSelected: Boolean,
    onSelect: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onSelect() }
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Clip: ${formatDuration(clip.startTime)} - ${formatDuration(clip.endTime)}",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "${clip.filters.size} filter(s) • Speed: ${clip.speed}x",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                Icon(Icons.Default.Close, contentDescription = "Delete", modifier = Modifier.size(18.dp))
            }
        }
    }
}

/**
 * Utility: Format duration in milliseconds
 */
fun formatDuration(ms: Long): String {
    val seconds = (ms / 1000) % 60
    val minutes = (ms / (1000 * 60)) % 60
    val hours = (ms / (1000 * 60 * 60))

    return when {
        hours > 0 -> String.format("%02d:%02d:%02d", hours, minutes, seconds)
        else -> String.format("%02d:%02d", minutes, seconds)
    }
}

/**
 * Utility: Format file size
 */
fun formatFileSize(bytes: Long): String {
    return when {
        bytes <= 0 -> "0 B"
        bytes < 1024 -> "$bytes B"
        bytes < 1024 * 1024 -> "${bytes / 1024} KB"
        bytes < 1024 * 1024 * 1024 -> "${bytes / (1024 * 1024)} MB"
        else -> "${bytes / (1024 * 1024 * 1024)} GB"
    }
}

/**
 * Info item for display
 */
@Composable
private fun InfoItem(
    label: String,
    value: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold
        )
    }
}
