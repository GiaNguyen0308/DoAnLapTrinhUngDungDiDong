package com.example.n

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.n.ui.calendar.CalendarScreen
import com.example.n.ui.components.BottomNavBar
import com.example.n.ui.dashboard.DashboardScreen
import com.example.n.ui.navigation.Screen
import com.example.n.ui.reminders.RemindersScreen
import com.example.n.ui.statistics.StatisticsScreen
import com.example.n.ui.tasks.AddEditTaskDialog
import com.example.n.ui.tasks.TaskListScreen
import com.example.n.ui.theme.ĐồÁnTheme
import com.example.n.ui.viewmodel.TaskViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ĐồÁnTheme {
                MainApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp() {
    val navController = rememberNavController()
    val viewModel: TaskViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Quản Lý Công Việc Hằng Ngày") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        bottomBar = {
            BottomNavBar(navController = navController)
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.showAddDialog() }) {
                Icon(Icons.Default.Add, contentDescription = "Thêm công việc")
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavHost(
                navController = navController,
                startDestination = Screen.Dashboard.route
            ) {
                composable(Screen.Dashboard.route) {
                    DashboardScreen(
                        uiState = uiState,
                        onTaskToggle = { viewModel.toggleTaskCompletion(it) },
                        onAddTaskClick = { viewModel.showAddDialog() },
                        onNavigateToTasks = { navController.navigate(Screen.Tasks.route) }
                    )
                }
                composable(Screen.Tasks.route) {
                    TaskListScreen(
                        uiState = uiState,
                        onSearchQueryChange = { viewModel.setSearchQuery(it) },
                        onCategorySelected = { viewModel.setSelectedCategory(it) },
                        onToggleTask = { viewModel.toggleTaskCompletion(it) },
                        onEditTask = { viewModel.showEditDialog(it) },
                        onDeleteTask = { viewModel.deleteTask(it) }
                    )
                }
                composable(Screen.Calendar.route) {
                    CalendarScreen(
                        uiState = uiState,
                        onToggleTask = { viewModel.toggleTaskCompletion(it) }
                    )
                }
                composable(Screen.Statistics.route) {
                    StatisticsScreen(uiState = uiState)
                }
                composable(Screen.Reminders.route) {
                    RemindersScreen(uiState = uiState)
                }
            }

            if (uiState.isAddEditDialogVisible) {
                AddEditTaskDialog(
                    taskToEdit = uiState.currentEditingTask,
                    onDismiss = { viewModel.hideAddEditDialog() },
                    onSave = { task -> viewModel.saveTask(task) }
                )
            }
        }
    }
}
