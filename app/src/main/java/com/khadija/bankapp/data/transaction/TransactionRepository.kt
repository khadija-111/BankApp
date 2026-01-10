package com.khadija.bankapp.data.transaction

import com.google.firebase.firestore.FirebaseFirestore

class TransactionRepository {

    private val db = FirebaseFirestore.getInstance()

    fun addTransaction(transaction: Transaction, onComplete: (Boolean) -> Unit) {
        db.collection("transactions")
            .add(transaction)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }
    fun getTransactionsByUid(
        uid: String,
        onResult: (List<Transaction>) -> Unit
    ) {
        db.collection("transactions")
            .whereEqualTo("uid", uid)
            .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { snapshot ->
                val list = snapshot.toObjects(Transaction::class.java)
                onResult(list)
            }
            .addOnFailureListener {
                onResult(emptyList())
            }
    }

}
