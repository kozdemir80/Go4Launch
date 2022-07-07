package com.example.go4launch.activities

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.go4launch.R
import com.example.go4launch.constants.Constants
import com.google.firebase.messaging.FirebaseMessagingService

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class MyFirebaseMessagingService:FirebaseMessagingService() {


    @SuppressLint("UnspecifiedImmutableFlag")
    fun generateNotification(title:String, message:String){
        val intent= Intent(this,RestaurantDetails::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT)
        var builder:NotificationCompat.Builder=NotificationCompat.Builder(applicationContext,Constants.channelId)
            .setSmallIcon(R.drawable.soup)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000,1000,1000,1000))
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)
    }
}
