package com.dehar.player.feature.smb.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.dehar.player.feature.smb.model.SMBBrowserState
import com.dehar.player.feature.smb.model.SMBBrowserUiState
import com.dehar.player.feature.smb.model.SMBConnectionState
import com.dehar.player.feature.smb.model.SMBCredentials
import com.dehar.player.feature.smb.model.SMBServer
import com.dehar.player.feature.smb.model.SMBShare
import com.dehar.player.feature.smb.repository.SMBRepository

/**
 * ViewModel for SMB browser feature
 */
@HiltViewModel
class SMBBrowserViewModel @Inject constructor(
    private val repository: SMBRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SMBBrowserUiState())
    val uiState: StateFlow<SMBBrowserUiState> = _uiState.asStateFlow()

    /**
     * Discover available SMB servers on network
     */
    fun discoverServers() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                connectionState = SMBConnectionState.CONNECTING
            )
            try {
                val servers = repository.discoverServers()
                _uiState.value = _uiState.value.copy(
                    servers = servers,
                    isLoading = false,
                    connectionState = if (servers.isNotEmpty()) {
                        SMBConnectionState.IDLE
                    } else {
                        SMBConnectionState.DISCONNECTED
                    }
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Failed to discover servers: ${e.message}",
                    connectionState = SMBConnectionState.ERROR
                )
            }
        }
    }

    /**
     * Add server manually
     */
    fun addServer(address: String, serverName: String? = null) {
        val server = SMBServer(
            id = address.hashCode().toString(),
            name = serverName ?: address,
            address = address,
            port = 445,
            isOnline = false
        )
        
        val updatedServers = _uiState.value.servers.toMutableList()
        updatedServers.add(server)
        
        _uiState.value = _uiState.value.copy(servers = updatedServers)
    }

    /**
     * Connect to SMB server
     */
    fun connectToServer(server: SMBServer, credentials: SMBCredentials? = null) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                connectionState = SMBConnectionState.CONNECTING,
                selectedServer = server
            )
            try {
                val success = repository.connectToServer(server.address, credentials)
                
                if (success) {
                    // Get shares from server
                    val shares = repository.getShares(server.address)
                    _uiState.value = _uiState.value.copy(
                        shares = shares,
                        connectionState = SMBConnectionState.CONNECTED,
                        isLoading = false,
                        errorMessage = null,
                        credentials = credentials,
                        showCredentialsDialog = false
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        connectionState = SMBConnectionState.ERROR,
                        errorMessage = "Authentication failed",
                        showCredentialsDialog = true
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    connectionState = SMBConnectionState.ERROR,
                    errorMessage = "Connection failed: ${e.message}",
                    showCredentialsDialog = true
                )
            }
        }
    }

    /**
     * Select and browse a share
     */
    fun selectShare(share: SMBShare) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                selectedShare = share,
                isLoading = true,
                browserState = SMBBrowserState(currentShare = share)
            )
            try {
                val files = repository.listFiles(share.path)
                _uiState.value = _uiState.value.copy(
                    files = files,
                    isLoading = false,
                    browserState = _uiState.value.browserState.copy(
                        currentPath = share.path,
                        breadcrumbs = listOf(share.name)
                    )
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Failed to load share: ${e.message}"
                )
            }
        }
    }

    /**
     * Navigate into a folder
     */
    fun navigateToFolder(folderPath: String, folderName: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val files = repository.listFiles(folderPath)
                val currentBreadcrumbs = _uiState.value.browserState.breadcrumbs.toMutableList()
                currentBreadcrumbs.add(folderName)
                
                val history = _uiState.value.browserState.navigationHistory.toMutableList()
                history.add(_uiState.value.browserState.currentPath)
                
                _uiState.value = _uiState.value.copy(
                    files = files,
                    isLoading = false,
                    browserState = _uiState.value.browserState.copy(
                        currentPath = folderPath,
                        breadcrumbs = currentBreadcrumbs,
                        navigationHistory = history
                    )
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Failed to navigate: ${e.message}"
                )
            }
        }
    }

    /**
     * Navigate back to previous folder
     */
    fun navigateBack() {
        val history = _uiState.value.browserState.navigationHistory
        if (history.isEmpty()) return
        
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val previousPath = history.last()
                val updatedHistory = history.dropLast(1)
                val breadcrumbs = _uiState.value.browserState.breadcrumbs.dropLast(1)
                
                val files = repository.listFiles(previousPath)
                
                _uiState.value = _uiState.value.copy(
                    files = files,
                    isLoading = false,
                    browserState = _uiState.value.browserState.copy(
                        currentPath = previousPath,
                        navigationHistory = updatedHistory,
                        breadcrumbs = breadcrumbs
                    )
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Failed to navigate back: ${e.message}"
                )
            }
        }
    }

    /**
     * Download file from SMB share
     */
    fun downloadFile(smbPath: String, localPath: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val success = repository.downloadFile(smbPath, localPath)
                
                if (success) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = null
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Failed to download file"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Download error: ${e.message}"
                )
            }
        }
    }

    /**
     * Toggle network discovery
     */
    fun toggleNetworkDiscovery(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(enableNetworkDiscovery = enabled)
        if (enabled) {
            discoverServers()
        }
    }

    /**
     * Show credentials dialog
     */
    fun showCredentialsDialog() {
        _uiState.value = _uiState.value.copy(showCredentialsDialog = true)
    }

    /**
     * Hide credentials dialog
     */
    fun hideCredentialsDialog() {
        _uiState.value = _uiState.value.copy(showCredentialsDialog = false)
    }

    /**
     * Clear error message
     */
    fun clearErrorMessage() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    /**
     * Disconnect from server
     */
    fun disconnect() {
        repository.disconnect()
        _uiState.value = SMBBrowserUiState()
    }

    override fun onCleared() {
        super.onCleared()
        disconnect()
    }
}
