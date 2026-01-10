package com.khadija.bankapp.ui.navigation

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.khadija.bankapp.R
import com.khadija.bankapp.ui.auth.AuthViewModel
import com.khadija.bankapp.ui.auth.LoginScreen
import com.khadija.bankapp.ui.auth.RegisterScreen
import com.khadija.bankapp.ui.home.HomeScreen
import com.khadija.bankapp.ui.profile.ProfileScreen
import com.khadija.bankapp.ui.account.AccountScreen
import com.khadija.bankapp.ui.transaction.TransactionScreen
import com.khadija.bankapp.ui.transaction.TransactionHistoryScreen
import com.khadija.bankapp.ui.transfer.TransferScreen

@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController(),
    authViewModel: AuthViewModel = viewModel()
) {

    val startDestination =
        if (authViewModel.isUserLoggedIn()) "home" else "login"

    val context = LocalContext.current

    // 🔹 Google Sign In config
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(context.getString(R.string.default_web_client_id))
        .requestEmail()
        .build()

    val googleSignInClient = GoogleSignIn.getClient(context, gso)

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                val idToken = account.idToken
                if (idToken != null) {
                    authViewModel.loginWithGoogle(idToken) {
                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        composable("login") {
            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onGoToRegister = {
                    navController.navigate("register")
                },
                onGoogleClick = {
                    launcher.launch(googleSignInClient.signInIntent)
                }
            )
        }

        composable("register") {
            RegisterScreen(
                viewModel = authViewModel,
                onRegisterSuccess = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onBackToLogin = {
                    navController.popBackStack()
                },
                onGoogleClick = {
                    launcher.launch(googleSignInClient.signInIntent)
                }
            )
        }

        composable("home") {
            HomeScreen(
                authViewModel = authViewModel,
                onProfile = { navController.navigate("profile") },
                onAccount = { navController.navigate("account") },
                onTransfer = { navController.navigate("transfer") },
                onHistory = { navController.navigate("transactionHistory") },
                onLogout = {
                    authViewModel.logout {
                        navController.navigate("login") {
                            popUpTo("home") { inclusive = true }
                        }
                    }
                }
            )
        }

        composable("profile") {
            ProfileScreen(onBack = { navController.popBackStack() })
        }

        composable("account") {
            AccountScreen(navController)
        }

        composable("transaction/{balance}") { backStackEntry ->
            val balance =
                backStackEntry.arguments?.getString("balance")?.toDoubleOrNull() ?: 0.0
            TransactionScreen(
                balance = balance,
                onDone = { navController.popBackStack() }
            )
        }

        composable("transactionHistory") {
            TransactionHistoryScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable("transfer") {
            TransferScreen(
                onBack = { navController.popBackStack() },
                onDone = { navController.popBackStack() }
            )
        }
    }
}
