package com.example.galaxystore.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.galaxystore.R
import com.example.galaxystore.databinding.FragmentLoginBinding
import com.example.galaxystore.utils.Utils

class LoginFragment : Fragment() {

    private var _binding:FragmentLoginBinding?=null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
       _binding = FragmentLoginBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        validateNumber()

        binding.btnContinue.setOnClickListener {
            val number = binding.edNumber.text.toString().trim()
            if (number.isEmpty() || number.length!=10) {
                Utils.showToast(requireContext(),"Please enter valid phone number")
            }
            else {
                val bundle = Bundle()
                bundle.putString("number",number)
                findNavController().navigate(R.id.action_loginFragment_to_otpFragment,bundle)
            }
        }

    }



    private fun validateNumber() {
        binding.edNumber.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(number: CharSequence?, p1: Int, p2: Int, p3: Int) {

                val checkNumber = number?.length

                if (checkNumber==10)
                { binding.btnContinue.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.green))}
                   // binding.btnContinue.text = "Ok"
                    // binding.btnContinue.isEnabled=true

                else
                    binding.btnContinue.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.grey))

            }

            override fun afterTextChanged(p0: Editable?) { }
           })
    }


}