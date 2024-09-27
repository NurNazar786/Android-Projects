package com.example.galaxystore.fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.denzcoskun.imageslider.models.SlideModel
import com.example.galaxystore.R
import com.example.galaxystore.cart_room_db.ProductEntity
import com.example.galaxystore.databinding.FragmentProductBinding
import com.example.galaxystore.models.Products
import com.example.galaxystore.viewmodels.ProductViewModel
import com.google.gson.Gson
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class ProductFragment : Fragment() {

    private var _binding : FragmentProductBinding? = null
    private val binding get() = _binding!!
    private val productViewModel: ProductViewModel by viewModels()

    private lateinit var productId:String
    private lateinit var productName:String
    private lateinit var productCategory:String
    private lateinit var adminUid:String
    private var productStock:Int? = null
    private var productPrice = 0.0
    private var productDiscount:Int = 0
    private var productCount:Int = 1
    private var productDiscountPrice = 0.0

    private lateinit var productDesc:String
    private var productImages:   ArrayList<String?>? =null
    private var price:Double = 0.0


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
       _binding = FragmentProductBinding.inflate(inflater,container,false)
        return binding.root
    }


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val str = arguments?.getString("products")
       val product = Gson().fromJson(str,Products::class.java)

        product?.let {

            productId = it.productId!!
            productName = it.productName!!
            productPrice = it.productPrice!!
            productDiscount = it.productDiscount!!
            productDesc = it.productDesc!!
            productImages = it.productImages
            productCategory = it.productCategory!!
            adminUid = it.adminUid!!
            productStock = it.productStock!!


            val priceDiscount = productPrice * productDiscount/100
            price = productPrice - priceDiscount
             productDiscountPrice = (price * 100.0).roundToInt() /100.0

            binding.firstPrice.paintFlags = binding.firstPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

            binding.name.text = productName
            binding.discount.text = "-$productDiscount%"
            binding.firstPrice.text = "$productPrice"
            binding.price.text = "₹$productDiscountPrice"
            binding.desc.text  = productDesc

            if (productStock==0){
                binding.stock.text = "Out Of Stock"
                binding.stock.setTextColor(Color.parseColor("#DD2C00")) }

        }

          val imageList = ArrayList<SlideModel>()

        for (i in 0 until productImages?.size!!) {

             imageList.add(SlideModel(product.productImages!![i].toString()))
        }

        binding.imageSlider.setImageList(imageList)

        binding.addToCart.setOnClickListener {

            if (productStock!!>0){
                saveProductInRoom(product)
                binding.addToCart.isEnabled=false
                binding.addToCart.text = "Added in Cart"
            } }

        binding.back.setOnClickListener { findNavController().navigate(R.id.action_productFragment_to_homeFragment) }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun saveProductInRoom(products:Products) {

      val  cartProduct = ProductEntity(productId,productName, "₹$productDiscountPrice",productCount,productStock, productImages?.get(0)!!,productCategory,adminUid,productDiscount)
        lifecycleScope.launch {
            productViewModel.insertCartData(cartProduct)
            productViewModel.addItemCount(products,productCount) }
    }


}