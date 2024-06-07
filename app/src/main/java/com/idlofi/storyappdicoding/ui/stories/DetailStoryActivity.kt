package com.idlofi.storyappdicoding.ui.stories

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.idlofi.storyappdicoding.R
import com.idlofi.storyappdicoding.databinding.ActivityDetailStoryBinding

class DetailStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailStoryBinding
    companion object {
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_IMAGE = "extra_image"
        const val EXTRA_DESCRIPTION = "extra_description"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setSupportActionBar(binding.toolbar)

        // Pastikan supportActionBar tidak null sebelum mengaksesnya
        supportActionBar?.let { actionBar ->
            actionBar.title = intent.getStringExtra("extra_name")
        }

        binding.apply {
            ivDetailName.text = intent.getStringExtra("extra_name")
            ivDetailDescription.text = intent.getStringExtra("extra_description")
            Glide.with(this@DetailStoryActivity)
                .load(intent.getStringExtra("extra_image"))
                .error(R.drawable.ic_launcher_background)
                .into(ivDetailPhoto)
        }
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