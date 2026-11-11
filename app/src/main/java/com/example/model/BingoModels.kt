package com.example.model

data class BingoCell(
    val row: Int,
    val col: Int,
    val number: Int?,
    val isFree: Boolean = false,
    val isMarked: Boolean = false,
    val isWinning: Boolean = false
) {
    val displayValue: String
        get() = if (isFree) "FREE" else (number?.toString() ?: "")
}

data class BingoCard(
    val id: String,
    val cardIndex: Int,
    val cells: List<List<BingoCell>>
)

data class CalledBall(
    val number: Int,
    val letter: Char,
    val timestamp: Long = System.currentTimeMillis()
) {
    companion object {
        fun fromNumber(n: Int): CalledBall {
            val letter = when (n) {
                in 1..15 -> 'B'
                in 16..30 -> 'I'
                in 31..45 -> 'N'
                in 46..60 -> 'G'
                else -> 'O'
            }
            return CalledBall(n, letter)
        }
    }
}

enum class WinType(val label: String, val points: Int) {
    HORIZONTAL_LINE("Línea Horizontal", 100),
    VERTICAL_LINE("Línea Vertical", 100),
    DIAGONAL("Línea Diagonal", 150),
    FOUR_CORNERS("4 Esquinas", 200),
    BLACKOUT("¡Cartón Lleno / BINGO!", 1000)
}

data class BingoWin(
    val winType: WinType,
    val message: String,
    val winningCoords: Set<Pair<Int, Int>>,
    val pointsAwarded: Int
)
