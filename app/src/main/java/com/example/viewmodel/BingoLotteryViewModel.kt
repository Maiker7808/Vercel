package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.BingoCard
import com.example.model.BingoCell
import com.example.model.BingoWin
import com.example.model.CalledBall
import com.example.model.LotteryDraw
import com.example.model.LotteryType
import com.example.model.TicketCheckResult
import com.example.model.WinType
import com.example.util.ButlerTtsManager
import com.example.util.SoundHapticHelper
import com.example.util.TimeUtils
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.UUID

data class BingoLotteryUiState(
    // Lottery State
    val selectedLotteryType: LotteryType = LotteryType.POWERBALL,
    val currentDraw: LotteryDraw? = null,
    val drawHistory: List<LotteryDraw> = emptyList(),
    val isLotteryStreaming: Boolean = true,
    val secondsToNextDraw: Int = 15,
    val totalDrawsToday: Int = 42,
    
    // Bingo State
    val cards: List<BingoCard> = emptyList(),
    val currentCardIndex: Int = 0,
    val autoDaubEnabled: Boolean = false,
    val playerPoints: Int = 350,
    val totalWins: Int = 0,
    val activeWin: BingoWin? = null,
    
    // Caller State
    val isCallerRunning: Boolean = false,
    val calledBalls: List<CalledBall> = emptyList(),
    val callerIntervalSeconds: Int = 5,
    val lastCalledBall: CalledBall? = null,
    
    // Taxi Context
    val taxiUnitNumber: String = "75",
    val taxiDriverName: String = "Carlos M. - Let's Go Taxi",
    val currentQuitoTime: String = "",
    val soundEnabled: Boolean = true,
    
    // Ticket Checker
    val checkedTicketResult: TicketCheckResult? = null,

    // Authentication & Subscription
    val currentUser: com.example.model.UserAccount? = null,

    // Admin & Financial Algorithm State
    val registeredUsers: List<com.example.model.AppUserRecord> = emptyList(),
    val totalAppLogins: Int = 148,
    val cashDeposits: List<com.example.model.CashDepositTransaction> = emptyList(),
    val storePurchases: List<com.example.model.StorePurchaseRecord> = emptyList(),
    val couponList: List<com.example.model.CouponRecord> = emptyList(),
    val winnersHistory: List<com.example.model.BingoLotteryWinnerRecord> = emptyList(),
    val accumulatedMoneyPoolUsd: Double = 1250.75
)

class BingoLotteryViewModel(application: Application) : AndroidViewModel(application) {

    private val soundHelper = SoundHapticHelper(application)
    private val ttsManager = ButlerTtsManager(application)

    private val _uiState = MutableStateFlow(BingoLotteryUiState())
    val uiState: StateFlow<BingoLotteryUiState> = _uiState.asStateFlow()

    private var lotteryJob: Job? = null
    private var callerJob: Job? = null
    private var clockJob: Job? = null

    init {
        // Initialize default cards, lottery, and sample financial ledger records
        initializeCards()
        initializeFinancialData()
        simulateNewLotteryDraw(playSound = false)
        startClock()
        startLotteryLoop()
    }

    private fun startClock() {
        clockJob?.cancel()
        clockJob = viewModelScope.launch {
            while (isActive) {
                _uiState.update { it.copy(currentQuitoTime = TimeUtils.getCurrentQuitoTime()) }
                delay(1000)
            }
        }
    }

    // ----------------------------------------------------
    // LOTTERY LOGIC
    // ----------------------------------------------------

    fun setLotteryType(type: LotteryType) {
        if (_uiState.value.selectedLotteryType != type) {
            _uiState.update { it.copy(selectedLotteryType = type) }
            simulateNewLotteryDraw(playSound = true)
        }
    }

    fun toggleLotteryStreaming() {
        val willBeStreaming = !_uiState.value.isLotteryStreaming
        _uiState.update { it.copy(isLotteryStreaming = willBeStreaming) }
        if (willBeStreaming) {
            startLotteryLoop()
        } else {
            lotteryJob?.cancel()
        }
    }

