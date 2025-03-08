package com.bayraktar.alertapp

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.bayraktar.alertapp.ui.NotificationSettingsScreen
import com.bayraktar.alertapp.ui.theme.AlertAppTheme
import com.bayraktar.alertapp.util.NotificationWorker
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createNotificationChannel()

        setContent {
            AlertAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    NotificationSettingsScreen { selectedTime, timeUnit ->
                        startPeriodicNotification(selectedTime, timeUnit)
                    }
                }
            }
        }
    }


    // Bildirimleri belirli aralıklarla gönderen fonksiyon
    private fun startPeriodicNotification(repeatTime: Long, timeUnit: TimeUnit) {
        val workRequest = PeriodicWorkRequestBuilder<NotificationWorker>(repeatTime, timeUnit)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "notification_work",
            ExistingPeriodicWorkPolicy.REPLACE,
            workRequest
        )

        Toast.makeText(this, "$repeatTime ve ${timeUnit.name.toLowerCase()} sonra bildirim tekrarlanacak!", Toast.LENGTH_SHORT).show()
    }
    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            "notify_channel_id",
            "Reminder Notifications",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "This channel is for reminder notifications"
        }

        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }

}
