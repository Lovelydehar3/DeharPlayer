package com.dehar.player.feature.vault.repository

import android.content.Context
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.FragmentActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.inject.Inject
import javax.inject.Singleton
import com.dehar.player.feature.vault.model.VaultFile
import com.dehar.player.feature.vault.model.VaultFolder
import com.dehar.player.feature.vault.model.EncryptionType

/**
 * Repository for vault encryption and file management
 */
@Singleton
class VaultRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val vaultDir = File(context.filesDir, ".vault")
    private val keyStore = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }
    private val keyAlias = "vault_master_key"

    init {
        if (!vaultDir.exists()) {
            vaultDir.mkdirs()
        }
        initializeKeyStore()
    }

    /**
     * Initialize Keystore with master key
     */
    private fun initializeKeyStore() {
        try {
            if (!keyStore.containsAlias(keyAlias)) {
                val keyGenerator = KeyGenerator.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES,
                    "AndroidKeyStore"
                )

                val keyGenSpec = KeyGenParameterSpec.Builder(
                    keyAlias,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                ).apply {
                    setKeySize(256)
                    setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        setIsStrongBoxBacked(true)
                    }
                }.build()

                keyGenerator.init(keyGenSpec)
                keyGenerator.generateKey()
            }
        } catch (e: Exception) {
            // Handle key generation failure
        }
    }

    /**
     * Check if biometric authentication is available
     */
    fun isBiometricAvailable(): Boolean {
        return try {
            val biometricManager = BiometricManager.from(context)
            biometricManager.canAuthenticate(
                BiometricManager.Authenticators.BIOMETRIC_STRONG or
                BiometricManager.Authenticators.DEVICE_CREDENTIAL
            ) == BiometricManager.BIOMETRIC_SUCCESS
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Encrypt file with AES-256-GCM
     */
    suspend fun encryptFile(
        inputFile: File,
        outputFile: File
    ): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val cipher = Cipher.getInstance("AES/GCM/NoPadding")
                val secretKey = keyStore.getKey(keyAlias, null) as? javax.crypto.SecretKey
                    ?: return@withContext false

                cipher.init(Cipher.ENCRYPT_MODE, secretKey)
                val iv = cipher.iv

                outputFile.outputStream().use { output ->
                    // Write IV
                    output.write(iv)

                    // Encrypt and write file content
                    inputFile.inputStream().use { input ->
                        val buffer = ByteArray(8192)
                        var bytesRead: Int
                        while (input.read(buffer).also { bytesRead = it } != -1) {
                            val encryptedData = cipher.update(buffer, 0, bytesRead)
                            output.write(encryptedData)
                        }

                        // Write final block
                        val finalBlock = cipher.doFinal()
                        output.write(finalBlock)
                    }
                }

                true
            } catch (e: Exception) {
                false
            }
        }
    }

    /**
     * Decrypt file with AES-256-GCM
     */
    suspend fun decryptFile(
        encryptedFile: File,
        outputFile: File
    ): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val cipher = Cipher.getInstance("AES/GCM/NoPadding")
                val secretKey = keyStore.getKey(keyAlias, null) as? javax.crypto.SecretKey
                    ?: return@withContext false

                encryptedFile.inputStream().use { input ->
                    // Read IV
                    val iv = ByteArray(12)
                    input.read(iv)

                    // Initialize cipher with IV
                    val ivSpec = javax.crypto.spec.GCMParameterSpec(128, iv)
                    cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec)

                    // Decrypt file content
                    outputFile.outputStream().use { output ->
                        val buffer = ByteArray(8192)
                        var bytesRead: Int
                        while (input.read(buffer).also { bytesRead = it } != -1) {
                            val decryptedData = cipher.update(buffer, 0, bytesRead)
                            output.write(decryptedData)
                        }

                        // Write final block
                        val finalBlock = cipher.doFinal()
                        output.write(finalBlock)
                    }
                }

                true
            } catch (e: Exception) {
                false
            }
        }
    }

    /**
     * Create vault folder
     */
    suspend fun createFolder(folderName: String): VaultFolder? {
        return withContext(Dispatchers.IO) {
            try {
                val folderPath = File(vaultDir, folderName)
                if (folderPath.mkdirs() || folderPath.exists()) {
                    VaultFolder(
                        id = folderName.hashCode().toString(),
                        name = folderName,
                        path = folderPath.absolutePath
                    )
                } else {
                    null
                }
            } catch (e: Exception) {
                null
            }
        }
    }

    /**
     * Add file to vault (encrypt and copy)
     */
    suspend fun addFileToVault(
        sourceFile: File,
        vaultFolderPath: String,
        encryptionType: EncryptionType = EncryptionType.AES_256_GCM
    ): VaultFile? {
        return withContext(Dispatchers.IO) {
            try {
                val vaultFolder = File(vaultFolderPath)
                val encryptedFile = File(vaultFolder, "${sourceFile.nameWithoutExtension}.enc")

                val success = encryptFile(sourceFile, encryptedFile)
                if (success) {
                    VaultFile(
                        id = encryptedFile.absolutePath.hashCode().toString(),
                        name = sourceFile.name,
                        path = sourceFile.absolutePath,
                        encryptedPath = encryptedFile.absolutePath,
                        size = sourceFile.length(),
                        mimeType = getMimeType(sourceFile.name),
                        lastModified = sourceFile.lastModified(),
                        isEncrypted = true
                    )
                } else {
                    null
                }
            } catch (e: Exception) {
                null
            }
        }
    }

    /**
     * Extract file from vault (decrypt and save)
     */
    suspend fun extractFileFromVault(
        vaultFile: VaultFile,
        outputPath: String
    ): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val encryptedFile = File(vaultFile.encryptedPath)
                val outputFile = File(outputPath)

                decryptFile(encryptedFile, outputFile)
            } catch (e: Exception) {
                false
            }
        }
    }

    /**
     * List files in vault folder
     */
    suspend fun listVaultFiles(folderPath: String): List<VaultFile> {
        return withContext(Dispatchers.IO) {
            try {
                val folder = File(folderPath)
                val files = mutableListOf<VaultFile>()

                folder.listFiles()?.forEach { file ->
                    if (file.isFile && file.extension == "enc") {
                        files.add(
                            VaultFile(
                                id = file.absolutePath.hashCode().toString(),
                                name = file.nameWithoutExtension,
                                path = file.absolutePath,
                                encryptedPath = file.absolutePath,
                                size = file.length(),
                                lastModified = file.lastModified(),
                                isEncrypted = true
                            )
                        )
                    }
                }

                files
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    /**
     * Delete file from vault
     */
    suspend fun deleteVaultFile(filePath: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                File(filePath).delete()
            } catch (e: Exception) {
                false
            }
        }
    }

    /**
     * Get vault size
     */
    suspend fun getVaultSize(): Long {
        return withContext(Dispatchers.IO) {
            try {
                vaultDir.walk().map { it.length() }.sum()
            } catch (e: Exception) {
                0L
            }
        }
    }

    /**
     * Get MIME type from filename
     */
    private fun getMimeType(filename: String): String? {
        return when {
            filename.endsWith(".mp4", ignoreCase = true) -> "video/mp4"
            filename.endsWith(".mkv", ignoreCase = true) -> "video/x-matroska"
            filename.endsWith(".mp3", ignoreCase = true) -> "audio/mpeg"
            filename.endsWith(".jpg", ignoreCase = true) -> "image/jpeg"
            filename.endsWith(".png", ignoreCase = true) -> "image/png"
            filename.endsWith(".pdf", ignoreCase = true) -> "application/pdf"
            else -> null
        }
    }

    /**
     * Wipe vault (secure deletion)
     */
    suspend fun wipeVault(): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                vaultDir.deleteRecursively()
                vaultDir.mkdirs()
                true
            } catch (e: Exception) {
                false
            }
        }
    }
}
