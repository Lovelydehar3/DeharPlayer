package com.dehar.player.player

import android.content.Intent
import androidx.annotation.OptIn
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.booleanPreferencesKey
import com.dehar.player.widget.MusicWidget
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@OptIn(UnstableApi::class)
class MusicService : MediaSessionService() {

    private var mediaSession: MediaSession? = null
    private var player: ExoPlayer? = null
    private val serviceScope = CoroutineScope(Dispatchers.Main + Job())

    override fun onCreate() {
        super.onCreate()
        
        // 1. Initialize ExoPlayer with AudioAttributes for proper system focus handling
        val audioAttributes = AudioAttributes.Builder()
            .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
            .setUsage(C.USAGE_MEDIA)
            .build()

        player = ExoPlayer.Builder(this)
            .setAudioAttributes(audioAttributes, true)
            .setHandleAudioBecomingNoisy(true)
            .build()

        // 2. Create the MediaSession
        player?.let { exoPlayer ->
            mediaSession = MediaSession.Builder(this, exoPlayer)
                .setCallback(CustomMediaSessionCallback())
                .build()
            
            exoPlayer.addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(playbackState: Int) {
                    updateWidget()
                }
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    updateWidget()
                }
                override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                    updateWidget()
                }
            })
        }
    }

    private fun updateWidget() {
        val p = player ?: return
        serviceScope.launch {
            try {
                val glanceId = GlanceAppWidgetManager(this@MusicService)
                    .getGlanceIds(MusicWidget::class.java).firstOrNull() ?: return@launch
                
                updateAppWidgetState(this@MusicService, glanceId) { prefs ->
                    val currentMedia = p.currentMediaItem
                    prefs[stringPreferencesKey("title")] = currentMedia?.mediaMetadata?.title?.toString() ?: "Not playing"
                    prefs[stringPreferencesKey("artist")] = currentMedia?.mediaMetadata?.artist?.toString() ?: ""
                    prefs[booleanPreferencesKey("is_playing")] = p.isPlaying
                }
                MusicWidget().update(this@MusicService, glanceId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Required getter to return active media session
    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        return mediaSession
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent == null) return START_STICKY
        
        when (intent.action) {
            "ACTION_TOGGLE_PLAY" -> {
                player?.let { if (it.isPlaying) it.pause() else it.play() }
            }
            "ACTION_NEXT" -> player?.seekToNext()
            "ACTION_PREVIOUS" -> player?.seekToPrevious()
        }
        
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        // If app is swiped away from recent tasks, pause or release depending on state
        val playerInstance = mediaSession?.player
        if (playerInstance != null && !playerInstance.playWhenReady) {
            stopSelf()
        }
    }

    override fun onDestroy() {
        mediaSession?.run {
            player.release()
            release()
            mediaSession = null
        }
        player = null
        super.onDestroy()
    }

    private inner class CustomMediaSessionCallback : MediaSession.Callback {
        // Allows custom system hooks, lockscreen commands, and custom playback action binds
    }
}
