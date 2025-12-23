package com.example.habitchecklist.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.habitchecklist.R

class ReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        val habitName = intent?.getStringExtra(EXTRA_HABIT_NAME) ?: return
        val habitId = intent.getIntExtra(EXTRA_HABIT_ID, 0)

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Напоминание о привычке")
            .setContentText(habitName)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context).notify(NOTIF_BASE_ID + habitId, notification)
    }

    companion object {
        const val EXTRA_HABIT_ID = "habit_id"
        const val EXTRA_HABIT_NAME = "habit_name"
        const val CHANNEL_ID = "habit_reminders"
        private const val NOTIF_BASE_ID = 10_000
    }
}

