package com.example.galaxystoreadmin.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.galaxystoreadmin.R
import com.example.galaxystoreadmin.activity.MainActivity
import com.example.galaxystoreadmin.databinding.FragmentOtpBinding
import com.example.galaxystoreadmin.models.Admin
import com.example.galaxystoreadmin.utils.Utils
import com.example.galaxystoreadmin.viewmodels.AdminAuthViewModel
import kotlinx.coroutines.launch


class OtpFragment : Fragment() {

     private var _binding : FragmentOtpBinding?=null
    private val binding get() = _binding!!

    private val authViewModel: AdminAuthViewModel by viewModels()
    private lateinit var phoneNumber:String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        _binding = FragmentOtpBinding.inflate(inflater,container,false)

        val bundle = arguments
        phoneNumber = bundle?.getString("number").toString()
        binding.num.text = phoneNumber

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        enterOtp()
        otpSend()
        binding.btnLogin.setOnClickListener {
            val otpArray = arrayOf(binding.otp1,binding.otp2,binding.otp3,binding.otp4,binding.otp5,binding.otp6)
            val otp = otpArray.joinToString(""){ it.text.toString()}

            if (otp.length<otpArray.size) { Utils.showToast(requireContext(),"Please enter valid otp")}
            else
                Utils.showDialog(requireContext(),"Signing you....")
            verifyOtp(otp)
        }



        binding.backToolBar.setOnClickListener { findNavController().navigate(R.id.action_otpFragment_to_signInFragment) }
    }


    private fun enterOtp() {
        val editTexts = arrayOf(binding.otp1,binding.otp2,binding.otp3,binding.otp4,binding.otp5,binding.otp6)

        for (i in editTexts.indices)
        {
            editTexts[i].addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(otp:CharSequence?, p1: Int, p2: Int, p3: Int)  { }

                override fun onTextChanged(otp: CharSequence?, p1: Int, p2: Int, p3: Int)      { }

                override fun afterTextChanged(otp: Editable?) {

                    if (otp?.length==1) {
                        if (i<editTexts.size-1){
                            editTexts[i+1].requestFocus()
                        }
                    }else if (otp?.length==0){

                        if (i>0){ editTexts[i-1].requestFocus() }
                    }
                }

            })
        }
    }

    private fun otpSend() {
        Utils.showDialog(requireContext(),"Sending OTP...")
        authViewModel.apply {
            sendOtp(phoneNumber,requireActivity())
            lifecycleScope.launch {
                otpSent.collect{
                    if (it){
                        Utils.hideDialog()
                        Utils.showToast(requireContext(),"Otp sent to the number...") }
                } } }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun verifyOtp(otp:String){
       // val admin = Admin(Utils.getCurrentUserId(),phoneNumber,null)
        val admin = Admin(
                  uid = null,
                  phoneNumber = phoneNumber,
                  adminToken = null)

        authViewModel.signInWithPhoneAuthCredential(otp,admin)

        lifecycleScope.launch {
            authViewModel.isLoginSuccess.collect{
                if (it)
                { Utils.hideDialog()
                Utils.showToast(requireContext(),"Logged in Success...")
                startActivity(Intent(requireContext(),MainActivity::class.java))
                requireActivity().finish()}
            } }
    }


}