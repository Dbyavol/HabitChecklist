package com.example.habitchecklist.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong

/**
 * Система мониторинга метрик приложения
 * Отслеживает crash rate, performance metrics, error rates
 */
object AppMetrics {
    private const val PREFS_NAME = "app_metrics"
    private const val KEY_CRASH_COUNT = "crash_count"
    private const val KEY_SESSION_COUNT = "session_count"
    private const val KEY_APP_START_TIME = "app_start_time"
    private const val KEY_LAST_ERROR = "last_error"
    
    private val crashCount = AtomicInteger(0)
    private val sessionCount = AtomicInteger(0)
    private val totalAppStartTime = AtomicLong(0)
    
    private var prefs: SharedPreferences? = null
    
    fun initialize(context: Context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        crashCount.set(prefs?.getInt(KEY_CRASH_COUNT, 0) ?: 0)
        sessionCount.set(prefs?.getInt(KEY_SESSION_COUNT, 0) ?: 0)
        totalAppStartTime.set(prefs?.getLong(KEY_APP_START_TIME, 0) ?: 0)
    }
    
    fun recordSession() {
        val count = sessionCount.incrementAndGet()
        prefs?.edit()?.putInt(KEY_SESSION_COUNT, count)?.apply()
        Log.d("AppMetrics", "Session count: $count")
    }
    
    fun recordAppStartTime(timeMs: Long) {
        val total = totalAppStartTime.addAndGet(timeMs)
        prefs?.edit()?.putLong(KEY_APP_START_TIME, total)?.apply()
        val avg = if (sessionCount.get() > 0) total / sessionCount.get() else 0
        Log.d("AppMetrics", "App start time: ${timeMs}ms, Average: ${avg}ms")
    }
    
    fun recordError(error: Throwable, context: String = "") {
        val message = "${error.javaClass.simpleName}: ${error.message} in $context"
        prefs?.edit()?.putString(KEY_LAST_ERROR, message)?.apply()
        Log.e("AppMetrics", "Error recorded: $message", error)
    }
    
    fun recordCrash() {
        val count = crashCount.incrementAndGet()
        prefs?.edit()?.putInt(KEY_CRASH_COUNT, count)?.apply()
        Log.e("AppMetrics", "Crash recorded. Total crashes: $count")
    }
    
    fun getCrashRate(): Float {
        val crashes = crashCount.get()
        val sessions = sessionCount.get()
        return if (sessions > 0) crashes.toFloat() / sessions else 0f
    }
    
    fun getAverageStartTime(): Long {
        val total = totalAppStartTime.get()
        val sessions = sessionCount.get()
        return if (sessions > 0) total / sessions else 0
    }
    
    fun getMetricsReport(): String {
        return """
            Metrics Report:
            - Sessions: ${sessionCount.get()}
            - Crashes: ${crashCount.get()}
            - Crash Rate: ${String.format("%.2f%%", getCrashRate() * 100)}
            - Avg Start Time: ${getAverageStartTime()}ms
            - Last Error: ${prefs?.getString(KEY_LAST_ERROR, "None")}
        """.trimIndent()
    }
}

