package com.example.paypesa.data.datasource

import com.example.paypesa.data.model.ModelMap
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
                    it.printStackTrace()
                }

        } catch (error : IOException) {
            send(Result.failure(Exception("Network Error. Kindly check your internet")))
            error.printStackTrace()
        } catch (error: Exception) {
            send(Result.failure(error))
            error.printStackTrace()
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
                    it.printStackTrace()
                }

        }  catch (error : IOException) {
            send(Result.failure(Exception("Network Error. Kindly check your internet")))
            error.printStackTrace()
        } catch (error: Exception) {
            send(Result.failure(error))
            error.printStackTrace()
        }
        awaitClose()
    }

    suspend fun<T: ModelMap> createDocument(path: String, data: T ) = channelFlow<Result<String>> {
        try {
            firebaseFirestore.collection(path)
                .add(data.asMap())
                .addOnSuccessListener {
                    launch {
                        send(Result.success(it.id))
                    }
                }
                .addOnFailureListener {
                    launch {
                        send(Result.failure(it))
                    }
                    it.printStackTrace()
                }
        } catch (error: IOException) {
            send(Result.failure(Exception("Network Error: Kindly check your internet")))
            error.printStackTrace()
        } catch (error: Exception) {
            send(Result.failure(error))
            error.printStackTrace()
        }
        awaitClose()
    }

    suspend fun<T: ModelMap> createNestedDocument(path: String, id: String, data: T) = channelFlow<Result<String>> {
        try {
            firebaseFirestore.collection(path).document(id).collection("transactionLog")
                .add(data.asMap())
                .addOnSuccessListener {
                    launch {
                        send(Result.success(it.id))
                    }
                }
                .addOnFailureListener {
                    launch {
                        send(Result.failure(it))
                    }
                    it.printStackTrace()
                }
        } catch (error: IOException) {
            send(Result.failure(Exception("Network Error: Kindly check your internet")))
            error.printStackTrace()
        } catch (error: Exception) {
            send(Result.failure(error))
            error.printStackTrace()
        }
        awaitClose()
    }
}