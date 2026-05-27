package com.dehar.player.feature.subtitle.domain

class SAMIParser : SubtitleParser {
    override fun parse(content: String): List<SubtitleLine> {
        val result = mutableListOf<SubtitleLine>()
        val regex = Regex("""<SYNC\s+Start=(\d+)[^>]*>\s*<P[^>]*>(.*?)(?=<SYNC|\z)""",
            setOf(RegexOption.DOT_MATCHES_ALL, RegexOption.IGNORE_CASE))
        val matches = regex.findAll(content).toList()
        
        matches.forEachIndexed { i, m ->
            val startMs = m.groupValues[1].toLong()
            val text = m.groupValues[2]
                .replace(Regex("<[^>]+>"), "")
                .replace("&nbsp;", " ").trim()
            if (text.isNotEmpty() && text != "&nbsp;") {
                val endMs = if (i + 1 < matches.size) matches[i+1].groupValues[1].toLong()
                            else startMs + 5000L
                result.add(SubtitleLine(startMs, endMs, text, result.size))
            }
        }
        return result
    }

    override fun supports(format: SubtitleFormat) = format is SubtitleFormat.SAMI
}
