package com.dehar.player.feature.usb.repository

import android.content.Context
import android.os.Environment
import android.os.StatFs
import androidx.core.content.ContextCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import com.dehar.player.feature.usb.model.USBDevice
import com.dehar.player.feature.usb.model.USBFileEntry
import java.io.File

/**
 * Repository for USB storage operations
 */
@Singleton
class USBRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {

    /**
     * Get list of connected USB storage devices
     */
    suspend fun getConnectedDevices(): List<USBDevice> {
        return withContext(Dispatchers.IO) {
            try {
                val devices = mutableListOf<USBDevice>()

                // Get external storage directories
                val externalFilesDirs = ContextCompat.getExternalFilesDirs(context, null)
                
                for ((index, dir) in externalFilesDirs.withIndex()) {
                    if (dir != null && Environment.getExternalStorageState(dir) == Environment.MEDIA_MOUNTED) {
                        val device = createUSBDeviceFromPath(dir, index)
                        devices.add(device)
                    }
                }

                // Also try to access removable storage if available
                try {
                    val externalStorageDir = Environment.getExternalStorageDirectory()
                    if (externalStorageDir.exists()) {
                        val device = createUSBDeviceFromPath(externalStorageDir, 0)
                        if (!devices.any { it.path == device.path }) {
                            devices.add(device)
                        }
                    }
                } catch (e: Exception) {
                    // Ignore errors
                }

                devices
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    /**
     * List files in USB storage directory
     */
    suspend fun listFiles(devicePath: String): List<USBFileEntry> {
        return withContext(Dispatchers.IO) {
            try {
                val files = mutableListOf<USBFileEntry>()
                val directory = File(devicePath)

                if (!directory.exists() || !directory.isDirectory) return@withContext emptyList()

                val fileList = directory.listFiles() ?: return@withContext emptyList()
                
                for (file in fileList) {
                    val fileEntry = USBFileEntry(
                        id = file.path.hashCode().toString(),
                        name = file.name,
                        path = file.absolutePath,
                        isDirectory = file.isDirectory,
                        size = if (file.isFile) file.length() else 0,
                        lastModified = file.lastModified(),
                        isHidden = file.isHidden,
                        canRead = file.canRead(),
                        canWrite = file.canWrite(),
                        mimeType = getMimeType(file.name)
                    )
                    files.add(fileEntry)
                }

                // Sort: directories first, then by name
                files.sortWith(compareBy({ !it.isDirectory }, { it.name }))
                
                files
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    /**
     * Check if USB device is accessible
     */
    suspend fun isAccessible(devicePath: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val directory = File(devicePath)
                directory.exists() && directory.canRead()
            } catch (e: Exception) {
                false
            }
        }
    }

    /**
     * Get USB device storage info
     */
    suspend fun getDeviceInfo(devicePath: String): USBDevice? {
        return withContext(Dispatchers.IO) {
            try {
                val directory = File(devicePath)
                if (!directory.exists()) return@withContext null

                val stat = StatFs(devicePath)
                val blockSize = stat.blockSizeLong
                val totalBlocks = stat.blockCountLong
                val availableBlocks = stat.availableBlocksLong

                USBDevice(
                    id = devicePath.hashCode().toString(),
                    name = directory.name.ifEmpty { "USB Storage" },
                    path = devicePath,
                    totalSpace = totalBlocks * blockSize,
                    availableSpace = availableBlocks * blockSize,
                    isReadable = directory.canRead(),
                    isWritable = directory.canWrite()
                )
            } catch (e: Exception) {
                null
            }
        }
    }

    /**
     * Create USBDevice from file path
     */
    private fun createUSBDeviceFromPath(path: File, index: Int): USBDevice {
        return try {
            val stat = StatFs(path.absolutePath)
            val blockSize = stat.blockSizeLong
            val totalBlocks = stat.blockCountLong
            val availableBlocks = stat.availableBlocksLong

            USBDevice(
                id = path.absolutePath.hashCode().toString(),
                name = path.name.ifEmpty { "USB Drive ${index + 1}" },
                path = path.absolutePath,
                totalSpace = totalBlocks * blockSize,
                availableSpace = availableBlocks * blockSize,
                isReadable = path.canRead(),
                isWritable = path.canWrite()
            )
        } catch (e: Exception) {
            USBDevice(
                id = path.absolutePath.hashCode().toString(),
                name = path.name.ifEmpty { "USB Drive ${index + 1}" },
                path = path.absolutePath
            )
        }
    }

    /**
     * Copy file from USB storage to app cache
     */
    suspend fun copyFile(sourceFile: String, destinationFile: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val source = File(sourceFile)
                val destination = File(destinationFile)

                if (!source.exists() || source.isDirectory) return@withContext false

                source.copyTo(destination, overwrite = true)
                true
            } catch (e: Exception) {
                false
            }
        }
    }

    /**
     * Delete file/folder from USB storage
     */
    suspend fun delete(path: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val file = File(path)
                if (file.isDirectory) {
                    file.deleteRecursively()
                } else {
                    file.delete()
                }
            } catch (e: Exception) {
                false
            }
        }
    }

    /**
     * Get MIME type for file
     */
    private fun getMimeType(filename: String): String? {
        return when {
            filename.endsWith(".mp3", ignoreCase = true) -> "audio/mpeg"
            filename.endsWith(".mp4", ignoreCase = true) -> "video/mp4"
            filename.endsWith(".mkv", ignoreCase = true) -> "video/x-matroska"
            filename.endsWith(".flac", ignoreCase = true) -> "audio/flac"
            filename.endsWith(".aac", ignoreCase = true) -> "audio/aac"
            filename.endsWith(".ogg", ignoreCase = true) -> "audio/ogg"
            filename.endsWith(".wav", ignoreCase = true) -> "audio/wav"
            filename.endsWith(".m4a", ignoreCase = true) -> "audio/mp4"
            filename.endsWith(".3gp", ignoreCase = true) -> "video/3gpp"
            filename.endsWith(".webm", ignoreCase = true) -> "video/webm"
            else -> null
        }
    }

    /**
     * Check if file is media file
     */
    fun isMediaFile(filename: String): Boolean {
        val mediaExtensions = listOf(
            "mp3", "mp4", "mkv", "flac", "aac", "ogg", "wav", "m4a", "3gp", "webm",
            "avi", "mov", "flv", "wmv", "wma", "aiff", "opus", "m4b"
        )
        val extension = filename.substringAfterLast('.').lowercase()
        return extension in mediaExtensions
    }
}
