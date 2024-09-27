package com.example.royalstoreonline.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.royalstoreonline.R
import com.example.royalstoreonline.adapter.ProductAdapter
import com.example.royalstoreonline.adapter.CategoryAdapter
import com.example.royalstoreonline.databinding.FragmentHomeBinding
import com.example.royalstoreonline.repository.ProductRepository
import com.example.royalstoreonline.response.Category
import com.example.royalstoreonline.response.Product
import com.example.royalstoreonline.response_handling.HandleResponse
import com.example.royalstoreonline.retrofit.ApiController
import com.example.royalstoreonline.viewmodel_factory.ProductViewModelFactory
import com.example.royalstoreonline.viewmodels.ProductViewModel
import com.google.gson.Gson
import com.mancj.materialsearchbar.MaterialSearchBar
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem


class HomeFragment : Fragment() {

   private var _binding:FragmentHomeBinding? = null
   private val binding get() = _binding!!

   private lateinit var adapter: ProductAdapter
   private lateinit var catAdapterCat: CategoryAdapter
   private lateinit var productViewModel: ProductViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        _binding = FragmentHomeBinding.inflate(inflater,container,false)

        val productService = ApiController.getApi()
        val repository     = ProductRepository(productService)
        productViewModel   = ViewModelProvider(this,ProductViewModelFactory(repository))[ProductViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        productViewModel.getAllProducts()
        bindObserver()

        productViewModel.getCategory()
        bindCategoryObserver()

        binding.productList.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        adapter = ProductAdapter(::onItemClicked)
        binding.productList.adapter = adapter

        binding.categoriesList.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        catAdapterCat = CategoryAdapter(::onCategoryItemClicked)
        binding.categoriesList.adapter = catAdapterCat

        initSlider()
        binding.searchBar.setOnSearchActionListener(object : MaterialSearchBar.OnSearchActionListener {
            override fun onSearchStateChanged(enabled: Boolean) {

            }

            override fun onSearchConfirmed(text: CharSequence?) {
                val bundle = Bundle()
                bundle.putString("text",text.toString())
                findNavController().navigate(R.id.action_homeFragment_to_searchFragment,bundle)
            }

            override fun onButtonClicked(buttonCode: Int) {

            }
        })
        super.onViewCreated(view, savedInstanceState)
    }


    private fun bindObserver() {
        binding.simmerEffect.visibility = View.VISIBLE
        productViewModel.productResponseLiveData.observe(viewLifecycleOwner){ response->
            when(response)
            {
                is HandleResponse.Loading -> {      }

                is HandleResponse.Error -> {  binding.something.visibility = View.VISIBLE  }

                is HandleResponse.Success -> {

                    response.data?.let {
                        adapter.submitList(it.products) }
                    binding.simmerEffect.visibility = View.GONE
                    binding.something.visibility = View.GONE
                }

            } } }


    private fun bindCategoryObserver() {
        binding.categorySimmerEffect.visibility = View.VISIBLE
            productViewModel.categoriesLiveData.observe(viewLifecycleOwner) { response->
             when(response)
             {
                 is HandleResponse.Error -> {  binding.something.visibility = View.VISIBLE}

                 is HandleResponse.Loading -> {     }

                 is HandleResponse.Success -> {

                     response.data?.let {
                          catAdapterCat.submitList(it.categories) }
                     binding.categorySimmerEffect.visibility = View.GONE
                     binding.something.visibility = View.GONE
                 }
             } } }



    private fun onItemClicked(product: Product){

          val bundle = Bundle()
          bundle.putString("products",Gson().toJson(product))
          findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment,bundle)
    }

    private fun onCategoryItemClicked(category:Category ){

        val bundle = Bundle()
        bundle.putInt("id",category.id)
        bundle.putString("name",category.name)
        findNavController().navigate(R.id.action_homeFragment_to_categoryFragment,bundle)
    }






    private fun initSlider() {
        binding.carousel.addData(CarouselItem("https://www.apple.com/in/iphone/buy/images/meta/iphone_buy__chvehwtfgamq_og.jpg?202311160120","Mobile"))
        binding.carousel.addData(CarouselItem("https://cdn.pixabay.com/photo/2017/03/13/17/26/ecommerce-2140603_640.jpg","Some Caption Here"))
        binding.carousel.addData(CarouselItem("https://img.freepik.com/free-vector/gradient-sale-landing-page-template-with-photo_23-2149031860.jpg?size=626&ext=jpg","Sale"))
        binding.carousel.addData(CarouselItem("https://img.freepik.com/premium-photo/online-shopping-concept-3d-rendering_651547-364.jpg","Some Caption Here"))
        binding.carousel.addData(CarouselItem("https://img.freepik.com/premium-vector/gradient-sale-landing-page_52683-70651.jpg?size=626&ext=jpg","Sale"))
        }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}