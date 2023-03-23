package com.example.paypesa.ui.auth

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.paypesa.R
import com.example.paypesa.data.ConstantKey
import com.example.paypesa.data.model.ProfileModel
import com.example.paypesa.data.state.ResultState
import com.example.paypesa.databinding.ActivityProfileSetupBinding
import com.example.paypesa.ui.home.HomeActivity
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileSetup : AppCompatActivity() {

    private lateinit var binding: ActivityProfileSetupBinding
    private lateinit var authViewModel: AuthViewModel

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileSetupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]

        profileStateListener()

        binding.profileSetup.setOnClickListener {
            val profile  = ProfileModel(
                sharedPreferences.getString(ConstantKey.USER_EMAIL, "")!!,
                binding.profileFullName.text.toString(),
                binding.profilePhoneNumber.text.toString(),
                0,
                null
            )
            authViewModel.createProfile(profile)
        }

    }

    private fun navigateToHomeScreen() {
        val intent = Intent(applicationContext, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun profileStateListener() {
        authViewModel.profileState.observe(this) { resultState: ResultState<String> ->
            when(resultState) {
                is ResultState.Failure ->  {
                    showLoader(false)
                    showSnackbar(resultState.message?:"Unkown problem occurred\nTry setting up again")
                }
                ResultState.Loading -> {
                    showLoader(message = "Creating profile")
                }
                is ResultState.Success -> {
                    navigateToHomeScreen()
                }
            }
        }
    }

    private fun showLoader(visible: Boolean = true, message: String = "Loading") {
        if(visible) {
            binding.profileLoader.visibility = View.VISIBLE
            binding.profileLoaderText.text = message
        } else {
            binding.profileLoader.visibility = View.GONE
        }
    }

    private fun showSnackbar(message: String, length: Int = Snackbar.LENGTH_LONG) {
        Snackbar.make(binding.root, message, length).show()
    }
}