package com.idlofi.storyappdicoding.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.idlofi.storyappdicoding.data.StoriesRepository
import com.idlofi.storyappdicoding.network.StoryResponItem
import kotlinx.coroutines.launch

class MapsViewModel(private val repository: StoriesRepository) : ViewModel() {
    private val _stories = MutableLiveData<List<StoryResponItem>>()
    val stories: LiveData<List<StoryResponItem>> get() = _stories

    fun fetchStoriesWithLocation(authToken: String) {
        viewModelScope.launch {
            try {
                val response = repository.getStoriesWithLocation(authToken)
                _stories.postValue(response.listStory!!)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

}