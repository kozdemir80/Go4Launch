package com.example.go4launch.activities
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.go4launch.R
import com.example.go4launch.constants.Constants.Companion.channelId


import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlin.random.Random
@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class MyFirebaseMessagingService:FirebaseMessagingService() {
    @SuppressLint("UnspecifiedImmutableFlag")
    @RequiresApi(Build.VERSION_CODES.M)

    override fun onMessageReceived(message: RemoteMessage) {
        val intent= Intent(this@MyFirebaseMessagingService,RestaurantDetails::class.java)
        val notificationManager=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationId= Random.nextInt()
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        notificationChannel(notificationManager)
        val pendingIntent= PendingIntent.getActivity(applicationContext,0,intent,
            PendingIntent.FLAG_ONE_SHOT)
        val remoteViews= RemoteViews("com.example.go4launch", R.layout.natification_layout)
        remoteViews.setTextViewText(R.id.notificationTitleTV,message.data["title"])
        remoteViews.setTextViewText(R.id.notificationDescTV,message.data["message"])
        remoteViews.setTextViewText(R.id.userList,message.data["userList\n"])
        remoteViews.setImageViewResource(R.id.notificationLogoIV, R.drawable.soup)
        val notification=
            NotificationCompat.Builder(applicationContext,channelId)
                .setSmallIcon(R.drawable.soup)
                .setCustomContentView(remoteViews)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent)
                .build()
        notificationManager.notify(notificationId,notification)

    }
    private fun notificationChannel(notificationManager: NotificationManager){
        val channelName="channelName"
        val channel= if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel(channelId,channelName,IMPORTANCE_HIGH).apply {
                description="My Channel Description"
                enableLights(true)
                lightColor= Color.GREEN
            }
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        notificationManager.createNotificationChannel(channel)
    }

}
