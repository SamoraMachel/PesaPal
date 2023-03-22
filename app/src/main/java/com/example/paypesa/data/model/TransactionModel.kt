package com.example.paypesa.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TransactionModel(
    val user: String,
    val date: String,
    val amount: Int,
): Parcelable
