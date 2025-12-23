package com.example.habitchecklist.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Habit")
data class Habit(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val lastDone: Long,
    val streak: Int,
    val state: Int,
    val reminderEnabled: Boolean = false,
    val windowStartMinutes: Int = 8 * 60, // default 08:00
    val windowEndMinutes: Int = 22 * 60, // default 22:00
    val remindersPerDay: Int = 0
)