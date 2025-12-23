package com.example.habitchecklist.utils

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.habitchecklist.data.Habit
import kotlin.random.Random
import java.time.ZonedDateTime

class ReminderScheduler(private val context: Context) {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    init {
        ensureChannel()
    }

    fun schedule(habit: Habit) {
        cancel(habit.id)

        if (!habit.reminderEnabled || habit.remindersPerDay <= 0) return

        val times = generateReminderMinutes(habit)

        times.forEachIndexed { index, minutesOfDay ->
            val triggerAt = nextTriggerMillis(minutesOfDay)
            val pendingIntent = pendingIntent(habit, index)

            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                triggerAt,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )
        }
    }

    fun cancel(habitId: Int) {
        repeat(MAX_REMINDERS) { index ->
            val intent = Intent(context, ReminderReceiver::class.java)
            val pending = PendingIntent.getBroadcast(
                context,
                habitId * 100 + index,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or pendingIntentFlags()
            )
            pending.cancel()
            alarmManager.cancel(pending)
        }
    }

    private fun pendingIntent(habit: Habit, index: Int): PendingIntent {
        val intent = Intent(context, ReminderReceiver::class.java).apply {
            putExtra(ReminderReceiver.EXTRA_HABIT_ID, habit.id)
            putExtra(ReminderReceiver.EXTRA_HABIT_NAME, habit.name)
        }

        return PendingIntent.getBroadcast(
            context,
            habit.id * 100 + index,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or pendingIntentFlags()
        )
    }

    private fun nextTriggerMillis(minutesOfDay: Int): Long {
        val now = ZonedDateTime.now()
        val todayTrigger = now.toLocalDate().atStartOfDay(now.zone).plusMinutes(minutesOfDay.toLong())
        val trigger = if (todayTrigger.isAfter(now)) todayTrigger else todayTrigger.plusDays(1)
        return trigger.toInstant().toEpochMilli()
    }

    private fun generateReminderMinutes(habit: Habit): List<Int> {
        val start = habit.windowStartMinutes
        val end = habit.windowEndMinutes
        if (end <= start) return emptyList()

        val window = end - start
        val set = mutableSetOf<Int>()
        val random = Random(System.currentTimeMillis())

        while (set.size < habit.remindersPerDay && set.size < MAX_REMINDERS && window > 0) {
            val minutes = start + random.nextInt(window)
            set.add(minutes)
        }

        return set.toList().sorted()
    }

    private fun ensureChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel(
                ReminderReceiver.CHANNEL_ID,
                "Habit reminders",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            nm.createNotificationChannel(channel)
        }
    }

    private fun pendingIntentFlags(): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_IMMUTABLE
        } else {
            0
        }
    }

    companion object {
        private const val MAX_REMINDERS = 5
    }
}

