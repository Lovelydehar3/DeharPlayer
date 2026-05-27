package com.dehar.player.player

import android.content.ComponentName
import android.content.Context
import android.net.Uri
import androidx.annotation.OptIn
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.PlaybackParameters
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.dehar.player.data.SongData
import com.dehar.player.data.PlaylistManager
import com.google.common.util.concurrent.MoreExecutors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Collections

import com.dehar.player.feature.lyrics.LrcParser
import com.dehar.player.feature.lyrics.LrcLine
import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import org.json.JSONObject

@OptIn(UnstableApi::class)
class MusicPlaybackManager(private val context: Context) {

    private var controller: MediaController? = null
    private val scope = CoroutineScope(Dispatchers.Main + Job())
    private var progressTrackingJob: Job? = null
    private var sleepTimerJob: Job? = null
    private var lyricsJob: Job? = null

    // Lyrics state
    var lyricsLines by mutableStateOf<List<LrcLine>>(emptyList())
    var currentLyricsLine by mutableIntStateOf(-1)
    var lyricsLoading by mutableStateOf(false)
    var lyricsNotFound by mutableStateOf(false)
    var playbackPosition by mutableLongStateOf(0L)
    var playbackDuration by mutableLongStateOf(0L)
    var playbackBuffered by mutableLongStateOf(0L)
    
    var repeatMode by mutableIntStateOf(Player.REPEAT_MODE_OFF) // OFF, ONE, ALL
    var isShuffleEnabled by mutableStateOf(false)
    
    var currentQueue = mutableStateOf<List<SongData>>(emptyList())
    var currentSongIndex by mutableIntStateOf(-1)
    
    var sleepTimerRemainingSec by mutableIntStateOf(0)
    var playbackSpeed by mutableFloatStateOf(1.0f)

    // Equalizer sliders
    var eqBassBoost by mutableFloatStateOf(0f)
    var eqTreble by mutableFloatStateOf(0f)
    val eqBands = FloatArray(5) { 50f }
    var eqPreset by mutableStateOf("Normal")

    private val playlistManager = PlaylistManager(context)

    init {
        initializeController()
    }

