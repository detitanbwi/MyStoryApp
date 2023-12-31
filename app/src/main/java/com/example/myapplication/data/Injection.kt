package com.example.myapplication.data

import com.example.myapplication.data.retrofit.ApiConfig

object Injection {
    fun provideRepository(): UploadRepository {
        val apiService = ApiConfig.getApiService()
        return UploadRepository.getInstance(apiService)
    }
    fun providePagingRepository(token: String): StoryRepository {
        val apiService = ApiConfig.getApiService()
        return StoryRepository(apiService,token)
    }
}
