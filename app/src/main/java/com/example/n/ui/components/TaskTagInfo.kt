package com.example.n.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.n.data.Task
import com.example.n.data.TaskPriority

@Composable
fun TaskChip(
    text: String,
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer
) {
    Surface(
        color = containerColor,
        contentColor = contentColor,
        shape = RoundedCornerShape(6.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Medium,
            maxLines = 1,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
        )
    }
}

@Composable
fun TaskTagInfo(task: Task) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            TaskChip(
                text = task.category.displayName,
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
            TaskChip(
                text = "Ưu tiên: ${task.priority.displayName}",
                containerColor = when (task.priority) {
                    TaskPriority.HIGH -> MaterialTheme.colorScheme.errorContainer
                    TaskPriority.MEDIUM -> MaterialTheme.colorScheme.secondaryContainer
                    TaskPriority.LOW -> MaterialTheme.colorScheme.tertiaryContainer
                },
                contentColor = when (task.priority) {
                    TaskPriority.HIGH -> MaterialTheme.colorScheme.onErrorContainer
                    TaskPriority.MEDIUM -> MaterialTheme.colorScheme.onSecondaryContainer
                    TaskPriority.LOW -> MaterialTheme.colorScheme.onTertiaryContainer
                }
            )
        }
        TaskChip(
            text = "⏰ Hạn: ${task.dueDate} lúc ${task.dueTime}",
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
