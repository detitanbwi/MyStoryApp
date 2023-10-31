package com.example.myapplication

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.myapplication.data.Injection
import com.example.myapplication.data.StoryRepository
import com.example.myapplication.data.response.ListStoryItem

class StoryPagingViewModel (quoteRepository: StoryRepository) : ViewModel() {

    val story: LiveData<PagingData<ListStoryItem>> =
        quoteRepository.getStories().cachedIn(viewModelScope)
}
class PagingViewModelFactory(context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StoryPagingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StoryPagingViewModel(Injection.providePagingRepository()) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}