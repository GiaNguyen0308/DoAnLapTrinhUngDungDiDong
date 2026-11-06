package com.example.n.ui.viewmodel

import android.content.Context
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

data class TaskUiState(
    val tasks: List<Task> = emptyList(),
    val searchQuery: String = "",
    val selectedCategory: TaskCategory? = null,
    val isAddEditDialogVisible: Boolean = false,
    val currentEditingTask: Task? = null,
    val isDarkTheme: Boolean = false
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

    fun initRepository(context: Context) {
        TaskRepository.initRepository(context)
    }

    fun toggleTheme() {
        _uiState.update { it.copy(isDarkTheme = !it.isDarkTheme) }
    }

    fun setSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun setSelectedCategory(category: TaskCategory?) {
        _uiState.update { it.copy(selectedCategory = category) }
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

    fun saveTask(context: Context, task: Task) {
        if (_uiState.value.currentEditingTask == null) {
            TaskRepository.addTask(context, task)
        } else {
            TaskRepository.updateTask(context, task)
        }
        hideAddEditDialog()
    }

    fun deleteTask(context: Context, taskId: String) {
        TaskRepository.deleteTask(context, taskId)
    }

    fun toggleTaskCompletion(context: Context, taskId: String) {
        TaskRepository.toggleTaskCompletion(context, taskId)
    }
}
