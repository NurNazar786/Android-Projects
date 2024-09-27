package com.example.royalstoreonline.retrofit

import com.example.royalstoreonline.api.ApiUrl
import com.example.royalstoreonline.service.ProductService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiController {

    private val retrofit = Retrofit.Builder()
                           .baseUrl(ApiUrl.BASE_URL)
                           .addConverterFactory(GsonConverterFactory.create())
                           .build()

    fun getApi() : ProductService
    {
        return retrofit.create(ProductService::class.java)
    }
}