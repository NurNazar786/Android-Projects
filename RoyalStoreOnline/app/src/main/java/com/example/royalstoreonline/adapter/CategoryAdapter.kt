package com.example.royalstoreonline.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.royalstoreonline.api.ApiUrl
import com.example.royalstoreonline.databinding.ItemCategoriesBinding
import com.example.royalstoreonline.response.Category

class CategoryAdapter(private val onCategoryItemClicked:(Category)->Unit) : ListAdapter<Category,CategoryAdapter.MyHolder>(CompareDiffUtils()) {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryAdapter.MyHolder {
        val view = ItemCategoriesBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryAdapter.MyHolder, position: Int) {

        val category = getItem(position)
        category?.let {
            holder.dataBind(it)
        }
    }


    inner class MyHolder(private val binding: ItemCategoriesBinding):ViewHolder(binding.root)
    {
        fun dataBind(category: Category)
        {
            Glide.with(binding.image.context).load(ApiUrl.CATEGORIES_IMAGE_URL+category.icon).into(binding.image)
            binding.label.text = category.name


            binding.image.setOnClickListener{
                onCategoryItemClicked(category)
            }
        }
    }

}



class CompareDiffUtils : DiffUtil.ItemCallback<Category>()
{
    override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem == newItem
    }

}