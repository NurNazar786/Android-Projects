package com.example.galaxystoreadmin.models



data class OrderedProduct(
    val image : String? = null,
    val title : String? = null,
    val orderId: String? = null,
    val orderStatus: Int? = null,
    val orderDate: String? = null,
    val totalPrice : String? = null,
    val itemCount : Int? = null,
    val address : String? = null,
    val discount : Int?=null
)