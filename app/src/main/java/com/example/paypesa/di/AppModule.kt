package com.example.paypesa.di

import com.example.paypesa.data.datasource.FirebaseDataSource
import com.example.paypesa.data.repository.AuthRepository
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
}