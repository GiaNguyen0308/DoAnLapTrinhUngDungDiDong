package com.example.n.data

import java.time.LocalDate
import java.time.LocalTime

data class Task(
    val id: String = java.util.UUID.randomUUID().toString(),
    val title: String,
    val description: String = "",
    val category: TaskCategory = TaskCategory.WORK,
    val priority: TaskPriority = TaskPriority.MEDIUM,
    val dueDate: LocalDate = LocalDate.now(),
    val dueTime: LocalTime = LocalTime.of(9, 0),
    val isCompleted: Boolean = false,
    val reminderEnabled: Boolean = true
)

enum class TaskCategory(val displayName: String) {
    WORK("Công việc"),
    PERSONAL("Cá nhân"),
    STUDY("Học tập"),
    HEALTH("Sức khỏe"),
    FINANCE("Tài chính")
}

enum class TaskPriority(val displayName: String, val level: Int) {
    LOW("Thấp", 1),
    MEDIUM("Trung bình", 2),
    HIGH("Cao", 3)
}
