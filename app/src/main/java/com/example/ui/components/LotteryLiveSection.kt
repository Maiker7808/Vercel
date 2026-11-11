package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.LotteryDraw
import com.example.model.LotteryType
import com.example.ui.theme.Emerald500
import com.example.ui.theme.EmeraldBg
import com.example.ui.theme.MegaBallGold
import com.example.ui.theme.PowerballRed
import com.example.ui.theme.PowerballRedLight
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900
import com.example.ui.theme.TaxiYellow

@Composable
fun LotteryLiveSection(
    currentDraw: LotteryDraw?,
    selectedType: LotteryType,
    isStreaming: Boolean,
    secondsToNextDraw: Int,
    onSelectType: (LotteryType) -> Unit,
    onForceDraw: () -> Unit,
    onToggleStreaming: () -> Unit,
    onOpenChecker: () -> Unit,
    onOpenHistory: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("lottery_container"),
        shape = RoundedCornerShape(12.dp),
        color = Slate800,
        border = androidx.compose.foundation.BorderStroke(1.dp, TaxiYellow.copy(alpha = 0.35f))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {

            // Top Header: Title & Status
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
                            .background(if (isStreaming) PowerballRed else Slate400)
                            .alpha(if (isStreaming) pulseAlpha else 1f)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Lotería EE.UU. en Vivo",
                        color = TaxiYellow,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }

                // Streaming badge
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = if (isStreaming) EmeraldBg else Slate700,
                    modifier = Modifier
                        .clickable { onToggleStreaming() }
                        .testTag("lottery_status_badge")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isStreaming) "Transmisión Activa ($secondsToNextDraw s)" else "En Pausa",
                            color = if (isStreaming) Emerald500 else Slate400,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Game Selector Tabs (Powerball / Mega Millions)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                LotteryTypeTab(
                    title = "Powerball",
                    jackpot = "$485 M",
                    isSelected = selectedType == LotteryType.POWERBALL,
                    onClick = { onSelectType(LotteryType.POWERBALL) },
                    modifier = Modifier.weight(1f)
                )
                LotteryTypeTab(
                    title = "Mega Millions",
                    jackpot = "$620 M",
                    isSelected = selectedType == LotteryType.MEGA_MILLIONS,
                    onClick = { onSelectType(LotteryType.MEGA_MILLIONS) },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Winning Balls Row (5 White Balls + 1 Bonus Ball)
            AnimatedContent(
                targetState = currentDraw,
                transitionSpec = {
                    (scaleIn(tween(300)) + fadeIn(tween(300))).togetherWith(scaleOut(tween(200)) + fadeOut(tween(200)))
                },
                label = "balls_animation"
            ) { draw ->
                if (draw != null) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // 5 White numbers
                        draw.whiteBalls.forEach { number ->
                            WhiteBall(number = number)
                            Spacer(modifier = Modifier.width(6.dp))
                        }

                        // Bonus ball (Powerball or Mega Ball)
                        BonusBall(
                            number = draw.bonusBall,
                            isPowerball = draw.type == LotteryType.POWERBALL
                        )
                    }
                } else {
                    Text(
                        text = "Cargando balotas en vivo...",
                        color = Slate400,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Footer info & Update label
            Text(
                text = currentDraw?.let { "Última actualización: ${it.timeLabelQuito} (Hora Quito)" }
                    ?: "Esperando primer sorteo...",
                color = Slate400,
                fontSize = 11.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("lottery_last_update")
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Action Buttons: Sorteo Ahora, Comprobar Boleto, Historial
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onForceDraw,
                    colors = ButtonDefaults.buttonColors(containerColor = Slate700),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("force_draw_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Nuevo Sorteo",
                        tint = TaxiYellow,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Sorteo Ya", fontSize = 12.sp, color = Color.White)
                }

                Button(
                    onClick = onOpenChecker,
                    colors = ButtonDefaults.buttonColors(containerColor = TaxiYellow),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .weight(1.2f)
                        .testTag("open_checker_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.ConfirmationNumber,
                        contentDescription = "Comprobar Boleto",
                        tint = Slate900,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Mi Boleto", fontSize = 12.sp, color = Slate900, fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = onOpenHistory,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .weight(0.9f)
                        .testTag("history_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = "Historial",
                        tint = Slate400,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(text = "Previos", fontSize = 12.sp, color = Slate400)
                }
            }
        }
    }
}

@Composable
private fun WhiteBall(number: Int) {
    Box(
        modifier = Modifier
            .size(46.dp)
            .shadow(6.dp, CircleShape)
            .clip(CircleShape)
            .background(
                Brush.radialGradient(
                    colors = listOf(Color.White, Color(0xFFF1F5F9), Color(0xFFCBD5E1)),
                    center = androidx.compose.ui.geometry.Offset(25f, 25f),
                    radius = 45f
                )
            )
            .border(1.5.dp, Color(0xFFFFFFFF), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (number < 10) "0$number" else number.toString(),
            color = Color(0xFF0F172A),
            fontWeight = FontWeight.Black,
            fontSize = 18.sp,
            style = androidx.compose.ui.text.TextStyle(
                shadow = androidx.compose.ui.graphics.Shadow(
                    color = Color.White.copy(alpha = 0.8f),
                    blurRadius = 2f
                )
            )
        )
    }
}

@Composable
private fun BonusBall(number: Int, isPowerball: Boolean) {
    val ballColor = if (isPowerball) PowerballRed else MegaBallGold
    val ringColor = if (isPowerball) PowerballRedLight else TaxiYellow

    Box(
        modifier = Modifier
            .size(46.dp)
            .shadow(8.dp, CircleShape)
            .clip(CircleShape)
            .background(
                Brush.radialGradient(
                    colors = listOf(Color.White, ringColor, ballColor),
                    center = androidx.compose.ui.geometry.Offset(25f, 25f),
                    radius = 45f
                )
            )
            .border(2.dp, ringColor, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (number < 10) "0$number" else number.toString(),
            color = Color.White,
            fontWeight = FontWeight.Black,
            fontSize = 18.sp,
            style = androidx.compose.ui.text.TextStyle(
                shadow = androidx.compose.ui.graphics.Shadow(
                    color = Color.Black.copy(alpha = 0.6f),
                    blurRadius = 4f
                )
            )
        )
    }
}

@Composable
private fun LotteryTypeTab(
    title: String,
    jackpot: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() },
        color = if (isSelected) Slate700 else Slate900,
        shape = RoundedCornerShape(8.dp),
        border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, TaxiYellow) else null
    ) {
        Column(
            modifier = Modifier.padding(vertical = 6.dp, horizontal = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                color = if (isSelected) TaxiYellow else Slate400,
                fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
            )
            Text(
                text = "Pozo: $jackpot",
                color = if (isSelected) Color.White else Slate400,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
