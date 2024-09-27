package com.example.galaxystoreadmin.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.denzcoskun.imageslider.models.SlideModel
import com.example.galaxystoreadmin.databinding.ProductItemViewBinding
import com.example.galaxystoreadmin.filter.FilteringProducts
import com.example.galaxystoreadmin.models.Products

class ProductAdapter(val onEditBtnClicked: (Products) -> Unit) : RecyclerView.Adapter<ProductAdapter.ProductViewModel>() ,Filterable{


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

    override fun onBindViewHolder(holder: ProductViewModel, position: Int) {
        val products = differ.currentList[position]

        holder.binding.apply {

            val imageList = ArrayList<SlideModel>()
           val productImages = products.productImages

            for (i in 0 until 1) {
                imageList.add(SlideModel(products.productImages!![i].toString())) }

             imageSlider.setImageList(imageList)
            // productPic.
            //Picasso.get().load(p).into(productPic)
            productName.text  = products.productName
             productPrice.text = "₹"+products.productPrice

            holder.binding.productEdit.setOnClickListener {  onEditBtnClicked(products) }
        }




    }

    private val filter : FilteringProducts? = null
    var productList = ArrayList<Products>()

    override fun getFilter(): Filter {
        if (filter==null) return FilteringProducts(this,productList)
        return filter
    }


}