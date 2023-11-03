package com.example.myapplication

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.myapplication.data.Injection
import com.example.myapplication.data.StoryRepository
import com.example.myapplication.data.response.ListStoryItem

class StoryPagingViewModel (storyRepository: StoryRepository) : ViewModel() {

    val story: LiveData<PagingData<ListStoryItem>> =
        storyRepository.getStories().cachedIn(viewModelScope)

}
class PagingViewModelFactory(context: Context) : ViewModelProvider.Factory {
    private val userPreference = UserPreference(context)
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StoryPagingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")

            return StoryPagingViewModel(Injection.providePagingRepository(userPreference.getUser().token.toString())) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}