package com.khadija.bankapp.ui.profile

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.khadija.bankapp.data.user.UserProfile
import com.khadija.bankapp.data.user.UserRepository

class ProfileViewModel(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val userRepository: UserRepository = UserRepository()
) : ViewModel() {

    var userProfile by mutableStateOf<UserProfile?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    fun loadUserProfile() {
        val uid = auth.currentUser?.uid ?: return
        isLoading = true

        userRepository.getUserProfile(uid) { profile ->
            userProfile = profile
            isLoading = false
        }
    }

    fun updateProfile(updatedUser: UserProfile) {
        userRepository.updateUserProfile(updatedUser) { success ->
            if (success) {
                userProfile = updatedUser
            }
        }
    }

}
