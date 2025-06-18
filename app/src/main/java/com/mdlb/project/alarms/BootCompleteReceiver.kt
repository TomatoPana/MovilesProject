package com.mdlb.project.alarms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BootCompletedReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            // Re-schedule alarms from Firebase
            CoroutineScope(Dispatchers.IO).launch {
                rescheduleAlarms(context)
            }

            // Re-register Geofences
            CoroutineScope(Dispatchers.IO).launch {
                registerGeofences(context)
            }
        }
    }

    private suspend fun rescheduleAlarms(context: Context) {
        val db = FirebaseFirestore.getInstance()
        // Fetch all active reminders from Firebase for the current user
        // This requires you to have the user's ID available, which can be stored in SharedPreferences
        val userId = "current_user_id" // Replace with your logic to get the logged-in user's ID
        db.collection("reminders")
            .whereEqualTo("userId", userId)
            .whereEqualTo("isScheduled", true)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val reminderId = document.id
                    val title = document.getString("title") ?: ""
                    val timestamp = document.getLong("timestamp") ?: 0

                    if (timestamp > System.currentTimeMillis()) {
                        // TODO: Implement the Scheduler
                        // scheduleAlarm(context, reminderId, title, timestamp)
                    }
                }
            }
            .addOnFailureListener {
                // Handle failure
            }
    }

    private suspend fun registerGeofences(context: Context) {
        // Fetch all active reminders from Firebase for the current user
        // TODO: Implement this logic
    }
}