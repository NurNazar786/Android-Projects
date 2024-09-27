package com.example.royalstoreonline.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.royalstoreonline.R
import com.example.royalstoreonline.adapter.MenuAdapter
import com.example.royalstoreonline.databinding.FragmentMenuBinding
import com.example.royalstoreonline.repository.ProductRepository
import com.example.royalstoreonline.response.Category
import com.example.royalstoreonline.response_handling.HandleResponse
import com.example.royalstoreonline.retrofit.ApiController
import com.example.royalstoreonline.viewmodel_factory.ProductViewModelFactory
import com.example.royalstoreonline.viewmodels.ProductViewModel


class MenuFragment : Fragment() {

    private var _binding : FragmentMenuBinding? = null
    private val binding  get() = _binding!!
    private lateinit var productViewModel : ProductViewModel
    private lateinit var menuAdapter: MenuAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
     {
        _binding = FragmentMenuBinding.inflate(inflater,container,false)

         val productService = ApiController.getApi()
         val repository     = ProductRepository(productService)
         productViewModel   = ViewModelProvider(this,
             ProductViewModelFactory(repository)
         )[ProductViewModel::class.java]

         binding.rvMenu.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
         menuAdapter = MenuAdapter(::onCategoryItemClicked)
         binding.rvMenu.adapter = menuAdapter

         productViewModel.getCategory()
         bindCategoryObserver()

        return binding.root
     }


    private fun bindCategoryObserver() {
        productViewModel.categoriesLiveData.observe(viewLifecycleOwner) { response->
            when(response)
            {
                is HandleResponse.Error -> {
                    //binding.spinKit.visibility = View.INVISIBLE
                    Toast.makeText(requireContext(), "Some Error", Toast.LENGTH_SHORT).show() }

                is HandleResponse.Loading -> {
                   // binding.spinKit.visibility = View.INVISIBLE
                }

                is HandleResponse.Success -> {
                   // binding.spinKit.visibility = View.INVISIBLE
                    response.data?.let {
                        menuAdapter.submitList(it.categories) } }
            } } }


    private fun onCategoryItemClicked(category: Category){

        val bundle = Bundle()
        bundle.putInt("id",category.id)
        bundle.putString("name",category.name)
        findNavController().navigate(R.id.action_menu_to_categoryFragment,bundle)
    }


}