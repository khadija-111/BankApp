package com.khadija.bankapp.ui.account

import androidx.compose.runtime.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.khadija.bankapp.R

// 🎨 Account Color Palette
private object AccountColors {
    val CardGradientStart = Color(0xFF1E3A8A)
    val CardGradientEnd = Color(0xFF3B82F6)
    val AccentGold = Color(0xFFFBBF24)
    val AccentGreen = Color(0xFF34D399)
}

@Composable
fun AccountScreen(
    navController: NavHostController,
    viewModel: AccountViewModel = viewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.loadAccount()
    }

    Box(modifier = Modifier.fillMaxSize()) {

        // 🖼️ Background
        Image(
            painter = painterResource(id = R.drawable.account_bg),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // 🌑 Overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color.Black.copy(alpha = 0.6f),
                            Color.Black.copy(alpha = 0.85f)
                        )
                    )
                )
        )

        // 🔙 Back Arrow
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopStart)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color.White
            )
        }

        // 📦 Content
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when {
                viewModel.isLoading -> {
                    CircularProgressIndicator(color = AccountColors.AccentGold)
                }

                viewModel.bankAccount != null -> {
                    val acc = viewModel.bankAccount!!
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        ModernBankCard(
                            iban = acc.iban,
                            accountType = acc.accountType,
                            balance = acc.balance
                        )

                        // ✅ غير Make Transaction
                        Button(
                            onClick = {
                                navController.navigate("transaction/${acc.balance}")
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp),
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AccountColors.AccentGold
                            )
                        ) {
                            Icon(Icons.Default.Send, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text(
                                "Make Transaction",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                else -> {
                    Text(
                        "No account found",
                        color = Color.White
                    )
                }
            }
        }
    }
}

// 💳 Bank Card
@Composable
private fun ModernBankCard(
    iban: String,
    accountType: String,
    balance: Double
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .shadow(20.dp, RoundedCornerShape(28.dp)),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        listOf(
                            AccountColors.CardGradientStart,
                            AccountColors.CardGradientEnd
                        )
                    )
                )
                .padding(24.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    "Bank Account",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    "IBAN\n$iban",
                    color = Color.White,
                    fontSize = 14.sp
                )

                Text(
                    "$balance MAD",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
