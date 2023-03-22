package com.example.paypesa.ui.payment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.paypesa.data.model.ProfileModel
import com.example.paypesa.data.repository.ProfileRepository
import com.example.paypesa.data.state.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SendAmountViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
): ViewModel() {

    private val _profileRetrieveState = MutableLiveData<ResultState<ProfileModel>>()
    val profileRetrieveState: LiveData<ResultState<ProfileModel>> get() = _profileRetrieveState

    fun retrieveProfile(phoneNumber: String) = viewModelScope.launch {
        profileRepository.readProfileByPhoneNumber(phoneNumber).collect { resultState: ResultState<ProfileModel> ->
            when(resultState) {
                is ResultState.Failure -> _profileRetrieveState.value = resultState
                ResultState.Loading -> _profileRetrieveState.value = resultState
                is ResultState.Success -> {
                    _profileRetrieveState.value = resultState
                }
            }
        }
    }
}