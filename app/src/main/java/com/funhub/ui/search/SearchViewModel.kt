package com.funhub.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.funhub.domain.model.Result
import com.funhub.domain.model.Video
import com.funhub.domain.usecase.GetVideosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getVideosUseCase: GetVideosUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchResults = MutableStateFlow<List<Video>>(emptyList())
    val searchResults: StateFlow<List<Video>> = _searchResults.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _searchHistory = MutableStateFlow<List<String>>(emptyList())
    val searchHistory: StateFlow<List<String>> = _searchHistory.asStateFlow()

    private var allVideos: List<Video> = emptyList()

    init {
        loadVideos()
        loadSearchHistory()

        // Debounce search query
        _searchQuery
            .debounce(300)
            .onEach { query ->
                if (query.isNotBlank()) {
                    performSearch(query)
                }
            }
            .launchIn(viewModelScope)
    }

    private fun loadVideos() {
        viewModelScope.launch {
            when (val result = getVideosUseCase()) {
                is Result.Success -> {
                    allVideos = result.data
                }
                else -> {
                    allVideos = emptyList()
                }
            }
        }
    }

    private fun loadSearchHistory() {
        // TODO: Load from DataStore
        _searchHistory.value = emptyList()
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun performSearch() {
        performSearch(_searchQuery.value)
    }

    private fun performSearch(query: String) {
        viewModelScope.launch {
            _isLoading.value = true

            // Simulate network delay
            kotlinx.coroutines.delay(300)

            val results = if (query.isBlank()) {
                emptyList()
            } else {
                allVideos.filter { video ->
                    video.title.contains(query, ignoreCase = true) ||
                    video.description?.contains(query, ignoreCase = true) == true ||
                    video.creatorName.contains(query, ignoreCase = true)
                }
            }

            _searchResults.value = results
            _isLoading.value = false

            // Add to history if not empty and has results
            if (query.isNotBlank() && results.isNotEmpty()) {
                addToHistory(query)
            }
        }
    }

    private fun addToHistory(query: String) {
        val currentHistory = _searchHistory.value.toMutableList()
        currentHistory.remove(query) // Remove if exists to avoid duplicates
        currentHistory.add(0, query) // Add to front

        // Keep only last 10 searches
        _searchHistory.value = currentHistory.take(10)
    }

    fun clearSearch() {
        _searchQuery.value = ""
        _searchResults.value = emptyList()
    }

    fun clearSearchHistory() {
        _searchHistory.value = emptyList()
        // TODO: Clear from DataStore
    }
}
