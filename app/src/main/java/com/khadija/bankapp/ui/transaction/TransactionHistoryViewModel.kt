package com.khadija.bankapp.ui.transaction

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.khadija.bankapp.data.transaction.Transaction

class TransactionHistoryViewModel : ViewModel() {

    var transactions by mutableStateOf<List<Transaction>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var error by mutableStateOf<String?>(null)
        private set

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    fun loadTransactions() {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            error = "User not logged in"
            return
        }

        isLoading = true
        error = null

        db.collection("transactions")
            .whereEqualTo("uid", uid)
            .orderBy("timestamp") // ⚠️ خاصها index فـ Firebase
            .get()
            .addOnSuccessListener { result ->
                transactions = result.toObjects(Transaction::class.java)
                isLoading = false
            }
            .addOnFailureListener { e ->
                error = e.message
                isLoading = false
            }
    }
}
