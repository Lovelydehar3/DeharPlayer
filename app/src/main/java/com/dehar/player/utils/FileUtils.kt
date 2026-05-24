package com.dehar.player.utils

import java.io.File
import java.util.Locale

object FileUtils {

    fun formatFileSize(bytes: Long): String {
        if (bytes <= 0) return "0 B"
        val units = arrayOf("B", "KB", "MB", "GB", "TB")
        val digitGroups = (Math.log10(bytes.toDouble()) / Math.log10(1024.0)).toInt()
        return String.format(
            Locale.US,
            "%.2f %s",
            bytes / Math.pow(1024.0, digitGroups.toDouble()),
            units[digitGroups]
        )
    }

    fun getFileExtension(path: String): String {
        val file = File(path)
        return file.extension.lowercase(Locale.ROOT)
    }

    fun getFileName(path: String): String {
        return File(path).name
    }

    fun getParentFolder(path: String): String {
        return File(path).parent ?: ""
    }

    fun getResolution(width: Int, height: Int): String {
        if (width <= 0 || height <= 0) return ""
        val minDim = Math.min(width, height)
        return when {
            minDim >= 2160 -> "4K"
            minDim >= 1080 -> "1080p"
            minDim >= 720 -> "720p"
            minDim >= 480 -> "480p"
            else -> "${minDim}p"
        }
    }
}
