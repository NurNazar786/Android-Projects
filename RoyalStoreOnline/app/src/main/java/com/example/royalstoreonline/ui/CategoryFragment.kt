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
import com.example.royalstoreonline.adapter.ProductAdapter
import com.example.royalstoreonline.databinding.FragmentCategoryBinding
import com.example.royalstoreonline.repository.ProductRepository
import com.example.royalstoreonline.response.Product
import com.example.royalstoreonline.response_handling.HandleResponse
import com.example.royalstoreonline.retrofit.ApiController
import com.example.royalstoreonline.viewmodel_factory.ProductViewModelFactory
import com.example.royalstoreonline.viewmodels.ProductViewModel
import com.google.gson.Gson


class CategoryFragment : Fragment() {

    private var _binding:FragmentCategoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var productViewModel: ProductViewModel
    private lateinit var productAdapter: ProductAdapter
    private var id:Int? =null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        _binding = FragmentCategoryBinding.inflate(inflater,container,false)

        val productService = ApiController.getApi()
        val repo = ProductRepository(productService)
        productViewModel = ViewModelProvider(this,ProductViewModelFactory(repo))[ProductViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val name = arguments?.getString("name")
        binding.text.text = name
         id = arguments?.getInt("id")

            productViewModel.getProductWithCategoryId(id!!)
        bindProductObserver()

        binding.catProductList.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        productAdapter = ProductAdapter(::onItemClicked)
        binding.catProductList.adapter = productAdapter


        binding.back.setOnClickListener {
            findNavController().navigate(R.id.action_categoryFragment_to_homeFragment) }
    }


    private fun bindProductObserver() {

        binding.simmerEffect.visibility = View.VISIBLE
        productViewModel.productLiveData.observe(viewLifecycleOwner) { response->
            when(response)
            {
                is HandleResponse.Loading -> {  }

                is HandleResponse.Success -> {

                    response.data?.let {
                        productAdapter.submitList(it.products) }
                    binding.simmerEffect.visibility = View.GONE
                    binding.something.visibility = View.GONE
                }

                is HandleResponse.Error -> {   binding.something.visibility = View.VISIBLE}
            }
        } }


    private fun onItemClicked(product: Product){
        val bundle = Bundle()
        bundle.putString("products", Gson().toJson(product))
        findNavController().navigate(R.id.action_categoryFragment_to_productDetailsFragment,bundle) }



    override fun onDestroy() {
        super.onDestroy()
        _binding =null
    }
}