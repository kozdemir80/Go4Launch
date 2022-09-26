package com.example.go4launch.api

import com.example.go4launch.constants.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NotificationInstance {

    companion object {
        private val retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(Constants.firebase_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        val api: NotificationApi by lazy {
            retrofit.create(NotificationApi::class.java)
        }
    }
}