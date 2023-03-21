package com.example.paypesa.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.paypesa.R
import com.example.paypesa.databinding.ActivityProfileSetupBinding
import com.example.paypesa.ui.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileSetup : AppCompatActivity() {

    private lateinit var binding: ActivityProfileSetupBinding
    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileSetupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]

        binding.profileSetup.setOnClickListener {
            navigateToHomeScreen()
        }

    }

    private fun navigateToHomeScreen() {
        val intent = Intent(applicationContext, HomeActivity::class.java)
        startActivity(intent)
    }
}