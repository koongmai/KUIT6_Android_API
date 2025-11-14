package com.example.kuit6_android_api.data.di

import com.example.kuit6_android_api.data.api.ApiService
import com.example.kuit6_android_api.data.api.RetrofitClient
import com.example.kuit6_android_api.data.repository.PostRepository
import com.example.kuit6_android_api.data.repository.PostRepositoryImpl

class AppContainer { //di를 수동으로 주입한다.
    private val apiService: ApiService by lazy {
        RetrofitClient.apiService
    }
    val postRepository: PostRepository by lazy{
        PostRepositoryImpl(apiService)
    }
}
