package com.herehearteam.herehear.ui.components

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.herehearteam.herehear.MainActivity
import com.herehearteam.herehear.domain.repository.UserRepository
import kotlinx.coroutines.flow.first

fun createNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = "Journal Mental Health Channel"
        val descriptionText = "Channel untuk notifikasi jurnal kesehatan mental"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel("JOURNAL_CHANNEL_ID", name, importance).apply {
            description = descriptionText
        }

        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}

suspend fun showJournalNotification(
    context: Context
) {
    // Cek izin notifikasi untuk Android 13+
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context as android.app.Activity,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                1
            )
            return
        }
    }

//    val user = userRepository.user.first()
    val userName = "Babi"

    createNotificationChannel(context)

    val deepLinkIntent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse("herehear://prediction")
    ).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                Intent.FLAG_ACTIVITY_CLEAR_TOP
    }

    val actionPendingIntent = PendingIntent.getActivity(
        context,
        0,
        deepLinkIntent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    // Bangun notifikasi dengan Deep Link
    val builder = NotificationCompat.Builder(context, "JOURNAL_CHANNEL_ID")
        .setSmallIcon(android.R.drawable.ic_dialog_info)
        .setContentTitle("Halo, $userName!")
        .setContentText("Kami telah menganalisis jurnal Anda untuk memahami kondisi mental Anda.")
        .setContentIntent(actionPendingIntent)
        .setAutoCancel(true)

    // Tampilkan notifikasi
    with(NotificationManagerCompat.from(context)) {
        notify(1, builder.build())
    }
}