package com.idlofi.storyappdicoding.adapter

import android.annotation.SuppressLint
import android.app.Activity
import java.util.*
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import androidx.core.util.Pair
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.idlofi.storyappdicoding.R
import com.idlofi.storyappdicoding.databinding.StoryItemCardBinding
import com.idlofi.storyappdicoding.network.StoryResponItem
import com.idlofi.storyappdicoding.ui.stories.DetailStoryActivity

class StoriesAdapter :
    PagingDataAdapter<StoryResponItem, StoriesAdapter.ViewHolder>(DIFF_CALLBACK) {
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        Log.d("StoriesAdapter", "Binding item at position $position: $data")
        if (data != null) {
            holder.bind(data)
        }
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = StoryItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHolder(binding)
    }

    inner class ViewHolder(private val binding: StoryItemCardBinding) :
        RecyclerView.ViewHolder(binding.root){

        fun bind(item: StoryResponItem?) {
            binding.apply {
                binding.tvItemName.text = item?.name
                binding.tvDescription.text = item?.description
                binding.tvCreatedAt.text = item?.createdAt
                Glide.with(binding.ivItemPhoto)
                    .load(item?.photoUrl)
                    .error(R.drawable.ic_launcher_background)
                    .into(binding.ivItemPhoto)

                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, DetailStoryActivity::class.java).apply {
                        putExtra(DetailStoryActivity.EXTRA_NAME, item?.name)
                        putExtra(DetailStoryActivity.EXTRA_IMAGE, item?.photoUrl)
                        putExtra(DetailStoryActivity.EXTRA_DESCRIPTION, item?.description)
                        putExtra(DetailStoryActivity.EXTRA_LONG, item!!.lon)
                        putExtra(DetailStoryActivity.EXTRA_LAT, item!!.lat)
                    }
                    val optionsCompat: ActivityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            itemView.context as Activity,
                            Pair(binding.ivItemPhoto, "image"),
                            Pair(binding.tvItemName, "name"),
                        )
                    Log.d("data : ", item.toString())
                    itemView.context.startActivity(intent, optionsCompat.toBundle())
                }
            }
        }

    }



    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoryResponItem>() {
            override fun areItemsTheSame(oldItem: StoryResponItem, newItem: StoryResponItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: StoryResponItem, newItem: StoryResponItem): Boolean {
                return oldItem.id == newItem.id
            }
    }

//    interface OnAdapterListener {
//        fun onClick(item: StoryResponItem?)
//    }
}
    }
