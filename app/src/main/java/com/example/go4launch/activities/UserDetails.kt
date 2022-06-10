package com.example.go4launch.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.example.go4launch.R
import com.example.go4launch.databinding.NavHeaderBinding

class UserDetails:AppCompatActivity(){
    private lateinit var binding:NavHeaderBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.nav_header)
        binding = NavHeaderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val preferences=getSharedPreferences("userDetails", MODE_PRIVATE)
        val name=preferences.getString("name",null)
        Log.d("myName",name.toString())
        val email=preferences.getString("email",null)
        val userPhoto=preferences.getString("userPhoto",null)
        binding.name.text=name
        binding.email.text=email
        binding.circleImageView.load(userPhoto)

}
}