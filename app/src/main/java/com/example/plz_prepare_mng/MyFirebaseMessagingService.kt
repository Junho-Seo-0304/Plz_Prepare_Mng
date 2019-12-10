package com.example.plz_prepare_mng

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

// 푸쉬알람을 받기 위해 서비스를 선언
class MyFirebaseMessagingService : FirebaseMessagingService() {
    private val TAG = "FirebaseService"

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // 여기서의 토큰은 푸쉬 받을 기기에 대한 토큰 즉, 현 기기의 토큰
        Log.d(TAG, "new Token: $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // FCM에서 메세지를 받으면 sendNotification 함수를 이용해서 푸쉬알람을 띄운다.
        Log.d(TAG, "From: " + remoteMessage.from)
        if(remoteMessage.notification != null) {
            Log.d(TAG, "Notification Message Body: ${remoteMessage.notification?.body}")
            sendNotification(remoteMessage.notification?.body)
        }
    }

    private fun sendNotification(body: String?) {
        // 푸쉬 알람을 띄우는 함수
        val intent = Intent(this, UserMainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("Notification", body)
        }

        val CHANNEL_ID = "Notification"
        val CHANNEL_NAME = "Channel"
        val description = "This is plz-prepare"
        val importance = NotificationManager.IMPORTANCE_HIGH
        // importance에 따라 푸쉬알람에 대한 설정이 바뀐다. IMPORTANCE_HIGH는 푸쉬알람과 소리까지 나는 것이다.

        val notificationManager: NotificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if( android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance)
            channel.description = description
            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.enableVibration(true)
            channel.setShowBadge(false)
            notificationManager.createNotificationChannel(channel)
        }
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        val notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationBuilder = NotificationCompat.Builder(this,"Notification")
            .setSmallIcon(R.mipmap.ic_launcher_icon)
            .setContentTitle("Push Notification FCM")
            .setContentText(body)
            .setAutoCancel(true)
            .setSound(notificationSound)
            .setContentIntent(pendingIntent)

        notificationManager.notify(0, notificationBuilder.build())
    }
}