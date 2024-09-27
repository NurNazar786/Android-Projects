package com.example.galaxystore.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.galaxystore.databinding.ProductItemViewBinding
import com.example.galaxystore.filter.FilteringProducts
import com.example.galaxystore.models.Products
import com.squareup.picasso.Picasso
import kotlin.reflect.KFunction1

class ProductAdapter(val onItemClicked: KFunction1<Products, Unit>) : RecyclerView.Adapter<ProductAdapter.ProductViewModel>() ,Filterable{


    private lateinit var img:String
    inner class ProductViewModel(val binding: ProductItemViewBinding) : ViewHolder(binding.root)

    private val diffUtil = object : DiffUtil.ItemCallback<Products>() {
        override fun areItemsTheSame(oldItem: Products, newItem: Products): Boolean {
            return oldItem.productId == newItem.productId
        }

        override fun areContentsTheSame(oldItem: Products, newItem: Products): Boolean {
            return oldItem == newItem
        } }

    val differ = AsyncListDiffer(this,diffUtil)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewModel {
        return ProductViewModel(ProductItemViewBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int { return differ.currentList.size }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ProductViewModel, position: Int) {
        val products = differ.currentList[position]

        holder.binding.apply {

            for (i in 0 until 1) {

                 img = products.productImages!![i].toString() }

            Picasso.get().load(img).into(productPic)
            productName.text  = products.productName
             productPrice.text = "₹"+products.productPrice
        }

        holder.binding.itemView.setOnClickListener { onItemClicked(products) }

    }

    private val filter : FilteringProducts? = null
    var productList = ArrayList<Products>()

    override fun getFilter(): Filter {
        if (filter==null) return FilteringProducts(this,productList)
        return filter
    }


}