    private fun initializeController() {
        val sessionToken = SessionToken(context, ComponentName(context, MusicService::class.java))
        val controllerFuture = MediaController.Builder(context, sessionToken).buildAsync()
        
        controllerFuture.addListener({
            try {
                val mediaController = controllerFuture.get()
                controller = mediaController
                setupControllerListener(mediaController)
                startProgressTracking()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, MoreExecutors.directExecutor())
    }

    private fun setupControllerListener(controller: MediaController) {
        // Sync initial states
        isPlaying = controller.isPlaying
        repeatMode = controller.repeatMode
        isShuffleEnabled = controller.shuffleModeEnabled
        playbackSpeed = controller.playbackParameters.speed
        
        controller.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(playing: Boolean) {
                isPlaying = playing
            }

            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                val activeIndex = controller.currentMediaItemIndex
                if (activeIndex in currentQueue.value.indices) {
                    currentSongIndex = activeIndex
                    currentSong = currentQueue.value[activeIndex]
                }
            }

            override fun onPlaybackStateChanged(state: Int) {
                if (state == Player.STATE_ENDED) {
                    // Check custom loop mode stubs
                }
            }

            override fun onRepeatModeChanged(mode: Int) {
                repeatMode = mode
            }

            override fun onShuffleModeEnabledChanged(enabled: Boolean) {
                isShuffleEnabled = enabled
            }
        })
    }

    // --- PLAYBACK OPERATIONS ---

    fun setQueue(songs: List<SongData>, startIndex: Int) {
        if (songs.isEmpty()) return
        
        currentQueue.value = songs
        currentSongIndex = if (startIndex in songs.indices) startIndex else 0
        currentSong = songs.getOrNull(currentSongIndex)

        controller?.let { player ->
            player.stop()
            player.clearMediaItems()
            
            val mediaItems = songs.map { song ->
                MediaItem.Builder()
                    .setUri(Uri.parse(song.uri))
                    .setMediaId(song.id.toString())
                    .setMediaMetadata(
                        MediaMetadata.Builder()
                            .setTitle(song.title)
                            .setArtist(song.artist)
                            .setAlbumTitle(song.album)
                            .build()
                    )
                    .build()
            }
            
            player.addMediaItems(mediaItems)
            player.seekTo(currentSongIndex, 0L)
            player.prepare()
            player.play()
        }
    }

    fun play() {
        controller?.play()
    }

    fun pause() {
        controller?.pause()
    }

    fun togglePlayPause() {
        if (isPlaying) pause() else play()
    }

    fun next() {
        controller?.let { player ->
            if (player.hasNextMediaItem()) {
                player.seekToNextMediaItem()
            } else if (repeatMode == Player.REPEAT_MODE_ALL) {
                player.seekTo(0, 0L)
            }
        }
    }

    fun previous() {
        controller?.let { player ->
            if (player.hasPreviousMediaItem()) {
                player.seekToPreviousMediaItem()
            } else if (repeatMode == Player.REPEAT_MODE_ALL) {
                player.seekTo(currentQueue.value.size - 1, 0L)
            }
        }
    }

    fun seekTo(positionMs: Long) {
        controller?.seekTo(positionMs)
    }

    fun setSpeed(speed: Float) {
        playbackSpeed = speed.coerceIn(0.25f, 3.0f)
        controller?.playbackParameters = PlaybackParameters(playbackSpeed)
    }

    // --- QUEUE MANAGEMENT ---

    fun playNext(song: SongData) {
        val updatedQueue = currentQueue.value.toMutableList()
        // Remove existing item to prevent duplicate
        updatedQueue.removeAll { it.id == song.id }
        
        val insertIndex = currentSongIndex + 1
        if (insertIndex in 0..updatedQueue.size) {
            updatedQueue.add(insertIndex, song)
        } else {
            updatedQueue.add(song)
        }
        
        rebuildControllerQueue(updatedQueue)
    }

    fun addToQueue(song: SongData) {
        val updatedQueue = currentQueue.value.toMutableList()
        if (updatedQueue.any { it.id == song.id }) return // Already in queue
        updatedQueue.add(song)
        
        rebuildControllerQueue(updatedQueue)
    }

    fun removeTrack(index: Int) {
        if (index !in currentQueue.value.indices) return
        val updatedQueue = currentQueue.value.toMutableList()
        updatedQueue.removeAt(index)
        
        rebuildControllerQueue(updatedQueue)
    }

    fun dragReorder(fromIndex: Int, toIndex: Int) {
        if (fromIndex !in currentQueue.value.indices || toIndex !in currentQueue.value.indices) return
        val updatedQueue = currentQueue.value.toMutableList()
        Collections.swap(updatedQueue, fromIndex, toIndex)
        
        rebuildControllerQueue(updatedQueue, keepActiveTrack = true, fromIdx = fromIndex, toIdx = toIndex)
    }

    private fun rebuildControllerQueue(
        newQueue: List<SongData>, 
        keepActiveTrack: Boolean = false,
        fromIdx: Int = -1,
        toIdx: Int = -1
    ) {
        val activeSongId = currentSong?.id
        currentQueue.value = newQueue
        
        controller?.let { player ->
            // Rebuild MediaSession tracks
            val currentPos = player.currentPosition
            player.stop()
            player.clearMediaItems()
            
            val mediaItems = newQueue.map { song ->
                MediaItem.Builder()
                    .setUri(Uri.parse(song.uri))
                    .setMediaId(song.id.toString())
                    .setMediaMetadata(
                        MediaMetadata.Builder()
                            .setTitle(song.title)
                            .setArtist(song.artist)
                            .setAlbumTitle(song.album)
                            .build()
                    )
                    .build()
            }
            
            player.addMediaItems(mediaItems)
            
            val newActiveIndex = if (activeSongId != null) {
                newQueue.indexOfFirst { it.id == activeSongId }
            } else -1

            if (newActiveIndex in newQueue.indices) {
                currentSongIndex = newActiveIndex
                currentSong = newQueue[newActiveIndex]
                player.seekTo(newActiveIndex, currentPos)
            } else {
                currentSongIndex = 0
                currentSong = newQueue.firstOrNull()
                player.seekTo(0, 0L)
            }
            
            player.prepare()
            if (isPlaying) player.play()
        }
    }

    // --- REPEAT & SHUFFLE SYSTEM ---

    fun toggleRepeatMode() {
        val nextMode = when (repeatMode) {
            Player.REPEAT_MODE_OFF -> Player.REPEAT_MODE_ONE
            Player.REPEAT_MODE_ONE -> Player.REPEAT_MODE_ALL
            else -> Player.REPEAT_MODE_OFF
        }
        repeatMode = nextMode
        controller?.repeatMode = nextMode
    }

    fun toggleShuffle() {
        val nextShuffle = !isShuffleEnabled
        isShuffleEnabled = nextShuffle
        controller?.shuffleModeEnabled = nextShuffle
        
        // Smart Shuffle stubs
        if (nextShuffle && currentQueue.value.isNotEmpty()) {
            // Re-randomize session playlist queue order inside MediaController natively
        }
    }

    // --- SLEEP TIMER ---

    fun startSleepTimer(minutes: Int) {
        sleepTimerJob?.cancel()
        sleepTimerRemainingSec = minutes * 60
        
        sleepTimerJob = scope.launch {
            while (sleepTimerRemainingSec > 0) {
                delay(1000)
                sleepTimerRemainingSec--
                if (sleepTimerRemainingSec == 0) {
                    pause()
                }
            }
        }
    }

    fun cancelSleepTimer() {
        sleepTimerJob?.cancel()
        sleepTimerRemainingSec = 0
    }

    // --- LYRICS SYSTEM ---

    fun loadLyrics(song: SongData) {
        lyricsJob?.cancel()
        lyricsJob = scope.launch {
            lyricsLoading = true
            lyricsNotFound = false
            lyricsLines = emptyList()
            
            // Step 1: Local .lrc file check
            val lrcFile = File(song.path).let { 
                File(it.parent, "${it.nameWithoutExtension}.lrc") 
            }
            if (lrcFile.exists()) {
                lyricsLines = LrcParser.parse(lrcFile.readText())
                lyricsLoading = false
                return@launch
            }
            
            // Step 2: Embedded lyrics
            song.embeddedLyrics?.let { embedded ->
                lyricsLines = if (embedded.contains("["))
                    LrcParser.parse(embedded)
                else embedded.lines().mapIndexed { i, l -> LrcLine(i * 3000L, l) }
                lyricsLoading = false
                return@launch
            }
            
            // Step 3: lrclib.net API
            try {
                val response = withContext(Dispatchers.IO) {
                    val url = "https://lrclib.net/api/get?artist_name=${java.net.URLEncoder.encode(song.artist, "UTF-8")}&track_name=${java.net.URLEncoder.encode(song.title, "UTF-8")}"
                    val conn = URL(url).openConnection() as HttpURLConnection
                    conn.setRequestProperty("User-Agent", "DeharPlayer/1.0")
                    conn.inputStream.bufferedReader().readText().also { conn.disconnect() }
                }
                val json = JSONObject(response)
                val synced = json.optString("syncedLyrics")
                val plain = json.optString("plainLyrics")
                when {
                    synced.isNotEmpty() -> lyricsLines = LrcParser.parse(synced)
                    plain.isNotEmpty() -> lyricsLines = plain.lines().mapIndexed { i, l -> LrcLine(i * 3000L, l) }
                    else -> lyricsNotFound = true
                }
            } catch (e: Exception) {
                lyricsNotFound = true
            }
            lyricsLoading = false
        }
    }

    private fun updateLyricsPosition(positionMs: Long) {
        if (lyricsLines.isNotEmpty()) {
            currentLyricsLine = lyricsLines.indexOfLast { it.timestampMs <= positionMs }
        }
    }

    // --- PROGRESS LOOP ---

    private fun startProgressTracking() {
        progressTrackingJob?.cancel()
        progressTrackingJob = scope.launch {
            while (true) {
                controller?.let { player ->
                    if (player.playbackState != Player.STATE_IDLE) {
                        playbackPosition = player.currentPosition
                        val dur = player.duration
                        playbackDuration = if (dur == C.TIME_UNSET) 0L else dur
                        playbackBuffered = player.bufferedPosition
                        updateLyricsPosition(playbackPosition)
                    }
                }
                delay(400)
            }
        }
    }
}
