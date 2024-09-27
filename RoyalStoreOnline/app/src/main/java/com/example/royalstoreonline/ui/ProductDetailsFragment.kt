package com.example.royalstoreonline.ui

import android.annotation.SuppressLint
import android.graphics.Paint
import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.royalstoreonline.R
import com.example.royalstoreonline.api.ApiUrl
import com.example.royalstoreonline.databinding.FragmentProductDetailsBinding
import com.example.royalstoreonline.response.Product
import com.google.gson.Gson
import com.hishd.tinycart.model.Cart
import com.hishd.tinycart.util.TinyCartHelper
import kotlin.math.roundToInt


class ProductDetailsFragment : Fragment() {

    private var _binding:FragmentProductDetailsBinding?=null
    private val binding get() = _binding!!

    private var product:Product? = null
    private var pId:Int = 0
    private var firstPrice = 0.0
    private var price:Double = 0.0
    private var discount:Int = 0
    private lateinit var name:String
    private lateinit var image:String
    private lateinit var desc:String
    private lateinit var cart:Cart
    private lateinit var radioButton: RadioButton
    private lateinit var colors:String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        _binding = FragmentProductDetailsBinding.inflate(inflater,container,false)

        cart = TinyCartHelper.getCart()
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        colors=binding.color.text.toString()

        val str = arguments?.getString("products")
        this.product = Gson().fromJson(str,Product::class.java)

        product?.let {

            pId = it.id
            name = it.name
            image = it.image
            firstPrice = it.price
            discount = it.price_discount
            desc = it.description



            val priceDiscount = firstPrice * discount/100
            price = firstPrice - priceDiscount


            binding.name.text = name

            val formattedPrice = (price * 100.0).roundToInt() /100.0

            product!!.price = formattedPrice
            product!!.quantity = 0

            binding.price.text = "INR $formattedPrice"

            binding.discount.text = "-$discount%"
            binding.desc.text  = Html.fromHtml(desc)
            binding.firstPrice.paintFlags = binding.firstPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            binding.firstPrice.text = "$firstPrice"
        }

       // binding.name.text = ""+product!!.price
        Glide.with(this).load(ApiUrl.PRODUCT_IMAGE_URL+product!!.image).into(binding.productPic)



        binding.radioGroupBtn.setOnCheckedChangeListener { _, id ->
            radioButton = binding.root.findViewById(id)
            colors = radioButton.text.toString()
            binding.color.text = colors
            product!!.color=colors
        }
        product!!.color=colors



        binding.addToCart.setOnClickListener{

            cart.addItem(product,1)

            binding.addToCart.isEnabled=false
            binding.addToCart.text = "Added in Cart"
          }




        binding.back.setOnClickListener {
           findNavController().navigate(R.id.action_productDetailsFragment_to_homeFragment)

          }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding =null
    }
}