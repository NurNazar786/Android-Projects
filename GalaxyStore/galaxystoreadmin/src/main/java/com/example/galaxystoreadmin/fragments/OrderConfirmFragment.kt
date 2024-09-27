package com.example.galaxystoreadmin.fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.galaxystoreadmin.R
import com.example.galaxystoreadmin.databinding.FragmentOrderConfirmBinding
import com.example.galaxystoreadmin.models.OrderedProduct
import com.example.galaxystoreadmin.utils.Utils
import com.example.galaxystoreadmin.viewmodels.ProductViewModel
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class OrderConfirmFragment : Fragment() {

    private var _binding:FragmentOrderConfirmBinding? = null
    private val binding get() = _binding!!
    private val productViewModel: ProductViewModel by viewModels()
    private var status = 0
    private var currentStatus = 0
    private lateinit var orderId:String
    private var discount = 0
    private var price = 0.0
    private var qnt = 0

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        _binding = FragmentOrderConfirmBinding.inflate(inflater,container,false)
        val str = arguments?.getString("orders")
        val product = Gson().fromJson(str, OrderedProduct::class.java)

        product?.let {

            binding.title.text = it.title
            binding.qnt.text = "Qty: "+it.itemCount.toString()
            binding.price.text = "₹"+it.totalPrice
            binding.orderId.text = it.orderId
            binding.orderDate.text = it.orderDate
            status = it.orderStatus!!
            orderId = it.orderId!!
            discount = it.discount!!
            price = it.totalPrice.toString().toDouble()
            qnt = it.itemCount!!

            val address = it.address
            val name = address?.substringBefore(',')         //Name

            val address1 = address?.substringAfter(',')     //Area,Colony,CIty,State,Pin,Mobile
            val area = address1?.substringBefore(',')      //Area

            val address2 = address1?.substringAfter(',')     //Colony,CIty,State,Pin,Mobile
            val colony = address2?.substringBefore(',')     //Colony

            val address3 = address2?.substringAfter(',')     //CIty,State,Pin,Mobile
            val city = address3?.substringBefore(',')       //CIty

            val address4 = address3?.substringAfter(',')     //State,Pin,Mobile
            val state = address4?.substringBefore(',')      //State

            val address5 = address4?.substringAfter(',')     //Pin,Mobile
            val pin = address5?.substringBefore(',')        //Pin

            val adr5 = address5?.substringAfter(',')       //Mobile
            val mobile = adr5?.substringBefore(',')

            binding.name.text = name
            binding.mobile.text = mobile
            binding.area.text = "$area ,"
            binding.colony.text = "$colony ,"
            binding.city.text = "$city ,"
            binding.state.text = "$state : $pin ,"
            binding.discount.text = discount.toString()

            Picasso.get().load(it.image).into(binding.image)
        }

        setOrderProcess(status)

        orderStatusConfirmed()

        setCostDetails()

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun setOrderProcess(status:Int){
        val statusView = mapOf(
            0 to listOf(binding.status0),
            1 to listOf(binding.status0,binding.status1,binding.view1),
            2 to listOf(binding.status0,binding.status1,binding.view1,binding.status2,binding.view2),
            3 to listOf(binding.status0,binding.status1,binding.view1,binding.status2,binding.view2,binding.status3,binding.view3)
        )

        val viewToTintColor = statusView.getOrDefault(status, emptyList())
        for (view in viewToTintColor){
            view.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.green)
        }


        when(status){

            0->  {  binding.orderStatus.text = "Order Confirmed "
                binding.orderStatus.setTextColor(Color.parseColor("#40D6C6"))}

            1->  {  binding.orderStatus.text = "Shipping Done"
                binding.orderStatus.setTextColor(Color.parseColor("#FFA500")) }

            2->  {  binding.orderStatus.text = "Dispatched Done "
                binding.orderStatus.setTextColor(Color.parseColor("#448AFF")) }

            3->  {  binding.orderStatus.text = "Delivered"
                binding.orderStatus.setTextColor(Color.parseColor("#008000")) }
        }
    }


    private fun orderStatusConfirmed(){
        binding.confirmOrderBtn.setOnClickListener {
            val orderMenu = PopupMenu(requireContext(),it)
            orderMenu.menuInflater.inflate(R.menu.order_confirm_menu,orderMenu.menu)
            orderMenu.show()

            orderMenu.setOnMenuItemClickListener { menu->
                when(menu.itemId){

                    R.id.actionSipped  -> {
                        currentStatus = 1
                        if (currentStatus > status){
                            setOrderProcess(1)
                            productViewModel.updateOrderStatus(orderId,1)
                            lifecycleScope.launch {  productViewModel.sendNotification(orderId,"Shipped","Your Order is Shipped") }
                        }
                        else { Utils.showToast(requireContext(),"Order is already shipped") }
                        true
                    }

                    R.id.actionDispatched ->   {
                        currentStatus = 2
                        if (currentStatus > status){
                            setOrderProcess(2)
                            productViewModel.updateOrderStatus(orderId,2)
                            lifecycleScope.launch {  productViewModel.sendNotification(orderId,"Dispatched","Your Order is Dispatched") }
                           }
                        else { Utils.showToast(requireContext(),"Order is already dispatched") }
                        true }

                    R.id.actionDelivered  ->    {
                        currentStatus = 3
                        if (currentStatus > status){
                            setOrderProcess(3)
                            productViewModel.updateOrderStatus(orderId,3)
                            lifecycleScope.launch {  productViewModel.sendNotification(orderId,"Delivered","Your Order is Delivered") }
                        }
                        true }

                    else                  ->    { false }
                }
            }
        }

    }


    @SuppressLint("SetTextI18n")
    private fun setCostDetails(){
        val total = price*10/100+price
        val totalAmount = (total * 100.0).roundToInt() /100.0 //270.4

        val r:Double = discount/100.toDouble()
        val r2 = 1-r
        val original = price/r2
        val sellPrice = original/qnt
        val formattedPrice = (sellPrice * 100.0).roundToInt() /100.0

        binding.sellingPrice.text = formattedPrice.toString()
        binding.discount.text ="-$discount%"
        binding.afterDiscount.text = "₹$price"
        binding.totalAmount.text = "₹$totalAmount"
    }
}