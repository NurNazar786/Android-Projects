package com.example.galaxystore.models

data class Users(
    var uid : String? = null,
    val phoneNumber : String? = null,
    val address : String? = null,
    val name : String?=null,
    var userToken : String?=null,
)
