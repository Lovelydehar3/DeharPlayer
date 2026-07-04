package com.dehar.player.feature.vault.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

/**
 * Vault folder item
 */
@Composable
fun VaultFolderItem(
    folder: com.dehar.player.feature.vault.model.VaultFolder,
    onFolderClick: (com.dehar.player.feature.vault.model.VaultFolder) -> Unit,
    onMoreClick: (com.dehar.player.feature.vault.model.VaultFolder) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .clickable { onFolderClick(folder) }
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if (folder.isLocked) Icons.Default.Folder else Icons.Default.FolderOpen,
            contentDescription = "Folder",
            modifier = Modifier.size(32.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = folder.name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "${folder.itemCount} items",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        if (folder.isLocked) {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = "Locked",
                modifier = Modifier.size(20.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        IconButton(onClick = { onMoreClick(folder) }) {
            Icon(Icons.Default.MoreVert, contentDescription = "More")
        }
    }
}

/**
 * Vault file item
 */
@Composable
fun VaultFileItem(
    file: com.dehar.player.feature.vault.model.VaultFile,
    onFileClick: (com.dehar.player.feature.vault.model.VaultFile) -> Unit,
    onExtractClick: (com.dehar.player.feature.vault.model.VaultFile) -> Unit,
    onDeleteClick: (com.dehar.player.feature.vault.model.VaultFile) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .clickable { onFileClick(file) }
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Lock,
            contentDescription = "Encrypted",
            modifier = Modifier.size(32.dp),
            tint = MaterialTheme.colorScheme.tertiary
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
            Text(
                text = formatFileSize(file.size),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        IconButton(
            onClick = { onExtractClick(file) },
            modifier = Modifier.size(40.dp)
        ) {
            Icon(Icons.Default.Download, contentDescription = "Extract", modifier = Modifier.size(20.dp))
        }

        IconButton(
            onClick = { onDeleteClick(file) },
            modifier = Modifier.size(40.dp)
        ) {
            Icon(Icons.Default.Delete, contentDescription = "Delete", modifier = Modifier.size(20.dp))
        }
    }
}

/**
 * Vault folder list
 */
@Composable
fun VaultFolderList(
    folders: List<com.dehar.player.feature.vault.model.VaultFolder>,
    onFolderClick: (com.dehar.player.feature.vault.model.VaultFolder) -> Unit,
    onMoreClick: (com.dehar.player.feature.vault.model.VaultFolder) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(folders) { folder ->
            VaultFolderItem(
                folder = folder,
                onFolderClick = onFolderClick,
                onMoreClick = onMoreClick
            )
        }
    }
}

/**
 * Vault file list
 */
@Composable
fun VaultFileList(
    files: List<com.dehar.player.feature.vault.model.VaultFile>,
    onFileClick: (com.dehar.player.feature.vault.model.VaultFile) -> Unit,
    onExtractClick: (com.dehar.player.feature.vault.model.VaultFile) -> Unit,
    onDeleteClick: (com.dehar.player.feature.vault.model.VaultFile) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(files) { file ->
            VaultFileItem(
                file = file,
                onFileClick = onFileClick,
                onExtractClick = onExtractClick,
                onDeleteClick = onDeleteClick
            )
        }
    }
}

/**
 * Vault info card
 */
@Composable
fun VaultInfoCard(
    vaultSize: Long,
    fileCount: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Total Size", style = MaterialTheme.typography.labelSmall)
            Text(
                formatFileSize(vaultSize),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Files", style = MaterialTheme.typography.labelSmall)
            Text(
                fileCount.toString(),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
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
