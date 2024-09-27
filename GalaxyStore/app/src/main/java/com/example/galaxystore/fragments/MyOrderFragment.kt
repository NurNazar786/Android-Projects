package com.example.galaxystore.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.galaxystore.R
import com.example.galaxystore.adapters.OrderedProductAdapter
import com.example.galaxystore.databinding.FragmentMyOrderBinding
import com.example.galaxystore.models.OrderedProduct
import com.example.galaxystore.viewmodels.ProductViewModel
import com.google.gson.Gson
import kotlinx.coroutines.launch


class MyOrderFragment : Fragment() {

    private var _binding : FragmentMyOrderBinding? =null
    private val binding get() = _binding!!
    private val productViewModel: ProductViewModel by viewModels()
    private lateinit var arrayList: ArrayList<OrderedProduct>
    private lateinit var orderedAdapter: OrderedProductAdapter



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMyOrderBinding.inflate(inflater,container,false)


        arrayList = ArrayList()
        getOrderedList()

        binding.back.setOnClickListener { findNavController().navigate(R.id.action_myOrderFragment_to_homeFragment) }
        return binding.root
    }

    private fun getOrderedList() {
        binding.rvOrderedList.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL,false)
        orderedAdapter = OrderedProductAdapter(::onItemViewClicked)
        binding.rvOrderedList.adapter = orderedAdapter

        lifecycleScope.launch {
            productViewModel.getAllOrders().collect{


               for (order in it)
               {
                   for (item in order.orderList!!){

                       val priceDouble = item.productPrice?.substring(1)?.toDouble()
                       val quantity = item.productCount!!
                       val price = (priceDouble!! * quantity)


                       val ordered = OrderedProduct(item.productImage!!,item.productName!!,order.orderId,order.orderStatus,order.orderDate,price.toString(),quantity,order.userAddress,item.productDiscount)
                       arrayList.add(ordered)
                   }
               }
                orderedAdapter.differ.submitList(arrayList)

            } }
    }


    private fun onItemViewClicked(items : OrderedProduct){
        val bundle = Bundle()
        bundle.putString("orders", Gson().toJson(items))
        findNavController().navigate(R.id.action_myOrderFragment_to_orderDetailsFragment,bundle)
    }


}