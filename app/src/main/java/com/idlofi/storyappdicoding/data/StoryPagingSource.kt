package com.idlofi.storyappdicoding.data

import android.content.Context
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
            val responseData = apiService.getAllStoriesWithPaging(token, page, params.loadSize)


            LoadResult.Page(
                data = responseData,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (responseData.isNullOrEmpty()) null else page + 1
            )
        } catch (exception: Exception) {
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
