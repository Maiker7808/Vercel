package com.example.model

enum class LotteryType(
    val title: String,
    val whiteCount: Int,
    val whiteMax: Int,
    val bonusName: String,
    val bonusMax: Int,
    val baseJackpot: String
) {
    POWERBALL(
        title = "Powerball",
        whiteCount = 5,
        whiteMax = 69,
        bonusName = "Powerball",
        bonusMax = 26,
        baseJackpot = "$485,000,000"
    ),
    MEGA_MILLIONS(
        title = "Mega Millions",
        whiteCount = 5,
        whiteMax = 70,
        bonusName = "Mega Ball",
        bonusMax = 25,
        baseJackpot = "$620,000,000"
    )
}

data class LotteryDraw(
    val id: String,
    val type: LotteryType,
    val whiteBalls: List<Int>,
    val bonusBall: Int,
    val powerPlay: Int = 2,
    val jackpotFormatted: String,
    val timestamp: Long,
    val timeLabelQuito: String
)

data class TicketCheckResult(
    val matchedWhiteNumbers: List<Int>,
    val matchedBonus: Boolean,
    val prizeTier: String,
    val estimatedPayout: String
)
