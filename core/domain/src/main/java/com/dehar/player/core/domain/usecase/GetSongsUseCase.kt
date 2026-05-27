package com.dehar.player.core.domain.usecase

import com.dehar.player.core.domain.model.SongItem
import com.dehar.player.core.domain.repository.SongRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSongsUseCase @Inject constructor(
    private val songRepository: SongRepository
) {
    operator fun invoke(sortBy: SortOption = SortOption.TITLE_ASC): Flow<List<SongItem>> {
        return songRepository.getSongs(sortBy)
    }
}

class GetSongsByAlbumUseCase @Inject constructor(
    private val songRepository: SongRepository
) {
    operator fun invoke(albumId: Long): Flow<List<SongItem>> {
        return songRepository.getSongsByAlbum(albumId)
    }
}

class GetFavoriteSongsUseCase @Inject constructor(
    private val songRepository: SongRepository
) {
    operator fun invoke(): Flow<List<SongItem>> {
        return songRepository.getFavoriteSongs()
    }
}

class UpdateSongPlaybackUseCase @Inject constructor(
    private val songRepository: SongRepository
) {
    suspend operator fun invoke(songId: Long, position: Long) {
        songRepository.updateSongPlaybackPosition(songId, position)
    }
}

class ToggleFavoriteSongUseCase @Inject constructor(
    private val songRepository: SongRepository
) {
    suspend operator fun invoke(songId: Long, isFavorite: Boolean) {
        songRepository.toggleFavoriteSong(songId, isFavorite)
    }
}

class SearchSongsUseCase @Inject constructor(
    private val songRepository: SongRepository
) {
    operator fun invoke(query: String): Flow<List<SongItem>> {
        return songRepository.searchSongs(query)
    }
}
