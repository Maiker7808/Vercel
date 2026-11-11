package com.example.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.LocalAtm
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.R
import com.example.model.CardBundleType
import com.example.model.PaymentMethodType
import com.example.ui.theme.Emerald500
import com.example.ui.theme.Emerald600
import com.example.ui.theme.GoldenGlow
import com.example.ui.theme.GoldenSparkle
import com.example.ui.theme.LegoBlue
import com.example.ui.theme.LegoGreen
import com.example.ui.theme.LegoRed
import com.example.ui.theme.LegoYellow
import com.example.ui.theme.LegoYellowGlow
import com.example.ui.theme.SkyBlue400
import com.example.ui.theme.Slate300
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate850
import com.example.ui.theme.Slate900
import com.example.ui.theme.Slate950
import com.example.util.TimeUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

@Composable
fun LegoStoreScreen(
    currentPoints: Int,
    currentCardsCount: Int,
    taxiUnit: String,
    onPurchaseConfirmed: (CardBundleType, PaymentMethodType) -> Unit,
    onBackClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var selectedBundle by remember { mutableStateOf(CardBundleType.FOUR_CARDS) }
    var selectedPaymentMethod by remember { mutableStateOf(PaymentMethodType.DE_UNA) }
    var isProcessingPayment by remember { mutableStateOf(false) }
    var showReceiptDialog by remember { mutableStateOf(false) }
    var lastReceiptCode by remember { mutableStateOf("") }
    var lastReceiptDate by remember { mutableStateOf("") }

    // Intercept hardware/system back button
    BackHandler(enabled = onBackClick != null) {
        onBackClick?.invoke()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Slate950)
            .testTag("lego_store_screen"),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 640.dp)
                .navigationBarsPadding()
        ) {
            // Top Navigation & Balance Bar
            LegoStoreTopBar(
                currentPoints = currentPoints,
                currentCardsCount = currentCardsCount,
                onBackClick = onBackClick
            )

            // Scrollable Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // LEGO Store Hero Card with 3D Brick Banner Art
                LegoStoreHeroBanner()

                // Section 1: Bingo Card Bundles (Individual, Double, Triple, Quadruple)
                CardPackagesSection(
                    selectedBundle = selectedBundle,
                    onBundleSelected = { selectedBundle = it }
                )

                // Section 2: Mock Payment Methods (Pichincha, Transferencia, Deuna)
                PaymentMethodsSection(
                    selectedMethod = selectedPaymentMethod,
                    onMethodSelected = { selectedPaymentMethod = it }
                )

                // Section 3: Dynamic Payment Details & Account Info
                PaymentAccountDetailsCard(
                    method = selectedPaymentMethod,
                    bundle = selectedBundle,
                    taxiUnit = taxiUnit,
                    context = context
                )

                // Section 4: Checkout CTA Button
                CheckoutActionCard(
                    bundle = selectedBundle,
                    method = selectedPaymentMethod,
                    isProcessing = isProcessingPayment,
                    onProceedPayment = {
                        isProcessingPayment = true
                        coroutineScope.launch {
                            delay(900) // Realistic mock transaction simulation
                            isProcessingPayment = false
                            val refCode = "MOCK-LEGO-${(10000..99999).random()}"
                            lastReceiptCode = refCode
                            lastReceiptDate = "${TimeUtils.getFullQuitoDate()} - ${TimeUtils.getCurrentQuitoTime()}"
                            onPurchaseConfirmed(selectedBundle, selectedPaymentMethod)
                            showReceiptDialog = true
                        }
                    }
                )

                // Store Security & Guarantee Footer
                StoreGuaranteeFooter()

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    // Mock Electronic Receipt Dialog
    if (showReceiptDialog) {
        MockReceiptDialog(
            bundle = selectedBundle,
            method = selectedPaymentMethod,
            referenceCode = lastReceiptCode,
            dateTime = lastReceiptDate,
            taxiUnit = taxiUnit,
            onDismiss = { showReceiptDialog = false },
            onGoToGame = {
                showReceiptDialog = false
                onBackClick?.invoke()
            }
        )
    }
}

