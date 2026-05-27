package com.dehar.player.core.domain.repository

import com.dehar.player.core.domain.model.PlaylistItem
import com.dehar.player.core.domain.model.SongItem
import com.dehar.player.core.domain.usecase.SortOption
import com.dehar.player.core.domain.usecase.VideoItem
import kotlinx.coroutines.flow.Flow

interface VideoRepository {
    fun getVideos(
        sortBy: SortOption = SortOption.DATE_DESC,
        filterResolution: String? = null
    ): Flow<List<VideoItem>>

    fun getVideoById(id: Long): Flow<VideoItem?>

    fun getVideosByFolder(folderId: Long, sort: SortOption = SortOption.DATE_DESC): Flow<List<VideoItem>>

    fun getFavoriteVideos(): Flow<List<VideoItem>>

    fun getRecentlyPlayedVideos(limit: Int = 20): Flow<List<VideoItem>>

    fun getUnwatchedVideos(): Flow<List<VideoItem>>

    suspend fun updatePlaybackPosition(videoId: Long, position: Long, duration: Long)

    suspend fun toggleFavorite(videoId: Long, isFavorite: Boolean)

    suspend fun deleteVideo(videoId: Long)

    suspend fun rescanLibrary()

    fun getVideoCount(): Flow<Int>

    fun getTotalVideoSize(): Flow<Long>
}

interface SongRepository {
    fun getSongs(sort: SortOption = SortOption.TITLE_ASC): Flow<List<SongItem>>

    fun getSongsByAlbum(albumId: Long): Flow<List<SongItem>>

    fun getSongsByArtist(artistId: Long): Flow<List<SongItem>>

    fun getSongsByGenre(genre: String): Flow<List<SongItem>>

    fun getFavoriteSongs(): Flow<List<SongItem>>

    fun getRecentlyPlayedSongs(limit: Int = 20): Flow<List<SongItem>>

    fun searchSongs(query: String): Flow<List<SongItem>>

    suspend fun updateSongPlaybackPosition(songId: Long, position: Long)

    suspend fun toggleFavoriteSong(songId: Long, isFavorite: Boolean)

    suspend fun deleteSong(songId: Long)

    fun getSongCount(): Flow<Int>
}

interface PlaylistRepository {
    fun getPlaylists(): Flow<List<PlaylistItem>>

    fun getPlaylistSongs(playlistId: Long): Flow<List<SongItem>>

    suspend fun createPlaylist(name: String): Long

    suspend fun addSongsToPlaylist(playlistId: Long, songIds: List<Long>)

    suspend fun removeFromPlaylist(playlistId: Long, songId: Long)

    suspend fun deletePlaylist(playlistId: Long)

    suspend fun renamePlaylist(playlistId: Long, newName: String)
}
