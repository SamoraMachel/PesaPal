package com.example.paypesa.ui.transaction

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.paypesa.data.ConstantKey
import com.example.paypesa.data.model.TransactionModel
import com.example.paypesa.data.repository.TransactionRepository
import com.example.paypesa.data.state.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    sharedPreferences: SharedPreferences,
): ViewModel() {
    private val _transactionsState = MutableLiveData<ResultState<List<TransactionModel?>>>()
    val transactionState: LiveData<ResultState<List<TransactionModel?>>> get() = _transactionsState

    private var profileId: String = sharedPreferences.getString(ConstantKey.PROFILE_ID, "")?:""

    init {
        fetchTransactions()
    }

    fun fetchTransactions() = viewModelScope.launch {
        transactionRepository.readTransactionLogs(profileId).collect { resultState ->
            when(resultState) {
                is ResultState.Failure -> _transactionsState.value = resultState
                ResultState.Loading -> _transactionsState.value = ResultState.Loading
                is ResultState.Success -> {
                    _transactionsState.value = resultState
                }
            }
        }
    }
}