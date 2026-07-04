package com.dehar.player.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SleepTimerDialog(
    isMusic: Boolean = false,
    onTimerSet: (minutes: Int, finishCurrent: Boolean, afterCurrentSong: Boolean) -> Unit,
    onCancel: () -> Unit,
    onDismiss: () -> Unit
) {
    val options = listOf(5, 10, 15, 30, 45, 60, 90, 120)
    var finishCurrent by remember { mutableStateOf(true) }
    var customMinutes by remember { mutableStateOf(30) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF1A1A2E),
        titleContentColor = Color.White,
        textContentColor = Color.White,
        title = {
            Text("Sleep Timer", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                // After current song (music only)
                if (isMusic) {
                    Button(
                        onClick = { onTimerSet(0, true, true); onDismiss() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2A2A4A)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("🎵 After current song ends", color = Color.White, fontSize = 14.sp)
                    }
                    HorizontalDivider(color = Color.White.copy(alpha = 0.1f))
                }

                // Quick pick chips
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    options.forEach { min ->
                        FilterChip(
                            selected = false,
                            onClick = { onTimerSet(min, finishCurrent, false); onDismiss() },
                            label = {
                                Text(if (min < 60) "${min}m" else "${min / 60}h${if (min % 60 > 0) "${min % 60}m" else ""}")
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                containerColor = Color(0xFF2A2A4A),
                                labelColor = Color.White
                            )
                        )
                    }
                }

                // Finish current toggle
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        if (isMusic) "Finish current song" else "Finish current video",
                        modifier = Modifier.weight(1f),
                        fontSize = 14.sp,
                        color = Color.White
                    )
                    Switch(
                        checked = finishCurrent,
                        onCheckedChange = { finishCurrent = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFFB388FF))
                    )
                }

                // Custom slider
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Custom:", fontSize = 14.sp, color = Color.White)
                    Slider(
                        value = customMinutes.toFloat(),
                        onValueChange = { customMinutes = it.toInt() },
                        valueRange = 1f..180f,
                        modifier = Modifier.weight(1f).padding(horizontal = 8.dp),
                        colors = SliderDefaults.colors(
                            thumbColor = Color(0xFFB388FF),
                            activeTrackColor = Color(0xFFB388FF)
                        )
                    )
                    Text("${customMinutes}m", fontSize = 14.sp, color = Color.White, modifier = Modifier.width(40.dp))
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onTimerSet(customMinutes, finishCurrent, false); onDismiss() }) {
                Text("Set ${customMinutes}m", color = Color(0xFFB388FF))
            }
        },
        dismissButton = {
            Row {
                TextButton(onClick = { onCancel(); onDismiss() }) {
                    Text("Cancel Timer", color = Color.Red.copy(alpha = 0.8f))
                }
                TextButton(onClick = onDismiss) {
                    Text("Close", color = Color.Gray)
                }
            }
        }
    )
}
