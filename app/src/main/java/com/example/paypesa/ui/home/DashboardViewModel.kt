package com.example.paypesa.ui.home

import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.paypesa.data.ConstantKey
import com.example.paypesa.data.model.ProfileModel
import com.example.paypesa.data.repository.ProfileRepository
import com.example.paypesa.data.state.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val sharedPreferences: SharedPreferences,
    private val sharedPreferenceEditor: Editor
): ViewModel() {

    private val _profileState: MutableLiveData<ResultState<ProfileModel>> = MutableLiveData()
    val profileState: LiveData<ResultState<ProfileModel>> get() = _profileState

    init {
        fetchProfile()
    }

    fun fetchProfile() = viewModelScope.launch {
        val profileId: String = sharedPreferences.getString(ConstantKey.PROFILE_ID, "")?:""

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
}