package com.example.myapplication.data

import com.example.myapplication.data.retrofit.ApiConfig

object Injection {
    fun provideRepository(): UploadRepository {
        val apiService = ApiConfig.getApiService()
        return UploadRepository.getInstance(apiService)
    }
}