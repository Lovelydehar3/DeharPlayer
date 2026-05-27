package com.dehar.player.core.data.database

import androidx.room.*
import com.dehar.player.core.data.model.*
import kotlinx.coroutines.flow.Flow

/**
 * Video DAO - Data Access Object for videos
 */
@Dao
interface VideoDao {
    
    @Query("SELECT * FROM videos WHERE isHidden = 0 AND privateVaultId IS NULL ORDER BY dateAdded DESC")
    fun getAllVideos(): Flow<List<VideoEntity>>
    
    @Query("SELECT * FROM videos WHERE isHidden = 0 AND privateVaultId IS NULL ORDER BY dateAdded DESC")
    suspend fun getAllVideosOnce(): List<VideoEntity>
    
    @Query("SELECT * FROM videos WHERE bucketId = :folderId AND isHidden = 0 ORDER BY displayName ASC")
    fun getVideosInFolder(folderId: Long): Flow<List<VideoEntity>>
    
    @Query("SELECT * FROM videos WHERE id = :id")
    fun getVideoById(id: Long): Flow<VideoEntity?>
    
    @Query("SELECT * FROM videos WHERE id = :id")
    suspend fun getVideoByIdOnce(id: Long): VideoEntity?
    
    @Query("SELECT * FROM videos WHERE path = :path")
    suspend fun getVideoByPath(path: String): VideoEntity?
    
    @Query("SELECT DISTINCT bucketId, bucketName FROM videos WHERE isHidden = 0 AND privateVaultId IS NULL ORDER BY bucketName ASC")
    fun getVideoFolders(): Flow<List<FolderInfo>>
    
    @Query("SELECT * FROM videos WHERE isFavorite = 1 AND isHidden = 0 ORDER BY lastPlayedAt DESC")
    fun getFavoriteVideos(): Flow<List<VideoEntity>>
    
    @Query("SELECT * FROM videos WHERE lastPlayedAt > 0 AND isHidden = 0 ORDER BY lastPlayedAt DESC LIMIT :limit")
    fun getRecentlyPlayedVideos(limit: Int = 20): Flow<List<VideoEntity>>
    
    @Query("SELECT * FROM videos WHERE width >= 3840 AND isHidden = 0 ORDER BY dateAdded DESC")
    fun get4KVideos(): Flow<List<VideoEntity>>
    
    @Query("SELECT * FROM videos WHERE duration >= :minDuration AND isHidden = 0 ORDER BY dateAdded DESC")
    fun getLongVideos(minDuration: Long = 3600000L): Flow<List<VideoEntity>>
    
    @Query("SELECT * FROM videos WHERE playCount = 0 AND duration > 120000 AND isHidden = 0 ORDER BY dateAdded DESC")
    fun getUnwatchedVideos(): Flow<List<VideoEntity>>
    
    @Query("SELECT * FROM videos WHERE dateAdded >= :timestamp AND isHidden = 0 ORDER BY dateAdded DESC")
    fun getRecentlyAddedVideos(timestamp: Long): Flow<List<VideoEntity>>
    
    @Query("SELECT * FROM videos WHERE displayName LIKE '%' || :query || '%' OR title LIKE '%' || :query || '%' AND isHidden = 0")
    fun searchVideos(query: String): Flow<List<VideoEntity>>
    
