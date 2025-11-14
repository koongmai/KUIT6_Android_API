package com.example.kuit6_android_api.ui.post.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kuit6_android_api.data.model.request.PostCreateRequest
import com.example.kuit6_android_api.data.repository.PostRepository
import com.example.kuit6_android_api.ui.post.state.PostCreateUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PostCreateViewModel(
    private val postRepository: PostRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<PostCreateUiState>(PostCreateUiState.Idle)
    val uiState = _uiState.asStateFlow()

    var uploadedImageUrl by mutableStateOf<String?>(null)
        private set

    fun clearUploadedImageUrl() {
        uploadedImageUrl = null
    }

    fun createPost(
        author: String,
        title: String,
        content: String,
        onSuccess: () -> Unit = {}
    ) {
        viewModelScope.launch {
            _uiState.value = PostCreateUiState.Loading

            val request = PostCreateRequest(
                title = title,
                content = content,
                imageUrl = uploadedImageUrl
            )

            // author가 비어있으면 기본값 사용
            val finalAuthor = author.ifBlank { "anonymous" }

            postRepository.createPost(finalAuthor, request)
                .onSuccess {
                    uploadedImageUrl = null
                   // _uiState.value = PostCreateUiState.Success
                    onSuccess()
                }
                .onFailure { e ->
                    _uiState.value =
                        PostCreateUiState.Error(e.message ?: "작성 실패")
                }
        }
    }
}

