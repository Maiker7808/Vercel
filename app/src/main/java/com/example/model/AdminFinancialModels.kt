package com.example.model

data class AppUserRecord(
    val id: String,
    val name: String,
    val email: String,
    val registrationDate: String,
    val loginCount: Int,
    val totalInvestedUsd: Double,
    val currentPoints: Int,
    val isVip: Boolean = false,
    val status: String = "Activo"
)

data class CashDepositTransaction(
    val id: String,
    val userEmail: String,
    val method: String, // "Banco Pichincha Transferencia", "DeUna! QR", "Depósito Mi Vecino"
    val referenceCode: String,
    val amountUsd: Double,
    val timestampFormatted: String,
    val status: String = "Acreditado"
)

data class StorePurchaseRecord(
    val id: String,
    val userEmail: String,
    val itemDescription: String,
    val cardsPurchased: Int,
    val pointsEarned: Int,
    val paidUsd: Double,
    val timestampFormatted: String
)

data class CouponRecord(
    val code: String,
    val discountPercent: Int,
    val bonusCards: Int,
    val bonusPoints: Int,
    val timesUsed: Int,
    val maxUses: Int,
    val expiryDate: String,
    val isActive: Boolean = true
)

data class BingoLotteryWinnerRecord(
    val id: String,
    val winnerName: String,
    val userEmail: String,
    val gameType: String, // "BINGO 75 (Línea)", "BINGO 75 (Cartón Lleno)", "Powerball Lotería"
    val investedUsd: Double,
    val prizeCalculatedUsd: Double,
    val prizePoints: Int,
    val status: String = "Pagado",
    val timestampFormatted: String
)

data class FinancialAlgorithmStats(
    val totalRegisteredUsers: Int,
    val totalAppSessions: Int,
    val totalCashAccumulatedUsd: Double,
    val totalStoreRevenueUsd: Double,
    val totalPayoutsAwardedUsd: Double,
    val reserveMarginPercentage: Double = 15.0,
    val dynamicPrizePoolUsd: Double
) {
    /**
     * Financial Algorithm: calculates payout multiplier based on total pool and individual investment
     */
    fun calculateWinnerPayout(investedUsd: Double, tierMultiplier: Double): Double {
        val basePrize = investedUsd * tierMultiplier
        val maxAllowed = dynamicPrizePoolUsd * 0.35 // Safety reserve rule
        return minOf(basePrize, maxAllowed).coerceAtLeast(investedUsd * 1.5)
    }
}
