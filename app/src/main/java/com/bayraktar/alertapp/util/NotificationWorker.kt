package com.bayraktar.alertapp.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.bayraktar.alertapp.R

class NotificationWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    override fun doWork(): Result {
        val title = "Hatırlatma"  // Varsayılan başlık
        val message = "Zamanı geldi! Hadi Harekete Geç!!!" // Varsayılan mesaj

        sendNotification(title, message)
        return Result.success()
    }

    private fun sendNotification(title: String, message: String) {
        val context = applicationContext
        val channelId = "notify_channel_id"



        // Bildirim sesi için MediaPlayer kullan (Alternatif: setSound kullanabilirsin)
        val mediaPlayer = MediaPlayer.create(context, R.raw.ses)
        mediaPlayer.start()
        mediaPlayer.setOnCompletionListener {
            it.release() // MediaPlayer'ı serbest bırak
        }

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.notification)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1, notification)
    }
}