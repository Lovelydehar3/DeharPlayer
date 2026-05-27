package com.dehar.player.feature.subtitle.domain

import androidx.compose.runtime.Stable

@Stable
data class SubtitleLine(
    val startTimeMs: Long,
    val endTimeMs: Long,
    val text: String,
    val index: Int
)

@Stable
sealed class SubtitleFormat(val extension: String) {
    object SRT : SubtitleFormat("srt")
    object ASS : SubtitleFormat("ass")
    object SSA : SubtitleFormat("ssa")
    object VTT : SubtitleFormat("vtt")
    object SAMI : SubtitleFormat("smi")
    object SUB : SubtitleFormat("sub")
    object IDX : SubtitleFormat("idx")
    object MICRODVD : SubtitleFormat("sub")
    object MPLAYER : SubtitleFormat("txt")
    object PJS : SubtitleFormat("pjs")
    object WEBVTT : SubtitleFormat("vtt")
    object BITMAP : SubtitleFormat("idx")
}

interface SubtitleParser {
    fun parse(content: String): List<SubtitleLine>
    fun supports(format: SubtitleFormat): Boolean
}

class SRTParser : SubtitleParser {
    override fun parse(content: String): List<SubtitleLine> {
        val lines = mutableListOf<SubtitleLine>()
        val entries = content.split(Regex("\r?\n\r?\n"))

        entries.forEach { entry ->
            val parts = entry.trim().split(Regex("\r?\n"), limit = 3)
            if (parts.size >= 3) {
                try {
                    val timecodes = parts[1].split(" --> ")
                    if (timecodes.size == 2) {
                        val startMs = parseTimeCode(timecodes[0].trim())
                        val endMs = parseTimeCode(timecodes[1].trim().split("[")[0].trim())
                        val text = parts[2].trim()
                        val index = lines.size

                        lines.add(
                            SubtitleLine(
                                startTimeMs = startMs,
                                endTimeMs = endMs,
                                text = text,
                                index = index
                            )
                        )
                    }
                } catch (e: Exception) {
                    // Skip malformed entries
                }
            }
        }

        return lines
    }

    override fun supports(format: SubtitleFormat) = format is SubtitleFormat.SRT

    private fun parseTimeCode(timeCode: String): Long {
        val parts = timeCode.split(":")
        if (parts.size < 3) return 0L

        return try {
            val hours = parts[0].toLong() * 3600 * 1000
            val minutes = parts[1].toLong() * 60 * 1000
            val secondsAndMs = parts[2].split(",")
            val seconds = secondsAndMs[0].toLong() * 1000
            val ms = if (secondsAndMs.size > 1) {
                secondsAndMs[1].take(3).padEnd(3, '0').toLong()
            } else {
                0L
            }
            hours + minutes + seconds + ms
        } catch (e: Exception) {
            0L
        }
    }
}

class WebVTTParser : SubtitleParser {
    override fun parse(content: String): List<SubtitleLine> {
        val lines = mutableListOf<SubtitleLine>()
        val entries = content.split(Regex("\r?\n\r?\n")).filter { it.isNotBlank() }

        entries.forEach { entry ->
            val parts = entry.trim().split(Regex("\r?\n"), limit = 3)
            if (parts.size >= 3) {
                try {
                    val timecodes = parts[1].split(" --> ")
                    if (timecodes.size == 2) {
                        val startMs = parseWebVTTTimeCode(timecodes[0].trim())
                        val endMs = parseWebVTTTimeCode(timecodes[1].trim().split(Regex("\\s"))[0])
                        val text = parts[2].trim()
                        val index = lines.size

                        lines.add(
                            SubtitleLine(
                                startTimeMs = startMs,
                                endTimeMs = endMs,
                                text = text,
                                index = index
                            )
                        )
                    }
                } catch (e: Exception) {
                    // Skip malformed entries
                }
            }
        }

        return lines
    }

    override fun supports(format: SubtitleFormat) = format is SubtitleFormat.VTT || format is SubtitleFormat.WEBVTT

    private fun parseWebVTTTimeCode(timeCode: String): Long {
        val parts = timeCode.split(":")
        return try {
            when (parts.size) {
                3 -> { // HH:MM:SS.mmm
                    val hours = parts[0].toLong() * 3600 * 1000
                    val minutes = parts[1].toLong() * 60 * 1000
                    val secondsAndMs = parts[2].split(".")
                    val seconds = secondsAndMs[0].toLong() * 1000
                    val ms = if (secondsAndMs.size > 1) {
                        secondsAndMs[1].take(3).padEnd(3, '0').toLong()
                    } else {
                        0L
                    }
                    hours + minutes + seconds + ms
                }
                2 -> { // MM:SS.mmm
                    val minutes = parts[0].toLong() * 60 * 1000
                    val secondsAndMs = parts[1].split(".")
                    val seconds = secondsAndMs[0].toLong() * 1000
                    val ms = if (secondsAndMs.size > 1) {
                        secondsAndMs[1].take(3).padEnd(3, '0').toLong()
                    } else {
                        0L
                    }
                    minutes + seconds + ms
                }
                else -> 0L
            }
        } catch (e: Exception) {
            0L
        }
    }
}

class SubtitleParserFactory {
    fun getParser(format: SubtitleFormat): SubtitleParser {
        return when (format) {
            SubtitleFormat.SRT, SubtitleFormat.SUB -> SRTParser()
            SubtitleFormat.VTT, SubtitleFormat.WEBVTT -> WebVTTParser()
            SubtitleFormat.SSA, SubtitleFormat.ASS -> SSAParser()
            SubtitleFormat.SAMI -> SAMIParser()
            SubtitleFormat.MICRODVD -> MicroDVDParser()
            else -> SRTParser() // Default fallback
        }
    }

    fun detectFormat(fileName: String, firstContent: String): SubtitleFormat = when {
        fileName.endsWith(".ass", true) -> SubtitleFormat.ASS
        fileName.endsWith(".ssa", true) -> SubtitleFormat.SSA
        fileName.endsWith(".smi", true) -> SubtitleFormat.SAMI
        fileName.endsWith(".sub", true) && firstContent.contains(Regex("^\\{\\d+\\}")) -> SubtitleFormat.MICRODVD
        fileName.endsWith(".vtt", true) -> SubtitleFormat.VTT
        else -> SubtitleFormat.SRT
    }
}
