package com.dehar.player.feature.whatsappstatus.repository

import android.content.Context
import android.os.Environment
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton
import com.dehar.player.feature.whatsappstatus.model.StatusContact
import com.dehar.player.feature.whatsappstatus.model.StatusFile
import com.dehar.player.feature.whatsappstatus.model.StatusFileType

/**
 * Repository for WhatsApp status file monitoring and downloading
 */
@Singleton
class StatusRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val whatsappStatusDir = File(
        Environment.getExternalStorageDirectory(),
        "Android/media/com.whatsapp/WhatsApp/Media/.Statuses"
    )

    private val downloadDir = File(
        context.getExternalFilesDir(null),
        "WhatsApp_Status"
    ).apply {
        mkdirs()
    }

    /**
     * Scan for WhatsApp status files
     */
    suspend fun scanForStatuses(): List<StatusFile> {
        return withContext(Dispatchers.IO) {
            try {
                if (!whatsappStatusDir.exists()) {
                    return@withContext emptyList()
                }

                val statuses = mutableListOf<StatusFile>()
                
                whatsappStatusDir.listFiles()?.forEach { file ->
                    if (isStatusFile(file)) {
                        val statusFile = StatusFile(
                            id = file.absolutePath.hashCode().toString(),
                            name = file.name,
                            path = file.absolutePath,
                            size = file.length(),
                            lastModified = file.lastModified(),
                            type = getStatusFileType(file.name),
                            mimeType = getMimeType(file.name)
                        )
                        statuses.add(statusFile)
                    }
                }

                // Sort by latest first
                statuses.sortByDescending { it.lastModified }
                statuses
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    /**
     * Monitor WhatsApp status folder for new files
     */
    suspend fun monitorStatusFolder(
        onNewStatus: (StatusFile) -> Unit
    ): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                if (!whatsappStatusDir.exists()) {
                    return@withContext false
                }

                // TODO: Implement FileObserver or similar for real-time monitoring
                // For now, return true to indicate monitoring started
                true
            } catch (e: Exception) {
                false
            }
        }
    }

    /**
     * Download status file
     */
    suspend fun downloadStatus(statusFile: StatusFile): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val sourceFile = File(statusFile.path)
                if (!sourceFile.exists()) return@withContext false

                val destinationFile = File(downloadDir, statusFile.name)
                sourceFile.copyTo(destinationFile, overwrite = true)

                true
            } catch (e: Exception) {
                false
            }
        }
    }

    /**
     * Download multiple status files
     */
    suspend fun downloadMultipleStatuses(
        statusFiles: List<StatusFile>,
        onProgress: (Int, Int) -> Unit
    ): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                var downloaded = 0
                for ((index, statusFile) in statusFiles.withIndex()) {
                    if (downloadStatus(statusFile)) {
                        downloaded++
                    }
                    onProgress(index + 1, statusFiles.size)
                }
                
                downloaded > 0
            } catch (e: Exception) {
                false
            }
        }
    }

    /**
     * Get list of downloaded statuses
     */
    suspend fun getDownloadedStatuses(): List<StatusFile> {
        return withContext(Dispatchers.IO) {
            try {
                val downloaded = mutableListOf<StatusFile>()
                
                downloadDir.listFiles()?.forEach { file ->
                    if (isStatusFile(file)) {
                        downloaded.add(
                            StatusFile(
                                id = file.absolutePath.hashCode().toString(),
                                name = file.name,
                                path = file.absolutePath,
                                size = file.length(),
                                lastModified = file.lastModified(),
                                type = getStatusFileType(file.name),
                                mimeType = getMimeType(file.name),
                                isDownloaded = true,
                                downloadPath = file.absolutePath
                            )
                        )
                    }
                }

                downloaded.sortByDescending { it.lastModified }
                downloaded
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    /**
     * Delete downloaded status
     */
    suspend fun deleteDownloadedStatus(filePath: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                File(filePath).delete()
            } catch (e: Exception) {
                false
            }
        }
    }

    /**
     * Get download folder size
     */
    suspend fun getDownloadFolderSize(): Long {
        return withContext(Dispatchers.IO) {
            try {
                downloadDir.walk().map { it.length() }.sum()
            } catch (e: Exception) {
                0L
            }
        }
    }

    /**
     * Get status folder path
     */
    fun getStatusFolderPath(): String = whatsappStatusDir.absolutePath

    /**
     * Get download folder path
     */
    fun getDownloadFolderPath(): String = downloadDir.absolutePath

    /**
     * Check if file is a status file
     */
    private fun isStatusFile(file: File): Boolean {
        if (!file.isFile) return false
        
        val extension = file.extension.lowercase()
        return extension in listOf("jpg", "jpeg", "png", "mp4", "gif")
    }

    /**
     * Get status file type
     */
    private fun getStatusFileType(filename: String): StatusFileType {
        return when {
            filename.endsWith(".mp4", ignoreCase = true) -> StatusFileType.VIDEO
            filename.endsWith(".jpg", ignoreCase = true) || 
            filename.endsWith(".jpeg", ignoreCase = true) ||
            filename.endsWith(".png", ignoreCase = true) ||
            filename.endsWith(".gif", ignoreCase = true) -> StatusFileType.IMAGE
            else -> StatusFileType.UNKNOWN
        }
    }

    /**
     * Get MIME type for filename
     */
    private fun getMimeType(filename: String): String? {
        return when {
            filename.endsWith(".mp4", ignoreCase = true) -> "video/mp4"
            filename.endsWith(".jpg", ignoreCase = true) || 
            filename.endsWith(".jpeg", ignoreCase = true) -> "image/jpeg"
            filename.endsWith(".png", ignoreCase = true) -> "image/png"
            filename.endsWith(".gif", ignoreCase = true) -> "image/gif"
            else -> null
        }
    }

    /**
     * Check WhatsApp status folder access
     */
    fun isStatusFolderAccessible(): Boolean {
        return whatsappStatusDir.exists() && whatsappStatusDir.canRead()
    }

    /**
     * Get available storage space
     */
    fun getAvailableStorageSpace(): Long {
        return try {
            val stat = android.os.StatFs(downloadDir.absolutePath)
            stat.availableBlocksLong * stat.blockSizeLong
        } catch (e: Exception) {
            0L
        }
    }
}
