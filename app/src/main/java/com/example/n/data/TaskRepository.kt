package com.example.n.data

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.json.JSONArray

object TaskRepository {
    private const val PREFS_NAME = "task_manager_prefs"
    private const val KEY_TASKS_JSON = "key_tasks_json"

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()

    fun initRepository(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val jsonString = prefs.getString(KEY_TASKS_JSON, null)
        if (!jsonString.isNullOrBlank()) {
            try {
                val jsonArray = JSONArray(jsonString)
                val loadedTasks = mutableListOf<Task>()
                for (i in 0 until jsonArray.length()) {
                    val jsonObj = jsonArray.getJSONObject(i)
                    loadedTasks.add(Task.fromJson(jsonObj))
                }
                _tasks.value = loadedTasks
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun persistTasks(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val jsonArray = JSONArray()
        _tasks.value.forEach { task ->
            jsonArray.put(task.toJson())
        }
        prefs.edit().putString(KEY_TASKS_JSON, jsonArray.toString()).apply()
    }

    fun addTask(context: Context, task: Task) {
        _tasks.update { listOf(task) + it }
        persistTasks(context)
    }

    fun updateTask(context: Context, updatedTask: Task) {
        _tasks.update { list ->
            list.map { if (it.id == updatedTask.id) updatedTask else it }
        }
        persistTasks(context)
    }

    fun deleteTask(context: Context, taskId: String) {
        _tasks.update { list -> list.filter { it.id != taskId } }
        persistTasks(context)
    }

    fun toggleTaskCompletion(context: Context, taskId: String) {
        _tasks.update { list ->
            list.map { task ->
                if (task.id == taskId) {
                    val nextCompletionState = !task.isCompleted
                    if (nextCompletionState && task.repeatMode != TaskRepeatMode.ONCE) {
                        val nextDueDate = when (task.repeatMode) {
                            TaskRepeatMode.DAILY -> task.dueDate.plusDays(1)
                            TaskRepeatMode.WEEKLY -> task.dueDate.plusWeeks(1)
                            TaskRepeatMode.MONTHLY -> task.dueDate.plusMonths(1)
                            else -> task.dueDate
                        }
                        task.copy(dueDate = nextDueDate, isCompleted = false)
                    } else {
                        task.copy(isCompleted = nextCompletionState)
                    }
                } else {
                    task
                }
            }
        }
        persistTasks(context)
    }
}
