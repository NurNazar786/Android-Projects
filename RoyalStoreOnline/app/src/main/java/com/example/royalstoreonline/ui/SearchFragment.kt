package com.example.royalstoreonline.ui

import android.os.Bundle
import android.util.Log
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
import com.example.royalstoreonline.databinding.FragmentSearchBinding
import com.example.royalstoreonline.repository.ProductRepository
import com.example.royalstoreonline.response.Product
import com.example.royalstoreonline.response_handling.HandleResponse
import com.example.royalstoreonline.retrofit.ApiController
import com.example.royalstoreonline.viewmodel_factory.ProductViewModelFactory
import com.example.royalstoreonline.viewmodels.ProductViewModel
import com.google.gson.Gson

class SearchFragment : Fragment() {

    private var _binding:FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var productViewModel:ProductViewModel
    private lateinit var productAdapter:ProductAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        _binding = FragmentSearchBinding.inflate(inflater,container,false)

        val service = ApiController.getApi()
        val repository = ProductRepository(service)
        productViewModel = ViewModelProvider(this, ProductViewModelFactory(repository))[ProductViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val  name= arguments?.getString("text")

        productViewModel.getSearchedProduct(name!!)
        bindObserveSearchedData()

        binding.searchProductList.layoutManager = StaggeredGridLayoutManager(2,
            StaggeredGridLayoutManager.VERTICAL)
        productAdapter = ProductAdapter(::onItemClicked)
        binding.searchProductList.adapter = productAdapter

        binding.back.setOnClickListener {
            findNavController().navigate(R.id.action_searchFragment_to_homeFragment) }

    }




    private fun bindObserveSearchedData() {
        productViewModel.searchedProductLiveData.observe(viewLifecycleOwner){ response->

            when(response)
            {
                is HandleResponse.Loading -> { }
                is HandleResponse.Error ->   {  Toast.makeText(requireContext(), ""+response.message, Toast.LENGTH_SHORT).show()   }

                is HandleResponse.Success -> {
                    response.data?.let {
                        if (it.count == 0)
                            Toast.makeText(requireContext(), "Sorry, no result found! Please check the spelling ", Toast.LENGTH_SHORT).show()
                        else
                            productAdapter.submitList(it.products) }
                } }
        } }


    private fun onItemClicked(product: Product){
        val bundle = Bundle()
        bundle.putString("products", Gson().toJson(product))
        findNavController().navigate(R.id.action_searchFragment_to_productDetailsFragment,bundle) }



    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}