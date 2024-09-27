package com.example.galaxystore.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.galaxystore.cart_room_db.ProductEntity
import com.example.galaxystore.databinding.OrderItemListBinding
import com.squareup.picasso.Picasso

class ProductOrderListAdapter : RecyclerView.Adapter<ProductOrderListAdapter.OrderListViewHolder>() {

    inner class OrderListViewHolder(val binding: OrderItemListBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffUtil = object : DiffUtil.ItemCallback<ProductEntity>()
    {
        override fun areItemsTheSame(oldItem: ProductEntity, newItem: ProductEntity): Boolean {
            return oldItem.productId == newItem.productId
        }

        override fun areContentsTheSame(oldItem: ProductEntity, newItem: ProductEntity): Boolean {
            return oldItem == newItem
        } }

    var differ = AsyncListDiffer(this,diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderListViewHolder {
        return OrderListViewHolder(OrderItemListBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: OrderListViewHolder, position: Int) {

        val products = differ.currentList[position]

        holder.binding.apply {
            Picasso.get().load(products.productImage).into(image)
            title.text = products.productName
            price.text = products.productPrice.toString()
            itemCount.text = products.productCount.toString()
        }
    }
}