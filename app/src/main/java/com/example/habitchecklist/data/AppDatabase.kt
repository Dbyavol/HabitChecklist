package com.example.habitchecklist.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Habit::class], version = 2)
abstract class AppDatabase : RoomDatabase() {

    abstract fun habitDao(): HabitDao

    companion object {
        fun get(context: Context): AppDatabase =
            Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "habits.db"
            )
                .addMigrations(MIGRATION_1_2)
                .build()

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE Habit ADD COLUMN reminderEnabled INTEGER NOT NULL DEFAULT 0"
                )
                db.execSQL(
                    "ALTER TABLE Habit ADD COLUMN windowStartMinutes INTEGER NOT NULL DEFAULT 480"
                )
                db.execSQL(
                    "ALTER TABLE Habit ADD COLUMN windowEndMinutes INTEGER NOT NULL DEFAULT 1320"
                )
                db.execSQL(
                    "ALTER TABLE Habit ADD COLUMN remindersPerDay INTEGER NOT NULL DEFAULT 0"
                )
            }
        }
    }
}
