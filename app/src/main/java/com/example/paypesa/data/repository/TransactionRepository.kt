package com.example.paypesa.data.repository

import com.example.paypesa.data.datasource.FirebaseDataSource
import com.example.paypesa.data.model.TransactionModel
import com.example.paypesa.data.state.ResultState
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TransactionRepository @Inject constructor(
    private val firebaseDataSource: FirebaseDataSource
) {
    suspend fun createTransaction(id: String, transaction: TransactionModel) = flow {
        emit(ResultState.Loading)

        firebaseDataSource.createNestedDocument("profile", id, transaction).collect { result ->
            if(result.isSuccess) {
                emit(ResultState.Success(result.getOrNull()))
            } else if(result.isFailure) {
                emit(ResultState.Failure(result.exceptionOrNull()))
            } else {
                emit(ResultState.Failure(Throwable(Exception("Unknown Error occurred"))))
            }
        }
    }
}
