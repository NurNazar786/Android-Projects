package com.example.galaxystoreadmin.models

data class Products(
    var productId: String? = null,
    var productName: String? = null,
    var productPrice: Double? = null,
    var productStock: Int? = null,
    var productDiscount: Int? = null,
    var productCategory: String? = null,
    var productDesc:     String? = null,
    var itemCount:Int?=null,
    var adminUid:String?=null,
    var productImages:   ArrayList<String?>? =null,
    )