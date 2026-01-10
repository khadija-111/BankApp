package com.khadija.bankapp.ui.transfer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.text.KeyboardOptions
import androidx.lifecycle.viewmodel.compose.viewModel
import com.khadija.bankapp.R

// 🎨 Transfer Color Palette
private object TransferColors {
    val PrimaryBlue = Color(0xFF3B82F6)
    val AccentPurple = Color(0xFF8B5CF6)
    val AccentGreen = Color(0xFF10B981)
    val AccentGold = Color(0xFFFBBF24)
    val TextPrimary = Color(0xFF1E293B)
    val TextSecondary = Color(0xFF64748B)
    val Error = Color(0xFFEF4444)
    val Surface = Color(0xFFFFFFFF)
    val BorderColor = Color(0xFFE2E8F0)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransferScreen(
    onBack: () -> Unit,
    onDone: () -> Unit,
    viewModel: TransferViewModel = viewModel()
) {
    var iban by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }

    // نفس اللوجيك ديال النجاح
    LaunchedEffect(viewModel.success) {
        if (viewModel.success) {
            viewModel.resetState()
            onDone()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        // 🖼️ Background
        Image(
            painter = painterResource(id = R.drawable.transaction_bg),
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

        // 🔙 HEADER (Back)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
            Text(
                text = "Back",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }

        // 📦 Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Icon
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .shadow(16.dp, CircleShape)
                    .clip(CircleShape)
                    .background(TransferColors.AccentGold),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.SwapHoriz,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(24.dp, RoundedCornerShape(32.dp)),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.97f)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(28.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Transfer Money",
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Send money securely",
                            fontSize = 13.sp,
                            color = Color.Gray
                        )
                    }

                    ModernTransferTextField(
                        value = iban,
                        onValueChange = { iban = it },
                        label = "Recipient IBAN",
                        placeholder = "MA00 0000 0000 0000",
                        icon = Icons.Default.AccountBalance,
                        iconColor = TransferColors.PrimaryBlue
                    )

                    ModernTransferTextField(
                        value = amount,
                        onValueChange = { amount = it },
                        label = "Amount",
                        placeholder = "0.00 MAD",
                        icon = Icons.Default.AttachMoney,
                        iconColor = TransferColors.AccentGreen,
                        keyboardType = KeyboardType.Number
                    )

                    ModernTransferButton(
                        isLoading = viewModel.isLoading,
                        onClick = {
                            val amountValue = amount.toDoubleOrNull()
                            if (iban.isBlank() || amountValue == null || amountValue <= 0) {
                                viewModel.showError("Please enter valid IBAN and amount")
                            } else {
                                viewModel.transferMoney(
                                    receiverIban = iban,
                                    amount = amountValue
                                )
                            }
                        }
                    )

                    viewModel.error?.let {
                        ModernErrorMessage(message = it)
                    }

                    ModernInfoBox()
                }
            }
        }
    }
}



// 📝 Modern TextField Component
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ModernTransferTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    icon: ImageVector,
    iconColor: Color,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(iconColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(12.dp)
                )
            }
            Text(
                text = label,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = TransferColors.TextPrimary
            )
        }

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    text = placeholder,
                    color = TransferColors.TextSecondary.copy(alpha = 0.5f)
                )
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = iconColor,
                unfocusedBorderColor = TransferColors.BorderColor,
                focusedContainerColor = iconColor.copy(alpha = 0.03f),
                unfocusedContainerColor = TransferColors.Surface,
                cursorColor = iconColor
            ),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType)
        )
    }
}

// 🚀 Modern Transfer Button
@Composable
private fun ModernTransferButton(
    onClick: () -> Unit,
    isLoading: Boolean
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .shadow(
                elevation = if (isLoading) 0.dp else 12.dp,
                shape = RoundedCornerShape(18.dp),
                spotColor = TransferColors.PrimaryBlue.copy(alpha = 0.3f)
            ),
        enabled = !isLoading,
        shape = RoundedCornerShape(18.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = TransferColors.PrimaryBlue,
            disabledContainerColor = TransferColors.TextSecondary.copy(alpha = 0.3f)
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp
        )
    ) {
        if (isLoading) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.5.dp,
                    color = Color.White
                )
                Text(
                    text = "Processing...",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        } else {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = null,
                    modifier = Modifier.size(22.dp)
                )
                Text(
                    text = "Transfer Now",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// ❌ Modern Error Message
@Composable
private fun ModernErrorMessage(message: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = TransferColors.Error.copy(alpha = 0.1f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.ErrorOutline,
                contentDescription = null,
                tint = TransferColors.Error,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = message,
                color = TransferColors.Error,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

// ℹ️ Info Box
@Composable
private fun ModernInfoBox() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = TransferColors.PrimaryBlue.copy(alpha = 0.08f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null,
                tint = TransferColors.PrimaryBlue,
                modifier = Modifier.size(18.dp)
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Secure Transfer",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TransferColors.PrimaryBlue
                )
                Text(
                    text = "All transactions are encrypted and processed securely",
                    fontSize = 11.sp,
                    color = TransferColors.TextSecondary,
                    lineHeight = 16.sp
                )
            }
        }
    }
}