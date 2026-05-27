package com.dehar.player.feature.subtitle.domain

class SSAParser : SubtitleParser {
    override fun parse(content: String): List<SubtitleLine> {
        val result = mutableListOf<SubtitleLine>()
        var inEvents = false
        val formatFields = mutableListOf<String>()
        
        content.lines().forEach { line ->
            when {
                line.trim() == "[Events]" -> inEvents = true
                inEvents && line.startsWith("Format:") ->
                    formatFields.addAll(line.removePrefix("Format:").split(",").map { it.trim() })
                inEvents && line.startsWith("Dialogue:") && formatFields.isNotEmpty() -> {
                    val parts = line.removePrefix("Dialogue:").split(",", limit = formatFields.size)
                    if (parts.size >= formatFields.size) {
                        val si = formatFields.indexOf("Start")
                        val ei = formatFields.indexOf("End")
                        val ti = formatFields.indexOf("Text")
                        if (si >= 0 && ei >= 0 && ti >= 0) {
                            val startMs = parseSSATime(parts[si].trim())
                            val endMs = parseSSATime(parts[ei].trim())
                            val rawText = parts.drop(ti).joinToString(",")
                            val text = rawText
                                .replace(Regex("\\\\{[^}]*\\\\}"), "") // strip override tags
                                .replace("\\\\N", "\n").replace("\\\\n", "\n").trim()
                            if (text.isNotEmpty())
                                result.add(SubtitleLine(startMs, endMs, text, result.size))
                        }
                    }
                }
            }
        }
        return result
    }
    
    override fun supports(format: SubtitleFormat) = format is SubtitleFormat.SSA || format is SubtitleFormat.ASS
    
    private fun parseSSATime(t: String): Long {
        // H:MM:SS.cc
        val p = t.split(":", ".")
        return if (p.size >= 4)
            (p[0].toLong() * 3600 + p[1].toLong() * 60 + p[2].toLong()) * 1000 + p[3].toLong() * 10
        else 0L
    }
}
