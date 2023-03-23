package com.example.paypesa.ui.home

import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
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
class DashboardViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val sharedPreferences: SharedPreferences,
    private val sharedPreferenceEditor: Editor,
    private val transactionRepository: TransactionRepository
): ViewModel() {

    private val _profileState: MutableLiveData<ResultState<ProfileModel>> = MutableLiveData()
    val profileState: LiveData<ResultState<ProfileModel>> get() = _profileState

    private val _transactionsState = MutableLiveData<ResultState<List<TransactionModel?>>>()
    val transactionState: LiveData<ResultState<List<TransactionModel?>>> get() = _transactionsState

    private var profileId: String = sharedPreferences.getString(ConstantKey.PROFILE_ID, "")?:""

    init {
        fetchProfile()
    }

    fun fetchProfile() = viewModelScope.launch {
        profileRepository.readProfile(profileId).collect { resultState ->
            when(resultState) {
                is ResultState.Failure -> _profileState.value = ResultState.Failure(resultState.exception, resultState.message)
                ResultState.Loading -> _profileState.value = ResultState.Loading
                is ResultState.Success -> {
                    _profileState.value = resultState
                    resultState.data?.let {
                        sharedPreferenceEditor.putLong(ConstantKey.WALLET_AMOUNT, it.amount)
                        sharedPreferenceEditor.apply()
                    }
                }
            }
        }
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