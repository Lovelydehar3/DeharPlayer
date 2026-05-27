package com.dehar.player.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.dehar.player.data.VideoData
import com.dehar.player.ui.theme.DeharSurface
import com.dehar.player.ui.theme.DeharUnplayedCyan
import com.dehar.player.utils.TimeUtils
import android.graphics.Bitmap
import android.os.Build
import android.util.Size
import android.provider.MediaStore
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.asImageBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun VideoItemCard(
    video: VideoData,
    isPlayed: Boolean = false,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit = {},
    onInfoClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    var showMenu by remember { mutableStateOf(false) }
    
    val thumbnailBitmap by produceState<Bitmap?>(initialValue = null, video.uri) {
        value = withContext(Dispatchers.IO) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    context.contentResolver.loadThumbnail(video.uri, Size(252, 140), null)
                } else {
                    @Suppress("DEPRECATION")
                    MediaStore.Video.Thumbnails.getThumbnail(
                        context.contentResolver,
                        video.id,
                        MediaStore.Video.Thumbnails.MINI_KIND,
                        null
                    )
                }
            } catch (e: Exception) {
                null
            }
        }
    }

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
            if (thumbnailBitmap != null) {
                Image(
                    bitmap = thumbnailBitmap!!.asImageBitmap(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.matchParentSize()
                )
            } else {
                AsyncImage(
                    model = video.uri,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.matchParentSize()
                )
            }

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

        Box {
            IconButton(onClick = { showMenu = true }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Video options",
                    tint = Color(0xFFE8EDF3),
                    modifier = Modifier.size(28.dp)
                )
            }
            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false },
                modifier = Modifier.background(DeharSurface)
            ) {
                DropdownMenuItem(
                    text = { Text("Delete", color = Color.Red) },
                    leadingIcon = { Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Red) },
                    onClick = {
                        showMenu = false
                        onDeleteClick()
                    }
                )
                DropdownMenuItem(
                    text = { Text("Info", color = Color.White) },
                    leadingIcon = { Icon(Icons.Default.Info, contentDescription = null, tint = Color.White) },
                    onClick = {
                        showMenu = false
                        onInfoClick()
                    }
                )
            }
        }
    }
}

@Composable
fun VideoGridItemCard(
    video: VideoData,
    isPlayed: Boolean = false,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val thumbnailBitmap by produceState<Bitmap?>(initialValue = null, video.uri) {
        value = withContext(Dispatchers.IO) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    context.contentResolver.loadThumbnail(video.uri, Size(252, 140), null)
                } else {
                    @Suppress("DEPRECATION")
                    MediaStore.Video.Thumbnails.getThumbnail(
                        context.contentResolver,
                        video.id,
                        MediaStore.Video.Thumbnails.MINI_KIND,
                        null
                    )
                }
            } catch (e: Exception) {
                null
            }
        }
    }

    val titleColor = if (isPlayed) Color(0xFFE8EDF3) else DeharUnplayedCyan

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.77f)
                .clip(RoundedCornerShape(8.dp))
                .background(DeharSurface),
            contentAlignment = Alignment.Center
        ) {
            if (thumbnailBitmap != null) {
                Image(
                    bitmap = thumbnailBitmap!!.asImageBitmap(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.matchParentSize()
                )
            } else {
                AsyncImage(
                    model = video.uri,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.matchParentSize()
                )
            }

            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(Color.Black.copy(alpha = 0.22f))
            )

            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "Video Play",
                tint = DeharUnplayedCyan,
                modifier = Modifier.size(24.dp)
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
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = video.displayName,
            color = titleColor,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
