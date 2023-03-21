package com.example.paypesa.data.model

data class ProfileModel(
    val email: String,
    val phoneNumber: String,
    val amount: Int,
    val transactionLog: List<TransactionModel>?
)
