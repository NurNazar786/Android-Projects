package com.example.galaxystore.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.galaxystore.R
import com.example.galaxystore.adapters.CartAdapter
import com.example.galaxystore.cart_room_db.ProductEntity
import com.example.galaxystore.databinding.CartItemViewBinding
import com.example.galaxystore.databinding.FragmentCartBinding
import com.example.galaxystore.models.Products
import com.example.galaxystore.utils.Utils
import com.example.galaxystore.viewmodels.ProductViewModel
import kotlinx.coroutines.launch


class CartFragment : Fragment() {

    private var _binding : FragmentCartBinding? = null
    private val binding get() = _binding!!
    private val productViewModel: ProductViewModel by viewModels()
    private lateinit var cartAdapter: CartAdapter
    private lateinit var cartProductList : List<ProductEntity>


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        _binding = FragmentCartBinding.inflate(inflater,container,false)
        cartProductList = ArrayList()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getCartProducts()

        binding.checkout.setOnClickListener {
            findNavController().navigate(R.id.action_cartFragment_to_checkoutFragment) }

        binding.back.setOnClickListener { findNavController().navigate(R.id.action_cartFragment_to_homeFragment) }

    }

    @SuppressLint("SetTextI18n")
    private fun getCartProducts() {

        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL,false)
        val itemDecoration = DividerItemDecoration(requireContext(),layoutManager.orientation)
        binding.cartRecycle.layoutManager = layoutManager
        binding.cartRecycle.addItemDecoration(itemDecoration)
        cartAdapter = CartAdapter(::onClickedIncrement,::onClickedDecrement,::onClickedDelete)
        binding.cartRecycle.adapter = cartAdapter

        productViewModel.getProductsFromRoom().observe(viewLifecycleOwner){
           if (it.isNotEmpty())
           {
               binding.checkout.isEnabled = true
           }

            cartProductList =it
            cartAdapter.differ.submitList(it)

            var total = 0.0
            for (product in cartProductList)
            {
                val price = product.productPrice?.substring(1)?.toDouble()
                val itemCount = product.productCount!!
                total += (price?.times(itemCount)!!)
            }
            binding.total.text = "Subtotal : ₹$total"
        }
    }

    private fun onClickedIncrement(productEntity: ProductEntity,binding: CartItemViewBinding) {

          val products = Products()
          products.productId = productEntity.productId

         var itemCount = binding.itemCount.text.toString().toInt()
         itemCount++

         if(itemCount>productEntity.productStock!!) {
             Utils.showToast(requireContext(),"Max stock available "+productEntity.productStock)
         }
         else {
             binding.itemCount.text = itemCount.toString()
             productEntity.productCount = itemCount
             lifecycleScope.launch {
                 productViewModel.insertCartData(productEntity)
                 productViewModel.addItemCount(products,itemCount)
             } }
       }

    private fun onClickedDecrement(productEntity: ProductEntity,binding: CartItemViewBinding) {

        var itemCount = binding.itemCount.text.toString().toInt()
        val products = Products()
        products.productId = productEntity.productId

        if (productEntity.productCount!!>1) {
            itemCount--
            binding.itemCount.text = itemCount.toString()
            productEntity.productCount = itemCount
            lifecycleScope.launch {
                productViewModel.insertCartData(productEntity)
                productViewModel.addItemCount(products,itemCount)
            } }
    }

    private fun onClickedDelete(products: ProductEntity) {
       lifecycleScope.launch { productViewModel.removeCartProduct(products.productId) }
    }
}