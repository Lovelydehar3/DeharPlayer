package com.dehar.player.core.domain.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dehar.player.core.domain.model.SongItem
import com.dehar.player.core.domain.usecase.GetFavoriteSongsUseCase
import com.dehar.player.core.domain.usecase.GetSongsUseCase
import com.dehar.player.core.domain.usecase.SearchSongsUseCase
import com.dehar.player.core.domain.usecase.SortOption
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MusicLibraryViewModel @Inject constructor(
    private val getSongsUseCase: GetSongsUseCase,
    private val getFavoriteSongsUseCase: GetFavoriteSongsUseCase,
    private val searchSongsUseCase: SearchSongsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<MusicLibraryUiState>(MusicLibraryUiState.Loading)
    val uiState: StateFlow<MusicLibraryUiState> = _uiState.asStateFlow()

    private val _currentTab = MutableStateFlow<MusicTab>(MusicTab.SONGS)
    val currentTab: StateFlow<MusicTab> = _currentTab.asStateFlow()

    private val _sortOption = MutableStateFlow(SortOption.TITLE_ASC)
    val sortOption: StateFlow<SortOption> = _sortOption.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    init {
        loadSongs()
    }

    fun loadSongs() {
        viewModelScope.launch {
            _uiState.value = MusicLibraryUiState.Loading
            try {
                when (_currentTab.value) {
                    MusicTab.SONGS -> {
                        getSongsUseCase(_sortOption.value).collect { songs ->
                            _uiState.update { MusicLibraryUiState.Success(songs = songs) }
                        }
                    }
                    MusicTab.FAVORITES -> {
                        getFavoriteSongsUseCase().collect { songs ->
                            _uiState.update { MusicLibraryUiState.Success(songs = songs) }
                        }
                    }
                    MusicTab.ALBUMS -> {
                        // TODO: Implement albums view
                        _uiState.update { MusicLibraryUiState.Success(songs = emptyList()) }
                    }
                    MusicTab.ARTISTS -> {
                        // TODO: Implement artists view
                        _uiState.update { MusicLibraryUiState.Success(songs = emptyList()) }
                    }
                }
            } catch (e: Exception) {
                _uiState.value = MusicLibraryUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun setTab(tab: MusicTab) {
        _currentTab.value = tab
        loadSongs()
    }

    fun setSortOption(sortOption: SortOption) {
        _sortOption.value = sortOption
        loadSongs()
    }

    fun search(query: String) {
        _searchQuery.value = query
        if (query.isEmpty()) {
            loadSongs()
            return
        }
        viewModelScope.launch {
            try {
                searchSongsUseCase(query).collect { songs ->
                    _uiState.update { MusicLibraryUiState.Success(songs = songs) }
                }
            } catch (e: Exception) {
                _uiState.value = MusicLibraryUiState.Error(e.message ?: "Search failed")
            }
        }
    }

    fun onRetry() {
        loadSongs()
    }
}

sealed class MusicLibraryUiState {
    object Loading : MusicLibraryUiState()
    data class Success(val songs: List<SongItem> = emptyList()) : MusicLibraryUiState()
    data class Error(val message: String) : MusicLibraryUiState()
}

enum class MusicTab {
    SONGS, ALBUMS, ARTISTS, FAVORITES
}
