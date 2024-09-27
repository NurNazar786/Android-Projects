package com.example.galaxystoreadmin.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.galaxystoreadmin.R
import com.example.galaxystoreadmin.databinding.ActivityAdminAuthBinding
import com.example.galaxystoreadmin.databinding.ActivityMainBinding

class AdminAuthActivity : AppCompatActivity() {

    private lateinit var binding:ActivityAdminAuthBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}