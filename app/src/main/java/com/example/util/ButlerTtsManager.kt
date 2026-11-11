package com.example.util

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.Locale

class ButlerTtsManager(context: Context) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private var isInitialized = false

    init {
        try {
            tts = TextToSpeech(context.applicationContext, this)
        } catch (e: Exception) {
            Log.w("ButlerTtsManager", "Error initializing TextToSpeech: ${e.message}")
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts?.setLanguage(Locale("es", "ES"))
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.w("ButlerTtsManager", "Spanish language not supported for TTS, trying default locale")
                tts?.setLanguage(Locale.getDefault())
            }
            isInitialized = true
            Log.i("ButlerTtsManager", "TextToSpeech initialized successfully in Spanish")
        } else {
            Log.e("ButlerTtsManager", "Initialization failed for TextToSpeech with status: $status")
        }
    }

    fun speak(text: String) {
        try {
            if (isInitialized) {
                tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
            } else {
                Log.w("ButlerTtsManager", "TTS not yet initialized, message: $text")
            }
        } catch (e: Exception) {
            Log.e("ButlerTtsManager", "Error speaking text: ${e.message}")
        }
    }

    fun shutdown() {
        try {
            tts?.stop()
            tts?.shutdown()
            tts = null
            isInitialized = false
        } catch (e: Exception) {
            Log.w("ButlerTtsManager", "Error shutting down TTS: ${e.message}")
        }
    }
}
