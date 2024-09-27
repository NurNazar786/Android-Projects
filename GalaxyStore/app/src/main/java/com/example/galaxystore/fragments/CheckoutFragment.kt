package com.example.galaxystore.fragments

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.galaxystore.R
import com.example.galaxystore.adapters.ProductOrderListAdapter
import com.example.galaxystore.cart_room_db.ProductEntity
import com.example.galaxystore.databinding.AddressDialogBinding
import com.example.galaxystore.databinding.FragmentCheckoutBinding
import com.example.galaxystore.models.ProductsOrder
import com.example.galaxystore.utils.Constants
import com.example.galaxystore.utils.Utils
import com.example.galaxystore.viewmodels.ProductViewModel
import com.phonepe.intent.sdk.api.B2BPGRequest
import com.phonepe.intent.sdk.api.B2BPGRequestBuilder
import com.phonepe.intent.sdk.api.PhonePe
import com.phonepe.intent.sdk.api.PhonePeInitException
import com.phonepe.intent.sdk.api.models.PhonePeEnvironment
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.nio.charset.Charset
import java.security.MessageDigest
import kotlin.math.roundToInt

class CheckoutFragment : Fragment() {

    private var _binding : FragmentCheckoutBinding? = null
    private val binding get() = _binding!!
    private val productViewModel: ProductViewModel by viewModels()
    private lateinit var cartAdapter: ProductOrderListAdapter
    private lateinit var cartProductList : List<ProductEntity>
    private lateinit var b2BPGRequest:B2BPGRequest

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        _binding = FragmentCheckoutBinding.inflate(inflater,container,false)
        cartProductList = ArrayList()
        initializedPhonePe()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val count = binding.totalItems.text.toString().toInt()
        if (count>0){
            binding.checkout.isEnabled = true }

        getCartProducts()
        onPlaceOrder()

