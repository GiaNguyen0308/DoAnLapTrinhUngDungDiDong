package com.example.n.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class TaskNotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val taskId = intent.getStringExtra(NotificationHelper.EXTRA_TASK_ID) ?: return
        val title = intent.getStringExtra(NotificationHelper.EXTRA_TASK_TITLE) ?: "Nhắc nhở công việc"
        val desc = intent.getStringExtra(NotificationHelper.EXTRA_TASK_DESC) ?: ""

        NotificationHelper.showNotification(context, taskId, title, desc)
    }
}
