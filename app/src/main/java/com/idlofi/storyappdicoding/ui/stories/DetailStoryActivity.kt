package com.idlofi.storyappdicoding.ui.stories

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.idlofi.storyappdicoding.R
import com.idlofi.storyappdicoding.databinding.ActivityDetailStoryBinding

class DetailStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailStoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Pastikan supportActionBar tidak null sebelum mengaksesnya
        supportActionBar?.let { actionBar ->
            actionBar.title = intent.getStringExtra("list_name")
        }

        binding.ivDetailName.text = intent.getStringExtra("list_name")
        Glide.with(this)
            .load(intent.getStringExtra("list_image"))
            .error(R.drawable.ic_launcher_background)
            .into(binding.ivDetailPhoto)
        binding.ivDetailDescription.text = intent.getStringExtra("list_description")
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}