package com.example.paypesa.ui.payment

import android.opengl.Visibility
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.paypesa.R
import com.example.paypesa.data.model.ProfileModel
import com.example.paypesa.data.state.ResultState
import com.example.paypesa.databinding.FragmentSendAmountBinding
import com.google.android.material.snackbar.Snackbar


class SendAmountFragment : Fragment() {

    private lateinit var binding: FragmentSendAmountBinding
    private lateinit var navController: NavController

    private val viewModel: SendAmountViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSendAmountBinding.inflate(inflater, container, false)

        navController = findNavController()

        profileRetrieveListener()

        binding.proceed.setOnClickListener {
            viewModel.retrieveProfile(binding.sendPhoneNumber.text.toString())
        }

        return binding.root
    }

    private fun openPaymentReviewFragment(amount: Float, profileModel: ProfileModel) {
        val action = SendAmountFragmentDirections.actionSendAmountFragmentToPaymentReviewFragment(amount, profileModel)
        navController.navigate(action)
    }

    private fun profileRetrieveListener() {
        viewModel.profileRetrieveState.observe(viewLifecycleOwner) { resultState: ResultState<ProfileModel> ->
            when(resultState) {
                is ResultState.Failure -> {
                    showLoader(false)
                    showSnackbar(message = resultState.message?:"Unknown problem occurred")
                }
                ResultState.Loading -> {
                    showLoader(true, "Retrieving profile")
                }
                is ResultState.Success -> {
                    showLoader(false)
                    resultState.data?.let {
                        openPaymentReviewFragment(
                            binding.sendAmount.text.toString().toFloat(),
                            it
                        )
                    }
                }
            }
        }
    }

    private fun showLoader(visible: Boolean = true, message: String = "Loading") {
        if(visible) {
            binding.sendMoneyLoader.visibility = View.VISIBLE
            binding.sendMoneyLoaderText.text = message
        } else {
            binding.sendMoneyLoader.visibility = View.GONE
        }
    }

    private fun showSnackbar(message: String, length: Int = Snackbar.LENGTH_LONG) {
        Snackbar.make(binding.root, message, length).show()
    }
}