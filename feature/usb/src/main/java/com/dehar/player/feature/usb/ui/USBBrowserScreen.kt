package com.dehar.player.feature.usb.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dehar.player.feature.usb.model.USBPermissionState
import com.dehar.player.feature.usb.viewmodel.USBBrowserViewModel

/**
 * Main USB browser screen
 */
@Composable
fun USBBrowserScreen(
    onNavigateBack: () -> Unit,
    onFileSelected: (String) -> Unit,
    viewModel: USBBrowserViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Request permission on first load
    LaunchedEffect(Unit) {
        if (uiState.permissionState == USBPermissionState.NOT_REQUESTED) {
            viewModel.requestUSBPermission()
        }
    }

    // Show error snackbar
    LaunchedEffect(uiState.errorMessage) {
        if (uiState.errorMessage != null) {
            snackbarHostState.showSnackbar(uiState.errorMessage!!)
            viewModel.clearErrorMessage()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("USB Storage") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (uiState.selectedDevice != null) {
                        IconButton(onClick = { viewModel.scanForDevices() }) {
                            Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                        }
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
            uiState.showPermissionRequest -> {
                PermissionRequestView(
                    onAllow = { viewModel.grantUSBPermission() },
                    onDeny = { viewModel.denyUSBPermission() },
                    modifier = Modifier.padding(paddingValues)
                )
            }
            uiState.permissionState == USBPermissionState.DENIED -> {
                PermissionDeniedView(
                    onNavigateBack = onNavigateBack,
                    modifier = Modifier.padding(paddingValues)
                )
            }
            uiState.selectedDevice != null -> {
                FileBrowserView(
                    uiState = uiState,
                    onFileClick = { file ->
                        if (file.isDirectory) {
                            viewModel.navigateToFolder(file.path, file.name)
                        } else {
                            onFileSelected(file.path)
                        }
                    },
                    onCopyClick = { file ->
                        // TODO: Implement copy functionality
                    },
                    onBackClick = {
                        if (uiState.browserState.canGoBack()) {
                            viewModel.navigateBack()
                        } else {
                            viewModel.exitDeviceBrowser()
                        }
                    },
                    onBreadcrumbClick = { index ->
                        // TODO: Implement breadcrumb navigation
                    },
                    modifier = Modifier.padding(paddingValues)
                )
            }
            else -> {
                DeviceBrowserView(
                    uiState = uiState,
                    onDeviceClick = { viewModel.selectDevice(it) },
                    onRefreshClick = { viewModel.scanForDevices() },
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
    }
}

/**
 * Permission request view
 */
@Composable
fun PermissionRequestView(
    onAllow: () -> Unit,
    onDeny: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = "Access USB Storage",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Allow Dehar Player to access USB storage devices connected to your device?",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = onDeny,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Deny")
                }

                Button(
                    onClick = onAllow,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Allow")
                }
            }
        }
    }
}

/**
 * Permission denied view
 */
@Composable
fun PermissionDeniedView(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = "Permission Denied",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "USB storage access is required to browse connected devices. Please grant permission in app settings.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Button(onClick = onNavigateBack) {
                Text("Go Back")
            }
        }
    }
}

/**
 * Device browser view
 */
@Composable
fun DeviceBrowserView(
    uiState: com.dehar.player.feature.usb.model.USBBrowserUiState,
    onDeviceClick: (com.dehar.player.feature.usb.model.USBDevice) -> Unit,
    onRefreshClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (uiState.devices.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    Text(
                        text = "No USB Devices Found",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Connect a USB drive or SD card to browse files",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Button(onClick = onRefreshClick) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                        Text("Refresh")
                    }
                }
            }
        } else {
            USBDeviceList(
                devices = uiState.devices,
                onDeviceClick = onDeviceClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    }
}

/**
 * File browser view
 */
@Composable
fun FileBrowserView(
    uiState: com.dehar.player.feature.usb.model.USBBrowserUiState,
    onFileClick: (com.dehar.player.feature.usb.model.USBFileEntry) -> Unit,
    onCopyClick: (com.dehar.player.feature.usb.model.USBFileEntry) -> Unit,
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

            USBBreadcrumbNavigation(
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
                    text = "No files in this folder",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        } else {
            USBFileList(
                files = uiState.files,
                onItemClick = onFileClick,
                onCopyClick = onCopyClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .weight(1f)
            )
        }
    }
}
