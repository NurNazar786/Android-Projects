package com.example.galaxystoreadmin.notification

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiController {

    val notificationApi : ApiInterface by lazy {
        Retrofit.Builder()
            .baseUrl("https://fcm.googleapis.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)
        }
}