@Composable
private fun LegoStoreTopBar(
    currentPoints: Int,
    currentCardsCount: Int,
    onBackClick: (() -> Unit)?
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Slate900,
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            Brush.horizontalGradient(listOf(LegoRed, LegoYellow, LegoBlue))
        ),
        tonalElevation = 6.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (onBackClick != null) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .size(38.dp)
                            .testTag("btn_back_from_store")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver al Bingo",
                            tint = LegoYellow
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                }

                // LEGO Store Mini Emblem
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            Brush.linearGradient(listOf(LegoRed, LegoYellow))
                        )
                        .border(1.dp, LegoYellowGlow, RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = null,
                        tint = Slate950,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "LEGO Store",
                            color = LegoYellow,
                            fontWeight = FontWeight.Black,
                            fontSize = 17.sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = LegoRed
                        ) {
                            Text(
                                text = "OFICIAL",
                                color = Color.White,
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Black,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                            )
                        }
                    }
                    Text(
                        text = "Tienda de Cartones & Puntos",
                        color = Slate400,
                        fontSize = 10.sp
                    )
                }
            }

            // Live User Balances (Points + Active Cards)
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Points pill
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Slate800,
                    border = androidx.compose.foundation.BorderStroke(1.dp, LegoYellow.copy(alpha = 0.5f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = GoldenGlow,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "$currentPoints pts",
                            color = GoldenSparkle,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Active cards pill
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Slate800,
                    border = androidx.compose.foundation.BorderStroke(1.dp, SkyBlue400.copy(alpha = 0.5f))
                ) {
                    Text(
                        text = "$currentCardsCount/4 🎯",
                        color = SkyBlue400,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun LegoStoreHeroBanner() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(12.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        color = Slate900,
        border = androidx.compose.foundation.BorderStroke(
            1.5.dp,
            Brush.linearGradient(listOf(LegoYellow, LegoRed, LegoBlue))
        )
    ) {
        Column {
            // Render the generated HD 3D LEGO Store banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_lego_store_banner_1793836331255),
                    contentDescription = "LEGO Bingo Store Banner",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Gradient Overlay to ensure readable text
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Slate900.copy(alpha = 0.85f), Slate900)
                            )
                        )
                )

                // Top Floating Badge
                Surface(
                    shape = RoundedCornerShape(bottomEnd = 12.dp),
                    color = LegoRed,
                    modifier = Modifier.align(Alignment.TopStart)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bolt,
                            contentDescription = null,
                            tint = LegoYellow,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "COMPRA EN LÍNEA QUITO",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
            }

            // Description block under the hero image
            Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)) {
                Text(
                    text = "¡Adquiere Cartones Oficiales y Multiplica tus Premios!",
                    color = Color.White,
                    fontWeight = FontWeight.Black,
                    fontSize = 15.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Selecciona cartón individual, 2, 3 o 4 cartones completos. Paga en Dólares ($ USD) con DeUna! o Banco Pichincha.",
                    color = Slate300,
                    fontSize = 11.sp,
                    lineHeight = 15.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                // LEGO Brick decorative color dots ribbon
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LegoBrickStud(LegoRed, "B: 1-15")
                    LegoBrickStud(LegoBlue, "I: 16-30")
                    LegoBrickStud(LegoYellow, "N: 31-45")
                    LegoBrickStud(LegoGreen, "G: 46-60")
                    LegoBrickStud(GoldenGlow, "O: 61-75")
                }
            }
        }
    }
}

