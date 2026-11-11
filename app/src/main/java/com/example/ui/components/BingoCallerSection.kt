package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoMode
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CalledBall
import com.example.ui.theme.ColumnB
import com.example.ui.theme.ColumnG
import com.example.ui.theme.ColumnI
import com.example.ui.theme.ColumnN
import com.example.ui.theme.ColumnO
import com.example.ui.theme.Emerald500
import com.example.ui.theme.Emerald600
import com.example.ui.theme.SkyBlue400
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900
import com.example.ui.theme.TaxiYellow

@Composable
fun BingoCallerSection(
    lastCalledBall: CalledBall?,
    calledBalls: List<CalledBall>,
    isCallerRunning: Boolean,
    autoDaubEnabled: Boolean,
    onCallNext: () -> Unit,
    onToggleAutoCaller: () -> Unit,
    onToggleAutoDaub: () -> Unit,
    onResetCaller: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("bingo_caller_section"),
        shape = RoundedCornerShape(12.dp),
        color = Slate800,
        border = androidx.compose.foundation.BorderStroke(1.dp, Slate700)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {

            // Header & Counter
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "📢 Balotera en Vivo (75 Bolas)",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }

                Text(
                    text = "${calledBalls.size} / 75",
                    color = TaxiYellow,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Main Caller Presentation: Big Ball + Controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Large Called Ball Display
                AnimatedContent(
                    targetState = lastCalledBall,
                    transitionSpec = {
                        (scaleIn(tween(250)) + fadeIn(tween(250))).togetherWith(scaleOut(tween(180)) + fadeOut(tween(180)))
                    },
                    label = "caller_ball_anim"
                ) { ball ->
                    if (ball != null) {
                        BigCalledBall(ball = ball)
                    } else {
                        EmptyCallerBall()
                    }
                }

                // Controls: "Cantar Balota", "Auto Cantar", "Auto Marcado"
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Button(
                            onClick = onCallNext,
                            colors = ButtonDefaults.buttonColors(containerColor = TaxiYellow),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("call_ball_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.TouchApp,
                                contentDescription = null,
                                tint = Slate900,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Cantar",
                                color = Slate900,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }

                        Button(
                            onClick = onToggleAutoCaller,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isCallerRunning) Color(0xFFDC2626) else Slate700
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("auto_caller_button")
                        ) {
                            Icon(
                                imageVector = if (isCallerRunning) Icons.Default.Stop else Icons.Default.PlayArrow,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (isCallerRunning) "Parar" else "Auto (5s)",
                                color = Color.White,
                                fontSize = 12.sp
                            )
                        }
                    }

                    // Auto-daub chip & Reset
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FilterChip(
                            selected = autoDaubEnabled,
                            onClick = onToggleAutoDaub,
                            label = {
                                Text(
                                    text = if (autoDaubEnabled) "Auto-Marcar: SÍ" else "Auto-Marcar: NO",
                                    fontSize = 11.sp
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = if (autoDaubEnabled) Icons.Default.CheckCircle else Icons.Default.AutoMode,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp)
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Emerald600,
                                selectedLabelColor = Color.White,
                                containerColor = Slate900,
                                labelColor = Slate400
                            ),
                            modifier = Modifier.testTag("auto_daub_toggle")
                        )

                        if (calledBalls.isNotEmpty()) {
                            OutlinedButton(
                                onClick = onResetCaller,
                                shape = RoundedCornerShape(6.dp),
                                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                modifier = Modifier.testTag("reset_caller_button")
                            ) {
                                Text(text = "Reiniciar", fontSize = 11.sp, color = Slate400)
                            }
                        }
                    }
                }
            }

            // Recent called balls strip
            if (calledBalls.size > 1) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Últimas balotas cantadas:",
                    color = Slate400,
                    fontSize = 11.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(calledBalls.drop(1).take(10)) { ball ->
                        MiniBall(ball = ball)
                    }
                }
            }
        }
    }
}

@Composable
private fun BigCalledBall(ball: CalledBall) {
    val ballColor = when (ball.letter) {
        'B' -> ColumnB
        'I' -> ColumnI
        'N' -> ColumnN
        'G' -> ColumnG
        else -> ColumnO
    }

    Box(
        modifier = Modifier
            .size(78.dp)
            .shadow(10.dp, CircleShape)
            .clip(CircleShape)
            .background(
                Brush.radialGradient(
                    colors = listOf(Color.White, ballColor, ballColor.copy(alpha = 0.85f)),
                    center = androidx.compose.ui.geometry.Offset(35f, 35f),
                    radius = 65f
                )
            )
            .border(2.5.dp, Color.White, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "${ball.letter}",
                color = Color.White,
                fontWeight = FontWeight.Black,
                fontSize = 15.sp,
                style = androidx.compose.ui.text.TextStyle(
                    shadow = androidx.compose.ui.graphics.Shadow(
                        color = Color.Black.copy(alpha = 0.6f),
                        blurRadius = 4f
                    )
                )
            )
            Text(
                text = "${ball.number}",
                color = Color.White,
                fontWeight = FontWeight.Black,
                fontSize = 28.sp,
                style = androidx.compose.ui.text.TextStyle(
                    shadow = androidx.compose.ui.graphics.Shadow(
                        color = Color.Black.copy(alpha = 0.7f),
                        blurRadius = 5f
                    )
                )
            )
        }
    }
}

@Composable
private fun EmptyCallerBall() {
    Box(
        modifier = Modifier
            .size(70.dp)
            .clip(CircleShape)
            .background(Slate900)
            .border(1.5.dp, Slate700, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "--",
            color = Slate400,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
    }
}

@Composable
private fun MiniBall(ball: CalledBall) {
    val ballColor = when (ball.letter) {
        'B' -> ColumnB
        'I' -> ColumnI
        'N' -> ColumnN
        'G' -> ColumnG
        else -> ColumnO
    }

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Slate900,
        border = androidx.compose.foundation.BorderStroke(1.dp, ballColor)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${ball.letter}-${ball.number}",
                color = ballColor,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
