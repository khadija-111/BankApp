package com.khadija.bankapp.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onLoginSuccess: () -> Unit,
    onGoToRegister: () -> Unit,
    onGoogleClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val YellowMain = Color(0xFFFFC107)
    val LightGrayBg = Color(0xFFF2F2F2)
    val GrayText = Color(0xFF666666)
    val DarkGray = Color(0xFF333333)
    val cornerRadius = 16.dp

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(Modifier.height(40.dp))

        // Icon
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(YellowMain.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Login,
                contentDescription = "Login",
                tint = YellowMain,
                modifier = Modifier.size(40.dp)
            )
        }

        Spacer(Modifier.height(24.dp))

        Text(
            text = "Welcome Back",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = DarkGray
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Login to your account",
            fontSize = 14.sp,
            color = GrayText
        )

        Spacer(Modifier.height(36.dp))

        // Email
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            leadingIcon = {
                Icon(Icons.Default.Email, contentDescription = null)
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(cornerRadius),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = LightGrayBg,
                focusedContainerColor = LightGrayBg,
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = YellowMain
            )
        )

        Spacer(Modifier.height(16.dp))

        // Password
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            leadingIcon = {
                Icon(Icons.Default.Lock, contentDescription = null)
            },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(cornerRadius),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = LightGrayBg,
                focusedContainerColor = LightGrayBg,
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = YellowMain
            )
        )

        Spacer(Modifier.height(28.dp))

        // Button
        Button(
            onClick = {
                viewModel.login(email, password) {
                    onLoginSuccess()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .shadow(8.dp, RoundedCornerShape(30.dp)),
            shape = RoundedCornerShape(30.dp),
            colors = ButtonDefaults.buttonColors(containerColor = YellowMain)
        ) {
            if (viewModel.isLoading) {
                CircularProgressIndicator(
                    color = Color.Black,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(20.dp)
                )
            } else {
                Text(
                    text = "Login",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(Modifier.height(12.dp))
        // 🔹 Divider
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Divider(modifier = Modifier.weight(1f))
            Text(
                text = " OR ",
                color = GrayText,
                fontSize = 12.sp
            )
            Divider(modifier = Modifier.weight(1f))
        }

// 🔹 Login with Google
        OutlinedButton(
            onClick = { onGoogleClick() }, // ✅ هنا الخدمة
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(30.dp)
        ) {
            Icon(Icons.Default.Email, contentDescription = "Google", tint = Color.Red)
            Spacer(Modifier.width(8.dp))
            Text("Login with Google")
        }


        // Error
        viewModel.errorMessage?.let { error ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFFEBEE)
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Error,
                        contentDescription = null,
                        tint = Color.Red
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(error, color = Color.Red)
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        // Go to register
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Don't have an account? ", color = GrayText)
            Text(
                text = "Register",
                color = YellowMain,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { onGoToRegister() }
            )
        }

        Spacer(Modifier.height(40.dp))
    }
}
