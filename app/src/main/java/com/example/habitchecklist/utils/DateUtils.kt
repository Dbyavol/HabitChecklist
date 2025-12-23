package com.example.habitchecklist.utils

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.math.max
import kotlin.math.min

object DateUtils {

    private val timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    fun isSameDay(first: Long, second: Long): Boolean {
        val zone = ZoneId.systemDefault()
        val d1 = Instant.ofEpochMilli(first).atZone(zone).toLocalDate()
        val d2 = Instant.ofEpochMilli(second).atZone(zone).toLocalDate()
        return d1 == d2
    }

    fun daysBetween(startMillis: Long, endMillis: Long): Long {
        val zone = ZoneId.systemDefault()
        val start = Instant.ofEpochMilli(startMillis).atZone(zone).toLocalDate()
        val end = Instant.ofEpochMilli(endMillis).atZone(zone).toLocalDate()
        return java.time.temporal.ChronoUnit.DAYS.between(start, end)
    }

    fun parseTime(text: String): Int? {
        return runCatching {
            val localTime = java.time.LocalTime.parse(text.trim(), timeFormatter)
            localTime.hour * 60 + localTime.minute
        }.getOrNull()
    }

    fun formatTime(minutes: Int): String {
        val normalized = max(0, min(minutes, 23 * 60 + 59))
        val hours = normalized / 60
        val mins = normalized % 60
        return "%02d:%02d".format(hours, mins)
    }
}

