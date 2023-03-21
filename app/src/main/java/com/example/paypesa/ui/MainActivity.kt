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
import com.example.paypesa.ui.auth.ProfileSetup
import com.example.paypesa.ui.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userLoggedIn: Boolean = sharedPreferences.getBoolean(ConstantKey.IS_USER_LOGGED_IN, false)
        val userCreatedProfile: Boolean = sharedPreferences.getBoolean(ConstantKey.IS_PROFILE_CREATED, false)

        val dashboardIntent = Intent(this, HomeActivity::class.java)
        val authenticationIntent = Intent(this, AuthActivity::class.java)
        val profileIntent = Intent(this, ProfileSetup::class.java)

        Handler().postDelayed(
            Runnable {
                     if(userLoggedIn && userCreatedProfile) {
                         startActivity(dashboardIntent)
                     } else if (userLoggedIn && !userCreatedProfile) {
                         startActivity(profileIntent)
                     } else {
                         startActivity(authenticationIntent)
                     }
                finish()
            },
            1000
        )
    }
}