        binding.backArrow.setOnClickListener { findNavController().navigate(R.id.action_checkoutFragment_to_cartFragment) }

    }


    @SuppressLint("SetTextI18n")
    private fun getCartProducts() {
        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL,false)
        val itemDecoration = DividerItemDecoration(requireContext(),layoutManager.orientation)
        binding.productOrderList.layoutManager = layoutManager
        binding.productOrderList.addItemDecoration(itemDecoration)
        cartAdapter = ProductOrderListAdapter()
        binding.productOrderList.adapter = cartAdapter

        productViewModel.getProductsFromRoom().observe(viewLifecycleOwner){
           if (it.isNotEmpty()){
               binding.checkout.isEnabled = true
           }

            cartProductList =it
            cartAdapter.differ.submitList(it)

            var total = 0.0
            var itemCount=0
            
            for (product in cartProductList)
            { val price = product.productPrice?.substring(1)?.toDouble()
                itemCount = product.productCount!!
                total += (price?.times(itemCount)!!) }

            val formattedTotal = (total * 100.0).roundToInt() /100.0
            binding.subtotal.text = "Total: ₹$formattedTotal"

            val totalWithTax = total*10/100+total
            val totalPayment = (totalWithTax * 100.0).roundToInt() /100.0
            binding.allTotal.text = "₹$totalPayment"
            binding.total.text = "₹$totalPayment"
            binding.totalItems.text = itemCount.toString()
        }
    }

    private fun onPlaceOrder() {
       binding.checkout.setOnClickListener {
           productViewModel.getAddress().observe(viewLifecycleOwner){

           if(it){ paymentProcess()}
           else{ addUserAddress() } }
       } }



    private fun initializedPhonePe() {
        val data = JSONObject()
        PhonePe.init(requireContext(), PhonePeEnvironment.UAT, Constants.MERCHANT_ID,"")

        data.put("merchantId", Constants.MERCHANT_ID)//String. Mandatory

        data.put("merchantTransactionId" , Constants.transactionId) //String. Mandatory

        data.put("amount", 200 )//Long. Mandatory

        data.put("mobileNumber", "9653756282") //String. Optional

        data.put("callbackUrl", "https://webhook.site/callback-url") //String. Mandatory

        val paymentInstrument = JSONObject()
        paymentInstrument.put("type", "UPI_INTENT")
        paymentInstrument.put("targetApp", "com.phonepe.simulator")

        data.put("paymentInstrument", paymentInstrument )//OBJECT. Mandatory

        val deviceContext = JSONObject()
        deviceContext.put("deviceOS", "ANDROID")
        data.put("deviceContext", deviceContext)

        //val base64Body = kotlin.io.encoding.Base64(Gson().toJson(data))

        val payloadBase64 = Base64.encodeToString(
            data.toString().toByteArray(Charset.defaultCharset()), Base64.NO_WRAP)

        val checksum = sha256(payloadBase64 +Constants.apiEndPoint+ Constants.SALT_KEY) + "###1"
        Log.d("checksum","H:$checksum")

         b2BPGRequest = B2BPGRequestBuilder()
            .setData(payloadBase64)
            .setChecksum(checksum)
            .setUrl(Constants.apiEndPoint)
            .build()


    }

    private fun sha256(input: String): String {
        val bytes = input.toByteArray(Charsets.UTF_8)
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("") { str, it -> str + "%02x".format(it) }
    }


    private fun paymentProcess(){

        try {
            PhonePe.getImplicitIntent(requireContext(),b2BPGRequest,"com.phonepe.simulator")
                .let {
                    phonePeView.launch(it)
                } }

         catch (e:PhonePeInitException){
            Utils.showToast(requireContext(),e.toString()) }
    }

    private val phonePeView = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if (it.resultCode == RESULT_OK){
            checkStatus()
        }
    }

    private fun checkStatus(){
        val xVerify = sha256("/pg/v1/status/${Constants.MERCHANT_ID}/${Constants.MERCHANT_ID}" + Constants.SALT_KEY) + "###1"

        Log.d("hlo","Check: $xVerify")
        val headers = mapOf(
            "Content-Type" to "application/json",
            "X-VERIFY" to xVerify,
            "X-MERCHANT-ID" to Constants.MERCHANT_ID )

        lifecycleScope.launch {
            productViewModel.checkPayment(headers)
            productViewModel.paymentStatus.collect{
                if (it)
                {
                    Utils.showToast(requireContext(), "Something went wrong...")
                }
                else{
                    Utils.showToast(requireContext(), "Payment Successfully done...")
                    saveOrder()
                    lifecycleScope.launch { productViewModel.deleteAllCartProduct() }

                    findNavController().navigate(R.id.action_checkoutFragment_to_cartFragment)

                }
            }
        }


    }



    private fun addUserAddress(){
        val addressDialogBinding = AddressDialogBinding.inflate(LayoutInflater.from(requireContext()))
        val alertDialog = AlertDialog.Builder(requireContext())
            .setView(addressDialogBinding.root)
            .create()
        alertDialog.show()

        addressDialogBinding.saveBtn.setOnClickListener {
            Utils.showToast(requireContext(),"Please wait...")

            val fullName = addressDialogBinding.fullName.text.toString()
            val pinCode = addressDialogBinding.pinCode.text.toString()
            val phoneNumber = addressDialogBinding.phoneNumber.text.toString()
            val state = addressDialogBinding.state.text.toString()
            val city = addressDialogBinding.city.text.toString()
            val adr = addressDialogBinding.adr.text.toString()

            if (fullName.isEmpty() || pinCode.isEmpty() || phoneNumber.isEmpty() || state.isEmpty() || city.isEmpty() || adr.isEmpty())
            { Utils.showToast(requireContext(),"Please fill all the fields") }


            else {
                val userAddress = "$fullName,$adr,$city,$state,$pinCode,$phoneNumber"

                lifecycleScope.launch {
                    productViewModel.addUserAddress(userAddress,fullName)
                    productViewModel.saveUserAddress()
                }
                alertDialog.dismiss()
                Utils.hideDialog()
            }
        }
    }

    
    private fun saveOrder(){

        productViewModel.getProductsFromRoom().observe(viewLifecycleOwner){productList->

           if (productList.isNotEmpty())
           {
               productViewModel.getUserAddress{

                   val order = ProductsOrder(
                       orderId = Utils.getRandomId(),
                       orderList = productList,
                       userAddress = it,
                       orderStatus = 0,
                       orderDate   = Utils.getCurrentDate(),
                       orderUserId = Utils.getCurrentUserId())

                   productViewModel.saveOrderedProduct(order)

                   lifecycleScope.launch {
                       productViewModel.sendNotification(productList[0].adminUid!!,"Ordered","Some Products")
                   }

               }

               for (products in productList){
                   val count = products.productCount
                   val stock = products.productStock?.minus(count!!)

                   if (stock!=null){
                       productViewModel.updateProductStock(stock,products) }
               }
           }

        } }


}