package com.example.myapplication

import androidx.lifecycle.ViewModel
import com.example.myapplication.data.UploadRepository
import java.io.File

class AddStoryViewModel(private val repository: UploadRepository) : ViewModel() {
    fun uploadImage(token: String, file: File, description: String,lat: Double? = null, lon: Double? = null) = repository.uploadImage(token ,file, description, lat, lon)
}
