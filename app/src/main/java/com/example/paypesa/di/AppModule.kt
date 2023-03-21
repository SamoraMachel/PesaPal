package com.example.paypesa.di

import android.content.Context
import android.content.SharedPreferences
import com.example.paypesa.data.ConstantKey
import com.example.paypesa.data.datasource.FirebaseDataSource
import com.example.paypesa.data.repository.AuthRepository
import com.example.paypesa.data.repository.ProfileRepository
import com.example.paypesa.data.repository.TransactionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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

    @Provides
    fun providesSharedPreference(@ApplicationContext context: Context) : SharedPreferences {
        return context.getSharedPreferences(ConstantKey.PREFERENCE_NAME, Context.MODE_PRIVATE)
    }

    @Provides
    fun providesSharedPreferenceEditor(sharedPreferences: SharedPreferences): SharedPreferences.Editor {
        return sharedPreferences.edit()
    }
}