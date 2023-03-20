package com.example.paypesa.ui.payment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.paypesa.R
import com.example.paypesa.databinding.FragmentSendAmountBinding


class SendAmountFragment : Fragment() {

    private lateinit var binding: FragmentSendAmountBinding
    private lateinit var navController: NavController

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

        binding.proceed.setOnClickListener {
            openPaymentReviewFragment()
        }

        return binding.root
    }

    private fun openPaymentReviewFragment() {
        navController.navigate(R.id.paymentReviewFragment)
    }


}