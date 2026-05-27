package com.dehar.player.feature.videoeditor.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dehar.player.feature.videoeditor.model.ExportQuality
import com.dehar.player.feature.videoeditor.model.ExportSettings
import com.dehar.player.feature.videoeditor.model.VideoEditState
import com.dehar.player.feature.videoeditor.model.VideoFormat
import com.dehar.player.feature.videoeditor.ui.components.ClipItem
import com.dehar.player.feature.videoeditor.ui.components.EncodingProgressCard
import com.dehar.player.feature.videoeditor.ui.components.FilterSelector
import com.dehar.player.feature.videoeditor.ui.components.QualitySelector
import com.dehar.player.feature.videoeditor.ui.components.TimelineSlider
import com.dehar.player.feature.videoeditor.ui.components.VideoInfoCard
import com.dehar.player.feature.videoeditor.viewmodel.VideoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoEditorScreen(
    viewModel: VideoViewModel,
    videoPath: String? = null,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    var selectedTab by remember { mutableStateOf(0) }
    var exportQuality by remember { mutableStateOf(ExportQuality.HIGH) }
    var startMs by remember { mutableStateOf(0L) }
    var endMs by remember { mutableStateOf(0L) }

    if (videoPath != null && uiState.project == null) {
        viewModel.loadVideo(videoPath)
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = uiState.project?.projectName ?: "Video Editor",
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        if (uiState.isLoading) {
            LoadingScreen(modifier = Modifier.padding(paddingValues))
        } else if (uiState.project == null) {
            NoProjectScreen(modifier = Modifier.padding(paddingValues))
        } else {
            val project = uiState.project!!

            // Update end time when video loads
            if (endMs == 0L) {
                endMs = project.videoDuration
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
            ) {
                when {
                    uiState.editState == VideoEditState.ENCODING && uiState.encodingProgress != null -> {
                        EncodingProgressCard(
                            progress = uiState.encodingProgress!!,
                            fileName = project.projectName
                        )
                    }
                    uiState.editState == VideoEditState.SUCCESS -> {
                        SuccessCard(
                            outputPath = uiState.outputPath ?: "",
                            onReset = { viewModel.reset() }
                        )
                    }
                    else -> {
                        VideoInfoCard(project)

                        // Tab selection
                        TabRow(selectedTabIndex = selectedTab) {
                            Tab(
                                selected = selectedTab == 0,
                                onClick = { selectedTab = 0 },
                                text = { Text("Trim") }
                            )
                            Tab(
                                selected = selectedTab == 1,
                                onClick = { selectedTab = 1 },
                                text = { Text("Filters") }
                            )
                            Tab(
                                selected = selectedTab == 2,
                                onClick = { selectedTab = 2 },
                                text = { Text("Advanced") }
                            )
                        }

                        when (selectedTab) {
                            0 -> {
                                TrimTab(
                                    project = project,
                                    startMs = startMs,
                                    endMs = endMs,
                                    onStartChange = { startMs = it },
                                    onEndChange = { endMs = it },
                                    exportQuality = exportQuality,
                                    onQualityChange = { exportQuality = it },
                                    onExport = {
                                        viewModel.trimAndExport(
                                            startMs,
                                            endMs,
                                            ExportSettings(quality = exportQuality)
                                        )
                                    }
                                )
                            }
                            1 -> {
                                FiltersTab(
                                    filters = uiState.filters,
                                    onFilterAdd = { viewModel.addFilter(it) },
                                    onFilterRemove = { viewModel.removeFilter(it) },
                                    exportQuality = exportQuality,
                                    onQualityChange = { exportQuality = it },
                                    onExport = {
                                        viewModel.applyFiltersAndExport(
                                            uiState.filters,
                                            ExportSettings(quality = exportQuality)
                                        )
                                    }
                                )
                            }
                            2 -> {
                                AdvancedTab(
                                    project = project,
                                    clips = uiState.clips,
                                    selectedClipId = uiState.selectedClip?.id,
                                    onClipSelect = { viewModel.selectClip(it) },
                                    onClipDelete = { viewModel.deleteClip(it) },
                                    onRotate = { viewModel.rotateVideo(it) },
                                    onSpeedChange = { viewModel.changeSpeed(it) }
                                )
                            }
                        }
                    }
                }
            }
        }

        // Error dialog
        if (uiState.error != null) {
            AlertDialog(
                onDismissRequest = { viewModel.clearError() },
                icon = { Icon(Icons.Default.Error, contentDescription = null) },
                title = { Text("Error") },
                text = { Text(uiState.error!!) },
                confirmButton = {
                    TextButton(onClick = { viewModel.clearError() }) {
                        Text("OK")
                    }
                }
            )
        }
    }
}