    fun forceLotteryDraw() {
        simulateNewLotteryDraw(playSound = true)
        _uiState.update { it.copy(secondsToNextDraw = 15) }
    }

    private fun startLotteryLoop() {
        lotteryJob?.cancel()
        lotteryJob = viewModelScope.launch {
            while (isActive) {
                if (_uiState.value.isLotteryStreaming) {
                    for (sec in 15 downTo 1) {
                        _uiState.update { it.copy(secondsToNextDraw = sec) }
                        delay(1000)
                    }
                    simulateNewLotteryDraw(playSound = true)
                } else {
                    delay(1000)
                }
            }
        }
    }

    fun simulateNewLotteryDraw(playSound: Boolean = true) {
        val type = _uiState.value.selectedLotteryType
        val whitePool = (1..type.whiteMax).toMutableList()
        whitePool.shuffle()
        val winningWhites = whitePool.take(type.whiteCount).sorted()
        val bonus = (1..type.bonusMax).random()
        val powerPlay = (2..5).random()

        val draw = LotteryDraw(
            id = UUID.randomUUID().toString(),
            type = type,
            whiteBalls = winningWhites,
            bonusBall = bonus,
            powerPlay = powerPlay,
            jackpotFormatted = type.baseJackpot,
            timestamp = System.currentTimeMillis(),
            timeLabelQuito = TimeUtils.getCurrentQuitoTime()
        )

        _uiState.update { state ->
            val updatedHistory = listOf(draw) + state.drawHistory.take(9)
            state.copy(
                currentDraw = draw,
                drawHistory = updatedHistory,
                totalDrawsToday = state.totalDrawsToday + 1
            )
        }

        if (playSound && _uiState.value.soundEnabled) {
            soundHelper.playBallDrawnSound()
        }
    }

    fun checkCustomTicket(selectedWhites: Set<Int>, selectedBonus: Int?) {
        val currentDraw = _uiState.value.currentDraw ?: return
        val matchedWhites = selectedWhites.filter { currentDraw.whiteBalls.contains(it) }.sorted()
        val matchedBonus = selectedBonus != null && selectedBonus == currentDraw.bonusBall

        val (tier, payout) = when {
            matchedWhites.size == 5 && matchedBonus -> Pair("¡JACKPOT MAYOR!", currentDraw.jackpotFormatted)
            matchedWhites.size == 5 -> Pair("5 Aciertos", "$1,000,000 USD")
            matchedWhites.size == 4 && matchedBonus -> Pair("4 Aciertos + Bonus", "$50,000 USD")
            matchedWhites.size == 4 -> Pair("4 Aciertos", "$100 USD")
            matchedWhites.size == 3 && matchedBonus -> Pair("3 Aciertos + Bonus", "$100 USD")
            matchedWhites.size == 3 -> Pair("3 Aciertos", "$7 USD")
            matchedWhites.size == 2 && matchedBonus -> Pair("2 Aciertos + Bonus", "$7 USD")
            matchedWhites.size == 1 && matchedBonus -> Pair("1 Acierto + Bonus", "$4 USD")
            matchedBonus -> Pair("Solo Bola Bonus", "$4 USD")
            else -> Pair("Sin premio esta vez", "$0 USD")
        }

        _uiState.update {
            it.copy(
                checkedTicketResult = TicketCheckResult(
                    matchedWhiteNumbers = matchedWhites,
                    matchedBonus = matchedBonus,
                    prizeTier = tier,
                    estimatedPayout = payout
                )
            )
        }
    }

    fun clearCheckedTicket() {
        _uiState.update { it.copy(checkedTicketResult = null) }
    }

    // ----------------------------------------------------
    // BINGO LOGIC
    // ----------------------------------------------------

    private fun initializeCards() {
        val initialCards = listOf(
            generateRandomCard(0),
            generateRandomCard(1)
        )
        _uiState.update { it.copy(cards = initialCards, currentCardIndex = 0) }
    }

