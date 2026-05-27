package com.dehar.player.ui.components

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dehar.player.utils.TimeUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun VideoInfoDialog(uri: Uri, context: Context, onDismiss: () -> Unit) {
    val info by produceState<VideoInfo?>(null, uri) {
        value = withContext(Dispatchers.IO) {
            val r = MediaMetadataRetriever()
            runCatching {
                r.setDataSource(context, uri)
                VideoInfo(
                    title = r.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
                        ?: uri.lastPathSegment ?: "Unknown",
                    path = uri.path ?: "",
                    duration = r.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                        ?.toLongOrNull() ?: 0L,
                    width = r.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)
                        ?.toIntOrNull() ?: 0,
                    height = r.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)
                        ?.toIntOrNull() ?: 0,
                    bitrate = r.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE)
                        ?.toLongOrNull() ?: 0L,
                    fps = r.extractMetadata(MediaMetadataRetriever.METADATA_KEY_CAPTURE_FRAMERATE)
                        ?.toFloatOrNull() ?: 0f,
                    mimeType = r.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE) ?: ""
                )
            }.getOrNull().also { r.release() }
        }
    }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Video Info") },
        text = {
            info?.let { v ->
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    InfoRow("File", v.title)
                    InfoRow("Path", v.path, isPath = true)
                    InfoRow("Resolution", "${v.width} × ${v.height}")
                    InfoRow("Duration", TimeUtils.formatDuration(v.duration))
                    InfoRow("Bitrate", "${v.bitrate / 1000} kbps")
                    InfoRow("Frame Rate", "${v.fps} fps")
                    InfoRow("Format", v.mimeType)
                }
            } ?: Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        },
        confirmButton = { TextButton(onClick = onDismiss) { Text("Close") } }
    )
}

@Composable
private fun InfoRow(label: String, value: String, isPath: Boolean = false) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(label, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.width(80.dp))
        Text(value, fontSize = 12.sp,
            modifier = Modifier.weight(1f),
            maxLines = if (isPath) 2 else 1,
            overflow = TextOverflow.Ellipsis)
    }
}

data class VideoInfo(val title: String, val path: String, val duration: Long,
    val width: Int, val height: Int, val bitrate: Long, val fps: Float, val mimeType: String)
