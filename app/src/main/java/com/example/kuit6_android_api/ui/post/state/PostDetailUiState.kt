package com.example.kuit6_android_api.ui.post.state

import com.example.kuit6_android_api.data.model.response.PostResponse

sealed class PostDetailUiState {

    data object Loading: PostDetailUiState()

    //상세 조회 성공
    data class Success(
        val post: PostResponse
    ): PostDetailUiState()

    data class Error(
        val message: String
    ): PostDetailUiState()

    data object DeleteSuccess: PostDetailUiState()
}
