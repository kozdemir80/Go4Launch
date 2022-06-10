package com.example.go4launch.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.go4launch.R
import com.example.go4launch.databinding.NavHeaderBinding

class UserDetails:AppCompatActivity(){
    private lateinit var binding:NavHeaderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.nav_header)
        binding = NavHeaderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val preference=getSharedPreferences("name", MODE_PRIVATE)
        val username= preference.getString("myName",null)

        binding.name.text=username
        Log.d("name",username.toString())
        binding.email.text=UserPreferences.getEmail(applicationContext)


}
}