    fun generateNewCurrentCard() {
        val currentIndex = _uiState.value.currentCardIndex
        val newCard = generateRandomCard(currentIndex)
        _uiState.update { state ->
            val updated = state.cards.toMutableList()
            if (currentIndex in updated.indices) {
                updated[currentIndex] = newCard
            } else {
                updated.add(newCard)
            }
            state.copy(cards = updated, activeWin = null)
        }
        if (_uiState.value.soundEnabled) {
            soundHelper.playClickSound()
        }
    }

    fun switchCardIndex(index: Int) {
        if (index in _uiState.value.cards.indices) {
            _uiState.update { it.copy(currentCardIndex = index, activeWin = null) }
            if (_uiState.value.soundEnabled) soundHelper.playClickSound()
        }
    }

    fun addExtraCard() {
        if (_uiState.value.cards.size < 4) {
            val newIdx = _uiState.value.cards.size
            val card = generateRandomCard(newIdx)
            _uiState.update { state ->
                state.copy(cards = state.cards + card, currentCardIndex = newIdx)
            }
            if (_uiState.value.soundEnabled) soundHelper.playClickSound()
        }
    }

    private fun generateRandomCard(index: Int): BingoCard {
        // Rangos estándar: B: 1-15, I: 16-30, N: 31-45, G: 46-60, O: 61-75
        val bCol = (1..15).shuffled().take(5)
        val iCol = (16..30).shuffled().take(5)
        val nCol = (31..45).shuffled().take(5)
        val gCol = (46..60).shuffled().take(5)
        val oCol = (61..75).shuffled().take(5)

        val columns = listOf(bCol, iCol, nCol, gCol, oCol)
        val grid = mutableListOf<List<BingoCell>>()

        for (row in 0 until 5) {
            val rowCells = mutableListOf<BingoCell>()
            for (col in 0 until 5) {
                val isFree = (row == 2 && col == 2)
                val number = if (isFree) null else columns[col][row]
                rowCells.add(
                    BingoCell(
                        row = row,
                        col = col,
                        number = number,
                        isFree = isFree,
                        isMarked = isFree, // FREE is always pre-marked
                        isWinning = false
                    )
                )
            }
            grid.add(rowCells)
        }

        return BingoCard(
            id = UUID.randomUUID().toString(),
            cardIndex = index,
            cells = grid
        )
    }

    fun toggleCellMark(row: Int, col: Int) {
        val state = _uiState.value
        val currentCard = state.cards.getOrNull(state.currentCardIndex) ?: return
        val targetCell = currentCard.cells[row][col]
        if (targetCell.isFree) return // Free stays marked

        val newMarked = !targetCell.isMarked
        val updatedGrid = currentCard.cells.mapIndexed { r, rList ->
            rList.mapIndexed { c, cell ->
                if (r == row && c == col) cell.copy(isMarked = newMarked) else cell
            }
        }

        val updatedCard = currentCard.copy(cells = updatedGrid)
        val updatedCards = state.cards.toMutableList().also { it[state.currentCardIndex] = updatedCard }

        _uiState.update { it.copy(cards = updatedCards) }

        if (state.soundEnabled) {
            soundHelper.playClickSound()
        }

        // Check for wins if marked
        if (newMarked) {
            checkForWins(updatedCard)
        }
    }

