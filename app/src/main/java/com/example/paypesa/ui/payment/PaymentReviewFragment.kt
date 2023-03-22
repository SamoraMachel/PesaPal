package com.example.paypesa.ui.payment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavArgs
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.paypesa.R
import com.example.paypesa.databinding.FragmentPaymentReviewBinding
import com.google.android.material.snackbar.Snackbar


class PaymentReviewFragment : Fragment() {

    private lateinit var binding: FragmentPaymentReviewBinding
    private lateinit var navController: NavController

    private val navArgs: PaymentReviewFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPaymentReviewBinding.inflate(inflater, container, false)

        val recieverProfile = navArgs.profile

        binding.recieverName.text = recieverProfile.name
        binding.recieverNumber.text = recieverProfile.phoneNumber
        binding.amountToSend.text = navArgs.amount.toString()

        binding.sendMoneyBtn.setOnClickListener {
            openDashboardFragment()
        }

        navController = findNavController()

        return binding.root
    }

    private fun openDashboardFragment() {
        navController.navigate(R.id.dashboardFragment)
    }

    private fun showLoader(visible: Boolean = true, message: String = "Loading") {
//        if(visible) {
//            binding.sendMoneyLoader.visibility = View.VISIBLE
//            binding.sendMoneyLoaderText.text = message
//        } else {
//            binding.sendMoneyLoader.visibility = View.GONE
//        }
    }

    private fun showSnackbar(message: String, length: Int = Snackbar.LENGTH_LONG) {
        Snackbar.make(binding.root, message, length).show()
    }
}