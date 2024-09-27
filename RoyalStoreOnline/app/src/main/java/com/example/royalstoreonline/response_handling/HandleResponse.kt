package com.example.royalstoreonline.response_handling

sealed class HandleResponse<T>(val data:T?=null,val message:String?=null)
{
    class Loading<T>:HandleResponse<T>()

    class Success<T>(data:T):HandleResponse<T>(data)

    class Error<T>(message: String,data:T?=null):HandleResponse<T>(data,message)
}