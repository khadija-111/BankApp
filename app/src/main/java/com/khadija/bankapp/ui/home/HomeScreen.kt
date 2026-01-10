package com.khadija.bankapp.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.khadija.bankapp.R
import com.khadija.bankapp.ui.auth.AuthViewModel
import java.text.SimpleDateFormat
import java.util.*

/* 🎨 ألوان قريبة لـ CIH */
private object CIHColors {
    val Primary = Color(0xFFF57C00)
    val PrimaryLight = Color(0xFFFFA726)
    val Surface = Color.White
    val TextPrimary = Color(0xFF212121)
    val TextSecondary = Color(0xFF757575)
}

@Composable
fun HomeScreen(
    authViewModel: AuthViewModel,
    onProfile: () -> Unit,
    onAccount: () -> Unit,
    onTransfer: () -> Unit,
    onHistory: () -> Unit,
    onLogout: () -> Unit
) {
    LaunchedEffect(Unit) { authViewModel.loadBalance() }
    val balance = authViewModel.balance

    Box(modifier = Modifier.fillMaxSize()) {

        // 🖼️ BACKGROUND IMAGE
        Image(
            painter = painterResource(id = R.drawable.homeimg),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // 🌑 Overlay خفيف
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.08f))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {

            /* 🔶 HEADER */
            ModernHeader()

            /* 💳 BALANCE CARD */
            Card(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .offset(y = (-30).dp)
                    .fillMaxWidth()
                    .height(140.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = CIHColors.Surface),
                elevation = CardDefaults.cardElevation(10.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Solde actuel",
                            color = CIHColors.TextSecondary,
                            fontSize = 14.sp
                        )
                        Text(
                            text = balance?.let { "$it DH" } ?: "Chargement...",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold,
                            color = CIHColors.TextPrimary
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.AccountBalance,
                        contentDescription = null,
                        tint = CIHColors.Primary,
                        modifier = Modifier.size(44.dp)
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            /* 🟧 GRID */
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    BigActionCard(
                        title = "Mon Profil",
                        icon = Icons.Default.Person,
                        onClick = onProfile,
                        modifier = Modifier.weight(1f)
                    )
                    BigActionCard(
                        title = "Mon Compte",
                        icon = Icons.Default.AccountBalanceWallet,
                        onClick = onAccount,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    BigActionCard(
                        title = "Virement",
                        icon = Icons.Default.Send,
                        onClick = onTransfer,
                        modifier = Modifier.weight(1f)
                    )
                    BigActionCard(
                        title = "Historique",
                        icon = Icons.Default.History,
                        onClick = onHistory,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(Modifier.height(30.dp))

            /* 🚪 LOGOUT */
            Card(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
                    .clickable { onLogout() },
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE)),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Row(
                    modifier = Modifier.padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Logout,
                        contentDescription = null,
                        tint = Color.Red
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(
                        text = "Se déconnecter",
                        color = Color.Red,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(Modifier.height(30.dp))
        }
    }
}

/* 🔶 HEADER */
@Composable
private fun ModernHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(
                brush = Brush.verticalGradient(
                    listOf(CIHColors.Primary, CIHColors.PrimaryLight)
                ),
                shape = RoundedCornerShape(
                    bottomStart = 32.dp,
                    bottomEnd = 32.dp
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Welcome to your account 👋",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = getCurrentDate(),
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.85f)
            )
        }
    }
}

/* 🧱 CARD */
@Composable
private fun BigActionCard(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(170.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = CIHColors.Surface),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(18.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(CIHColors.Primary.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = CIHColors.Primary,
                    modifier = Modifier.size(28.dp)
                )
            }
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = CIHColors.TextPrimary
            )
        }
    }
}

/* 📅 DATE */
private fun getCurrentDate(): String {
    val sdf = SimpleDateFormat("EEEE, d MMMM", Locale.getDefault())
    return sdf.format(Date())
}
