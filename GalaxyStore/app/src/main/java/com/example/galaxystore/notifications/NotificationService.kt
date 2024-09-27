package com.example.galaxystore.notifications

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.galaxystore.R
import com.example.galaxystore.activity.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlin.random.Random

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class NotificationService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val channelId = "GalaxyStore"

        val channel = NotificationChannel(channelId,"Galaxy",NotificationManager.IMPORTANCE_HIGH).apply {
            description = "Galaxy message"
            enableLights(true)
        }

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)

        val pendingIntent = PendingIntent.getActivity(this,0, Intent(this,MainActivity::class.java),PendingIntent.FLAG_UPDATE_CURRENT)

        val notification = NotificationCompat.Builder(this,channelId)
                           .setContentTitle(message.data["title"])
                           .setContentText(message.data["body"])
                           .setSmallIcon(R.drawable.van)
                           .setContentIntent(pendingIntent)
                           .setAutoCancel(true)
                           .build()

        manager.notify(Random.nextInt(),notification)
    }

}