package com.idlofi.storyappdicoding.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.idlofi.storyappdicoding.data.Result
import com.idlofi.storyappdicoding.data.StoriesRepository
import com.idlofi.storyappdicoding.network.StoryResponItem
import com.idlofi.storyappdicoding.service.GetAllStoryResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsViewModel(private val repository: StoriesRepository) : ViewModel() {
    private val _stories = MutableLiveData<List<StoryResponItem>>()
    val stories: LiveData<List<StoryResponItem>> get() = _stories

    fun getMapStories(token: String) = repository.getMapsStories(token)


    fun fetchStoriesWithLocation(authToken: String) {
        viewModelScope.launch {
            try {
                val myResultMapsStories = MediatorLiveData<Result<GetAllStoryResponse>>()
                val call = repository.getStoriesWithLocation(authToken)
                call.enqueue(object : Callback<GetAllStoryResponse> {
                    override fun onResponse(
                        call: Call<GetAllStoryResponse>,
                        response: Response<GetAllStoryResponse>,
                    ) {
                        if(response.isSuccessful){
                            val storyResponse = response.body()
                            storyResponse?.let {
                                _stories.postValue(it.listStory)
                            }
                        }
                    }

                    override fun onFailure(call: Call<GetAllStoryResponse>, t: Throwable) {
                        myResultMapsStories.value = Result.Error(t.message.toString())

                    }
                })
            } catch (e: Exception) {
                // Handle error
            }
        }
    }


}