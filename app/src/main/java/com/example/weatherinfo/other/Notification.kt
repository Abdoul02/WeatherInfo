package com.example.weatherinfo.other

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import com.example.weatherinfo.MainActivity
import com.example.weatherinfo.MyApplication.Companion.CHANEL_ID
import com.example.weatherinfo.MyApplication.Companion.NOTIFICATION_REQUEST_CODE
import com.example.weatherinfo.R

object Notification {

    fun showNotification(context: Context, message: String) {
        val notificationIntent = Intent(context, MainActivity::class.java)
        notificationIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        val stackBuilder = TaskStackBuilder.create(context)
        stackBuilder.addParentStack(MainActivity::class.java)
        stackBuilder.addNextIntent(notificationIntent)
        val pendingIntent = stackBuilder.getPendingIntent(NOTIFICATION_REQUEST_CODE, PendingIntent.FLAG_UPDATE_CURRENT)
        val builder = NotificationCompat.Builder(context, CHANEL_ID)
        val notification = builder
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentTitle("Weather Report")
            .setContentText(message)
            .setContentIntent(pendingIntent)
            .build()
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_REQUEST_CODE, notification)
    }
}