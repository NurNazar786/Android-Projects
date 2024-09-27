package com.example.royalstoreonline.response

data class ProductList(
    val count: Int,
    val count_total: Int,
    val pages: Int,
    val products: List<Product>,
    val status: String
)