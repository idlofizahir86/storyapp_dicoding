package com.idlofi.storyappdicoding.network

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


@Entity(tableName = "story")
data class StoryResponItem(

    @PrimaryKey
    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("createdAt")
    val createdAt: String? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("lat")
    val lat: Double? = null,

    @field:SerializedName("lon")
    val lon: Double? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("photoUrl")
    val photoUrl: String? = null,
)