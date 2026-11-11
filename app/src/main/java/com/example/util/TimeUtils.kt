package com.example.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object TimeUtils {
    private val quitoTimeZone: TimeZone = TimeZone.getTimeZone("America/Guayaquil")

    fun getCurrentQuitoTime(): String {
        val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        sdf.timeZone = quitoTimeZone
        return sdf.format(Date())
    }

    fun formatQuitoTimestamp(timestamp: Long): String {
        val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        sdf.timeZone = quitoTimeZone
        return sdf.format(Date(timestamp))
    }

    fun getFullQuitoDate(): String {
        val sdf = SimpleDateFormat("EEE d 'de' MMM, yyyy", Locale("es", "EC"))
        sdf.timeZone = quitoTimeZone
        return sdf.format(Date()).replaceFirstChar { it.uppercase() }
    }
}
