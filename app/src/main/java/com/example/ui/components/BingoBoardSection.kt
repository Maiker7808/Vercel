package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BingoCard
import com.example.model.BingoCell
import com.example.ui.theme.ColumnB
import com.example.ui.theme.ColumnG
import com.example.ui.theme.ColumnI
import com.example.ui.theme.ColumnN
import com.example.ui.theme.ColumnO
import com.example.ui.theme.Emerald500
import com.example.ui.theme.Emerald600
import com.example.ui.theme.SkyBlue400
import com.example.ui.theme.SkyBlue600
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900
import com.example.ui.theme.Slate950
import com.example.ui.theme.TaxiGold
import com.example.ui.theme.TaxiYellow

@Composable
fun BingoBoardSection(
    currentCard: BingoCard?,
    totalCards: Int,
    currentCardIndex: Int,
    playerPoints: Int,
    onSelectCard: (Int) -> Unit,
    onAddCard: () -> Unit,
    onGenerateNewCard: () -> Unit,
    onOpenStore: () -> Unit,
    onCellClick: (row: Int, col: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("bingo_container"),
        shape = RoundedCornerShape(12.dp),
        color = Slate950,
        border = androidx.compose.foundation.BorderStroke(1.dp, Slate800)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {

            // Top Header: Title, Points, and "Nuevo Cartón" Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "🎯 Cartón LEGO BINGO HD",
                        color = SkyBlue400,
                        fontWeight = FontWeight.Black,
                        fontSize = 17.sp
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Points chip (clickable to open store)
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Slate850Color,
                        border = androidx.compose.foundation.BorderStroke(1.dp, TaxiGold.copy(alpha = 0.5f)),
                        modifier = Modifier
                            .clickable { onOpenStore() }
                            .testTag("points_shop_chip")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Casino,
                                contentDescription = null,
                                tint = TaxiYellow,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "$playerPoints pts",
                                color = TaxiYellow,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    // Botón Tienda de Cartones en USD
                    Button(
                        onClick = onOpenStore,
                        colors = ButtonDefaults.buttonColors(containerColor = Emerald600),
                        shape = RoundedCornerShape(6.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                        modifier = Modifier.testTag("buy_cards_store_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "Comprar $",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    // "Nuevo Cartón" button matching prompt: "bg-sky-600 hover:bg-sky-500 text-xs font-semibold"
                    Button(
                        onClick = onGenerateNewCard,
                        colors = ButtonDefaults.buttonColors(containerColor = SkyBlue600),
                        shape = RoundedCornerShape(6.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier.testTag("new_card_button")
                    ) {
                        Text(
                            text = "Nuevo Cartón",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Card Switcher Tabs (Cartón 1, Cartón 2, +)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (i in 0 until totalCards) {
                    val isSelected = (i == currentCardIndex)
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = if (isSelected) SkyBlue600 else Slate800,
                        modifier = Modifier
                            .clickable { onSelectCard(i) }
                            .testTag("card_tab_$i")
                    ) {
                        Text(
                            text = "Cartón ${i + 1}",
                            color = if (isSelected) Color.White else Slate400,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }

                if (totalCards < 4) {
                    IconButton(
                        onClick = onAddCard,
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(Slate800)
                            .testTag("add_card_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Agregar Cartón",
                            tint = SkyBlue400,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Header Letters: B - I - N - G - O with High Definition Glossy Brick Style
            val headerColumns = listOf(
                Pair("B", ColumnB),
                Pair("I", ColumnI),
                Pair("N", ColumnN),
                Pair("G", ColumnG),
                Pair("O", ColumnO)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                headerColumns.forEach { (letter, color) ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .shadow(4.dp, RoundedCornerShape(8.dp))
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                Brush.verticalGradient(
                                    listOf(color, color.copy(alpha = 0.75f))
                                )
                            )
                            .border(1.5.dp, Color.White.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
                            .padding(vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            // Stud dot on top
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.7f))
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = letter,
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Black,
                                style = androidx.compose.ui.text.TextStyle(
                                    shadow = androidx.compose.ui.graphics.Shadow(
                                        color = Color.Black.copy(alpha = 0.5f),
                                        blurRadius = 4f
                                    )
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // 5x5 Matrix Board
            if (currentCard != null) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("bingo_board"),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    for (rowIndex in 0 until 5) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            for (colIndex in 0 until 5) {
                                val cell = currentCard.cells[rowIndex][colIndex]
                                BingoCellItem(
                                    cell = cell,
                                    onClick = { onCellClick(rowIndex, colIndex) },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Board Footer Help
            Text(
                text = "Toca cada número para marcarlo • Línea, 4 esquinas o Cartón lleno ganan premios",
                color = Slate400,
                fontSize = 10.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

private val Slate850Color = Color(0xFF151F34)

@Composable
private fun BingoCellItem(
    cell: BingoCell,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isFree = cell.isFree
    val isMarked = cell.isMarked
    val isWinning = cell.isWinning

    val infiniteTransition = rememberInfiniteTransition(label = "win_glow")
    val winScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "winScale"
    )

    // Cell colors matching prompt:
    // val === "FREE" ? "bg-yellow-500 text-slate-950 font-black border-yellow-400"
    // : isMarked ? "bg-green-600" : "bg-slate-800 hover:bg-slate-700 text-white border-slate-700"
    val backgroundColor by animateColorAsState(
        targetValue = when {
            isWinning -> TaxiGold
            isFree -> TaxiYellow
            isMarked -> Emerald600
            else -> Slate800
        },
        label = "cell_bg"
    )

    val textColor = when {
        isWinning -> Slate950
        isFree -> Slate950
        isMarked -> Color.White
        else -> Color.White
    }

    val borderColor = when {
        isWinning -> TaxiYellow
        isFree -> TaxiYellow
        isMarked -> Emerald500
        else -> Slate700
    }

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .scale(if (isWinning) winScale else 1f)
            .shadow(if (isMarked || isFree) 3.dp else 1.dp, RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .border(
                width = if (isWinning) 2.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable(onClick = onClick)
            .testTag("cell_${cell.row}_${cell.col}"),
        contentAlignment = Alignment.Center
    ) {
        if (isFree) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "FREE Cell",
                    tint = Slate950,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = "FREE",
                    color = textColor,
                    fontWeight = FontWeight.Black,
                    fontSize = 11.sp
                )
            }
        } else {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = cell.displayValue,
                    color = textColor,
                    fontWeight = FontWeight.Black,
                    fontSize = 17.sp,
                    style = androidx.compose.ui.text.TextStyle(
                        shadow = if (isMarked) androidx.compose.ui.graphics.Shadow(
                            color = Color.Black.copy(alpha = 0.5f),
                            blurRadius = 3f
                        ) else null
                    )
                )

                // Subtle check badge on marked cells
                if (isMarked) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Marcado",
                        tint = Color.White.copy(alpha = 0.45f),
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
        }
    }
}
