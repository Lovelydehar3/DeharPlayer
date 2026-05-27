package com.dehar.player.feature.usb.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.dehar.player.feature.usb.model.USBBrowserState
import com.dehar.player.feature.usb.model.USBBrowserUiState
import com.dehar.player.feature.usb.model.USBPermissionState
import com.dehar.player.feature.usb.repository.USBRepository

/**
 * ViewModel for USB browser feature
 */
@HiltViewModel
class USBBrowserViewModel @Inject constructor(
    private val repository: USBRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(USBBrowserUiState())
    val uiState: StateFlow<USBBrowserUiState> = _uiState.asStateFlow()

    /**
     * Request USB permission (simulated - in real app would use DocumentsProvider API)
     */
    fun requestUSBPermission() {
        _uiState.value = _uiState.value.copy(
            permissionState = USBPermissionState.PENDING,
            showPermissionRequest = true
        )
    }

    /**
     * Grant USB permission
     */
    fun grantUSBPermission() {
        _uiState.value = _uiState.value.copy(
            permissionState = USBPermissionState.GRANTED,
            showPermissionRequest = false
        )
        scanForDevices()
    }

    /**
     * Deny USB permission
     */
    fun denyUSBPermission() {
        _uiState.value = _uiState.value.copy(
            permissionState = USBPermissionState.DENIED,
            showPermissionRequest = false
        )
    }

    /**
     * Scan for connected USB devices
     */
    fun scanForDevices() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val devices = repository.getConnectedDevices()
                _uiState.value = _uiState.value.copy(
                    devices = devices,
                    isLoading = false,
                    errorMessage = if (devices.isEmpty()) {
                        "No USB devices found"
                    } else {
                        null
                    }
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Failed to scan devices: ${e.message}"
                )
            }
        }
    }

    /**
     * Select and browse a USB device
     */
    fun selectDevice(device: com.dehar.player.feature.usb.model.USBDevice) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                selectedDevice = device,
                isLoading = true,
                browserState = USBBrowserState(currentDevice = device)
            )
            try {
                val files = repository.listFiles(device.path)
                _uiState.value = _uiState.value.copy(
                    files = files,
                    isLoading = false,
                    browserState = _uiState.value.browserState.copy(
                        currentPath = device.path,
                        breadcrumbs = listOf(device.name)
                    )
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Failed to browse device: ${e.message}"
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
     * Copy file from USB to device storage
     */
    fun copyFile(sourceFile: String, destinationFile: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val success = repository.copyFile(sourceFile, destinationFile)
                
                if (success) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = null
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Failed to copy file"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Copy error: ${e.message}"
                )
            }
        }
    }

    /**
     * Clear error message
     */
    fun clearErrorMessage() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    /**
     * Exit device browser
     */
    fun exitDeviceBrowser() {
        _uiState.value = _uiState.value.copy(
            selectedDevice = null,
            files = emptyList(),
            browserState = USBBrowserState()
        )
    }

    override fun onCleared() {
        super.onCleared()
        exitDeviceBrowser()
    }
}