    private fun checkForWins(card: BingoCard) {
        val grid = card.cells

        // 1. Horizontal Lines
        for (r in 0 until 5) {
            if (grid[r].all { it.isMarked }) {
                val coords = (0 until 5).map { Pair(r, it) }.toSet()
                triggerWin(WinType.HORIZONTAL_LINE, "¡Línea Horizontal en Fila ${r + 1}!", coords)
                return
            }
        }

        // 2. Vertical Columns
        for (c in 0 until 5) {
            if ((0 until 5).all { r -> grid[r][c].isMarked }) {
                val coords = (0 until 5).map { Pair(it, c) }.toSet()
                val colLetter = listOf('B', 'I', 'N', 'G', 'O')[c]
                triggerWin(WinType.VERTICAL_LINE, "¡Columna Completa $colLetter!", coords)
                return
            }
        }

        // 3. Main Diagonal (top-left to bottom-right)
        if ((0 until 5).all { idx -> grid[idx][idx].isMarked }) {
            val coords = (0 until 5).map { Pair(it, it) }.toSet()
            triggerWin(WinType.DIAGONAL, "¡Línea Diagonal Ganadora!", coords)
            return
        }

        // 4. Anti Diagonal (top-right to bottom-left)
        if ((0 until 5).all { idx -> grid[idx][4 - idx].isMarked }) {
            val coords = (0 until 5).map { Pair(it, 4 - it) }.toSet()
            triggerWin(WinType.DIAGONAL, "¡Línea Diagonal Invertida!", coords)
            return
        }

        // 5. Four Corners
        val corners = listOf(Pair(0, 0), Pair(0, 4), Pair(4, 0), Pair(4, 4))
        if (corners.all { (r, c) -> grid[r][c].isMarked }) {
            triggerWin(WinType.FOUR_CORNERS, "¡Las 4 Esquinas de la Suerte!", corners.toSet())
            return
        }

        // 6. Blackout (Full Card)
        if (grid.flatten().all { it.isMarked }) {
            val allCoords = (0 until 5).flatMap { r -> (0 until 5).map { c -> Pair(r, c) } }.toSet()
            triggerWin(WinType.BLACKOUT, "¡¡¡CARTÓN LLENO TOTAL - GRAN BINGO!!!", allCoords)
            return
        }
    }

    private fun triggerWin(type: WinType, message: String, coords: Set<Pair<Int, Int>>) {
        val state = _uiState.value
        val currentCard = state.cards.getOrNull(state.currentCardIndex) ?: return

        // Mark winning cells in current card
        val updatedGrid = currentCard.cells.mapIndexed { r, rList ->
            rList.mapIndexed { c, cell ->
                if (coords.contains(Pair(r, c))) cell.copy(isWinning = true) else cell
            }
        }
        val updatedCard = currentCard.copy(cells = updatedGrid)
        val updatedCards = state.cards.toMutableList().also { it[state.currentCardIndex] = updatedCard }

        val win = BingoWin(
            winType = type,
            message = message,
            winningCoords = coords,
            pointsAwarded = type.points
        )

        _uiState.update {
            it.copy(
                cards = updatedCards,
                activeWin = win,
                playerPoints = it.playerPoints + type.points,
                totalWins = it.totalWins + 1
            )
        }

        if (state.soundEnabled) {
            soundHelper.playBingoWinSound()
        }
    }

    fun dismissWinDialog() {
        _uiState.update { it.copy(activeWin = null) }
    }

    // ----------------------------------------------------
    // LIVE CALLER (BALOTERA) LOGIC
    // ----------------------------------------------------

    fun toggleCaller() {
        val willRun = !_uiState.value.isCallerRunning
        _uiState.update { it.copy(isCallerRunning = willRun) }
        if (willRun) {
            startCallerLoop()
        } else {
            callerJob?.cancel()
        }
    }

    fun callNextBallManually() {
        callNextBall()
    }

    private fun startCallerLoop() {
        callerJob?.cancel()
        callerJob = viewModelScope.launch {
            while (isActive) {
                callNextBall()
                delay((_uiState.value.callerIntervalSeconds * 1000).toLong())
            }
        }
    }

    private fun callNextBall() {
        val calledNumbers = _uiState.value.calledBalls.map { it.number }.toSet()
        val remaining = (1..75).filter { !calledNumbers.contains(it) }

        if (remaining.isEmpty()) {
            callerJob?.cancel()
            _uiState.update { it.copy(isCallerRunning = false) }
            return
        }

        val nextNumber = remaining.random()
        val nextBall = CalledBall.fromNumber(nextNumber)

        _uiState.update { state ->
            val updatedList = listOf(nextBall) + state.calledBalls
            state.copy(
                calledBalls = updatedList,
                lastCalledBall = nextBall
            )
        }

        if (_uiState.value.soundEnabled) {
            soundHelper.playBallDrawnSound()
            ttsManager.speak("Bola ${nextBall.letter} ${nextBall.number}")
        }

        // Auto-daub if enabled
        if (_uiState.value.autoDaubEnabled) {
            autoDaubNumber(nextNumber)
        }
    }

