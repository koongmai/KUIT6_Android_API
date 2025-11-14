package com.example.kuit6_android_api.ui.post.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kuit6_android_api.data.repository.PostRepository
import com.example.kuit6_android_api.ui.post.state.PostDetailUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PostDetailViewModel(
    private val postRepository: PostRepository,
    private val postId: Long
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<PostDetailUiState>(PostDetailUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private var authorUsername: String? = null

    init {
        loadDetail()
    }

    private fun loadDetail() {
        viewModelScope.launch {
            _uiState.value = PostDetailUiState.Loading

            postRepository.getPostDetail(postId)
                .onSuccess { post ->
                    authorUsername = post.author.username
                    android.util.Log.d("PostDetailViewModel", "Loaded post - author: ${post.author.username}, id: ${post.id}")
                    _uiState.value = PostDetailUiState.Success(post)
                }
                .onFailure { error ->
                    android.util.Log.e("PostDetailViewModel", "Failed to load post: ${error.message}")
                    _uiState.value =
                        PostDetailUiState.Error(error.message ?: "상세 조회 실패")
                }
        }
    }

    fun deletePost(onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            // authorUsername이 null이면 에러 반환
            val author = authorUsername
            if (author == null) {
                android.util.Log.e("PostDetailViewModel", "ERROR: authorUsername is null! Cannot delete post.")
                _uiState.value = PostDetailUiState.Error("작성자 정보를 불러올 수 없습니다. 다시 시도해주세요.")
                return@launch
            }
            
            android.util.Log.d("PostDetailViewModel", "deletePost() called - postId: $postId, author: $author")
            
            postRepository.deletePost(postId, author)
                .onSuccess {
                    android.util.Log.d("PostDetailViewModel", "deletePost() success")
                    _uiState.value = PostDetailUiState.DeleteSuccess
                    onSuccess()
                }
                .onFailure { error ->
                    android.util.Log.e("PostDetailViewModel", "deletePost() failed: ${error.message}", error)
                    _uiState.value =
                        PostDetailUiState.Error(error.message ?: "삭제 실패")
                }
        }
    }
}

