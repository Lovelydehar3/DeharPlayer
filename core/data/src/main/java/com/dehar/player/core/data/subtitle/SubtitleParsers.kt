package com.dehar.player.core.data.subtitle

/**
 * Parses SubRip (SRT) subtitle format
 * Format: 
 * 1
 * 00:00:01,000 --> 00:00:05,000
 * Subtitle text here
 */
class SRTParser : SubtitleParser {
    override fun parse(content: String): List<SubtitleLine> {
        val lines = mutableListOf<SubtitleLine>()
        val blocks = content.split("\n\n")

        for (block in blocks) {
            val blockLines = block.trim().split("\n")
            if (blockLines.size < 3) continue

            val timeLine = blockLines[1]
            val parts = timeLine.split("-->")
            if (parts.size != 2) continue

            val startMs = timeToMs(parts[0].trim())
            val endMs = timeToMs(parts[1].trim())
            val text = blockLines.drop(2).joinToString("\n").trim()

            if (startMs >= 0 && endMs >= 0 && text.isNotEmpty()) {
                lines.add(SubtitleLine(startMs, endMs, text))
            }
        }

        return lines
    }

    override fun canParse(format: SubtitleFormat): Boolean {
        return format == SubtitleFormat.SRT
    }

    private fun timeToMs(time: String): Long {
        val parts = time.split(":")
        if (parts.size != 3) return -1

        val hours = parts[0].toLongOrNull() ?: return -1
        val minutes = parts[1].toLongOrNull() ?: return -1
        val secondsParts = parts[2].split(",")
        if (secondsParts.size != 2) return -1

        val seconds = secondsParts[0].toLongOrNull() ?: return -1
        val millis = secondsParts[1].toLongOrNull() ?: return -1

        return hours * 3600000 + minutes * 60000 + seconds * 1000 + millis
    }
}

/**
 * Parses WebVTT (VTT) subtitle format
 * Similar to SRT but with different time format
 */
class VTTParser : SubtitleParser {
    override fun parse(content: String): List<SubtitleLine> {
        val lines = mutableListOf<SubtitleLine>()
        val blocks = content.split("\n\n")

        for (block in blocks) {
            val blockLines = block.trim().split("\n")
            if (blockLines.isEmpty()) continue

            val timeLine = blockLines.firstOrNull { it.contains("-->") } ?: continue
            val parts = timeLine.split("-->")
            if (parts.size != 2) continue

            val startMs = timeToMs(parts[0].trim())
            val endMs = timeToMs(parts[1].trim().split(" ").first())
            val textLines = blockLines.filter { !it.startsWith("WEBVTT") && !it.contains("-->") }
            val text = textLines.joinToString("\n").trim()

            if (startMs >= 0 && endMs >= 0 && text.isNotEmpty()) {
                lines.add(SubtitleLine(startMs, endMs, text))
            }
        }

        return lines
    }

    override fun canParse(format: SubtitleFormat): Boolean {
        return format == SubtitleFormat.VTT
    }

    private fun timeToMs(time: String): Long {
        val timeParts = time.split(":")
        if (timeParts.size < 2) return -1

        return try {
            when (timeParts.size) {
                2 -> {
                    val minutes = timeParts[0].toLong()
                    val secondsParts = timeParts[1].split(".")
                    val seconds = secondsParts[0].toLong()
                    val millis = (secondsParts.getOrNull(1) ?: "0").padEnd(3, '0').toLong()
                    minutes * 60000 + seconds * 1000 + millis
                }
                3 -> {
                    val hours = timeParts[0].toLong()
                    val minutes = timeParts[1].toLong()
                    val secondsParts = timeParts[2].split(".")
                    val seconds = secondsParts[0].toLong()
                    val millis = (secondsParts.getOrNull(1) ?: "0").padEnd(3, '0').toLong()
                    hours * 3600000 + minutes * 60000 + seconds * 1000 + millis
                }
                else -> -1
            }
        } catch (e: Exception) {
            -1
        }
    }
}

/**
 * Parses ASS/SSA (Advanced SubStation Alpha) format
 */
class ASSParser : SubtitleParser {
    override fun parse(content: String): List<SubtitleLine> {
        val lines = mutableListOf<SubtitleLine>()
        val contentLines = content.split("\n")
        var inDialogue = false

        for (line in contentLines) {
            if (line.startsWith("[Events]")) {
                inDialogue = true
                continue
            }

            if (inDialogue && line.startsWith("Dialogue:")) {
                val parts = line.substringAfter("Dialogue:").split(",", limit = 10)
                if (parts.size >= 10) {
                    val startMs = timeToMs(parts[1].trim())
                    val endMs = timeToMs(parts[2].trim())
                    val text = parts.drop(9).joinToString(",").trim()
                        .replace(Regex("\\{[^}]*\\}"), "") // Remove formatting tags
                        .trim()

                    if (startMs >= 0 && endMs >= 0 && text.isNotEmpty()) {
                        lines.add(SubtitleLine(startMs, endMs, text, isFormatted = true))
                    }
                }
            }
        }

        return lines
    }