    private fun autoDaubNumber(number: Int) {
        val state = _uiState.value
        val updatedCards = state.cards.map { card ->
            val updatedGrid = card.cells.map { row ->
                row.map { cell ->
                    if (cell.number == number) cell.copy(isMarked = true) else cell
                }
            }
            card.copy(cells = updatedGrid)
        }
        _uiState.update { it.copy(cards = updatedCards) }

        // Check if current card won
        val activeCard = updatedCards.getOrNull(state.currentCardIndex)
        if (activeCard != null) {
            checkForWins(activeCard)
        }
    }

    fun resetCaller() {
        callerJob?.cancel()
        _uiState.update {
            it.copy(
                isCallerRunning = false,
                calledBalls = emptyList(),
                lastCalledBall = null
            )
        }
        if (_uiState.value.soundEnabled) {
            soundHelper.playClickSound()
        }
    }

    fun toggleAutoDaub() {
        val newState = !_uiState.value.autoDaubEnabled
        _uiState.update { it.copy(autoDaubEnabled = newState) }
        if (_uiState.value.soundEnabled) {
            soundHelper.playClickSound()
        }
    }

    fun purchaseCardBundle(
        bundle: com.example.model.CardBundleType,
        method: com.example.model.PaymentMethodType = com.example.model.PaymentMethodType.DE_UNA
    ) {
        // Guarantee player has at least as many cards as purchased
        val currentCards = _uiState.value.cards.toMutableList()
        while (currentCards.size < bundle.cardCount && currentCards.size < 4) {
            val newIdx = currentCards.size
            currentCards.add(generateRandomCard(newIdx))
        }

        val userEmail = _uiState.value.currentUser?.email ?: "usuario.invitado@letsgobingo.ec"
        val timestamp = TimeUtils.getCurrentQuitoTime()
        val refCode = "MOCK-TX-${(10000..99999).random()}"

        val purchaseRecord = com.example.model.StorePurchaseRecord(
            id = UUID.randomUUID().toString().take(8),
            userEmail = userEmail,
            itemDescription = "LEGO Store - ${bundle.title} (${method.displayName})",
            cardsPurchased = bundle.cardCount,
            pointsEarned = bundle.pointsAwarded,
            paidUsd = bundle.priceUsd,
            timestampFormatted = timestamp
        )

        val depositTx = com.example.model.CashDepositTransaction(
            id = UUID.randomUUID().toString().take(8),
            userEmail = userEmail,
            method = method.displayName,
            referenceCode = refCode,
            amountUsd = bundle.priceUsd,
            timestampFormatted = timestamp,
            status = "Acreditado"
        )

        _uiState.update {
            it.copy(
                cards = currentCards,
                currentCardIndex = 0,
                playerPoints = it.playerPoints + bundle.pointsAwarded,
                storePurchases = listOf(purchaseRecord) + it.storePurchases,
                cashDeposits = listOf(depositTx) + it.cashDeposits,
                accumulatedMoneyPoolUsd = it.accumulatedMoneyPoolUsd + bundle.priceUsd
            )
        }

        if (_uiState.value.soundEnabled) {
            soundHelper.playBingoWinSound()
        }
    }

    fun recordQrVoucherDeposit(referenceCode: String, amountUsd: Double) {
        val userEmail = _uiState.value.currentUser?.email ?: "usuario.invitado@letsgobingo.ec"
        val timestamp = TimeUtils.getCurrentQuitoTime()
        val depositTx = com.example.model.CashDepositTransaction(
            id = UUID.randomUUID().toString().take(8),
            userEmail = userEmail,
            method = "Banco Pichincha (QR Voucher Escaneado)",
            referenceCode = referenceCode,
            amountUsd = amountUsd,
            timestampFormatted = timestamp,
            status = "Acreditado & Verificado"
        )
        _uiState.update {
            it.copy(
                cashDeposits = listOf(depositTx) + it.cashDeposits,
                accumulatedMoneyPoolUsd = it.accumulatedMoneyPoolUsd + amountUsd,
                playerPoints = it.playerPoints + (amountUsd * 500).toInt()
            )
        }
        if (_uiState.value.soundEnabled) {
            soundHelper.playBingoWinSound()
        }
    }

