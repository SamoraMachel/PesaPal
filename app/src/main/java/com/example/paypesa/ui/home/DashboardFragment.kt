package com.example.paypesa.ui.home

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.paypesa.R
import com.example.paypesa.data.ConstantKey
import com.example.paypesa.data.model.ProfileModel
import com.example.paypesa.data.model.TransactionModel
import com.example.paypesa.data.state.ResultState
import com.example.paypesa.databinding.FragmentDashboardBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DashboardFragment : Fragment() {

    private lateinit var binding: FragmentDashboardBinding
    private val viewModel: DashboardViewModel by activityViewModels()

    @Inject lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDashboardBinding.inflate(layoutInflater, container, false)

        val walletAmount = sharedPreferences.getLong(ConstantKey.WALLET_AMOUNT, 0)
        binding.dashboardAccountBalance.text = walletAmount.toString()

        profileStateListener()
        transactionStateListener()

        return binding.root
    }

    private fun profileStateListener() {
        viewModel.profileState.observe(viewLifecycleOwner) { resultState: ResultState<ProfileModel> ->
            when(resultState) {
                is ResultState.Failure -> {
                    showLoader(false)
                    showSnackbar(resultState.message?:"Unknown error occurred. Try reopening the application")
                }
                ResultState.Loading -> {
                    showLoader(true)
                }
                is ResultState.Success -> {
                    showLoader(false)
                    resultState.data?.let {
                        binding.welcomeTextView.text = "Welcome Back\n${it.name}"
                    }
                }
            }
        }
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
                    showLoader(false)
                    resultState.data?.let {
                        binding.recentRecyclerView.adapter = RecentRecyclerView(it)
                        binding.recentRecyclerView.layoutManager = LinearLayoutManager(
                            requireContext(), LinearLayoutManager.VERTICAL, false
                        )
                    }
                }
            }
        }
    }

    private fun showLoader(visible: Boolean = true,  message: String = "Loading") {
        if(visible) {
            binding.dashboardLoader.visibility = View.VISIBLE
            binding.dashboardLoaderText.text = message
        } else {
            binding.dashboardLoader.visibility = View.GONE
        }
    }

    private fun showSnackbar(message: String, length: Int = Snackbar.LENGTH_LONG) {
        Snackbar.make(binding.root, message, length).show()
    }


}