package com.example.habitchecklist.domain

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.habitchecklist.data.*
import com.example.habitchecklist.utils.DateUtils
import com.example.habitchecklist.utils.ReminderScheduler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HabitViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.get(application).habitDao()
    private val reminders = ReminderScheduler(application)

    private val _habits = MutableStateFlow<List<Habit>>(emptyList())
    val habits = _habits.asStateFlow()

    init {
        loadHabits()
    }

    private fun loadHabits() {
        viewModelScope.launch {
            val data = dao.getAll()
            _habits.value = data
            data.forEach { reminders.schedule(it) }
        }
    }

    fun addHabit(
        name: String,
        reminderEnabled: Boolean,
        windowStartMinutes: Int,
        windowEndMinutes: Int,
        remindersPerDay: Int
    ) {
        viewModelScope.launch {
            val habit = Habit(
                name = name,
                lastDone = System.currentTimeMillis(),
                streak = 0,
                state = 0,
                reminderEnabled = reminderEnabled && remindersPerDay > 0,
                windowStartMinutes = windowStartMinutes,
                windowEndMinutes = windowEndMinutes,
                remindersPerDay = remindersPerDay
            )

            val id = dao.insert(habit).toInt()
            reminders.schedule(habit.copy(id = id))
            loadHabits()
        }
    }

    fun markDone(habit: Habit) {
        viewModelScope.launch {
            val now = System.currentTimeMillis()

            if (DateUtils.isSameDay(habit.lastDone, now)) {
                return@launch
            }

            val days = DateUtils.daysBetween(habit.lastDone, now)

            val newStreak = if (days <= 1) habit.streak + 1 else 1

            dao.update(
                habit.copy(
                    lastDone = now,
                    streak = newStreak,
                    state = 0
                )
            )
            loadHabits()
        }
    }

    fun delete(habit: Habit) {
        viewModelScope.launch {
            dao.delete(habit)
            reminders.cancel(habit.id)
            loadHabits()
        }
    }
}
