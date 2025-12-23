package com.ivors.everything.ui.habits

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ivors.everything.data.Habit
import com.ivors.everything.data.HabitCompletion
import com.ivors.everything.data.HabitDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class HabitTrackerViewModel(private val dao: HabitDao) : ViewModel() {

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate

    val allHabits: StateFlow<List<Habit>> = dao.getAllHabits()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val completionsForSelectedDate: StateFlow<List<HabitCompletion>> = _selectedDate
        .flatMapLatest { date ->
            dao.getCompletionsForDate(date.format(DateTimeFormatter.ISO_LOCAL_DATE))
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /**
     * Map of Habit ID to its completion count for the selected date.
     */
    val habitCompletions: StateFlow<Map<Long, Int>> = completionsForSelectedDate
        .combine(allHabits) { completions, habits ->
            completions.groupBy { it.habitId }.mapValues { it.value.size }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())

    fun selectDate(date: LocalDate) {
        _selectedDate.value = date
    }

    fun toggleHabit(habitId: Long) {
        viewModelScope.launch {
            val dateStr = _selectedDate.value.format(DateTimeFormatter.ISO_LOCAL_DATE)
            val currentCount = dao.getCompletionCountForHabitOnDate(habitId, dateStr)
            
            // For now, toggle means add if 0, else we can think about multiple completions
            // For simplicity, let's just add one more or if we want to "untoggle", we delete all for that day
            if (currentCount > 0) {
                dao.deleteCompletion(habitId, dateStr)
            } else {
                dao.insertCompletion(
                    HabitCompletion(
                        habitId = habitId,
                        timestamp = System.currentTimeMillis(),
                        date = dateStr
                    )
                )
            }
        }
    }

    fun addHabit(name: String, icon: String, frequency: Int) {
        viewModelScope.launch {
            dao.insertHabit(
                Habit(
                    name = name,
                    icon = icon,
                    color = 0, // Placeholder
                    frequency = frequency
                )
            )
        }
    }

    fun deleteHabit(habit: Habit) {
        viewModelScope.launch {
            dao.deleteHabit(habit)
        }
    }

    class Factory(private val dao: HabitDao) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return HabitTrackerViewModel(dao) as T
        }
    }
}
