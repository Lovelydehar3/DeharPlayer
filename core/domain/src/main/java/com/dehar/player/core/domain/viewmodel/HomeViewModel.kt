package com.dehar.player.core.domain.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dehar.player.core.domain.usecase.GetVideosUseCase
import com.dehar.player.core.domain.usecase.SortOption
import com.dehar.player.core.domain.usecase.VideoItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getVideosUseCase: GetVideosUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _sortOption = MutableStateFlow(SortOption.DATE_DESC)
    val sortOption: StateFlow<SortOption> = _sortOption.asStateFlow()

    init {
        loadVideos()
    }

    fun loadVideos() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            try {
                getVideosUseCase(sortBy = _sortOption.value).collect { videos ->
                    _uiState.update {
                        HomeUiState.Success(
                            videos = videos,
                            recentlyPlayed = videos.filter { it.playCount > 0 }
                                .sortedByDescending { it.lastPlayedPosition }
                                .take(10),
                            favorites = videos.filter { it.isFavorite }
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun setSortOption(sortOption: SortOption) {
        _sortOption.value = sortOption
        loadVideos()
    }

    fun onRetry() {
        loadVideos()
    }
}

sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(
        val videos: List<VideoItem> = emptyList(),
        val recentlyPlayed: List<VideoItem> = emptyList(),
        val favorites: List<VideoItem> = emptyList()
    ) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}
