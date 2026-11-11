package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.LocalAtm
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.CardBundleType
import com.example.model.PaymentMethodType
import com.example.ui.theme.Emerald500
import com.example.ui.theme.Emerald600
import com.example.ui.theme.SkyBlue400
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900
import com.example.ui.theme.Slate950
import com.example.ui.theme.TaxiGold
import com.example.ui.theme.TaxiYellow

@Composable
fun StorePointsCardsDialog(
    taxiUnit: String,
    currentPoints: Int,
    onPurchaseConfirmed: (CardBundleType) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    var selectedBundle by remember { mutableStateOf(CardBundleType.ONE_CARD) }
    var selectedPaymentMethod by remember { mutableStateOf(PaymentMethodType.DE_UNA) }
    var showPaymentSuccess by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp)
                .testTag("store_points_cards_dialog"),
            shape = RoundedCornerShape(22.dp),
            color = Slate900,
            border = androidx.compose.foundation.BorderStroke(1.5.dp, TaxiYellow.copy(alpha = 0.7f)),
            tonalElevation = 12.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(18.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top Header Row
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
                                .background(TaxiYellow),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ShoppingCart,
                                contentDescription = null,
                                tint = Slate950,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Tienda de Cartones & Puntos",
                                color = TaxiYellow,
                                fontWeight = FontWeight.Black,
                                fontSize = 16.sp
                            )
                            Text(
                                text = "Precios en Dólares Americanos ($ USD)",
                                color = Emerald500,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(30.dp)
                            .testTag("close_store_dialog")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar",
                            tint = Slate400
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Section 1: Selector de Paquetes de Cartones (1, 2, 3, 4 cartones)
                Text(
                    text = "1. Selecciona tu Paquete de Cartones de Bingo:",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CardBundleType.values().forEach { bundle ->
                        BundleSelectionCard(
                            bundle = bundle,
                            isSelected = selectedBundle == bundle,
                            onClick = {
                                selectedBundle = bundle
                                showPaymentSuccess = false
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Section 2: Métodos de Pago Locales (Banco Pichincha y DeUna!)
                Text(
                    text = "2. Selecciona la Forma de Depósito o Pago:",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Tabs / Buttons para Pichincha Transferencia, Depósito y DeUna!
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    PaymentTabButton(
                        title = "DeUna!",
                        subtitle = "QR",
                        icon = Icons.Default.QrCode2,
                        isSelected = selectedPaymentMethod == PaymentMethodType.DE_UNA,
                        accentColor = Emerald500,
                        onClick = { selectedPaymentMethod = PaymentMethodType.DE_UNA },
                        modifier = Modifier.weight(1f)
                    )

                    PaymentTabButton(
                        title = "Pichincha",
                        subtitle = "Transf.",
                        icon = Icons.Default.AccountBalance,
                        isSelected = selectedPaymentMethod == PaymentMethodType.BANCO_PICHINCHA_TRANSFER,
                        accentColor = TaxiYellow,
                        onClick = { selectedPaymentMethod = PaymentMethodType.BANCO_PICHINCHA_TRANSFER },
                        modifier = Modifier.weight(1f)
                    )

                    PaymentTabButton(
                        title = "Depósito",
                        subtitle = "Vecino",
                        icon = Icons.Default.LocalAtm,
                        isSelected = selectedPaymentMethod == PaymentMethodType.BANCO_PICHINCHA_DEPOSIT,
                        accentColor = SkyBlue400,
                        onClick = { selectedPaymentMethod = PaymentMethodType.BANCO_PICHINCHA_DEPOSIT },
                        modifier = Modifier.weight(1f)
                    )
                    
                    PaymentTabButton(
                        title = "Nequi",
                        subtitle = "QR",
                        icon = Icons.Default.QrCode2,
                        isSelected = selectedPaymentMethod == PaymentMethodType.NEQUI,
                        accentColor = Color(0xFFDA007F), // Nequi Magenta
                        onClick = { selectedPaymentMethod = PaymentMethodType.NEQUI },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Section 3: Datos de Cuenta para Depósito / Transferencia / DeUna!
                PaymentDetailsCard(
                    method = selectedPaymentMethod,
                    bundle = selectedBundle,
                    taxiUnit = taxiUnit,
                    context = context
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Confirmation / Action Buttons
                Button(
                    onClick = {
                        onPurchaseConfirmed(selectedBundle)
                        showPaymentSuccess = true
                        Toast.makeText(
                            context,
                            "¡Pago procesado! Se han acreditado ${selectedBundle.cardCount} cartones y +${selectedBundle.pointsAwarded} puntos",
                            Toast.LENGTH_LONG
                        ).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Emerald500),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("confirm_purchase_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Slate950,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Confirmar y Acreditar $${String.format(java.util.Locale.US, "%.2f", selectedBundle.priceUsd)} USD",
                        color = Slate950,
                        fontWeight = FontWeight.Black,
                        fontSize = 14.sp
                    )
                }

                if (showPaymentSuccess) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Slate950,
                        border = androidx.compose.foundation.BorderStroke(1.dp, Emerald500),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = "✅ ¡Transacción confirmada para Unidad #$taxiUnit!",
                                color = Emerald500,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Text(
                                text = "Tus cartones están listos en la pantalla para jugar el Bingo de inmediato.",
                                color = Slate400,
                                fontSize = 11.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BundleSelectionCard(
    bundle: CardBundleType,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .testTag("bundle_card_${bundle.cardCount}"),
        shape = RoundedCornerShape(10.dp),
        color = if (isSelected) Slate800 else Slate950,
        border = androidx.compose.foundation.BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) TaxiYellow else Slate700
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Radio indicator
                Box(
                    modifier = Modifier
                        .size(18.dp)
                        .clip(CircleShape)
                        .background(if (isSelected) TaxiYellow else Color.Transparent)
                        .border(2.dp, if (isSelected) TaxiYellow else Slate400, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    if (isSelected) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(Slate950)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = bundle.title,
                            color = if (isSelected) Color.White else Slate400,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                        if (bundle.badge != null) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = TaxiYellow.copy(alpha = 0.2f),
                                border = androidx.compose.foundation.BorderStroke(1.dp, TaxiYellow.copy(alpha = 0.6f))
                            ) {
                                Text(
                                    text = bundle.badge,
                                    color = TaxiYellow,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                                )
                            }
                        }
                    }
                    Text(
                        text = "+${bundle.pointsAwarded} Puntos para la carrera",
                        color = SkyBlue400,
                        fontSize = 11.sp
                    )
                }
            }

            // Price in USD
            Text(
                text = "$${String.format(java.util.Locale.US, "%.2f", bundle.priceUsd)} USD",
                color = if (isSelected) Emerald500 else Color.White,
                fontWeight = FontWeight.Black,
                fontSize = 15.sp
            )
        }
    }
}

@Composable
private fun PaymentTabButton(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    accentColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .testTag("payment_tab_${title.lowercase()}"),
        shape = RoundedCornerShape(8.dp),
        color = if (isSelected) Slate800 else Slate950,
        border = androidx.compose.foundation.BorderStroke(
            width = if (isSelected) 1.5.dp else 1.dp,
            color = if (isSelected) accentColor else Slate700
        )
    ) {
        Column(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) accentColor else Slate400,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = title,
                color = if (isSelected) Color.White else Slate400,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp
            )
            Text(
                text = subtitle,
                color = if (isSelected) accentColor else Slate400,
                fontSize = 9.sp
            )
        }
    }
}

