package com.ivors.everything.ui.worktracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ivors.everything.data.WorkLog
import com.ivors.everything.data.WorkLogDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

class WorkTrackerViewModel(private val dao: WorkLogDao) : ViewModel() {
    
    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate
    
    val logsForSelectedDay: StateFlow<List<WorkLog>> = _selectedDate
        .flatMapLatest { date ->
            val startOfDay = date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
            val endOfDay = date.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
            dao.getLogsForDay(startOfDay, endOfDay)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val weeklyWorkHours: StateFlow<Map<LocalDate, Double>> = dao.getAllLogs()
        .flatMapLatest { allLogs ->
            val sevenDaysAgo = LocalDate.now().minusDays(6)
            val hourlyMap = mutableMapOf<LocalDate, Double>()
            
            val logsByDay = allLogs.filter { 
                Instant.ofEpochMilli(it.timestamp).atZone(ZoneId.systemDefault()).toLocalDate() >= sevenDaysAgo 
            }.groupBy { 
                Instant.ofEpochMilli(it.timestamp).atZone(ZoneId.systemDefault()).toLocalDate() 
            }

            for (i in 0..6) {
                val date = sevenDaysAgo.plusDays(i.toLong())
                val dayLogs = logsByDay[date] ?: emptyList()
                hourlyMap[date] = calculateHours(dayLogs)
            }
            kotlinx.coroutines.flow.flowOf(hourlyMap)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())

    fun getLogsInRange(start: LocalDate, end: LocalDate): Flow<Map<LocalDate, Double>> {
        val startMillis = start.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val endMillis = end.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()

        return dao.getLogsInRange(startMillis, endMillis).flatMapLatest { logs ->
            val hourlyMap = mutableMapOf<LocalDate, Double>()
            val logsByDay = logs.groupBy { 
                Instant.ofEpochMilli(it.timestamp).atZone(ZoneId.systemDefault()).toLocalDate() 
            }
            
            var currentDate = start
            while (!currentDate.isAfter(end)) {
                val dayLogs = logsByDay[currentDate] ?: emptyList()
                hourlyMap[currentDate] = calculateHours(dayLogs)
                currentDate = currentDate.plusDays(1)
            }
            kotlinx.coroutines.flow.flowOf(hourlyMap)
        }
    }

    private fun calculateHours(logs: List<WorkLog>): Double {
        if (logs.size < 2) return 0.0
        var totalMillis = 0L
        var lastInTime: Long? = null
        
        logs.sortedBy { it.timestamp }.forEach { log ->
            if (log.eventType == "IN") {
                lastInTime = log.timestamp
            } else if (log.eventType == "OUT" && lastInTime != null) {
                totalMillis += (log.timestamp - lastInTime!!)
                lastInTime = null
            }
        }
        return totalMillis / (1000.0 * 60 * 60)
    }
    
    fun selectDate(date: LocalDate) {
        _selectedDate.value = date
    }
    
    fun walkIn() {
        viewModelScope.launch {
            dao.insert(WorkLog(timestamp = System.currentTimeMillis(), eventType = "IN"))
        }
    }
    
    fun walkOut() {
        viewModelScope.launch {
            dao.insert(WorkLog(timestamp = System.currentTimeMillis(), eventType = "OUT"))
        }
    }
    
    fun deleteLog(id: Long) {
        viewModelScope.launch {
            dao.deleteById(id)
        }
    }
    
    class Factory(private val dao: WorkLogDao) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return WorkTrackerViewModel(dao) as T
        }
    }
}
