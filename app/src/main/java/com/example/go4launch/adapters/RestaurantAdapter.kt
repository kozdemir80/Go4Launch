package com.example.go4launch.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.example.go4launch.R

class RestaurantAdapter:RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        return RestaurantViewHolder(
            LayoutInflater.from(parent.context).inflate
                (R.layout.item_restaurant,parent,false)
        )
    }


    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    class RestaurantViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val rIcon:AppCompatImageView=view.findViewById(R.id.iv_restaurant_icon)
        val rName:AppCompatTextView=view.findViewById(R.id.tv_restaurant_name)
        val rAddress:AppCompatTextView=view.findViewById(R.id.tv_restaurant_address)
        val rTimeTable:AppCompatTextView=view.findViewById(R.id.tv_restaurant_timetable)
        val rDistance:AppCompatTextView=view.findViewById(R.id.tv_restaurant_distance)
        val rPerson:AppCompatImageView=view.findViewById(R.id.iv_restaurant_person)
        val rNumber:AppCompatTextView=view.findViewById(R.id.tv_restaurant_number)
        val rRating:AppCompatRatingBar=view.findViewById(R.id.rb_restaurant_rating)
    }
}