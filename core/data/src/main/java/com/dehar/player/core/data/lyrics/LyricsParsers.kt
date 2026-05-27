package com.dehar.player.core.data.lyrics

/**
 * Parses LRC (LyRiCs) format
 * Format:
 * [ti:Title]
 * [ar:Artist]
 * [al:Album]
 * [00:12.00]Lyric text here
 * [00:15.50]More lyrics
 */
class LRCParser : LyricsParser {
    override fun parse(content: String): List<LyricLine> {
        val lines = mutableListOf<LyricLine>()
        
        for (line in content.split("\n")) {
            val trimmed = line.trim()
            if (trimmed.isEmpty()) continue

            // Skip metadata lines
            if (trimmed.startsWith("[ti:") || trimmed.startsWith("[ar:") || 
                trimmed.startsWith("[al:") || trimmed.startsWith("[by:") ||
                trimmed.startsWith("[offset:")) {
                continue
            }

            // Parse time-synced lyrics: [MM:SS.ms]text
            val timeRegex = Regex("""\[(\d{1,2}):(\d{2})(?:\.(\d{2,3}))?\](.*)""")
            val match = timeRegex.find(trimmed)
            
            if (match != null) {
                val minutes = match.groupValues[1].toLongOrNull() ?: continue
                val seconds = match.groupValues[2].toLongOrNull() ?: continue
                val millis = match.groupValues[3].let {
                    if (it.isEmpty()) 0L else it.padEnd(3, '0').toLongOrNull() ?: 0L
                }
                val text = match.groupValues[4].trim()

                if (text.isNotEmpty()) {
                    val timeMs = minutes * 60000 + seconds * 1000 + millis
                    lines.add(LyricLine(timeMs, text, isSynced = true))
                }
            }
        }

        return lines.sortedBy { it.timeMs }
    }

    override fun canParse(format: LyricsFormat): Boolean {
        return format == LyricsFormat.LRC || format == LyricsFormat.LRC_PLUS
    }
}

/**
 * Parses plain text lyrics (no sync)
 */
class PlainTextLyricsParser : LyricsParser {
    override fun parse(content: String): List<LyricLine> {
        val lines = mutableListOf<LyricLine>()
        val lyricsLines = content.split("\n")

        var currentTimeMs = 0L
        for (line in lyricsLines) {
            val trimmed = line.trim()
            if (trimmed.isNotEmpty()) {
                lines.add(
                    LyricLine(
                        timeMs = currentTimeMs,
                        text = trimmed,
                        isSynced = false
                    )
                )
                currentTimeMs += 3000 // 3 seconds per line estimate
            }
        }

        return lines
    }

    override fun canParse(format: LyricsFormat): Boolean {
        return format == LyricsFormat.PLAIN
    }
}

/**
 * Parses WebVTT lyrics format
 */
class VTTLyricsParser : LyricsParser {
    override fun parse(content: String): List<LyricLine> {
        val lines = mutableListOf<LyricLine>()
        val blocks = content.split("\n\n")

        for (block in blocks) {
            val blockLines = block.trim().split("\n")
            if (blockLines.size < 2) continue

            val timeLine = blockLines[0]
            val parts = timeLine.split("-->")
            if (parts.size != 2) continue

            val timeMs = vttTimeToMs(parts[0].trim())
            val text = blockLines.drop(1).joinToString("\n").trim()

            if (timeMs >= 0 && text.isNotEmpty()) {
                lines.add(LyricLine(timeMs, text, isSynced = true))
            }
        }

        return lines
    }

    override fun canParse(format: LyricsFormat): Boolean {
        return format == LyricsFormat.VTT
    }

    private fun vttTimeToMs(time: String): Long {
        val parts = time.split(":")
        return if (parts.size == 3) {
            val hours = parts[0].toLongOrNull() ?: return -1
            val minutes = parts[1].toLongOrNull() ?: return -1
            val secondsParts = parts[2].split(".")
            val seconds = secondsParts[0].toLongOrNull() ?: return -1
            val millis = (secondsParts.getOrNull(1) ?: "0").padEnd(3, '0').toLongOrNull() ?: 0
            hours * 3600000 + minutes * 60000 + seconds * 1000 + millis
        } else {
            -1
        }
    }
}

/**
 * Factory for creating appropriate parser based on format
 */
object LyricsParserFactory {
    fun getParser(format: LyricsFormat): LyricsParser {
        return when (format) {
            LyricsFormat.LRC, LyricsFormat.LRC_PLUS -> LRCParser()
            LyricsFormat.VTT -> VTTLyricsParser()
            LyricsFormat.PLAIN, LyricsFormat.JSON -> PlainTextLyricsParser()
        }
    }
}
