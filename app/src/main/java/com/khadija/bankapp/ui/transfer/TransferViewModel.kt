package com.khadija.bankapp.ui.transfer

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.khadija.bankapp.data.account.BankAccount
import com.khadija.bankapp.data.transaction.Transaction

class TransferViewModel : ViewModel() {

    var isLoading by mutableStateOf(false)
        private set
    var error by mutableStateOf<String?>(null)
        private set
    var success by mutableStateOf(false)
        private set

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    fun showError(message: String) {
        error = message
    }

    fun resetState() {
        isLoading = false
        error = null
        success = false
    }

    fun transferMoney(receiverIban: String, amount: Double) {
        val senderUid = auth.currentUser?.uid
        if (senderUid == null) {
            error = "User not logged in"
            return
        }

        if (amount <= 0) {
            error = "Invalid amount"
            return
        }

        isLoading = true
        error = null
        success = false

        val accountsRef = db.collection("accounts")
        val transactionsRef = db.collection("transactions")

        // 1️⃣ Sender account ➜ document(uid)
        val senderRef = accountsRef.document(senderUid)
        senderRef.get().addOnSuccessListener { senderSnap ->

            if (!senderSnap.exists()) {
                error = "Sender account not found"
                isLoading = false
                return@addOnSuccessListener
            }

            val senderAccount = senderSnap.toObject(BankAccount::class.java)!!

            if (senderAccount.balance < amount) {
                error = "Insufficient balance"
                isLoading = false
                return@addOnSuccessListener
            }

            // 2️⃣ Receiver account ➜ by IBAN
            accountsRef
                .whereEqualTo("iban", receiverIban)
                .limit(1)
                .get()
                .addOnSuccessListener { receiverSnap ->

                    if (receiverSnap.isEmpty) {
                        error = "Receiver IBAN not found"
                        isLoading = false
                        return@addOnSuccessListener
                    }

                    val receiverDoc = receiverSnap.documents[0]
                    val receiverAccount =
                        receiverDoc.toObject(BankAccount::class.java)!!

                    // 3️⃣ Firestore transaction
                    db.runTransaction { transaction ->
                        transaction.update(
                            senderRef,
                            "balance",
                            senderAccount.balance - amount
                        )
                        transaction.update(
                            receiverDoc.reference,
                            "balance",
                            receiverAccount.balance + amount
                        )

                        val outTx = transactionsRef.document()
                        transaction.set(
                            outTx,
                            Transaction(
                                id = outTx.id,
                                uid = senderUid,
                                type = "TRANSFER_OUT",
                                amount = amount
                            )
                        )

                        val inTx = transactionsRef.document()
                        transaction.set(
                            inTx,
                            Transaction(
                                id = inTx.id,
                                uid = receiverAccount.uid,
                                type = "TRANSFER_IN",
                                amount = amount
                            )
                        )
                    }
                        .addOnSuccessListener {
                            success = true
                            isLoading = false
                        }
                        .addOnFailureListener {
                            error = it.message
                            isLoading = false
                        }
                }
                .addOnFailureListener {
                    error = it.message
                    isLoading = false
                }
        }.addOnFailureListener {
            error = it.message
            isLoading = false
        }
    }
}
