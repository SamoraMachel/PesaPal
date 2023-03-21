package com.example.paypesa.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.paypesa.data.model.AuthModel
import com.example.paypesa.data.state.ResultState
import com.example.paypesa.databinding.ActivityAuthBinding
import com.example.paypesa.ui.home.HomeActivity
import com.google.android.material.snackbar.Snackbar
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

        loginListener()
        registrationListener()

        binding.authLogin.setOnClickListener {
            val authModel = AuthModel(
                binding.authEmail.text.toString(),
                binding.authPassword.text.toString()
            )
            authViewModel.login(authModel)
        }

        binding.authSignup.setOnClickListener {
            val authModel = AuthModel(
                binding.authEmail.text.toString(),
                binding.authPassword.text.toString()
            )
            authViewModel.register(authModel)
        }
    }

    private fun navigateToProfileSetup() {
        val intent = Intent(applicationContext, ProfileSetup::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToHomeScreen() {
        val intent = Intent(applicationContext, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun registrationListener() {
        authViewModel.authResult.observe(this) { resultState: ResultState<Boolean> ->
            when(resultState) {
                is ResultState.Failure -> {
                    showLoadingFrame(false)
                    showSnackbar(resultState.message?:"Unknown error occurred")
                }
                ResultState.Loading -> {
                    showLoadingFrame(message = "Registering User")
                }
                is ResultState.Success -> {
                    if(resultState.data == true)
                        navigateToProfileSetup()
                    else
                        showSnackbar("Could not register user\nTry registering again")
                }
            }
        }
    }

    private fun loginListener() {
        authViewModel.loginState.observe(this) { resultState: ResultState<Boolean> ->
            when(resultState) {
                is ResultState.Failure -> {
                    showLoadingFrame(false)
                    showSnackbar(resultState.message?:"Unknown error occurred")
                }
                ResultState.Loading -> {
                    showLoadingFrame(message = "Logging In")
                }
                is ResultState.Success -> {
                    if(resultState.data == true)
                        navigateToHomeScreen()
                    else
                        showSnackbar("Could not login user\nTry logging in again")
                }
            }
        }
    }

    private fun showLoadingFrame(visible: Boolean = true, message: String = "Loading..") {
        if(visible) {
            binding.authLoader.visibility = View.VISIBLE
            binding.authLoaderText.text = message
        } else {
            binding.authLoader.visibility = View.GONE
        }
    }

    private fun showSnackbar(message: String, length: Int = Snackbar.LENGTH_LONG) {
        Snackbar.make(binding.root, message, length).show()
    }
}