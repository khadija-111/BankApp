package com.khadija.bankapp.ui.transaction

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.khadija.bankapp.data.account.AccountRepository
import com.khadija.bankapp.data.transaction.Transaction
import com.khadija.bankapp.data.transaction.TransactionRepository

class TransactionViewModel(
    private val accountRepo: AccountRepository = AccountRepository(),
    private val transactionRepo: TransactionRepository = TransactionRepository(),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) : ViewModel() {

    var isLoading by mutableStateOf(false)
        private set

    var error by mutableStateOf<String?>(null)
        private set

    fun deposit(currentBalance: Double, amount: Double, onSuccess: () -> Unit) {
        val uid = auth.currentUser?.uid ?: return
        val newBalance = currentBalance + amount

        isLoading = true
        accountRepo.updateBalance(uid, newBalance) { success ->
            if (success) {
                transactionRepo.addTransaction(
                    Transaction(uid = uid, type = "DEPOSIT", amount = amount)
                ) {
                    isLoading = false
                    onSuccess()
                }
            } else {
                isLoading = false
                error = "Deposit failed"
            }
        }
    }

    fun withdraw(currentBalance: Double, amount: Double, onSuccess: () -> Unit) {
        if (amount > currentBalance) {
            error = "Insufficient balance"
            return
        }

        val uid = auth.currentUser?.uid ?: return
        val newBalance = currentBalance - amount

        isLoading = true
        accountRepo.updateBalance(uid, newBalance) { success ->
            if (success) {
                transactionRepo.addTransaction(
                    Transaction(uid = uid, type = "WITHDRAW", amount = amount)
                ) {
                    isLoading = false
                    onSuccess()
                }
            } else {
                isLoading = false
                error = "Withdraw failed"
            }
        }
    }
}
