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
    val state: Int
)