@Composable
private fun LegoBrickStud(color: Color, label: String) {
    Surface(
        shape = RoundedCornerShape(6.dp),
        color = color.copy(alpha = 0.2f),
        border = androidx.compose.foundation.BorderStroke(1.dp, color)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(7.dp)
                    .clip(CircleShape)
                    .background(color)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = label, color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun CardPackagesSection(
    selectedBundle: CardBundleType,
    onBundleSelected: (CardBundleType) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "1. Selecciona tu Paquete de Cartones",
                color = LegoYellow,
                fontWeight = FontWeight.Black,
                fontSize = 14.sp
            )
            Text(
                text = "Precios en $ USD",
                color = Emerald500,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            CardBundleOptionCard(
                bundle = CardBundleType.ONE_CARD,
                themeColor = LegoRed,
                subtitle = "1 cartón activo en el sorteo en vivo",
                isSelected = selectedBundle == CardBundleType.ONE_CARD,
                onClick = { onBundleSelected(CardBundleType.ONE_CARD) }
            )

            CardBundleOptionCard(
                bundle = CardBundleType.TWO_CARDS,
                themeColor = LegoBlue,
                subtitle = "2 cartones simultáneos con rotación rápida",
                isSelected = selectedBundle == CardBundleType.TWO_CARDS,
                onClick = { onBundleSelected(CardBundleType.TWO_CARDS) }
            )

            CardBundleOptionCard(
                bundle = CardBundleType.THREE_CARDS,
                themeColor = GoldenGlow,
                subtitle = "3 cartones: aumenta tus opciones en cada bola",
                isSelected = selectedBundle == CardBundleType.THREE_CARDS,
                onClick = { onBundleSelected(CardBundleType.THREE_CARDS) }
            )

            CardBundleOptionCard(
                bundle = CardBundleType.FOUR_CARDS,
                themeColor = Emerald500,
                subtitle = "4 cartones full: máxima cobertura total",
                isSelected = selectedBundle == CardBundleType.FOUR_CARDS,
                onClick = { onBundleSelected(CardBundleType.FOUR_CARDS) }
            )
        }
    }
}

@Composable
private fun CardBundleOptionCard(
    bundle: CardBundleType,
    themeColor: Color,
    subtitle: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .testTag("bundle_card_${bundle.cardCount}"),
        shape = RoundedCornerShape(14.dp),
        color = if (isSelected) Slate850 else Slate900,
        border = androidx.compose.foundation.BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) themeColor else Slate700
        ),
        tonalElevation = if (isSelected) 8.dp else 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // Radio selection indicator with LEGO theme
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(if (isSelected) themeColor else Slate800)
                        .border(2.dp, if (isSelected) themeColor else Slate700, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    if (isSelected) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Slate950,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Card Icon Preview Graphic showing number of cards
                CardCountVisualIcon(count = bundle.cardCount, color = themeColor)

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = bundle.title,
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontSize = 14.sp
                        )

                        if (bundle.badge != null) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = themeColor.copy(alpha = 0.25f),
                                border = androidx.compose.foundation.BorderStroke(1.dp, themeColor)
                            ) {
                                Text(
                                    text = bundle.badge,
                                    color = themeColor,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Black,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = subtitle,
                        color = Slate400,
                        fontSize = 11.sp
                    )

                    Spacer(modifier = Modifier.height(3.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = GoldenGlow,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "+${bundle.pointsAwarded} Puntos",
                            color = GoldenSparkle,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Price in USD
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "$${String.format(Locale.US, "%.2f", bundle.priceUsd)}",
                    color = if (isSelected) Emerald500 else Color.White,
                    fontWeight = FontWeight.Black,
                    fontSize = 18.sp
                )
                Text(
                    text = "USD",
                    color = Slate400,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp
                )
            }
        }
    }
}

@Composable
private fun CardCountVisualIcon(count: Int, color: Color) {
    Box(
        modifier = Modifier
            .size(38.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(color.copy(alpha = 0.2f))
            .border(1.dp, color, RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "$count",
                color = color,
                fontWeight = FontWeight.Black,
                fontSize = 16.sp
            )
            Text(
                text = if (count == 1) "CARD" else "CARDS",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 7.sp
            )
        }
    }
}

