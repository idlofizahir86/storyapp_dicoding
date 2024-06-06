package com.idlofi.storyappdicoding

import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.idlofi.storyappdicoding.adapter.LoadingStateAdapter
import com.idlofi.storyappdicoding.adapter.StoriesAdapter
import com.idlofi.storyappdicoding.databinding.ActivityMainBinding
import com.idlofi.storyappdicoding.ui.MapsActivity
import com.idlofi.storyappdicoding.ui.stories.AddStoryActivity
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val storiesAdapter: StoriesAdapter by lazy { StoriesAdapter() }
//    private lateinit var storiesAdapter: StoriesAdapter
    private val mainViewModel: MainViewModel by viewModels {
        ViewModelFactory(this)
    }
    companion object {
        const val SUCCESS_UPLOAD_STORY = "Story berhasil diupload"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        setupRecyclerView()
//        setupObservers()

        binding.fabAdd.setOnClickListener {
            val addStoryIntent = Intent(this@MainActivity, AddStoryActivity::class.java)
            startActivity(addStoryIntent)
        }
    }



    private fun setupObservers() {
        lifecycleScope.launch {
            mainViewModel.stories.observe(this@MainActivity) { pagingData ->
                Log.d("MainActivity", "Received new data: ${pagingData}")
                storiesAdapter.submitData(lifecycle, pagingData)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_item, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.btn_logout -> {
                // Implement logout functionality
            }
            R.id.btn_maps -> {
                val mapsIntent = Intent(this@MainActivity, MapsActivity::class.java)
                startActivity(mapsIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
    private fun setupRecyclerView() {
        val adapter = StoriesAdapter()
        binding.rvStories.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter{
                adapter.retry()
            }
        )

        mainViewModel.stories.observe(this) {
            adapter.submitData(lifecycle, it)
        }
    }

    }



