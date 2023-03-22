package com.example.paypesa.data.model

data class ProfileModel(
    val email: String,
    val name: String,
    val phoneNumber: String,
    val amount: Double,
    val transactionLog: List<TransactionModel>?
)
