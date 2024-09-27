package com.example.royalstoreonline.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.royalstoreonline.response.CategoriesList
import com.example.royalstoreonline.response.ProductList
import com.example.royalstoreonline.response_handling.HandleResponse
import com.example.royalstoreonline.service.ProductService
import java.io.IOException

class ProductRepository(private val productService: ProductService) {

    private val _productResponseLiveData = MutableLiveData<HandleResponse<ProductList>>()

    val productResponseLiveData: LiveData<HandleResponse<ProductList>>
    get() = _productResponseLiveData

    suspend fun getAllProduct()
    {
        _productResponseLiveData.postValue(HandleResponse.Loading())
        try {
            val response = productService.getAllProduct()
            if (response.isSuccessful && response.body()!=null)
              {
                _productResponseLiveData.postValue(HandleResponse.Success(response.body()!!))
              }
            else
              {
                _productResponseLiveData.postValue(HandleResponse.Error(response.message()))
              }
            }
        catch (e:IOException)
        {
            e.printStackTrace()
            _productResponseLiveData.postValue(HandleResponse.Error("Something went wrong...."))
        }
    }


    private val _categoriesLiveData = MutableLiveData<HandleResponse<CategoriesList>>()

    val categoriesLiveData : LiveData<HandleResponse<CategoriesList>>
        get() = _categoriesLiveData

    suspend fun getCategories()
    {
        _categoriesLiveData.postValue(HandleResponse.Loading())
        try {
            val response = productService.getCategories()
            if (response.isSuccessful && response.body()!=null) {
                _categoriesLiveData.postValue(HandleResponse.Success(response.body()!!))
               }
            else
                _categoriesLiveData.postValue(HandleResponse.Error(response.message()))
           }

        catch (e:IOException) {
            e.printStackTrace()
            _categoriesLiveData.postValue(HandleResponse.Error("Something went wrong...."))
           }
    }

    private val _productLiveData = MutableLiveData<HandleResponse<ProductList>>()
    val productLiveData :LiveData<HandleResponse<ProductList>>
    get() = _productLiveData

    suspend fun getProduct(id:Int)
    {
        _productLiveData.postValue(HandleResponse.Loading())
        try {
             val response= productService.getProduct(id)
            if (response.isSuccessful && response.body()!=null)
            {
                _productLiveData.postValue(HandleResponse.Success(response.body()!!)) }

            else{
                _productLiveData.postValue(HandleResponse.Error("Some Error")) }

        }
        catch (e:IOException) {
            _productLiveData.postValue(HandleResponse.Error("Something went wrong"))
            e.printStackTrace() }
    }


    private val _searchedProductLiveData = MutableLiveData<HandleResponse<ProductList>>()
    val searchedProductLiveData : LiveData<HandleResponse<ProductList>>
    get() = _searchedProductLiveData

    suspend fun getSearchedProduct(name:String)
    {
        _searchedProductLiveData.postValue(HandleResponse.Loading())
        try {
          val response = productService.getSearchedProduct(name)
            if (response.isSuccessful && response.body()!=null) {
                _searchedProductLiveData.postValue(HandleResponse.Success(response.body()!!)) }
            else
                _searchedProductLiveData.postValue(HandleResponse.Error("Some error"))
        }
        catch (e:IOException) {
            _searchedProductLiveData.postValue(HandleResponse.Error("Something went wrong"))
            e.printStackTrace()
        }
    }

}