package com.dehar.player.feature.ringtoneeditor.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import kotlin.math.abs

@Composable
fun WaveformView(
    amplitudes: FloatArray,
    startFraction: Float,
    endFraction: Float,
    playFraction: Float,
    onStartDrag: (Float) -> Unit,
    onEndDrag: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    val primary = MaterialTheme.colorScheme.primary
    val muted = MaterialTheme.colorScheme.surfaceVariant
    val played = primary.copy(alpha = 0.4f)
    
    Canvas(modifier = modifier
        .height(100.dp)
        .pointerInput(Unit) {
            detectDragGestures(
                onDrag = { change, _ ->
                    val fraction = (change.position.x / size.width).coerceIn(0f, 1f)
                    if (abs(fraction - startFraction) < abs(fraction - endFraction)) {
                        onStartDrag(fraction)
                    } else {
                        onEndDrag(fraction)
                    }
                }
            )
        }
    ) {
        val barW = size.width / amplitudes.size
        val cy = size.height / 2
        
        amplitudes.forEachIndexed { i, amp ->
            val frac = i.toFloat() / amplitudes.size
            val barH = (amp * size.height * 0.85f).coerceAtLeast(2f)
            val color = when {
                frac < startFraction || frac > endFraction -> muted
                frac < playFraction -> played
                else -> primary
            }
            drawRect(color, Offset(i * barW, cy - barH/2), Size(barW * 0.65f, barH))
        }
        
        drawLine(Color.Green, Offset(startFraction * size.width, 0f),
            Offset(startFraction * size.width, size.height), strokeWidth = 3f)
        drawCircle(Color.Green, 8f, Offset(startFraction * size.width, cy))
        
        drawLine(Color.Red, Offset(endFraction * size.width, 0f),
            Offset(endFraction * size.width, size.height), strokeWidth = 3f)
        drawCircle(Color.Red, 8f, Offset(endFraction * size.width, cy))
    }
}
