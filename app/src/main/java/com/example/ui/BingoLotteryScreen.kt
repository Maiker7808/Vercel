package com.example.ui

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardMembership
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.AdminPanelDialog
import com.example.ui.components.AuthSubscriptionDialog
import com.example.ui.components.AutoScrollControllerFab
import com.example.ui.components.BingoBoardSection
import com.example.ui.components.BingoCallerSection
import com.example.ui.components.BingoWinDialog
import com.example.ui.components.ButlerNarratorBar
import com.example.ui.components.DrawHistoryDialog
import com.example.ui.components.LegoBingoHeroBanner
import com.example.ui.components.LotteryLiveSection
import com.example.ui.components.LuckyTicketCheckerDialog
import com.example.ui.components.QrVoucherScannerDialog
import com.example.ui.components.ShareAppDialog
import com.example.ui.components.StorePointsCardsDialog
import com.example.ui.components.TaxiHeaderBar
import com.example.ui.components.TaxiRulesDialog
import com.example.ui.theme.Emerald500
import com.example.ui.theme.GoldenGlow
import com.example.ui.theme.GoldenSparkle
import com.example.ui.theme.LegoBlue
import com.example.ui.theme.LegoGreen
import com.example.ui.theme.LegoRed
import com.example.ui.theme.LegoYellow
import com.example.ui.theme.LegoYellowGlow
import com.example.ui.theme.SkyBlue400
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900
import com.example.ui.theme.Slate950
import com.example.viewmodel.BingoLotteryViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BingoLotteryScreen(
    viewModel: BingoLotteryViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()

    // Dialog flags
    var showHistoryDialog by remember { mutableStateOf(false) }
    var showCheckerDialog by remember { mutableStateOf(false) }
    var showRulesDialog by remember { mutableStateOf(false) }
    var showShareDialog by remember { mutableStateOf(false) }
    var showAdminDialog by remember { mutableStateOf(false) }
    var showStoreDialog by remember { mutableStateOf(false) }
    var showAuthDialog by remember { mutableStateOf(false) }
    var showQrScannerDialog by remember { mutableStateOf(false) }
    var authInitialTab by remember { mutableIntStateOf(0) }

    val currentCard = uiState.cards.getOrNull(uiState.currentCardIndex)

    // 4 Independent Swipeable Screens:
    // Page 0 = BINGO & JUEGO HD
    // Page 1 = LEGO STORE (TIENDA DE CARTONES)
    // Page 2 = LOTERÍA EE.UU. EN VIVO
    // Page 3 = CLUB VIP & MEMBRESÍAS
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 4 })

    // Independent vertical scroll states for smooth mouse wheel and auto-scroll
    val scrollStatePage0 = rememberScrollState()
    val scrollStatePage1 = rememberScrollState()
    val scrollStatePage2 = rememberScrollState()
    val scrollStatePage3 = rememberScrollState()

    val currentActiveScrollState = when (pagerState.currentPage) {
        0 -> scrollStatePage0
        1 -> scrollStatePage1
        2 -> scrollStatePage2
        else -> scrollStatePage3
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("bingo_lottery_screen"),
        containerColor = Slate950,
        topBar = {
            Column(modifier = Modifier.fillMaxWidth()) {
                TaxiHeaderBar(
                    quitoTime = uiState.currentQuitoTime,
                    taxiUnit = uiState.taxiUnitNumber,
                    soundEnabled = uiState.soundEnabled,
                    onToggleSound = { viewModel.toggleSound() },
                    onOpenRules = { showRulesDialog = true },
                    onOpenAdmin = { showAdminDialog = true },
                    onOpenShare = { showShareDialog = true },
                    onOpenStore = {
                        coroutineScope.launch { pagerState.animateScrollToPage(1) }
                    }
                )

                // Swipe Navigation Bar with 4 Independent Tabs
                TabRow(
                    selectedTabIndex = pagerState.currentPage,
                    containerColor = Slate900,
                    contentColor = LegoYellow,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                            color = LegoYellow,
                            height = 3.dp
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Tab(
                        selected = pagerState.currentPage == 0,
                        onClick = {
                            coroutineScope.launch { pagerState.animateScrollToPage(0) }
                        },
                        text = {
                            Text(
                                text = "🎯 BINGO",
                                fontSize = 11.sp,
                                fontWeight = if (pagerState.currentPage == 0) FontWeight.Black else FontWeight.Bold
                            )
                        }
                    )

                    Tab(
                        selected = pagerState.currentPage == 1,
                        onClick = {
                            coroutineScope.launch { pagerState.animateScrollToPage(1) }
                        },
                        text = {
                            Text(
                                text = "🛒 TIENDA",
                                color = if (pagerState.currentPage == 1) LegoYellow else Color.White,
                                fontSize = 11.sp,
                                fontWeight = if (pagerState.currentPage == 1) FontWeight.Black else FontWeight.Bold
                            )
                        }
                    )

                    Tab(
                        selected = pagerState.currentPage == 2,
                        onClick = {
                            coroutineScope.launch { pagerState.animateScrollToPage(2) }
                        },
                        text = {
                            Text(
                                text = "🔴 LOTERÍA",
                                fontSize = 11.sp,
                                fontWeight = if (pagerState.currentPage == 2) FontWeight.Black else FontWeight.Bold
                            )
                        }
                    )

                    Tab(
                        selected = pagerState.currentPage == 3,
                        onClick = {
                            coroutineScope.launch { pagerState.animateScrollToPage(3) }
                        },
                        text = {
                            Text(
                                text = "👑 VIP",
                                fontSize = 11.sp,
                                fontWeight = if (pagerState.currentPage == 3) FontWeight.Black else FontWeight.Bold
                            )
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Slate950)
        ) {
            // Horizontal Pager for 3 Independent Swipeable Screens
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                when (page) {
                    // PANTALLA 1: LEGO BINGO PRINCIPAL
                    0 -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.TopCenter
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .widthIn(max = 640.dp)
                                    .padding(horizontal = 12.dp, vertical = 8.dp)
                                    .verticalScroll(scrollStatePage0),
                                verticalArrangement = Arrangement.spacedBy(14.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                // High Definition LEGO Bingo Hero Banner
                                LegoBingoHeroBanner(
                                    playerPoints = uiState.playerPoints,
                                    onOpenStore = {
                                        coroutineScope.launch { pagerState.animateScrollToPage(1) }
                                    },
                                    onQuickPlay = { viewModel.generateNewCurrentCard() }
                                )

                                // Interactive Quick Auth Header Bar (Inicio de Sesión & Crear Cuenta)
                                AuthQuickActionBar(
                                    currentUser = uiState.currentUser,
                                    onOpenSubscriptionLogin = {
                                        authInitialTab = 0
                                        showAuthDialog = true
                                    },
                                    onOpenCreateAccount = {
                                        authInitialTab = 1
                                        showAuthDialog = true
                                    },
                                    onSignOut = { viewModel.signOutUser() }
                                )

                                // Butler Voice Narrator Bar (Tasks, Events, Results & Organizer)
                                ButlerNarratorBar(
                                    onNarrateTask = { viewModel.narrateTasks() },
                                    onNarrateEvent = { viewModel.narrateEvents() },
                                    onNarrateResults = { viewModel.narrateResults() },
                                    onOrganizeResults = { viewModel.organizeResults() }
                                )

                                // QR Scanner & Voucher Storage Button for Banco Pichincha Account
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = Slate900,
                                    border = androidx.compose.foundation.BorderStroke(1.dp, LegoYellow),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(12.dp))
                                        .clickable { showQrScannerDialog = true }
                                        .testTag("open_qr_voucher_scanner_btn")
                                ) {
                                    Row(
                                        modifier = Modifier.padding(14.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = androidx.compose.material.icons.Icons.Default.QrCode2,
                                                contentDescription = null,
                                                tint = LegoYellow,
                                                modifier = Modifier.size(24.dp)
                                            )
                                            Spacer(modifier = Modifier.width(10.dp))
                                            Column {
                                                Text(
                                                    text = "Lector QR & Voucher Banco Pichincha",
                                                    color = LegoYellow,
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 13.sp
                                                )
                                                Text(
                                                    text = "Cámara activada para cobros, pagos y almacenamiento",
                                                    color = Slate400,
                                                    fontSize = 10.sp
                                                )
                                            }
                                        }
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = LegoYellow
                                        ) {
                                            Text(
                                                text = "ABRIR",
                                                color = Slate950,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Black,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                            )
                                        }
                                    }
                                }

                                // Interactive 75-Ball Bingo Board Section
                                BingoBoardSection(
                                    currentCard = currentCard,
                                    totalCards = uiState.cards.size,
                                    currentCardIndex = uiState.currentCardIndex,
                                    playerPoints = uiState.playerPoints,
                                    onSelectCard = { viewModel.switchCardIndex(it) },
                                    onAddCard = { viewModel.addExtraCard() },
                                    onGenerateNewCard = { viewModel.generateNewCurrentCard() },
                                    onOpenStore = {
                                        coroutineScope.launch { pagerState.animateScrollToPage(1) }
                                    },
                                    onCellClick = { row, col -> viewModel.toggleCellMark(row, col) }
                                )

                                // Live 75-Ball Bingo Caller Section
                                BingoCallerSection(
                                    lastCalledBall = uiState.lastCalledBall,
                                    calledBalls = uiState.calledBalls,
                                    isCallerRunning = uiState.isCallerRunning,
                                    autoDaubEnabled = uiState.autoDaubEnabled,
                                    onCallNext = { viewModel.callNextBallManually() },
                                    onToggleAutoCaller = { viewModel.toggleCaller() },
                                    onToggleAutoDaub = { viewModel.toggleAutoDaub() },
                                    onResetCaller = { viewModel.resetCaller() }
                                )

                                Spacer(modifier = Modifier.height(24.dp))
                            }
                        }
                    }

                    // PANTALLA 2: LEGO STORE (TIENDA DE CARTONES & MOCK PAYMENTS)
                    1 -> {
                        LegoStoreScreen(
                            currentPoints = uiState.playerPoints,
                            currentCardsCount = uiState.cards.size,
                            taxiUnit = uiState.taxiUnitNumber,
                            onPurchaseConfirmed = { bundle, method ->
                                viewModel.purchaseCardBundle(bundle, method)
                            },
                            onBackClick = {
                                coroutineScope.launch { pagerState.animateScrollToPage(0) }
                            }
                        )
                    }

                    // PANTALLA 3: LOTERÍA EE.UU. EN VIVO
                    2 -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.TopCenter
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .widthIn(max = 640.dp)
                                    .padding(horizontal = 12.dp, vertical = 8.dp)
                                    .verticalScroll(scrollStatePage2),
                                verticalArrangement = Arrangement.spacedBy(14.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = Slate900,
                                    border = androidx.compose.foundation.BorderStroke(1.5.dp, GoldenGlow),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Text(
                                            text = "Transmisión en Directo Powerball & Mega Millions",
                                            color = LegoYellow,
                                            fontWeight = FontWeight.Black,
                                            fontSize = 16.sp
                                        )
                                        Text(
                                            text = "Sorteos continuos cada 15 segundos con pozos acumulados de cientos de millones de dólares.",
                                            color = Slate400,
                                            fontSize = 12.sp
                                        )
                                    }
                                }

                                // Full Live US Lottery Section
                                LotteryLiveSection(
                                    currentDraw = uiState.currentDraw,
                                    selectedType = uiState.selectedLotteryType,
                                    isStreaming = uiState.isLotteryStreaming,
                                    secondsToNextDraw = uiState.secondsToNextDraw,
                                    onSelectType = { viewModel.setLotteryType(it) },
                                    onForceDraw = { viewModel.forceLotteryDraw() },
                                    onToggleStreaming = { viewModel.toggleLotteryStreaming() },
                                    onOpenChecker = { showCheckerDialog = true },
                                    onOpenHistory = { showHistoryDialog = true }
                                )

                                Spacer(modifier = Modifier.height(24.dp))
                            }
                        }
                    }

                    // PANTALLA 4: CLUB VIP & MEMBRESÍAS DE SUSCRIPCIÓN
                    3 -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.TopCenter
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .widthIn(max = 640.dp)
                                    .padding(horizontal = 12.dp, vertical = 8.dp)
                                    .verticalScroll(scrollStatePage3),
                                verticalArrangement = Arrangement.spacedBy(14.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                // VIP Membership Hero Card
                                Surface(
                                    shape = RoundedCornerShape(18.dp),
                                    color = Slate900,
                                    border = androidx.compose.foundation.BorderStroke(2.dp, GoldenGlow),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(
                                        modifier = Modifier.padding(18.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = "👑 Club VIP LEGO Bingo",
                                            color = GoldenGlow,
                                            fontWeight = FontWeight.Black,
                                            fontSize = 20.sp
                                        )
                                        Text(
                                            text = "Beneficios ilimitados, cartones diarios gratuitos y multiplicadores de puntos.",
                                            color = Slate400,
                                            fontSize = 12.sp,
                                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                            modifier = Modifier.padding(vertical = 4.dp)
                                        )

                                        Spacer(modifier = Modifier.height(14.dp))

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Button(
                                                onClick = {
                                                    authInitialTab = 0
                                                    showAuthDialog = true
                                                },
                                                colors = ButtonDefaults.buttonColors(containerColor = GoldenGlow),
                                                shape = RoundedCornerShape(10.dp),
                                                modifier = Modifier.weight(1f).testTag("screen3_vip_login_btn")
                                            ) {
                                                Text(
                                                    text = "Iniciar Suscripción",
                                                    color = Slate950,
                                                    fontWeight = FontWeight.Black,
                                                    fontSize = 12.sp
                                                )
                                            }

                                            Button(
                                                onClick = {
                                                    authInitialTab = 1
                                                    showAuthDialog = true
                                                },
                                                colors = ButtonDefaults.buttonColors(containerColor = LegoBlue),
                                                shape = RoundedCornerShape(10.dp),
                                                modifier = Modifier.weight(1f).testTag("screen3_register_btn")
                                            ) {
                                                Text(
                                                    text = "Crear Cuenta",
                                                    color = Color.White,
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 12.sp
                                                )
                                            }
                                        }
                                    }
                                }

                                // Social Share Networks Banner inside screen 3
                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = Slate900,
                                    border = androidx.compose.foundation.BorderStroke(1.dp, Slate700),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(14.dp)) {
                                        Text(
                                            text = "Comparte la App con Amigos en Redes:",
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Button(
                                            onClick = { showShareDialog = true },
                                            colors = ButtonDefaults.buttonColors(containerColor = LegoYellow),
                                            shape = RoundedCornerShape(10.dp),
                                            modifier = Modifier.fillMaxWidth().testTag("open_social_share_btn")
                                        ) {
                                            Text(
                                                text = "Abrir TikTok, Facebook, Instagram, YouTube, Telegram",
                                                color = Slate950,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 11.sp
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(24.dp))
                            }
                        }
                    }
                }
            }

            // Floating Mouse Auto-Scroll Controller Widget (Bottom-End)
            AutoScrollControllerFab(
                scrollState = currentActiveScrollState,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 16.dp, bottom = 20.dp)
            )
        }
    }

    // Active Win Celebration Dialog
    if (uiState.activeWin != null) {
        BingoWinDialog(
            win = uiState.activeWin!!,
            onDismiss = { viewModel.dismissWinDialog() }
        )
    }

    // Lucky Ticket Checker Dialog
    if (showCheckerDialog) {
        LuckyTicketCheckerDialog(
            currentDraw = uiState.currentDraw,
            checkResult = uiState.checkedTicketResult,
            onCheckTicket = { whites, bonus -> viewModel.checkCustomTicket(whites, bonus) },
            onClearResult = { viewModel.clearCheckedTicket() },
            onDismiss = {
                viewModel.clearCheckedTicket()
                showCheckerDialog = false
            }
        )
    }

    // Draw History Dialog
    if (showHistoryDialog) {
        DrawHistoryDialog(
            history = uiState.drawHistory,
            onDismiss = { showHistoryDialog = false }
        )
    }

    // Rules / How-to-play Dialog
    if (showRulesDialog) {
        TaxiRulesDialog(onDismiss = { showRulesDialog = false })
    }

    // Share App Dialog with TikTok, Facebook, Instagram, YouTube, Telegram, QR, and APK
    if (showShareDialog) {
        ShareAppDialog(
            taxiUnit = uiState.taxiUnitNumber,
            onDismiss = { showShareDialog = false }
        )
    }

    // Private Admin Panel Dialog with Financial Algorithm & Ledgers
    if (showAdminDialog) {
        AdminPanelDialog(
            taxiUnit = uiState.taxiUnitNumber,
            driverName = uiState.taxiDriverName,
            callerSpeedSeconds = uiState.callerIntervalSeconds,
            lotteryIntervalSeconds = uiState.secondsToNextDraw,
            pointsAwarded = uiState.playerPoints,
            registeredUsers = uiState.registeredUsers,
            totalAppLogins = uiState.totalAppLogins,
            cashDeposits = uiState.cashDeposits,
            storePurchases = uiState.storePurchases,
            couponList = uiState.couponList,
            winnersHistory = uiState.winnersHistory,
            accumulatedMoneyPoolUsd = uiState.accumulatedMoneyPoolUsd,
            onSaveTaxiConfig = { unit, driver, speed, pts ->
                viewModel.updateAdminConfig(unit, driver, speed, pts)
            },
            onToggleUserStatus = { userId ->
                viewModel.toggleUserStatus(userId)
            },
            onRecordManualDeposit = { amount, method, email ->
                viewModel.recordManualDeposit(amount, method, email)
            },
            onRecordWinnerPayout = { winnerName, gameType, invested, multiplier ->
                viewModel.recordWinnerPayout(winnerName, gameType, invested, multiplier)
            },
            onAddNewCoupon = { code, discount, cards, pts ->
                viewModel.addNewCoupon(code, discount, cards, pts)
            },
            onForceLotteryReset = { viewModel.forceLotteryDraw() },
            onResetAllCards = { viewModel.resetAllCards() },
            onDismiss = { showAdminDialog = false }
        )
    }

    // Store Points & Cards Dialog
    if (showStoreDialog) {
        StorePointsCardsDialog(
            taxiUnit = uiState.taxiUnitNumber,
            currentPoints = uiState.playerPoints,
            onPurchaseConfirmed = { bundle ->
                viewModel.purchaseCardBundle(bundle)
            },
            onDismiss = { showStoreDialog = false }
        )
    }

    // QR Code & Voucher Scanner Dialog for Banco Pichincha Account
    if (showQrScannerDialog) {
        QrVoucherScannerDialog(
            taxiUnit = uiState.taxiUnitNumber,
            onVoucherCaptured = { refCode, amount ->
                viewModel.recordQrVoucherDeposit(refCode, amount)
            },
            onDismiss = { showQrScannerDialog = false }
        )
    }

    // Authentication & Subscription Dialog (Subscription Login / Email Registration)
    if (showAuthDialog) {
        AuthSubscriptionDialog(
            initialTab = authInitialTab,
            onSignInSubscription = { email, plan ->
                viewModel.signInUser(email, isSubscriptionLogin = true, plan = plan)
            },
            onRegisterAccount = { email, name ->
                viewModel.registerNewAccount(email, name)
            },
            onDismiss = { showAuthDialog = false }
        )
    }
}

