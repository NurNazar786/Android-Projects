package com.example.royalstoreonline.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.royalstoreonline.api.ApiUrl
import com.example.royalstoreonline.databinding.MenuCategoryItemBinding
import com.example.royalstoreonline.response.Category

class MenuAdapter(val onCategoryItemClicked: (Category) -> Unit) : ListAdapter<Category,MenuAdapter.MenuViewHolder>(DiffUtils()){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuAdapter.MenuViewHolder {
        val view = MenuCategoryItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MenuViewHolder(view)
    }

    override fun onBindViewHolder(holder: MenuAdapter.MenuViewHolder, position: Int) {

        val category = getItem(position)
        category?.let {
            holder.dataBind(it)
        }
    }


    inner class MenuViewHolder(private val binding: MenuCategoryItemBinding): RecyclerView.ViewHolder(binding.root)
    {
        fun dataBind(category: Category)
        {
            Glide.with(binding.categoryIcon.context).load(ApiUrl.CATEGORIES_IMAGE_URL+category.icon).into(binding.categoryIcon)
            binding.categoryTitle.text = category.name


            binding.itemView.setOnClickListener{
                onCategoryItemClicked(category)
            }
        }
    }

}



class DiffUtils : DiffUtil.ItemCallback<Category>()
{
    override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem == newItem
    }

}