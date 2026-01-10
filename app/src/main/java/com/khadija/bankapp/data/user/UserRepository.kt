package com.khadija.bankapp.data.user

import com.google.firebase.firestore.FirebaseFirestore

class UserRepository {

    private val db = FirebaseFirestore.getInstance()

    fun createUserProfile(
        user: UserProfile,
        onComplete: (Boolean) -> Unit
    ) {
        db.collection("users")
            .document(user.uid)
            .set(user)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }

    fun getUserProfile(
        uid: String,
        onResult: (UserProfile?) -> Unit
    ) {
        db.collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener { doc ->
                onResult(doc.toObject(UserProfile::class.java))
            }
            .addOnFailureListener {
                onResult(null)
            }
    }
    fun updateUserProfile(
        user: UserProfile,
        onComplete: (Boolean) -> Unit
    ) {
        db.collection("users")
            .document(user.uid)
            .set(user)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }

}
