package com.example.ui.components

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocalTaxi
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.ColumnB
import com.example.ui.theme.ColumnG
import com.example.ui.theme.ColumnI
import com.example.ui.theme.ColumnN
import com.example.ui.theme.ColumnO
import com.example.ui.theme.Emerald500
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900
import com.example.ui.theme.TaxiYellow

@Composable
fun TaxiRulesDialog(onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .testTag("rules_dialog"),
            shape = RoundedCornerShape(16.dp),
            color = Slate900,
            border = androidx.compose.foundation.BorderStroke(1.dp, TaxiYellow.copy(alpha = 0.5f))
        ) {
            Column(
                modifier = Modifier
                    .padding(18.dp)
                    .verticalScroll(rememberScrollState())
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
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(TaxiYellow),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocalTaxi,
                                contentDescription = null,
                                tint = Slate900,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Guía Let's Go Taxi Quito",
                            color = TaxiYellow,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }

                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Cerrar", tint = Slate400)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                RuleItem(
                    emoji = "🔴",
                    title = "Lotería EE.UU. en Vivo",
                    description = "Transmisión simulada en tiempo real de Powerball y Mega Millions. Se actualiza automáticamente cada 15 segundos con pozos multimillonarios."
                )

                RuleItem(
                    emoji = "🎯",
                    title = "Cartón de Bingo 75 Bolas",
                    description = "Estructura tradicional: B (1-15), I (16-30), N (31-45, centro GRATIS), G (46-60), O (61-75). Toca cada celda para marcarla con el dauber virtual."
                )

                RuleItem(
                    emoji = "📢",
                    title = "Balotera de la Suerte",
                    description = "Toca 'Cantar' o activa 'Auto' para que el sistema cante números del 1 al 75. Puedes activar 'Auto-Marcar' para que el cartón se marque solo."
                )

                RuleItem(
                    emoji = "🏆",
                    title = "Formas de Ganar BINGO",
                    description = "Gana completando: 1 Línea Horizontal (100 pts), 1 Columna Vertical (100 pts), Diagonales (150 pts), 4 Esquinas (200 pts), o Cartón Lleno (1,000 pts)."
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = TaxiYellow),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "¡Entendido, a jugar!", color = Slate900, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun RuleItem(emoji: String, title: String, description: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Text(text = emoji, fontSize = 18.sp)
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(text = title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            Text(text = description, color = Slate400, fontSize = 11.sp, lineHeight = 15.sp)
        }
    }
}
