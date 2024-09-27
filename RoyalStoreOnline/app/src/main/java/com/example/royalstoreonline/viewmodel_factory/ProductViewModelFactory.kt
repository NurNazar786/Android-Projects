package com.example.royalstoreonline.viewmodel_factory

import android.widget.ViewSwitcher.ViewFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.royalstoreonline.repository.ProductRepository
import com.example.royalstoreonline.viewmodels.ProductViewModel

class ProductViewModelFactory(private val productRepository: ProductRepository):ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProductViewModel(productRepository) as T
    }
}