    @Query("SELECT * FROM videos WHERE displayName LIKE '%' || :query || '%' OR title LIKE '%' || :query || '%' AND isHidden = 0")
    suspend fun searchVideosOnce(query: String): List<VideoEntity>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideo(video: VideoEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllVideos(videos: List<VideoEntity>)
    
    @Update
    suspend fun updateVideo(video: VideoEntity)
    
    @Delete
    suspend fun deleteVideo(video: VideoEntity)
    
    @Query("DELETE FROM videos WHERE id = :id")
    suspend fun deleteVideoById(id: Long)
    
    @Query("UPDATE videos SET lastPlayedAt = :timestamp, lastPlayedPosition = :position, playCount = playCount + 1 WHERE id = :id")
    suspend fun updatePlaybackState(id: Long, timestamp: Long, position: Long)
    
    @Query("UPDATE videos SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavoriteStatus(id: Long, isFavorite: Boolean)
    
    @Query("UPDATE videos SET isHidden = :isHidden WHERE id = :id")
    suspend fun updateHiddenStatus(id: Long, isHidden: Boolean)
    
    @Query("UPDATE videos SET userRating = :rating WHERE id = :id")
    suspend fun updateRating(id: Long, rating: Int)
    
    @Query("DELETE FROM videos")
    suspend fun deleteAllVideos()

    @Query("SELECT COUNT(*) FROM videos WHERE isHidden = 0 AND privateVaultId IS NULL")
    fun getVideoCount(): Flow<Int>

    @Query("SELECT COALESCE(SUM(size), 0) FROM videos WHERE isHidden = 0 AND privateVaultId IS NULL")
    fun getTotalSize(): Flow<Long>
    
    @Transaction
    suspend fun upsertVideos(videos: List<VideoEntity>) {
        for (video in videos) {
            insertVideo(video)
        }
    }
}

/**
 * Folder info data class for folder listing
 */
data class FolderInfo(
    val bucketId: Long,
    val bucketName: String
)

/**
 * Song/Audio DAO
 */
@Dao
interface SongDao {
    
    @Query("SELECT * FROM songs ORDER BY title ASC")
    fun getAllSongs(): Flow<List<SongEntity>>
    
    @Query("SELECT * FROM songs ORDER BY title ASC")
    suspend fun getAllSongsOnce(): List<SongEntity>
    
    @Query("SELECT * FROM songs WHERE id = :id")
    fun getSongById(id: Long): Flow<SongEntity?>
    
    @Query("SELECT * FROM songs WHERE id = :id")
    suspend fun getSongByIdOnce(id: Long): SongEntity?
    
    @Query("SELECT * FROM songs WHERE albumId = :albumId ORDER BY trackNumber ASC")
    fun getSongsByAlbum(albumId: Long): Flow<List<SongEntity>>
    
    @Query("SELECT * FROM songs WHERE artistId = :artistId ORDER BY title ASC")
    fun getSongsByArtist(artistId: Long): Flow<List<SongEntity>>
    
    @Query("SELECT * FROM songs WHERE genre = :genre ORDER BY title ASC")
    fun getSongsByGenre(genre: String): Flow<List<SongEntity>>
    
    @Query("SELECT * FROM songs WHERE isFavorite = 1 ORDER BY title ASC")
    fun getFavoriteSongs(): Flow<List<SongEntity>>
    
    @Query("SELECT * FROM songs WHERE lastPlayedAt > 0 ORDER BY lastPlayedAt DESC LIMIT :limit")
    fun getRecentlyPlayedSongs(limit: Int = 20): Flow<List<SongEntity>>
    
    @Query("SELECT * FROM songs WHERE playCount >= :minPlays ORDER BY playCount DESC LIMIT :limit")
    fun getMostPlayedSongs(minPlays: Int = 3, limit: Int = 20): Flow<List<SongEntity>>
    
    @Query("SELECT * FROM songs WHERE dateAdded >= :timestamp ORDER BY dateAdded DESC")
    fun getRecentlyAddedSongs(timestamp: Long): Flow<List<SongEntity>>
    
    @Query("SELECT * FROM songs WHERE title LIKE '%' || :query || '%' OR artist LIKE '%' || :query || '%' OR album LIKE '%' || :query || '%'")
    fun searchSongs(query: String): Flow<List<SongEntity>>
    
    @Query("SELECT * FROM songs WHERE title LIKE '%' || :query || '%' OR artist LIKE '%' || :query || '%' OR album LIKE '%' || :query || '%'")
    suspend fun searchSongsOnce(query: String): List<SongEntity>
    
    @Query("SELECT DISTINCT album, albumArtist FROM songs WHERE album != '' AND album != '<unknown>' ORDER BY album ASC")
    fun getAllAlbums(): Flow<List<AlbumInfo>>
    
    @Query("SELECT DISTINCT artistId, artist FROM songs WHERE artist != '' AND artist != '<unknown>' ORDER BY artist ASC")
    fun getAllArtists(): Flow<List<ArtistInfo>>
    
    @Query("SELECT DISTINCT genre FROM songs WHERE genre != '' AND genre != '<unknown>' ORDER BY genre ASC")
    fun getAllGenres(): Flow<List<String>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSong(song: SongEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllSongs(songs: List<SongEntity>)
    
    @Update
    suspend fun updateSong(song: SongEntity)
    
    @Delete
    suspend fun deleteSong(song: SongEntity)
    
    @Query("UPDATE songs SET lastPlayedAt = :timestamp, lastPlayedPosition = :position, playCount = playCount + 1 WHERE id = :id")
    suspend fun updatePlaybackState(id: Long, timestamp: Long, position: Long)
    
    @Query("UPDATE songs SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavoriteStatus(id: Long, isFavorite: Boolean)
    
    @Query("DELETE FROM songs WHERE id = :id")
    suspend fun deleteSongById(id: Long)

    @Query("SELECT COUNT(*) FROM songs")
    fun getSongCount(): Flow<Int>

    @Query("DELETE FROM songs")
    suspend fun deleteAllSongs()
}

data class AlbumInfo(
    val album: String,
    val albumArtist: String
)

data class ArtistInfo(
    val artistId: Long,
    val artist: String
)

/**
 * Playlist DAO
 */
@Dao
interface PlaylistDao {
    
    @Query("SELECT * FROM playlists WHERE isSystemPlaylist = 0 ORDER BY name ASC")
    fun getAllPlaylists(): Flow<List<PlaylistEntity>>
    
    @Query("SELECT * FROM playlists WHERE isSystemPlaylist = 1 ORDER BY id ASC")
    fun getSystemPlaylists(): Flow<List<PlaylistEntity>>
    
    @Query("SELECT * FROM playlists WHERE id = :id")
    fun getPlaylistById(id: Long): Flow<PlaylistEntity?>
    
    @Query("SELECT * FROM playlists WHERE id = :id")
    suspend fun getPlaylistByIdOnce(id: Long): PlaylistEntity?
    
    @Query("SELECT * FROM playlist_songs WHERE playlistId = :playlistId ORDER BY position ASC")
    fun getSongsInPlaylist(playlistId: Long): Flow<List<PlaylistSongCrossRef>>
    
    @Query("SELECT * FROM playlist_videos WHERE playlistId = :playlistId ORDER BY position ASC")
    fun getVideosInPlaylist(playlistId: Long): Flow<List<PlaylistVideoCrossRef>>
    
    @Query("SELECT songs.* FROM songs INNER JOIN playlist_songs ON songs.id = playlist_songs.songId WHERE playlist_songs.playlistId = :playlistId ORDER BY playlist_songs.position ASC")
    fun getSongEntitiesInPlaylist(playlistId: Long): Flow<List<SongEntity>>
    
    @Query("SELECT videos.* FROM videos INNER JOIN playlist_videos ON videos.id = playlist_videos.videoId WHERE playlist_videos.playlistId = :playlistId ORDER BY playlist_videos.position ASC")
    fun getVideoEntitiesInPlaylist(playlistId: Long): Flow<List<VideoEntity>>
    
    @Query("SELECT COUNT(*) FROM playlist_songs WHERE playlistId = :playlistId")
    fun getSongCountInPlaylist(playlistId: Long): Flow<Int>
    
    @Query("SELECT COUNT(*) FROM playlist_videos WHERE playlistId = :playlistId")
    fun getVideoCountInPlaylist(playlistId: Long): Flow<Int>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: PlaylistEntity): Long
    
    @Update
    suspend fun updatePlaylist(playlist: PlaylistEntity)
    
    @Delete
    suspend fun deletePlaylist(playlist: PlaylistEntity)
    
    @Query("DELETE FROM playlists WHERE id = :id")
    suspend fun deletePlaylistById(id: Long)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSongToPlaylist(crossRef: PlaylistSongCrossRef)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addVideoToPlaylist(crossRef: PlaylistVideoCrossRef)
    
    @Query("DELETE FROM playlist_songs WHERE playlistId = :playlistId AND songId = :songId")
    suspend fun removeSongFromPlaylist(playlistId: Long, songId: Long)
    
    @Query("DELETE FROM playlist_videos WHERE playlistId = :playlistId AND videoId = :videoId")
    suspend fun removeVideoFromPlaylist(playlistId: Long, videoId: Long)
    
    @Query("DELETE FROM playlist_songs WHERE playlistId = :playlistId")
    suspend fun clearPlaylistSongs(playlistId: Long)
    
    @Query("DELETE FROM playlist_videos WHERE playlistId = :playlistId")
    suspend fun clearPlaylistVideos(playlistId: Long)
    
    @Query("UPDATE playlist_songs SET position = :position WHERE playlistId = :playlistId AND songId = :songId")
    suspend fun updateSongPosition(playlistId: Long, songId: Long, position: Int)
    
    @Query("SELECT DISTINCT p.* FROM playlists p INNER JOIN playlist_songs ps ON p.id = ps.playlistId WHERE ps.songId = :songId")
    fun getPlaylistsContainingSong(songId: Long): Flow<List<PlaylistEntity>>
    
    @Query("SELECT DISTINCT p.* FROM playlists p INNER JOIN playlist_videos pv ON p.id = pv.playlistId WHERE pv.videoId = :videoId")
    fun getPlaylistsContainingVideo(videoId: Long): Flow<List<PlaylistEntity>>
}

/**
 * Bookmark DAO
 */
@Dao
interface BookmarkDao {
    
    @Query("SELECT * FROM bookmarks WHERE mediaId = :mediaId AND mediaType = :mediaType ORDER BY positionMs ASC")
    fun getBookmarksForMedia(mediaId: Long, mediaType: MediaType): Flow<List<BookmarkEntity>>
    
    @Query("SELECT * FROM bookmarks WHERE mediaId = :mediaId AND mediaType = :mediaType ORDER BY positionMs ASC")
    suspend fun getBookmarksForMediaOnce(mediaId: Long, mediaType: MediaType): List<BookmarkEntity>
    
    @Query("SELECT * FROM bookmarks WHERE id = :id")
    suspend fun getBookmarkById(id: Long): BookmarkEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmark(bookmark: BookmarkEntity): Long
    
    @Delete
    suspend fun deleteBookmark(bookmark: BookmarkEntity)
    
    @Query("DELETE FROM bookmarks WHERE mediaId = :mediaId AND mediaType = :mediaType")
    suspend fun deleteAllBookmarksForMedia(mediaId: Long, mediaType: MediaType)
}

/**
 * Subtitle DAO
 */
@Dao
interface SubtitleDao {
    
    @Query("SELECT * FROM subtitle_tracks WHERE videoId = :videoId ORDER BY isDefault DESC, language ASC")
    fun getSubtitleTracksForVideo(videoId: Long): Flow<List<SubtitleTrackEntity>>
    
    @Query("SELECT * FROM subtitle_tracks WHERE videoId = :videoId ORDER BY isDefault DESC, language ASC")
    suspend fun getSubtitleTracksForVideoOnce(videoId: Long): List<SubtitleTrackEntity>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubtitleTrack(track: SubtitleTrackEntity): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllSubtitleTracks(tracks: List<SubtitleTrackEntity>)
    
    @Delete
    suspend fun deleteSubtitleTrack(track: SubtitleTrackEntity)
    
    @Query("DELETE FROM subtitle_tracks WHERE videoId = :videoId")
    suspend fun deleteAllSubtitleTracksForVideo(videoId: Long)
    
    @Query("UPDATE subtitle_tracks SET isDefault = CASE WHEN id = :trackId THEN 1 ELSE 0 END WHERE videoId = :videoId")
    suspend fun setDefaultTrack(videoId: Long, trackId: Long)
}

/**
 * Playback History DAO
 */
@Dao
interface PlaybackHistoryDao {
    
    @Query("SELECT * FROM playback_history ORDER BY playedAt DESC LIMIT :limit")
    fun getRecentHistory(limit: Int = 50): Flow<List<PlaybackHistoryEntity>>
    
    @Query("SELECT * FROM playback_history WHERE mediaId = :mediaId AND mediaType = :mediaType ORDER BY playedAt DESC LIMIT 1")
    suspend fun getLatestHistoryForMedia(mediaId: Long, mediaType: MediaType): PlaybackHistoryEntity?
    
    @Query("SELECT * FROM playback_history WHERE playedAt < :timestamp")
    suspend fun getHistoryOlderThan(timestamp: Long): List<PlaybackHistoryEntity>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(history: PlaybackHistoryEntity): Long
    
    @Query("DELETE FROM playback_history WHERE playedAt < :timestamp")
    suspend fun deleteHistoryOlderThan(timestamp: Long)
    
    @Query("DELETE FROM playback_history")
    suspend fun deleteAllHistory()
}

/**
 * Private Vault DAO
 */
@Dao
interface PrivateVaultDao {
    
    @Query("SELECT * FROM private_vault ORDER BY addedAt DESC")
    fun getAllVaultItems(): Flow<List<PrivateVaultItemEntity>>
    
    @Query("SELECT * FROM private_vault WHERE id = :id")
    suspend fun getVaultItemById(id: String): PrivateVaultItemEntity?
    
    @Query("SELECT * FROM private_vault WHERE mediaType = :mediaType ORDER BY addedAt DESC")
    fun getVaultItemsByType(mediaType: MediaType): Flow<List<PrivateVaultItemEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVaultItem(item: PrivateVaultItemEntity)
    
    @Delete
    suspend fun deleteVaultItem(item: PrivateVaultItemEntity)
    
    @Query("DELETE FROM private_vault WHERE id = :id")
    suspend fun deleteVaultItemById(id: String)
    
    @Query("SELECT COUNT(*) FROM private_vault")
    fun getVaultItemCount(): Flow<Int>
}

/**
 * Download DAO
 */
@Dao
interface DownloadDao {
    
    @Query("SELECT * FROM downloads ORDER BY startedAt DESC")
    fun getAllDownloads(): Flow<List<DownloadEntity>>
    
    @Query("SELECT * FROM downloads WHERE id = :id")
    suspend fun getDownloadById(id: String): DownloadEntity?
    
    @Query("SELECT * FROM downloads WHERE status = :status")
    fun getDownloadsByStatus(status: DownloadStatus): Flow<List<DownloadEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDownload(download: DownloadEntity)
    
    @Update
    suspend fun updateDownload(download: DownloadEntity)
    
    @Delete
    suspend fun deleteDownload(download: DownloadEntity)
    
    @Query("UPDATE downloads SET status = :status, downloadedBytes = :downloadedBytes, totalBytes = :totalBytes WHERE id = :id")
    suspend fun updateDownloadProgress(id: String, status: DownloadStatus, downloadedBytes: Long, totalBytes: Long)
    
    @Query("DELETE FROM downloads WHERE status = :status")
    suspend fun deleteCompletedDownloads(status: DownloadStatus = DownloadStatus.COMPLETED)
}

/**
 * Network Stream DAO
 */
@Dao
interface NetworkStreamDao {
    
    @Query("SELECT * FROM network_streams ORDER BY addedAt DESC")
    fun getAllStreams(): Flow<List<NetworkStreamEntity>>
    
    @Query("SELECT * FROM network_streams WHERE id = :id")
    suspend fun getStreamById(id: Long): NetworkStreamEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStream(stream: NetworkStreamEntity): Long
    
    @Update
    suspend fun updateStream(stream: NetworkStreamEntity)
    
    @Delete
    suspend fun deleteStream(stream: NetworkStreamEntity)
    
    @Query("UPDATE network_streams SET lastPlayedAt = :timestamp WHERE id = :id")
    suspend fun updateLastPlayed(id: Long, timestamp: Long)
}

/**
 * EQ Preset DAO
 */
@Dao
interface EqPresetDao {
    
    @Query("SELECT * FROM eq_presets WHERE isSystem = 0 ORDER BY name ASC")
    fun getCustomPresets(): Flow<List<EqPresetEntity>>
    
    @Query("SELECT * FROM eq_presets WHERE isSystem = 1 ORDER BY name ASC")
    fun getSystemPresets(): Flow<List<EqPresetEntity>>
    
    @Query("SELECT * FROM eq_presets WHERE isActive = 1 LIMIT 1")
    fun getActivePreset(): Flow<EqPresetEntity?>
    
    @Query("SELECT * FROM eq_presets WHERE id = :id")
    suspend fun getPresetById(id: Long): EqPresetEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPreset(preset: EqPresetEntity): Long
    
    @Update
    suspend fun updatePreset(preset: EqPresetEntity)
    
    @Delete
    suspend fun deletePreset(preset: EqPresetEntity)
    
    @Query("UPDATE eq_presets SET isActive = CASE WHEN id = :id THEN 1 ELSE 0 END")
    suspend fun setActivePreset(id: Long)
    
    @Query("UPDATE eq_presets SET isActive = 0")
    suspend fun clearActivePreset()
}

/**
 * Recycle Bin DAO
 */
@Dao
interface RecycleBinDao {
    
    @Query("SELECT * FROM recycle_bin ORDER BY deletedAt DESC")
    fun getAllDeletedItems(): Flow<List<RecycleBinEntity>>
    
    @Query("SELECT * FROM recycle_bin WHERE id = :id")
    suspend fun getDeletedItemById(id: String): RecycleBinEntity?
    
    @Query("SELECT * FROM recycle_bin WHERE deletedAt < :timestamp")
    suspend fun getItemsOlderThan(timestamp: Long): List<RecycleBinEntity>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDeletedItem(item: RecycleBinEntity)
    
    @Delete
    suspend fun deleteDeletedItem(item: RecycleBinEntity)
    
    @Query("DELETE FROM recycle_bin WHERE id = :id")
    suspend fun deleteById(id: String)
    
    @Query("DELETE FROM recycle_bin WHERE deletedAt < :timestamp")
    suspend fun deleteItemsOlderThan(timestamp: Long)
    
    @Query("SELECT COUNT(*) FROM recycle_bin")
    fun getItemCount(): Flow<Int>
}