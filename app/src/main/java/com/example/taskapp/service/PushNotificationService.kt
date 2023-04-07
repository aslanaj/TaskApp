package com.example.taskapp.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import com.example.taskapp.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class PushNotificationService : FirebaseMessagingService() {

    @SuppressLint("MissingPermission")
    override fun onMessageReceived(message: RemoteMessage) {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Heads Up Notification",
            NotificationManager.IMPORTANCE_HIGH
        )
        getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        Log.e("ololo", "onMessageReceived: ${message.notification?.title}")
        val notification = android.app.Notification.Builder(this, CHANNEL_ID)

        notification.apply {
            setContentTitle(message.notification?.title)
            setContentText(message.notification?.body)
            setSmallIcon(R.drawable.ic_launcher)
            setAutoCancel(true)
        }
        NotificationManagerCompat.from(this).notify(2, notification.build())
        super.onMessageReceived(message)
    }

    companion object {
        const val CHANNEL_ID = "channel.taskApp"
    }
}