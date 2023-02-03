package com.example.paypesa.data.repository

import com.example.paypesa.data.datasource.FirebaseDataSource
import com.example.paypesa.data.model.AuthModel
import com.example.paypesa.data.state.ResultState
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val firebaseDataSource: FirebaseDataSource
) {
    suspend fun loginUser(authModel: AuthModel) =  flow{
        emit(ResultState.Loading)

        firebaseDataSource.signInUser(authModel.email, authModel.password).collect { result: Result<Boolean> ->
            if (result.isSuccess)
                emit(ResultState.Success(result.getOrNull()))
            else if (result.isFailure)
                emit(ResultState.Failure(result.exceptionOrNull()))
            else
                emit(ResultState.Failure(Throwable(Exception("Unknown Error occurred"))))
        }
    }

    suspend fun registerUser(authModel: AuthModel) = flow {
        emit(ResultState.Loading)

        firebaseDataSource.createUser(authModel.email, authModel.password).collect {result: Result<Boolean> ->
            if (result.isSuccess)
                emit(ResultState.Success(result.getOrNull()))
            else if(result.isFailure)
                emit(ResultState.Failure(result.exceptionOrNull()))
            else
                emit(ResultState.Failure(Throwable(Exception("Unknown Error occurred"))))
        }

    }
}