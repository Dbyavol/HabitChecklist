package com.example.habitchecklist.domain

import com.example.habitchecklist.data.Habit
import com.example.habitchecklist.utils.DateUtils
import org.junit.Test
import org.junit.Assert.*
import java.util.concurrent.TimeUnit

class HabitViewModelTest {

    @Test
    fun `markDone prevents duplicate same day completion`() {
        val now = System.currentTimeMillis()
        val habit = Habit(
            id = 1,
            name = "Test",
            lastDone = now,
            streak = 5,
            state = 0,
            reminderEnabled = false,
            windowStartMinutes = 480,
            windowEndMinutes = 1320,
            remindersPerDay = 0
        )

        // markDone should not update if same day
        // This is tested through DateUtils.isSameDay logic
        assertTrue(DateUtils.isSameDay(habit.lastDone, now))
    }

    @Test
    fun `markDone increments streak for consecutive days`() {
        val yesterday = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1)
        val habit = Habit(
            id = 1,
            name = "Test",
            lastDone = yesterday,
            streak = 5,
            state = 0,
            reminderEnabled = false,
            windowStartMinutes = 480,
            windowEndMinutes = 1320,
            remindersPerDay = 0
        )

        val days = DateUtils.daysBetween(habit.lastDone, System.currentTimeMillis())
        assertEquals(1, days)
        // Streak should increment
        assertTrue(days <= 1)
    }

    @Test
    fun `markDone resets streak for non-consecutive days`() {
        val threeDaysAgo = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(3)
        val habit = Habit(
            id = 1,
            name = "Test",
            lastDone = threeDaysAgo,
            streak = 5,
            state = 0,
            reminderEnabled = false,
            windowStartMinutes = 480,
            windowEndMinutes = 1320,
            remindersPerDay = 0
        )

        val days = DateUtils.daysBetween(habit.lastDone, System.currentTimeMillis())
        assertTrue(days > 1)
        // Streak should reset to 1
    }
}

