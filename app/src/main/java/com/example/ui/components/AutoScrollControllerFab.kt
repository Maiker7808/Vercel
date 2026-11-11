package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.gestures.scrollBy
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
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.GoldenGlow
import com.example.ui.theme.LegoBlue
import com.example.ui.theme.LegoRed
import com.example.ui.theme.LegoYellow
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900
import com.example.ui.theme.Slate950
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AutoScrollControllerFab(
    scrollState: ScrollState,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    var isAutoScrollingDown by remember { mutableStateOf(false) }
    var isAutoScrollingUp by remember { mutableStateOf(false) }

    // Auto-scroll loop for smooth rolling up or down
    LaunchedEffect(isAutoScrollingDown, isAutoScrollingUp) {
        while (isAutoScrollingDown && scrollState.value < scrollState.maxValue) {
            scrollState.scrollBy(15f)
            delay(16)
        }
        if (scrollState.value >= scrollState.maxValue) {
            isAutoScrollingDown = false
        }

        while (isAutoScrollingUp && scrollState.value > 0) {
            scrollState.scrollBy(-15f)
            delay(16)
        }
        if (scrollState.value <= 0) {
            isAutoScrollingUp = false
        }
    }

    Surface(
        modifier = modifier
            .shadow(12.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .border(1.5.dp, LegoYellow, RoundedCornerShape(16.dp))
            .testTag("auto_scroll_controller"),
        color = Slate900.copy(alpha = 0.92f),
        tonalElevation = 8.dp
    ) {
        Column(
            modifier = Modifier.padding(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Scroll to Top / Auto Scroll Up
            IconButton(
                onClick = {
                    if (isAutoScrollingUp) {
                        isAutoScrollingUp = false
                    } else {
                        isAutoScrollingDown = false
                        isAutoScrollingUp = true
                    }
                },
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(if (isAutoScrollingUp) LegoRed else Slate800)
                    .testTag("scroll_up_button")
            ) {
                Icon(
                    imageVector = if (isAutoScrollingUp) Icons.Default.Stop else Icons.Default.KeyboardArrowUp,
                    contentDescription = "Subir Pantalla",
                    tint = if (isAutoScrollingUp) Color.White else LegoYellow,
                    modifier = Modifier.size(22.dp)
                )
            }

            // Scroll to Bottom / Auto Scroll Down
            IconButton(
                onClick = {
                    if (isAutoScrollingDown) {
                        isAutoScrollingDown = false
                    } else {
                        isAutoScrollingUp = false
                        isAutoScrollingDown = true
                    }
                },
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(if (isAutoScrollingDown) LegoRed else Slate800)
                    .testTag("scroll_down_button")
            ) {
                Icon(
                    imageVector = if (isAutoScrollingDown) Icons.Default.Stop else Icons.Default.KeyboardArrowDown,
                    contentDescription = "Bajar Pantalla",
                    tint = if (isAutoScrollingDown) Color.White else LegoYellow,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}