    fun narrateTasks() {
        if (_uiState.value.soundEnabled) {
            soundHelper.playBallDrawnSound()
            ttsManager.speak("Mayordomo: Tareas del juego, sorteo de cartones activos y marcado automático operando en Quito.")
        }
    }

    fun narrateEvents() {
        if (_uiState.value.soundEnabled) {
            soundHelper.playBallDrawnSound()
            ttsManager.speak("Mayordomo: Evento en vivo de Powerball y Mega Millions con pozo acumulado millonario.")
        }
    }

    fun narrateResults() {
        if (_uiState.value.soundEnabled) {
            soundHelper.playBingoWinSound()
            ttsManager.speak("Mayordomo: Resultados oficiales de premios y ganadores anunciados correctamente.")
        }
    }

    fun organizeResults() {
        if (_uiState.value.soundEnabled) {
            soundHelper.playClickSound()
            ttsManager.speak("Mayordomo: Algoritmo financiero organizando datos de usuarios, depósitos y vouchers Pichincha.")
        }
    }

    fun signInUser(email: String, isSubscriptionLogin: Boolean, plan: com.example.model.SubscriptionPlan? = null) {
        val name = email.substringBefore("@").replaceFirstChar { it.uppercase() }
        val account = com.example.model.UserAccount(
            email = email,
            name = name,
            activeSubscription = plan ?: if (isSubscriptionLogin) com.example.model.SubscriptionPlan.VIP_GOLD else null,
            isSubscribed = isSubscriptionLogin,
            subscriptionExpiryDate = if (isSubscriptionLogin) "30 de Noviembre, 2026" else null
        )
        val addedPoints = if (isSubscriptionLogin) (plan?.bonusPoints ?: 5000) else 1000
        _uiState.update {
            it.copy(
                currentUser = account,
                playerPoints = it.playerPoints + addedPoints
            )
        }
        if (_uiState.value.soundEnabled) {
            soundHelper.playBingoWinSound()
        }
    }

    fun registerNewAccount(email: String, name: String) {
        val account = com.example.model.UserAccount(
            email = email,
            name = name.ifBlank { email.substringBefore("@") },
            activeSubscription = null,
            isSubscribed = false
        )
        _uiState.update {
            it.copy(
                currentUser = account,
                playerPoints = it.playerPoints + 1500 // Welcome gift
            )
        }
        if (_uiState.value.soundEnabled) {
            soundHelper.playClickSound()
        }
    }

    fun signOutUser() {
        _uiState.update { it.copy(currentUser = null) }
    }

    fun updateAdminConfig(unit: String, driver: String, callerSpeed: Int, points: Int) {
        _uiState.update {
            it.copy(
                taxiUnitNumber = unit,
                taxiDriverName = driver,
                callerIntervalSeconds = callerSpeed.coerceIn(2, 15),
                playerPoints = points
            )
        }
    }

    fun resetAllCards() {
        initializeCards()
        resetCaller()
        _uiState.update { it.copy(activeWin = null) }
    }

    // ----------------------------------------------------
    // FINANCIAL ALGORITHM & ADMIN LEDGER LOGIC
    // ----------------------------------------------------

