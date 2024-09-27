package com.example.galaxystore.models

import com.example.galaxystore.cart_room_db.ProductEntity

data class ProductsOrder(
    var orderId: String? = null,
    var orderList: List<ProductEntity>? = null,
    var userAddress: String? = null,
    var orderStatus: Int? = 0,
    var orderDate: String? = null,
    var orderUserId: String? = null,
)
