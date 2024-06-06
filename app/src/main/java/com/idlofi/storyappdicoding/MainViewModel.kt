package com.idlofi.storyappdicoding

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.idlofi.storyappdicoding.data.StoriesRepository
import com.idlofi.storyappdicoding.di.Injection
import com.idlofi.storyappdicoding.network.StoryResponItem

class MainViewModel(private val storiesRepository: StoriesRepository) : ViewModel() {

    val stories: LiveData<PagingData<StoryResponItem>> =
        storiesRepository.getStories().cachedIn(viewModelScope)

}

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(Injection.provideRepository(context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
