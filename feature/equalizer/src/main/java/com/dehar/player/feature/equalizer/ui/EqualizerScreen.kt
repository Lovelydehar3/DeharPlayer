package com.dehar.player.feature.equalizer.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class EqPreset(
    val id: Long,
    val name: String,
    val bands: List<Int>,
    val isSystem: Boolean = false
)

data class EqBand(
    val frequency: String,
    val value: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EqualizerScreen(
    onNavigateBack: () -> Unit,
) {
    var eqEnabled by remember { mutableStateOf(false) }
    var selectedPreset by remember { mutableStateOf<EqPreset?>(null) }

    val presets = listOf(
        EqPreset(1, "Flat", listOf(0, 0, 0, 0, 0), isSystem = true),
        EqPreset(2, "Bass Boost", listOf(10, 5, 0, -5, -10), isSystem = true),
        EqPreset(3, "Treble Boost", listOf(-10, -5, 0, 5, 10), isSystem = true),
        EqPreset(4, "Vocal Boost", listOf(-5, 0, 10, 5, -10), isSystem = true),
    )

    val bands = listOf(
        EqBand("60 Hz", 0),
        EqBand("230 Hz", 0),
        EqBand("910 Hz", 0),
        EqBand("3 kHz", 0),
        EqBand("14 kHz", 0)
    )

    var currentBands by remember { mutableStateOf(bands) }
    var bassBoost by remember { mutableStateOf(0) }
    var virtualizer by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Equalizer") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            item {
                // Enable/Disable
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Equalizer", style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
                    Switch(
                        checked = eqEnabled,
                        onCheckedChange = { eqEnabled = it }
                    )
                }
            }

            // EQ Bands
            item {
                Card(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("5-Band EQ", style = MaterialTheme.typography.titleSmall)
                        Spacer(modifier = Modifier.height(16.dp))

                        currentBands.forEachIndexed { index, band ->
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(band.frequency, modifier = Modifier.width(60.dp))
                                    Slider(
                                        value = band.value.toFloat(),
                                        onValueChange = {
                                            val newBands = currentBands.toMutableList()
                                            newBands[index] = band.copy(value = it.toInt())
                                            currentBands = newBands
                                        },
                                        valueRange = -15f..15f,
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(horizontal = 8.dp),
                                        steps = 29
                                    )
                                    Text("${band.value}dB", modifier = Modifier.width(60.dp))
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }
            }

            // Audio Effects
            item {
                Card(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Audio Effects", style = MaterialTheme.typography.titleSmall)
                        Spacer(modifier = Modifier.height(16.dp))

                        // Bass Boost
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Bass Boost", modifier = Modifier.width(80.dp))
                                Slider(
                                    value = bassBoost.toFloat(),
                                    onValueChange = { bassBoost = it.toInt() },
                                    valueRange = 0f..100f,
                                    modifier = Modifier.weight(1f),
                                    steps = 99
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        // Virtualizer
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Virtualizer", modifier = Modifier.width(80.dp))
                                Slider(
                                    value = virtualizer.toFloat(),
                                    onValueChange = { virtualizer = it.toInt() },
                                    valueRange = 0f..100f,
                                    modifier = Modifier.weight(1f),
                                    steps = 99
                                )
                            }
                        }
                    }
                }
            }

            // Presets
            item {
                Text("Presets", style = MaterialTheme.typography.titleSmall, modifier = Modifier.padding(bottom = 8.dp))
            }

            items(presets) { preset ->
                PresetCard(
                    preset = preset,
                    isSelected = selectedPreset?.id == preset.id,
                    onClick = { selectedPreset = preset }
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun PresetCard(
    preset: EqPreset,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = preset.name,
                    style = MaterialTheme.typography.bodyMedium
                )
                if (preset.isSystem) {
                    Text(
                        text = "System Preset",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            if (isSelected) {
                Text("✓", color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}
