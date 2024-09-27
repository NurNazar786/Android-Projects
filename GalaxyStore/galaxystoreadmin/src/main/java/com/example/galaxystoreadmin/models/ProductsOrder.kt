package com.example.galaxystoreadmin.models

data class ProductsOrder(
    var orderId: String? = null,
    var orderList: List<ProductEntity>? = null,
    var userAddress: String? = null,
    var orderStatus: Int? = 0,
    var orderDate: String? = null,
    var orderUserId: String? = null,
)
