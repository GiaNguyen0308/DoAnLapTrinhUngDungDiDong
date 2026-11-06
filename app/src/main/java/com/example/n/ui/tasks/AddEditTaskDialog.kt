package com.example.n.ui.tasks

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.n.data.Task
import com.example.n.data.TaskCategory
import com.example.n.data.TaskPriority
import java.time.LocalDate
import java.time.LocalTime

data class TaskPreset(val title: String, val category: TaskCategory, val priority: TaskPriority)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTaskDialog(
    taskToEdit: Task? = null,
    onDismiss: () -> Unit,
    onSave: (Task) -> Unit
) {
    var title by remember { mutableStateOf(taskToEdit?.title ?: "") }
    var description by remember { mutableStateOf(taskToEdit?.description ?: "") }
    var category by remember { mutableStateOf(taskToEdit?.category ?: TaskCategory.WORK) }
    var priority by remember { mutableStateOf(taskToEdit?.priority ?: TaskPriority.MEDIUM) }
    var dueDateStr by remember { mutableStateOf(taskToEdit?.dueDate?.toString() ?: LocalDate.now().toString()) }
    var dueTimeStr by remember { mutableStateOf(taskToEdit?.dueTime?.toString() ?: "09:00") }
    var reminderEnabled by remember { mutableStateOf(taskToEdit?.reminderEnabled ?: true) }
    var showError by remember { mutableStateOf(false) }

    var categoryExpanded by remember { mutableStateOf(false) }
    var priorityExpanded by remember { mutableStateOf(false) }

    val presets = listOf(
        TaskPreset("Họp nhóm tiến độ", TaskCategory.WORK, TaskPriority.HIGH),
        TaskPreset("Đi tập gym 45 phút", TaskCategory.HEALTH, TaskPriority.MEDIUM),
        TaskPreset("Đi siêu thị mua đồ", TaskCategory.PERSONAL, TaskPriority.LOW),
        TaskPreset("Nộp bài tập về nhà", TaskCategory.STUDY, TaskPriority.HIGH),
        TaskPreset("Thanh toán hóa đơn", TaskCategory.FINANCE, TaskPriority.HIGH)
    )

    val timePresets = listOf("08:00", "10:00", "14:00", "17:30", "20:00")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (taskToEdit == null) "Thêm công việc mới" else "Chỉnh sửa công việc") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Quick Presets
                if (taskToEdit == null) {
                    Text("Gợi ý chọn nhanh mẫu công việc:", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(presets) { preset ->
                            SuggestionChip(
                                onClick = {
                                    title = preset.title
                                    category = preset.category
                                    priority = preset.priority
                                },
                                label = { Text(preset.title) }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = title,
                    onValueChange = {
                        title = it
                        if (it.isNotBlank()) showError = false
                    },
                    label = { Text("Tiêu đề công việc *") },
                    isError = showError,
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                if (showError) {
                    Text("Vui lòng nhập tiêu đề công việc!", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                }

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Mô tả chi tiết") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )

                // Category Dropdown
                ExposedDropdownMenuBox(
                    expanded = categoryExpanded,
                    onExpandedChange = { categoryExpanded = !categoryExpanded }
                ) {
                    OutlinedTextField(
                        value = category.displayName,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Danh mục phân loại") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) }
                    )
                    ExposedDropdownMenu(
                        expanded = categoryExpanded,
                        onDismissRequest = { categoryExpanded = false }
                    ) {
                        TaskCategory.entries.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text(cat.displayName) },
                                onClick = {
                                    category = cat
                                    categoryExpanded = false
                                }
                            )
                        }
                    }
                }

                // Priority Dropdown
                ExposedDropdownMenuBox(
                    expanded = priorityExpanded,
                    onExpandedChange = { priorityExpanded = !priorityExpanded }
                ) {
                    OutlinedTextField(
                        value = priority.displayName,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Mức độ ưu tiên") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = priorityExpanded) }
                    )
                    ExposedDropdownMenu(
                        expanded = priorityExpanded,
                        onDismissRequest = { priorityExpanded = false }
                    ) {
                        TaskPriority.entries.forEach { pri ->
                            DropdownMenuItem(
                                text = { Text(pri.displayName) },
                                onClick = {
                                    priority = pri
                                    priorityExpanded = false
                                }
                            )
                        }
                    }
                }

                // Due Date
                OutlinedTextField(
                    value = dueDateStr,
                    onValueChange = { dueDateStr = it },
                    label = { Text("Ngày hết hạn (YYYY-MM-DD)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // Due Time + Quick Time Selection
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    OutlinedTextField(
                        value = dueTimeStr,
                        onValueChange = { dueTimeStr = it },
                        label = { Text("Giờ hết hạn (HH:mm)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Text("Chọn giờ nhanh:", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(timePresets) { time ->
                            FilterChip(
                                selected = dueTimeStr == time,
                                onClick = { dueTimeStr = time },
                                label = { Text(time) }
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Bật nhắc nhở thông báo", fontWeight = FontWeight.Medium)
                    Switch(
                        checked = reminderEnabled,
                        onCheckedChange = { reminderEnabled = it }
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        val parsedDate = try {
                            LocalDate.parse(dueDateStr)
                        } catch (_: Exception) {
                            LocalDate.now()
                        }
                        val parsedTime = try {
                            LocalTime.parse(dueTimeStr)
                        } catch (_: Exception) {
                            LocalTime.of(9, 0)
                        }
                        val newTask = taskToEdit?.copy(
                            title = title,
                            description = description,
                            category = category,
                            priority = priority,
                            dueDate = parsedDate,
                            dueTime = parsedTime,
                            reminderEnabled = reminderEnabled
                        ) ?: Task(
                            title = title,
                            description = description,
                            category = category,
                            priority = priority,
                            dueDate = parsedDate,
                            dueTime = parsedTime,
                            reminderEnabled = reminderEnabled
                        )
                        onSave(newTask)
                    } else {
                        showError = true
                    }
                }
            ) {
                Text("Lưu")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Hủy")
            }
        }
    )
}
