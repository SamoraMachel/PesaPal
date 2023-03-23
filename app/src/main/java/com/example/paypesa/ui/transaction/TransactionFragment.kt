package com.example.paypesa.ui.transaction

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.paypesa.R
import com.example.paypesa.data.model.TransactionModel
import com.example.paypesa.data.state.ResultState
import com.example.paypesa.databinding.FragmentPaymentReviewBinding
import com.example.paypesa.databinding.FragmentTransactionBinding
import com.example.paypesa.ui.home.RecentRecyclerView
import com.google.android.material.snackbar.Snackbar

class TransactionFragment : Fragment() {

    private lateinit var binding: FragmentTransactionBinding

    val viewModel: TransactionViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTransactionBinding.inflate(inflater, container, false)

        transactionStateListener()


        return binding.root
    }

    private fun transactionStateListener() {
        viewModel.transactionState.observe(viewLifecycleOwner) { resultState: ResultState<List<TransactionModel?>> ->
            when(resultState) {
                is ResultState.Failure -> {
                    showLoader(false)
                    resultState.message?.let {
                        showSnackbar("it")
                    }
                }
                ResultState.Loading -> {
                    showLoader(true, "Collecting transactions")
                }
                is ResultState.Success -> {
                    Log.d("Dashboard", "transactionStateListener: ${resultState.data}")
                    showLoader(false)
                    resultState.data?.let {
                        binding.transactionList.adapter = RecentRecyclerView(it)
                        binding.transactionList.layoutManager = LinearLayoutManager(
                            requireContext(), LinearLayoutManager.VERTICAL, false
                        )
                    }

                }
            }
        }
    }

    private fun showLoader(visible: Boolean = true,  message: String = "Loading") {
        if(visible) {
            binding.transactionLoader.visibility = View.VISIBLE
            binding.transactionLoaderText.text = message
        } else {
            binding.transactionLoader.visibility = View.GONE
        }
    }

    private fun showSnackbar(message: String, length: Int = Snackbar.LENGTH_LONG) {
        Snackbar.make(binding.root, message, length).show()
    }

}