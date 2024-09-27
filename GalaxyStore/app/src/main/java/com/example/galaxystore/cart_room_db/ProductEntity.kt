package com.example.galaxystore.cart_room_db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product")
data class ProductEntity(
    @PrimaryKey
    val productId : String ="random",

    val productName : String? = null,
    val productPrice : String? = null,
    var productCount : Int? = null,
    val productStock : Int? = null,
    val productImage : String? = null,
    val productCategory : String? = null,
    val adminUid : String? = null,
    val productDiscount : Int?=null
)
