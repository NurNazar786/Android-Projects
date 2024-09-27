package com.example.galaxystore.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.galaxystore.databinding.ItemCategoriesBinding
import com.example.galaxystore.databinding.MenuCategoryItemBinding
import com.example.galaxystore.models.Category

class MenuAdapter(val catArrayList: ArrayList<Category>) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    inner class MenuViewHolder(val binding: MenuCategoryItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        return MenuViewHolder(MenuCategoryItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {  return catArrayList.size}

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {

        val category = catArrayList[position]
        holder.binding.apply {
            categoryIcon.setImageResource(category.icon)
            categoryTitle.text = category.name
        }


    }
}