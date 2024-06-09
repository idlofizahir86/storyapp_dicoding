package com.idlofi.storyappdicoding

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.idlofi.storyappdicoding.adapter.StoriesAdapter
import com.idlofi.storyappdicoding.data.StoriesRepository
import com.idlofi.storyappdicoding.data.StoryPagingSource
import com.idlofi.storyappdicoding.network.StoryResponItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest{
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var storyRepository: StoriesRepository

    @Test
    fun `when Get Story Should Not Null and Return Data`() = runTest {
        val dummyStory = DataDummy.generateDummyAllStories()
        val data: PagingData<StoryResponItem> = StoryPagingSource.snapshot(dummyStory)

        Mockito.`when`(storyRepository.getStories()).thenReturn(MutableLiveData(data))

        val mainViewModel = MainViewModel(storyRepository)
        var actualStory: PagingData<StoryResponItem>? = null
        mainViewModel.stories.observeForever { value ->
            value?.let { actualStory = it }  // Capture the single emitted value
        }

        advanceUntilIdle()  // Wait for the LiveData to emit a value

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoriesAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )

        // Check if actualStory has a value before submitting
        actualStory?.let { differ.submitData(it) }

        assertNotNull(differ.snapshot())
        assertEquals(dummyStory.size, differ.snapshot().size)
        assertEquals(dummyStory[0], differ.snapshot()[0])
    }

    @Test
    fun `when Get Story Empty Should Return No Data`() = runTest {
        val data: PagingData<StoryResponItem> = PagingData.from(emptyList())
        val expectedStory = MutableLiveData<PagingData<StoryResponItem>>()
        expectedStory.value = data
        Mockito.`when`(storyRepository.getStories()).thenReturn(expectedStory)

        val mainViewModel = MainViewModel(storyRepository)
        val actualStory: PagingData<StoryResponItem> = mainViewModel.stories.getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoriesAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualStory)

        assertEquals(0, differ.snapshot().size)
    }
}

class StoryPagingSource : PagingSource<Int, LiveData<List<StoryResponItem>>>() {
    companion object {
        fun snapshot(items: List<StoryResponItem>): PagingData<StoryResponItem> {
            return PagingData.from(items)
        }
    }
    override fun getRefreshKey(state: PagingState<Int, LiveData<List<StoryResponItem>>>): Int {
        return 0
    }
    override suspend fun load(params: PagingSource.LoadParams<Int>): PagingSource.LoadResult<Int, LiveData<List<StoryResponItem>>> {
        return PagingSource.LoadResult.Page(emptyList(), 0, 1)
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}

//@RunWith(MockitoJUnitRunner::class)
//
//class MainViewModelTest {
//
//    private lateinit var viewModel: MainViewModel
//
//    @get:Rule
//    val instantExecutorRule = InstantTaskExecutorRule()
//
//    @OptIn(ExperimentalCoroutinesApi::class)
//    @get:Rule
//    val mainDispatcherRules = MainDispatcherRule()
//
//    @Mock
//    private lateinit var storiesRepository: StoriesRepository
//
//    @Before
//    fun setUp() {
//        viewModel = MainViewModel(storiesRepository)
//
//    }
//
//
//    @OptIn(ExperimentalCoroutinesApi::class)
//    @Test
//    fun `when Get Story Should Not Null and Return Data`() = runTest {
//        val dummyStory = DataDummy.generateDummyAllStoriesWithLoc()
//        val data: PagingData<StoryResponItem> = StoryPagingSource.snapshot(dummyStory)
//        val expectedStory = MutableLiveData<PagingData<StoryResponItem>>()
//
//        Mockito.`when`(storiesRepository.getStories()).thenReturn(expectedStory)
//
//        val mainViewModel = MainViewModel(storiesRepository)
//        expectedStory.value = data  // Set the value before accessing with getOrAwaitValue
//
//        val actualStory: PagingData<StoryResponItem> = mainViewModel.stories.getOrAwaitValue()
//
//        val differ = AsyncPagingDataDiffer(
//            diffCallback = StoriesAdapter.DIFF_CALLBACK,
//            updateCallback = noopListUpdateCallback,
//            workerDispatcher = Dispatchers.Main,
//        )
//        differ.submitData(actualStory)
//
//        Assert.assertNotNull(differ.snapshot())
//        Assert.assertEquals(dummyStory.size, differ.snapshot().size)
//        Assert.assertEquals(dummyStory[0], differ.snapshot()[0])
//    }
//
//}
//
//
//val noopListUpdateCallback = object : ListUpdateCallback {
//    override fun onInserted(position: Int, count: Int) {}
//    override fun onRemoved(position: Int, count: Int) {}
//    override fun onMoved(fromPosition: Int, toPosition: Int) {}
//    override fun onChanged(position: Int, count: Int, payload: Any?) {}
//}