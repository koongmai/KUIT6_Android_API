package com.example.kuit6_android_api.data.api

import com.example.kuit6_android_api.data.model.request.PostCreateRequest
import com.example.kuit6_android_api.data.model.response.BaseResponse
import com.example.kuit6_android_api.data.model.response.PostResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("/api/posts")
    suspend fun getPosts(): BaseResponse<List<PostResponse>>

    @POST("/api/posts")
    suspend fun createPost(
        @Query("author") author: String = "경민",
        @Body request: PostCreateRequest
    ): BaseResponse<PostResponse>

    @GET("/api/posts/{id}")
    suspend fun getPostDetail(
        @Path("id") id: Long
    ): BaseResponse<PostResponse>

    @PUT("/api/posts/{id}")
    suspend fun editPost(
        @Path("id") id: Long,
        @Query("author") author: String,
        @Body request: PostCreateRequest
    ): BaseResponse<PostResponse>

    @DELETE("/api/posts/{id}")
    suspend fun deletePost(
        @Path("id") id: Long,
        @Query("author") author: String
    ): BaseResponse<Unit>
}