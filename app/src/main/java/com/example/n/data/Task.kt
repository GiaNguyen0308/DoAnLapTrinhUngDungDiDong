package com.example.n.data

import java.time.LocalDate
import java.time.LocalTime

enum class RepeatMode(val displayName: String) {
    NONE("Không lặp"),
    DAILY("Hằng ngày"),
    WEEKLY("Hằng tuần"),
    MONTHLY("Hằng tháng")
}

data class Task(
    val id: String = java.util.UUID.randomUUID().toString(),
    val title: String,
    val description: String = "",
    val category: TaskCategory = TaskCategory.WORK,
    val priority: TaskPriority = TaskPriority.MEDIUM,
    val dueDate: LocalDate = LocalDate.now(),
    val dueTime: LocalTime = LocalTime.of(9, 0),
    val isCompleted: Boolean = false,
    val reminderEnabled: Boolean = true,
    val repeatMode: RepeatMode = RepeatMode.NONE
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
