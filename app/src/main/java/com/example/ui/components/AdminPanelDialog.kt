package com.example.ui.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.AppUserRecord
import com.example.model.BingoLotteryWinnerRecord
import com.example.model.CashDepositTransaction
import com.example.model.CouponRecord
import com.example.model.StorePurchaseRecord
import com.example.ui.theme.Emerald500
import com.example.ui.theme.GoldenGlow
import com.example.ui.theme.GoldenSparkle
import com.example.ui.theme.LegoBlue
import com.example.ui.theme.LegoRed
import com.example.ui.theme.LegoYellow
import com.example.ui.theme.PowerballRed
import com.example.ui.theme.SkyBlue400
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900
import com.example.ui.theme.Slate950

@Composable
fun AdminPanelDialog(
    taxiUnit: String,
    driverName: String,
    callerSpeedSeconds: Int,
    lotteryIntervalSeconds: Int,
    pointsAwarded: Int,
    // Financial algorithm state & records
    registeredUsers: List<AppUserRecord>,
    totalAppLogins: Int,
    cashDeposits: List<CashDepositTransaction>,
    storePurchases: List<StorePurchaseRecord>,
    couponList: List<CouponRecord>,
    winnersHistory: List<BingoLotteryWinnerRecord>,
    accumulatedMoneyPoolUsd: Double,
    onSaveTaxiConfig: (unit: String, driver: String, speed: Int, points: Int) -> Unit,
    onToggleUserStatus: (userId: String) -> Unit,
    onRecordManualDeposit: (amount: Double, method: String, email: String) -> Unit,
    onRecordWinnerPayout: (winnerName: String, gameType: String, investedUsd: Double, multiplier: Double) -> Unit,
    onAddNewCoupon: (code: String, discount: Int, cards: Int, points: Int) -> Unit,
    onForceLotteryReset: () -> Unit,
    onResetAllCards: () -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    // Security PIN state (Default PIN: 7575 or 1234)
    var isUnlocked by remember { mutableStateOf(false) }
    var enteredPin by remember { mutableStateOf("") }
    var pinError by remember { mutableStateOf(false) }
    var showPin by remember { mutableStateOf(false) }

    // Admin Tabs:
    // 0 = Dashboard Financiero & Algoritmo de Ganadores
    // 1 = Control de Usuarios & Registros
    // 2 = Historial de Ingresos & Depósitos
    // 3 = Historial de Compras en Tienda
    // 4 = Gestión de Cupones
    // 5 = Ajustes Técnicos
    var selectedTab by remember { mutableIntStateOf(0) }

    // Dialog state for calculator / simulator
    var simInvestedInput by remember { mutableStateOf("5.00") }
    var simMultiplier by remember { mutableFloatStateOf(8.5f) }

    // New Coupon inputs
    var couponCodeInput by remember { mutableStateOf("") }
    var couponDiscountInput by remember { mutableStateOf("20") }
    var couponCardsInput by remember { mutableStateOf("2") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .testTag("admin_panel_dialog"),
            shape = RoundedCornerShape(22.dp),
            color = Slate900,
            border = androidx.compose.foundation.BorderStroke(1.5.dp, GoldenGlow),
            tonalElevation = 14.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header with Shield / Financial Icon
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(if (isUnlocked) GoldenGlow else LegoRed),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isUnlocked) Icons.Default.MonetizationOn else Icons.Default.Lock,
                                contentDescription = null,
                                tint = Slate950,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Panel Administrativo & Financiero",
                                color = LegoYellow,
                                fontWeight = FontWeight.Black,
                                fontSize = 15.sp
                            )
                            Text(
                                text = if (isUnlocked) "Algoritmo Activo • Control de Caja" else "Área Privada de Gerencia",
                                color = if (isUnlocked) Emerald500 else Slate400,
                                fontSize = 11.sp
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(28.dp)
                            .testTag("close_admin_dialog")
                    ) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Cerrar", tint = Slate400)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Phase 1: PIN Gate if locked
                if (!isUnlocked) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Slate950),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Ingresa el PIN de Acceso Financiero",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "Acceso reservado para auditoría, control de depósitos, algoritmo de premios y base de usuarios.",
                                color = Slate400,
                                fontSize = 11.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            OutlinedTextField(
                                value = enteredPin,
                                onValueChange = {
                                    if (it.length <= 6) {
                                        enteredPin = it
                                        pinError = false
                                    }
                                },
                                label = { Text("PIN de Seguridad") },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                                visualTransformation = if (showPin) VisualTransformation.None else PasswordVisualTransformation(),
                                trailingIcon = {
                                    IconButton(onClick = { showPin = !showPin }) {
                                        Icon(
                                            imageVector = if (showPin) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                            contentDescription = null,
                                            tint = Slate400
                                        )
                                    }
                                },
                                isError = pinError,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = LegoYellow,
                                    unfocusedBorderColor = Slate700,
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("admin_pin_input")
                            )

                            if (pinError) {
                                Text(
                                    text = "PIN incorrecto. Intenta nuevamente.",
                                    color = PowerballRed,
                                    fontSize = 11.sp,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Button(
                                onClick = {
                                    if (enteredPin == "7575" || enteredPin == "1234" || enteredPin == "0000") {
                                        isUnlocked = true
                                        pinError = false
                                    } else {
                                        pinError = true
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = GoldenGlow),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(44.dp)
                                    .testTag("unlock_admin_button")
                            ) {
                                Text(
                                    text = "Desbloquear Panel Financiero",
                                    color = Slate950,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                } else {
                    // Phase 2: Unlocked Full Financial & Management Tabs
                    TabRow(
                        selectedTabIndex = selectedTab,
                        containerColor = Slate950,
                        contentColor = GoldenGlow,
                        indicator = { tabPositions ->
                            TabRowDefaults.SecondaryIndicator(
                                modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                                color = GoldenGlow,
                                height = 3.dp
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                    ) {
                        Tab(
                            selected = selectedTab == 0,
                            onClick = { selectedTab = 0 },
                            text = { Text("Finanzas", fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                        )
                        Tab(
                            selected = selectedTab == 1,
                            onClick = { selectedTab = 1 },
                            text = { Text("Usuarios", fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                        )
                        Tab(
                            selected = selectedTab == 2,
                            onClick = { selectedTab = 2 },
                            text = { Text("Depósitos", fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                        )
                        Tab(
                            selected = selectedTab == 3,
                            onClick = { selectedTab = 3 },
                            text = { Text("Ventas", fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                        )
                        Tab(
                            selected = selectedTab == 4,
                            onClick = { selectedTab = 4 },
                            text = { Text("Cupones", fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    when (selectedTab) {
                        // TAB 0: FINANZAS & ALGORITMO DE PREMIOS POR INVERSIÓN
                        0 -> {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                // KPI Card: Total Dinero Acumulado
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = Slate950,
                                    border = androidx.compose.foundation.BorderStroke(1.5.dp, GoldenGlow),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(
                                        modifier = Modifier.padding(14.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = "TOTAL DINERO ACUMULADO",
                                            color = Slate400,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.sp
                                        )
                                        Text(
                                            text = "$${String.format(java.util.Locale.US, "%,.2f", accumulatedMoneyPoolUsd)} USD",
                                            color = Emerald500,
                                            fontWeight = FontWeight.Black,
                                            fontSize = 24.sp
                                        )
                                        Text(
                                            text = "Fondo de Reserva de Pagos (85%): $${String.format(java.util.Locale.US, "%,.2f", accumulatedMoneyPoolUsd * 0.85)} USD",
                                            color = SkyBlue400,
                                            fontSize = 10.sp
                                        )
                                    }
                                }

                                // Interactive Algorithm: Calculadora de Payout según Inversión
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = Slate800,
                                    border = androidx.compose.foundation.BorderStroke(1.dp, LegoBlue),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.Calculate, contentDescription = null, tint = GoldenGlow, modifier = Modifier.size(18.dp))
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = "Algoritmo Financiero: Premio por Inversión",
                                                color = Color.White,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 13.sp
                                            )
                                        }

                                        Spacer(modifier = Modifier.height(8.dp))

                                        OutlinedTextField(
                                            value = simInvestedInput,
                                            onValueChange = { simInvestedInput = it },
                                            label = { Text("Dinero Invertido por Jugador ($ USD)") },
                                            singleLine = true,
                                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                            colors = OutlinedTextFieldDefaults.colors(
                                                focusedBorderColor = GoldenGlow,
                                                unfocusedBorderColor = Slate700,
                                                focusedTextColor = Color.White,
                                                unfocusedTextColor = Color.White
                                            ),
                                            modifier = Modifier.fillMaxWidth().testTag("algo_invest_input")
                                        )

                                        Spacer(modifier = Modifier.height(6.dp))

                                        val investedVal = simInvestedInput.toDoubleOrNull() ?: 1.0
                                        val calculatedPrize = (investedVal * simMultiplier).coerceAtMost(accumulatedMoneyPoolUsd * 0.35)

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(text = "Multiplicador de Juego: ${String.format(java.util.Locale.US, "%.1fx", simMultiplier)}", color = Slate400, fontSize = 11.sp)
                                            Text(
                                                text = "Monto a Pagar: $${String.format(java.util.Locale.US, "%.2f", calculatedPrize)} USD",
                                                color = Emerald500,
                                                fontWeight = FontWeight.Black,
                                                fontSize = 13.sp
                                            )
                                        }

                                        Slider(
                                            value = simMultiplier,
                                            onValueChange = { simMultiplier = it },
                                            valueRange = 2f..25f,
                                            colors = SliderDefaults.colors(thumbColor = GoldenGlow, activeTrackColor = GoldenGlow)
                                        )

                                        Button(
                                            onClick = {
                                                onRecordWinnerPayout("Ganador en Sala", "Bingo 75", investedVal, simMultiplier.toDouble())
                                                Toast.makeText(context, "Premio de $${String.format(java.util.Locale.US, "%.2f", calculatedPrize)} USD registrado en historial", Toast.LENGTH_SHORT).show()
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = Emerald500),
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier.fillMaxWidth().testTag("algo_payout_btn")
                                        ) {
                                            Text("Acreditar Premio Según Algoritmo", color = Slate950, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                        }
                                    }
                                }

                                // Historial de Ganadores de Lotería y Bingo
                                Text(
                                    text = "Historial de Ganadores Registrados (${winnersHistory.size})",
                                    color = GoldenGlow,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )

                                winnersHistory.forEach { winner ->
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = Slate950,
                                        modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(10.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Column {
                                                Text(text = "${winner.winnerName} • ${winner.gameType}", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                                Text(text = "Invertido: $${winner.investedUsd} USD • Fecha: ${winner.timestampFormatted}", color = Slate400, fontSize = 10.sp)
                                            }
                                            Text(text = "+$${String.format(java.util.Locale.US, "%.2f", winner.prizeCalculatedUsd)} USD", color = Emerald500, fontWeight = FontWeight.Black, fontSize = 13.sp)
                                        }
                                    }
                                }
                            }
                        }

                        // TAB 1: CONTROL DE USUARIOS
                        1 -> {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                // Estadísticas de Usuarios
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(10.dp),
                                        color = Slate950,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Column(modifier = Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                            Text(text = "USUARIOS", color = Slate400, fontSize = 10.sp)
                                            Text(text = "${registeredUsers.size}", color = GoldenGlow, fontWeight = FontWeight.Black, fontSize = 20.sp)
                                            Text(text = "Registrados", color = Slate400, fontSize = 9.sp)
                                        }
                                    }

                                    Surface(
                                        shape = RoundedCornerShape(10.dp),
                                        color = Slate950,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Column(modifier = Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                            Text(text = "INGRESOS", color = Slate400, fontSize = 10.sp)
                                            Text(text = "$totalAppLogins", color = SkyBlue400, fontWeight = FontWeight.Black, fontSize = 20.sp)
                                            Text(text = "Sesiones App", color = Slate400, fontSize = 9.sp)
                                        }
                                    }
                                }

                                Text(text = "Lista de Usuarios Registrados:", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)

                                registeredUsers.forEach { user ->
                                    Surface(
                                        shape = RoundedCornerShape(10.dp),
                                        color = Slate950,
                                        border = androidx.compose.foundation.BorderStroke(1.dp, if (user.status == "Activo") Slate700 else PowerballRed),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(10.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Column(modifier = Modifier.weight(1f)) {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Text(text = user.name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                                    if (user.isVip) {
                                                        Spacer(modifier = Modifier.width(4.dp))
                                                        Text(text = "VIP", color = GoldenGlow, fontSize = 9.sp, fontWeight = FontWeight.Black)
                                                    }
                                                }
                                                Text(text = user.email, color = Slate400, fontSize = 10.sp)
                                                Text(text = "Ingresos: ${user.loginCount} | Invertido: $${user.totalInvestedUsd} USD", color = SkyBlue400, fontSize = 10.sp)
                                            }

                                            OutlinedButton(
                                                onClick = { onToggleUserStatus(user.id) },
                                                shape = RoundedCornerShape(6.dp),
                                                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                                            ) {
                                                Text(
                                                    text = if (user.status == "Activo") "Suspender" else "Reactivar",
                                                    color = if (user.status == "Activo") PowerballRed else Emerald500,
                                                    fontSize = 10.sp
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        // TAB 2: HISTORIAL DE DEPÓSITOS Y EFECTIVO
                        2 -> {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "Historial de Ingreso de Efectivo (${cashDeposits.size} transacciones)",
                                    color = GoldenGlow,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )

                                cashDeposits.forEach { dep ->
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = Slate950,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(10.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Column {
                                                Text(text = "${dep.method} • ${dep.referenceCode}", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                                Text(text = "${dep.userEmail} • ${dep.timestampFormatted}", color = Slate400, fontSize = 10.sp)
                                            }
                                            Text(text = "+$${String.format(java.util.Locale.US, "%.2f", dep.amountUsd)} USD", color = Emerald500, fontWeight = FontWeight.Black, fontSize = 13.sp)
                                        }
                                    }
                                }
                            }
                        }

                        // TAB 3: HISTORIAL DE COMPRAS EN LA TIENDA
                        3 -> {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "Historial de Ventas de Cartones & Puntos (${storePurchases.size})",
                                    color = GoldenGlow,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )

                                storePurchases.forEach { purchase ->
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = Slate950,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(10.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Column {
                                                Text(text = purchase.itemDescription, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                                Text(text = "${purchase.userEmail} • ${purchase.timestampFormatted}", color = Slate400, fontSize = 10.sp)
                                            }
                                            Text(text = "$${String.format(java.util.Locale.US, "%.2f", purchase.paidUsd)} USD", color = LegoYellow, fontWeight = FontWeight.Black, fontSize = 13.sp)
                                        }
                                    }
                                }
                            }
                        }

                        // TAB 4: HISTORIAL Y CREACIÓN DE CUPONES
                        4 -> {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Text(text = "Historial de Cupones Promocionales:", color = GoldenGlow, fontWeight = FontWeight.Bold, fontSize = 13.sp)

                                couponList.forEach { cp ->
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = Slate950,
                                        border = androidx.compose.foundation.BorderStroke(1.dp, if (cp.isActive) LegoYellow else Slate700),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(10.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Column {
                                                Text(text = "CÓDIGO: ${cp.code}", color = LegoYellow, fontWeight = FontWeight.Black, fontSize = 12.sp)
                                                Text(text = "Descuento: ${cp.discountPercent}% | +${cp.bonusCards} Cartones | Vence: ${cp.expiryDate}", color = Slate400, fontSize = 10.sp)
                                            }
                                            Text(text = "Usos: ${cp.timesUsed}/${cp.maxUses}", color = SkyBlue400, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(text = "Crear Nuevo Cupón:", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    OutlinedTextField(
                                        value = couponCodeInput,
                                        onValueChange = { couponCodeInput = it },
                                        label = { Text("Código") },
                                        singleLine = true,
                                        modifier = Modifier.weight(1.5f).testTag("coupon_code_in")
                                    )
                                    OutlinedTextField(
                                        value = couponDiscountInput,
                                        onValueChange = { couponDiscountInput = it },
                                        label = { Text("% Desc") },
                                        singleLine = true,
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        modifier = Modifier.weight(1f).testTag("coupon_disc_in")
                                    )
                                }

                                Button(
                                    onClick = {
                                        if (couponCodeInput.isNotBlank()) {
                                            onAddNewCoupon(
                                                couponCodeInput,
                                                couponDiscountInput.toIntOrNull() ?: 20,
                                                couponCardsInput.toIntOrNull() ?: 2,
                                                500
                                            )
                                            couponCodeInput = ""
                                            Toast.makeText(context, "Cupón creado exitosamente", Toast.LENGTH_SHORT).show()
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = LegoYellow),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.fillMaxWidth().testTag("add_coupon_btn")
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = null, tint = Slate950)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Agregar Cupón Activo", color = Slate950, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
