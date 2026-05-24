package com.dehar.player.player

import android.net.Uri
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.common.util.UnstableApi
import java.io.File
import java.util.Locale

@OptIn(UnstableApi::class)
object SubtitleHelper {
    private val SUBTITLE_EXTENSIONS = listOf(".srt", ".ass", ".ssa", ".vtt")
    
    fun findSubtitles(videoPath: String): List<SubtitleInfo> {
        val videoFile = File(videoPath)
        val baseName = videoFile.nameWithoutExtension
        val parentDir = videoFile.parentFile ?: return emptyList()
        
        return parentDir.listFiles()?.filter { file ->
            SUBTITLE_EXTENSIONS.any { ext ->
                file.name.startsWith(baseName, ignoreCase = true) && 
                file.name.endsWith(ext, ignoreCase = true)
            }
        }?.map { file ->
            SubtitleInfo(
                path = file.absolutePath,
                uri = Uri.fromFile(file),
                language = extractLanguage(file.name, baseName),
                mimeType = getMimeType(file.extension)
            )
        } ?: emptyList()
    }
    
    fun createSubtitleConfig(subtitle: SubtitleInfo): MediaItem.SubtitleConfiguration {
        return MediaItem.SubtitleConfiguration.Builder(subtitle.uri)
            .setMimeType(subtitle.mimeType)
            .setLanguage(subtitle.language)
            .setSelectionFlags(androidx.media3.common.C.SELECTION_FLAG_DEFAULT)
            .build()
    }
    
    private fun extractLanguage(fileName: String, baseName: String): String {
        val withoutExt = fileName.substringBeforeLast(".")
        val langPart = withoutExt.removePrefix(baseName).trim('.', '_', ' ')
        return if (langPart.isNotEmpty()) langPart else "en"
    }
    
    private fun getMimeType(extension: String): String {
        return when (extension.lowercase(Locale.US)) {
            "srt" -> MimeTypes.APPLICATION_SUBRIP
            "ass", "ssa" -> MimeTypes.TEXT_SSA
            "vtt" -> MimeTypes.TEXT_VTT
            else -> MimeTypes.APPLICATION_SUBRIP
        }
    }
}

data class SubtitleInfo(
    val path: String,
    val uri: Uri,
    val language: String,
    val mimeType: String
)
