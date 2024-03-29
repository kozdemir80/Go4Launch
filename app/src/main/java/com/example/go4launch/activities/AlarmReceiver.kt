package com.example.go4launch.activities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.go4launch.api.NotificationInstance
import com.example.go4launch.constants.Constants.Companion.TOPIC
import com.example.go4launch.model.userdetails.NotificationData
import com.example.go4launch.model.userdetails.PushNotification
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlarmReceiver : BroadcastReceiver() {
    private val tAG = "restaurantDetails"

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onReceive(context: Context?, intent: Intent?) {
        Intent(context, MyFirebaseMessagingService::class.java)
        intent?.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val preferences =
            context?.getSharedPreferences("myPreferences", AppCompatActivity.MODE_PRIVATE)
        val address1 = preferences!!.getString("address", null)
        val name1 = preferences.getString("name", null)
        val user = preferences.getStringSet("user", null)
        // push notification topic items
        PushNotification(
            NotificationData(name1, address1, user.toString()),
            TOPIC
        ).also {
            sendNotification(it)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun sendNotification(notification: PushNotification) =
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = NotificationInstance.api.postNotification(notification)
                if (response.isSuccessful) {
                    Log.e(tAG, response.message().toString())
                } else {
                    Log.e(tAG, response.errorBody().toString())
                }
            } catch (e: Exception) {
                Log.e(tAG, e.toString())
            }
        }
}