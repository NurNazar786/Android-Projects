package com.example.galaxystore.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.galaxystore.R
import com.example.galaxystore.adapters.CategoryAdapter
import com.example.galaxystore.adapters.ProductAdapter
import com.example.galaxystore.databinding.FragmentHomeBinding
import com.example.galaxystore.models.Category
import com.example.galaxystore.models.Products
import com.example.galaxystore.utils.Constants
import com.example.galaxystore.viewmodels.ProductViewModel
import com.google.gson.Gson
import kotlinx.coroutines.launch
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem

class HomeFragment : Fragment() {

    private var _binding : FragmentHomeBinding?= null
    private val binding get() = _binding!!

    private val productViewModel: ProductViewModel by viewModels()
    private lateinit var categoryList : ArrayList<Category>
    private lateinit var adapter: ProductAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        _binding = FragmentHomeBinding.inflate(inflater,container,false)
        initSlider()
        getProductList("All")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getCategoryList()
        getProductList("All")
        searchProducts()
    }

    private fun getCategoryList() {
        categoryList = ArrayList()
        for (i in 0 until Constants.categoryIcon.size)
        {
            categoryList.add(Category(Constants.categoryName[i],Constants.categoryIcon[i])) }

        binding.categoriesList.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.HORIZONTAL,false)
        binding.categoriesList.adapter =  CategoryAdapter(categoryList,::onClickedItem)
    }



    private fun getProductList(name: String?) {

        binding.simmerEffect.visibility = View.VISIBLE
        binding.productList.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        adapter = ProductAdapter(::onItemClicked)
        binding.productList.adapter = adapter

        lifecycleScope.launch {
            productViewModel.getAllProduct(name).collect{

                if (it.isEmpty()) {
                    binding.productList.visibility=View.GONE
                    binding.noProducts.visibility = View.VISIBLE }

                else{
                    binding.productList.visibility=View.VISIBLE
                    binding.noProducts.visibility = View.GONE }

                adapter.differ.submitList(it)
                adapter.productList = it as ArrayList<Products>
                binding.simmerEffect.visibility = View.GONE
            } } }

    private fun onClickedItem(category: Category){    getProductList(category.name)   }

    private fun searchProducts() {
        binding.searchBar.addTextChangeListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(search: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val query = search.toString().trim()
                adapter.filter.filter(query) }

            override fun afterTextChanged(p0: Editable?) { }

        })

    }

    private fun onItemClicked(products: Products) {
        val bundle = Bundle()
        bundle.putString("products", Gson().toJson(products))
        findNavController().navigate(R.id.action_homeFragment_to_productFragment,bundle)
          }



    private fun initSlider() {
        binding.carousel.addData(CarouselItem("https://www.apple.com/in/iphone/buy/images/meta/iphone_buy__chvehwtfgamq_og.jpg?202311160120","Mobile"))
        binding.carousel.addData(CarouselItem("https://cdn.pixabay.com/photo/2017/03/13/17/26/ecommerce-2140603_640.jpg","Some Caption Here"))
        binding.carousel.addData(CarouselItem("https://img.freepik.com/free-vector/gradient-sale-landing-page-template-with-photo_23-2149031860.jpg?size=626&ext=jpg","Sale"))
        binding.carousel.addData(CarouselItem("https://img.freepik.com/premium-photo/online-shopping-concept-3d-rendering_651547-364.jpg","Some Caption Here"))
        binding.carousel.addData(CarouselItem("https://img.freepik.com/premium-vector/gradient-sale-landing-page_52683-70651.jpg?size=626&ext=jpg","Sale"))
    }

}