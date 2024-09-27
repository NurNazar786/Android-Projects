package com.example.galaxystoreadmin.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.galaxystoreadmin.R
import com.example.galaxystoreadmin.activity.MainActivity
import com.example.galaxystoreadmin.databinding.FragmentSplashBinding
import com.example.galaxystoreadmin.viewmodels.AdminAuthViewModel
import kotlinx.coroutines.launch


class SplashFragment : Fragment() {

    private var _binding:FragmentSplashBinding?=null
    private val binding get() = _binding!!
    private val authViewModel: AdminAuthViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View{

        _binding = FragmentSplashBinding.inflate(inflater,container,false)

        Handler(Looper.getMainLooper()).postDelayed({

            lifecycleScope.launch {
                authViewModel.isLoggedInUser.collect {
                    if (it) {
                        startActivity(Intent(requireContext(),MainActivity::class.java))
                        requireActivity().finish() }
                    else
                        findNavController().navigate(R.id.action_splashFragment_to_signInFragment)

                } }
        },3000)
        return binding.root
    }


}