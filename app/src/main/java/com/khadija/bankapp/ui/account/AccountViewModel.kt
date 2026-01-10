package com.khadija.bankapp.ui.account

import com.khadija.bankapp.data.account.AccountRepository
import com.khadija.bankapp.data.account.BankAccount


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class AccountViewModel(
    private val repository: AccountRepository = AccountRepository(),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) : ViewModel() {

    var bankAccount by mutableStateOf<BankAccount?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var error by mutableStateOf<String?>(null)
        private set

    fun loadAccount() {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            error = "User not logged in"
            return
        }

        isLoading = true
        error = null

        repository.getAccount(uid) { account ->
            bankAccount = account
            isLoading = false
        }
    }
}
