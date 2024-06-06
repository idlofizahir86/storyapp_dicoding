package com.idlofi.storyappdicoding.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.idlofi.storyappdicoding.network.StoryResponItem

@Dao
interface StoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(quote: List<StoryResponItem>)

    @Query("SELECT * from story")
    fun getAllStory(): PagingSource<Int, StoryResponItem>

    @Query("DELETE FROM story")
    suspend fun deleteAll()

//    @Query("DELETE FROM story")
//    fun deleteAll()
}