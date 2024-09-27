package com.example.royalstoreonline.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.royalstoreonline.api.ApiUrl
import com.example.royalstoreonline.databinding.ProductViewBinding
import com.example.royalstoreonline.response.Product

class ProductAdapter(private val onItemClicked:(Product)->Unit) : ListAdapter<Product,ProductAdapter.MyViewHolder>(CompareDiffUtil()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
       val binding = ProductViewBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
       val product = getItem(position)
       product?.let {
           holder.bindData(it)
       }
    }

    inner class MyViewHolder(private val binding: ProductViewBinding) : ViewHolder(binding.root)
    {
        @SuppressLint("SetTextI18n")
        fun bindData(product: Product) {

            Glide.with(binding.productPic.context).load(ApiUrl.PRODUCT_IMAGE_URL+product.image).into(binding.productPic)
            binding.productName.text = product.name
            binding.productPrice.text = "INR "+product.price

            binding.productPic.setOnClickListener{
                onItemClicked(product)
            }

        }
    }

}


    class CompareDiffUtil : DiffUtil.ItemCallback<Product>()
    {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }

    }
