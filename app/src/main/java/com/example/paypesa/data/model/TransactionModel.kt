package com.example.paypesa.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.time.LocalDateTime

@Parcelize
data class TransactionModel(
    val user: String,
    val date: Long,
    val amount: Float,
): Parcelable
