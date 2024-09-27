package com.example.royalstoreonline.ui

import android.os.Bundle
import android.text.Html
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.royalstoreonline.R
import com.example.royalstoreonline.databinding.FragmentProfileBinding
import com.example.royalstoreonline.order_response.OrderList
import com.example.royalstoreonline.repository.ProductRepository
import com.example.royalstoreonline.response.Product
import com.example.royalstoreonline.response.ProductList
import com.example.royalstoreonline.retrofit.ApiController
import com.example.royalstoreonline.viewmodel_factory.ProductViewModelFactory
import com.example.royalstoreonline.viewmodels.ProductViewModel
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException


@Suppress("DEPRECATION")
class ProfileFragment : Fragment() {

    private var _binding:FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var productViewModel: ProductViewModel

    private lateinit var arrayList: ArrayList<Product>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
      {
         _binding = FragmentProfileBinding.inflate(inflater,container,false)

          initSlider()

        return binding.root
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