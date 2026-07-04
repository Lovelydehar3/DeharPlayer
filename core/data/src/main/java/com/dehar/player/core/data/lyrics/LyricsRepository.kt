package com.dehar.player.core.data.lyrics

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for managing lyrics operations
 */
@Singleton
class LyricsRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {

    /**
     * Load lyrics from local LRC file
     */
    suspend fun loadLocalLyrics(lyricsPath: String): List<LyricLine> {
        return withContext(Dispatchers.IO) {
            try {
                val file = File(lyricsPath)
                if (!file.exists()) return@withContext emptyList()

                val content = file.readText(Charsets.UTF_8)
                val format = detectFormat(file.extension)
                val parser = LyricsParserFactory.getParser(format)
                parser.parse(content)
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    /**
     * Find local lyrics file for an audio file
     * Searches for .lrc file with matching name in same directory
     */
    suspend fun findLocalLyrics(audioPath: String): String? {
        return withContext(Dispatchers.IO) {
            val audioFile = File(audioPath)
            val audioDir = audioFile.parentFile ?: return@withContext null
            val audioNameWithoutExt = audioFile.nameWithoutExtension

            audioDir.listFiles()?.find { file ->
                file.isFile && 
                file.extension == "lrc" && 
                file.nameWithoutExtension.equals(audioNameWithoutExt, ignoreCase = true)
            }?.absolutePath
        }
    }

    /**
     * Search for lyrics online using lrclib.net API
     * Query parameters: artist, title, duration
     */
    suspend fun searchLyricsOnline(
        artist: String,
        title: String,
        durationMs: Long
    ): List<LyricLine> {
        return withContext(Dispatchers.IO) {
            try {
                // TODO: Implement actual API call to lrclib.net
                // For now, return empty list as placeholder
                emptyList()
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    /**
     * Get the current lyric line for a given playback position
     */
    fun getCurrentLyricLine(lyrics: List<LyricLine>, positionMs: Long): LyricLine? {
        return lyrics.findLast { it.timeMs <= positionMs }
    }

    /**
     * Get nearby lyric lines for display (current and next few lines)
     */
    fun getNearbyLyrics(
        lyrics: List<LyricLine>,
        positionMs: Long,
        count: Int = 3
    ): List<LyricLine> {
        val currentIndex = lyrics.indexOfLast { it.timeMs <= positionMs }
        if (currentIndex < 0) return emptyList()

        return lyrics.subList(currentIndex, minOf(currentIndex + count, lyrics.size))
    }

    /**
     * Detect lyrics format from file extension
     */
    private fun detectFormat(extension: String): LyricsFormat {
        return when (extension?.lowercase()) {
            "lrc" -> LyricsFormat.LRC
            "vtt" -> LyricsFormat.VTT
            "txt" -> LyricsFormat.PLAIN
            "json" -> LyricsFormat.JSON
            else -> LyricsFormat.PLAIN
        }
    }

    /**
     * Save lyrics to local file
     */
    suspend fun saveLyrics(
        audioPath: String,
        lyrics: List<LyricLine>,
        format: LyricsFormat = LyricsFormat.LRC
    ): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val audioFile = File(audioPath)
                val lyricsPath = (audioFile.parent ?: audioFile.absolutePath) + File.separator + audioFile.nameWithoutExtension + ".lrc"
                val lyricsFile = File(lyricsPath)

                val content = when (format) {
                    LyricsFormat.LRC -> formatAsLRC(lyrics)
                    LyricsFormat.PLAIN -> formatAsPlain(lyrics)
                    else -> formatAsLRC(lyrics)
                }

                lyricsFile.writeText(content, Charsets.UTF_8)
                true
            } catch (e: Exception) {
                false
            }
        }
    }

    private fun formatAsLRC(lyrics: List<LyricLine>): String {
        val sb = StringBuilder()
        sb.appendLine("[ar:Unknown]")
        sb.appendLine("[ti:Unknown]")
        sb.appendLine("[al:Unknown]")
        sb.appendLine()

        for (line in lyrics) {
            val minutes = line.timeMs / 60000
            val seconds = (line.timeMs % 60000) / 1000
            val millis = line.timeMs % 1000
            sb.appendLine(String.format("[%02d:%02d.%02d]%s", minutes, seconds, millis / 10, line.text))
        }

        return sb.toString()
    }

    private fun formatAsPlain(lyrics: List<LyricLine>): String {
        return lyrics.joinToString("\n") { it.text }
    }

    /**
     * Delete local lyrics file
     */
    suspend fun deleteLocalLyrics(audioPath: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val audioFile = File(audioPath)
                val lyricsPath = (audioFile.parent ?: audioFile.absolutePath) + File.separator + audioFile.nameWithoutExtension + ".lrc"
                File(lyricsPath).delete()
            } catch (e: Exception) {
                false
            }
        }
    }
}
