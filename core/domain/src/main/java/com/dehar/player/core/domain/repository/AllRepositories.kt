package com.dehar.player.core.domain.repository

import com.dehar.player.core.domain.model.AlbumItem
import com.dehar.player.core.domain.model.ArtistItem
import com.dehar.player.core.domain.model.SongItem
import kotlinx.coroutines.flow.Flow

interface AlbumRepository {
    fun getAllAlbums(): Flow<List<AlbumItem>>
    fun getAlbumById(id: Long): Flow<AlbumItem?>
    fun getAlbumSongs(albumId: Long): Flow<List<SongItem>>
}

interface ArtistRepository {
    fun getAllArtists(): Flow<List<ArtistItem>>
    fun getArtistById(id: Long): Flow<ArtistItem?>
    fun getArtistSongs(artistId: Long): Flow<List<SongItem>>
}
