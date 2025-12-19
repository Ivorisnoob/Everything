package com.ivors.everything.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a single work log entry.
 * @param id Unique identifier for the log entry.
 * @param timestamp The time of the event in milliseconds since epoch.
 * @param eventType The type of event: "IN" for walk-in, "OUT" for walk-out.
 */
@Entity(tableName = "work_logs")
data class WorkLog(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val timestamp: Long,
    val eventType: String // "IN" or "OUT"
)
