package com.mdlb.project.geofencing;

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import android.app.NotificationChannel
import android.app.NotificationManager
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.mdlb.project.R

class GeofenceBroadcastReceiver : BroadcastReceiver() {
    private val NOTIFICATION_CHANNEL_ID = "location_reminder_channel"
    private val NOTIFICATION_ID = 123

    override fun onReceive(context: Context, intent: Intent) {
        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        if (geofencingEvent != null && geofencingEvent.hasError()) {
            // Handle error
            return
        }

        val geofenceTransition = geofencingEvent!!.geofenceTransition
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
            val triggeringGeofences = geofencingEvent.triggeringGeofences
            triggeringGeofences?.forEach { geofence ->
                val reminderId = geofence.requestId
                // Here you would fetch the reminder details from Firestore using the reminderId
                // and then trigger a notification.
                sendNotification(context, "Reminder", "You are near your reminder location!")
            }
        }
    }

    fun sendNotification(context: Context, title: String, message: String) {
        val notificationManager = ContextCompat.getSystemService(
            context,
            NotificationManager::class.java
        ) as NotificationManager

        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            "Location Reminders",
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification_icon) // Make sure you have a notification icon
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        notificationManager.notify(NOTIFICATION_ID, notification)
    }
}