package com.example.myapplication

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.response.ListStoryItem
import com.example.myapplication.data.response.StoryListResponse
import com.example.myapplication.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {
    private val _stories = MutableLiveData<StoryListResponse>()
    val stories: LiveData<StoryListResponse> = _stories

    private val _listReview = MutableLiveData<List<ListStoryItem>>()
    val listReview: LiveData<List<ListStoryItem>> = _listReview

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getStories(token: String) {
        _isLoading.value = true
        Log.d("getStories","Called")
        val client = ApiConfig.getApiService().getStories("Bearer $token")
        client.enqueue(object : Callback<StoryListResponse> {
            override fun onResponse(
                call: Call<StoryListResponse>,
                response: Response<StoryListResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _stories.value = response.body()
                    _listReview.value = response.body()?.listStory
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<StoryListResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }
}