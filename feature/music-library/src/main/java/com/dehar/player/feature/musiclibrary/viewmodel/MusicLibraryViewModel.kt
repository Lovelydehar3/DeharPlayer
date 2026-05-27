package com.dehar.player.feature.musiclibrary.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dehar.player.core.ui.components.SongUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MusicLibraryUiState(
    val isLoading: Boolean = false,
    val songs: List<SongUiModel> = emptyList(),
    val albums: List<AlbumUiModel> = emptyList(),
    val artists: List<ArtistUiModel> = emptyList(),
    val genres: List<GenreUiModel> = emptyList(),
    val playlists: List<PlaylistUiModel> = emptyList(),
    val folders: List<FolderUiModel> = emptyList(),
    val searchQuery: String = "",
    val errorMessage: String? = null
)

data class AlbumUiModel(
    val id: Long,
    val title: String,
    val artist: String,
    val songCount: Int,
    val albumArtUri: String? = null
)

data class ArtistUiModel(
    val id: Long,
    val name: String,
    val songCount: Int,
    val albumCount: Int
)

data class GenreUiModel(
    val id: Long,
    val name: String,
    val songCount: Int
)

data class PlaylistUiModel(
    val id: Long,
    val name: String,
    val songCount: Int,
    val thumbnailUri: String? = null,
    val isSystemPlaylist: Boolean = false
)

data class FolderUiModel(
    val id: Long,
    val path: String,
    val songCount: Int
)

@HiltViewModel
class MusicLibraryViewModel @Inject constructor(
    // TODO: Inject repositories as they become available
) : ViewModel() {

    private val _uiState = MutableStateFlow(MusicLibraryUiState())
    val uiState: StateFlow<MusicLibraryUiState> = _uiState.asStateFlow()

    init {
        loadMusicLibraryData()
    }

    private fun loadMusicLibraryData() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)
                
                // TODO: Load data from repositories when available
                // For now, we'll create mock data to demonstrate the UI
                val mockSongs = listOf(
                    SongUiModel(
                        id = 1,
                        title = "Song 1",
                        artist = "Artist 1",
                        album = "Album 1",
                        durationMs = 180000,
                        hasLyrics = true
                    ),
                    SongUiModel(
                        id = 2,
                        title = "Song 2",
                        artist = "Artist 2",
                        album = "Album 2",
                        durationMs = 240000,
                        hasLyrics = false
                    ),
                    SongUiModel(
                        id = 3,
                        title = "Song 3",
                        artist = "Artist 1",
                        album = "Album 1",
                        durationMs = 200000,
                        hasLyrics = true
                    )
                )

                val mockAlbums = listOf(
                    AlbumUiModel(
                        id = 1,
                        title = "Album 1",
                        artist = "Artist 1",
                        songCount = 12
                    ),
                    AlbumUiModel(
                        id = 2,
                        title = "Album 2",
                        artist = "Artist 2",
                        songCount = 8
                    )
                )

                val mockArtists = listOf(
                    ArtistUiModel(
                        id = 1,
                        name = "Artist 1",
                        songCount = 25,
                        albumCount = 3
                    ),
                    ArtistUiModel(
                        id = 2,
                        name = "Artist 2",
                        songCount = 18,
                        albumCount = 2
                    )
                )

                val mockGenres = listOf(
                    GenreUiModel(id = 1, name = "Pop", songCount = 45),
                    GenreUiModel(id = 2, name = "Rock", songCount = 32),
                    GenreUiModel(id = 3, name = "Jazz", songCount = 28)
                )

                val mockPlaylists = listOf(
                    PlaylistUiModel(
                        id = 1,
                        name = "Favorites",
                        songCount = 42,
                        isSystemPlaylist = true
                    ),
                    PlaylistUiModel(
                        id = 2,
                        name = "Workout Mix",
                        songCount = 38,
                        isSystemPlaylist = false
                    ),
                    PlaylistUiModel(
                        id = 3,
                        name = "Chill Vibes",
                        songCount = 55,
                        isSystemPlaylist = false
                    )
                )

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    songs = mockSongs,
                    albums = mockAlbums,
                    artists = mockArtists,
                    genres = mockGenres,
                    playlists = mockPlaylists
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Unknown error occurred"
                )
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
        // TODO: Filter songs, albums, artists based on search query
    }

    fun clearSearch() {
        _uiState.value = _uiState.value.copy(searchQuery = "")
    }
}
