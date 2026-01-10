package com.khadija.bankapp.data.transaction

data class Transaction(
    val id: String = "",
    val uid: String = "",
    val type: String = "", // DEPOSIT / WITHDRAW
    val amount: Double = 0.0,
    val timestamp: Long = System.currentTimeMillis()
)
