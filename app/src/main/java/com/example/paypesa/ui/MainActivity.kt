package com.example.paypesa.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.paypesa.R
import com.example.paypesa.data.ConstantKey
import com.example.paypesa.databinding.ActivityMainBinding
import com.example.paypesa.ui.auth.AuthActivity
import com.example.paypesa.ui.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences(ConstantKey.PREFERENCE_NAME, Context.MODE_PRIVATE)
        val userLoggedIn: Boolean = sharedPreferences.getBoolean(ConstantKey.IS_USER_LOGGED_IN, false)

        val dashboardIntent = Intent(this, HomeActivity::class.java)
        val authenticationIntent = Intent(this, AuthActivity::class.java)

        Handler().postDelayed(
            Runnable {
                     if(userLoggedIn) {
                         startActivity(dashboardIntent)
                     } else {
                         startActivity(authenticationIntent)
                     }
            },
            1000
        )
    }
}