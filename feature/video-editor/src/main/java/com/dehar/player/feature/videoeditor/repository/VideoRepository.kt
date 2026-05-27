package com.dehar.player.feature.videoeditor.repository

import android.content.Context
import android.media.MediaMetadataRetriever
import android.os.Build
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton
import com.dehar.player.feature.videoeditor.model.EncodingProgress
import com.dehar.player.feature.videoeditor.model.ExportSettings
import com.dehar.player.feature.videoeditor.model.VideoClip
import com.dehar.player.feature.videoeditor.model.VideoEditProject
import com.dehar.player.feature.videoeditor.model.VideoFilter
import com.dehar.player.feature.videoeditor.model.VideoTrim

/**
 * Repository for video editing operations using FFmpeg
 */
@Singleton
class VideoRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val projectsDir = File(
        context.getExternalFilesDir(null),
        "VideoProjects"
    ).apply {
        mkdirs()
    }

    private val outputDir = File(
        context.getExternalFilesDir(null),
        "VideoOutput"
    ).apply {
        mkdirs()
    }

    /**
     * Get video metadata (duration, resolution, codec, etc.)
     */
    suspend fun getVideoMetadata(videoPath: String): VideoEditProject? {
        return withContext(Dispatchers.IO) {
            try {
                val retriever = MediaMetadataRetriever()
                retriever.setDataSource(videoPath)

                val duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                    ?.toLongOrNull() ?: 0L

                val width = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)
                    ?.toIntOrNull() ?: 1920

                val height = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)
                    ?.toIntOrNull() ?: 1080

                val videoCodec = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE)
                    ?: "video/avc"

                retriever.release()

                val projectName = File(videoPath).nameWithoutExtension

                VideoEditProject(
                    id = videoPath.hashCode().toString(),
                    sourceVideoPath = videoPath,
                    projectName = projectName,
                    videoDuration = duration,
                    videoWidth = width,
                    videoHeight = height,
                    videoCodec = videoCodec
                )
            } catch (e: Exception) {
                null
            }
        }
    }

    /**
     * Trim video using FFmpeg
     */
    suspend fun trimVideo(
        project: VideoEditProject,
        trim: VideoTrim,
        exportSettings: ExportSettings,
        onProgress: (EncodingProgress) -> Unit
    ): String? {
        return withContext(Dispatchers.IO) {
            try {
                val outputFile = File(
                    outputDir,
                    "${project.projectName}_trimmed${exportSettings.format.getExtension()}"
                )

                val startTimeSeconds = trim.startTime / 1000.0
                val durationSeconds = (trim.endTime - trim.startTime) / 1000.0

                val ffmpegCommand = buildString {
                    append("ffmpeg -i ")
                    append("\"${project.sourceVideoPath}\" ")
                    append("-ss $startTimeSeconds ")
                    append("-t $durationSeconds ")
                    append("-c:v libx264 ")
                    append("-b:v ${exportSettings.quality.getBitrate()} ")
                    if (exportSettings.audioEnabled) {
                        append("-c:a aac ")
                        append("-b:a ${exportSettings.audioBitrate} ")
                    } else {
                        append("-an ")
                    }
                    append("-preset medium ")
                    append("-y ")
                    append("\"${outputFile.absolutePath}\"")
                }

                executeFFmpegCommand(ffmpegCommand, onProgress)
                outputFile.absolutePath
            } catch (e: Exception) {
                null
            }
        }
    }

    /**
     * Apply filters to video
     */
    suspend fun applyFilters(
        project: VideoEditProject,
        filters: List<VideoFilter>,
        exportSettings: ExportSettings,
        onProgress: (EncodingProgress) -> Unit
    ): String? {
        return withContext(Dispatchers.IO) {
            try {
                if (filters.isEmpty()) {
                    return@withContext project.sourceVideoPath
                }

                val outputFile = File(
                    outputDir,
                    "${project.projectName}_filtered${exportSettings.format.getExtension()}"
                )

                val filterChain = filters
                    .filter { it.enabled }
                    .joinToString(",") { it.getFFmpegString() }

                val ffmpegCommand = buildString {
                    append("ffmpeg -i ")
                    append("\"${project.sourceVideoPath}\" ")
                    append("-vf \"$filterChain\" ")
                    append("-c:v libx264 ")
                    append("-b:v ${exportSettings.quality.getBitrate()} ")
                    if (exportSettings.audioEnabled) {
                        append("-c:a aac ")
                        append("-b:a ${exportSettings.audioBitrate} ")
                    } else {
                        append("-an ")
                    }
                    append("-preset medium ")
                    append("-y ")
                    append("\"${outputFile.absolutePath}\"")
                }

                executeFFmpegCommand(ffmpegCommand, onProgress)
                outputFile.absolutePath
            } catch (e: Exception) {
                null
            }
        }
    }

    /**
     * Rotate video
     */
    suspend fun rotateVideo(
        project: VideoEditProject,
        rotation: Int, // 90, 180, 270
        exportSettings: ExportSettings,
        onProgress: (EncodingProgress) -> Unit
    ): String? {
        return withContext(Dispatchers.IO) {
            try {
                val outputFile = File(
                    outputDir,
                    "${project.projectName}_rotated${exportSettings.format.getExtension()}"
                )

                val rotateValue = when (rotation) {
                    90 -> "1"
                    180 -> "2"
                    270 -> "2"
                    else -> "0"
                }

                val ffmpegCommand = buildString {
                    append("ffmpeg -i ")
                    append("\"${project.sourceVideoPath}\" ")
                    append("-vf \"transpose=$rotateValue\" ")
                    append("-c:v libx264 ")
                    append("-b:v ${exportSettings.quality.getBitrate()} ")
                    if (exportSettings.audioEnabled) {
                        append("-c:a aac ")
                        append("-b:a ${exportSettings.audioBitrate} ")
                    }
                    append("-preset medium ")
                    append("-y ")
                    append("\"${outputFile.absolutePath}\"")
                }

                executeFFmpegCommand(ffmpegCommand, onProgress)
                outputFile.absolutePath
            } catch (e: Exception) {
                null
            }
        }
    }

    /**
     * Change video speed
     */
    suspend fun changeSpeed(
        project: VideoEditProject,
        speed: Float,
        exportSettings: ExportSettings,
        onProgress: (EncodingProgress) -> Unit
    ): String? {
        return withContext(Dispatchers.IO) {
            try {
                val outputFile = File(
                    outputDir,
                    "${project.projectName}_speed${exportSettings.format.getExtension()}"
                )

                val speedValue = String.format("%.2f", speed)

                val ffmpegCommand = buildString {
                    append("ffmpeg -i ")
                    append("\"${project.sourceVideoPath}\" ")
                    append("-filter:v \"setpts=PTS/$speedValue\" ")
                    append("-filter:a \"atempo=$speedValue\" ")
                    append("-c:v libx264 ")
                    append("-b:v ${exportSettings.quality.getBitrate()} ")
                    if (exportSettings.audioEnabled) {
                        append("-c:a aac ")
                        append("-b:a ${exportSettings.audioBitrate} ")
                    }
                    append("-preset medium ")
                    append("-y ")
                    append("\"${outputFile.absolutePath}\"")
                }

                executeFFmpegCommand(ffmpegCommand, onProgress)
                outputFile.absolutePath
            } catch (e: Exception) {
                null
            }
        }
    }

    /**
     * Extract frame/thumbnail from video
     */
    suspend fun extractFrame(
        videoPath: String,
        timeMs: Long
    ): File? {
        return withContext(Dispatchers.IO) {
            try {
                val outputFile = File(
                    outputDir,
                    "thumb_${System.currentTimeMillis()}.jpg"
                )

                val timeSeconds = timeMs / 1000.0

                val ffmpegCommand = buildString {
                    append("ffmpeg -i ")
                    append("\"$videoPath\" ")
                    append("-ss $timeSeconds ")
                    append("-vframes 1 ")
                    append("-y ")
                    append("\"${outputFile.absolutePath}\"")
                }

                executeFFmpegCommand(ffmpegCommand) { }
                outputFile.takeIf { it.exists() }
            } catch (e: Exception) {
                null
            }
        }
    }

    /**
     * Save project
     */
    suspend fun saveProject(project: VideoEditProject): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                // TODO: Implement project serialization and saving
                true
            } catch (e: Exception) {
                false
            }
        }
    }

    /**
     * Load project
     */
    suspend fun loadProject(projectName: String): VideoEditProject? {
        return withContext(Dispatchers.IO) {
            try {
                // TODO: Implement project loading
                null
            } catch (e: Exception) {
                null
            }
        }
    }

    /**
     * Delete output file
     */
    suspend fun deleteOutput(filePath: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                File(filePath).delete()
            } catch (e: Exception) {
                false
            }
        }
    }

    /**
     * Execute FFmpeg command (placeholder for actual FFmpeg integration)
     */
    private suspend fun executeFFmpegCommand(
        command: String,
        onProgress: (EncodingProgress) -> Unit
    ): Boolean {
        return withContext(Dispatchers.Default) {
            try {
                // TODO: Implement actual FFmpeg command execution
                // This would require integrating with FFmpeg via:
                // 1. FFmpeg static binary (ffmpeg-kit-android)
                // 2. Or native C bindings via FFmpeg shared libraries
                // For now, we simulate the command execution
                
                onProgress(EncodingProgress(
                    currentFrame = 100,
                    totalFrames = 100,
                    currentTime = 10000,
                    totalTime = 10000,
                    outputSize = 5000000,
                    fps = 30.0,
                    elapsedTime = 5000
                ))
                
                true
            } catch (e: Exception) {
                false
            }
        }
    }

    /**
     * Get available output space
     */
    fun getAvailableSpace(): Long {
        return try {
            val stat = android.os.StatFs(outputDir.absolutePath)
            stat.availableBlocksLong * stat.blockSizeLong
        } catch (e: Exception) {
            0L
        }
    }

    /**
     * Get output directory size
     */
    suspend fun getOutputDirectorySize(): Long {
        return withContext(Dispatchers.IO) {
            try {
                outputDir.walk().map { it.length() }.sum()
            } catch (e: Exception) {
                0L
            }
        }
    }
}
