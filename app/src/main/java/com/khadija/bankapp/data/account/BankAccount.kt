package com.khadija.bankapp.data.account

data class BankAccount(
    val uid: String = "",
    val iban: String = "",
    val accountType: String = "CURRENT", // CURRENT / SAVINGS
    val balance: Double = 0.0,
    val createdAt: Long = System.currentTimeMillis()
)
