package com.ivors.everything.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(tableName = "habits")
data class Habit(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val icon: String, // Resource name or Emoji
    val color: Int, // Color hex
    val frequency: Int = 1, // Times per day
    val targetDays: String = "1234567" // 1-7 for Mon-Sun
)

@Entity(
    tableName = "habit_completions",
    foreignKeys = [
        ForeignKey(
            entity = Habit::class,
            parentColumns = ["id"],
            childColumns = ["habitId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("habitId")]
)
data class HabitCompletion(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val habitId: Long,
    val timestamp: Long, // Epoch milli
    val date: String // YYYY-MM-DD for easier querying
)
