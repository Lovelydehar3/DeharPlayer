package com.dehar.player.feature.whatsappstatus.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dehar.player.feature.whatsappstatus.model.MonitorState
import com.dehar.player.feature.whatsappstatus.ui.components.DownloadProgressCard
import com.dehar.player.feature.whatsappstatus.ui.components.MonitoringIndicator
import com.dehar.player.feature.whatsappstatus.ui.components.StatusContactItem
import com.dehar.player.feature.whatsappstatus.ui.components.StatusFileItem
import com.dehar.player.feature.whatsappstatus.ui.components.StatusFileList
import com.dehar.player.feature.whatsappstatus.ui.components.formatFileSize
import com.dehar.player.feature.whatsappstatus.viewmodel.StatusViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatusBrowserScreen(
    viewModel: StatusViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("WhatsApp Status Downloader")
                        if (uiState.statuses.isNotEmpty()) {
                            Text(
                                text = "${uiState.statuses.size} statuses found",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.scanForStatuses() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        if (uiState.isLoading) {
            LoadingState(modifier = Modifier.padding(paddingValues))
        } else if (!viewModel.isStatusFolderAccessible()) {
            NoAccessState(modifier = Modifier.padding(paddingValues))
        } else if (uiState.statuses.isEmpty()) {
            EmptyState(
                onRefresh = { viewModel.scanForStatuses() },
                modifier = Modifier.padding(paddingValues)
            )
        } else if (uiState.selectedStatus != null) {
            StatusDetailView(
                status = uiState.selectedStatus!!,
                isDownloading = uiState.selectedStatus!!.id in uiState.downloadProgress.keys,
                onDownload = { viewModel.downloadStatus(uiState.selectedStatus!!) },
                onBack = { viewModel.clearStatusSelection() },
                modifier = Modifier.padding(paddingValues)
            )
        } else {
            StatusListView(
                contacts = uiState.contacts,
                statuses = uiState.statuses,
                downloadingStatusIds = uiState.downloadProgress.keys,
                monitorState = uiState.monitorState,
                onContactSelect = { contact ->
                    // Filter statuses for selected contact
                },
                onStatusSelect = { viewModel.selectStatus(it) },
                onDownload = { viewModel.downloadStatus(it) },
                onDownloadAll = { viewModel.downloadMultipleStatuses(uiState.statuses) },
                onDelete = { viewModel.deleteDownloadedStatus(it) },
                onMonitoringToggle = { viewModel.toggleMonitoring(it) },
                downloadProgress = uiState.downloadProgress,
                modifier = Modifier.padding(paddingValues)
            )
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
private fun LoadingState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        Text(
            text = "Scanning for statuses...",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}

@Composable
private fun NoAccessState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Error,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Text(
            text = "Cannot Access WhatsApp Status Folder",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Please ensure you have read permissions for WhatsApp storage",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 16.dp)
        )
    }
}

@Composable
private fun EmptyState(
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "No Statuses Found",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "No WhatsApp statuses detected. Check back when new statuses are available.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        TextButton(onClick = onRefresh) {
            Text("Refresh")
        }
    }
}

@Composable
private fun StatusDetailView(
    status: com.dehar.player.feature.whatsappstatus.model.StatusFile,
    isDownloading: Boolean,
    onDownload: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.Close, contentDescription = "Back")
            }
            Text(
                text = status.name,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f)
            )
        }

        // File info
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            InfoRow("Size:", formatFileSize(status.size))
            InfoRow("Type:", status.type.name)
            InfoRow("Path:", status.path)
        }

        // Download button
        if (!status.isDownloaded) {
            TextButton(
                onClick = onDownload,
                enabled = !isDownloading,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (isDownloading) "Downloading..." else "Download Status")
            }
        } else {
            Text(
                text = "✓ Downloaded",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.tertiary
            )
        }
    }
}

@Composable
private fun StatusListView(
    contacts: List<com.dehar.player.feature.whatsappstatus.model.StatusContact>,
    statuses: List<com.dehar.player.feature.whatsappstatus.model.StatusFile>,
    downloadingStatusIds: Set<String>,
    monitorState: MonitorState,
    onContactSelect: (com.dehar.player.feature.whatsappstatus.model.StatusContact) -> Unit,
    onStatusSelect: (com.dehar.player.feature.whatsappstatus.model.StatusFile) -> Unit,
    onDownload: (com.dehar.player.feature.whatsappstatus.model.StatusFile) -> Unit,
    onDownloadAll: () -> Unit,
    onDelete: (String) -> Unit,
    onMonitoringToggle: (Boolean) -> Unit,
    downloadProgress: Map<String, com.dehar.player.feature.whatsappstatus.model.DownloadProgress>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Controls
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            MonitoringIndicator(isMonitoring = monitorState == MonitorState.MONITORING)

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Monitor",
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Switch(
                    checked = monitorState == MonitorState.MONITORING,
                    onCheckedChange = { onMonitoringToggle(it) }
                )
            }
        }

        // Download progress
        if (downloadProgress.isNotEmpty()) {
            Text(
                text = "Downloading...",
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                downloadProgress.forEach { (statusId, progress) ->
                    DownloadProgressCard(
                        statusId = statusId,
                        fileName = statuses.find { it.id == statusId }?.name ?: "Unknown",
                        progress = progress
                    )
                }
            }
        }

        // Status list
        StatusFileList(
            statuses = statuses,
            downloadingStatusIds = downloadingStatusIds,
            onDownload = onDownload,
            onDelete = onDelete,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp)
        )

        // Download all button
        TextButton(
            onClick = onDownloadAll,
            enabled = downloadProgress.isEmpty(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Download All (${statuses.size})")
        }
    }
}

@Composable
private fun InfoRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = androidx.compose.ui.text.font.FontWeight.Medium
        )
    }
}
