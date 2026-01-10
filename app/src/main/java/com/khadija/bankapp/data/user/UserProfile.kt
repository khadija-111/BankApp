package com.khadija.bankapp.data.user

data class UserProfile(
    val uid: String = "",
    val email: String = "",
    val fullName: String = "",
    val phone: String = "",
    val role: String = "client",

    val createdAt: Long = System.currentTimeMillis()
)
