package com.example.habitchecklist.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Habit::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun habitDao(): HabitDao

    companion object {
        fun get(context: Context): AppDatabase =
            Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "habits.db"
            ).build()
    }
}
