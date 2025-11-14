package com.example.kuit6_android_api.ui.post.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kuit6_android_api.data.model.request.PostCreateRequest
import com.example.kuit6_android_api.data.repository.PostRepository
import com.example.kuit6_android_api.ui.post.state.PostEditUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PostEditViewModel(
    private val postRepository: PostRepository,
    private val postId: Long
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<PostEditUiState>(PostEditUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private var authorUsername: String? = null

    init {
        loadPost()
    }

    private fun loadPost() {
        viewModelScope.launch {
            _uiState.value = PostEditUiState.Loading

            postRepository.getPostDetail(postId)
                .onSuccess { post ->
                    authorUsername = post.author.username
                    android.util.Log.e("PostEditViewModel", "Loaded post - author: ${post.author.username}, id: ${post.id}")
                    _uiState.value = PostEditUiState.Success(post)
                }
                .onFailure { error ->
                    android.util.Log.e("PostEditViewModel", "Failed to load post: ${error.message}")
                    _uiState.value =
                        PostEditUiState.Error(error.message ?: "게시글 조회 실패")
                }
        }
    }

    fun updatePost(
        title: String,
        content: String,
        imageUrl: String?,
        onSuccess: () -> Unit = {}
    ) {
        viewModelScope.launch {
            _uiState.value = PostEditUiState.Loading

            // authorUsername이 null이면 에러 반환
            val author = authorUsername
            if (author == null) {
                android.util.Log.e("PostEditViewModel", "ERROR: authorUsername is null! Cannot update post.")
                _uiState.value = PostEditUiState.Error("작성자 정보를 불러올 수 없습니다. 다시 시도해주세요.")
                return@launch
            }

            val request = PostCreateRequest(
                title = title,
                content = content,
                imageUrl = imageUrl
            )
            
            android.util.Log.d("PostEditViewModel", "updatePost() called")
            android.util.Log.d("PostEditViewModel", "postId: $postId, author: $author")
            android.util.Log.d("PostEditViewModel", "request - title: $title, content: $content, imageUrl: $imageUrl")

            postRepository.editPost(postId, author, request)
                .onSuccess { updatedPost ->
                    android.util.Log.d("PostEditViewModel", "updatePost() success")
                    _uiState.value = PostEditUiState.Success(updatedPost)
                    onSuccess()
                }
                .onFailure { error ->
                    android.util.Log.e("PostEditViewModel", "updatePost() failed: ${error.message}", error)
                    _uiState.value =
                        PostEditUiState.Error(error.message ?: "게시글 수정 실패")
                }
        }
    }
}

