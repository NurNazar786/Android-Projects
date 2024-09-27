package com.example.galaxystoreadmin.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.galaxystoreadmin.databinding.ItemViewImageSelectionBinding

class ProductImagesAdapter(private val imageUri: ArrayList<Uri>) : RecyclerView.Adapter<ProductImagesAdapter.ProductImagesViewHolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductImagesViewHolder {

        return ProductImagesViewHolder(ItemViewImageSelectionBinding.inflate(LayoutInflater.from(parent.context),parent,false)) }

    override fun getItemCount(): Int {
        return imageUri.size
    }

    override fun onBindViewHolder(holder: ProductImagesViewHolder, position: Int) {

        val image = imageUri[position]
        holder.binding.apply {
            ivImage.setImageURI(image) }

        holder.binding.closeBtn.setOnClickListener {

            if (position < imageUri.size)
                imageUri.removeAt(position)
                notifyItemRemoved(position)}
    }

     inner class ProductImagesViewHolder(val binding:ItemViewImageSelectionBinding) : ViewHolder(binding.root)

}