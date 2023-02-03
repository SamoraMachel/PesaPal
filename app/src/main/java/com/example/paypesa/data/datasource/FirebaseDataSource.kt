package com.example.paypesa.data.datasource

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

class FirebaseDataSource @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore
) {
    suspend fun signInUser(email: String, password: String) = channelFlow<Result<Boolean>>{
        try {
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    launch {
                        send(Result.success(true))
                    }
                }
                .addOnFailureListener {
                    launch {
                        send(Result.failure(it))
                    }
                }

        } catch (error : IOException) {
            send(Result.failure(Exception("Network Error. Kindly check your internet")))
        } catch (error: Exception) {
            send(Result.failure(error))
        }
        awaitClose()
    }

    suspend fun createUser(email: String, password: String) = channelFlow<Result<Boolean>> {
        try {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    launch {
                        send(Result.success(true))
                    }
                }
                .addOnFailureListener {
                    launch {
                        send(Result.failure(it))
                    }
                }

        }  catch (error : IOException) {
            send(Result.failure(Exception("Network Error. Kindly check your internet")))
        } catch (error: Exception) {
            send(Result.failure(error))
        }
        awaitClose()
    }
}