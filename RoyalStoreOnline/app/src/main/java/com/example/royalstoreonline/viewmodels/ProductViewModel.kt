package com.example.royalstoreonline.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.royalstoreonline.repository.ProductRepository
import com.example.royalstoreonline.response.CategoriesList
import com.example.royalstoreonline.response.ProductList
import com.example.royalstoreonline.response_handling.HandleResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductViewModel(val productRepository: ProductRepository):ViewModel(){

   // private val _productResponseLiveData = MutableLiveData<ProductList>()

    val productResponseLiveData: LiveData<HandleResponse<ProductList>>
        get() = productRepository.productResponseLiveData

    val categoriesLiveData :LiveData<HandleResponse<CategoriesList>>
            get() = productRepository.categoriesLiveData


    fun getAllProducts()
    {
        viewModelScope.launch(Dispatchers.IO) {
            productRepository.getAllProduct()
        }
    }

    fun getCategory()
    {
        viewModelScope.launch(Dispatchers.IO) {
            productRepository.getCategories()
        }
    }


    val productLiveData:LiveData<HandleResponse<ProductList>>
        get() = productRepository.productLiveData
    fun getProductWithCategoryId(id:Int)
    {
        viewModelScope.launch(Dispatchers.IO) {
            productRepository.getProduct(id)
        }
    }


   val searchedProductLiveData : LiveData<HandleResponse<ProductList>>
   get() = productRepository.searchedProductLiveData
    fun getSearchedProduct(name:String)
    {
        viewModelScope.launch(Dispatchers.IO) {
            productRepository.getSearchedProduct(name)
        }
    }


}