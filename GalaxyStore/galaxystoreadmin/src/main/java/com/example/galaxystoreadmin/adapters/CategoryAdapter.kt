package com.example.galaxystoreadmin.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.galaxystoreadmin.databinding.ItemCategoriesBinding
import com.example.galaxystoreadmin.models.Category

class CategoryAdapter(val catArrayList: ArrayList<Category>,val  onClickedItem: (Category) -> Unit) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {



    inner class CategoryViewHolder(val binding:ItemCategoriesBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(ItemCategoriesBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {  return catArrayList.size}

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {

        val category = catArrayList[position]
        holder.binding.apply {
            image.setImageResource(category.icon)
            name.text = category.name
        }

        holder.binding.image.setOnClickListener { onClickedItem(category) }
    }
}