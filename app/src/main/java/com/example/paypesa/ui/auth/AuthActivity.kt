package com.example.paypesa.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.paypesa.databinding.ActivityAuthBinding
import com.example.paypesa.ui.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding
    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]

        binding.authLogin.setOnClickListener {
            navigateToHomeScreen()
        }
    }

    private fun navigateToHomeScreen() {
        val intent = Intent(applicationContext, HomeActivity::class.java)
        startActivity(intent)
    }
}