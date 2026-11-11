package com.example.ui.components

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
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
import com.example.model.LotteryDraw
import com.example.model.LotteryType
import com.example.ui.theme.PowerballRed
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900
import com.example.ui.theme.TaxiYellow

@Composable
fun DrawHistoryDialog(
    history: List<LotteryDraw>,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .testTag("draw_history_dialog"),
            shape = RoundedCornerShape(16.dp),
            color = Slate900,
            border = androidx.compose.foundation.BorderStroke(1.dp, Slate700)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "📜 Sorteos Previos en Vivo",
                        color = TaxiYellow,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar",
                            tint = Slate400
                        )
                    }
                }

                Text(
                    text = "Resultados transmitidos durante la carrera actual en Quito:",
                    color = Slate400,
                    fontSize = 11.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                if (history.isEmpty()) {
                    Text(
                        text = "Aún no hay sorteos registrados.",
                        color = Slate400,
                        modifier = Modifier.padding(vertical = 20.dp)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(history) { draw ->
                            HistoryDrawCard(draw = draw)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HistoryDrawCard(draw: LotteryDraw) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = Slate800,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "${draw.type.title} • ${draw.timeLabelQuito}",
                    color = TaxiYellow,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Pozo: ${draw.jackpotFormatted}",
                    color = Slate400,
                    fontSize = 10.sp
                )
            }

            // Balls
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                draw.whiteBalls.forEach { num ->
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (num < 10) "0$num" else num.toString(),
                            color = Slate900,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(PowerballRed),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (draw.bonusBall < 10) "0${draw.bonusBall}" else draw.bonusBall.toString(),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp
                    )
                }
            }
        }
    }
}
