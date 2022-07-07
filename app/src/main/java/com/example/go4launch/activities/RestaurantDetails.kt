package com.example.go4launch.activities


import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.go4launch.R
import com.example.go4launch.adapters.UserAdapter
import com.example.go4launch.databinding.RestaurantDetailsActivityBinding
import com.example.go4launch.model.userdetails.CurrentUser
import com.like.LikeButton
import com.like.OnLikeListener


class RestaurantDetails:AppCompatActivity() {
      private lateinit var binding: RestaurantDetailsActivityBinding
      private lateinit var recyclerView: RecyclerView
      private lateinit var adapter:UserAdapter
    private lateinit var workmatesList: ArrayList<CurrentUser>

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.restaurant_details_activity)
        binding = RestaurantDetailsActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        recyclerView=findViewById(R.id.restaurant_attendees_recycler_view)
        recyclerView.layoutManager= LinearLayoutManager(applicationContext)
        recyclerView.setHasFixedSize(true)
        workmatesList= arrayListOf()

        val preferences=getSharedPreferences("myPreferences", MODE_PRIVATE)
        val editor=preferences.edit()
        val address=preferences.getString("address",null)
        val name=preferences.getString("name",null)
        val website=preferences.getString("website",null)
        val phoneNumber=preferences.getString("phone_number",null)
        val rating=preferences.getFloat("rating",0.0f)
        val myImage=preferences.getString("image",null)

      
        binding.fabBook.setOnClickListener {view->
        if (binding.fabBook.isChecked){
            binding.fabBook.isChecked=true
            editor.putString("Name",name)
            editor.apply()

        }

        }
        binding.restaurantImageView.load(myImage)
        binding.detailsRestaurantName.text=name

        if (binding.detailsRestaurantName.text.isNotEmpty()){
            binding.like.isVisible
        }
        binding.detailsRetaurantAddress.text=address
        binding.btnCall.setOnClickListener {
            val intent=Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse(phoneNumber)

            startActivity(intent)
        }
        binding.btnWebsite.setOnClickListener{
            try {

            val intent=Intent(Intent.ACTION_VIEW)
            intent.data= Uri.parse(website)

            startActivity(intent)}catch (e:NullPointerException){}
        }

        binding.btnLike.setOnLikeListener(object:OnLikeListener{
            override fun liked(likeButton: LikeButton?) {
                if(binding.btnLike.isLiked){
                  binding.like.isVisible
                }else{
                    binding.like.isInvisible
                }
            }

            override fun unLiked(likeButton: LikeButton?) {
                binding.like.isInvisible
            }

        })

    }


    }
