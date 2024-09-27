package com.example.galaxystoreadmin.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.galaxystoreadmin.databinding.OrderItemViewBinding
import com.example.galaxystoreadmin.models.OrderedProduct
import com.squareup.picasso.Picasso
import kotlin.math.roundToInt

class OrderedProductAdapter(val onItemViewClicked: (OrderedProduct) -> Unit) : RecyclerView.Adapter<OrderedProductAdapter.OrderListViewHolder>() {



    inner class OrderListViewHolder(val binding: OrderItemViewBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffUtil = object : DiffUtil.ItemCallback<OrderedProduct>()
    {
        override fun areItemsTheSame(oldItem: OrderedProduct, newItem: OrderedProduct): Boolean {
            return oldItem.orderId == newItem.orderId
        }

        override fun areContentsTheSame(oldItem: OrderedProduct, newItem: OrderedProduct): Boolean {
            return oldItem == newItem
        } }

    var differ = AsyncListDiffer(this,diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderListViewHolder {
        return OrderListViewHolder(OrderItemViewBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: OrderListViewHolder, position: Int) {

        val products = differ.currentList[position]

        holder.binding.apply {
            Picasso.get().load(products.image).into(image)
            title.text = "Apple iphone 15 Pro Max with 128 Gb Ram And A14 bionic chip"+products.title

            val priceDouble = products.totalPrice?.toDouble()
            val totalWithTax = priceDouble!!*10/100+priceDouble
            val totalPayment = (totalWithTax * 100.0).roundToInt() /100.0

            price.text = "₹"+totalPayment

            orderDate.text = ": "+products.orderDate.toString()

            when(products.orderStatus){

                0->  {  tvOrderDate.text = "Ordered On "
                    tvOrderDate.setTextColor(Color.parseColor("#40D6C6"))}

                1->  {  tvOrderDate.text = "Shipped On "
                    tvOrderDate.setTextColor(Color.parseColor("#FFA500")) }

                2->  {  tvOrderDate.text = "Dispatched On "
                    tvOrderDate.setTextColor(Color.parseColor("#008000")) }

                3->  {  tvOrderDate.text = "Delivered On "
                    tvOrderDate.setTextColor(Color.GRAY) }
            }
        }

        holder.binding.itemView.setOnClickListener {
            onItemViewClicked(products)
        }
    }
}