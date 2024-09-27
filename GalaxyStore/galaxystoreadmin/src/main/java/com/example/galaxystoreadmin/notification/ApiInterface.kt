package com.example.galaxystoreadmin.notification

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiInterface {



    @Headers(
        "Content-Type: application/json",
        "Authorization: key=AAAAvovE-Eg:APA91bHOP2bQSGe8zgM2xxpXCUgZSJsdQbAXeYXX-vEN820ybRKlM-p9VxVFLFoze5_4Ywe8nVof3QQZh3KqBWNUJueG6BkbSGOW8D2dKrJsqsJd87zcYBcBMCUq0Tbn2ZriH3uxI0NJ"
    )
    @POST("fcm/send")
    fun sendNotification(@Body notification: Notification) : Call<Notification>


}