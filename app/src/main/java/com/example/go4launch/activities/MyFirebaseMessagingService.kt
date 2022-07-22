package com.example.go4launch.activities

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
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

class MyFirebaseMessagingService:FirebaseMessagingService() {

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onMessageReceived(message: RemoteMessage) {
      val intent= Intent(this,RestaurantDetails::class.java)
      val notificationManager=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
      val notificationId= Random.nextInt()
      intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        notificationChannel(notificationManager)
        val remoteViews= RemoteViews("com.example.go4launch",R.layout.natification_layout)
        remoteViews.setTextViewText(R.id.notificationTitleTV,message.data["title"])
        remoteViews.setTextViewText(R.id.notificationDescTV,message.data["message"])
        remoteViews.setImageViewResource(R.id.notificationLogoIV,R.drawable.soup)

        val pendingIntent=PendingIntent.getActivity(this,0,intent, FLAG_IMMUTABLE)
      val notification=NotificationCompat.Builder(this,channelId)
          .setContentTitle(message.data["title"])
          .setContentText(message.data["message"])
          .setSmallIcon(R.drawable.soup)
          .setAutoCancel(true)
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