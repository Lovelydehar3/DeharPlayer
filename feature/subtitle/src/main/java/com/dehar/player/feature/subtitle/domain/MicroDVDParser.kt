package com.dehar.player.feature.subtitle.domain

class MicroDVDParser(private val videoFps: Float = 25f) : SubtitleParser {
    override fun parse(content: String): List<SubtitleLine> {
        var fps = videoFps
        val result = mutableListOf<SubtitleLine>()
        content.lines().forEach { line ->
            val m = Regex("""\{(\d+)\}\{(\d+)\}(.*)""").matchEntire(line.trim()) ?: return@forEach
            val sf = m.groupValues[1].toLong()
            val ef = m.groupValues[2].toLong()
            if (sf == 0L && ef == 0L) { // FPS header
                m.groupValues[3].trim().toFloatOrNull()?.let { fps = it }
                return@forEach
            }
            result.add(
                SubtitleLine(
                    startTimeMs = (sf * 1000 / fps).toLong(),
                    endTimeMs = (ef * 1000 / fps).toLong(),
                    text = m.groupValues[3].replace("|", "\n"),
                    index = result.size
                )
            )
        }
        return result
    }

    override fun supports(format: SubtitleFormat) = format is SubtitleFormat.MICRODVD
}
