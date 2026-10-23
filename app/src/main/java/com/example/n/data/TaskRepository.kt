package com.example.n.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import java.time.LocalTime

object TaskRepository {
    private val initialTasks = listOf(
        Task(
            title = "Họp tiến độ dự án tuần",
            description = "Báo cáo tiến độ hoàn thành các tính năng với trưởng nhóm",
            category = TaskCategory.WORK,
            priority = TaskPriority.HIGH,
            dueDate = LocalDate.now(),
            dueTime = LocalTime.of(10, 0),
            isCompleted = false
        ),
        Task(
            title = "Đi tập gym 45 phút",
            description = "Bài tập cardio và tạ nhẹ buổi chiều",
            category = TaskCategory.HEALTH,
            priority = TaskPriority.MEDIUM,
            dueDate = LocalDate.now(),
            dueTime = LocalTime.of(17, 30),
            isCompleted = true
        ),
        Task(
            title = "Nộp báo cáo tài chính tháng",
            description = "Tổng hợp thu chi và hóa đơn chứng từ",
            category = TaskCategory.FINANCE,
            priority = TaskPriority.HIGH,
            dueDate = LocalDate.now().plusDays(1),
            dueTime = LocalTime.of(15, 0),
            isCompleted = false
        ),
        Task(
            title = "Đọc sách lập trình Android",
            description = "Đọc chương về Jetpack Compose Navigation và State Management",
            category = TaskCategory.STUDY,
            priority = TaskPriority.LOW,
            dueDate = LocalDate.now().plusDays(2),
            dueTime = LocalTime.of(20, 0),
            isCompleted = false
        )
    )

    private val _tasks = MutableStateFlow(initialTasks)
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()

    fun addTask(task: Task) {
        _tasks.update { listOf(task) + it }
    }

    fun updateTask(updatedTask: Task) {
        _tasks.update { list ->
            list.map { if (it.id == updatedTask.id) updatedTask else it }
        }
    }

    fun deleteTask(taskId: String) {
        _tasks.update { list -> list.filter { it.id != taskId } }
    }

    fun toggleTaskCompletion(taskId: String) {
        _tasks.update { list ->
            list.map { if (it.id == taskId) it.copy(isCompleted = !it.isCompleted) else it }
        }
    }
}
