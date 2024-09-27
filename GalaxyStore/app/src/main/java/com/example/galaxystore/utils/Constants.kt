package com.example.galaxystore.utils

import com.example.galaxystore.R

object Constants {
    const val MERCHANT_ID = "PGTESTPAYUAT"
    const val SALT_KEY = "099eb0cd-02cf-4e2a-8aca-3e6c6aff0399"
    var apiEndPoint = "/pg/v1/pay"
    const val transactionId = "txnId47"

    val categoryName = arrayOf(
        "All",
        "Mobile",
        "Laptop",
        "Smart Watch",
        "Camera",
        "Shoes",
        "Men Fashion",
        "Women Fashion",
        "Trimmers",
        "Headphones")

    val categoryIcon = arrayOf(
        R.drawable.all,
        R.drawable.mobile,
        R.drawable.laptop,
        R.drawable.watch,
        R.drawable.camera,
        R.drawable.shoes,
        R.drawable.men,
        R.drawable.women,
        R.drawable.trimmer,
        R.drawable.headphones)
}