package com.example.galaxystore.phone_pay_payment

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiController {

    val retrofit : ApiInterface by lazy {
        Retrofit.Builder()
            .baseUrl("https://api-preprod.phonepe.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)
    }

    val notificationApi : ApiInterface by lazy {
        Retrofit.Builder()
            .baseUrl("https://fcm.googleapis.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)
    }
}