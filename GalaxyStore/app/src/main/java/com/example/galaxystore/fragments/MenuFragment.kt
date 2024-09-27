package com.example.galaxystore.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.galaxystore.adapters.MenuAdapter
import com.example.galaxystore.databinding.FragmentMenuBinding
import com.example.galaxystore.models.Category
import com.example.galaxystore.utils.Constants


class MenuFragment : Fragment() {

    private var _binding: FragmentMenuBinding?=null
    private val binding get() = _binding!!
    private lateinit var categoryList : ArrayList<Category>


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
       _binding = FragmentMenuBinding.inflate(inflater,container,false)
        getCategoryList()
        return binding.root
    }

    private fun getCategoryList() {
        categoryList = ArrayList()
        for (i in 0 until Constants.categoryIcon.size)
        {
            categoryList.add(Category(Constants.categoryName[i], Constants.categoryIcon[i])) }

        binding.rvMenu.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        binding.rvMenu.adapter =  MenuAdapter(categoryList)
    }

    //  binding.rvMenu.layoutManager = LinearLayoutManager(requireContext(),
    //            LinearLayoutManager.HORIZONTAL,false)


}