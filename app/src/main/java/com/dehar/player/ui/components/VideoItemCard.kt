package com.dehar.player.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.dehar.player.data.VideoData
import com.dehar.player.ui.theme.DeharSurface
import com.dehar.player.ui.theme.DeharUnplayedCyan
import com.dehar.player.utils.TimeUtils

@Composable
fun VideoItemCard(
    video: VideoData,
    isPlayed: Boolean = false,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 13.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(width = 126.dp, height = 70.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(DeharSurface),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = video.uri,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()
            )

            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(Color.Black.copy(alpha = 0.22f))
            )

            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "Video Play",
                tint = DeharUnplayedCyan,
                modifier = Modifier.size(32.dp)
            )

            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(4.dp)
                    .background(Color.Black.copy(alpha = 0.7f), shape = RoundedCornerShape(4.dp))
                    .padding(horizontal = 4.dp, vertical = 2.dp)
            ) {
                Text(
                    text = TimeUtils.formatDuration(video.duration),
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        val titleColor = if (isPlayed) Color(0xFFE8EDF3) else DeharUnplayedCyan

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = video.displayName,
                color = titleColor,
                fontSize = 17.sp,
                lineHeight = 21.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            
            // Format size and resolution
            val resolution = when {
                video.height >= 2160 -> "4K"
                video.height >= 1080 -> "1080p"
                video.height >= 720 -> "720p"
                video.height >= 480 -> "480p"
                video.height > 0 -> "${video.height}p"
                else -> ""
            }
            val sizeStr = when {
                video.size >= 1024 * 1024 * 1024 -> String.format("%.2f GB", video.size.toDouble() / (1024 * 1024 * 1024))
                video.size >= 1024 * 1024 -> String.format("%.2f MB", video.size.toDouble() / (1024 * 1024))
                else -> String.format("%.2f KB", video.size.toDouble() / 1024)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = sizeStr,
                    color = Color(0xFF9AA6B2),
                    fontSize = 13.sp
                )
                if (resolution.isNotEmpty()) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .background(Color(0xFF3A3A3A), RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = resolution,
                            color = Color(0xFFCCCCCC),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = "Video options",
            tint = Color(0xFFE8EDF3),
            modifier = Modifier.size(28.dp)
        )
    }
}
