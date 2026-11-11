package com.example.ui.components

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.LotteryDraw
import com.example.model.LotteryType
import com.example.model.TicketCheckResult
import com.example.ui.theme.Emerald500
import com.example.ui.theme.PowerballRed
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900
import com.example.ui.theme.Slate950
import com.example.ui.theme.TaxiYellow

@Composable
fun LuckyTicketCheckerDialog(
    currentDraw: LotteryDraw?,
    checkResult: TicketCheckResult?,
    onCheckTicket: (whites: Set<Int>, bonus: Int?) -> Unit,
    onClearResult: () -> Unit,
    onDismiss: () -> Unit
) {
    val lotteryType = currentDraw?.type ?: LotteryType.POWERBALL

    var selectedWhites by remember {
        mutableStateOf(
            currentDraw?.whiteBalls?.take(3)?.toSet() ?: setOf(7, 14, 21, 35, 42)
        )
    }
    var selectedBonus by remember {
        mutableStateOf(currentDraw?.bonusBall ?: 7)
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .testTag("ticket_checker_dialog"),
            shape = RoundedCornerShape(16.dp),
            color = Slate900,
            border = androidx.compose.foundation.BorderStroke(1.dp, TaxiYellow.copy(alpha = 0.5f))
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header with close
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🎟️ Comprobar Boleto de Lotería",
                        color = TaxiYellow,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar",
                            tint = Slate400
                        )
                    }
                }

                Text(
                    text = "Selecciona tus números de la suerte para comparar con el sorteo en vivo de ${lotteryType.title}.",
                    color = Slate400,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Quick Pick button
                OutlinedButton(
                    onClick = {
                        val pool = (1..lotteryType.whiteMax).shuffled().take(5).toSet()
                        val bonus = (1..lotteryType.bonusMax).random()
                        selectedWhites = pool
                        selectedBonus = bonus
                        onClearResult()
                    },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("quick_pick_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = TaxiYellow,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "Generar Boleto Rápido (Quick Pick)", color = Color.White, fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Current Selected Numbers
                Text(
                    text = "Tus 5 Números Blancos (${selectedWhites.size}/5):",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    selectedWhites.sorted().forEach { num ->
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                                .border(1.dp, Slate700, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = num.toString(),
                                color = Slate900,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Bonus Ball
                Text(
                    text = "Bola Especial (${lotteryType.bonusName}):",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(PowerballRed)
                        .border(1.dp, Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = selectedBonus.toString(),
                        color = Color.White,
                        fontWeight = FontWeight.Black,
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Check Button
                Button(
                    onClick = { onCheckTicket(selectedWhites, selectedBonus) },
                    colors = ButtonDefaults.buttonColors(containerColor = TaxiYellow),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .testTag("verify_ticket_button")
                ) {
                    Text(
                        text = "Verificar Contra Sorteo Actual",
                        color = Slate950,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }

                // Result Box if checked
                if (checkResult != null) {
                    Spacer(modifier = Modifier.height(14.dp))
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Slate950,
                        border = androidx.compose.foundation.BorderStroke(1.dp, Emerald500),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("ticket_result_box")
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = Emerald500,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = checkResult.prizeTier,
                                    color = Emerald500,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Aciertos Blancos: ${checkResult.matchedWhiteNumbers.size} (${checkResult.matchedWhiteNumbers.joinToString()})",
                                color = Color.White,
                                fontSize = 12.sp
                            )
                            Text(
                                text = "Acierto Bola Bonus: ${if (checkResult.matchedBonus) "¡SÍ!" else "No"}",
                                color = if (checkResult.matchedBonus) Emerald500 else Slate400,
                                fontSize = 12.sp
                            )
                            Text(
                                text = "Premio Estimado: ${checkResult.estimatedPayout}",
                                color = TaxiYellow,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