@Composable
private fun TrimTab(
    project: com.dehar.player.feature.videoeditor.model.VideoEditProject,
    startMs: Long,
    endMs: Long,
    onStartChange: (Long) -> Unit,
    onEndChange: (Long) -> Unit,
    exportQuality: ExportQuality,
    onQualityChange: (ExportQuality) -> Unit,
    onExport: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TimelineSlider(
            duration = project.videoDuration,
            startMs = startMs,
            endMs = endMs,
            onStartChange = onStartChange,
            onEndChange = onEndChange
        )

        QualitySelector(
            selectedQuality = exportQuality,
            onQualitySelect = onQualityChange
        )

        Button(
            onClick = onExport,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Export Trimmed Video")
        }
    }
}

@Composable
private fun FiltersTab(
    filters: List<com.dehar.player.feature.videoeditor.model.VideoFilter>,
    onFilterAdd: (com.dehar.player.feature.videoeditor.model.VideoFilter) -> Unit,
    onFilterRemove: (String) -> Unit,
    exportQuality: ExportQuality,
    onQualityChange: (ExportQuality) -> Unit,
    onExport: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        FilterSelector(
            selectedFilters = filters,
            onFilterAdd = onFilterAdd,
            onFilterRemove = onFilterRemove
        )

        if (filters.isNotEmpty()) {
            Text(
                text = "Applied Filters: ${filters.size}",
                style = MaterialTheme.typography.labelMedium
            )
        }

        QualitySelector(
            selectedQuality = exportQuality,
            onQualitySelect = onQualityChange
        )

        Button(
            onClick = onExport,
            enabled = filters.isNotEmpty(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Apply Filters & Export")
        }
    }
}

@Composable
private fun AdvancedTab(
    project: com.dehar.player.feature.videoeditor.model.VideoEditProject,
    clips: List<com.dehar.player.feature.videoeditor.model.VideoClip>,
    selectedClipId: String?,
    onClipSelect: (String) -> Unit,
    onClipDelete: (String) -> Unit,
    onRotate: (Int) -> Unit,
    onSpeedChange: (Float) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Rotation",
            style = MaterialTheme.typography.labelMedium
        )
        androidx.compose.foundation.layout.Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { onRotate(90) },
                modifier = Modifier.weight(1f)
            ) {
                Text("90°")
            }
            Button(
                onClick = { onRotate(180) },
                modifier = Modifier.weight(1f)
            ) {
                Text("180°")
            }
            Button(
                onClick = { onRotate(270) },
                modifier = Modifier.weight(1f)
            ) {
                Text("270°")
            }
        }

        Text(
            text = "Playback Speed",
            style = MaterialTheme.typography.labelMedium
        )
        androidx.compose.foundation.layout.Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf(0.5f, 1.0f, 1.5f, 2.0f).forEach { speed ->
                Button(
                    onClick = { onSpeedChange(speed) },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("${speed}x")
                }
            }
        }

        if (clips.isNotEmpty()) {
            Text(
                text = "Clips (${clips.size})",
                style = MaterialTheme.typography.labelMedium
            )
            clips.forEach { clip ->
                ClipItem(
                    clip = clip,
                    isSelected = clip.id == selectedClipId,
                    onSelect = { onClipSelect(clip.id) },
                    onDelete = { onClipDelete(clip.id) }
                )
            }
        }
    }
}

@Composable
private fun LoadingScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Loading video...",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun NoProjectScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "No video selected",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = "Select a video to start editing",
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
private fun SuccessCard(
    outputPath: String,
    onReset: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .fillMaxWidth()
        )
        Text(
            text = "Export Successful!",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = outputPath,
            style = MaterialTheme.typography.labelSmall
        )
        Button(onClick = onReset) {
            Text("Edit Another Video")
        }
    }
}
