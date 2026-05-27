package com.dehar.player.feature.private_folder.domain

import android.content.Context
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import java.security.KeyStore
import java.util.UUID
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import javax.crypto.KeyGenerator

/**
 * Private Vault Manager
 * Handles AES-256-GCM encryption/decryption for vault items
 */
class PrivateVaultManager(context: Context) {
    private val keyStore = KeyStore.getInstance("AndroidKeyStore")
    private val context = context

    init {
        keyStore.load(null)
        ensureKeyExists()
    }

    private fun ensureKeyExists() {
        if (!keyStore.containsAlias(VAULT_KEY_ALIAS)) {
            val keyGenerator = KeyGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_AES,
                "AndroidKeyStore"
            )
            val keySpec = KeyGenParameterSpec.Builder(
                VAULT_KEY_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setKeySize(256)
                .setUserAuthenticationRequired(false)  // Can be enabled for higher security
                .build()

            keyGenerator.init(keySpec)
            keyGenerator.generateKey()
        }
    }

    fun encryptData(data: ByteArray): Pair<ByteArray, ByteArray> {
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        val key = keyStore.getKey(VAULT_KEY_ALIAS, null)

        cipher.init(Cipher.ENCRYPT_MODE, key)
        val iv = cipher.iv
        val encryptedData = cipher.doFinal(data)

        return Pair(iv, encryptedData)
    }

    fun decryptData(iv: ByteArray, encryptedData: ByteArray): ByteArray {
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        val key = keyStore.getKey(VAULT_KEY_ALIAS, null)
        val spec = GCMParameterSpec(128, iv)

        cipher.init(Cipher.DECRYPT_MODE, key, spec)
        return cipher.doFinal(encryptedData)
    }

    companion object {
        private const val VAULT_KEY_ALIAS = "DeharVaultKey"
    }
}

/**
 * Biometric Authentication Manager
 * Handles fingerprint/face unlock for vault
 */
class BiometricVaultLock(activity: FragmentActivity) {
    private val activity = activity
    private val biometricPrompt: BiometricPrompt

    init {
        val executor = ContextCompat.getMainExecutor(activity)

        biometricPrompt = BiometricPrompt(
            activity,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    // Unlock vault
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    // Handle error
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    // Handle failure
                }
            }
        )
    }

    fun authenticate() {
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Unlock Private Vault")
            .setSubtitle("Use your biometric to unlock")
            .setNegativeButtonText("Cancel")
            .build()

        biometricPrompt.authenticate(promptInfo)
    }
}

/**
 * PIN-based vault lock (bcrypt hashing)
 */
class PINVaultLock {
    fun hashPIN(pin: String): String {
        // Use bcrypt library
        return "" // TODO: Implement bcrypt
    }

    fun verifyPIN(pin: String, hash: String): Boolean {
        // Verify bcrypt hash
        return false // TODO: Implement bcrypt verification
    }
}

data class VaultItem(
    val id: String = UUID.randomUUID().toString(),
    val originalPath: String,
    val encryptedPath: String,
    val mediaType: String,  // VIDEO / AUDIO
    val addedAt: Long = System.currentTimeMillis(),
    val thumbnailPath: String? = null,
    val title: String,
    val duration: Long
)

enum class VaultLockType {
    BIOMETRIC,
    PIN,
    PATTERN,
    PASSWORD
}

data class VaultState(
    val isLocked: Boolean = true,
    val lockType: VaultLockType = VaultLockType.BIOMETRIC,
    val items: List<VaultItem> = emptyList(),
    val autoLockMinutes: Int = 5
)
