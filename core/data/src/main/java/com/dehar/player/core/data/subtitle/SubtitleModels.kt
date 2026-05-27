package com.dehar.player.core.data.subtitle

import java.io.File

/**
 * Represents a subtitle track
 */
data class SubtitleTrack(
    val id: Long,
    val name: String,
    val language: String,
    val format: SubtitleFormat,
    val path: String,
    val encoding: String = "UTF-8",
    val isExternal: Boolean = true
)

/**
 * Represents a single subtitle line
 */
data class SubtitleLine(
    val startTimeMs: Long,
    val endTimeMs: Long,
    val text: String,
    val isFormatted: Boolean = false
)

/**
 * Supported subtitle formats
 */
enum class SubtitleFormat(val extension: String, val displayName: String) {
    SRT("srt", "SubRip"),
    ASS("ass", "Advanced SubStation Alpha"),
    SSA("ssa", "SubStation Alpha"),
    VTT("vtt", "WebVTT"),
    SAMI("sami", "Synchronized Accessible Media Interchange"),
    MICRODVD("sub", "MicroDVD"),
    SUBVIEWER2("sub", "SubViewer 2.0"),
    MPL2("mpl", "MPL2"),
    TMPLAYER("txt", "TMPlayer"),
    PJS("pjs", "Phoenix Subtitles"),
    POWERDIVX("psb", "PowerDivX"),
    SUBRIP("srt", "SubRip"),
    PGS("sup", "Blu-ray PGS")
}

/**
 * Interface for subtitle parsers
 */
interface SubtitleParser {
    fun parse(content: String): List<SubtitleLine>
    fun canParse(format: SubtitleFormat): Boolean
}
