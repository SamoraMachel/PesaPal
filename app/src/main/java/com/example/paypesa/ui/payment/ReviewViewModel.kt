package com.example.paypesa.ui.payment

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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val sharedPreferences: SharedPreferences
): ViewModel() {

    private val _transactionState = MutableLiveData<ResultState<String>>()
    val transactionState: LiveData<ResultState<String>> get() = _transactionState

    private val _senderTransactionState = MutableLiveData<ResultState<String>>()
    val senderTransactionState: LiveData<ResultState<String>> get() = _senderTransactionState

    fun saveTransaction(recieverId: String, transModel: TransactionModel) = viewModelScope.launch {
        transactionRepository.createTransaction(recieverId, transModel).collect { resultState: ResultState<String> ->
            when(resultState) {
                is ResultState.Failure -> _transactionState.value = resultState
                ResultState.Loading -> _transactionState.value = resultState
                is ResultState.Success -> _transactionState.value = resultState
            }
        }
    }

    fun saveSenderTransactionState(transModel: TransactionModel) = viewModelScope.launch {
        val userId = sharedPreferences.getString(ConstantKey.PROFILE_ID, "")?:""
        transactionRepository.createTransaction(userId, transModel).collect { resultState: ResultState<String> ->
            when(resultState){
                is ResultState.Failure -> _senderTransactionState.value = resultState
                ResultState.Loading -> _senderTransactionState.value = resultState
                is ResultState.Success -> _senderTransactionState.value = resultState
            }
        }
    }
}