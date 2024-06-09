package com.idlofi.storyappdicoding.service

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object{
        fun getApiService(): ApiService{
            val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl("https://story-api.dicoding.dev/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(ApiService::class.java)
        }

//        fun getApiService(context: Context): ApiService {
//
//            val sharedPref = SharedPreferenceHelper(context)
//            val token = sharedPref.getUserToken()
//
//            val retrofit = Retrofit.Builder()
//                .baseUrl("https://story-api.dicoding.dev/v1/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .client(getInterceptor(token))
//                .build()
//            return retrofit.create(ApiService::class.java)
//        }
    }
}