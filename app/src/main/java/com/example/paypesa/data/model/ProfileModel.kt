package com.example.paypesa.data.model

data class ProfileModel(
    val email: String,
    val phoneNumber: Int,
    val amount: Int,
    val transactionLog: List<TransactionModel>?
): ModelMap()
