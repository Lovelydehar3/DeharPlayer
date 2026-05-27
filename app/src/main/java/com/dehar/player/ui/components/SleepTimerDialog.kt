package com.dehar.player.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SleepTimerDialog(
    onTimerSet: (minutes: Int, finishCurrent: Boolean) -> Unit,
    onDismiss: () -> Unit
) {
    val options = listOf(5, 10, 15, 30, 45, 60, 90, 120)
    var finishCurrent by remember { mutableStateOf(true) }
    var customMinutes by remember { mutableStateOf(30) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Sleep Timer") },
        text = {
            Column {
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    options.forEach { min ->
                        FilterChip(
                            selected = false,
                            onClick = { onTimerSet(min, finishCurrent); onDismiss() },
                            label = { Text(if (min < 60) "${min}m" else "${min/60}h${if(min%60>0) "${min%60}m" else ""}") }
                        )
                    }
                }
                Spacer(Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Finish current video", modifier = Modifier.weight(1f), fontSize = 14.sp)
                    Switch(checked = finishCurrent, onCheckedChange = { finishCurrent = it })
                }
                Spacer(Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Custom:", fontSize = 14.sp)
                    Slider(
                        value = customMinutes.toFloat(),
                        onValueChange = { customMinutes = it.toInt() },
                        valueRange = 1f..180f,
                        modifier = Modifier.weight(1f).padding(horizontal = 8.dp)
                    )
                    Text("${customMinutes}m", fontSize = 14.sp, modifier = Modifier.width(36.dp))
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onTimerSet(customMinutes, finishCurrent); onDismiss() }) {
                Text("Set ${customMinutes}m")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
