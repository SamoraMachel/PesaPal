package com.example.paypesa.data.model

data class ProfileModel(
    val phoneNumber: Number,
    val amount: Int,
    val transactionLog: TransactionModel
): ModelMap()
