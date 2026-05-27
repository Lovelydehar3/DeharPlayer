package com.dehar.player.feature.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dehar.player.core.ui.components.SongUiModel
import com.dehar.player.core.ui.components.VideoUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val isLoading: Boolean = false,
    val continuePlaying: PlayingItem? = null,
    val recentVideos: List<VideoUiModel> = emptyList(),
    val recentSongs: List<SongUiModel> = emptyList(),
    val favoriteVideos: List<VideoUiModel> = emptyList(),
    val favoriteSongs: List<SongUiModel> = emptyList(),
    val quickFolders: List<QuickFolder> = emptyList(),
    val smartCollections: List<SmartCollection> = emptyList(),
    val errorMessage: String? = null
)

data class PlayingItem(
    val id: Long,
    val title: String,
    val type: MediaType,
    val positionMs: Long,
    val durationMs: Long,
    val thumbnailUri: String? = null
)

enum class MediaType {
    VIDEO, MUSIC
}

data class QuickFolder(
    val id: Long,
    val name: String,
    val path: String,
    val itemCount: Int,
    val type: MediaType
)

data class SmartCollection(
    val id: Long,
    val name: String,
    val icon: String,
    val itemCount: Int,
    val description: String
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    // TODO: Inject repositories
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadHomeData()
    }

    private fun loadHomeData() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)

                // Mock continue playing
                val continuePlaying = PlayingItem(
                    id = 1,
                    title = "Movie Title - 45% Watched",
                    type = MediaType.VIDEO,
                    positionMs = 4800000,
                    durationMs = 10800000,
                    thumbnailUri = null
                )

                // Mock recent videos
                val recentVideos = listOf(
                    VideoUiModel(1, "Recent Video 1", 3600, null, 524288000, "1080p"),
                    VideoUiModel(2, "Recent Video 2", 7200, null, 1048576000, "720p"),
                    VideoUiModel(3, "Recent Video 3", 1800, null, 262144000, "480p")
                )

                // Mock recent songs
                val recentSongs = listOf(
                    SongUiModel(1, "Recent Song 1", "Artist 1", "Album 1", 240000, hasLyrics = true),
                    SongUiModel(2, "Recent Song 2", "Artist 2", "Album 2", 200000, hasLyrics = false),
                    SongUiModel(3, "Recent Song 3", "Artist 1", "Album 1", 220000, hasLyrics = true)
                )

                // Mock favorite videos
                val favoriteVideos = listOf(
                    VideoUiModel(4, "Favorite Video 1", 5400, null, 786432000, "1080p"),
                    VideoUiModel(5, "Favorite Video 2", 2700, null, 393216000, "720p")
                )

                // Mock favorite songs
                val favoriteSongs = listOf(
                    SongUiModel(4, "Favorite Song 1", "Artist 3", "Album 3", 280000, hasLyrics = true),
                    SongUiModel(5, "Favorite Song 2", "Artist 4", "Album 4", 260000, hasLyrics = false)
                )

                // Mock quick folders
                val quickFolders = listOf(
                    QuickFolder(1, "Movies", "/storage/movies", 45, MediaType.VIDEO),
                    QuickFolder(2, "Music", "/storage/music", 320, MediaType.MUSIC),
                    QuickFolder(3, "Downloads", "/storage/downloads", 128, MediaType.VIDEO),
                    QuickFolder(4, "Documentaries", "/storage/docs", 12, MediaType.VIDEO)
                )

                // Mock smart collections
                val smartCollections = listOf(
                    SmartCollection(1, "New Additions", "🆕", 23, "Recently added media"),
                    SmartCollection(2, "Currently Watching", "▶️", 5, "In-progress videos"),
                    SmartCollection(3, "Most Watched", "⭐", 12, "Your favorites"),
                    SmartCollection(4, "4K Videos", "🎬", 8, "Ultra HD content")
                )

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    continuePlaying = continuePlaying,
                    recentVideos = recentVideos,
                    recentSongs = recentSongs,
                    favoriteVideos = favoriteVideos,
                    favoriteSongs = favoriteSongs,
                    quickFolders = quickFolders,
                    smartCollections = smartCollections
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Unknown error"
                )
            }
        }
    }

    fun refreshHomeData() {
        loadHomeData()
    }
}
