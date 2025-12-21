package com.ivors.everything.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for WorkLog entities.
 */
@Dao
interface WorkLogDao {
    
    @Insert
    suspend fun insert(workLog: WorkLog)
    
    /**
     * Get all logs for a specific day.
     * @param startOfDay Start of the day in milliseconds.
     * @param endOfDay End of the day in milliseconds.
     */
    @Query("SELECT * FROM work_logs WHERE timestamp >= :startOfDay AND timestamp < :endOfDay ORDER BY timestamp ASC")
    fun getLogsForDay(startOfDay: Long, endOfDay: Long): Flow<List<WorkLog>>

    @Query("SELECT * FROM work_logs WHERE timestamp >= :startTime AND timestamp <= :endTime ORDER BY timestamp ASC")
    fun getLogsInRange(startTime: Long, endTime: Long): Flow<List<WorkLog>>
    
    @Query("SELECT * FROM work_logs ORDER BY timestamp DESC")
    fun getAllLogs(): Flow<List<WorkLog>>
    
    @Query("DELETE FROM work_logs WHERE id = :id")
    suspend fun deleteById(id: Long)
    
    /**
     * Get the most recent log entry to determine current state.
     */
    @Query("SELECT * FROM work_logs ORDER BY timestamp DESC LIMIT 1")
    fun getLastLog(): Flow<WorkLog?>
}
