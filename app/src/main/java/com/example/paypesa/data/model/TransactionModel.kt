package com.example.paypesa.data.model

data class TransactionModel(
    val user: String,
    val date: String,
    val amount: Int,
): ModelMap()
