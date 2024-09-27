package com.example.royalstoreonline.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.royalstoreonline.R
import com.example.royalstoreonline.api.ApiUrl
import com.example.royalstoreonline.databinding.FragmentPaymentBinding

class PaymentFragment : Fragment() {

    private var _binding:FragmentPaymentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
         _binding = FragmentPaymentBinding.inflate(inflater,container,false)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       val orderCode = arguments?.getString("order")

        binding.webView.setMixedContentAllowed(true)
        binding.webView.loadUrl(ApiUrl.PAYMENT_URL+orderCode)

        binding.back.setOnClickListener {
            findNavController().navigate(R.id.action_paymentFragment_to_cart)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}