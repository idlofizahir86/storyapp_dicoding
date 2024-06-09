package com.idlofi.storyappdicoding.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.idlofi.storyappdicoding.database.StoryDatabase
import com.idlofi.storyappdicoding.network.StoryResponItem
import com.idlofi.storyappdicoding.service.ApiService
import com.idlofi.storyappdicoding.service.GetAllStoryResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoriesRepository(private val storyDatabase: StoryDatabase,
                        private val context: Context,
                        private val apiService: ApiService,){

    private val storyPagingSource = StoryPagingSource(apiService, context)
    val myResultMapsStories = MediatorLiveData<Result<GetAllStoryResponse>>()
    fun getStories() : LiveData<PagingData<StoryResponItem>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService, context),
            pagingSourceFactory = {
              storyPagingSource
//            storyDatabase.storyDao().getAllStory()
            }

        ).liveData
    }
    fun getStoriesWithLocation(authToken: String): Call<GetAllStoryResponse> {
        return apiService.getAllStoriesWithLoc("Bearer $authToken", location = 1)
    }

    fun getMapsStories(token: String): LiveData<Result<GetAllStoryResponse>>{
        myResultMapsStories.value = Result.Loading
        val mapStories = MutableLiveData<GetAllStoryResponse>()
        val client = apiService.getAllStoriesWithLoc("Bearer $token", location = 1)
        client.enqueue(object: Callback<GetAllStoryResponse> {
            override fun onResponse(
                call: Call<GetAllStoryResponse>,
                response: Response<GetAllStoryResponse>,
            ) {
                if(response.isSuccessful){
                    mapStories.value =  response.body()
                }
            }
            override fun onFailure(call: Call<GetAllStoryResponse>, t: Throwable) {
                myResultMapsStories.value = Result.Error(t.message.toString())

            }
        })
        myResultMapsStories.addSource(mapStories){
                list : GetAllStoryResponse ->
            myResultMapsStories.value = Result.Success(list)
        }
        return myResultMapsStories
    }
}