@Composable
private fun PaymentMethodsSection(
    selectedMethod: PaymentMethodType,
    onMethodSelected: (PaymentMethodType) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "2. Selecciona tu Método de Pago (Mock / Simulado)",
            color = LegoYellow,
            fontWeight = FontWeight.Black,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Row 1: DeUna and Pichincha
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PaymentMethodButton(
                title = "DeUna!",
                tagline = "QR / Móvil",
                icon = Icons.Default.QrCode2,
                themeColor = Emerald500,
                isSelected = selectedMethod == PaymentMethodType.DE_UNA,
                testTag = "payment_btn_deuna",
                onClick = { onMethodSelected(PaymentMethodType.DE_UNA) },
                modifier = Modifier.weight(1f)
            )

            PaymentMethodButton(
                title = "Pichincha",
                tagline = "Depósito",
                icon = Icons.Default.LocalAtm,
                themeColor = LegoYellow,
                isSelected = selectedMethod == PaymentMethodType.BANCO_PICHINCHA_DEPOSIT,
                testTag = "payment_btn_pichincha",
                onClick = { onMethodSelected(PaymentMethodType.BANCO_PICHINCHA_DEPOSIT) },
                modifier = Modifier.weight(1f)
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Row 2: Transferencia and Nequi
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PaymentMethodButton(
                title = "Transferencia",
                tagline = "Directa",
                icon = Icons.Default.AccountBalance,
                themeColor = SkyBlue400,
                isSelected = selectedMethod == PaymentMethodType.BANCO_PICHINCHA_TRANSFER,
                testTag = "payment_btn_transfer",
                onClick = { onMethodSelected(PaymentMethodType.BANCO_PICHINCHA_TRANSFER) },
                modifier = Modifier.weight(1f)
            )

            PaymentMethodButton(
                title = "Nequi",
                tagline = "QR / Saldo",
                icon = Icons.Default.QrCode2,
                themeColor = Color(0xFFDA007F), // Nequi Magenta
                isSelected = selectedMethod == PaymentMethodType.NEQUI,
                testTag = "payment_btn_nequi",
                onClick = { onMethodSelected(PaymentMethodType.NEQUI) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun PaymentMethodButton(
    title: String,
    tagline: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    themeColor: Color,
    isSelected: Boolean,
    testTag: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .testTag(testTag),
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) Slate850 else Slate900,
        border = androidx.compose.foundation.BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) themeColor else Slate700
        ),
        tonalElevation = if (isSelected) 6.dp else 1.dp
    ) {
        Column(
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) themeColor.copy(alpha = 0.2f) else Slate800),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = if (isSelected) themeColor else Slate400,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = title,
                color = if (isSelected) Color.White else Slate300,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )

            Text(
                text = tagline,
                color = if (isSelected) themeColor else Slate400,
                fontSize = 10.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun PaymentAccountDetailsCard(
    method: PaymentMethodType,
    bundle: CardBundleType,
    taxiUnit: String,
    context: Context
) {
    val methodColor = when (method) {
        PaymentMethodType.DE_UNA -> Emerald500
        PaymentMethodType.BANCO_PICHINCHA_DEPOSIT -> LegoYellow
        PaymentMethodType.BANCO_PICHINCHA_TRANSFER -> SkyBlue400
        PaymentMethodType.NEQUI -> Color(0xFFDA007F) // Nequi Magenta
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("payment_details_card"),
        shape = RoundedCornerShape(14.dp),
        color = Slate900,
        border = androidx.compose.foundation.BorderStroke(1.dp, methodColor.copy(alpha = 0.6f))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header with copy button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(methodColor)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = method.displayName,
                        color = methodColor,
                        fontWeight = FontWeight.Black,
                        fontSize = 13.sp
                    )
                }

                // Copy details button
                IconButton(
                    onClick = {
                        val textToCopy = if (method == PaymentMethodType.NEQUI) {
                            "Nequi\nNúmero: ${method.phoneNumber}\nMonto: $${String.format(Locale.US, "%.2f", bundle.priceUsd)} USD\nRef: LEGO Bingo"
                        } else if (method.isDeUna) {
                            "DeUna! Pichincha\nCelular: ${method.phoneNumber}\n${if (method.bankAccountName.isNotBlank()) "Titular: ${method.bankAccountName}\n" else ""}Monto: $${String.format(Locale.US, "%.2f", bundle.priceUsd)} USD\nRef: Taxi Quito #$taxiUnit"
                        } else {
                            "Banco Pichincha\nCuenta: ${method.accountType}\nN°: ${method.accountNumber}\n${if (method.bankAccountName.isNotBlank()) "Titular: ${method.bankAccountName}\n" else ""}${if (method.identificationNumber.isNotBlank()) "Cédula: ${method.identificationNumber}\n" else ""}Monto: $${String.format(Locale.US, "%.2f", bundle.priceUsd)} USD\nRef: Unidad $taxiUnit"
                        }
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        clipboard.setPrimaryClip(ClipData.newPlainText("Datos de Pago", textToCopy))
                        Toast.makeText(context, "Datos de ${method.appOrBankName} copiados", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.size(30.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "Copiar Datos Bancarios",
                        tint = methodColor,
                        modifier = Modifier.size(17.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Bank Details List
            if (method == PaymentMethodType.NEQUI) {
                DetailItemRow(label = "Red / Aplicación:", value = "Nequi Ecuador")
                DetailItemRow(label = "Número Nequi:", value = method.phoneNumber)
                DetailItemRow(label = "Monto a Transferir:", value = "$${String.format(Locale.US, "%.2f", bundle.priceUsd)} USD")
                
                Spacer(modifier = Modifier.height(14.dp))
                
                // Nequi QR Code Generator
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Escanea para pagar con Nequi",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .size(140.dp)
                            .background(Color.White, RoundedCornerShape(8.dp))
                            .padding(8.dp)
                    ) {
                        val qrMatrix = com.example.util.QrCodeGenerator.generateQrMatrix(
                            "nequi:pay?phone=${method.phoneNumber}&amount=${bundle.priceUsd}",
                            size = 32
                        )
                        androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
                            val blockSize = size.width / qrMatrix.size
                            for (x in qrMatrix.indices) {
                                for (y in qrMatrix[x].indices) {
                                    if (qrMatrix[x][y]) {
                                        drawRect(
                                            color = Color.Black,
                                            topLeft = androidx.compose.ui.geometry.Offset(x * blockSize, y * blockSize),
                                            size = androidx.compose.ui.geometry.Size(blockSize, blockSize)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            } else if (method.isDeUna) {
                DetailItemRow(label = "Red / Aplicación:", value = method.appOrBankName)
                DetailItemRow(label = "Número Celular DeUna!:", value = method.phoneNumber)
                if (method.bankAccountName.isNotBlank()) {
                    DetailItemRow(label = "Titular de Cuenta:", value = method.bankAccountName)
                }
                DetailItemRow(label = "Monto a Transferir:", value = "$${String.format(Locale.US, "%.2f", bundle.priceUsd)} USD")
                DetailItemRow(label = "Concepto de Pago:", value = "LEGO Bingo Unidad #$taxiUnit (${bundle.title})")

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedButton(
                    onClick = {
                        try {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://deuna.app.link/pay?phone=${method.phoneNumber}&amount=${bundle.priceUsd}"))
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            Toast.makeText(context, "Simulación: Abriendo DeUna! para transferir a ${method.phoneNumber}", Toast.LENGTH_SHORT).show()
                        }
                    },
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Emerald500),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("open_deuna_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.TouchApp,
                        contentDescription = null,
                        tint = Emerald500,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Abrir App DeUna! / Simular Enlace Móvil",
                        color = Emerald500,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {
                DetailItemRow(label = "Institución Bancaria:", value = method.appOrBankName)
                DetailItemRow(label = "Tipo de Cuenta:", value = method.accountType)
                DetailItemRow(label = "Número de Cuenta:", value = method.accountNumber)
                if (method.bankAccountName.isNotBlank()) {
                    DetailItemRow(label = "Beneficiario:", value = method.bankAccountName)
                }
                if (method.identificationNumber.isNotBlank()) {
                    DetailItemRow(label = "Cédula:", value = method.identificationNumber)
                }
                DetailItemRow(label = "Monto Exacto:", value = "$${String.format(Locale.US, "%.2f", bundle.priceUsd)} USD")
                DetailItemRow(label = "Referencia / Depósito:", value = "Bingo Taxi Unidad #$taxiUnit")
            }
        }
    }
}

@Composable
private fun DetailItemRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, color = Slate400, fontSize = 11.sp)
        Text(
            text = value,
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            fontSize = 11.sp,
            textAlign = TextAlign.End,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Composable
private fun CheckoutActionCard(
    bundle: CardBundleType,
    method: PaymentMethodType,
    isProcessing: Boolean,
    onProceedPayment: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Slate900,
        border = androidx.compose.foundation.BorderStroke(
            1.5.dp,
            Brush.horizontalGradient(listOf(LegoYellow, LegoRed))
        )
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Total a Pagar:",
                        color = Slate400,
                        fontSize = 11.sp
                    )
                    Text(
                        text = "$${String.format(Locale.US, "%.2f", bundle.priceUsd)} USD",
                        color = Emerald500,
                        fontWeight = FontWeight.Black,
                        fontSize = 20.sp
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Slate800,
                    border = androidx.compose.foundation.BorderStroke(1.dp, Slate700)
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = "Acreditación Inmediata",
                            color = GoldenGlow,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "+${bundle.pointsAwarded} pts y ${bundle.cardCount} cartones",
                            color = Slate300,
                            fontSize = 9.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = onProceedPayment,
                enabled = !isProcessing,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Emerald600,
                    disabledContainerColor = Slate700
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("btn_process_mock_payment")
            ) {
                if (isProcessing) {
                    CircularProgressIndicator(
                        color = Slate950,
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Verificando con ${method.displayName}...",
                        color = Slate950,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Slate950,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Pagar $${String.format(Locale.US, "%.2f", bundle.priceUsd)} con ${if (method.isDeUna) "DeUna!" else if (method == PaymentMethodType.BANCO_PICHINCHA_DEPOSIT) "Pichincha" else "Transferencia"}",
                        color = Slate950,
                        fontWeight = FontWeight.Black,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun StoreGuaranteeFooter() {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Slate950,
        border = androidx.compose.foundation.BorderStroke(1.dp, Slate800),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "🛡️ Garantía y Transparencia LEGO Bingo",
                color = LegoYellow,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = "Las compras de cartones y puntos se integran con el algoritmo de premios auditado y balance de caja en tiempo real. Soporte para conductores y pasajeros en Quito.",
                color = Slate400,
                fontSize = 10.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun MockReceiptDialog(
    bundle: CardBundleType,
    method: PaymentMethodType,
    referenceCode: String,
    dateTime: String,
    taxiUnit: String,
    onDismiss: () -> Unit,
    onGoToGame: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .testTag("mock_receipt_dialog"),
            shape = RoundedCornerShape(20.dp),
            color = Slate900,
            border = androidx.compose.foundation.BorderStroke(2.dp, Emerald500),
            tonalElevation = 14.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(18.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Success Badge
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(Emerald500),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Pago Aprobado",
                        tint = Slate950,
                        modifier = Modifier.size(36.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "¡Pago Exitoso Acreditado!",
                    color = Emerald500,
                    fontWeight = FontWeight.Black,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Comprobante Electrónico Mock - LEGO Bingo",
                    color = Slate400,
                    fontSize = 11.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Receipt Breakdown Card
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Slate950,
                    border = androidx.compose.foundation.BorderStroke(1.dp, Slate800),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        ReceiptItem("N° de Referencia:", referenceCode)
                        ReceiptItem("Fecha y Hora:", dateTime)
                        ReceiptItem("Método de Pago:", method.displayName)
                        ReceiptItem("Paquete:", bundle.title)
                        ReceiptItem("Cartones Listos:", "${bundle.cardCount} Cartón(es)")
                        ReceiptItem("Puntos Añadidos:", "+${bundle.pointsAwarded} Pts")
                        ReceiptItem("Unidad de Taxi:", "#$taxiUnit Quito")

                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 8.dp),
                            color = Slate800
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Total Pagado:", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text(
                                text = "$${String.format(Locale.US, "%.2f", bundle.priceUsd)} USD",
                                color = Emerald500,
                                fontWeight = FontWeight.Black,
                                fontSize = 15.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Primary Button to return and play
                Button(
                    onClick = onGoToGame,
                    colors = ButtonDefaults.buttonColors(containerColor = LegoYellow),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("btn_back_to_bingo")
                ) {
                    Text(
                        text = "🎮 ¡Ir a Jugar con mis Cartones!",
                        color = Slate950,
                        fontWeight = FontWeight.Black,
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = onDismiss,
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Slate700),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("btn_close_receipt")
                ) {
                    Text(
                        text = "Seguir en la Tienda",
                        color = Slate300,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun ReceiptItem(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.5.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = Slate400, fontSize = 10.sp)
        Text(text = value, color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 10.sp)
    }
}
