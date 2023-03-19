package com.example.paypesa.data.repository

import com.example.paypesa.data.datasource.FirebaseDataSource
import com.example.paypesa.data.model.ProfileModel
import com.example.paypesa.data.state.ResultState
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    private val firebaseDataSource: FirebaseDataSource
) {
    suspend fun createProfile(profileData: ProfileModel) = flow {
        emit(ResultState.Loading)

        firebaseDataSource.createDocument("profile", profileData).collect { result ->
            if(result.isSuccess)
                emit(ResultState.Success(result.getOrNull()))
            else if(result.isFailure) {
                emit(ResultState.Failure(result.exceptionOrNull()))
            }
            else
                emit(ResultState.Failure(Throwable(Exception("Unknown Error occurred"))))
        }
    }
}