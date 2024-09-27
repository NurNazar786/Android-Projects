package com.example.royalstoreonline.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.royalstoreonline.R
import com.example.royalstoreonline.adapter.OrderListAdapter
import com.example.royalstoreonline.databinding.FragmentCheckoutBinding
import com.example.royalstoreonline.order_response.OrderList
import com.example.royalstoreonline.response.Product
import com.example.royalstoreonline.retrofit.ApiController
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.hishd.tinycart.model.Cart
import com.hishd.tinycart.util.TinyCartHelper
import org.json.JSONException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar


class CheckoutFragment : Fragment() {

   private  var _binding:FragmentCheckoutBinding? = null
   private val binding get() = _binding!!

    private lateinit var cartAdapter: OrderListAdapter
    private lateinit var productList: ArrayList<Product>
    private lateinit var cart: Cart
    private var price:Double? =0.0

    private lateinit var name:String
    private lateinit var email:String
    private lateinit var mobile:String
    private lateinit var comments:String
    private lateinit var address:String
    private lateinit var progressBar: ProgressBar
    private lateinit var alertBuilder:AlertDialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
         _binding = FragmentCheckoutBinding.inflate(inflater,container,false)

        productList = ArrayList()
        cart = TinyCartHelper.getCart()

        progressBar = ProgressBar(requireContext())
        return binding.root
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        for (item in cart.allItemsWithQty)
        {
            item.let {
                val product:Product = it.key as Product
                val qnt =it.value
                product.quantity = qnt
                productList.add(product)
            }
        }

        cartAdapter = OrderListAdapter(binding.subtotal,binding.total,binding.qnt,productList)
        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL,false)
        val itemDecoration = DividerItemDecoration(requireContext(),layoutManager.orientation)
        binding.cartList.layoutManager = layoutManager
        binding.cartList.addItemDecoration(itemDecoration)
        binding.cartList.adapter = cartAdapter

        binding.checkoutBtn.setOnClickListener {     validateUserInput()   }

        binding.backArrow.setOnClickListener   {    findNavController().navigate(R.id.action_checkoutFragment_to_cart) }
    }

    @SuppressLint("SetTextI18n")
    private fun validateUserInput() {
        name   = binding.name.text.toString().trim()
        email  = binding.email.text.toString().trim()
        mobile = binding.mobile.text.toString().trim()
        address   = binding.address.text.toString().trim()
       // dateShipping= binding.shipping.text.toString().trim()
        comments= binding.comments.text.toString().trim()

        if (name.isEmpty() || email.isEmpty() || mobile.isEmpty() || mobile.isEmpty() || address.isEmpty())
        {
            binding.error.text = "Please provide the credentials" }

        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            binding.email.error ="Please provide valid email";  binding.email.requestFocus()   }

        else if(mobile.length<=9)
        {
            binding.mobile.error ="Mobile No. length should be greater than 9";  binding.mobile.requestFocus() }

        else  {
            binding.error.text = ""
            binding.linearProgressIndicator.visibility = View.VISIBLE
            orderProcess(name, email, mobile, address,comments) }

    }


    private fun orderProcess(name:String,email:String,mobile:String,address:String,comments:String)
    {
        price = binding.total.text.toString().trim().toDoubleOrNull()
        val jsonObject = JsonObject()
        val productOrder = JsonObject()
        val productOrderDetail = JsonArray()

        val alertDialog = AlertDialog.Builder(requireContext())
        alertDialog.setTitle("Order Successful")
        alertDialog.setCancelable(false)


        try {
            productOrder.addProperty("address",address)
            productOrder.addProperty("buyer",name)
            productOrder.addProperty("comment",comments)
            productOrder.addProperty("created_at",Calendar.getInstance().timeInMillis)
            productOrder.addProperty("date_ship",Calendar.getInstance().timeInMillis)
            productOrder.addProperty("email",email)
            productOrder.addProperty("last_update",Calendar.getInstance().timeInMillis)
            productOrder.addProperty("phone",mobile)
            productOrder.addProperty("serial","cab8c1a4e4421a3b")
            productOrder.addProperty("shipping","")
            productOrder.addProperty("shipping_location", "")
            productOrder.addProperty("shipping_rate", 0.0)
            productOrder.addProperty("status", "WAITING")
            productOrder.addProperty("tax", 11.0)
            productOrder.addProperty("total_fees", price)



            for (item in cart.allItemsWithQty)
            {
                item.let {
                    val product:Product = it.key as Product
                    val qnt = it.value
                    product.quantity = qnt

                    val products = JsonObject()

                    products.addProperty("amount",qnt)
                    products.addProperty("price_item",product.price)
                    products.addProperty("product_id",product.id)
                    products.addProperty("product_name",product.name)
                    productOrderDetail.add(products)
                }

            }

            jsonObject.add("product_order",productOrder)
            jsonObject.add("product_order_detail",productOrderDetail)



        }  catch (e: JSONException) {     e.printStackTrace()   }



        val  call: Call<OrderList> = ApiController.getApi().getOrder(jsonObject)

        call.enqueue(object : Callback<OrderList?> {
            override fun onResponse(call: Call<OrderList?>, response: Response<OrderList?>) {
                val orderList = response.body()

                if (orderList != null) {
                    if (orderList.status == "success")
                    {
                        binding.linearProgressIndicator.visibility = View.INVISIBLE
                        alertDialog.setMessage("Your Order number is : "+orderList.data.code)

                        alertDialog.setPositiveButton("Pay Now")
                        { _, _->
                            val bundle = Bundle()
                            bundle.putString("order",orderList.data.code)
                           findNavController().navigate(R.id.action_checkoutFragment_to_paymentFragment,bundle)
                        }

                        alertDialog.setNegativeButton("Cancel") { _, _->  alertBuilder.dismiss()   }

                         alertBuilder = alertDialog.create()
                        alertBuilder.show() }
                    //Log.d("order",""+orderList.data)
                }
            }

            override fun onFailure(call: Call<OrderList?>, t: Throwable) {
                Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()
            }
        })



    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}