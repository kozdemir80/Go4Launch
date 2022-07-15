package com.example.go4launch.activities


import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.go4launch.R
import com.example.go4launch.adapters.AttendeesAdapter
import com.example.go4launch.api.NotificationInstance
import com.example.go4launch.constants.Constants.Companion.TOPIC
import com.example.go4launch.databinding.RestaurantDetailsActivityBinding
import com.example.go4launch.model.userdetails.CurrentUser
import com.example.go4launch.model.userdetails.NotificationData
import com.example.go4launch.model.userdetails.PushNotification
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.like.LikeButton
import com.like.OnLikeListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Suppress("NAME_SHADOWING")
class RestaurantDetails:AppCompatActivity() {
      private lateinit var binding: RestaurantDetailsActivityBinding
      private lateinit var recyclerView: RecyclerView
      private lateinit var adapter:AttendeesAdapter
      private lateinit var attendeesList: ArrayList<CurrentUser>
      private val TAG="RestaurantDetails"

      private lateinit var auth:FirebaseAuth
      private lateinit var title:TextView
      private lateinit var message:TextView



    @SuppressLint("ResourceAsColor", "StringFormatInvalid")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.restaurant_details_activity)
        setContentView(R.layout.natification_layout)

        binding = RestaurantDetailsActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        recyclerView = findViewById(R.id.restaurant_attendees_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.setHasFixedSize(true)
        attendeesList = arrayListOf()

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            val msg = getString(R.string.msg_token_fmt, token)
            Log.d(TAG, msg)
            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
        })
        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)

        val preferences = getSharedPreferences("myPreferences", MODE_PRIVATE)
        val editor = preferences.edit()
        val address1 = preferences.getString("address", null)
        val name1 = preferences.getString("name", null)

        val website = preferences.getString("website", null)
        val phoneNumber = preferences.getString("phone_number", null)
        val rating = preferences.getFloat("rating", 0.0f)
        val myImage = preferences.getString("image", null)


        binding.fabBook.setOnClickListener { view ->
            if (binding.fabBook.isChecked) {
                binding.fabBook.isChecked = true


                    editor.putString("Name", name1)
                    editor.apply()
                    auth = Firebase.auth
                    auth = FirebaseAuth.getInstance()
                    val name = auth.currentUser?.displayName
                    val photo = auth.currentUser?.photoUrl.toString()
                    val id = auth.currentUser?.uid
                    val user = CurrentUser(name, id, photo)
                    attendeesList.add(user)
                    recyclerView.adapter = AttendeesAdapter(attendeesList)


                        PushNotification(
                            NotificationData(name1, address1),
                            TOPIC

                        ).also {
                            sendNotification(it)
                        }

                 savedStateRegistry
            }
        }
        binding.restaurantImageView.load(myImage)
        binding.detailsRestaurantName.text = name1

        if (binding.detailsRestaurantName.text.isNotEmpty()) {
            binding.like.isVisible
        }
        binding.detailsRetaurantAddress.text = address1
        binding.btnCall.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse(phoneNumber)

            startActivity(intent)
        }
        binding.btnWebsite.setOnClickListener {
            try {

                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(website)

                startActivity(intent)
            } catch (e: NullPointerException) {
            }
        }

        binding.btnLike.setOnLikeListener(object : OnLikeListener {
            override fun liked(likeButton: LikeButton?) {
                if (binding.btnLike.isLiked) {
                    binding.like.isVisible
                } else {
                    binding.like.isInvisible
                }
            }

            override fun unLiked(likeButton: LikeButton?) {
                binding.like.isInvisible
            }

        })

    }

    private fun sendNotification(notification:PushNotification)= CoroutineScope(Dispatchers.IO).launch {
        try {
           val response=NotificationInstance.api.postNotification(notification)
            if (response.isSuccessful){

            }else{
                Log.e(TAG,response.errorBody().toString())
            }
        }catch (e:Exception){
            Log.e(TAG,e.toString())
        }
    }

    }