@Composable
private fun AuthQuickActionBar(
    currentUser: com.example.model.UserAccount?,
    onOpenSubscriptionLogin: () -> Unit,
    onOpenCreateAccount: () -> Unit,
    onSignOut: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Slate900,
        border = androidx.compose.foundation.BorderStroke(1.dp, Slate700),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("auth_quick_action_bar")
    ) {
        if (currentUser != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(if (currentUser.isSubscribed) GoldenGlow else LegoBlue),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = currentUser.name.take(1).uppercase(),
                            color = Slate950,
                            fontWeight = FontWeight.Black,
                            fontSize = 12.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "Hola, ${currentUser.name}",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                        Text(
                            text = if (currentUser.isSubscribed) "👑 Suscriptor VIP Activo" else "Cuenta Registrada",
                            color = if (currentUser.isSubscribed) GoldenGlow else SkyBlue400,
                            fontSize = 10.sp
                        )
                    }
                }

                OutlinedButton(
                    onClick = onSignOut,
                    shape = RoundedCornerShape(6.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(text = "Salir", color = Slate400, fontSize = 11.sp)
                }
            }
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Botón Iniciar Sesión de Suscripción
                Button(
                    onClick = onOpenSubscriptionLogin,
                    colors = ButtonDefaults.buttonColors(containerColor = GoldenGlow),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .weight(1.1f)
                        .height(38.dp)
                        .testTag("quick_sub_login_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.CardMembership,
                        contentDescription = null,
                        tint = Slate950,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Iniciar Suscripción",
                        color = Slate950,
                        fontWeight = FontWeight.Black,
                        fontSize = 11.sp
                    )
                }

                // Botón Crear Cuenta Nueva con Correo Electrónico
                Button(
                    onClick = onOpenCreateAccount,
                    colors = ButtonDefaults.buttonColors(containerColor = LegoBlue),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(38.dp)
                        .testTag("quick_register_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.PersonAdd,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Crear Cuenta",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}
