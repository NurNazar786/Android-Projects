package com.example.galaxystoreadmin.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.galaxystoreadmin.R
import com.example.galaxystoreadmin.databinding.FragmentSignInBinding
import com.example.galaxystoreadmin.utils.Utils

class SignInFragment : Fragment() {

    private var _binding :FragmentSignInBinding?=null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
       _binding = FragmentSignInBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getUserNumber()

        binding.btnContinue.setOnClickListener {
            val number = binding.edNumber.text.toString().trim()
            if (number.isEmpty() || number.length!=10) {
                Utils.showToast(requireContext(),"Please enter valid phone number")
            }
            else {
                val bundle = Bundle()
                bundle.putString("number",number)
                findNavController().navigate(R.id.action_signInFragment_to_otpFragment,bundle)
            }
        }
    }

    private fun getUserNumber() {
        binding.edNumber.addTextChangedListener(object : TextWatcher{

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(number: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val numberLength = number?.length
                if (numberLength==10)
                {
                    binding.btnContinue.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.orange))
                }
                else
                    binding.btnContinue.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.grey))
               }

            override fun afterTextChanged(p0: Editable?) {}
        })

    }


}