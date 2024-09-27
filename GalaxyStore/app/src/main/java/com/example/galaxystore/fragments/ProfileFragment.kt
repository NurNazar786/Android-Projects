package com.example.galaxystore.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.galaxystore.R
import com.example.galaxystore.activity.MainActivity
import com.example.galaxystore.databinding.FragmentProfileBinding
import com.example.galaxystore.databinding.ShowUserAddressBinding
import com.example.galaxystore.utils.Utils
import com.example.galaxystore.viewmodels.ProductViewModel
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem


class ProfileFragment : Fragment() {

    private var _binding : FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val productViewModel: ProductViewModel by viewModels()
    private lateinit var address:String
    private lateinit var name:String
    private lateinit var account : String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        _binding = FragmentProfileBinding.inflate(inflater,container,false)
        initSlider()
        getProfile()

        binding.orderBtn.setOnClickListener { findNavController().navigate(R.id.action_profileFragment_to_myOrderFragment) }
        binding.addressBtn.setOnClickListener { getAddress() }
        binding.accountBtn.setOnClickListener {  Utils.showToast(requireContext(),account) }
        binding.logoutBtn.setOnClickListener { logoutUser() }
        return binding.root
    }


    @SuppressLint("SetTextI18n")
    private fun getProfile(){
        productViewModel.getUserProfile { user->
            binding.mobile.text = user?.phoneNumber.toString()
            address = user?.address.toString()
            name = user?.name.toString()
            binding.name.text = "Hello, $name"
            account = user?.phoneNumber.toString() }
    }

    @SuppressLint("SetTextI18n")
    private fun getAddress(){

        val name = address.substringBefore(',')         //Name

        val address1 = address.substringAfter(',')     //Area,Colony,CIty,State,Pin,Mobile
        val area = address1.substringBefore(',')      //Area

        val address2 = address1.substringAfter(',')     //Colony,CIty,State,Pin,Mobile
        val colony = address2.substringBefore(',')     //Colony

        val address3 = address2.substringAfter(',')     //CIty,State,Pin,Mobile
        val dist = address3.substringBefore(',')       //CIty

        val address4 = address3.substringAfter(',')     //State,Pin,Mobile
        val states = address4.substringBefore(',')      //State

        val address5 = address4.substringAfter(',')     //Pin,Mobile
        val pin = address5.substringBefore(',')        //Pin

        val adr5 = address5.substringAfter(',')       //Mobile
        val mobile = adr5.substringBefore(',')

        val editAdress = ShowUserAddressBinding.inflate(LayoutInflater.from(requireContext()))

        editAdress.apply {
            fullName.setText(name)
            phoneNumber.setText(mobile)
            pinCode.setText(pin)
            state.setText(states)
            city.setText(dist)
            adr.setText("$area,$colony") }

        val alertDialog = AlertDialog.Builder(requireContext()).setView(editAdress.root).create()
        alertDialog.show()

        editAdress.editBtn.setOnClickListener {
            editAdress.apply {
                fullName.isEnabled     = true
                phoneNumber.isEnabled    = true
                pinCode.isEnabled    = true
                state.isEnabled = true
                city.isEnabled = true
                adr.isEnabled     = true
                saveBtn.isEnabled      = true } }

        editAdress.saveBtn.setOnClickListener {
            val fullName= editAdress.fullName.text.toString().trim()
            val number= editAdress.phoneNumber.text.toString().trim()
            val code= editAdress.pinCode.text.toString().trim()
            val st= editAdress.state.text.toString().trim()
            val city= editAdress.city.text.toString().trim()
            val adr= editAdress.adr.text.toString().trim()

            if (fullName.isEmpty() || number.isEmpty() || code.isEmpty() || st.isEmpty() || city.isEmpty() || adr.isEmpty())
                 {  Utils.showToast(requireContext(),"Please fill all fields")  }

            else {
                val userAddress = "$fullName,$adr,$city,$st,$code,$number"
                productViewModel.addUserAddress(userAddress,name) }

                alertDialog.dismiss() }
    }

    private fun logoutUser(){
        val builder  = AlertDialog.Builder(requireContext())
        val alertDialog = builder.create()

        builder.setIcon(R.drawable.ic_baseline_delete_24)
           .setTitle("Logout")
           .setMessage("Are you sure want to be logout?")

           .setPositiveButton("Yes") { _, _ ->
            productViewModel.logout()
            Utils.showToast(requireContext(),"You have been logout successfully")
            startActivity(Intent(requireContext(),MainActivity::class.java))
            activity?.finish()
        }
        .setNegativeButton("No"){_,_ ->
            alertDialog.dismiss()
        }
            .show()
            .setCancelable(false)
    }

    private fun initSlider() {
        binding.carousel.addData(CarouselItem("https://www.apple.com/in/iphone/buy/images/meta/iphone_buy__chvehwtfgamq_og.jpg?202311160120","Mobile"))
        binding.carousel.addData(CarouselItem("https://cdn.pixabay.com/photo/2017/03/13/17/26/ecommerce-2140603_640.jpg","Some Caption Here"))
        binding.carousel.addData(CarouselItem("https://img.freepik.com/free-vector/gradient-sale-landing-page-template-with-photo_23-2149031860.jpg?size=626&ext=jpg","Sale"))
        binding.carousel.addData(CarouselItem("https://img.freepik.com/premium-photo/online-shopping-concept-3d-rendering_651547-364.jpg","Some Caption Here"))
        binding.carousel.addData(CarouselItem("https://img.freepik.com/premium-vector/gradient-sale-landing-page_52683-70651.jpg?size=626&ext=jpg","Sale"))
    }
}