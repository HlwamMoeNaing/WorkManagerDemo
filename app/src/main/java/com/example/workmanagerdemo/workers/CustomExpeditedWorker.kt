package com.example.workmanagerdemo.workers

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.ServiceInfo
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.workmanagerdemo.R
import com.example.workmanagerdemo.util.sendNotification
import kotlinx.coroutines.delay

class CustomExpeditedWorker constructor(
    context: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {

    // This is for Expedited WorkManager
    val channelId = "m_channel_id"
    override suspend fun doWork(): Result {
        try {
            setForeground(getForegroundInf(applicationContext)) // This is to get foreground notification for higher version above android 12
        } catch (e: Exception) {
            Log.d("MCustomWorker", "Fail")
            return Result.failure()
        }
        delay(20000)
        Log.d("MCustomWorker", "Success")
        return Result.success()
    }

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return getForegroundInf(applicationContext)
    }

    private fun getForegroundInf(context: Context): ForegroundInfo {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ForegroundInfo(
                1,
                createNotification(context = context),
                ServiceInfo.FOREGROUND_SERVICE_TYPE_NONE
            )
        } else {
            ForegroundInfo(
                1,
                createNotification(context)
            )
        }
    }

    private fun createNotification(context: Context): Notification {
        val channelId = "main_channel_id"
        val channelName = "Main Channel"
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("Notification Title")
            .setContentText("This is my first notification")
            .setOngoing(true)
            .setAutoCancel(true)

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }


        return builder.build()
    }


    fun createForegroundNotification(
        context: Context,
        channelId: String,
        notificationId: Int
    ): ForegroundInfo {
        val notification = buildNotification(context, channelId)
        return ForegroundInfo(notificationId, notification)
    }

    private fun buildNotification(context: Context, channelId: String): Notification {
        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setContentTitle("Foreground Service")
            .setContentText("Running in the background")
            .setSmallIcon(R.drawable.ic_launcher_background)
        // Add any other desired configuration to the notification

        // Create a notification channel for Android Oreo and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(context, channelId)
        }

        return notificationBuilder.build()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(context: Context, channelId: String) {
        val channelName = "Foreground Service Channel"
        val channelDescription = "Channel for foreground service"
        val importance = NotificationManager.IMPORTANCE_DEFAULT

        val channel = NotificationChannel(channelId, channelName, importance).apply {
            description = channelDescription
        }

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}