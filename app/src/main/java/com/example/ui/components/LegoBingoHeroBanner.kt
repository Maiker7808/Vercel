package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.Emerald500
import com.example.ui.theme.GoldenGlow
import com.example.ui.theme.GoldenSparkle
import com.example.ui.theme.LegoBlue
import com.example.ui.theme.LegoGreen
import com.example.ui.theme.LegoOrange
import com.example.ui.theme.LegoRed
import com.example.ui.theme.LegoYellow
import com.example.ui.theme.LegoYellowGlow
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900
import com.example.ui.theme.Slate950

@Composable
fun LegoBingoHeroBanner(
    playerPoints: Int,
    onOpenStore: () -> Unit,
    onQuickPlay: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "banner_sparkle")
    val glowScale by infiniteTransition.animateFloat(
        initialValue = 0.98f,
        targetValue = 1.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowScale"
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("lego_bingo_hero_banner"),
        shape = RoundedCornerShape(18.dp),
        color = Slate900,
        border = androidx.compose.foundation.BorderStroke(
            2.dp,
            Brush.linearGradient(
                listOf(LegoRed, LegoYellow, LegoGreen, LegoBlue)
            )
        ),
        shadowElevation = 10.dp
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {

            // High Definition 3D LEGO Bingo Artwork Banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .background(Slate950)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.lego_bingo_banner_1793832702480),
                    contentDescription = "LEGO Bingo HD Artwork Banner",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.matchParentSize()
                )

                // Top Left: LEGO Brand Floating Tag
                Row(
                    modifier = Modifier
                        .padding(12.dp)
                        .align(Alignment.TopStart),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = LegoRed,
                        shadowElevation = 4.dp
                    ) {
                        Text(
                            text = "LEGO",
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontSize = 13.sp,
                            letterSpacing = 1.2.sp,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = LegoYellow,
                        shadowElevation = 4.dp
                    ) {
                        Text(
                            text = "BINGO HD",
                            color = Slate950,
                            fontWeight = FontWeight.Black,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }

                // Top Right: Live Jackpot Marquee Chip
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Slate950.copy(alpha = 0.85f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, GoldenGlow),
                    modifier = Modifier
                        .padding(12.dp)
                        .align(Alignment.TopEnd)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = GoldenSparkle,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "POZO $620M USD",
                            color = GoldenSparkle,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
            }

            // Bottom Sub-Bar with Interactive Quick Actions & Points
            Surface(
                color = Slate950,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Left: High Definition Highlights
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "¡Construye tus Victorias!",
                                color = Color.White,
                                fontWeight = FontWeight.Black,
                                fontSize = 14.sp
                            )
                        }
                        Text(
                            text = "75 Bolas Brillantes • Lotería en Vivo",
                            color = Slate400,
                            fontSize = 11.sp
                        )
                    }

                    // Right: Quick CTA Action to Buy Cards / Points
                    Button(
                        onClick = onOpenStore,
                        colors = ButtonDefaults.buttonColors(containerColor = LegoRed),
                        shape = RoundedCornerShape(10.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp),
                        modifier = Modifier
                            .scale(glowScale)
                            .testTag("hero_banner_store_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Comprar Cartones",
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}
