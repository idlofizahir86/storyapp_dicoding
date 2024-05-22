package com.idlofi.storyappdicoding.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.idlofi.storyappdicoding.R
import com.idlofi.storyappdicoding.model.StoriesModel


class StoriesAdapter(
    private val list: MutableList<StoriesModel>, private val listener: OnAdapterListener
) : RecyclerView.Adapter<StoriesAdapter.ViewHolder>()  {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val name : TextView = view.findViewById(R.id.tv_item_name)
        val photo: ImageView = view.findViewById(R.id.iv_item_photo)
        val description: TextView = view.findViewById(R.id.tv_description)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.story_item_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.name.text = item.name
        holder.description.text = item.description
        Glide.with(holder.photo)
            .load(item.photoUrl)
            .error(R.drawable.ic_launcher_background)
            .into(holder.photo)

        holder.itemView.setOnClickListener {
            listener.onClick(item)

        }
    }

    interface OnAdapterListener{
        fun onClick(list: StoriesModel)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}