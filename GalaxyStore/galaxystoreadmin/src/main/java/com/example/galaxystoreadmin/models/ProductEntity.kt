package com.example.galaxystoreadmin.models

data class ProductEntity(

    val productId : String? = null,
    val productName : String? = null,
    val productPrice : String? = null,
    var productCount : Int? = null,
    val productStock : Int? = null,
    val productImage : String? = null,
    val productCategory : String? = null,
    val adminUid : String? = null,
    val productDiscount : Int?=null
)
