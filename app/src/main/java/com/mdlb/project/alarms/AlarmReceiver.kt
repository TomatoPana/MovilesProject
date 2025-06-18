package com.mdlb.project.alarms

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.mdlb.project.R

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val reminderId = intent.getStringExtra("REMINDER_ID")
        val reminderTitle = intent.getStringExtra("REMINDER_TITLE") ?: "Reminder"

        // Create and display a notification
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            "reminder_channel",
            "Reminders",
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(context, "reminder_channel")
            .setSmallIcon(R.drawable.ic_notification_icon)
            .setContentTitle("Reminder")
            .setContentText(reminderTitle)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        notificationManager.notify(reminderId.hashCode(), notification)

        // Here you can also update the reminder's state in Firebase
    }
}