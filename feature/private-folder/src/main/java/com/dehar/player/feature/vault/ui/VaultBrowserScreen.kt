package com.dehar.player.feature.vault.ui

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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dehar.player.feature.vault.model.VaultAuthState
import com.dehar.player.feature.vault.viewmodel.VaultViewModel

/**
 * Main vault browser screen
 */
@Composable
fun VaultBrowserScreen(
    onNavigateBack: () -> Unit,
    viewModel: VaultViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

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
                title = { 
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = if (uiState.authState == VaultAuthState.UNLOCKED) {
                                Icons.Default.LockOpen
                            } else {
                                Icons.Default.Lock
                            },
                            contentDescription = "Vault"
                        )
                        Text("Private Vault")
                    }
                },
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
        when (uiState.authState) {
            VaultAuthState.LOCKED -> {
                VaultLockedView(
                    biometricAvailable = uiState.biometricAvailable,
                    onUnlockBiometric = { viewModel.unlockWithBiometric() },
                    onUnlockPasscode = { viewModel.requestVaultUnlock() },
                    modifier = Modifier.padding(paddingValues)
                )
            }
            VaultAuthState.AUTHENTICATING -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            VaultAuthState.AUTHENTICATION_FAILED -> {
                VaultAuthFailedView(
                    errorMessage = uiState.errorMessage ?: "Authentication failed",
                    onRetry = { viewModel.requestVaultUnlock() },
                    modifier = Modifier.padding(paddingValues)
                )
            }
            VaultAuthState.UNLOCKED -> {
                VaultContentView(
                    uiState = uiState,
                    onLock = { viewModel.lockVault() },
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
    }

    // Auth dialog
    if (uiState.showAuthDialog) {
        VaultAuthDialog(
            biometricAvailable = uiState.biometricAvailable,
            onPasscodeSubmit = { viewModel.unlockWithPasscode(it) },
            onBiometricClick = { viewModel.unlockWithBiometric() },
            onDismiss = { viewModel.hideAuthDialog() }
        )
    }
}

/**
 * Vault locked view
 */
@Composable
fun VaultLockedView(
    biometricAvailable: Boolean,
    onUnlockBiometric: () -> Unit,
    onUnlockPasscode: () -> Unit,
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
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = "Vault Locked",
                modifier = Modifier.fillMaxWidth(0.3f),
                tint = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "Private Vault",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Your encrypted content is protected",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (biometricAvailable) {
                Button(
                    onClick = onUnlockBiometric,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Unlock with Biometric")
                }
            }

            Button(
                onClick = onUnlockPasscode,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Unlock with Passcode")
            }
        }
    }
}

/**
 * Vault auth failed view
 */
@Composable
fun VaultAuthFailedView(
    errorMessage: String,
    onRetry: () -> Unit,
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Authentication Failed",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = errorMessage,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Button(onClick = onRetry) {
                Text("Try Again")
            }
        }
    }
}

/**
 * Vault content view
 */
@Composable
fun VaultContentView(
    uiState: com.dehar.player.feature.vault.model.VaultUiState,
    onLock: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Vault info
        VaultInfoCard(
            vaultSize = uiState.vaultSize,
            fileCount = uiState.files.size
        )

        // Files
        if (uiState.files.isNotEmpty()) {
            Text(
                text = "Encrypted Files (${uiState.files.size})",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            VaultFileList(
                files = uiState.files,
                onFileClick = { /* TODO: Show file options */ },
                onExtractClick = { /* TODO: Extract file */ },
                onDeleteClick = { /* TODO: Delete file */ }
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No files in vault",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Button(
            onClick = onLock,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Lock, contentDescription = "Lock")
            Text("Lock Vault")
        }
    }
}

/**
 * Vault authentication dialog
 */
@Composable
fun VaultAuthDialog(
    biometricAvailable: Boolean,
    onPasscodeSubmit: (String) -> Unit,
    onBiometricClick: () -> Unit,
    onDismiss: () -> Unit
) {
    var passcode by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Unlock Private Vault") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (biometricAvailable) {
                    Button(
                        onClick = onBiometricClick,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Use Biometric")
                    }
                }

                Text("Or enter passcode:", style = MaterialTheme.typography.labelSmall)

                OutlinedTextField(
                    value = passcode,
                    onValueChange = { passcode = it },
                    label = { Text("Passcode") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(onClick = { onPasscodeSubmit(passcode) }) {
                Text("Unlock")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
