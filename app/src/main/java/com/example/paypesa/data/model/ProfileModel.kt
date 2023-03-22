package com.example.paypesa.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProfileModel(
    val email: String,
    val name: String,
    val phoneNumber: String,
    val amount: Double,
    val transactionLog: List<TransactionModel>?
) : Parcelable {
    val documentId: String? = null
}