package com.example.royalstoreonline.response

import com.hishd.tinycart.model.Item
import java.math.BigDecimal

data class Product (
    val created_at: Long,
    val description: String,
    val draft: Int,
    val id: Int,
    val image: String,
    val last_update: Long,
    val name: String,
    var price: Double,
    val price_discount: Int,
    val status: String,
    val stock: Int,
    var quantity:Int,
    var color:String

) : Item {
    override fun getItemPrice(): BigDecimal {
       return BigDecimal(price)
    }

    override fun getItemName(): String {
        return name
    }
}