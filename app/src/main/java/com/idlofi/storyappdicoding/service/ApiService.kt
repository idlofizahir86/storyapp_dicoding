package com.idlofi.storyappdicoding.service

import com.google.gson.annotations.SerializedName
import com.idlofi.storyappdicoding.network.StoryResponItem
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

data class RegisterResponse(

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String

)
data class LoginResponse(

    @field:SerializedName("loginResult")
    val loginResult: LoginResult,

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)

data class GetAllStoryResponse(

    @SerializedName("error")
    val error: Boolean? = null,
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("listStory")
    val listStory: ArrayList<StoryResponItem>? = null,
    @Header("Authorization")
    val authHeader: String? = null,

//    @field:SerializedName("listStory")
//    val listStory: MutableList<StoriesModel>,
//
//    @field:SerializedName("error")
//    val error: Boolean,
//
//    @field:SerializedName("message")
//    val message: String
)

data class AddNewStoryResponse(

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)

interface ApiService{
    @POST("v1/register")
    @FormUrlEncoded fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegisterResponse>

    @POST("v1/login")
    @FormUrlEncoded fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @GET("v1/stories")
    fun getAllStories(
        @Header("Authorization") Bearer: String
    ): Call<GetAllStoryResponse>

    @Multipart
    @POST("v1/stories")
    fun uploadStories(
        @Header("Authorization") Bearer: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody
    ): Call<AddNewStoryResponse>

    @Multipart
    @POST("v1/stories")
    fun uploadStoriesWithLoc(
        @Header("Authorization") Bearer: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: Float,
        @Part("lon") lon: Float
    ): Call<AddNewStoryResponse>

    @JvmSuppressWildcards
    @GET("v1/stories")
    suspend fun getAllStoriesWithPaging(
        @Header("Authorization") Barier: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): List<StoryResponItem>

    @GET("v1/stories")
    suspend fun getAllStoriesWithLoc(
        @Header("Authorization") Bearer: String,
        @Query("location") location: Int? = 1,
    ): GetAllStoryResponse

}
//class ApiConfig{
//
//}
