package com.example.n.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.n.data.Task
import com.example.n.data.TaskCategory
import com.example.n.data.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class SortOption(val displayName: String) {
    DUE_DATE("Hạn gần nhất"),
    PRIORITY("Ưu tiên (Cao -> Thấp)"),
    TITLE("Tên (A-Z)")
}

enum class StatusFilter(val displayName: String) {
    ALL("Tất cả"),
    PENDING("Chưa làm"),
    COMPLETED("Đã hoàn thành")
}

data class TaskUiState(
    val tasks: List<Task> = emptyList(),
    val searchQuery: String = "",
    val selectedCategory: TaskCategory? = null,
    val sortOption: SortOption = SortOption.DUE_DATE,
    val statusFilter: StatusFilter = StatusFilter.ALL,
    val isDarkMode: Boolean = false,
    val isAddEditDialogVisible: Boolean = false,
    val currentEditingTask: Task? = null
)

class TaskViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(TaskUiState())
    val uiState: StateFlow<TaskUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            TaskRepository.tasks.collect { allTasks ->
                _uiState.update { it.copy(tasks = allTasks) }
            }
        }
    }

    fun setSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun setSelectedCategory(category: TaskCategory?) {
        _uiState.update { it.copy(selectedCategory = category) }
    }

    fun setSortOption(option: SortOption) {
        _uiState.update { it.copy(sortOption = option) }
    }

    fun setStatusFilter(filter: StatusFilter) {
        _uiState.update { it.copy(statusFilter = filter) }
    }

    fun toggleDarkMode() {
        _uiState.update { it.copy(isDarkMode = !it.isDarkMode) }
    }

    fun showAddDialog() {
        _uiState.update { it.copy(isAddEditDialogVisible = true, currentEditingTask = null) }
    }

    fun showEditDialog(task: Task) {
        _uiState.update { it.copy(isAddEditDialogVisible = true, currentEditingTask = task) }
    }

    fun hideAddEditDialog() {
        _uiState.update { it.copy(isAddEditDialogVisible = false, currentEditingTask = null) }
    }

    fun saveTask(task: Task) {
        if (_uiState.value.currentEditingTask == null) {
            TaskRepository.addTask(task)
        } else {
            TaskRepository.updateTask(task)
        }
        hideAddEditDialog()
    }

    fun deleteTask(taskId: String) {
        TaskRepository.deleteTask(taskId)
    }

    fun toggleTaskCompletion(taskId: String) {
        TaskRepository.toggleTaskCompletion(taskId)
    }
}
