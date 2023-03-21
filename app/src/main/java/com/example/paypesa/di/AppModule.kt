package com.example.paypesa.di

import com.example.paypesa.data.datasource.FirebaseDataSource
import com.example.paypesa.data.repository.AuthRepository
import com.example.paypesa.data.repository.ProfileRepository
import com.example.paypesa.data.repository.TransactionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun providesAuthRepository(firebaseDataSource: FirebaseDataSource): AuthRepository {
        return AuthRepository(firebaseDataSource)
    }

    @Provides
    fun providesProfileRepository(firebaseDataSource: FirebaseDataSource): ProfileRepository {
        return ProfileRepository(firebaseDataSource)
    }

    @Provides
    fun providesTransactionRepository(firebaseDataSource: FirebaseDataSource): TransactionRepository {
        return TransactionRepository(firebaseDataSource)
    }
}