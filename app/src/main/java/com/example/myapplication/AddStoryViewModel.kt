package com.example.myapplication

import android.content.Context
import android.database.Cursor
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.UploadRepository
import com.example.myapplication.data.response.PostStoryResponse
import com.example.myapplication.data.retrofit.ApiConfig
import okhttp3.MultipartBody
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.Response
import java.io.File

class AddStoryViewModel(private val repository: UploadRepository) : ViewModel() {
    fun uploadImage(token: String, file: File, description: String) = repository.uploadImage(token ,file, description)
}
