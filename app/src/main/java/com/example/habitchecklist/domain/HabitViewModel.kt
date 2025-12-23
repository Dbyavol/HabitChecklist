package com.example.habitchecklist.domain

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.habitchecklist.data.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class HabitViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.get(application).habitDao()

    private val _habits = MutableStateFlow<List<Habit>>(emptyList())
    val habits = _habits.asStateFlow()

    init {
        loadHabits()
    }

    private fun loadHabits() {
        viewModelScope.launch {
            _habits.value = dao.getAll()
        }
    }

    fun addHabit(name: String) {
        viewModelScope.launch {
            dao.insert(
                Habit(
                    name = name,
                    lastDone = System.currentTimeMillis(),
                    streak = 0,
                    state = 0
                )
            )
            loadHabits()
        }
    }

    fun markDone(habit: Habit) {
        viewModelScope.launch {
            val days = TimeUnit.MILLISECONDS.toDays(
                System.currentTimeMillis() - habit.lastDone
            )

            val newStreak = if (days <= 1) habit.streak + 1 else 1

            dao.update(
                habit.copy(
                    lastDone = System.currentTimeMillis(),
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
            loadHabits()
        }
    }
}
