package com.dehar.player.core.testing

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.dehar.player.core.data.database.DeharDatabase
import com.dehar.player.core.domain.model.SongItem
import com.dehar.player.core.domain.repository.SongRepository
import com.dehar.player.core.domain.repository.VideoRepository
import com.dehar.player.core.domain.usecase.SortOption
import com.dehar.player.core.domain.usecase.VideoItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.runner.RunWith

/**
 * Base test class for database integration tests
 */
@RunWith(AndroidJUnit4::class)
abstract class DatabaseTestBase {
    protected lateinit var database: DeharDatabase

    @Before
    fun initDb() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            DeharDatabase::class.java
        ).build()
    }
}

/**
 * Test utilities
 */
object TestDataFactory {
    fun createVideoEntity(
        id: Long = 1,
        title: String = "Test Video",
        path: String = "/path/to/video.mp4",
        duration: Long = 60000
    ) = com.dehar.player.core.data.model.VideoEntity(
        id = id,
        title = title,
        displayName = title,
        path = path,
        size = 1024000,
        duration = duration,
        width = 1920,
        height = 1080,
        mimeType = "video/mp4",
        dateAdded = 0,
        dateModified = 0,
        bucketId = 1,
        bucketName = "Test",
        resolution = "1920x1080",
        frameRate = 30,
        bitrate = 5_000_000,
        codecName = "H.264",
        lastPlayedPosition = 0,
        playCount = 0,
        isFavorite = false
    )

    fun createSongEntity(
        id: Long = 1,
        title: String = "Test Song",
        artist: String = "Test Artist",
        path: String = "/path/to/song.mp3",
        duration: Long = 180000
    ) = com.dehar.player.core.data.model.SongEntity(
        id = id,
        title = title,
        artist = artist,
        album = "Test Album",
        albumArtist = artist,
        genre = "Unknown",
        path = path,
        size = 1024,
        duration = duration,
        trackNumber = 1,
        discNumber = 1,
        year = 2024,
        dateAdded = 0,
        dateModified = 0,
        albumId = 1,
        artistId = 1,
        bitrate = 320000,
        sampleRate = 44100,
        mimeType = "audio/mpeg",
        playCount = 0,
        isFavorite = false
    )
}

class FakeVideoRepository : VideoRepository {
    private val videos = mutableListOf<VideoItem>()

    override fun getVideos(
        sortBy: SortOption,
        filterResolution: String?
    ): Flow<List<VideoItem>> = flowOf(videos)

    override fun getVideoById(id: Long): Flow<VideoItem?> = flowOf(videos.find { it.id == id })

    override fun getVideosByFolder(folderId: Long, sort: SortOption): Flow<List<VideoItem>> =
        flowOf(videos)

    override fun getFavoriteVideos(): Flow<List<VideoItem>> =
        flowOf(videos.filter { it.isFavorite })

    override fun getRecentlyPlayedVideos(limit: Int): Flow<List<VideoItem>> = flowOf(videos)

    override fun getUnwatchedVideos(): Flow<List<VideoItem>> = flowOf(videos)

    override suspend fun updatePlaybackPosition(videoId: Long, position: Long, duration: Long) = Unit

    override suspend fun toggleFavorite(videoId: Long, isFavorite: Boolean) = Unit

    override suspend fun deleteVideo(videoId: Long) {
        videos.removeIf { it.id == videoId }
    }

    override suspend fun rescanLibrary() = Unit

    override fun getVideoCount(): Flow<Int> = flowOf(videos.size)

    override fun getTotalVideoSize(): Flow<Long> = flowOf(videos.sumOf { it.size })
}

class FakeSongRepository : SongRepository {
    private val songs = mutableListOf<SongItem>()

    override fun getSongs(sort: SortOption): Flow<List<SongItem>> = flowOf(songs)

    override fun getSongsByAlbum(albumId: Long): Flow<List<SongItem>> = flowOf(songs)

    override fun getSongsByArtist(artistId: Long): Flow<List<SongItem>> = flowOf(songs)

    override fun getSongsByGenre(genre: String): Flow<List<SongItem>> = flowOf(songs)

    override fun getFavoriteSongs(): Flow<List<SongItem>> =
        flowOf(songs.filter { it.isFavorite })

    override fun getRecentlyPlayedSongs(limit: Int): Flow<List<SongItem>> = flowOf(songs)

    override fun searchSongs(query: String): Flow<List<SongItem>> =
        flowOf(songs.filter { it.title.contains(query, ignoreCase = true) })

    override suspend fun updateSongPlaybackPosition(songId: Long, position: Long) = Unit

    override suspend fun toggleFavoriteSong(songId: Long, isFavorite: Boolean) = Unit

    override suspend fun deleteSong(songId: Long) {
        songs.removeIf { it.id == songId }
    }

    override fun getSongCount(): Flow<Int> = flowOf(songs.size)
}
