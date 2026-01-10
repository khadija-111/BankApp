package com.khadija.bankapp.ui.auth

// Compose core
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow

// Layout
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape

// Material 3
import androidx.compose.material3.*

// Icons
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Error

// Keyboard


import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.material3.Divider


// icons spécifiques


@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,
    onRegisterSuccess: () -> Unit,
    onBackToLogin: () -> Unit,
    onGoogleClick: () -> Unit
) {
    var fullName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var agree by remember { mutableStateOf(false) }

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

        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(YellowMain.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.PersonAdd,
                contentDescription = "Register",
                tint = YellowMain,
                modifier = Modifier.size(40.dp)
            )
        }

        Spacer(Modifier.height(24.dp))

        Text(
            text = "Create Account",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = DarkGray
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Fill your information below",
            fontSize = 14.sp,
            color = GrayText
        )

        Spacer(Modifier.height(36.dp))

        // Full name
        OutlinedTextField(
            value = fullName,
            onValueChange = { fullName = it },
            label = { Text("Full Name") },
            leadingIcon = {
                Icon(Icons.Default.Person, contentDescription = null)
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

        // Email
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email Address") },
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

        // Phone
        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Mobile Number") },
            leadingIcon = {
                Icon(Icons.Default.Phone, contentDescription = null)
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
            label = { Text("Create Password") },
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

        Spacer(Modifier.height(20.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(LightGrayBg.copy(alpha = 0.5f))
                .padding(12.dp)
        ) {
            Checkbox(
                checked = agree,
                onCheckedChange = { agree = it },
                colors = CheckboxDefaults.colors(checkedColor = YellowMain)
            )
            Spacer(Modifier.width(8.dp))
            Text("I agree with ", color = GrayText)
            Text(
                "Terms & Conditions",
                color = YellowMain,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable { agree = !agree }
            )
        }

        Spacer(Modifier.height(28.dp))

        Button(
            onClick = {
                viewModel.register(
                    email = email,
                    password = password,
                    fullName = fullName,
                    phone = phone,
                    onSuccess = onRegisterSuccess
                )
            },
            enabled = agree && !viewModel.isLoading,
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
                    "Sign Up",
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
                text = "  OR  ",
                color = GrayText,
                fontSize = 12.sp
            )
            Divider(modifier = Modifier.weight(1f))
        }

// 🔹 Sign up with Google (UI فقط)
        // 🔹 Sign up with Google
        OutlinedButton(
            onClick = { onGoogleClick() }, // ✅ هنا الخدمة
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(30.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color.White
            )
        ) {
            Icon(
                imageVector = Icons.Default.Email,
                contentDescription = "Google",
                tint = Color.Red
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = "Sign up with Google",
                fontWeight = FontWeight.SemiBold,
                color = DarkGray
            )
        }



        viewModel.errorMessage?.let { error ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Error, contentDescription = null, tint = Color.Red)
                    Spacer(Modifier.width(8.dp))
                    Text(error, color = Color.Red)
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Already have an account? ", color = GrayText)
            TextButton(onClick = onBackToLogin) {
                Text("Sign In", fontWeight = FontWeight.Bold)
            }
        }

        Spacer(Modifier.height(40.dp))
    }

}
