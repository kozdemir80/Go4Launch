package com.example.go4launch.api
import com.example.go4launch.constants.Constants.Companion.content_type
import com.example.go4launch.constants.Constants.Companion.server_key
import com.example.go4launch.model.userdetails.PushNotification
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
interface NotificationApi {
  @Headers("Authorization: key=$server_key", "Content-Type:$content_type")
  @POST("fcm/send")
    suspend fun postNotification(
        @Body notification:PushNotification
    ):Response<okhttp3.ResponseBody>
}