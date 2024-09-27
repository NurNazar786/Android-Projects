package com.example.royalstoreonline.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.royalstoreonline.api.ApiUrl
import com.example.royalstoreonline.databinding.ProductOrderListBinding
import com.example.royalstoreonline.response.Product
import kotlin.math.roundToInt

class OrderListAdapter(private val price: TextView,private val total: TextView,private val qnt: TextView, private var productList:List<Product>) : RecyclerView.Adapter<OrderListAdapter.CartViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ProductOrderListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        updatePrice()
        updateTotal()
        return CartViewHolder(binding)
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.binding.pName.text = productList[position].name
        holder.binding.price.text     = ""+productList[position].price
        holder.binding.quantity.text  = ""+productList[position].quantity
        holder.binding.color.text  = productList[position].color
        Glide.with(holder.binding.image.context).load(ApiUrl.PRODUCT_IMAGE_URL+productList[position].image).into(holder.binding.image)


        holder.binding.incrBtn.setOnClickListener {
            var qnt = productList[position].quantity
            qnt++

            if (qnt>productList[position].stock) {
                Toast.makeText(holder.binding.incrBtn.context, "Max stock available: "+productList[position].stock, Toast.LENGTH_SHORT).show() }

            else {
                productList[position].quantity = qnt
                notifyDataSetChanged()
                updatePrice()
                updateTotal()
                updateQnt() }
         }


        holder.binding.decrBtn.setOnClickListener {
            var qnt = productList[position].quantity

            if (productList[position].quantity>1) {
                qnt--
                productList[position].quantity = qnt
                notifyDataSetChanged()
                updatePrice()
                updateTotal()
                updateQnt()}
        }

    }

    override fun getItemCount(): Int {   return productList.size  }



    inner class CartViewHolder(val binding: ProductOrderListBinding): RecyclerView.ViewHolder(binding.root)


    @SuppressLint("SetTextI18n")
    private fun updatePrice()
    {
        var sum = 0.0
        for (i in productList.indices)
            sum += (productList[i].quantity * productList[i].price)
        val total = (sum * 100.0).roundToInt() /100.0
        price.text = "INR : $total"

    }


    @SuppressLint("SetTextI18n")
    private fun updateTotal() {
        var sum = 0.0
        for (i in productList.indices)
            sum += (productList[i].quantity * productList[i].price)
        val subtotal =sum*11/100+sum
        val totalPayment = (subtotal * 100.0).roundToInt() /100.0
        total.text= "$totalPayment"   }


    @SuppressLint("SetTextI18n")
    private fun updateQnt() {

        var quantity = 0
        for (i in productList.indices)
            quantity += productList[i].quantity
            qnt.text = "$quantity"   }
}
