package com.example.n.data

import org.json.JSONObject
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
    val reminderEnabled: Boolean = true,
    val repeatMode: TaskRepeatMode = TaskRepeatMode.ONCE
) {
    fun toJson(): JSONObject {
        val json = JSONObject()
        json.put("id", id)
        json.put("title", title)
        json.put("description", description)
        json.put("category", category.name)
        json.put("priority", priority.name)
        json.put("dueDate", dueDate.toString())
        json.put("dueTime", dueTime.toString())
        json.put("isCompleted", isCompleted)
        json.put("reminderEnabled", reminderEnabled)
        json.put("repeatMode", repeatMode.name)
        return json
    }

    companion object {
        fun fromJson(json: JSONObject): Task {
            return Task(
                id = json.optString("id", java.util.UUID.randomUUID().toString()),
                title = json.optString("title", ""),
                description = json.optString("description", ""),
                category = try { TaskCategory.valueOf(json.optString("category", "WORK")) } catch (_: Exception) { TaskCategory.WORK },
                priority = try { TaskPriority.valueOf(json.optString("priority", "MEDIUM")) } catch (_: Exception) { TaskPriority.MEDIUM },
                dueDate = try { LocalDate.parse(json.optString("dueDate", LocalDate.now().toString())) } catch (_: Exception) { LocalDate.now() },
                dueTime = try { LocalTime.parse(json.optString("dueTime", "09:00")) } catch (_: Exception) { LocalTime.of(9, 0) },
                isCompleted = json.optBoolean("isCompleted", false),
                reminderEnabled = json.optBoolean("reminderEnabled", true),
                repeatMode = try { TaskRepeatMode.valueOf(json.optString("repeatMode", "ONCE")) } catch (_: Exception) { TaskRepeatMode.ONCE }
            )
        }
    }
}

enum class TaskRepeatMode(val displayName: String) {
    ONCE("Chỉ một lần"),
    DAILY("Lặp lại hằng ngày"),
    WEEKLY("Lặp lại hằng tuần"),
    MONTHLY("Lặp lại hằng tháng")
}

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
