package com.dehar.player.feature.lyrics.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dehar.player.core.data.lyrics.LyricLine

/**
 * Displays synchronized lyrics for the currently playing song
 */
@Composable
fun LyricsDisplay(
    lyrics: List<LyricLine>,
    currentPositionMs: Long,
    modifier: Modifier = Modifier,
    fontSize: Int = 18
) {
    if (lyrics.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No lyrics available",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        return
    }

    val listState = rememberLazyListState()
    val currentIndex = lyrics.indexOfLast { it.timeMs <= currentPositionMs }.coerceAtLeast(0)

    LaunchedEffect(currentIndex) {
        if (currentIndex > 0) {
            listState.animateScrollToItem(
                index = currentIndex,
                scrollOffset = -100
            )
        }
    }

    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        state = listState,
        contentPadding = PaddingValues(vertical = 120.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(
            items = lyrics,
            key = { it.timeMs }
        ) { lyric ->
            val isCurrent = lyric.timeMs <= currentPositionMs
            val nextLyric = lyrics.getOrNull(lyrics.indexOf(lyric) + 1)
            val isNext = nextLyric?.timeMs ?: Long.MAX_VALUE > currentPositionMs && !isCurrent

            val alpha by animateFloatAsState(
                targetValue = when {
                    isCurrent -> 1.0f
                    isNext -> 0.6f
                    else -> 0.3f
                }
            )

            val fontSize_sp = fontSize.sp
            val fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal

            Text(
                text = lyric.text,
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .alpha(alpha),
                style = MaterialTheme.typography.bodyLarge,
                fontSize = fontSize_sp,
                fontWeight = fontWeight,
                color = if (isCurrent) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurface
                },
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * Mini lyrics display showing current and next line
 */
@Composable
fun MiniLyricsDisplay(
    lyrics: List<LyricLine>,
    currentPositionMs: Long,
    modifier: Modifier = Modifier
) {
    if (lyrics.isEmpty()) return

    val currentIndex = lyrics.indexOfLast { it.timeMs <= currentPositionMs }.coerceAtLeast(-1)
    val currentLyric = if (currentIndex >= 0) lyrics[currentIndex] else null
    val nextLyric = if (currentIndex + 1 < lyrics.size) lyrics[currentIndex + 1] else null

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (currentLyric != null) {
            Text(
                text = currentLyric.text,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        if (nextLyric != null) {
            Text(
                text = nextLyric.text,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(0.6f)
            )
        }
    }
}

@Composable
fun LyricsToolbar(
    hasSyncedLyrics: Boolean,
    onSearch: () -> Unit,
    onSettings: () -> Unit,
    onDownload: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (!hasSyncedLyrics) {
                Button(
                    onClick = onDownload,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Download Lyrics")
                }
            }

            IconButton(
                onClick = onSearch
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search lyrics"
                )
            }

            IconButton(
                onClick = onSettings
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings"
                )
            }
        }
    }
}
