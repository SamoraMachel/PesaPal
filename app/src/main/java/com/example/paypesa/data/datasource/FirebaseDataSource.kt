package com.example.paypesa.data.datasource

import android.util.Log
import com.example.paypesa.data.model.ModelMap
import com.example.paypesa.data.model.ProfileModel
import com.example.paypesa.data.model.mapToObject
import com.example.paypesa.data.model.toMap
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject
import kotlin.reflect.KClass

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

    suspend fun<T: Any> createDocument(path: String, data: T ) = channelFlow<Result<String>> {
        try {
            firebaseFirestore.collection(path)
                .add(toMap(data))
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

    suspend fun updateProfileAmount(id: String, amount: Long) = channelFlow{
        try {
            firebaseFirestore.collection("profile")
                .document(id)
                .update("amount", amount)
                .addOnSuccessListener {
                    launch {
                        send(Result.success(true))
                    }
                }
                .addOnFailureListener {
                    launch {
                        send(Result.failure<Boolean>(it))
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

    suspend fun<T: Any> createNestedDocument(path: String, id: String, data: T) = channelFlow<Result<String>> {
        try {
            firebaseFirestore.collection(path).document(id).collection("transactionLog")
                .add(toMap(data))
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

    suspend fun<T: Any> readDocument(path: String, id: String, clazz: KClass<T>) = channelFlow {
        try {
            firebaseFirestore.collection(path)
                .document(id).get()
                .addOnSuccessListener { documentSnapshot: DocumentSnapshot ->
                    val data: Map<String, Any>? = documentSnapshot.data
                    val obj: T? = data?.let { mapToObject(it, clazz) }
                    launch {
                        send(Result.success(obj))
                    }
                }
                .addOnFailureListener {
                    launch {
                        send(Result.failure<T>(it))
                    }
                    it.printStackTrace()
                }
        } catch (error: IOException) {
            error.printStackTrace()
            send(Result.failure(Exception("Network Error: Kindly check your internet")))
        } catch (error: Exception) {
            error.printStackTrace()
            send(Result.failure(error))
        }
        awaitClose()
    }

    suspend fun readProfileByPhoneNumber(phoneNumber: String) = channelFlow {
        try {
            firebaseFirestore.collection("profile")
                .whereEqualTo("phoneNumber", phoneNumber)
                .get()
                .addOnSuccessListener { querySnapshot: QuerySnapshot ->
                    val documentList = querySnapshot.documents
                    if(documentList.isNotEmpty()) {
                        val dataProfile = documentList[0]
                        val obj: ProfileModel? = dataProfile.data?.let { mapToObject(it, ProfileModel::class) }
                        obj?.documentId = dataProfile.id
                        launch {
                            send(Result.success(obj))
                        }

                    } else {
                        launch {
                            send(Result.failure<ProfileModel>(Exception("User not found")))
                        }
                    }
                }

        } catch (error: IOException) {
            error.printStackTrace()
            send(Result.failure(Exception("Network Error: Kindly check your internet")))
        } catch (error: Exception) {
            error.printStackTrace()
            send(Result.failure(error))
        }
        awaitClose()
    }

    suspend fun readProfileByEmail(email: String) = channelFlow {
        try {
            firebaseFirestore.collection("profile")
                .whereEqualTo("email", email)
                .get()
                .addOnSuccessListener { querySnapshot: QuerySnapshot ->
                    val documentList = querySnapshot.documents
                    if(documentList.isNotEmpty()) {
                        val dataProfile = documentList[0]
                        val obj: ProfileModel? = dataProfile.data?.let { mapToObject(it, ProfileModel::class) }
                        obj?.documentId = dataProfile.id
                        launch {
                            send(Result.success(obj))
                        }

                    } else {
                        launch {
                            send(Result.failure<ProfileModel>(Exception("User not found")))
                        }
                    }
                }

        } catch (error: IOException) {
            error.printStackTrace()
            send(Result.failure(Exception("Network Error: Kindly check your internet")))
        } catch (error: Exception) {
            error.printStackTrace()
            send(Result.failure(error))
        }
        awaitClose()
    }

    suspend fun<T: Any> readNestedListDocument(path: String, id: String, clazz: KClass<T>) = channelFlow<Result<List<T?>>> {
        try {
            firebaseFirestore.collection(path).document(id)
                .collection("transactionLog").get()
                .addOnSuccessListener { querySnapshot: QuerySnapshot ->
                    val data = querySnapshot.documents
                    Log.d("FirebaseDataSource", "readNestedListDocument: Fetching data")
                    val listOfData = data.map { documentSnapshot: DocumentSnapshot ->
                        documentSnapshot.data?.let { it1 -> mapToObject(it1, clazz) }
                    }
                    Log.d("FirebaseDataSource", "readNestedListDocument: $listOfData")
                    launch {
                        send(Result.success(listOfData))
                    }
                }
                .addOnFailureListener {
                    launch {
                        send(Result.failure(it))
                    }
                }

        } catch (error: IOException) {
            error.printStackTrace()
            send(Result.failure(Exception("Network Error: Kindly check your internet")))
        } catch (error: Exception) {
            error.printStackTrace()
            send(Result.failure(error))
        }
        awaitClose()
    }
}