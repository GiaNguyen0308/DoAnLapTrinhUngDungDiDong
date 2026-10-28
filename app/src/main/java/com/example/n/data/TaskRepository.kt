package com.example.n.data

import android.content.Context
import android.content.SharedPreferences
import com.example.n.notification.NotificationHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalDate
import java.time.LocalTime

object TaskRepository {
    private const val PREF_NAME = "task_prefs"
    private const val KEY_TASKS = "saved_tasks"

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()

    private var appContext: Context? = null
    private var sharedPreferences: SharedPreferences? = null

    fun init(context: Context) {
        if (sharedPreferences == null) {
            appContext = context.applicationContext
            sharedPreferences = appContext?.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            appContext?.let { NotificationHelper.createNotificationChannel(it) }
            loadTasks()
        }
    }

    private fun loadTasks() {
        val prefs = sharedPreferences ?: return
        val jsonString = prefs.getString(KEY_TASKS, null) ?: return
        try {
            val jsonArray = JSONArray(jsonString)
            val loadedList = mutableListOf<Task>()
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val task = Task(
                    id = jsonObject.optString("id", java.util.UUID.randomUUID().toString()),
                    title = jsonObject.getString("title"),
                    description = jsonObject.optString("description", ""),
                    category = try { TaskCategory.valueOf(jsonObject.optString("category", TaskCategory.WORK.name)) } catch (_: Exception) { TaskCategory.WORK },
                    priority = try { TaskPriority.valueOf(jsonObject.optString("priority", TaskPriority.MEDIUM.name)) } catch (_: Exception) { TaskPriority.MEDIUM },
                    dueDate = try { LocalDate.parse(jsonObject.getString("dueDate")) } catch (_: Exception) { LocalDate.now() },
                    dueTime = try { LocalTime.parse(jsonObject.getString("dueTime")) } catch (_: Exception) { LocalTime.of(9, 0) },
                    isCompleted = jsonObject.optBoolean("isCompleted", false),
                    reminderEnabled = jsonObject.optBoolean("reminderEnabled", true),
                    repeatMode = try { RepeatMode.valueOf(jsonObject.optString("repeatMode", RepeatMode.NONE.name)) } catch (_: Exception) { RepeatMode.NONE }
                )
                loadedList.add(task)
            }
            _tasks.value = loadedList
            appContext?.let { ctx ->
                loadedList.forEach { NotificationHelper.scheduleNotification(ctx, it) }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun saveTasks(tasks: List<Task>) {
        val prefs = sharedPreferences ?: return
        try {
            val jsonArray = JSONArray()
            for (task in tasks) {
                val jsonObject = JSONObject().apply {
                    put("id", task.id)
                    put("title", task.title)
                    put("description", task.description)
                    put("category", task.category.name)
                    put("priority", task.priority.name)
                    put("dueDate", task.dueDate.toString())
                    put("dueTime", task.dueTime.toString())
                    put("isCompleted", task.isCompleted)
                    put("reminderEnabled", task.reminderEnabled)
                    put("repeatMode", task.repeatMode.name)
                }
                jsonArray.put(jsonObject)
            }
            prefs.edit().putString(KEY_TASKS, jsonArray.toString()).apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun addTask(task: Task) {
        _tasks.update { list ->
            val newList = listOf(task) + list
            saveTasks(newList)
            appContext?.let { NotificationHelper.scheduleNotification(it, task) }
            newList
        }
    }

    fun updateTask(updatedTask: Task) {
        _tasks.update { list ->
            val newList = list.map { if (it.id == updatedTask.id) updatedTask else it }
            saveTasks(newList)
            appContext?.let { NotificationHelper.scheduleNotification(it, updatedTask) }
            newList
        }
    }

    fun deleteTask(taskId: String) {
        _tasks.update { list ->
            val newList = list.filter { it.id != taskId }
            saveTasks(newList)
            appContext?.let { NotificationHelper.cancelNotification(it, taskId) }
            newList
        }
    }

    fun toggleTaskCompletion(taskId: String) {
        _tasks.update { list ->
            val updatedList = mutableListOf<Task>()
            for (task in list) {
                if (task.id == taskId) {
                    val newCompletedState = !task.isCompleted
                    val updatedTask = task.copy(isCompleted = newCompletedState)
                    updatedList.add(updatedTask)

                    if (newCompletedState) {
                        appContext?.let { NotificationHelper.cancelNotification(it, taskId) }
                        if (task.repeatMode != RepeatMode.NONE) {
                            val nextDueDate = when (task.repeatMode) {
                                RepeatMode.DAILY -> task.dueDate.plusDays(1)
                                RepeatMode.WEEKLY -> task.dueDate.plusWeeks(1)
                                RepeatMode.MONTHLY -> task.dueDate.plusMonths(1)
                                else -> task.dueDate
                            }
                            val nextTask = task.copy(
                                id = java.util.UUID.randomUUID().toString(),
                                dueDate = nextDueDate,
                                isCompleted = false
                            )
                            updatedList.add(0, nextTask)
                            appContext?.let { NotificationHelper.scheduleNotification(it, nextTask) }
                        }
                    } else {
                        appContext?.let { NotificationHelper.scheduleNotification(it, updatedTask) }
                    }
                } else {
                    updatedList.add(task)
                }
            }
            saveTasks(updatedList)
            updatedList
        }
    }

    fun saveCurrentTasks() {
        saveTasks(_tasks.value)
    }
}