    override fun canParse(format: SubtitleFormat): Boolean {
        return format == SubtitleFormat.ASS || format == SubtitleFormat.SSA
    }

    private fun timeToMs(time: String): Long {
        val parts = time.split(":")
        if (parts.size != 3) return -1

        return try {
            val hours = parts[0].toLong()
            val minutes = parts[1].toLong()
            val secondsCentis = parts[2].split(".")
            val seconds = secondsCentis[0].toLong()
            val centis = (secondsCentis.getOrNull(1) ?: "0").padEnd(2, '0').toLong()
            hours * 3600000 + minutes * 60000 + seconds * 1000 + centis * 10
        } catch (e: Exception) {
            -1
        }
    }
}

/**
 * Parses SAMI subtitle format
 */
class SAMIParser : SubtitleParser {
    override fun parse(content: String): List<SubtitleLine> {
        val lines = mutableListOf<SubtitleLine>()
        val regex = Regex("""<SYNC Start=(\d+)>(.*?)</SYNC>""", RegexOption.DOT_MATCHES_ALL)
        val matches = regex.findAll(content)

        var lastStartMs = 0L
        var lastText = ""

        for (match in matches) {
            val startMs = match.groupValues[1].toLongOrNull() ?: continue
            val content = match.groupValues[2].trim()

            if (lastText.isNotEmpty() && lastStartMs >= 0) {
                lines.add(SubtitleLine(lastStartMs, startMs, lastText))
            }

            lastStartMs = startMs
            lastText = content.replace(Regex("</?[^>]*>"), "").trim()
        }

        return lines
    }

    override fun canParse(format: SubtitleFormat): Boolean {
        return format == SubtitleFormat.SAMI
    }
}

/**
 * Parses MicroDVD subtitle format
 * Format: {startFrame}{endFrame}Subtitle text
 */
class MicroDVDParser(private val fps: Float = 25f) : SubtitleParser {
    override fun parse(content: String): List<SubtitleLine> {
        val lines = mutableListOf<SubtitleLine>()
        val lineRegex = Regex("""\{(\d+)\}\{(\d+)\}(.+)""")

        for (line in content.split("\n")) {
            val match = lineRegex.find(line) ?: continue
            val startFrame = match.groupValues[1].toLongOrNull() ?: continue
            val endFrame = match.groupValues[2].toLongOrNull() ?: continue
            val text = match.groupValues[3].trim()

            val startMs = (startFrame * 1000 / fps).toLong()
            val endMs = (endFrame * 1000 / fps).toLong()

            if (text.isNotEmpty()) {
                lines.add(SubtitleLine(startMs, endMs, text))
            }
        }

        return lines
    }

    override fun canParse(format: SubtitleFormat): Boolean {
        return format == SubtitleFormat.MICRODVD
    }
}

/**
 * Parses SubViewer 2.0 subtitle format
 */
class SubViewer2Parser : SubtitleParser {
    override fun parse(content: String): List<SubtitleLine> {
        val lines = mutableListOf<SubtitleLine>()
        val blocks = content.split("\n\n")

        for (block in blocks) {
            val blockLines = block.trim().split("\n")
            if (blockLines.size < 2) continue

            val timeLine = blockLines[0]
            val parts = timeLine.split(",")
            if (parts.size != 2) continue

            val startMs = timeToMs(parts[0].trim())
            val endMs = timeToMs(parts[1].trim())
            val text = blockLines.drop(1).joinToString("\n").trim()

            if (startMs >= 0 && endMs >= 0 && text.isNotEmpty()) {
                lines.add(SubtitleLine(startMs, endMs, text))
            }
        }

        return lines
    }

    override fun canParse(format: SubtitleFormat): Boolean {
        return format == SubtitleFormat.SUBVIEWER2
    }

    private fun timeToMs(time: String): Long {
        val parts = time.split(":")
        if (parts.size != 3) return -1

        return try {
            val hours = parts[0].toLong()
            val minutes = parts[1].toLong()
            val secondsParts = parts[2].split(".")
            val seconds = secondsParts[0].toLong()
            val millis = (secondsParts.getOrNull(1) ?: "0").padEnd(3, '0').toLong()
            hours * 3600000 + minutes * 60000 + seconds * 1000 + millis
        } catch (e: Exception) {
            -1
        }
    }
}

