package com.example.kuit6_android_api.data.repository

import android.util.Log
import com.example.kuit6_android_api.data.api.ApiService
import com.example.kuit6_android_api.data.model.request.PostCreateRequest
import com.example.kuit6_android_api.data.model.response.PostResponse
import retrofit2.HttpException
import java.io.IOException

class PostRepositoryImpl(
    private val apiService: ApiService //수동으로 , 파라미터로 의존성을 주입한다.
): PostRepository {
    override suspend fun getPosts(): Result<List<PostResponse>> {
        return runCatching {
            val response = apiService.getPosts()

            if(response.success && response.data !=null) {
                response.data
            }else{
                throw Exception( response.message ?: "게시글 불러오기 실패")
            }
        }.onFailure { error->
            val errorMessage = when (error) {
                is HttpException -> {
                    val statusCode = error.code()
                    val errorBody = error.response()?.errorBody()?.string()
                    Log.e("PostRepository", "getPosts() HTTP error - Status: $statusCode, Body: $errorBody")
                    when (statusCode) {
                        403 -> "접근 권한이 없습니다. (403)"
                        500 -> "서버 오류가 발생했습니다. (500)"
                        else -> "HTTP 오류: $statusCode"
                    }
                }
                is IOException -> {
                    Log.e("PostRepository", "getPosts() Network error: ${error.message}")
                    "네트워크 연결을 확인해주세요."
                }
                else -> {
                    Log.e("PostRepository", "getPosts() error: ${error.message}", error)
                    error.message ?: "게시글 불러오기 실패"
                }
            }
            Log.e("PostRepository", "getPosts() error: $errorMessage")
        }
    }
    override suspend fun getPostDetail(id: Long): Result<PostResponse>{
        return runCatching {
            val response = apiService.getPostDetail(id)

            if (response.success && response.data != null){
                response.data
            } else {
                throw Exception(response.message ?: "게시글 상세 조회 실패")
            }
        }.onFailure { error->
            val errorMessage = when (error) {
                is HttpException -> {
                    val statusCode = error.code()
                    val errorBody = error.response()?.errorBody()?.string()
                    Log.e("PostRepository", "getPostDetail() HTTP error - Status: $statusCode, Body: $errorBody")
                    when (statusCode) {
                        403 -> "접근 권한이 없습니다. (403)"
                        500 -> "서버 오류가 발생했습니다. (500)"
                        else -> "HTTP 오류: $statusCode"
                    }
                }
                is IOException -> {
                    Log.e("PostRepository", "getPostDetail() Network error: ${error.message}")
                    "네트워크 연결을 확인해주세요."
                }
                else -> {
                    Log.e("PostRepository", "getPostDetail() error: ${error.message}", error)
                    error.message ?: "게시글 상세 조회 실패"
                }
            }
            Log.e("PostRepository", "getPostDetail() error: $errorMessage")
        }
    }
    override suspend fun createPost(
        author: String,
        request: PostCreateRequest
    ): Result<PostResponse> {
        return runCatching {
            val response = apiService.createPost(author, request)

            if (response.success && response.data != null) {
                response.data
            } else {
                throw Exception(response.message ?: "게시글 생성 실패")
            }
        }.onFailure { error ->
            val errorMessage = when (error) {
                is HttpException -> {
                    val statusCode = error.code()
                    val errorBody = error.response()?.errorBody()?.string()
                    Log.e("PostRepository", "createPost() HTTP error - Status: $statusCode, Body: $errorBody")
                    when (statusCode) {
                        403 -> "접근 권한이 없습니다. (403)"
                        500 -> "서버 오류가 발생했습니다. (500)"
                        else -> "HTTP 오류: $statusCode"
                    }
                }
                is IOException -> {
                    Log.e("PostRepository", "createPost() Network error: ${error.message}")
                    "네트워크 연결을 확인해주세요."
                }
                else -> {
                    Log.e("PostRepository", "createPost() error: ${error.message}", error)
                    error.message ?: "게시글 생성 실패"
                }
            }
            Log.e("PostRepository", "createPost() error: $errorMessage")
        }
    }

    override suspend fun editPost(
        id: Long,
        author: String,
        request: PostCreateRequest
    ): Result<PostResponse> {
        return runCatching {
            val response = apiService.editPost(id, author, request)

            if (response.success && response.data != null) {
                response.data
            } else {
                throw Exception(response.message ?: "게시글 수정 실패")
            }
        }.onFailure { error ->
            val errorMessage = when (error) {
                is HttpException -> {
                    val statusCode = error.code()
                    val errorBody = error.response()?.errorBody()?.string()
                    Log.e("PostRepository", "editPost() HTTP error - Status: $statusCode, Body: $errorBody")
                    when (statusCode) {
                        403 -> "접근 권한이 없습니다. (403)"
                        500 -> "서버 오류가 발생했습니다. (500)"
                        else -> "HTTP 오류: $statusCode"
                    }
                }
                is IOException -> {
                    Log.e("PostRepository", "editPost() Network error: ${error.message}")
                    "네트워크 연결을 확인해주세요."
                }
                else -> {
                    Log.e("PostRepository", "editPost() error: ${error.message}", error)
                    error.message ?: "게시글 수정 실패"
                }
            }
            Log.e("PostRepository", "editPost() error: $errorMessage")
        }
    }

    override suspend fun deletePost(id: Long, author: String): Result<Unit> {
        return runCatching {
            Log.d("PostRepository", "deletePost() called - id: $id, author: $author")
            
            val response = apiService.deletePost(id, author)
            
            Log.d("PostRepository", "deletePost() response - success: ${response.success}, message: ${response.message}")

            if (response.success) {
                Unit
            } else {
                Log.e("PostRepository", "deletePost() failed - success: ${response.success}, message: ${response.message}")
                throw Exception(response.message ?: "게시글 삭제 실패")
            }
        }.onFailure { error ->
            val errorMessage = when (error) {
                is HttpException -> {
                    val statusCode = error.code()
                    val errorBody = error.response()?.errorBody()?.string()
                    Log.e("PostRepository", "deletePost() HTTP error - Status: $statusCode, Body: $errorBody")
                    when (statusCode) {
                        403 -> "접근 권한이 없습니다. (403)"
                        500 -> "서버 오류가 발생했습니다. (500)"
                        else -> "HTTP 오류: $statusCode"
                    }
                }
                is IOException -> {
                    Log.e("PostRepository", "deletePost() Network error: ${error.message}")
                    "네트워크 연결을 확인해주세요."
                }
                else -> {
                    Log.e("PostRepository", "deletePost() error: ${error.message}", error)
                    error.message ?: "게시글 삭제 실패"
                }
            }
            Log.e("PostRepository", "deletePost() error: $errorMessage")
        }
    }

}

