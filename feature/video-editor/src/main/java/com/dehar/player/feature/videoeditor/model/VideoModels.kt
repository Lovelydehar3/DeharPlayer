package com.dehar.player.feature.videoeditor.model

import androidx.compose.runtime.Immutable
import java.io.File

@Immutable
data class VideoEditProject(
    val id: String = "",
    val sourceVideoPath: String = "",
    val projectName: String = "",
    val videoDuration: Long = 0L, // milliseconds
    val videoWidth: Int = 0,
    val videoHeight: Int = 0,
    val videoCodec: String = "h264",
    val audioCodec: String = "aac",
    val frameRate: Double = 30.0,
    val bitrate: String = "5000k",
    val createdAt: Long = System.currentTimeMillis(),
    val lastModified: Long = System.currentTimeMillis()
)

@Immutable
data class VideoTrim(
    val id: String = "",
    val startTime: Long = 0L, // milliseconds
    val endTime: Long = 0L,
    val duration: Long = 0L
) {
    fun isValid(): Boolean {
        return startTime >= 0 && endTime > startTime && duration > 0
    }
}

@Immutable
data class VideoFilter(
    val id: String = "",
    val type: FilterType = FilterType.BRIGHTNESS,
    val value: Float = 1.0f,
    val enabled: Boolean = true
) {
    fun getFFmpegString(): String {
        return when (type) {
            FilterType.BRIGHTNESS -> "eq=brightness=${value}"
            FilterType.CONTRAST -> "eq=contrast=${value}"
            FilterType.SATURATION -> "eq=saturation=${value}"
            FilterType.HUE -> "hue=h=${value * 360}"
            FilterType.BLUR -> "boxblur=${value}"
            FilterType.SHARPEN -> "unsharp=1.5:0.5:${value}"
            FilterType.GRAYSCALE -> "format=gray"
            FilterType.SEPIA -> "colorchannelmixer=.393:.769:.189:0:.349:.686:.168:0:.272:.534:.131"
            FilterType.INVERT -> "negate"
            FilterType.FLIP_H -> "hflip"
            FilterType.FLIP_V -> "vflip"
            FilterType.ROTATE_90 -> "transpose=1"
            FilterType.ROTATE_180 -> "transpose=1,transpose=1"
            FilterType.ROTATE_270 -> "transpose=2"
        }
    }
}

enum class FilterType {
    BRIGHTNESS, CONTRAST, SATURATION, HUE, BLUR, SHARPEN,
    GRAYSCALE, SEPIA, INVERT, FLIP_H, FLIP_V, ROTATE_90,
    ROTATE_180, ROTATE_270
}

@Immutable
data class VideoClip(
    val id: String = "",
    val startTime: Long = 0L,
    val endTime: Long = 0L,
    val filters: List<VideoFilter> = emptyList(),
    val speed: Float = 1.0f,
    val volume: Float = 1.0f
) {
    fun getDuration(): Long = endTime - startTime
}

enum class VideoEditState {
    IDLE, LOADING, TRIMMING, FILTERING, ENCODING, SUCCESS, ERROR
}

@Immutable
data class EncodingProgress(
    val currentFrame: Long = 0L,
    val totalFrames: Long = 0L,
    val currentTime: Long = 0L, // milliseconds
    val totalTime: Long = 0L, // milliseconds
    val outputSize: Long = 0L, // bytes
    val fps: Double = 0.0,
    val elapsedTime: Long = 0L // milliseconds
) {
    fun getProgressPercentage(): Int {
        return if (totalFrames > 0) {
            ((currentFrame.toDouble() / totalFrames.toDouble()) * 100).toInt().coerceIn(0, 100)
        } else {
            0
        }
    }

    fun getEstimatedTimeRemaining(): Long {
        return if (currentFrame > 0) {
            val avgFrameTime = elapsedTime.toDouble() / currentFrame.toDouble()
            ((totalFrames - currentFrame) * avgFrameTime).toLong()
        } else {
            0L
        }
    }
}

@Immutable
data class VideoEditorUiState(
    val project: VideoEditProject? = null,
    val selectedClip: VideoClip? = null,
    val clips: List<VideoClip> = emptyList(),
    val filters: List<VideoFilter> = emptyList(),
    val editState: VideoEditState = VideoEditState.IDLE,
    val encodingProgress: EncodingProgress? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val outputPath: String? = null,
    val isPreviewPlaying: Boolean = false,
    val previewCurrentTime: Long = 0L
)

@Immutable
data class ExportSettings(
    val quality: ExportQuality = ExportQuality.HIGH,
    val format: VideoFormat = VideoFormat.MP4,
    val audioEnabled: Boolean = true,
    val audioBitrate: String = "128k",
    val preserveAspectRatio: Boolean = true
)

enum class ExportQuality {
    LOW, MEDIUM, HIGH, ULTRA
}

enum class VideoFormat {
    MP4, MKV, AVI, MOV, WEBM
}

fun ExportQuality.getBitrate(): String {
    return when (this) {
        ExportQuality.LOW -> "2000k"
        ExportQuality.MEDIUM -> "5000k"
        ExportQuality.HIGH -> "10000k"
        ExportQuality.ULTRA -> "20000k"
    }
}

fun VideoFormat.getExtension(): String {
    return when (this) {
        VideoFormat.MP4 -> ".mp4"
        VideoFormat.MKV -> ".mkv"
        VideoFormat.AVI -> ".avi"
        VideoFormat.MOV -> ".mov"
        VideoFormat.WEBM -> ".webm"
    }
}
