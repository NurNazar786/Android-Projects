package com.example.royalstoreonline.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.royalstoreonline.R
import com.example.royalstoreonline.adapter.CartAdapter
import com.example.royalstoreonline.databinding.FragmentCartBinding
import com.example.royalstoreonline.response.Product
import com.hishd.tinycart.model.Cart
import com.hishd.tinycart.util.TinyCartHelper

class CartFragment : Fragment() {

    private var _binding:FragmentCartBinding?=null
    private val binding get() = _binding!!

    private lateinit var cartAdapter: CartAdapter
    private lateinit var productList: ArrayList<Product>
    private lateinit var cart: Cart



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        _binding = FragmentCartBinding.inflate(inflater,container,false)

        productList = ArrayList()
        cart = TinyCartHelper.getCart()



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // cart.allItemsWithQty
        for (item in cart.allItemsWithQty)
        {
            item.let {
                val product:Product = it.key as Product
               val qnt =it.value
               product.quantity = qnt
                productList.add(product)
            }
        }

        cartAdapter = CartAdapter(binding.total,productList)
        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL,false)
        val itemDecoration = DividerItemDecoration(requireContext(),layoutManager.orientation)
        binding.cartRecycle.layoutManager = layoutManager
        binding.cartRecycle.addItemDecoration(itemDecoration)
        binding.cartRecycle.adapter = cartAdapter

        binding.checkout.setOnClickListener {

            if (cart.isCartEmpty){
                Toast.makeText(requireContext(), "Please add some products", Toast.LENGTH_SHORT).show()
            }

            else{
                findNavController().navigate(R.id.action_cart_to_checkoutFragment) }

        }

        binding.back.setOnClickListener {
            findNavController().navigate(R.id.action_cart_to_homeFragment) }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}