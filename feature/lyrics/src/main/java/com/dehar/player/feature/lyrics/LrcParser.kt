package com.dehar.player.feature.lyrics

data class LrcLine(val timestampMs: Long, val text: String)

object LrcParser {
    private val lineRegex = Regex("""\[(\d{2}):(\d{2})\.(\d{2,3})\](.*)""")
    
    fun parse(lrc: String): List<LrcLine> {
        return lrc.lines().flatMap { line ->
            lineRegex.findAll(line).mapNotNull { m ->
                val text = m.groupValues[4].trim()
                if (text.isEmpty()) return@mapNotNull null
                val min = m.groupValues[1].toLong()
                val sec = m.groupValues[2].toLong()
                val ms  = m.groupValues[3].padEnd(3,'0').take(3).toLong()
                LrcLine((min * 60 + sec) * 1000 + ms, text)
            }.toList()
        }.sortedBy { it.timestampMs }
    }
}
