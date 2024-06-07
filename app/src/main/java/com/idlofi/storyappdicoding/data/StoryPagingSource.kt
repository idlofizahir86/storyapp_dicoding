package com.idlofi.storyappdicoding.data

import android.content.Context
import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.idlofi.storyappdicoding.service.ApiService
import com.idlofi.storyappdicoding.network.StoryResponItem
import com.idlofi.storyappdicoding.preferences.SharedPreferenceHelper

class StoryPagingSource(
    private val apiService: ApiService,
    context: Context
) : PagingSource<Int, StoryResponItem>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    private var sharedPreferenceHelper: SharedPreferenceHelper = SharedPreferenceHelper(context)
    private val token = "Bearer ${sharedPreferenceHelper.getUserToken()}"

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StoryResponItem> {
        return try {
            val page = params.key ?: INITIAL_PAGE_INDEX
            Log.d("StoryPagingSource", "Loading page: $page with loadSize: ${params.loadSize}")
            val responseData = apiService.getAllStoriesWithPaging(token, page, params.loadSize)

            if (responseData.error == false) {
                Log.d("StoryPagingSource", "Fetched data: ${responseData.listStory}")
                LoadResult.Page(
                    data = responseData.listStory ?: emptyList(),
                    prevKey = if (page == INITIAL_PAGE_INDEX) null else page - 1,
                    nextKey = if (responseData.listStory.isNullOrEmpty()) null else page + 1
                )
            } else {
                Log.e("StoryPagingSource", "Error fetching data: ${responseData.message}")
                LoadResult.Error(Exception("Error fetching data"))
            }
        } catch (exception: Exception) {
            Log.e("StoryPagingSource", "Exception: $exception")
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, StoryResponItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}
