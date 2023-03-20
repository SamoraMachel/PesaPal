package com.example.paypesa.ui.payment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.paypesa.R
import com.example.paypesa.databinding.FragmentPaymentReviewBinding


class PaymentReviewFragment : Fragment() {

    private lateinit var binding: FragmentPaymentReviewBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPaymentReviewBinding.inflate(inflater, container, false)

        binding.sendMoneyBtn.setOnClickListener {
            openDashboardFragment()
        }

        navController = findNavController()

        return binding.root
    }

    private fun openDashboardFragment() {
        navController.navigate(R.id.dashboardFragment)
    }
}