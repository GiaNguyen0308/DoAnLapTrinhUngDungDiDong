package com.example.n.ui.tasks

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.n.data.RepeatMode
import com.example.n.data.Task
import com.example.n.data.TaskCategory
import com.example.n.ui.viewmodel.SortOption
import com.example.n.ui.viewmodel.StatusFilter
import com.example.n.ui.viewmodel.TaskUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(
    uiState: TaskUiState,
    onSearchQueryChange: (String) -> Unit,
    onCategorySelected: (TaskCategory?) -> Unit,
    onSortOptionSelected: (SortOption) -> Unit,
    onStatusFilterSelected: (StatusFilter) -> Unit,
    onToggleTask: (String) -> Unit,
    onEditTask: (Task) -> Unit,
    onDeleteTask: (String) -> Unit
) {
    var sortDropdownExpanded by remember { mutableStateOf(false) }

    val filteredTasks = uiState.tasks
        .filter { task ->
            val matchesSearch = task.title.contains(uiState.searchQuery, ignoreCase = true) ||
                    task.description.contains(uiState.searchQuery, ignoreCase = true)
            val matchesCategory = (uiState.selectedCategory == null) || (task.category == uiState.selectedCategory)
            val matchesStatus = when (uiState.statusFilter) {
                StatusFilter.ALL -> true
                StatusFilter.PENDING -> !task.isCompleted
                StatusFilter.COMPLETED -> task.isCompleted
            }
            matchesSearch && matchesCategory && matchesStatus
        }
        .let { list ->
            when (uiState.sortOption) {
                SortOption.DUE_DATE -> list.sortedWith(compareBy({ it.dueDate }, { it.dueTime }))
                SortOption.PRIORITY -> list.sortedByDescending { it.priority.level }
                SortOption.TITLE -> list.sortedBy { it.title.lowercase() }
            }
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Search Bar
        OutlinedTextField(
            value = uiState.searchQuery,
            onValueChange = onSearchQueryChange,
            placeholder = { Text("Tìm kiếm công việc...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Tìm kiếm") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        // Status Filter Chips & Sort Selector Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(StatusFilter.entries) { filter ->
                    FilterChip(
                        selected = uiState.statusFilter == filter,
                        onClick = { onStatusFilterSelected(filter) },
                        label = { Text(filter.displayName) }
                    )
                }
            }

            Box {
                IconButton(onClick = { sortDropdownExpanded = true }) {
                    Icon(Icons.Default.FilterList, contentDescription = "Sắp xếp")
                }
                DropdownMenu(
                    expanded = sortDropdownExpanded,
                    onDismissRequest = { sortDropdownExpanded = false }
                ) {
                    SortOption.entries.forEach { option ->
                        DropdownMenuItem(
                            text = { Text("Sắp xếp: ${option.displayName}") },
                            onClick = {
                                onSortOptionSelected(option)
                                sortDropdownExpanded = false
                            }
                        )
                    }
                }
            }
        }

        // Category Filter Chips
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            item {
                FilterChip(
                    selected = uiState.selectedCategory == null,
                    onClick = { onCategorySelected(null) },
                    label = { Text("Tất cả danh mục") }
                )
            }
            items(TaskCategory.entries) { category ->
                FilterChip(
                    selected = uiState.selectedCategory == category,
                    onClick = { onCategorySelected(category) },
                    label = { Text(category.displayName) }
                )
            }
        }

        // Task List
        if (filteredTasks.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Không tìm thấy công việc nào",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredTasks) { task ->
                    TaskCard(
                        task = task,
                        onToggle = { onToggleTask(task.id) },
                        onEdit = { onEditTask(task) },
                        onDelete = { onDeleteTask(task.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun TaskCard(
    task: Task,
    onToggle: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = { onToggle() }
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    textDecoration = if (task.isCompleted) TextDecoration.LineThrough else null
                )
                if (task.description.isNotBlank()) {
                    Text(
                        text = task.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Badge { Text(task.category.displayName) }
                    Badge { Text("Ưu tiên: ${task.priority.displayName}") }
                    Badge { Text("Hạn: ${task.dueDate} ${task.dueTime}") }
                    if (task.repeatMode != RepeatMode.NONE) {
                        Badge { Text("🔁 ${task.repeatMode.displayName}") }
                    }
                }
            }

            Column {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = "Sửa", tint = MaterialTheme.colorScheme.primary)
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Xóa", tint = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}
