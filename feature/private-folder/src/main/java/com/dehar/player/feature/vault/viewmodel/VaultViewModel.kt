package com.dehar.player.feature.vault.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.dehar.player.feature.vault.model.VaultAuthState
import com.dehar.player.feature.vault.model.VaultBrowserState
import com.dehar.player.feature.vault.model.VaultUiState
import com.dehar.player.feature.vault.repository.VaultRepository

/**
 * ViewModel for private vault feature
 */
@HiltViewModel
class VaultViewModel @Inject constructor(
    private val repository: VaultRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(VaultUiState())
    val uiState: StateFlow<VaultUiState> = _uiState.asStateFlow()

    init {
        checkBiometricAvailability()
    }

    /**
     * Check if biometric authentication is available
     */
    private fun checkBiometricAvailability() {
        val biometricAvailable = repository.isBiometricAvailable()
        _uiState.value = _uiState.value.copy(
            biometricAvailable = biometricAvailable
        )
    }

    /**
     * Request vault unlock with authentication
     */
    fun requestVaultUnlock() {
        _uiState.value = _uiState.value.copy(
            showAuthDialog = true,
            authState = VaultAuthState.AUTHENTICATING
        )
    }

    /**
     * Unlock vault with passcode
     */
    fun unlockWithPasscode(passcode: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(authState = VaultAuthState.AUTHENTICATING)
            try {
                // Validate passcode
                val storedPasscode = _uiState.value.securitySettings.passcode
                if (passcode == storedPasscode && passcode.isNotEmpty()) {
                    loadVaultContents()
                    _uiState.value = _uiState.value.copy(
                        authState = VaultAuthState.UNLOCKED,
                        showAuthDialog = false,
                        errorMessage = null
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        authState = VaultAuthState.AUTHENTICATION_FAILED,
                        errorMessage = "Invalid passcode"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    authState = VaultAuthState.AUTHENTICATION_FAILED,
                    errorMessage = "Authentication error: ${e.message}"
                )
            }
        }
    }

    /**
     * Unlock vault with biometric
     */
    fun unlockWithBiometric() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(authState = VaultAuthState.AUTHENTICATING)
            try {
                // Biometric authentication would be handled by BiometricPrompt
                // Once authenticated, call this to unlock
                loadVaultContents()
                _uiState.value = _uiState.value.copy(
                    authState = VaultAuthState.UNLOCKED,
                    showAuthDialog = false,
                    errorMessage = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    authState = VaultAuthState.AUTHENTICATION_FAILED,
                    errorMessage = "Biometric authentication failed"
                )
            }
        }
    }

    /**
     * Load vault contents
     */
    private suspend fun loadVaultContents() {
        _uiState.value = _uiState.value.copy(isLoading = true)
        try {
            val vaultSize = repository.getVaultSize()
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                vaultSize = vaultSize
            )
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                errorMessage = "Failed to load vault: ${e.message}"
            )
        }
    }

    /**
     * Add file to vault
     */
    fun addFileToVault(sourceFilePath: String, folderPath: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val sourceFile = java.io.File(sourceFilePath)
                val vaultFile = repository.addFileToVault(sourceFile, folderPath)

                if (vaultFile != null) {
                    val updatedFiles = _uiState.value.files.toMutableList()
                    updatedFiles.add(vaultFile)
                    _uiState.value = _uiState.value.copy(
                        files = updatedFiles,
                        isLoading = false,
                        errorMessage = null
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Failed to add file to vault"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Error: ${e.message}"
                )
            }
        }
    }

    /**
     * Extract file from vault
     */
    fun extractFileFromVault(vaultFile: com.dehar.player.feature.vault.model.VaultFile, outputPath: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val success = repository.extractFileFromVault(vaultFile, outputPath)

                if (success) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = null
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Failed to extract file"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Extraction error: ${e.message}"
                )
            }
        }
    }

    /**
     * Delete file from vault
     */
    fun deleteVaultFile(filePath: String) {
        viewModelScope.launch {
            try {
                val success = repository.deleteVaultFile(filePath)

                if (success) {
                    val updatedFiles = _uiState.value.files.filter { it.encryptedPath != filePath }
                    _uiState.value = _uiState.value.copy(
                        files = updatedFiles,
                        errorMessage = null
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "Failed to delete file"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Delete error: ${e.message}"
                )
            }
        }
    }

    /**
     * Create vault folder
     */
    fun createVaultFolder(folderName: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val folder = repository.createFolder(folderName)

                if (folder != null) {
                    val updatedFolders = _uiState.value.folders.toMutableList()
                    updatedFolders.add(folder)
                    _uiState.value = _uiState.value.copy(
                        folders = updatedFolders,
                        isLoading = false,
                        errorMessage = null
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Failed to create folder"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Error: ${e.message}"
                )
            }
        }
    }

    /**
     * Lock vault
     */
    fun lockVault() {
        _uiState.value = VaultUiState(
            authState = VaultAuthState.LOCKED,
            biometricAvailable = _uiState.value.biometricAvailable
        )
    }

    /**
     * Clear error message
     */
    fun clearErrorMessage() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    /**
     * Hide auth dialog
     */
    fun hideAuthDialog() {
        _uiState.value = _uiState.value.copy(showAuthDialog = false)
    }

    override fun onCleared() {
        super.onCleared()
        lockVault()
    }
}
