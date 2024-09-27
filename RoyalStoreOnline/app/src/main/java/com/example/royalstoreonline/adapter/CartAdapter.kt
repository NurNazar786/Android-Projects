package com.example.royalstoreonline.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.royalstoreonline.api.ApiUrl
import com.example.royalstoreonline.databinding.CartItemBinding
import com.example.royalstoreonline.response.Product
import com.hishd.tinycart.model.Cart
import com.hishd.tinycart.util.TinyCartHelper
import kotlin.math.roundToInt

class CartAdapter(private val price:TextView, data:List<Product>) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

       private var  productList = data as MutableList<Product>
    val product:Product?=null


     private var cart:Cart = TinyCartHelper.getCart()

       override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = CartItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        updatePrice()
        return CartViewHolder(binding)
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
       holder.binding.pName.text = productList[position].name
       holder.binding.price.text     = ""+productList[position].price
       holder.binding.color.text  = productList[position].color
       holder.binding.quantity.text = " "+productList[position].quantity
        Glide.with(holder.binding.image.context).load(ApiUrl.PRODUCT_IMAGE_URL+productList[position].image).into(holder.binding.image)

        holder.binding.incrBtn.setOnClickListener {
            var qnt = productList[position].quantity
            qnt++

            if (qnt>productList[position].stock) {
                Toast.makeText(holder.binding.incrBtn.context, "Max stock available: "+productList[position].stock, Toast.LENGTH_SHORT).show() }

            else {
                  productList[position].quantity = qnt
                  notifyDataSetChanged()
                  updatePrice() }
            productList[position].quantity=qnt
            notifyDataSetChanged()
           }


        holder.binding.decrBtn.setOnClickListener {
            var qnt = productList[position].quantity

            if (productList[position].quantity>1) {
                qnt--
                productList[position].quantity = qnt
                notifyDataSetChanged()
                updatePrice() }
           }

        holder.binding.delProduct.setOnClickListener {
            deleteItem(position)
            Toast.makeText(holder.binding.delProduct.context, "Product Deleted", Toast.LENGTH_SHORT).show()
        }

    }

    override fun getItemCount(): Int {   return productList.size  }



    inner class CartViewHolder(val binding: CartItemBinding):ViewHolder(binding.root)


    @SuppressLint("SetTextI18n")
    private fun updatePrice()
    {
        var sum = 0.0
        for (i in productList.indices)
            sum += (productList[i].quantity * productList[i].price)
        val total = (sum * 100.0).roundToInt() /100.0
        price.text = "Subtotal INR : $total"

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun deleteItem(index:Int)
    {
        productList.removeAt(index)
        cart.clearCart()
        notifyDataSetChanged()
        updatePrice()
    }


}