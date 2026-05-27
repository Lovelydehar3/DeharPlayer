package com.dehar.player.core.common

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.text.format.DateUtils
import android.util.TypedValue
import android.webkit.MimeTypeMap
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.core.content.ContextCompat
import java.io.File
import java.text.DecimalFormat
import java.util.Locale

/**
 * Extension functions for common operations
 */

// Context Extensions
fun Context.getStoragePath(): String {
    return Environment.getExternalStorageDirectory()?.absolutePath ?: ""
}

fun Context.getVideoFolders(): List<File> {
    val storagePath = getStoragePath()
    val folders = mutableListOf<File>()
    
    // Common video folders
    val commonFolders = listOf(
        "Movies", "Videos", "DCIM", "Download", "WhatsApp/Media/WhatsApp Video",
        "Telegram/Telegram Video", "Instagram", "YouTube", "Amazon Prime Video",
        "Netflix", "Disney+ Hotstar"
    )
    
    commonFolders.forEach { folderName ->
        val folder = File("$storagePath/$folderName")
        if (folder.exists() && folder.isDirectory) {
            folders.add(folder)
        }
    }
    
    return folders.distinct()
}

fun Context.openAppSettings() {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", packageName, null)
    }
    startActivity(intent)
}

fun Context.canDrawOverlays(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        Settings.canDrawOverlays(this)
    } else {
        true
    }
}

// File Extensions
fun File.isVideoFile(): Boolean {
    val extension = extension.lowercase(Locale.ROOT)
    return Constants.VIDEO_EXTENSIONS.contains(extension)
}

fun File.isAudioFile(): Boolean {
    val extension = extension.lowercase(Locale.ROOT)
    return Constants.AUDIO_EXTENSIONS.contains(extension)
}

fun File.isSubtitleFile(): Boolean {
    val extension = extension.lowercase(Locale.ROOT)
    return Constants.SUBTITLE_EXTENSIONS.contains(extension)
}

fun File.getMimeType(): String {
    val extension = extension.lowercase(Locale.ROOT)
    return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        ?: when {
            isVideoFile() -> "video/*"
            isAudioFile() -> "audio/*"
            isSubtitleFile() -> "text/*"
            else -> "*/*"
        }
}

fun File.humanReadableSize(): String {
    if (!exists()) return "0 B"
    val bytes = length()
    return if (bytes < 1024) {
        "$bytes B"
    } else {
        val units = arrayOf("B", "KB", "MB", "GB", "TB")
        val digitGroups = (Math.log(bytes.toDouble()) / Math.log(1024.0)).toInt()
        DecimalFormat("#,##0.#").format(bytes / Math.pow(1024.0, digitGroups.toDouble())) + " " + units[digitGroups]
    }
}

fun File.humanReadableDuration(durationMs: Long): String {
    val totalSeconds = durationMs / 1000
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60
    
    return if (hours > 0) {
        String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, seconds)
    } else {
        String.format(Locale.getDefault(), "%d:%02d", minutes, seconds)
    }
}

// String Extensions
fun String?.orEmpty(): String = this ?: ""

fun String?.isNullOrEmpty(): Boolean = this.isNullOrBlank()

fun String.formatDuration(): String {
    return DateUtils.formatElapsedTime(toLong() / 1000)
}

fun String.toLongOrZero(): Long {
    return try {
        toLong()
    } catch (e: NumberFormatException) {
        0L
    }
}

fun String.toIntOrZero(): Int {
    return try {
        toInt()
    } catch (e: NumberFormatException) {
        0
    }
}

// Float Extensions
fun Float.cleanSpeed(): String {
    return if (this == this.toInt().toFloat()) {
        this.toInt().toString()
    } else {
        String.format(Locale.US, "%.2f", this).trimEnd('0').trimEnd('.')
    }
}

// Long Extensions
fun Long.formatFileSize(): String {
    return if (this < 1024) {
        "$this B"
    } else {
        val units = arrayOf("B", "KB", "MB", "GB", "TB")
        val digitGroups = (Math.log(this.toDouble()) / Math.log(1024.0)).toInt()
        DecimalFormat("#,##0.#").format(this / Math.pow(1024.0, digitGroups.toDouble())) + " " + units[digitGroups]
    }
}

fun Long.formatDuration(): String {
    val totalSeconds = this / 1000
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60
    
    return if (hours > 0) {
        String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, seconds)
    } else {
        String.format(Locale.getDefault(), "%d:%02d", minutes, seconds)
    }
}

fun Long.formatRemainingDuration(): String {
    if (this <= 0) return "0:00"
    return "-${formatDuration()}"
}

// Int Extensions
fun Int.toResolutionLabel(): String {
    return when {
        this >= Constants.RESOLUTION_4K_WIDTH -> "4K"
        this >= Constants.RESOLUTION_FHD_WIDTH -> "FHD"
        this >= Constants.RESOLUTION_HD_WIDTH -> "HD"
        this >= Constants.RESOLUTION_SD_WIDTH -> "SD"
        else -> "LD"
    }
}

// Collection Extensions
fun <T> List<T>.safeGet(index: Int): T? {
    return if (index in indices) this[index] else null
}

fun <T> List<T>.next(index: Int): T? {
    return safeGet(index + 1)
}

fun <T> List<T>.previous(index: Int): T? {
    return safeGet(index - 1)
}

// Boolean Extensions
inline fun Boolean.ifTrue(action: () -> Unit): Boolean {
    if (this) action()
    return this
}

inline fun Boolean.ifFalse(action: () -> Unit): Boolean {
    if (!this) action()
    return this
}

// Result Extensions
inline fun <T> Result<T>.onSuccess(action: (T) -> Unit): Result<T> {
    if (isSuccess) {
        getOrNull()?.let(action)
    }
    return this
}

inline fun <T> Result<T>.onFailure(action: (Throwable) -> Unit): Result<T> {
    if (isFailure) {
        exceptionOrNull()?.let(action)
    }
    return this
}