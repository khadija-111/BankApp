package com.khadija.bankapp.ui.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.khadija.bankapp.data.auth.AuthRepository
import com.khadija.bankapp.data.user.UserProfile
import com.khadija.bankapp.data.user.UserRepository
import com.khadija.bankapp.data.account.AccountRepository
import com.khadija.bankapp.data.account.BankAccount

class AuthViewModel(
    private val repository: AuthRepository = AuthRepository(),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val userRepository: UserRepository = UserRepository(),
    private val accountRepository: AccountRepository = AccountRepository()
) : ViewModel() {

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var balance by mutableStateOf<Double?>(null)
        private set

    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

   fun loadBalance() {
        val uid = auth.currentUser?.uid ?: return
        accountRepository.getAccount(uid) { account ->
            balance = account?.balance
        }
    }

    private fun generateIban(): String {
        return "MA" + (1000000000000000L..9999999999999999L).random()
    }

    /* ================= REGISTER ================= */

    fun register(
        email: String,
        password: String,
        fullName: String,
        phone: String,
        onSuccess: () -> Unit
    ) {
        isLoading = true
        errorMessage = null

        repository.register(email, password) { success, error ->
            if (!success) {
                isLoading = false
                errorMessage = error
                return@register
            }

            val uid = auth.currentUser?.uid
            if (uid == null) {
                isLoading = false
                errorMessage = "User not logged in after register"
                return@register
            }

            val profile = UserProfile(
                uid = uid,
                email = email,
                fullName = fullName,
                phone = phone
            )

            userRepository.createUserProfile(profile) { profileSuccess ->
                if (!profileSuccess) {
                    isLoading = false
                    errorMessage = "Failed to create user profile"
                    return@createUserProfile
                }

                val account = BankAccount(
                    uid = uid,
                    iban = generateIban(),
                    accountType = "CURRENT",
                    balance = 0.0
                )

                accountRepository.createAccount(account) { accountSuccess ->
                    isLoading = false
                    if (accountSuccess) {
                        onSuccess()
                    } else {
                        errorMessage = "Failed to create bank account"
                    }
                }
            }
        }
    }

    /* ================= LOGIN ================= */

    fun login(
        email: String,
        password: String,
        onSuccess: () -> Unit
    ) {
        isLoading = true
        errorMessage = null

        repository.login(email, password) { success, error ->
            isLoading = false
            if (success) {
                onSuccess()
            } else {
                errorMessage = error
            }
        }
    }

    fun logout(onLogout: () -> Unit) {
        auth.signOut()
        onLogout()
    }

    /* ================= GOOGLE LOGIN ================= */

    fun loginWithGoogle(
        idToken: String,
        onSuccess: () -> Unit
    ) {
        isLoading = true
        errorMessage = null

        repository.loginWithGoogle(idToken) { success, error ->
            if (!success) {
                isLoading = false
                errorMessage = error
                return@loginWithGoogle
            }

            val uid = auth.currentUser?.uid
            if (uid == null) {
                isLoading = false
                errorMessage = "Google login failed"
                return@loginWithGoogle
            }

            // نشوفو واش user قديم ولا جديد
            userRepository.getUserProfile(uid) { existingUser ->
                if (existingUser != null) {
                    // ✅ user قديم
                    isLoading = false
                    onSuccess()
                } else {
                    // 🆕 user جديد من Google
                    val profile = UserProfile(
                        uid = uid,
                        email = auth.currentUser?.email ?: "",
                        fullName = auth.currentUser?.displayName ?: "",
                        phone = ""
                    )

                    userRepository.createUserProfile(profile) {
                        val account = BankAccount(
                            uid = uid,
                            iban = generateIban(),
                            accountType = "CURRENT",
                            balance = 0.0
                        )

                        accountRepository.createAccount(account) {
                            isLoading = false
                            onSuccess()
                        }
                    }
                }
            }
        }
    }
}
