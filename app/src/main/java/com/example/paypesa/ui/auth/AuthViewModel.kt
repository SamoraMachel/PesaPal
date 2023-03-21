package com.example.paypesa.ui.auth

import android.content.SharedPreferences
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.paypesa.R
import com.example.paypesa.data.ConstantKey
import com.example.paypesa.data.model.AuthModel
import com.example.paypesa.data.model.ProfileModel
import com.example.paypesa.data.repository.AuthRepository
import com.example.paypesa.data.repository.ProfileRepository
import com.example.paypesa.data.state.AuthFormState
import com.example.paypesa.data.state.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository,
    private val sharedPrefEditor: SharedPreferences.Editor
) : ViewModel() {

    private val _authForm = MutableLiveData<AuthFormState>()
    val authFormState: LiveData<AuthFormState> = _authForm

    private val _authResult = MutableLiveData<ResultState<Boolean>>()
    val authResult: LiveData<ResultState<Boolean>> = _authResult

    private val _loginState = MutableLiveData<ResultState<Boolean>>()
    val loginState: LiveData<ResultState<Boolean>> = _loginState

    private val _profileState = MutableLiveData<ResultState<String>>()
    val profileState: LiveData<ResultState<String>> = _profileState

    fun register(authModel: AuthModel) = viewModelScope.launch {
        authRepository.registerUser(authModel).collect { resultState: ResultState<Boolean> ->
            when(resultState) {
                is ResultState.Failure -> _authResult.value = ResultState.Failure(resultState.exception, resultState.message)
                ResultState.Loading -> _authResult.value = ResultState.Loading
                is ResultState.Success -> {
                    updateAuthSharedPref(authModel)
                    _authResult.value = ResultState.Success(resultState.data)
                }
            }
        }
    }

    fun login(authModel: AuthModel) = viewModelScope.launch {
        authRepository.loginUser(authModel).collect {resultState: ResultState<Boolean> ->
            when(resultState) {
                is ResultState.Failure -> _loginState.value = ResultState.Failure(resultState.exception, resultState.message)
                ResultState.Loading -> _loginState.value = ResultState.Loading
                is ResultState.Success -> {
                    updateAuthSharedPref(authModel)
                    _loginState.value = ResultState.Success(resultState.data)
                }
            }
        }
    }

    private fun updateAuthSharedPref(authModel: AuthModel) {
        sharedPrefEditor.putString(ConstantKey.USER_EMAIL, authModel.email)
        sharedPrefEditor.putString(ConstantKey.USER_PASSWORD, authModel.password)
        sharedPrefEditor.putBoolean(ConstantKey.IS_USER_LOGGED_IN, true)
        sharedPrefEditor.commit()
    }

    private fun updateProfileCreationStatus() {
        sharedPrefEditor.putBoolean(ConstantKey.IS_PROFILE_CREATED, true)
        sharedPrefEditor.commit()
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isEmailValid(username)) {
            _authForm.value = AuthFormState(emailError =  R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _authForm.value = AuthFormState(passwordError = R.string.invalid_password)
        } else {
            _authForm.value = AuthFormState(isDataValid = true)
        }
    }


    private fun isEmailValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length > 8
    }

    fun createProfile(profile: ProfileModel) = viewModelScope.launch {
        profileRepository.createProfile(profile).collect { resultState: ResultState<String> ->
            when(resultState) {
                is ResultState.Failure -> _profileState.value = ResultState.Failure(resultState.exception, resultState.message)
                ResultState.Loading -> _profileState.value = ResultState.Loading
                is ResultState.Success -> {
                    resultState.data?.let { id: String ->
                        sharedPrefEditor.putString(ConstantKey.PROFILE_ID, id)
                    }
                    _profileState.value = ResultState.Success(resultState.data)
                    updateProfileCreationStatus()
                }
            }
        }
    }
}