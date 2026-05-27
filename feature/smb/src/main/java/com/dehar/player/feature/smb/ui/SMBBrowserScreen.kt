package com.dehar.player.feature.smb.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dehar.player.feature.smb.model.SMBConnectionState
import com.dehar.player.feature.smb.model.SMBCredentials
import com.dehar.player.feature.smb.viewmodel.SMBBrowserViewModel

/**
 * Main SMB browser screen
 */
@Composable
fun SMBBrowserScreen(
    onNavigateBack: () -> Unit,
    onFileSelected: (String) -> Unit,
    viewModel: SMBBrowserViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("SMB Browser") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        when {
            uiState.selectedShare != null -> {
                // File browser view
                FileBrowserView(
                    uiState = uiState,
                    onFileClick = { file ->
                        if (file.isDirectory) {
                            viewModel.navigateToFolder(file.path, file.name)
                        } else {
                            onFileSelected(file.path)
                        }
                    },
                    onDownloadClick = { file ->
                        // TODO: Implement download
                    },
                    onBackClick = {
                        if (uiState.browserState.canGoBack()) {
                            viewModel.navigateBack()
                        } else {
                            viewModel.uiState.value
                        }
                    },
                    onBreadcrumbClick = { index ->
                        // TODO: Implement breadcrumb navigation
                    },
                    modifier = Modifier.padding(paddingValues)
                )
            }
            uiState.selectedServer != null -> {
                // Share browser view
                ShareBrowserView(
                    uiState = uiState,
                    onShareClick = { viewModel.selectShare(it) },
                    onBackClick = {
                        viewModel.uiState.value = uiState.copy(
                            selectedServer = null,
                            shares = emptyList()
                        )
                    },
                    modifier = Modifier.padding(paddingValues)
                )
            }
            else -> {
                // Server browser view
                ServerBrowserView(
                    uiState = uiState,
                    onServerClick = { viewModel.connectToServer(it) },
                    onAddServerClick = { /* TODO: Show add server dialog */ },
                    onDiscoverClick = { viewModel.discoverServers() },
                    onNetworkDiscoveryToggle = { viewModel.toggleNetworkDiscovery(it) },
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
    }

    // Show error snackbar
    if (uiState.errorMessage != null) {
        androidx.compose.material3.LaunchedEffect(uiState.errorMessage) {
            snackbarHostState.showSnackbar(uiState.errorMessage!!)
            viewModel.clearErrorMessage()
        }
    }
}

/**
 * Server browser view
 */
@Composable
fun ServerBrowserView(
    uiState: com.dehar.player.feature.smb.model.SMBBrowserUiState,
    onServerClick: (com.dehar.player.feature.smb.model.SMBServer) -> Unit,
    onAddServerClick: () -> Unit,
    onDiscoverClick: () -> Unit,
    onNetworkDiscoveryToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Network discovery toggle
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Wifi,
                    contentDescription = "Network discovery"
                )
                Text("Network Discovery")
            }

            Switch(
                checked = uiState.enableNetworkDiscovery,
                onCheckedChange = onNetworkDiscoveryToggle
            )
        }

        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (uiState.servers.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        "No servers found",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Add a server manually or enable network discovery",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Button(onClick = onAddServerClick) {
                        Icon(Icons.Default.Add, contentDescription = "Add")
                        Text("Add Server")
                    }

                    if (!uiState.enableNetworkDiscovery) {
                        Button(onClick = onDiscoverClick) {
                            Text("Discover Servers")
                        }
                    }
                }
            }
        } else {
            ServerList(
                servers = uiState.servers,
                onServerClick = onServerClick,
                onMoreClick = { /* TODO: Show context menu */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .weight(1f)
            )
        }
    }
}

/**
 * Share browser view
 */
@Composable
fun ShareBrowserView(
    uiState: com.dehar.player.feature.smb.model.SMBBrowserUiState,
    onShareClick: (com.dehar.player.feature.smb.model.SMBShare) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    uiState.selectedServer?.name ?: "Unknown",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    uiState.selectedServer?.address ?: "",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
        }

        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (uiState.shares.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "No shares available",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        } else {
            ShareList(
                shares = uiState.shares,
                onShareClick = onShareClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .weight(1f)
            )
        }
    }
}

/**
 * File browser view
 */
@Composable
fun FileBrowserView(
    uiState: com.dehar.player.feature.smb.model.SMBBrowserUiState,
    onFileClick: (com.dehar.player.feature.smb.model.SMBFileEntry) -> Unit,
    onDownloadClick: (com.dehar.player.feature.smb.model.SMBFileEntry) -> Unit,
    onBackClick: () -> Unit,
    onBreadcrumbClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        // Breadcrumb navigation
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }

            BreadcrumbNavigation(
                breadcrumbs = uiState.browserState.breadcrumbs,
                onBreadcrumbClick = onBreadcrumbClick,
                modifier = Modifier.weight(1f)
            )
        }

        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (uiState.files.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "No files available",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        } else {
            FileList(
                files = uiState.files,
                onItemClick = onFileClick,
                onDownloadClick = onDownloadClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .weight(1f)
            )
        }
    }
}
