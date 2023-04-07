package com.example.paypesa.ui.payment

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.paypesa.data.ConstantKey
import com.example.paypesa.data.model.ProfileModel
import com.example.paypesa.data.model.TransactionModel
import com.example.paypesa.data.repository.ProfileRepository
import com.example.paypesa.data.repository.TransactionRepository
import com.example.paypesa.data.state.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val profileRepository: ProfileRepository,
    private val sharedPreferences: SharedPreferences,
    private val sharedPrefEditor: SharedPreferences.Editor
): ViewModel() {

    private val _transactionState = MutableLiveData<ResultState<String>>()
    val transactionState: LiveData<ResultState<String>> get() = _transactionState

    private val _senderTransactionState = MutableLiveData<ResultState<String>>()
    val senderTransactionState: LiveData<ResultState<String>> get() = _senderTransactionState

    private val _senderProfileUpdate = MutableLiveData<ResultState<Boolean>>()
    val senderProfileUpdate: LiveData<ResultState<Boolean>> get() = _senderProfileUpdate

    private val _recieverProfileUpdate = MutableLiveData<ResultState<Boolean>>()
    val recieverProfileUpdate: LiveData<ResultState<Boolean>> get() = _recieverProfileUpdate

    val profileId: String = sharedPreferences.getString(ConstantKey.PROFILE_ID, "")?:""

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

    fun updateRecieverProfile(amount: Long, profile: ProfileModel) = viewModelScope.launch {
        val newAmount = profile.amount + amount
        profile.documentId?.let {
            profileRepository.updatedProfileAmount(it, newAmount).collect { resultState ->
                when(resultState) {
                    is ResultState.Failure -> {
                        _recieverProfileUpdate.value = resultState
                    }
                    ResultState.Loading -> _recieverProfileUpdate.value = resultState
                    is ResultState.Success -> {
                        _recieverProfileUpdate.value = resultState
                    }
                }
            }
        }
    }

    fun updateSenderProfile(amount: Float) = viewModelScope.launch {
        profileRepository.updatedProfileAmount(profileId, amount.toLong()).collect { resultState ->
            when(resultState) {
                is ResultState.Failure -> _senderProfileUpdate.value = resultState
                ResultState.Loading -> _senderProfileUpdate.value = resultState
                is ResultState.Success -> {
                    _senderProfileUpdate.value = resultState
                    sharedPrefEditor.putLong(ConstantKey.WALLET_AMOUNT, amount.toLong())
                    sharedPrefEditor.apply()
                }
            }
        }
    }
}