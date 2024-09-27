package com.example.galaxystoreadmin.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.galaxystoreadmin.R
import com.example.galaxystoreadmin.adapters.OrderedProductAdapter
import com.example.galaxystoreadmin.databinding.FragmentOrderBinding
import com.example.galaxystoreadmin.models.OrderedProduct
import com.example.galaxystoreadmin.viewmodels.ProductViewModel
import com.google.gson.Gson
import kotlinx.coroutines.launch


class OrderFragment : Fragment() {

    private var _binding : FragmentOrderBinding? =null
    private val binding get() = _binding!!
    private val productViewModel: ProductViewModel by viewModels()
    private lateinit var arrayList: ArrayList<OrderedProduct>
    private lateinit var orderedAdapter: OrderedProductAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
       _binding = FragmentOrderBinding.inflate(inflater,container,false)

        arrayList = ArrayList()
        getOrderedList()

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
                        val price = (priceDouble!! * quantity).toString()

                        val ordered = OrderedProduct(item.productImage!!,item.productName,order.orderId,order.orderStatus,order.orderDate,price,quantity,order.userAddress,item.productDiscount)
                        arrayList.add(ordered)
                    } }

                orderedAdapter.differ.submitList(arrayList)

            }
        }
    }


    private fun onItemViewClicked(items : OrderedProduct){
        val bundle = Bundle()
        bundle.putString("orders", Gson().toJson(items))
        findNavController().navigate(R.id.action_order_to_orderConfirmFragment,bundle)

    }


}