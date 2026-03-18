package com.funhub.ui.tags

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.funhub.data.remote.api.FunHubApi
import com.funhub.data.remote.dto.CreateTagRequest
import com.funhub.data.remote.dto.TagDto
import com.funhub.data.remote.dto.UpdateTagRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TagsUiState(
    val tags: List<TagDto> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val showAddDialog: Boolean = false,
    val showEditDialog: Boolean = false,
    val selectedTag: TagDto? = null
)

@HiltViewModel
class TagsViewModel @Inject constructor(
    private val api: FunHubApi
) : ViewModel() {

    private val _uiState = MutableStateFlow(TagsUiState())
    val uiState: StateFlow<TagsUiState> = _uiState.asStateFlow()

    init {
        loadTags()
    }

    fun loadTags() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val response = api.getTags()
                if (response.isSuccessful) {
                    _uiState.update { it.copy(tags = response.body() ?: emptyList(), isLoading = false) }
                } else {
                    _uiState.update { it.copy(error = "加载标签失败", isLoading = false) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun createTag(name: String, color: String) {
        viewModelScope.launch {
            try {
                val response = api.createTag(CreateTagRequest(name, color))
                if (response.isSuccessful) {
                    loadTags()
                    hideAddDialog()
                } else {
                    _uiState.update { it.copy(error = "创建标签失败") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }

    fun updateTag(id: Int, name: String, color: String) {
        viewModelScope.launch {
            try {
                val response = api.updateTag(id, UpdateTagRequest(name, color))
                if (response.isSuccessful) {
                    loadTags()
                    hideEditDialog()
                } else {
                    _uiState.update { it.copy(error = "更新标签失败") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }

    fun deleteTag(id: Int) {
        viewModelScope.launch {
            try {
                val response = api.deleteTag(id)
                if (response.isSuccessful) {
                    loadTags()
                } else {
                    _uiState.update { it.copy(error = "删除标签失败") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }

    fun showAddDialog() {
        _uiState.update { it.copy(showAddDialog = true) }
    }

    fun hideAddDialog() {
        _uiState.update { it.copy(showAddDialog = false) }
    }

    fun showEditDialog(tag: TagDto) {
        _uiState.update { it.copy(showEditDialog = true, selectedTag = tag) }
    }

    fun hideEditDialog() {
        _uiState.update { it.copy(showEditDialog = false, selectedTag = null) }
    }
}
