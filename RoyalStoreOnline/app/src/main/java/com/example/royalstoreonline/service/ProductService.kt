package com.example.royalstoreonline.service

import com.example.royalstoreonline.api.ApiUrl
import com.example.royalstoreonline.order_response.OrderList
import com.example.royalstoreonline.response.CategoriesList
import com.example.royalstoreonline.response.ProductList
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductService {

    @GET(ApiUrl.GET_CATEGORIES_URL)
    suspend fun getCategories():Response<CategoriesList>

    @GET(ApiUrl.GET_PRODUCT_URL)
   suspend fun getAllProduct():Response<ProductList>

   @Headers("Security:secure_code")
   @POST(ApiUrl.ORDER_URL)
    fun getOrder(@Body jsonObject: JsonObject):Call<OrderList>

    @GET(ApiUrl.GET_PRODUCT_URL)
    suspend fun getProduct(@Query("category_id") id:Int):Response<ProductList>

    @GET(ApiUrl.GET_PRODUCT_URL)
    suspend fun getSearchedProduct(@Query("q") name:String):Response<ProductList>

}