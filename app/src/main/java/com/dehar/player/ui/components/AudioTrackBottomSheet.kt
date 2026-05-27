package com.dehar.player.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.media3.common.C
import androidx.media3.common.TrackSelectionOverride
import androidx.media3.exoplayer.ExoPlayer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AudioTrackBottomSheet(
    player: ExoPlayer,
    onDismiss: () -> Unit
) {
    val tracks = remember(player.currentTracks) {
        player.currentTracks.groups
            .filter { it.type == C.TRACK_TYPE_AUDIO }
            .mapIndexed { idx, group ->
                val fmt = group.getTrackFormat(0)
                Triple(
                    idx,
                    "${fmt.language?.uppercase() ?: "Track ${idx+1}"} — ${fmt.codecs ?: ""} ${if(fmt.channelCount>2) "5.1" else "Stereo"}",
                    group.isSelected
                )
            }
    }
    
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Text(
            "Audio Track",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(16.dp)
        )
        tracks.forEach { (idx, label, isSelected) ->
            ListItem(
                headlineContent = { Text(label) },
                leadingContent = {
                    if (isSelected) Icon(Icons.Default.Check, null, tint = MaterialTheme.colorScheme.primary)
                    else Spacer(Modifier.size(24.dp))
                },
                modifier = Modifier.clickable {
                    val params = player.trackSelectionParameters.buildUpon()
                        .setOverrideForType(
                            TrackSelectionOverride(
                                player.currentTracks.groups[idx].mediaTrackGroup, 0
                            )
                        ).build()
                    player.trackSelectionParameters = params
                    onDismiss()
                }
            )
        }
        Spacer(Modifier.height(32.dp))
    }
}
