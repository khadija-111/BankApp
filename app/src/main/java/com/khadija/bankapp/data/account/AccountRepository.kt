package com.khadija.bankapp.data.account

import com.google.firebase.firestore.FirebaseFirestore

class AccountRepository {

    private val db = FirebaseFirestore.getInstance()

    fun createAccount(
        account: BankAccount,
        onComplete: (Boolean) -> Unit
    ) {
        db.collection("accounts")
            .document(account.uid)
            .set(account)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }

    fun getAccount(uid: String, onResult: (BankAccount?) -> Unit) {
        db.collection("accounts")
            .document(uid)
            .get()
            .addOnSuccessListener { doc ->
                if (doc.exists()) {
                    onResult(doc.toObject(BankAccount::class.java))
                } else {
                    onResult(null)
                }
            }
            .addOnFailureListener {
                onResult(null)
            }
    }


    fun updateBalance(uid: String, newBalance: Double, onComplete: (Boolean) -> Unit) {
        db.collection("accounts")
            .document(uid)
            .update("balance", newBalance)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }

}
