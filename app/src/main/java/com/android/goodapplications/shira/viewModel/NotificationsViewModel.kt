package com.android.goodapplications.shira.viewModel

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.arch.lifecycle.ViewModel
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.support.v4.content.ContextCompat.getSystemService
import com.android.goodapplications.shira.R
import com.android.goodapplications.shira.view.activities.MainActivity

class NotificationsViewModel : ViewModel()
{
    private lateinit var mBuilder: NotificationCompat.Builder
    /*

Because you must create the notification channel before posting any notifications on Android 8.0 and higher,
 you should execute this code as soon as your app starts.
  It's safe to call this repeatedly because creating an existing notification channel performs no operation.
     */
    fun createNotificationChannel(con: Context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelID = con.getString(R.string.daily_artwork_notifications_ID)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelID, channelID, importance).apply {
                description = channelID
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                    con.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun initNotifications(context: Context, textTitle: String, textContent: String)
    {
        // Create an explicit intent for an Activity in your app
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
        mBuilder = NotificationCompat.Builder(context, context.getString(R.string.daily_artwork_notifications_ID))
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(textTitle)
                .setContentText(textContent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
    }

    fun showNotification(context: Context) {
        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define
            notify(1231232, mBuilder.build())
        }
    }

    /*
    var mBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
        .setSmallIcon(R.drawable.notification_icon)
        .setContentTitle("My notification")
        .setContentText("Much longer text that cannot fit one line...")
        .setStyle(NotificationCompat.BigTextStyle()
                .bigText("Much longer text that cannot fit one line..."))
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
     */
}