package com.example.ui.components

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.Emerald500
import com.example.ui.theme.GoldenGlow
import com.example.ui.theme.LegoBlue
import com.example.ui.theme.LegoRed
import com.example.ui.theme.LegoYellow
import com.example.ui.theme.SkyBlue400
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900
import com.example.ui.theme.Slate950
import com.example.util.TimeUtils

@Composable
fun QrVoucherScannerDialog(
    taxiUnit: String,
    onVoucherCaptured: (String, Double) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var isCameraActive by remember { mutableStateOf(false) }
    var scannedVoucherCode by remember { mutableStateOf<String?>(null) }
    var voucherAmount by remember { mutableStateOf(2.00) }
    var captureSuccess by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp)
                .testTag("qr_voucher_scanner_dialog"),
            shape = RoundedCornerShape(20.dp),
            color = Slate900,
            border = androidx.compose.foundation.BorderStroke(1.5.dp, LegoYellow),
            tonalElevation = 12.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(18.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header
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
                                .background(LegoYellow),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.QrCodeScanner,
                                contentDescription = null,
                                tint = Slate950,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Lector QR & Voucher Pichincha",
                                color = LegoYellow,
                                fontWeight = FontWeight.Black,
                                fontSize = 15.sp
                            )
                            Text(
                                text = "Cuenta Única Banco Pichincha: #2214686194",
                                color = Emerald500,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(30.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar",
                            tint = Slate400
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Unique Banco Pichincha Account Info Card
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Slate950,
                    border = androidx.compose.foundation.BorderStroke(1.dp, Slate700),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "🏦 Destino Exclusivo App (Banco Pichincha)",
                            color = GoldenGlow,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Banco: Banco Pichincha (Ecuador)", color = Color.White, fontSize = 11.sp)
                        Text(text = "Tipo: Cuenta de Ahorros", color = Color.White, fontSize = 11.sp)
                        Text(text = "Cuenta N°: 2214686194", color = SkyBlue400, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        Text(text = "Unidad Asociada: #$taxiUnit Quito", color = Slate400, fontSize = 10.sp)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Camera Scanner Box / Preview Simulation
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = Slate950,
                    border = androidx.compose.foundation.BorderStroke(2.dp, if (isCameraActive) Emerald500 else Slate700)
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCameraActive) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Default.CameraAlt,
                                    contentDescription = "Cámara Activa",
                                    tint = Emerald500,
                                    modifier = Modifier.size(48.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "🔴 Cámara Activada - Leyendo Código QR...",
                                    color = Emerald500,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                                Text(
                                    text = "Apunta al código QR de cobro o pago Pichincha",
                                    color = Slate400,
                                    fontSize = 10.sp
                                )
                            }
                        } else {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Default.QrCodeScanner,
                                    contentDescription = "Escanear QR",
                                    tint = LegoYellow,
                                    modifier = Modifier.size(48.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Lector de QR y Voucher Inactivo",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                                Text(
                                    text = "Presiona Activar Cámara para escanear",
                                    color = Slate400,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Action Buttons: Activate Camera / Simulate Scan
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            isCameraActive = true
                            Toast.makeText(context, "Cámara activada con lector automático de QR", Toast.LENGTH_SHORT).show()
                        },
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, LegoYellow),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(imageVector = Icons.Default.CameraAlt, contentDescription = null, tint = LegoYellow, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "Activar Cámara", color = LegoYellow, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = {
                            isCameraActive = true
                            val ref = "VOUCHER-PICHINCHA-${(1000..9999).random()}"
                            scannedVoucherCode = ref
                            captureSuccess = true
                            Toast.makeText(context, "¡QR escaneado y voucher almacenado con éxito!", Toast.LENGTH_LONG).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Emerald500),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(imageVector = Icons.Default.Receipt, contentDescription = null, tint = Slate950, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "Escanear & Capturar", color = Slate950, fontSize = 11.sp, fontWeight = FontWeight.Black)
                    }
                }

                if (captureSuccess && scannedVoucherCode != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Slate950,
                        border = androidx.compose.foundation.BorderStroke(1.dp, Emerald500),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = Emerald500, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = "Voucher Capturado y Almacenado:", color = Emerald500, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Referencia: $scannedVoucherCode", color = Color.White, fontSize = 11.sp)
                            Text(text = "Destino: Banco Pichincha #2214686194 (Cuenta de Ahorros)", color = Slate400, fontSize = 10.sp)
                            Text(text = "Fecha: ${TimeUtils.getCurrentQuitoTime()}", color = Slate400, fontSize = 10.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            onVoucherCaptured(scannedVoucherCode!!, voucherAmount)
                            onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = LegoYellow),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Confirmar y Enviar al Panel de Administración", color = Slate950, fontWeight = FontWeight.Black, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}
