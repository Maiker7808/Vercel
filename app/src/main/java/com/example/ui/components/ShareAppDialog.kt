package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.LegoBlue
import com.example.ui.theme.LegoRed
import com.example.ui.theme.LegoYellow
import com.example.ui.theme.SkyBlue400
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900
import com.example.ui.theme.Slate950
import com.example.util.QrCodeGenerator

@Composable
fun ShareAppDialog(
    taxiUnit: String,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    val appShareUrl = "https://ais-pre-jx7sep6qzcnl5by22algfc-10120980568.us-east1.run.app"
    val apkDownloadUrl = "$appShareUrl/download/lego_bingo_v1.apk"
    val shareMessage = "🎮 ¡Juega a LEGO Bingo HD y sigue la Lotería de EE.UU. en vivo con premios increíbles! Juega o descarga el APK aquí: $appShareUrl"

    val qrMatrix = remember(appShareUrl) {
        QrCodeGenerator.generateQrMatrix(appShareUrl, size = 29)
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp)
                .testTag("share_app_dialog"),
            shape = RoundedCornerShape(20.dp),
            color = Slate900,
            border = androidx.compose.foundation.BorderStroke(1.5.dp, LegoYellow),
            tonalElevation = 10.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(18.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(LegoYellow),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = null,
                                tint = Slate950,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Compartir LEGO Bingo",
                                color = LegoYellow,
                                fontWeight = FontWeight.Black,
                                fontSize = 16.sp
                            )
                            Text(
                                text = "Redes Sociales, QR & APK",
                                color = Slate400,
                                fontSize = 11.sp
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(30.dp)
                            .testTag("close_share_dialog")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar",
                            tint = Slate400
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Social Networks Buttons Row (TikTok, Facebook, Instagram, YouTube, Telegram)
                Text(
                    text = "Compartir directamente en Redes Sociales:",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SocialShareButton(
                        name = "TikTok",
                        emoji = "🎵",
                        brandColor = Color(0xFF00F2FE),
                        onClick = {
                            shareToPlatform(
                                context = context,
                                shareText = "$shareMessage #LegoBingo #BingoLive",
                                appPackage = "com.zhiliaoapp.musically",
                                webFallbackUrl = "https://www.tiktok.com"
                            )
                        }
                    )

                    SocialShareButton(
                        name = "Facebook",
                        emoji = "📘",
                        brandColor = Color(0xFF1877F2),
                        onClick = {
                            shareToPlatform(
                                context = context,
                                shareText = shareMessage,
                                appPackage = "com.facebook.katana",
                                webFallbackUrl = "https://www.facebook.com/sharer/sharer.php?u=${Uri.encode(appShareUrl)}"
                            )
                        }
                    )

                    SocialShareButton(
                        name = "Instagram",
                        emoji = "📸",
                        brandColor = Color(0xFFE1306C),
                        onClick = {
                            shareToPlatform(
                                context = context,
                                shareText = shareMessage,
                                appPackage = "com.instagram.android",
                                webFallbackUrl = "https://www.instagram.com"
                            )
                        }
                    )

                    SocialShareButton(
                        name = "YouTube",
                        emoji = "▶️",
                        brandColor = Color(0xFFFF0000),
                        onClick = {
                            shareToPlatform(
                                context = context,
                                shareText = shareMessage,
                                appPackage = "com.google.android.youtube",
                                webFallbackUrl = "https://www.youtube.com"
                            )
                        }
                    )

                    SocialShareButton(
                        name = "Telegram",
                        emoji = "✈️",
                        brandColor = Color(0xFF229ED9),
                        onClick = {
                            shareToPlatform(
                                context = context,
                                shareText = shareMessage,
                                appPackage = "org.telegram.messenger",
                                webFallbackUrl = "https://t.me/share/url?url=${Uri.encode(appShareUrl)}&text=${Uri.encode(shareMessage)}"
                            )
                        }
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // QR Code Container
                Surface(
                    modifier = Modifier
                        .size(190.dp)
                        .testTag("qr_code_container"),
                    shape = RoundedCornerShape(14.dp),
                    color = Color.White,
                    border = androidx.compose.foundation.BorderStroke(3.dp, LegoYellow),
                    shadowElevation = 8.dp
                ) {
                    Box(
                        modifier = Modifier
                            .padding(12.dp)
                            .aspectRatio(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.fillMaxWidth().aspectRatio(1f)) {
                            val matrixSize = qrMatrix.size
                            val cellSize = size.width / matrixSize

                            for (r in 0 until matrixSize) {
                                for (c in 0 until matrixSize) {
                                    if (qrMatrix[r][c]) {
                                        drawRect(
                                            color = Color(0xFF0B1329),
                                            topLeft = Offset(c * cellSize, r * cellSize),
                                            size = Size(cellSize + 0.5f, cellSize + 0.5f)
                                        )
                                    }
                                }
                            }
                        }

                        // Center decorative LEGO badge inside QR
                        Surface(
                            shape = CircleShape,
                            color = LegoRed,
                            border = androidx.compose.foundation.BorderStroke(2.dp, Color.White),
                            modifier = Modifier.size(34.dp),
                            shadowElevation = 4.dp
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "🧩",
                                    fontSize = 15.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "¡Escanea el código QR desde cualquier celular para jugar al instante!",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Action: Share sheet chooser
                Button(
                    onClick = {
                        val sendIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, shareMessage)
                            putExtra(Intent.EXTRA_SUBJECT, "LEGO Bingo HD & Lotería en Vivo")
                        }
                        context.startActivity(Intent.createChooser(sendIntent, "Compartir LEGO Bingo"))
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = LegoYellow),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .testTag("share_intent_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = null,
                        tint = Slate950,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Compartir por Más Aplicaciones",
                        color = Slate950,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Action: Descargar APK Directo
                Button(
                    onClick = {
                        try {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(apkDownloadUrl))
                            context.startActivity(intent)
                            Toast.makeText(context, "Iniciando descarga de APK de LEGO Bingo...", Toast.LENGTH_SHORT).show()
                        } catch (e: Exception) {
                            Toast.makeText(context, "Enlace copiado al portapapeles", Toast.LENGTH_SHORT).show()
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            clipboard.setPrimaryClip(ClipData.newPlainText("LEGO Bingo APK", apkDownloadUrl))
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Slate800),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .border(1.dp, SkyBlue400.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                        .testTag("download_apk_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.FileDownload,
                        contentDescription = null,
                        tint = SkyBlue400,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Descargar Archivo APK (Android)",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Action: Copiar Enlace
                OutlinedButton(
                    onClick = {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        clipboard.setPrimaryClip(ClipData.newPlainText("LEGO Bingo Link", appShareUrl))
                        Toast.makeText(context, "¡Enlace copiado al portapapeles!", Toast.LENGTH_SHORT).show()
                    },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(42.dp)
                        .testTag("copy_link_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Link,
                        contentDescription = null,
                        tint = Slate400,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Copiar Enlace de la Aplicación",
                        color = Slate400,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun SocialShareButton(
    name: String,
    emoji: String,
    brandColor: Color,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .padding(4.dp)
            .testTag("social_share_$name")
    ) {
        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .background(Slate950)
                .border(1.5.dp, brandColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(text = emoji, fontSize = 20.sp)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = name,
            color = Color.White,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

private fun shareToPlatform(
    context: Context,
    shareText: String,
    appPackage: String,
    webFallbackUrl: String
) {
    try {
        val appIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareText)
            setPackage(appPackage)
        }
        context.startActivity(appIntent)
    } catch (e: Exception) {
        try {
            val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(webFallbackUrl))
            context.startActivity(webIntent)
        } catch (_: Exception) {
            val genericIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, shareText)
            }
            context.startActivity(Intent.createChooser(genericIntent, "Compartir LEGO Bingo"))
        }
    }
}
