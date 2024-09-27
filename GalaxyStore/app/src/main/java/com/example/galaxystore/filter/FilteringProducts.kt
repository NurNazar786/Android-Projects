package com.example.galaxystore.filter

import android.widget.Filter
import com.example.galaxystore.adapters.ProductAdapter
import com.example.galaxystore.models.Products
import java.util.Locale

class FilteringProducts(private val productAdapter: ProductAdapter, private val productList:ArrayList<Products>) : Filter() {

    override fun performFiltering(s: CharSequence?): FilterResults {
        val result = FilterResults()

        if (!s.isNullOrEmpty()){
            val filteredList = ArrayList<Products>()
            val query = s.toString().trim().uppercase(Locale.getDefault()).split(" ")

            for (products in productList) {
                if (query.any {

                    products.productName?.uppercase(Locale.getDefault())?.contains(it) == true ||
                            products.productCategory?.uppercase(Locale.getDefault())?.contains(it)==true ||
                            products.productPrice?.toString()?.uppercase(Locale.getDefault())?.contains(it)==true }) {

                    filteredList.add(products) }
            }

            result.values = filteredList
            result.count = filteredList.size }

        else{
            result.values = productList
            result.count = productList.size }

        return result
    }

    override fun publishResults(p0: CharSequence?, results: FilterResults?) {

        productAdapter.differ.submitList(results?.values as ArrayList<Products>)
    }


}