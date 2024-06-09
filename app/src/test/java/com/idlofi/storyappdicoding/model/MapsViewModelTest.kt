package com.idlofi.storyappdicoding.model

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.idlofi.storyappdicoding.DataDummy
import com.idlofi.storyappdicoding.MainDispatcherRule
import com.idlofi.storyappdicoding.data.Result
import com.idlofi.storyappdicoding.data.StoriesRepository
import com.idlofi.storyappdicoding.getOrAwaitValue
import com.idlofi.storyappdicoding.service.GetAllStoryResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MapsViewModelTest{
    private lateinit var mapsViewModel: MapsViewModel

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var storiesRepository: StoriesRepository

    @Before
    fun setUp(){
        mapsViewModel = MapsViewModel(storiesRepository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when Get Stories With Location is called should not null and return success`() = runTest{
        val dummyMaps = DataDummy.generateDummyStoriesWithLoc()
        val expectedMaps = MutableLiveData<Result<GetAllStoryResponse>>()
        expectedMaps.value = Result.Success(dummyMaps)
        Mockito.`when`(storiesRepository.getMapsStories("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9")).thenReturn(expectedMaps)

        val actualMaps = mapsViewModel.getMapStories("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9").getOrAwaitValue()
        assertNotNull(actualMaps)
        assertTrue(actualMaps is Result.Success<*>)
        assertEquals(dummyMaps, (actualMaps as Result.Success<*>).data)
    }
}