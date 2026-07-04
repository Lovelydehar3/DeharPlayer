package com.dehar.player.feature.mediamanager

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dehar.player.core.data.database.DeharDatabase
import com.dehar.player.core.data.model.RecycleBinEntity
import com.dehar.player.core.common.TimeUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaManagerScreen(
    initialTab: String? = null,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val db = remember { DeharDatabase.getDatabase(context) }
    val recycleBinDao = db.recycleBinDao()
    
    val deletedItems by recycleBinDao.getAllDeletedItems().collectAsState(initial = emptyList())
    
    var selectedTab by remember { mutableIntStateOf(if (initialTab == "recycle_bin") 0 else 0) }
    val tabs = listOf("Recycle Bin")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Media Manager") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            TabRow(selectedTabIndex = selectedTab) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title) }
                    )
                }
            }

            when (selectedTab) {
                0 -> RecycleBinTab(
                    items = deletedItems,
                    onRestore = { item ->
                        scope.launch {
                            restoreItem(context, item, recycleBinDao)
                        }
                    },
                    onDelete = { item ->
                        scope.launch {
                            deletePermanently(context, item, recycleBinDao)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun RecycleBinTab(
    items: List<RecycleBinEntity>,
    onRestore: (RecycleBinEntity) -> Unit,
    onDelete: (RecycleBinEntity) -> Unit
) {
    if (items.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Recycle Bin is empty", color = Color.Gray)
        }
    } else {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(items, key = { it.id }) { item ->
                ListItem(
                    headlineContent = { Text(item.originalName, color = Color.White) },
                    supportingContent = {
                        Text(
                            "Deleted ${TimeUtils.formatDuration(System.currentTimeMillis() - item.deletedAt)} ago • ${formatSize(item.size)}",
                            color = Color.Gray,
                            fontSize = 12.sp
                        )
                    },
                    trailingContent = {
                        Row {
                            IconButton(onClick = { onRestore(item) }) {
                                Icon(Icons.Default.Restore, contentDescription = "Restore", tint = Color.Green)
                            }
                            IconButton(onClick = { onDelete(item) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete Permanently", tint = Color.Red)
                            }
                        }
                    },
                    colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                )
                HorizontalDivider(color = Color.White.copy(alpha = 0.1f))
            }
        }
    }
}

private suspend fun restoreItem(context: android.content.Context, item: RecycleBinEntity, dao: com.dehar.player.core.data.database.RecycleBinDao) {
    withContext(Dispatchers.IO) {
        val recycleBinDir = File(context.filesDir, ".recycle_bin")
        val recycledFile = File(recycleBinDir, item.id)
        val restoreFile = File(item.originalPath)
        
        if (recycledFile.exists()) {
            restoreFile.parentFile?.mkdirs()
            if (recycledFile.renameTo(restoreFile)) {
                dao.deleteDeletedItem(item)
            }
        }
    }
}

private suspend fun deletePermanently(context: android.content.Context, item: RecycleBinEntity, dao: com.dehar.player.core.data.database.RecycleBinDao) {
    withContext(Dispatchers.IO) {
        val recycleBinDir = File(context.filesDir, ".recycle_bin")
        val recycledFile = File(recycleBinDir, item.id)
        
        if (recycledFile.exists()) {
            recycledFile.delete()
        }
        dao.deleteDeletedItem(item)
    }
}

private fun formatSize(size: Long): String {
    return when {
        size >= 1024 * 1024 * 1024 -> String.format("%.2f GB", size.toDouble() / (1024 * 1024 * 1024))
        size >= 1024 * 1024 -> String.format("%.2f MB", size.toDouble() / (1024 * 1024))
        else -> String.format("%.2f KB", size.toDouble() / 1024)
    }
}
