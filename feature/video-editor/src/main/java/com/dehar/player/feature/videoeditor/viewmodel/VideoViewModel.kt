package com.dehar.player.feature.videoeditor.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dehar.player.feature.videoeditor.model.EncodingProgress
import com.dehar.player.feature.videoeditor.model.ExportQuality
import com.dehar.player.feature.videoeditor.model.ExportSettings
import com.dehar.player.feature.videoeditor.model.VideoClip
import com.dehar.player.feature.videoeditor.model.VideoEditProject
import com.dehar.player.feature.videoeditor.model.VideoEditState
import com.dehar.player.feature.videoeditor.model.VideoEditorUiState
import com.dehar.player.feature.videoeditor.model.VideoFilter
import com.dehar.player.feature.videoeditor.model.VideoTrim
import com.dehar.player.feature.videoeditor.repository.VideoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoViewModel @Inject constructor(
    private val repository: VideoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        VideoEditorUiState(
            editState = VideoEditState.IDLE,
            error = null,
            isLoading = false
        )
    )
    val uiState: StateFlow<VideoEditorUiState> = _uiState.asStateFlow()

    /**
     * Load video and extract metadata
     */
    fun loadVideo(videoPath: String) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, error = null)

                val project = repository.getVideoMetadata(videoPath)

                if (project != null) {
                    _uiState.value = _uiState.value.copy(
                        project = project,
                        isLoading = false,
                        editState = VideoEditState.IDLE
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Failed to load video metadata",
                        editState = VideoEditState.ERROR
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Error loading video",
                    editState = VideoEditState.ERROR
                )
            }
        }
    }

    /**
     * Create a trim clip
     */
    fun createTrimClip(startMs: Long, endMs: Long) {
        val project = _uiState.value.project ?: return

        val trim = VideoTrim(
            id = "trim_${System.currentTimeMillis()}",
            startTime = startMs,
            endTime = endMs,
            duration = endMs - startMs
        )

        if (!trim.isValid()) {
            _uiState.value = _uiState.value.copy(error = "Invalid trim range")
            return
        }

        val clip = VideoClip(
            id = trim.id,
            startTime = startMs,
            endTime = endMs
        )

        val currentClips = _uiState.value.clips.toMutableList()
        currentClips.add(clip)

        _uiState.value = _uiState.value.copy(
            clips = currentClips,
            selectedClip = clip
        )
    }

    /**
     * Add filter to current clip
     */
    fun addFilter(filter: VideoFilter) {
        val currentClip = _uiState.value.selectedClip ?: return

        val newClip = currentClip.copy(
            filters = currentClip.filters + filter
        )

        val currentClips = _uiState.value.clips.toMutableList()
        val index = currentClips.indexOfFirst { it.id == currentClip.id }

        if (index >= 0) {
            currentClips[index] = newClip
        }

        val currentFilters = _uiState.value.filters.toMutableList()
        currentFilters.add(filter)

        _uiState.value = _uiState.value.copy(
            clips = currentClips,
            selectedClip = newClip,
            filters = currentFilters
        )
    }

    /**
     * Remove filter
     */
    fun removeFilter(filterId: String) {
        val currentClip = _uiState.value.selectedClip ?: return

        val newClip = currentClip.copy(
            filters = currentClip.filters.filter { it.id != filterId }
        )

        val currentClips = _uiState.value.clips.toMutableList()
        val index = currentClips.indexOfFirst { it.id == currentClip.id }

        if (index >= 0) {
            currentClips[index] = newClip
        }

        val currentFilters = _uiState.value.filters.toMutableList()
        currentFilters.removeAll { it.id == filterId }

        _uiState.value = _uiState.value.copy(
            clips = currentClips,
            selectedClip = newClip,
            filters = currentFilters
        )
    }

    /**
     * Trim video and export
     */
    fun trimAndExport(
        startMs: Long,
        endMs: Long,
        exportSettings: ExportSettings = ExportSettings()
    ) {
        val project = _uiState.value.project ?: return

        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(
                    editState = VideoEditState.TRIMMING,
                    error = null
                )

                val trim = VideoTrim(
                    id = "trim_${System.currentTimeMillis()}",
                    startTime = startMs,
                    endTime = endMs,
                    duration = endMs - startMs
                )

                val outputPath = repository.trimVideo(project, trim, exportSettings) { progress ->
                    _uiState.value = _uiState.value.copy(
                        editState = VideoEditState.ENCODING,
                        encodingProgress = progress
                    )
                }

                if (outputPath != null) {
                    _uiState.value = _uiState.value.copy(
                        outputPath = outputPath,
                        editState = VideoEditState.SUCCESS,
                        encodingProgress = null
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        editState = VideoEditState.ERROR,
                        error = "Failed to trim video",
                        encodingProgress = null
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    editState = VideoEditState.ERROR,
                    error = e.message ?: "Trim operation failed",
                    encodingProgress = null
                )
            }
        }
    }

    /**
     * Apply filters and export
     */
    fun applyFiltersAndExport(
        filters: List<VideoFilter>,
        exportSettings: ExportSettings = ExportSettings()
    ) {
        val project = _uiState.value.project ?: return

        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(
                    editState = VideoEditState.FILTERING,
                    error = null
                )

                val outputPath = repository.applyFilters(project, filters, exportSettings) { progress ->
                    _uiState.value = _uiState.value.copy(
                        editState = VideoEditState.ENCODING,
                        encodingProgress = progress
                    )
                }

                if (outputPath != null) {
                    _uiState.value = _uiState.value.copy(
                        outputPath = outputPath,
                        editState = VideoEditState.SUCCESS,
                        encodingProgress = null
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        editState = VideoEditState.ERROR,
                        error = "Failed to apply filters",
                        encodingProgress = null
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    editState = VideoEditState.ERROR,
                    error = e.message ?: "Filter operation failed",
                    encodingProgress = null
                )
            }
        }
    }

    /**
     * Rotate video
     */
    fun rotateVideo(rotation: Int, exportSettings: ExportSettings = ExportSettings()) {
        val project = _uiState.value.project ?: return

        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(editState = VideoEditState.ENCODING)

                val outputPath = repository.rotateVideo(project, rotation, exportSettings) { progress ->
                    _uiState.value = _uiState.value.copy(
                        encodingProgress = progress
                    )
                }

                if (outputPath != null) {
                    _uiState.value = _uiState.value.copy(
                        outputPath = outputPath,
                        editState = VideoEditState.SUCCESS
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        editState = VideoEditState.ERROR,
                        error = "Failed to rotate video"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    editState = VideoEditState.ERROR,
                    error = e.message ?: "Rotation failed"
                )
            }
        }
    }

    /**
     * Change playback speed
     */
    fun changeSpeed(speed: Float, exportSettings: ExportSettings = ExportSettings()) {
        val project = _uiState.value.project ?: return

        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(editState = VideoEditState.ENCODING)

                val outputPath = repository.changeSpeed(project, speed, exportSettings) { progress ->
                    _uiState.value = _uiState.value.copy(encodingProgress = progress)
                }

                if (outputPath != null) {
                    _uiState.value = _uiState.value.copy(
                        outputPath = outputPath,
                        editState = VideoEditState.SUCCESS
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        editState = VideoEditState.ERROR,
                        error = "Failed to change speed"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    editState = VideoEditState.ERROR,
                    error = e.message ?: "Speed change failed"
                )
            }
        }
    }

    /**
     * Generate thumbnail
     */
    fun generateThumbnail(timeMs: Long) {
        val project = _uiState.value.project ?: return

        viewModelScope.launch {
            try {
                repository.extractFrame(project.sourceVideoPath, timeMs)
            } catch (e: Exception) {
                // Handle silently
            }
        }
    }

    /**
     * Select clip
     */
    fun selectClip(clipId: String) {
        val clip = _uiState.value.clips.find { it.id == clipId }
        if (clip != null) {
            _uiState.value = _uiState.value.copy(selectedClip = clip)
        }
    }

    /**
     * Delete clip
     */
    fun deleteClip(clipId: String) {
        val currentClips = _uiState.value.clips.filter { it.id != clipId }
        _uiState.value = _uiState.value.copy(
            clips = currentClips,
            selectedClip = if (_uiState.value.selectedClip?.id == clipId) null else _uiState.value.selectedClip
        )
    }

    /**
     * Save project
     */
    fun saveProject(projectName: String) {
        viewModelScope.launch {
            try {
                val project = _uiState.value.project?.copy(projectName = projectName) ?: return@launch
                repository.saveProject(project)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    /**
     * Clear error
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    /**
     * Reset editor
     */
    fun reset() {
        _uiState.value = VideoEditorUiState(editState = VideoEditState.IDLE)
    }

    /**
     * Get available storage
     */
    fun getAvailableStorage(): Long = repository.getAvailableSpace()
}
