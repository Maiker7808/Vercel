package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.VolumeMute
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.Emerald500
import com.example.ui.theme.GoldenGlow
import com.example.ui.theme.LegoBlue
import com.example.ui.theme.LegoRed
import com.example.ui.theme.LegoYellow
import com.example.ui.theme.LegoYellowGlow
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900
import com.example.ui.theme.Slate950

@Composable
fun TaxiHeaderBar(
    quitoTime: String,
    taxiUnit: String,
    soundEnabled: Boolean,
    onToggleSound: () -> Unit,
    onOpenRules: () -> Unit,
    onOpenAdmin: () -> Unit,
    onOpenShare: () -> Unit,
    onOpenStore: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("app_header_bar"),
        color = Slate900,
        shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            Brush.horizontalGradient(
                listOf(LegoRed, LegoYellow, LegoBlue)
            )
        ),
        tonalElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left: High Definition LEGO Bingo Logo & Titles
            Row(verticalAlignment = Alignment.CenterVertically) {
                // High Definition 3D LEGO Bingo Avatar / Logo Emblem
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .shadow(8.dp, RoundedCornerShape(12.dp))
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(LegoRed, LegoYellow)
                            )
                        )
                        .border(1.5.dp, LegoYellowGlow, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.lego_bingo_logo_1793832690148),
                        contentDescription = "Logotipo LEGO Bingo HD",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(46.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Glossy LEGO Brick Brand Chip
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = LegoRed,
                            shadowElevation = 2.dp
                        ) {
                            Text(
                                text = "LEGO",
                                color = Color.White,
                                fontWeight = FontWeight.Black,
                                fontSize = 12.sp,
                                letterSpacing = 1.sp,
                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(6.dp))

                        Text(
                            text = "BINGO HD",
                            color = LegoYellowGlow,
                            fontWeight = FontWeight.Black,
                            fontSize = 16.sp,
                            letterSpacing = 0.5.sp
                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = LegoBlue.copy(alpha = 0.35f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, LegoBlue)
                        ) {
                            Text(
                                text = "SALA #$taxiUnit",
                                color = Color.White,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                            )
                        }
                    }

                    Text(
                        text = "Juego en Vivo • Hora: ${if (quitoTime.isEmpty()) "--:--:--" else quitoTime}",
                        color = Slate400,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Right: Actions (Store, Share, Admin, Sound & Rules)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                // Tienda de Cartones & Puntos en USD
                IconButton(
                    onClick = onOpenStore,
                    modifier = Modifier
                        .size(36.dp)
                        .testTag("store_header_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "Tienda de Cartones",
                        tint = Emerald500,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Compartir Aplicación (QR / APK / Enlace)
                IconButton(
                    onClick = onOpenShare,
                    modifier = Modifier
                        .size(36.dp)
                        .testTag("share_app_header_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Compartir Aplicación",
                        tint = LegoYellow,
                        modifier = Modifier.size(19.dp)
                    )
                }

                // Administración Privada
                IconButton(
                    onClick = onOpenAdmin,
                    modifier = Modifier
                        .size(36.dp)
                        .testTag("admin_panel_header_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.AdminPanelSettings,
                        contentDescription = "Administración Privada",
                        tint = GoldenGlow,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Sonido
                IconButton(
                    onClick = onToggleSound,
                    modifier = Modifier
                        .size(36.dp)
                        .testTag("sound_toggle_button")
                ) {
                    Icon(
                        imageVector = if (soundEnabled) Icons.Default.VolumeUp else Icons.Default.VolumeMute,
                        contentDescription = "Toggle Sound",
                        tint = if (soundEnabled) LegoYellow else Slate400,
                        modifier = Modifier.size(19.dp)
                    )
                }

                // Reglas / Ayuda
                IconButton(
                    onClick = onOpenRules,
                    modifier = Modifier
                        .size(36.dp)
                        .testTag("rules_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.HelpOutline,
                        contentDescription = "Ver Reglas",
                        tint = Slate400,
                        modifier = Modifier.size(19.dp)
                    )
                }
            }
        }
    }
}
