package com.idlofi.storyappdicoding.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.idlofi.storyappdicoding.data.StoriesRepository

class ViewModelFactory(private val repository: StoriesRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MapsViewModel::class.java)) {
            return MapsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}