/**
 * Parses MPL2 subtitle format
 * Format: [startMs][endMs]Subtitle text
 */
class MPL2Parser : SubtitleParser {
    override fun parse(content: String): List<SubtitleLine> {
        val lines = mutableListOf<SubtitleLine>()
        val lineRegex = Regex("""\[(\d+)\]\[(\d+)\](.+)""")

        for (line in content.split("\n")) {
            val match = lineRegex.find(line) ?: continue
            val startMs = match.groupValues[1].toLongOrNull()?.times(100) ?: continue
            val endMs = match.groupValues[2].toLongOrNull()?.times(100) ?: continue
            val text = match.groupValues[3].trim()

            if (text.isNotEmpty()) {
                lines.add(SubtitleLine(startMs, endMs, text))
            }
        }

        return lines
    }

    override fun canParse(format: SubtitleFormat): Boolean {
        return format == SubtitleFormat.MPL2
    }
}

/**
 * Parses TMPlayer subtitle format
 */
class TMPlayerParser : SubtitleParser {
    override fun parse(content: String): List<SubtitleLine> {
        val lines = mutableListOf<SubtitleLine>()

        for (line in content.split("\n")) {
            if (line.isEmpty()) continue
            val timeRegex = Regex("""^(\d{1,2}):(\d{2}):(\d{2}):(.+)$""")
            val match = timeRegex.find(line) ?: continue

            val hours = match.groupValues[1].toLongOrNull() ?: continue
            val minutes = match.groupValues[2].toLongOrNull() ?: continue
            val seconds = match.groupValues[3].toLongOrNull() ?: continue
            val text = match.groupValues[4].trim()

            val timeMs = hours * 3600000 + minutes * 60000 + seconds * 1000

            if (text.isNotEmpty()) {
                lines.add(SubtitleLine(timeMs, timeMs + 5000, text))
            }
        }

        return lines
    }

    override fun canParse(format: SubtitleFormat): Boolean {
        return format == SubtitleFormat.TMPLAYER
    }
}

/**
 * Parses PJS (Phoenix Subtitles) format
 */
class PJSParser : SubtitleParser {
    override fun parse(content: String): List<SubtitleLine> {
        val lines = mutableListOf<SubtitleLine>()

        for (line in content.split("\n")) {
            if (line.isEmpty()) continue
            val parts = line.split(",")
            if (parts.size != 3) continue

            val startMs = parts[0].toLongOrNull() ?: continue
            val endMs = parts[1].toLongOrNull() ?: continue
            val text = parts[2].trim()

            if (text.isNotEmpty()) {
                lines.add(SubtitleLine(startMs, endMs, text))
            }
        }

        return lines
    }

    override fun canParse(format: SubtitleFormat): Boolean {
        return format == SubtitleFormat.PJS
    }
}

/**
 * Parses PowerDivX subtitle format
 */
class PowerDivXParser : SubtitleParser {
    override fun parse(content: String): List<SubtitleLine> {
        val lines = mutableListOf<SubtitleLine>()
        val regex = Regex("""^\{(\d+)\}\{(\d+)\}(.+)""")

        for (line in content.split("\n")) {
            val match = regex.find(line) ?: continue
            val startMs = match.groupValues[1].toLongOrNull() ?: continue
            val endMs = match.groupValues[2].toLongOrNull() ?: continue
            val text = match.groupValues[3].trim()

            if (text.isNotEmpty()) {
                lines.add(SubtitleLine(startMs, endMs, text))
            }
        }

        return lines
    }

    override fun canParse(format: SubtitleFormat): Boolean {
        return format == SubtitleFormat.POWERDIVX
    }
}

/**
 * Factory for creating appropriate parser based on format
 */
object SubtitleParserFactory {
    fun getParser(format: SubtitleFormat): SubtitleParser {
        return when (format) {
            SubtitleFormat.SRT -> SRTParser()
            SubtitleFormat.VTT -> VTTParser()
            SubtitleFormat.ASS, SubtitleFormat.SSA -> ASSParser()
            SubtitleFormat.SAMI -> SAMIParser()
            SubtitleFormat.MICRODVD -> MicroDVDParser()
            SubtitleFormat.SUBVIEWER2 -> SubViewer2Parser()
            SubtitleFormat.MPL2 -> MPL2Parser()
            SubtitleFormat.TMPLAYER -> TMPlayerParser()
            SubtitleFormat.PJS -> PJSParser()
            SubtitleFormat.POWERDIVX -> PowerDivXParser()
            SubtitleFormat.SUBRIP -> SRTParser()
            SubtitleFormat.PGS -> SRTParser() // Bitmap PGS requires special handling
        }
    }
}
