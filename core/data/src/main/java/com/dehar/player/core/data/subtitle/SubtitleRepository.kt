package com.dehar.player.core.data.subtitle

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for managing subtitle operations
 */
@Singleton
class SubtitleRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {

    /**
     * Detect and load external subtitles for a video file
     */
    suspend fun detectExternalSubtitles(videoPath: String): List<SubtitleTrack> {
        return withContext(Dispatchers.IO) {
            val videoFile = File(videoPath)
            val videoDir = videoFile.parentFile ?: return@withContext emptyList()
            val videoNameWithoutExt = videoFile.nameWithoutExtension

            val subtitleTracks = mutableListOf<SubtitleTrack>()
            val allExtensions = SubtitleFormat.values().map { it.extension }.toSet()

            videoDir.listFiles()?.forEach { file ->
                if (file.isFile && file.extension in allExtensions) {
                    // Check if the filename starts with the video name
                    if (file.nameWithoutExtension.startsWith(videoNameWithoutExt)) {
                        val format = detectFormat(file.extension)
                        if (format != null) {
                            val language = extractLanguage(file.name)
                            subtitleTracks.add(
                                SubtitleTrack(
                                    id = file.absolutePath.hashCode().toLong(),
                                    name = file.nameWithoutExtension,
                                    language = language,
                                    format = format,
                                    path = file.absolutePath,
                                    isExternal = true
                                )
                            )
                        }
                    }
                }
            }

            subtitleTracks.sortedBy { it.name }
        }
    }

    /**
     * Load and parse a subtitle file
     */
    suspend fun loadSubtitle(track: SubtitleTrack): List<SubtitleLine> {
        return withContext(Dispatchers.IO) {
            try {
                val file = File(track.path)
                if (!file.exists()) return@withContext emptyList()

                val content = file.readText(Charsets.UTF_8)
                val parser = SubtitleParserFactory.getParser(track.format)
                parser.parse(content)
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    /**
     * Load multiple subtitle files
     */
    suspend fun loadSubtitles(tracks: List<SubtitleTrack>): Map<Long, List<SubtitleLine>> {
        return withContext(Dispatchers.IO) {
            tracks.associate { track ->
                track.id to loadSubtitle(track)
            }
        }
    }

    /**
     * Find subtitle file for a video by pattern matching
     */
    suspend fun findSubtitleForVideo(videoPath: String, preferredLanguage: String = "en"): SubtitleTrack? {
        return withContext(Dispatchers.IO) {
            val detected = detectExternalSubtitles(videoPath)
            
            // Prioritize exact language match
            detected.find { 
                it.language.startsWith(preferredLanguage, ignoreCase = true) 
            } ?: detected.firstOrNull() ?: null
        }
    }

    /**
     * Detect subtitle format from file extension
     */
    private fun detectFormat(extension: String): SubtitleFormat? {
        return SubtitleFormat.values().find { 
            it.extension.equals(extension, ignoreCase = true) 
        }
    }

    /**
     * Extract language code from filename
     * Patterns: filename.en.srt, filename-en.srt, filename_eng.srt
     */
    private fun extractLanguage(filename: String): String {
        val nameWithoutExt = filename.substringBeforeLast(".")
        val languagePatterns = listOf(
            Regex("""\.([a-z]{2,3})$"""), // .en, .eng
            Regex("""-([a-z]{2,3})$"""),   // -en, -eng
            Regex("""_([a-z]{2,3})$""")    // _en, _eng
        )

        for (pattern in languagePatterns) {
            val match = pattern.find(nameWithoutExt)
            if (match != null) {
                return match.groupValues[1]
            }
        }

        return "unknown"
    }

    /**
     * Save custom subtitle track to database
     */
    suspend fun saveCustomSubtitleTrack(track: SubtitleTrack): Long {
        return withContext(Dispatchers.IO) {
            // TODO: Save to database when SubtitleTrackDao is available
            track.id
        }
    }

    /**
     * Delete subtitle track
     */
    suspend fun deleteSubtitleTrack(track: SubtitleTrack): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                File(track.path).delete()
            } catch (e: Exception) {
                false
            }
        }
    }

    /**
     * Convert subtitle from one format to another
     */
    suspend fun convertSubtitle(
        sourceTrack: SubtitleTrack,
        targetFormat: SubtitleFormat
    ): List<SubtitleLine> {
        return withContext(Dispatchers.IO) {
            try {
                // Load source
                val subtitles = loadSubtitle(sourceTrack)
                
                // Parse and re-format (the subtitles are now in a generic format)
                subtitles
            } catch (e: Exception) {
                emptyList()
            }
        }
    }
}