    private fun initializeFinancialData() {
        val defaultUsers = listOf(
            com.example.model.AppUserRecord(
                id = "USR-101",
                name = "Carlos Mendoza",
                email = "carlos.m@gmail.com",
                registrationDate = "15/10/2026",
                loginCount = 28,
                totalInvestedUsd = 45.50,
                currentPoints = 3200,
                isVip = true,
                status = "Activo"
            ),
            com.example.model.AppUserRecord(
                id = "USR-102",
                name = "Elena Morales",
                email = "elena.uio@hotmail.com",
                registrationDate = "22/10/2026",
                loginCount = 19,
                totalInvestedUsd = 22.00,
                currentPoints = 1850,
                isVip = false,
                status = "Activo"
            ),
            com.example.model.AppUserRecord(
                id = "USR-103",
                name = "Mateo Quintero",
                email = "mateo75@yahoo.es",
                registrationDate = "01/11/2026",
                loginCount = 12,
                totalInvestedUsd = 14.50,
                currentPoints = 1400,
                isVip = true,
                status = "Activo"
            )
        )

        val defaultDeposits = listOf(
            com.example.model.CashDepositTransaction(
                id = "DEP-801",
                userEmail = "carlos.m@gmail.com",
                method = "Banco Pichincha Transferencia",
                referenceCode = "TRF-98231",
                amountUsd = 20.00,
                timestampFormatted = "04/11/2026 10:15"
            ),
            com.example.model.CashDepositTransaction(
                id = "DEP-802",
                userEmail = "elena.uio@hotmail.com",
                method = "DeUna! Pichincha QR",
                referenceCode = "DEU-44129",
                amountUsd = 10.00,
                timestampFormatted = "04/11/2026 12:40"
            ),
            com.example.model.CashDepositTransaction(
                id = "DEP-803",
                userEmail = "mateo75@yahoo.es",
                method = "Pichincha Mi Vecino Depósito",
                referenceCode = "VEC-10934",
                amountUsd = 15.00,
                timestampFormatted = "04/11/2026 14:05"
            )
        )

        val defaultStorePurchases = listOf(
            com.example.model.StorePurchaseRecord(
                id = "ORD-501",
                userEmail = "carlos.m@gmail.com",
                itemDescription = "Paquete 4 Cartones (Full)",
                cardsPurchased = 4,
                pointsEarned = 1500,
                paidUsd = 2.00,
                timestampFormatted = "04/11/2026 11:00"
            ),
            com.example.model.StorePurchaseRecord(
                id = "ORD-502",
                userEmail = "elena.uio@hotmail.com",
                itemDescription = "Paquete 2 Cartones",
                cardsPurchased = 2,
                pointsEarned = 600,
                paidUsd = 1.00,
                timestampFormatted = "04/11/2026 13:20"
            )
        )

        val defaultCoupons = listOf(
            com.example.model.CouponRecord(
                code = "LEGOBINGO2026",
                discountPercent = 25,
                bonusCards = 2,
                bonusPoints = 500,
                timesUsed = 34,
                maxUses = 100,
                expiryDate = "31/12/2026",
                isActive = true
            ),
            com.example.model.CouponRecord(
                code = "PICHINCHA_VIP",
                discountPercent = 50,
                bonusCards = 4,
                bonusPoints = 1200,
                timesUsed = 18,
                maxUses = 50,
                expiryDate = "15/12/2026",
                isActive = true
            ),
            com.example.model.CouponRecord(
                code = "QUITO75",
                discountPercent = 10,
                bonusCards = 1,
                bonusPoints = 250,
                timesUsed = 50,
                maxUses = 50,
                expiryDate = "01/11/2026",
                isActive = false
            )
        )

        val defaultWinners = listOf(
            com.example.model.BingoLotteryWinnerRecord(
                id = "WIN-901",
                winnerName = "Carlos Mendoza",
                userEmail = "carlos.m@gmail.com",
                gameType = "BINGO 75 (Cartón Lleno)",
                investedUsd = 10.00,
                prizeCalculatedUsd = 85.00,
                prizePoints = 1000,
                timestampFormatted = "04/11/2026 11:45"
            ),
            com.example.model.BingoLotteryWinnerRecord(
                id = "WIN-902",
                winnerName = "Elena Morales",
                userEmail = "elena.uio@hotmail.com",
                gameType = "Powerball Lotería (4 Aciertos)",
                investedUsd = 4.00,
                prizeCalculatedUsd = 36.50,
                prizePoints = 400,
                timestampFormatted = "04/11/2026 13:50"
            )
        )

        _uiState.update {
            it.copy(
                registeredUsers = defaultUsers,
                cashDeposits = defaultDeposits,
                storePurchases = defaultStorePurchases,
                couponList = defaultCoupons,
                winnersHistory = defaultWinners,
                totalAppLogins = 148,
                accumulatedMoneyPoolUsd = 1250.75
            )
        }
    }

