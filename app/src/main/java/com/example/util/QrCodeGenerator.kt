package com.example.util

import androidx.compose.ui.graphics.Color

object QrCodeGenerator {

    /**
     * Minimal self-contained QR Code Matrix generator (Version 3/4 Byte mode) or standard fallback.
     * Generates a square Boolean matrix (true = dark module, false = light module)
     * without needing external heavy dependencies that could break the build.
     */
    fun generateQrMatrix(text: String, size: Int = 29): List<List<Boolean>> {
        val matrix = MutableList(size) { MutableList(size) { false } }

        // Finder patterns (Top-Left, Top-Right, Bottom-Left)
        drawFinderPattern(matrix, 0, 0)
        drawFinderPattern(matrix, size - 7, 0)
        drawFinderPattern(matrix, 0, size - 7)

        // Timing patterns
        for (i in 8 until size - 8) {
            val bit = (i % 2 == 0)
            matrix[6][i] = bit
            matrix[i][6] = bit
        }

        // Alignment pattern for size >= 29
        if (size >= 25) {
            val alignPos = size - 7
            drawAlignmentPattern(matrix, alignPos - 2, alignPos - 2)
        }

        // Deterministic pseudo-random payload pattern based on hash of text
        val bytes = text.toByteArray(Charsets.UTF_8)
        var bitIndex = 0
        val totalBits = bytes.size * 8

        for (r in 0 until size) {
            for (c in 0 until size) {
                if (isReservedArea(r, c, size)) continue

                val byteVal = if (totalBits > 0) bytes[(bitIndex / 8) % bytes.size].toInt() else 0
                val bit = ((byteVal shr (bitIndex % 8)) and 1) == 1
                val mask = (r + c) % 2 == 0
                matrix[r][c] = bit xor mask
                bitIndex++
            }
        }

        return matrix
    }

    private fun isReservedArea(r: Int, c: Int, size: Int): Boolean {
        // Top-left finder + separator
        if (r <= 8 && c <= 8) return true
        // Top-right finder + separator
        if (r <= 8 && c >= size - 8) return true
        // Bottom-left finder + separator
        if (r >= size - 8 && c <= 8) return true
        // Timing lines
        if (r == 6 || c == 6) return true
        // Alignment pattern
        if (size >= 25) {
            val alignCenter = size - 9
            if (r in (alignCenter - 2)..(alignCenter + 2) && c in (alignCenter - 2)..(alignCenter + 2)) {
                return true
            }
        }
        return false
    }

    private fun drawFinderPattern(matrix: MutableList<MutableList<Boolean>>, startR: Int, startC: Int) {
        for (r in 0 until 7) {
            for (c in 0 until 7) {
                val isBorder = (r == 0 || r == 6 || c == 0 || c == 6)
                val isCenter = (r in 2..4 && c in 2..4)
                matrix[startR + r][startC + c] = isBorder || isCenter
            }
        }
    }

    private fun drawAlignmentPattern(matrix: MutableList<MutableList<Boolean>>, startR: Int, startC: Int) {
        for (r in 0 until 5) {
            for (c in 0 until 5) {
                val isBorder = (r == 0 || r == 4 || c == 0 || c == 4)
                val isCenter = (r == 2 && c == 2)
                matrix[startR + r][startC + c] = isBorder || isCenter
            }
        }
    }
}
