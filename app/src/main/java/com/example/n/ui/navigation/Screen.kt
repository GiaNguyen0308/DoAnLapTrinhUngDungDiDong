package com.example.n.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Dashboard : Screen("dashboard", "Tổng quan", Icons.Default.Home)
    object Tasks : Screen("tasks", "Công việc", Icons.Default.CheckCircle)
    object Calendar : Screen("calendar", "Lịch", Icons.Default.CalendarMonth)
    object Statistics : Screen("statistics", "Thống kê", Icons.Default.BarChart)
    object Reminders : Screen("reminders", "Nhắc nhở", Icons.Default.Notifications)
}
