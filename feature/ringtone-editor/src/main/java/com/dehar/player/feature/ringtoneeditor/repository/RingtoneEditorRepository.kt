package com.dehar.player.feature.ringtoneeditor.repository

import android.content.Context
import android.media.MediaExtractor
import android.media.MediaFormat
import android.media.RingtoneManager
import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton
import com.dehar.player.feature.ringtoneeditor.model.RingtoneAudio
import com.dehar.player.feature.ringtoneeditor.model.RingtoneType
import com.dehar.player.feature.ringtoneeditor.model.WaveformData

import android.media.MediaCodec
import android.media.MediaMetadataRetriever
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * Repository for ringtone editor operations
 */
@Singleton
class RingtoneEditorRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {

    /**
     * Extract waveform data from audio file
     * Generates sample points for visual waveform display
     */
    suspend fun extractWaveform(audioPath: String, targetBars: Int = 200): WaveformData? {
        return withContext(Dispatchers.IO) {
            try {
                val file = File(audioPath)
                if (!file.exists()) return@withContext null
                val uri = Uri.fromFile(file)

                val retriever = MediaMetadataRetriever()
                retriever.setDataSource(context, uri)
                val durationMs = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLongOrNull() ?: 0L
                retriever.release()

                // Sample frames at equal intervals
                val amplitudes = FloatArray(targetBars)
                val extractor = MediaExtractor()
                extractor.setDataSource(context, uri, null)

                val audioTrack = (0 until extractor.trackCount).firstOrNull { i ->
                    extractor.getTrackFormat(i).getString(MediaFormat.KEY_MIME)?.startsWith("audio/") == true
                } ?: run {
                    extractor.release()
                    return@withContext null
                }

                extractor.selectTrack(audioTrack)
                val format = extractor.getTrackFormat(audioTrack)
                val mimeType = format.getString(MediaFormat.KEY_MIME) ?: return@withContext null
                val sampleRate = format.getInteger(MediaFormat.KEY_SAMPLE_RATE)
                val channelCount = format.getInteger(MediaFormat.KEY_CHANNEL_COUNT)

                val codec = MediaCodec.createDecoderByType(mimeType)
                codec.configure(format, null, null, 0)
                codec.start()

                val info = MediaCodec.BufferInfo()
                var inputDone = false
                var sampleIndex = 0

                while (!inputDone && sampleIndex < targetBars * 10) { // Limit samples for performance
                    val inIdx = codec.dequeueInputBuffer(5000)
                    if (inIdx >= 0) {
                        val buf = codec.getInputBuffer(inIdx)!!
                        val size = extractor.readSampleData(buf, 0)
                        if (size < 0) {
                            codec.queueInputBuffer(inIdx, 0, 0, 0, MediaCodec.BUFFER_FLAG_END_OF_STREAM)
                            inputDone = true
                        } else {
                            codec.queueInputBuffer(inIdx, 0, size, extractor.sampleTime, 0)
                            extractor.advance()
                        }
                    }
                    val outIdx = codec.dequeueOutputBuffer(info, 5000)
                    if (outIdx >= 0) {
                        val buf = codec.getOutputBuffer(outIdx)!!
                        val shorts = ShortArray(info.size / 2)
                        buf.order(java.nio.ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(shorts)
                        
                        val rms = if (shorts.isEmpty()) 0f else
                            sqrt(shorts.map { it.toDouble().pow(2) }.average()).toFloat() / Short.MAX_VALUE
                        
                        val idx = ((info.presentationTimeUs.toFloat() / (durationMs * 1000)) * targetBars)
                            .toInt().coerceIn(0, targetBars - 1)
                        
                        amplitudes[idx] = max(amplitudes[idx], rms)
                        codec.releaseOutputBuffer(outIdx, false)
                        sampleIndex++
                        if (info.flags and MediaCodec.BUFFER_FLAG_END_OF_STREAM != 0) inputDone = true
                    }
                }
                codec.stop()
                codec.release()
                extractor.release()

                WaveformData(
                    samples = amplitudes,
                    durationMs = durationMs,
                    sampleRate = sampleRate,
                    channelCount = channelCount
                )
            } catch (e: Exception) {
                null
            }
        }
    }

    /**
     * Trim audio file and save to temporary location
     */
    suspend fun trimAudio(
        inputPath: String,
        outputPath: String,
        startMs: Long,
        endMs: Long
    ): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                // TODO: Implement FFmpeg-based audio trimming using FFmpeg-Kit
                // For now, just copy the file as placeholder
                val inputFile = File(inputPath)
                val outputFile = File(outputPath)
                inputFile.copyTo(outputFile, overwrite = true)
                true
            } catch (e: Exception) {
                false
            }
        }
    }

    /**
     * Set audio file as system ringtone
     */
    suspend fun setAsRingtone(
        audioPath: String,
        ringtoneType: RingtoneType,
        displayName: String
    ): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val file = File(audioPath)
                if (!file.exists()) return@withContext false

                val ringtoneUri = Uri.fromFile(file)
                
                // Get RingtoneManager and set ringtone based on type
                when (ringtoneType) {
                    RingtoneType.RINGTONE -> RingtoneManager.setActualDefaultRingtoneUri(
                        context,
                        RingtoneManager.TYPE_RINGTONE,
                        ringtoneUri
                    )
                    RingtoneType.NOTIFICATION -> RingtoneManager.setActualDefaultRingtoneUri(
                        context,
                        RingtoneManager.TYPE_NOTIFICATION,
                        ringtoneUri
                    )
                    RingtoneType.ALARM -> RingtoneManager.setActualDefaultRingtoneUri(
                        context,
                        RingtoneManager.TYPE_ALARM,
                        ringtoneUri
                    )
                    RingtoneType.CUSTOM -> return@withContext true // Custom ringtone, no system integration
                }
                
                true
            } catch (e: Exception) {
                false
            }
        }
    }

    /**
     * Save trimmed audio to custom ringtones folder
     */
    suspend fun saveCustomRingtone(
        audioPath: String,
        ringtoneType: RingtoneType,
        displayName: String
    ): String? {
        return withContext(Dispatchers.IO) {
            try {
                val ringtoneDir = getRingtoneDir(ringtoneType)
                val outputFile = File(ringtoneDir, "$displayName.m4a")
                
                val inputFile = File(audioPath)
                inputFile.copyTo(outputFile, overwrite = true)
                
                outputFile.absolutePath
            } catch (e: Exception) {
                null
            }
        }
    }

    /**
     * Get list of custom ringtones
     */
    suspend fun getCustomRingtones(ringtoneType: RingtoneType): List<RingtoneAudio> {
        return withContext(Dispatchers.IO) {
            try {
                val ringtoneDir = getRingtoneDir(ringtoneType)
                if (!ringtoneDir.exists()) return@withContext emptyList()

                ringtoneDir.listFiles()?.filter { 
                    it.isFile && it.extension in listOf("m4a", "mp3", "ogg", "wav")
                }?.mapIndexed { index, file ->
                    val durationMs = getAudioDuration(file.absolutePath)
                    RingtoneAudio(
                        id = index.toLong(),
                        name = file.nameWithoutExtension,
                        path = file.absolutePath,
                        durationMs = durationMs
                    )
                } ?: emptyList()
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    /**
     * Delete custom ringtone
     */
    suspend fun deleteCustomRingtone(rintonePath: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                File(rintonePath).delete()
            } catch (e: Exception) {
                false
            }
        }
    }

    /**
     * Get audio file duration in milliseconds
     */
    suspend fun getAudioDuration(audioPath: String): Long {
        return withContext(Dispatchers.IO) {
            try {
                val extractor = MediaExtractor()
                extractor.setDataSource(audioPath)
                
                var durationMs = 0L
                for (i in 0 until extractor.trackCount) {
                    val format = extractor.getTrackFormat(i)
                    val mimeType = format.getString(MediaFormat.KEY_MIME)
                    if (mimeType?.startsWith("audio/") == true) {
                        durationMs = format.getLong(MediaFormat.KEY_DURATION) / 1000
                        break
                    }
                }
                
                extractor.release()
                durationMs
            } catch (e: Exception) {
                0L
            }
        }
    }

    private fun getRingtoneDir(ringtoneType: RingtoneType): File {
        val baseDir = File(context.getExternalFilesDir(null), "ringtones")
        val typeDir = when (ringtoneType) {
            RingtoneType.RINGTONE -> File(baseDir, "ringtones")
            RingtoneType.NOTIFICATION -> File(baseDir, "notifications")
            RingtoneType.ALARM -> File(baseDir, "alarms")
            RingtoneType.CUSTOM -> File(baseDir, "custom")
        }
        
        if (!typeDir.exists()) {
            typeDir.mkdirs()
        }
        
        return typeDir
    }
}
