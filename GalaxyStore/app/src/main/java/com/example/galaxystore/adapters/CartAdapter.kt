package com.example.galaxystore.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.galaxystore.cart_room_db.ProductEntity
import com.example.galaxystore.databinding.CartItemViewBinding
import com.squareup.picasso.Picasso

class CartAdapter(
   val onClickedIncrement: (ProductEntity,CartItemViewBinding) -> Unit,
   val onClickedDecrement: (ProductEntity,CartItemViewBinding) -> Unit,
    val onClickedDelete: (ProductEntity) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(val binding:CartItemViewBinding) : ViewHolder(binding.root)

    private val diffUtil = object : DiffUtil.ItemCallback<ProductEntity>()
    {
        override fun areItemsTheSame(oldItem: ProductEntity, newItem: ProductEntity): Boolean {
            return oldItem.productId == newItem.productId
        }

        override fun areContentsTheSame(oldItem: ProductEntity, newItem: ProductEntity): Boolean {
            return oldItem == newItem
        } }

    var differ = AsyncListDiffer(this,diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        return CartViewHolder(CartItemViewBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {  return differ.currentList.size }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {

        val products = differ.currentList[position]

        holder.binding.apply {
            Picasso.get().load(products.productImage).into(image)
            title.text = products.productName
            val priceDouble = products.productPrice?.substring(1)?.toDouble()
            val quantity = products.productCount!!
            price.text = "₹"+(priceDouble!! * quantity).toString()
            itemCount.text = quantity.toString()
        }

        holder.binding.incrementBtn.setOnClickListener {
            onClickedIncrement(products,holder.binding)
        }
        holder.binding.decrementBtn.setOnClickListener { onClickedDecrement(products,holder.binding) }
        holder.binding.delProduct.setOnClickListener {      onClickedDelete(products) }
    }

//    fun price():Double
//    {
//        var sum = 0.0
//        for (i in differ.currentList.indices)
//        {
//           val p: String = differ.currentList[i].productPrice!!.substring(1)
//            val s= p.toDouble()
//            sum += (differ.currentList[i].productCount!! * s)
//        }
//
//
//        return sum
//    }
}