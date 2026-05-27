package com.dehar.player.core.data.lyrics

/**
 * Represents a synchronized lyric line
 */
data class LyricLine(
    val timeMs: Long,
    val text: String,
    val isSynced: Boolean = true
)

/**
 * Represents complete lyrics for a song
 */
data class SongLyrics(
    val songId: Long,
    val title: String,
    val artist: String,
    val album: String,
    val lyrics: List<LyricLine>,
    val format: LyricsFormat = LyricsFormat.LRC,
    val language: String = "en",
    val source: LyricsSource = LyricsSource.OFFLINE
)

/**
 * Supported lyrics formats
 */
enum class LyricsFormat {
    LRC,    // LRC (LyRiCs) - most common
    VTT,    // WebVTT
    PLAIN,  // Plain text (no sync)
    JSON,   // JSON format
    LRC_PLUS // LRC with enhancements (timetags for syllables)
}

/**
 * Where lyrics come from
 */
enum class LyricsSource {
    OFFLINE,   // Local file
    LRCLIB,    // lrclib.net API
    GENIUS,    // genius.com API
    MUSIXMATCH // musixmatch API
}

/**
 * Interface for lyrics parsers
 */
interface LyricsParser {
    fun parse(content: String): List<LyricLine>
    fun canParse(format: LyricsFormat): Boolean
}
