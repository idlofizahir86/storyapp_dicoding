package com.idlofi.storyappdicoding.di

import android.content.Context
import com.idlofi.storyappdicoding.data.StoriesRepository
import com.idlofi.storyappdicoding.database.StoryDatabase
import com.idlofi.storyappdicoding.service.ApiConfig

object Injection {
    fun provideRepository(context: Context): StoriesRepository {
        val database = StoryDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiService()
        return StoriesRepository(database, context, apiService )
    }
}