package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.GoldenGlow
import com.example.ui.theme.GoldenSparkle
import com.example.ui.theme.LegoBlue
import com.example.ui.theme.LegoRed
import com.example.ui.theme.LegoYellow
import com.example.ui.theme.SkyBlue400
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900
import com.example.ui.theme.Slate950

@Composable
fun ButlerNarratorBar(
    onNarrateTask: () -> Unit,
    onNarrateEvent: () -> Unit,
    onNarrateResults: () -> Unit,
    onOrganizeResults: () -> Unit,
    modifier: Modifier = Modifier
) {
    var butlerStatusText by remember { mutableStateOf("Mayordomo LEGO: En espera de instrucciones...") }
    var isSpeaking by remember { mutableStateOf(false) }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("butler_narrator_bar"),
        shape = RoundedCornerShape(16.dp),
        color = Slate900,
        border = androidx.compose.foundation.BorderStroke(
            1.5.dp,
            Brush.horizontalGradient(listOf(LegoYellow, GoldenGlow, LegoBlue))
        ),
        tonalElevation = 8.dp
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
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
                            .background(
                                Brush.linearGradient(listOf(GoldenGlow, LegoYellow))
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.RecordVoiceOver,
                            contentDescription = "Mayordomo Narrador",
                            tint = Slate950,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Text(
                            text = "Mayordomo de Voz & Narrador",
                            color = LegoYellow,
                            fontWeight = FontWeight.Black,
                            fontSize = 14.sp
                        )
                        Text(
                            text = butlerStatusText,
                            color = if (isSpeaking) GoldenSparkle else Slate400,
                            fontSize = 11.sp
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (isSpeaking) LegoRed.copy(alpha = 0.3f) else Slate800,
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (isSpeaking) LegoRed else Slate700)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Mic,
                            contentDescription = null,
                            tint = if (isSpeaking) LegoRed else Slate400,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isSpeaking) "HABLANDO" else "LISTO",
                            color = if (isSpeaking) LegoRed else Slate400,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action Buttons for Narration & Organization Packages
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                ButlerActionButton(
                    title = "Tareas",
                    icon = Icons.Default.Campaign,
                    onClick = {
                        isSpeaking = true
                        butlerStatusText = "Mayordomo: Narrando tareas activas de sorteo y cartones..."
                        onNarrateTask()
                    },
                    modifier = Modifier.weight(1f)
                )

                ButlerActionButton(
                    title = "Eventos",
                    icon = Icons.Default.VolumeUp,
                    onClick = {
                        isSpeaking = true
                        butlerStatusText = "Mayordomo: Narrando evento en vivo Powerball / Bingo..."
                        onNarrateEvent()
                    },
                    modifier = Modifier.weight(1f)
                )

                ButlerActionButton(
                    title = "Resultados",
                    icon = Icons.Default.AutoAwesome,
                    onClick = {
                        isSpeaking = true
                        butlerStatusText = "Mayordomo: Narrando resultados ganadores y premios..."
                        onNarrateResults()
                    },
                    modifier = Modifier.weight(1f)
                )

                ButlerActionButton(
                    title = "Organizar",
                    icon = Icons.Default.RecordVoiceOver,
                    onClick = {
                        isSpeaking = true
                        butlerStatusText = "Mayordomo: Organizando todos los resultados financieros..."
                        onOrganizeResults()
                    },
                    modifier = Modifier.weight(1.1f)
                )
            }
        }
    }
}

@Composable
private fun ButlerActionButton(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = Slate800),
        shape = RoundedCornerShape(8.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Slate700),
        modifier = modifier.height(38.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = LegoYellow,
            modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = title,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp
        )
    }
}
