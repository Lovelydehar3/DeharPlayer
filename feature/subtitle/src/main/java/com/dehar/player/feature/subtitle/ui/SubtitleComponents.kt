package com.dehar.player.feature.subtitle.ui

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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.dehar.player.feature.subtitle.domain.SubtitleLine
import com.dehar.player.core.data.subtitle.SubtitleTrack
import com.dehar.player.core.ui.components.DeharBottomSheet

/**
 * Displays a subtitle line at the bottom of the video player
 */
@Composable
fun SubtitleDisplay(
    subtitleLine: SubtitleLine?,
    modifier: Modifier = Modifier,
    fontSize: Int = 18
) {
    if (subtitleLine == null) return

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.Black.copy(alpha = 0.7f))
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = subtitleLine.text,
            color = Color.White,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}

/**
 * Subtitle selector bottom sheet
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubtitleSelectorSheet(
    tracks: List<SubtitleTrack>,
    selectedTrackId: Long?,
    onTrackSelect: (SubtitleTrack) -> Unit,
    onDisable: () -> Unit,
    onDismiss: () -> Unit,
    sheetState: SheetState
) {
    DeharBottomSheet(
        title = "Subtitles",
        onDismiss = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            // Disable option
            SubtitleOptionItem(
                label = "None (Disable Subtitles)",
                isSelected = selectedTrackId == null,
                onClick = {
                    onDisable()
                    onDismiss()
                }
            )

            // Subtitle tracks
            tracks.forEach { track ->
                SubtitleOptionItem(
                    label = "${track.name} (${track.language})",
                    description = track.format.displayName,
                    isSelected = track.id == selectedTrackId,
                    onClick = {
                        onTrackSelect(track)
                        onDismiss()
                    }
                )
            }
        }
    }
}

@Composable
private fun SubtitleOptionItem(
    label: String,
    description: String? = null,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isSelected) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    },
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                if (description != null) {
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Selected",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

/**
 * Subtitle settings panel for customizing appearance
 */
@Composable
fun SubtitleSettingsPanel(
    fontSize: Int = 18,
    onFontSizeChange: (Int) -> Unit,
    fontColor: Color = Color.White,
    onFontColorChange: (Color) -> Unit,
    backgroundColor: Color = Color.Black,
    onBackgroundColorChange: (Color) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Subtitle Settings",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        // Font size slider
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Font Size",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = "$fontSize px",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            androidx.compose.material3.Slider(
                value = fontSize.toFloat(),
                onValueChange = { onFontSizeChange(it.toInt()) },
                valueRange = 10f..40f,
                steps = 5,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Preset text colors
        Text(
            text = "Text Color",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val colors = listOf(Color.White, Color.Yellow, Color.Cyan, Color.Green)
            colors.forEach { color ->
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(color)
                        .clickable { onFontColorChange(color) }
                        .padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (color == fontColor) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}
