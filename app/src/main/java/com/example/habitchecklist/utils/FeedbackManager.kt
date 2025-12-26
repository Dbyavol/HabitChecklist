package com.example.habitchecklist.utils

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Менеджер обратной связи от пользователей
 * Поддерживает периодические запросы и кнопку обратной связи
 */
object FeedbackManager {
    private const val PREFS_NAME = "feedback_prefs"
    private const val KEY_LAST_FEEDBACK_PROMPT = "last_feedback_prompt"
    private const val KEY_FEEDBACK_COUNT = "feedback_count"
    private const val KEY_SHOULD_PROMPT = "should_prompt"
    private const val PROMPT_INTERVAL_DAYS = 7L // Показывать запрос раз в неделю
    private const val MIN_ACTIONS_BEFORE_PROMPT = 5 // Минимум действий перед запросом
    
    private var prefs: SharedPreferences? = null
    
    fun initialize(context: Context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }
    
    fun shouldShowFeedbackPrompt(context: Context): Boolean {
        val prefs = prefs ?: return false
        val lastPrompt = prefs.getLong(KEY_LAST_FEEDBACK_PROMPT, 0)
        val now = System.currentTimeMillis()
        val daysSinceLastPrompt = (now - lastPrompt) / (1000 * 60 * 60 * 24)
        
        val feedbackCount = prefs.getInt(KEY_FEEDBACK_COUNT, 0)
        
        return daysSinceLastPrompt >= PROMPT_INTERVAL_DAYS && feedbackCount >= MIN_ACTIONS_BEFORE_PROMPT
    }
    
    fun recordAction() {
        val count = (prefs?.getInt(KEY_FEEDBACK_COUNT, 0) ?: 0) + 1
        prefs?.edit()?.putInt(KEY_FEEDBACK_COUNT, count)?.apply()
    }
    
    fun recordFeedbackPrompt() {
        prefs?.edit()?.putLong(KEY_LAST_FEEDBACK_PROMPT, System.currentTimeMillis())?.apply()
    }
    
    fun openFeedback(context: Context) {
        val email = "feedback@habitchecklist.app" // Замените на реальный email
        val subject = "Feedback for Habit Checklist"
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            putExtra(Intent.EXTRA_SUBJECT, subject)
        }
        context.startActivity(Intent.createChooser(intent, "Send feedback"))
    }
    
    fun openRating(context: Context) {
        val packageName = context.packageName
        try {
            context.startActivity(
                Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName"))
            )
        } catch (e: Exception) {
            context.startActivity(
                Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$packageName"))
            )
        }
    }
}

