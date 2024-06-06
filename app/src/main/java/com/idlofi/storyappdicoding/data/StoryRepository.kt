package com.idlofi.storyappdicoding.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.idlofi.storyappdicoding.database.StoryDatabase
import com.idlofi.storyappdicoding.network.StoryResponItem
import com.idlofi.storyappdicoding.service.ApiService
import com.idlofi.storyappdicoding.service.GetAllStoryResponse

class StoriesRepository(private val storyDatabase: StoryDatabase,
                        private  val context: Context,
                        private val apiService: ApiService,){
    fun getStories() : LiveData<PagingData<StoryResponItem>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService, context),
            pagingSourceFactory = {
                StoryPagingSource(apiService, context)
            storyDatabase.storyDao().getAllStory()}

        ).liveData
    }
    suspend fun getStoriesWithLocation(authToken: String): GetAllStoryResponse {
        return apiService.getAllStoriesWithLoc("Bearer $authToken", location = 1)
    }
}
