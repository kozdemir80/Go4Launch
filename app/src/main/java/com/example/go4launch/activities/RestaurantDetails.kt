package com.example.go4launch.activities

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.go4launch.R
import com.example.go4launch.adapters.AttendeesAdapter
import com.example.go4launch.constants.Constants.Companion.TOPIC
import com.example.go4launch.databinding.RestaurantDetailsActivityBinding
import com.example.go4launch.model.restaturantDetails.Result
import com.example.go4launch.model.userdetails.CurrentUser
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.like.LikeButton
import com.like.OnLikeListener
import java.util.*

@Suppress("NAME_SHADOWING")
class RestaurantDetails : AppCompatActivity() {
    private lateinit var binding: RestaurantDetailsActivityBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var attendeesList: ArrayList<CurrentUser>
    private val tAG = "restaurantDetails"
    private lateinit var calendar: Calendar
    private lateinit var auth: FirebaseAuth
    private lateinit var alarmManager: AlarmManager
    private lateinit var database: DatabaseReference
    private var myUsers = mutableSetOf<String>()

    @RequiresApi(Build.VERSION_CODES.M)
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
        val title = intent.getStringExtra("title")
        val myJson = intent.getStringExtra("markerTag")
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(tAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            // Get new FCM registration token
            val token = task.result
            // Log and toast
            val msg = getString(R.string.msg_token_fmt, token)
            Log.d(tAG, msg)
            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
        })
        /*
         * Subscribe the push notification topic
         */
        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)
        try {
            listDetails()
            /*
            * Restaurants Details from the mapview
             */
            val json = Gson()
            // Fetches the restaurant details from mapview marker
            val details = json.fromJson(myJson, Result::class.java)
            Log.d("myTagg",details.toString())
            binding.fabBook.setOnClickListener {
                if (binding.fabBook.isChecked) {
                    binding.fabBook.isChecked = true
                    val preferences = getSharedPreferences("myPreferences", MODE_PRIVATE)
                    val editor = preferences.edit()
                    auth = Firebase.auth
                    auth = FirebaseAuth.getInstance()
                    database = FirebaseDatabase.getInstance().getReference("Users")
                    database.addValueEventListener(object : ValueEventListener {
                        @SuppressLint("SetTextI18n")
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                for (i in snapshot.children) {
                                        editor.putString("address", details.vicinity)
                                        editor.putString("name", details.name)
                                        // Fetches the user details from firebase database
                                        val user = i.getValue(CurrentUser::class.java)
                                        if (user != null && user.restaurantId == details.name) {
                                            attendeesList.add(user)
                                            myUsers.add(user.Name!!.toString().replace("\n",
                                                System.getProperty("line.separator")!!))
                                        }
                                        // displays the list of attending colleagues to the current restaurant
                                        recyclerView.adapter = AttendeesAdapter(attendeesList)
                                    }
                                }
                                editor.putStringSet("user", myUsers)
                                editor.apply()
                                setAlarm()
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }
                    })
                    savedStateRegistry
                }
            }
            binding.detailsRestaurantName.text = title
            if (binding.detailsRestaurantName.text.isNotEmpty()) {
                binding.like.isVisible
            }
            binding.detailsRetaurantAddress.text = details.vicinity
            // to call restaurants phone
            binding.btnCall.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse(details.formatted_phone_number)
                startActivity(intent)
            }
            // to go restaurants website
            binding.btnWebsite.setOnClickListener {
                try {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(details.website)

                    startActivity(intent)
                } catch (e: NullPointerException) {
                }
            }
            // Like button
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
        } catch (e: NullPointerException) {
        }
    }

    /*
     * Show's the restaurant details from ListView Fragment
     */
    @SuppressLint("StringFormatInvalid")
    private fun listDetails() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(tAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            // Get new FCM registration token
            val token = task.result
            // Log and toast
            val msg = getString(R.string.msg_token_fmt, token)
            Log.d(tAG, msg)
            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
        })
        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)
        try {
        val address1 = intent.getStringExtra("address")
        val name1 = intent.getStringExtra("name")
        val website = intent.getStringExtra("website")
        val phoneNumber = intent.getStringExtra("phone_number")
        val restaurantLat=intent.getDoubleArrayExtra("myLat")
        val restaurantLng=intent.getDoubleArrayExtra("myLng")
        val myImage = intent.getStringExtra("image")
        // to go restaurants website
        binding.btnWebsite.setOnClickListener {
            try {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(website)

                startActivity(intent)
            } catch (e: NullPointerException) {
            }
        }
        // to dial phone number of the restaurant
        binding.btnCall.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse(phoneNumber)

            startActivity(intent)
        }
        // like button
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
        if (binding.detailsRestaurantName.text.isNotEmpty()) {
            binding.like.isVisible
        }
        binding.detailsRetaurantAddress.text = address1
        binding.restaurantImageView.load(myImage)
        binding.detailsRestaurantName.text = name1
        binding.fabBook.setOnClickListener {
            if (binding.fabBook.isChecked) {
                binding.fabBook.isChecked = true
                val preferences = getSharedPreferences("myPreferences", MODE_PRIVATE)
                val editor = preferences.edit()
                editor.putString("myLat", restaurantLat.toString())
                editor.putString("myLng", restaurantLng.toString())
                editor.putString("name", name1)
                editor.putString("address", address1)
                editor.apply()
                auth = Firebase.auth
                auth = FirebaseAuth.getInstance()
                database = FirebaseDatabase.getInstance().getReference("Users")
                database.addValueEventListener(object : ValueEventListener {
                    @RequiresApi(Build.VERSION_CODES.M)
                    @SuppressLint("SetTextI18n")
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            for (i in snapshot.children) {
                                val user = i.getValue(CurrentUser::class.java)
                                if (user != null && user.restaurantId == name1) {
                                    attendeesList.add(user)
                                }
                                myUsers.add(user!!.Name!!)
                                recyclerView.adapter = AttendeesAdapter(attendeesList)
                                setAlarm()
                            }
                            editor.putStringSet("user", myUsers)
                            editor.apply()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
                savedStateRegistry
            }
        }
        }catch (e:java.lang.NullPointerException){}
    }

    /*
     * Alarm for push notification
     */
    @SuppressLint("ShortAlarm")
    @RequiresApi(Build.VERSION_CODES.M)
    private fun setAlarm() {
        calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, 0)
        calendar.set(Calendar.PM, 12)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_CANCEL_CURRENT)
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY, pendingIntent)
        savedStateRegistry
    }
}