package com.example.galaxystore.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.galaxystore.R
import com.example.galaxystore.activity.MainActivity
import com.example.galaxystore.databinding.FragmentSplashBinding
import com.example.galaxystore.viewmodels.AuthViewModel
import kotlinx.coroutines.launch


@Suppress("DEPRECATION")
class SplashFragment : Fragment() {

    private var _binding:FragmentSplashBinding?=null
    private val binding get() = _binding!!

    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        _binding = FragmentSplashBinding.inflate(inflater,container,false)

        Handler().postDelayed({

           lifecycleScope.launch {
               authViewModel.isLoggedInUser.collect {
                  if (it) {
                      startActivity(Intent(requireContext(),MainActivity::class.java))
                      requireActivity().finish() }
                   else
                   findNavController().navigate(R.id.action_splashFragment_to_loginFragment)

              } }

        },3000)
        return binding.root
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}