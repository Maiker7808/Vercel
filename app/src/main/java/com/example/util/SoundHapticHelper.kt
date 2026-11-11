package com.example.util

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log

class SoundHapticHelper(private val context: Context) {

    private var toneGenerator: ToneGenerator? = null

    init {
        try {
            toneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, 70)
        } catch (e: Exception) {
            Log.w("SoundHapticHelper", "Could not initialize ToneGenerator: ${e.message}")
        }
    }

    private val vibrator: Vibrator? by lazy {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vm = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vm?.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            }
        } catch (e: Exception) {
            null
        }
    }

    fun playClickSound() {
        try {
            toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP, 35)
        } catch (_: Exception) {}
        vibrateLight()
    }

    fun playBallDrawnSound() {
        try {
            toneGenerator?.startTone(ToneGenerator.TONE_CDMA_PIP, 80)
        } catch (_: Exception) {}
        vibrateMedium()
    }

    fun playBingoWinSound() {
        try {
            toneGenerator?.startTone(ToneGenerator.TONE_PROP_PROMPT, 500)
        } catch (_: Exception) {}
        vibrateHeavy()
    }

    private fun vibrateLight() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(VibrationEffect.createOneShot(25, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(25)
            }
        } catch (_: Exception) {}
    }

    private fun vibrateMedium() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(VibrationEffect.createOneShot(60, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(60)
            }
        } catch (_: Exception) {}
    }

    private fun vibrateHeavy() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val timings = longArrayOf(0, 150, 80, 200, 80, 300)
                val amplitudes = intArrayOf(0, 255, 0, 255, 0, 255)
                vibrator?.vibrate(VibrationEffect.createWaveform(timings, amplitudes, -1))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(longArrayOf(0, 150, 80, 200), -1)
            }
        } catch (_: Exception) {}
    }

    fun release() {
        try {
            toneGenerator?.release()
            toneGenerator = null
        } catch (_: Exception) {}
    }
}
