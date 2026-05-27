package com.dehar.player.player

import android.content.Context
import android.net.Uri
import androidx.annotation.OptIn
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.TrackSelectionOverride
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import com.dehar.player.data.VideoData

import androidx.media3.exoplayer.DefaultRenderersFactory
import com.dehar.player.data.PreferencesManager

@OptIn(UnstableApi::class)
class PlayerManager(private val context: Context, private val preferencesManager: PreferencesManager) {
    var exoPlayer: ExoPlayer? = null
        private set

    private var playlist: List<VideoData> = emptyList()
    private var currentIndex: Int = -1

    fun initialize() {
        if (exoPlayer == null) {
            val audioAttributes = AudioAttributes.Builder()
                .setUsage(C.USAGE_MEDIA)
                .setContentType(C.AUDIO_CONTENT_TYPE_MOVIE)
                .build()

            val renderersFactory = DefaultRenderersFactory(context).apply {
                setExtensionRendererMode(
                    when (preferencesManager.decoderMode) {
                        "FFMPEG" -> DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER
                        "HARDWARE" -> DefaultRenderersFactory.EXTENSION_RENDERER_MODE_OFF
                        else -> DefaultRenderersFactory.EXTENSION_RENDERER_MODE_ON
                    }
                )
                setEnableDecoderFallback(true)
            }

            exoPlayer = ExoPlayer.Builder(context, renderersFactory)
                .build()
                .apply {
                    setAudioAttributes(audioAttributes, true)
                    setHandleAudioBecomingNoisy(true)
                    playWhenReady = true
                    repeatMode = Player.REPEAT_MODE_OFF
                }
        }
    }

    fun setPlaylist(videos: List<VideoData>, startIndex: Int = 0) {
        playlist = videos
        currentIndex = startIndex
        
        exoPlayer?.let { player ->
            player.stop()
            player.clearMediaItems()
            val mediaItems = videos.map { video ->
                val builder = MediaItem.Builder()
                    .setUri(video.uri)
                    .setMediaId(video.path)
                
                // Scan for subtitles in the folder
                val subs = SubtitleHelper.findSubtitles(video.path)
                if (subs.isNotEmpty()) {
                    builder.setSubtitleConfigurations(subs.map { SubtitleHelper.createSubtitleConfig(it) })
                }
                builder.build()
            }
            player.setMediaItems(mediaItems, startIndex, 0L)
            player.prepare()
        }
    }

    fun setSingleMedia(uri: Uri, mediaId: String = uri.toString()) {
        playlist = emptyList()
        currentIndex = -1

        val mimeType = if (uri.scheme == "content") {
            try {
                context.contentResolver.getType(uri)
            } catch (e: Exception) {
                null
            }
        } else {
            null
        }

        exoPlayer?.let { player ->
            player.stop()
            player.clearMediaItems()
            val mediaItem = MediaItem.Builder()
                .setUri(uri)
                .setMediaId(mediaId)
                .apply {
                    if (mimeType != null) {
                        setMimeType(mimeType)
                    }
                }
                .build()
            player.setMediaItem(mediaItem)
            player.prepare()
            player.play()
        }
    }

    fun playAt(index: Int, position: Long = 0L) {
        if (index in playlist.indices) {
            currentIndex = index
            exoPlayer?.seekTo(index, position)
        }
    }

    fun updateIndex(index: Int) {
        if (index in playlist.indices) {
            currentIndex = index
        }
    }

    fun getCurrentVideo(): VideoData? = playlist.getOrNull(currentIndex)

    fun setPlaybackSpeed(speed: Float) {
        exoPlayer?.setPlaybackSpeed(speed)
    }

    fun getAudioTracks(): List<MediaTrackOption> {
        return getTracksOfType(C.TRACK_TYPE_AUDIO, "Audio track")
    }

    fun getSubtitleTracks(): List<MediaTrackOption> {
        return getTracksOfType(C.TRACK_TYPE_TEXT, "Subtitle")
    }

    fun selectAudioTrack(option: MediaTrackOption?) {
        selectTrack(C.TRACK_TYPE_AUDIO, option)
    }

    fun selectSubtitleTrack(option: MediaTrackOption?) {
        selectTrack(C.TRACK_TYPE_TEXT, option)
    }

    private fun getTracksOfType(type: Int, fallbackLabel: String): List<MediaTrackOption> {
        val player = exoPlayer ?: return emptyList()
        val tracks = mutableListOf<MediaTrackOption>()
        player.currentTracks.groups.forEachIndexed { groupIndex, group ->
            if (group.type == type) {
                for (trackIndex in 0 until group.length) {
                    val format = group.getTrackFormat(trackIndex)
                    val label = listOfNotNull(
                        format.label?.takeIf { it.isNotBlank() },
                        format.language?.takeIf { it.isNotBlank() && it != "und" }
                    ).joinToString(" - ").ifBlank {
                        "$fallbackLabel #${tracks.size + 1}"
                    }
                    tracks.add(
                        MediaTrackOption(
                            groupIndex = groupIndex,
                            trackIndex = trackIndex,
                            label = label,
                            supported = group.isTrackSupported(trackIndex),
                            selected = group.isTrackSelected(trackIndex)
                        )
                    )
                }
            }
        }
        return tracks
    }

    private fun selectTrack(type: Int, option: MediaTrackOption?) {
        val player = exoPlayer ?: return
        val builder = player.trackSelectionParameters
            .buildUpon()
            .clearOverridesOfType(type)

        if (option == null) {
            builder.setTrackTypeDisabled(type, true)
        } else {
            val group = player.currentTracks.groups.getOrNull(option.groupIndex) ?: return
            builder
                .setTrackTypeDisabled(type, false)
                .setOverrideForType(
                    TrackSelectionOverride(group.mediaTrackGroup, listOf(option.trackIndex))
                )
        }

        player.trackSelectionParameters = builder.build()
    }

    fun seekForward(millis: Long = 10000L) {
        exoPlayer?.let {
            val newPos = (it.currentPosition + millis).coerceAtMost(it.duration)
            it.seekTo(newPos)
        }
    }

    fun seekBackward(millis: Long = 10000L) {
        exoPlayer?.let {
            val newPos = (it.currentPosition - millis).coerceAtLeast(0L)
            it.seekTo(newPos)
        }
    }

    fun playNext(): Boolean {
        if (currentIndex < playlist.size - 1) {
            currentIndex++
            exoPlayer?.seekToNextMediaItem()
            return true
        }
        return false
    }

    fun playPrevious(): Boolean {
        if (currentIndex > 0) {
            currentIndex--
            exoPlayer?.seekToPreviousMediaItem()
            return true
        }
        return false
    }

    fun getCurrentPosition(): Long = exoPlayer?.currentPosition ?: 0L
    fun getDuration(): Long = exoPlayer?.duration ?: 0L
    fun isPlaying(): Boolean = exoPlayer?.isPlaying ?: false

    fun togglePlayPause() {
        exoPlayer?.let {
            if (it.isPlaying) it.pause() else it.play()
        }
    }

    fun pause() {
        exoPlayer?.pause()
    }

    fun play() {
        exoPlayer?.play()
    }

    fun release() {
        exoPlayer?.release()
        exoPlayer = null
        currentIndex = -1
        playlist = emptyList()
    }
}

data class MediaTrackOption(
    val groupIndex: Int,
    val trackIndex: Int,
    val label: String,
    val supported: Boolean,
    val selected: Boolean
)