@Composable
private fun PaymentDetailsCard(
    method: PaymentMethodType,
    bundle: CardBundleType,
    taxiUnit: String,
    context: Context
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Slate950,
        border = androidx.compose.foundation.BorderStroke(1.dp, Slate700),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("payment_details_card")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = method.displayName,
                    color = TaxiYellow,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )

                // Quick copy all account details
                IconButton(
                    onClick = {
                        val textToCopy = if (method == PaymentMethodType.NEQUI) {
                            "Nequi\nNúmero: ${method.phoneNumber}\nMonto: $${String.format(java.util.Locale.US, "%.2f", bundle.priceUsd)} USD\nRef: LEGO Bingo"
                        } else if (method.isDeUna) {
                            "DeUna! Pichincha\nCelular: ${method.phoneNumber}\n${if (method.bankAccountName.isNotBlank()) "Beneficiario: ${method.bankAccountName}\n" else ""}Monto: $${String.format(java.util.Locale.US, "%.2f", bundle.priceUsd)} USD\nReferencia: Taxi Quito #$taxiUnit"
                        } else {
                            "Banco Pichincha\nCuenta: ${method.accountType}\nN°: ${method.accountNumber}\n${if (method.bankAccountName.isNotBlank()) "Beneficiario: ${method.bankAccountName}\n" else ""}${if (method.identificationNumber.isNotBlank()) "Cédula: ${method.identificationNumber}\n" else ""}Monto: $${String.format(java.util.Locale.US, "%.2f", bundle.priceUsd)} USD\nReferencia: Unidad $taxiUnit"
                        }
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        clipboard.setPrimaryClip(ClipData.newPlainText("Datos de Pago", textToCopy))
                        Toast.makeText(context, "Datos de ${method.appOrBankName} copiados", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "Copiar Datos",
                        tint = SkyBlue400,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (method == PaymentMethodType.NEQUI) {
                PaymentRowItem(label = "Red / Aplicación:", value = "Nequi Ecuador")
                PaymentRowItem(label = "Número Nequi:", value = method.phoneNumber)
                PaymentRowItem(label = "Monto a Transferir:", value = "$${String.format(java.util.Locale.US, "%.2f", bundle.priceUsd)} USD")
                
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
                            .size(100.dp)
                            .background(Color.White, RoundedCornerShape(8.dp))
                            .padding(6.dp)
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
                // Info para DeUna! Pichincha
                PaymentRowItem(label = "App / Red:", value = method.appOrBankName)
                PaymentRowItem(label = "Número Celular / DeUna:", value = method.phoneNumber)
                if (method.bankAccountName.isNotBlank()) {
                    PaymentRowItem(label = "Titular:", value = method.bankAccountName)
                }
                PaymentRowItem(label = "Monto Exacto:", value = "$${String.format(java.util.Locale.US, "%.2f", bundle.priceUsd)} USD")
                PaymentRowItem(label = "Concepto / Ref:", value = "Bingo Taxi #$taxiUnit (${bundle.cardCount} cartones)")

                Spacer(modifier = Modifier.height(10.dp))

                // Botón abrir DeUna o enlace directo
                OutlinedButton(
                    onClick = {
                        try {
                            val deUnaUri = Uri.parse("https://deuna.app.link/pay?phone=${method.phoneNumber}&amount=${bundle.priceUsd}")
                            val intent = Intent(Intent.ACTION_VIEW, deUnaUri)
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            Toast.makeText(context, "Abre tu app DeUna! y transfiere a ${method.phoneNumber}", Toast.LENGTH_LONG).show()
                        }
                    },
                    shape = RoundedCornerShape(8.dp),
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
                    Text(text = "Abrir DeUna! / Pagar desde Móvil", color = Emerald500, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            } else {
                // Info para Depósito o Transferencia Banco Pichincha
                PaymentRowItem(label = "Institución:", value = method.appOrBankName)
                PaymentRowItem(label = "Tipo de Cuenta:", value = method.accountType)
                PaymentRowItem(label = "N° de Cuenta:", value = method.accountNumber)
                if (method.bankAccountName.isNotBlank()) {
                    PaymentRowItem(label = "Beneficiario:", value = method.bankAccountName)
                }
                if (method.identificationNumber.isNotBlank()) {
                    PaymentRowItem(label = "Cédula:", value = method.identificationNumber)
                }
                PaymentRowItem(label = "Monto a Transferir:", value = "$${String.format(java.util.Locale.US, "%.2f", bundle.priceUsd)} USD")
                PaymentRowItem(label = "Referencia / Concepto:", value = "Taxi Quito Unidad #$taxiUnit")
            }
        }
    }
}

@Composable
private fun PaymentRowItem(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = Slate400, fontSize = 11.sp)
        Text(text = value, color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 11.sp)
    }
}
