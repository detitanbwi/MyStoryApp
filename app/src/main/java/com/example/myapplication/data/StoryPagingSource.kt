package com.example.myapplication.data

import android.content.Context
import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.myapplication.UserPreference
import com.example.myapplication.data.response.ListStoryItem
import com.example.myapplication.data.response.StoryListResponse
import com.example.myapplication.data.retrofit.ApiService
import okhttp3.Response

class StoryPagingSource(private val apiService: ApiService) : PagingSource<Int, ListStoryItem>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        return try {
            val page = params.key ?: INITIAL_PAGE_INDEX
            val responseData = apiService.getStoriesPaging("Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLTE0YW9tSVJGbFNQUllKcksiLCJpYXQiOjE2OTgzNzUzNjR9.IiNuGO806yU4cyTB2sqYKj3h4jdMYRNTrfw2FT9udy8",page, params.loadSize)

            if (responseData.isSuccessful) {
                val data = responseData.body()?.listStory ?: emptyList()
                return LoadResult.Page(
                    data = data,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (data.isNullOrEmpty()) null else page + 1
                )
            }else{
                return LoadResult.Error(Exception("Failed to fetch data"))
            }
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}