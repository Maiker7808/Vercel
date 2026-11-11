package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.BingoWin
import com.example.ui.theme.ColumnB
import com.example.ui.theme.ColumnG
import com.example.ui.theme.ColumnI
import com.example.ui.theme.ColumnO
import com.example.ui.theme.Emerald500
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate900
import com.example.ui.theme.Slate950
import com.example.ui.theme.TaxiGold
import com.example.ui.theme.TaxiYellow
import kotlin.random.Random

@Composable
fun BingoWinDialog(
    win: BingoWin,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("win_dialog"),
                shape = RoundedCornerShape(20.dp),
                color = Slate900,
                border = androidx.compose.foundation.BorderStroke(2.dp, TaxiYellow),
                tonalElevation = 12.dp
            ) {
                Box {
                    // Confetti Particle Background
                    ConfettiOverlay()

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Trophy Icon with Halo
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(CircleShape)
                                .background(TaxiYellow.copy(alpha = 0.2f))
                                .border(2.dp, TaxiYellow, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.EmojiEvents,
                                contentDescription = "Trophy",
                                tint = TaxiYellow,
                                modifier = Modifier.size(44.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "¡¡¡BINGO LET'S GO!!!",
                            color = TaxiYellow,
                            fontWeight = FontWeight.Black,
                            fontSize = 22.sp,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = win.message,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "¡Felicidades pasajero de Let's Go Taxi Quito!",
                            color = Slate400,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Points Badge
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Slate950,
                            border = androidx.compose.foundation.BorderStroke(1.dp, Emerald500)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Casino,
                                    contentDescription = null,
                                    tint = Emerald500,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "+${win.pointsAwarded} Puntos Taxi",
                                    color = Emerald500,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 16.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = onDismiss,
                            colors = ButtonDefaults.buttonColors(containerColor = TaxiYellow),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .testTag("continue_playing_button")
                        ) {
                            Text(
                                text = "Continuar Jugando",
                                color = Slate950,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ConfettiOverlay() {
    val confettiColors = listOf(
        TaxiYellow,
        ColumnB,
        ColumnI,
        ColumnG,
        ColumnO,
        TaxiGold
    )

    val particles = remember {
        List(35) {
            ConfettiParticle(
                xPct = Random.nextFloat(),
                yInit = Random.nextFloat() * 0.4f,
                speed = 0.5f + Random.nextFloat() * 0.8f,
                radius = 3f + Random.nextFloat() * 5f,
                color = confettiColors.random()
            )
        }
    }

    val animProgress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        animProgress.animateTo(
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(2200, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            )
        )
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        val t = animProgress.value

        particles.forEach { p ->
            val curY = ((p.yInit + t * p.speed) % 1.0f) * height
            val curX = (p.xPct * width) + kotlin.math.sin((t * 6.28f + p.xPct * 10f).toDouble()).toFloat() * 15f
            drawCircle(
                color = p.color,
                radius = p.radius,
                center = Offset(curX, curY)
            )
        }
    }
}

private data class ConfettiParticle(
    val xPct: Float,
    val yInit: Float,
    val speed: Float,
    val radius: Float,
    val color: Color
)
