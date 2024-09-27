package com.example.galaxystore.phone_pay_payment

import com.example.galaxystore.notifications.Notification
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiInterface {

    @GET("apis/pg-sandbox/pg/v1/status/{merchantId}/{transactionId}")
    suspend fun checkStatus(
        @HeaderMap headers: Map<String, String>,
        @Path("merchantId") merchantId: String,
        @Path("transactionId") transactionId: String
        ): Response<PhonePeResponse>


    @Headers(
        "Content-Type: application/json",
        "Authorization: key=AAAAvovE-Eg:APA91bHOP2bQSGe8zgM2xxpXCUgZSJsdQbAXeYXX-vEN820ybRKlM-p9VxVFLFoze5_4Ywe8nVof3QQZh3KqBWNUJueG6BkbSGOW8D2dKrJsqsJd87zcYBcBMCUq0Tbn2ZriH3uxI0NJ"
    )
    @POST("fcm/send")
    fun sendNotification(@Body notification: Notification) : Call<Notification>


}