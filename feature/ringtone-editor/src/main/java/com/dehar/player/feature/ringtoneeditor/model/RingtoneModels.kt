package com.dehar.player.feature.ringtoneEditor.model

import androidx.annotation.IntRange

/**
 * Represents a ringtone audio sample
 */
data class RingtoneAudio(
    val id: Long,
    val name: String,
    val path: String,
    val durationMs: Long,
    val bitrate: Int = 128,
    val sampleRate: Int = 44100
)

/**
 * Waveform data for visual representation
 */
data class WaveformData(
    val samples: FloatArray,
    val durationMs: Long,
    val sampleRate: Int,
    val channelCount: Int = 1
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as WaveformData

        if (!samples.contentEquals(other.samples)) return false
        if (durationMs != other.durationMs) return false
        if (sampleRate != other.sampleRate) return false
        if (channelCount != other.channelCount) return false

        return true
    }

    override fun hashCode(): Int {
        var result = samples.contentHashCode()
        result = 31 * result + durationMs.hashCode()
        result = 31 * result + sampleRate
        result = 31 * result + channelCount
        return result
    }
}

/**
 * Trim state for ringtone editor
 */
data class TrimState(
    @IntRange(from = 0)
    val startMs: Long = 0,
    
    @IntRange(from = 0)
    val endMs: Long = 0,
    
    val isDraggingStart: Boolean = false,
    val isDraggingEnd: Boolean = false
) {
    fun getTrimmedDuration(): Long = endMs - startMs
    
    fun isValid(): Boolean = startMs < endMs && endMs > 0
}

/**
 * Playback state for ringtone preview
 */
data class PlaybackState(
    val isPlaying: Boolean = false,
    val currentPositionMs: Long = 0,
    val volume: Float = 0.8f,
    val playbackSpeed: Float = 1.0f
)

/**
 * Ringtone type classification
 */
enum class RingtoneType {
    NOTIFICATION,
    ALARM,
    RINGTONE,
    CUSTOM
}

/**
 * Ringtone editor UI state
 */
data class RingtoneEditorUiState(
    val selectedAudio: RingtoneAudio? = null,
    val waveformData: WaveformData? = null,
    val trimState: TrimState = TrimState(),
    val playbackState: PlaybackState = PlaybackState(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSaving: Boolean = false,
    val ringtoneType: RingtoneType = RingtoneType.CUSTOM
)