    /**
     * Financial Algorithm: calculates dynamic payout based on invested amount, game difficulty and current pool reserve
     */
    fun calculatePrizeForWinner(investedUsd: Double, gameTypeMultiplier: Double): Double {
        val pool = _uiState.value.accumulatedMoneyPoolUsd
        val stats = com.example.model.FinancialAlgorithmStats(
            totalRegisteredUsers = _uiState.value.registeredUsers.size,
            totalAppSessions = _uiState.value.totalAppLogins,
            totalCashAccumulatedUsd = pool,
            totalStoreRevenueUsd = _uiState.value.storePurchases.sumOf { it.paidUsd },
            totalPayoutsAwardedUsd = _uiState.value.winnersHistory.sumOf { it.prizeCalculatedUsd },
            reserveMarginPercentage = 15.0,
            dynamicPrizePoolUsd = pool * 0.85
        )
        return stats.calculateWinnerPayout(investedUsd, gameTypeMultiplier)
    }

    fun recordManualDeposit(amountUsd: Double, method: String, email: String) {
        val deposit = com.example.model.CashDepositTransaction(
            id = "DEP-${System.currentTimeMillis().toString().takeLast(4)}",
            userEmail = email.ifBlank { "pasajero@letsgobingo.ec" },
            method = method,
            referenceCode = "REC-${(10000..99999).random()}",
            amountUsd = amountUsd,
            timestampFormatted = "Hoy ${TimeUtils.getCurrentQuitoTime()}"
        )
        _uiState.update {
            it.copy(
                cashDeposits = listOf(deposit) + it.cashDeposits,
                accumulatedMoneyPoolUsd = it.accumulatedMoneyPoolUsd + amountUsd
            )
        }
    }

    fun recordWinnerPayout(winnerName: String, gameType: String, investedUsd: Double, multiplier: Double) {
        val prize = calculatePrizeForWinner(investedUsd, multiplier)
        val winner = com.example.model.BingoLotteryWinnerRecord(
            id = "WIN-${System.currentTimeMillis().toString().takeLast(4)}",
            winnerName = winnerName,
            userEmail = "$winnerName@letsgobingo.ec".lowercase().replace(" ", "."),
            gameType = gameType,
            investedUsd = investedUsd,
            prizeCalculatedUsd = prize,
            prizePoints = (prize * 10).toInt(),
            timestampFormatted = "Hoy ${TimeUtils.getCurrentQuitoTime()}"
        )
        _uiState.update {
            it.copy(
                winnersHistory = listOf(winner) + it.winnersHistory,
                accumulatedMoneyPoolUsd = (it.accumulatedMoneyPoolUsd - prize).coerceAtLeast(100.0)
            )
        }
    }

    fun toggleUserStatus(userId: String) {
        _uiState.update { state ->
            val updated = state.registeredUsers.map { user ->
                if (user.id == userId) {
                    user.copy(status = if (user.status == "Activo") "Suspendido" else "Activo")
                } else user
            }
            state.copy(registeredUsers = updated)
        }
    }

    fun addNewCoupon(code: String, discount: Int, cards: Int, points: Int) {
        val newCoupon = com.example.model.CouponRecord(
            code = code.uppercase(),
            discountPercent = discount,
            bonusCards = cards,
            bonusPoints = points,
            timesUsed = 0,
            maxUses = 100,
            expiryDate = "31/12/2026",
            isActive = true
        )
        _uiState.update { it.copy(couponList = listOf(newCoupon) + it.couponList) }
    }

    fun toggleSound() {
        _uiState.update { it.copy(soundEnabled = !it.soundEnabled) }
    }

    override fun onCleared() {
        super.onCleared()
        soundHelper.release()
        ttsManager.shutdown()
        lotteryJob?.cancel()
        callerJob?.cancel()
        clockJob?.cancel()
    }
}
