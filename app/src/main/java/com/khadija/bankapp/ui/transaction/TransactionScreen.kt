package com.khadija.bankapp.ui.transaction

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.khadija.bankapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionScreen(
    balance: Double,
    onDone: () -> Unit,
    viewModel: TransactionViewModel = viewModel()
) {
    var amount by remember { mutableStateOf("") }
    var localError by remember { mutableStateOf<String?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {

        // 🖼️ Background Image
        Image(
            painter = painterResource(id = R.drawable.transaction_bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // 🌫️ Dark overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color.Black.copy(alpha = 0.55f),
                            Color.Black.copy(alpha = 0.75f)
                        )
                    )
                )
        )

        // 💳 Content Card
        Card(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(20.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White.copy(alpha = 0.95f)
            ),
            elevation = CardDefaults.cardElevation(10.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                // 🔹 Title
                Text(
                    text = "Transaction",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                // 💰 Balance
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AttachMoney,
                        contentDescription = null,
                        tint = Color(0xFF10B981)
                    )
                    Text(
                        text = "Balance: $balance MAD",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                // ✏️ Amount field
                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Amount") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp)
                )

                // ➕ Deposit
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    onClick = {
                        val value = amount.toDoubleOrNull()
                        if (value == null || value <= 0) {
                            localError = "Enter a valid amount"
                        } else {
                            localError = null
                            viewModel.deposit(balance, value, onDone)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF3B82F6)
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(Icons.Default.AddCircle, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Deposit")
                }

                // ➖ Withdraw
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    onClick = {
                        val value = amount.toDoubleOrNull()
                        if (value == null || value <= 0) {
                            localError = "Enter a valid amount"
                        } else {
                            localError = null
                            viewModel.withdraw(balance, value, onDone)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFEF4444)
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(Icons.Default.RemoveCircle, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Withdraw")
                }

                // ❌ Errors
                localError?.let {
                    Text(it, color = MaterialTheme.colorScheme.error)
                }

                viewModel.error?.let {
                    Text(it, color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}
