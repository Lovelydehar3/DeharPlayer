package com.dehar.player.feature.vault.model

import android.security.keystore.KeyProperties

/**
 * Vault folder information
 */
data class VaultFolder(
    val id: String,
    val name: String,
    val path: String,
    val itemCount: Int = 0,
    val createdDate: Long = System.currentTimeMillis(),
    val isLocked: Boolean = true
)

/**
 * Vault file information
 */
data class VaultFile(
    val id: String,
    val name: String,
    val path: String,
    val encryptedPath: String,
    val size: Long = 0,
    val mimeType: String? = null,
    val lastModified: Long = 0,
    val isEncrypted: Boolean = true,
    val thumbnailPath: String? = null
)

/**
 * Vault authentication state
 */
enum class VaultAuthState {
    LOCKED,
    AUTHENTICATING,
    UNLOCKED,
    AUTHENTICATION_FAILED
}

/**
 * Encryption algorithm type
 */
enum class EncryptionType {
    AES_256_GCM,
    AES_256_CBC
}

/**
 * Vault security settings
 */
data class VaultSecuritySettings(
    val encryptionType: EncryptionType = EncryptionType.AES_256_GCM,
    val useBiometric: Boolean = true,
    val usePasscode: Boolean = false,
    val passcode: String? = null,
    val biometricEnabled: Boolean = true,
    val lockTimeoutMinutes: Int = 5
)

/**
 * Vault browser navigation state
 */
data class VaultBrowserState(
    val currentPath: String = "",
    val currentFolder: VaultFolder? = null,
    val navigationHistory: List<String> = emptyList(),
    val breadcrumbs: List<String> = emptyList()
) {
    fun canGoBack(): Boolean = navigationHistory.isNotEmpty()
}

/**
 * Vault UI state
 */
data class VaultUiState(
    val authState: VaultAuthState = VaultAuthState.LOCKED,
    val folders: List<VaultFolder> = emptyList(),
    val files: List<VaultFile> = emptyList(),
    val selectedFolder: VaultFolder? = null,
    val selectedFile: VaultFile? = null,
    val browserState: VaultBrowserState = VaultBrowserState(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val showAuthDialog: Boolean = false,
    val securitySettings: VaultSecuritySettings = VaultSecuritySettings(),
    val biometricAvailable: Boolean = false,
    val vaultSize: Long = 0
)
