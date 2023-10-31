package com.example.myapplication.data

import com.example.myapplication.data.response.PostStoryResponse
import com.example.myapplication.data.retrofit.ApiService
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File
import androidx.lifecycle.liveData


class UploadRepository private constructor(
    private val apiService: ApiService
) {

    fun uploadImage(token: String,imageFile: File, description: String, lat: Double? = null,
                    lon: Double? = null) = liveData {
        emit(ResultState.Loading)
        val requestBody = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            imageFile.name,
            requestImageFile
        )
        val latRequestBody = lat?.toString()?.toRequestBody("text/plain".toMediaType())
        val lonRequestBody = lon?.toString()?.toRequestBody("text/plain".toMediaType())
        try {
            val successResponse = if (lat != null && lon != null) {
                apiService.postStory(token, multipartBody, requestBody, latRequestBody, lonRequestBody)
            } else {
                apiService.postStory(token, multipartBody, requestBody)
            }
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, PostStoryResponse::class.java)
            emit(ResultState.Error(errorResponse.message))
        }

    }

    companion object {
        @Volatile
        private var instance: UploadRepository? = null
        fun getInstance(apiService: ApiService) =
            instance ?: synchronized(this) {
                instance ?: UploadRepository(apiService)
            }.also { instance = it }
    }
}