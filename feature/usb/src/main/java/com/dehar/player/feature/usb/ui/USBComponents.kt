package com.dehar.player.feature.usb.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.InsertDriveFile
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.overflow.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

/**
 * USB device item
 */
@Composable
fun USBDeviceItem(
    device: com.dehar.player.feature.usb.model.USBDevice,
    onDeviceClick: (com.dehar.player.feature.usb.model.USBDevice) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .clickable { onDeviceClick(device) }
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Header with icon and name
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Storage,
                contentDescription = "USB Device",
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = device.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = formatFileSize(device.availableSpace) + " available",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Storage usage bar
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            LinearProgressIndicator(
                progress = device.getUsagePercentage() / 100f,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = formatFileSize(device.usedSpace) + " / " + formatFileSize(device.totalSpace),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "${device.getUsagePercentage().roundToInt()}%",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

/**
 * USB file item
 */
@Composable
fun USBFileItem(
    file: com.dehar.player.feature.usb.model.USBFileEntry,
    onItemClick: (com.dehar.player.feature.usb.model.USBFileEntry) -> Unit,
    onCopyClick: (com.dehar.player.feature.usb.model.USBFileEntry) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .clickable { onItemClick(file) }
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if (file.isDirectory) Icons.Default.Folder else Icons.Default.InsertDriveFile,
            contentDescription = if (file.isDirectory) "Folder" else "File",
            modifier = Modifier.size(32.dp),
            tint = if (file.isDirectory) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            }
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = file.name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (!file.isDirectory) {
                Text(
                    text = formatFileSize(file.size),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        if (!file.isDirectory && file.canRead) {
            IconButton(
                onClick = { onCopyClick(file) },
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Download,
                    contentDescription = "Copy",
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

/**
 * USB device list
 */
@Composable
fun USBDeviceList(
    devices: List<com.dehar.player.feature.usb.model.USBDevice>,
    onDeviceClick: (com.dehar.player.feature.usb.model.USBDevice) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(devices) { device ->
            USBDeviceItem(
                device = device,
                onDeviceClick = onDeviceClick
            )
        }
    }
}

/**
 * USB file list
 */
@Composable
fun USBFileList(
    files: List<com.dehar.player.feature.usb.model.USBFileEntry>,
    onItemClick: (com.dehar.player.feature.usb.model.USBFileEntry) -> Unit,
    onCopyClick: (com.dehar.player.feature.usb.model.USBFileEntry) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(files) { file ->
            USBFileItem(
                file = file,
                onItemClick = onItemClick,
                onCopyClick = onCopyClick
            )
        }
    }
}

/**
 * USB breadcrumb navigation
 */
@Composable
fun USBBreadcrumbNavigation(
    breadcrumbs: List<String>,
    onBreadcrumbClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    if (breadcrumbs.isEmpty()) return

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        breadcrumbs.forEachIndexed { index, breadcrumb ->
            Text(
                text = breadcrumb,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.clickable { onBreadcrumbClick(index) },
                color = if (index == breadcrumbs.size - 1) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
                fontSize = 10.sp
            )

            if (index < breadcrumbs.size - 1) {
                Text(
                    text = "/",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 10.sp
                )
            }
        }
    }
}

/**
 * Format file size for display
 */
fun formatFileSize(bytes: Long): String {
    return when {
        bytes < 1024 -> "$bytes B"
        bytes < 1024 * 1024 -> "${(bytes / 1024.0).roundToInt()} KB"
        bytes < 1024 * 1024 * 1024 -> "${(bytes / (1024.0 * 1024)).roundToInt()} MB"
        else -> "${(bytes / (1024.0 * 1024 * 1024)).roundToInt()} GB"
    }
}
