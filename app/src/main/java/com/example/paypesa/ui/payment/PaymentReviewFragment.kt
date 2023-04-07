package com.example.paypesa.ui.payment

import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.paypesa.R
import com.example.paypesa.data.ConstantKey
import com.example.paypesa.data.model.TransactionModel
import com.example.paypesa.data.state.ResultState
import com.example.paypesa.databinding.FragmentPaymentReviewBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.inject.Inject

@AndroidEntryPoint
class PaymentReviewFragment : Fragment() {

    private lateinit var binding: FragmentPaymentReviewBinding
    private lateinit var navController: NavController

    private val navArgs: PaymentReviewFragmentArgs by navArgs()

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    @Inject
    lateinit var sharedPreferenceEditor: SharedPreferences.Editor

    val viewModel: ReviewViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPaymentReviewBinding.inflate(inflater, container, false)

        transactionStateListener()
        senderTransactionStateListener()

        senderProfileUpdatedListener()
        recieverProfileUpdateListener()

        val recieverProfile = navArgs.profile

        binding.recieverName.text = recieverProfile.name
        binding.recieverNumber.text = recieverProfile.phoneNumber
        binding.amountToSend.text = "$${navArgs.amount}"

        val walletAmount: Long = sharedPreferences.getLong(ConstantKey.WALLET_AMOUNT, 0)
        val amountRemaining = walletAmount - navArgs.amount

        binding.remainingAmount.text = "$$amountRemaining"

        binding.sendMoneyBtn.setOnClickListener {
            navArgs.profile.documentId?.let { it1 ->
                viewModel.saveTransaction(
                    it1,
                    TransactionModel(
                        navArgs.profile.name,
                        LocalDateTime.now().toEpochSecond(ZoneOffset.UTC),
                        -navArgs.amount.toDouble()
                    )
                )
            }
        }

        navController = findNavController()

        return binding.root
    }

    private fun openDashboardFragment() {
        navController.navigate(R.id.dashboardFragment)
    }

    private fun showLoader(visible: Boolean = true, message: String = "Loading") {
        if(visible) {
            binding.reviewLoader.visibility = View.VISIBLE
            binding.reviewLoaderText.text = message
        } else {
            binding.reviewLoader.visibility = View.GONE
        }
    }

    private fun showSnackbar(message: String, length: Int = Snackbar.LENGTH_LONG) {
        Snackbar.make(binding.root, message, length).show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun transactionStateListener() {
        viewModel.transactionState.observe(viewLifecycleOwner) { resultState: ResultState<String> ->
            when(resultState) {
                is ResultState.Failure -> {
                    showLoader(false)
                    resultState.message?.let { showSnackbar(it) }
                }
                ResultState.Loading -> {
                    showLoader(message = "Saving Transaction")
                }
                is ResultState.Success -> {
                    viewModel.saveSenderTransactionState(
                        TransactionModel(
                            navArgs.profile.name,
                            LocalDateTime.now().toEpochSecond(ZoneOffset.UTC),
                            navArgs.amount.toDouble()
                        )
                    )
                }
            }
        }
    }

    private fun senderTransactionStateListener() {
        viewModel.senderTransactionState.observe(viewLifecycleOwner) { resultState: ResultState<String> ->
            when(resultState) {
                is ResultState.Failure -> {
                    showLoader(false)
                    resultState.message?.let { showSnackbar(it) }
                }
                ResultState.Loading -> {
                    showLoader(true, "Synching transaction log")
                }
                is ResultState.Success -> {
                    viewModel.updateRecieverProfile(
                        navArgs.amount.toLong(),
                        navArgs.profile
                    )
                }
            }
        }
    }

    private fun senderProfileUpdatedListener() {
        viewModel.senderProfileUpdate.observe(viewLifecycleOwner) { resultState ->
            when(resultState) {
                is ResultState.Failure -> {
                    showLoader(false)
                    resultState.message?.let { showSnackbar(it) }
                }
                ResultState.Loading -> showLoader(true, "Synching updates...")
                is ResultState.Success -> {
                    resultState.data?.let {
                        if(it) {
                            openDashboardFragment()
                        }
                    }
                }
            }
        }

    }

    private fun recieverProfileUpdateListener() {
        viewModel.recieverProfileUpdate.observe(viewLifecycleOwner) { resultState ->
            when(resultState) {
                is ResultState.Failure -> {
                    showLoader(false)
                    resultState.message?.let { showSnackbar(it) }
                }
                ResultState.Loading -> showLoader(true, "Updating data...")
                is ResultState.Success -> {
                    resultState.data?.let {
                        if(it) {
                            val walletAmount: Long = sharedPreferences.getLong(ConstantKey.WALLET_AMOUNT, 0)
                            val amountRemaining = walletAmount - navArgs.amount
                            viewModel.updateSenderProfile(amountRemaining)
                        }
                    }
                }
            }
        }
    }
}