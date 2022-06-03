package com.example.go4launch.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.go4launch.R
import com.example.go4launch.model.restaturantDetails.RestaurantDetails
import com.squareup.picasso.Picasso

class RestaurantAdapter:RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        return RestaurantViewHolder(
            LayoutInflater.from(parent.context).inflate
                (R.layout.item_restaurant,parent,false)
        )
    }


    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        val restaurants=differ.currentList[position]
        holder.view.apply {
         holder.rName.text=restaurants.name
         holder.rNumber.text=restaurants.phone
         Picasso.get().load(restaurants.photo.toString()).into(holder.rIcon)
         holder.rRating.rating=restaurants.review
         holder.rAddress.text=restaurants.address
         holder.rTimeTable.text=restaurants.openingHours.toString()




        }
    }

    override fun getItemCount(): Int {
      return  differ.currentList.size
    }

    class RestaurantViewHolder( val view: View): RecyclerView.ViewHolder(view) {
        val rIcon: AppCompatImageView = view.findViewById(R.id.iv_restaurant_icon)
        val rName: AppCompatTextView = view.findViewById(R.id.tv_restaurant_name)
        val rAddress: AppCompatTextView = view.findViewById(R.id.tv_restaurant_address)
        val rTimeTable: AppCompatTextView = view.findViewById(R.id.tv_restaurant_timetable)
        val rDistance: AppCompatTextView = view.findViewById(R.id.tv_restaurant_distance)
        val rPerson: AppCompatImageView = view.findViewById(R.id.iv_restaurant_person)
        val rNumber: AppCompatTextView = view.findViewById(R.id.tv_restaurant_number)
        val rRating: AppCompatRatingBar = view.findViewById(R.id.rb_restaurant_rating)
    }

    private val differCallBack =object : DiffUtil.ItemCallback<RestaurantDetails>(){
        override fun areItemsTheSame(oldItem:RestaurantDetails, newItem:RestaurantDetails): Boolean {
            return oldItem == newItem
        }
        override fun areContentsTheSame(oldItem:RestaurantDetails, newItem:RestaurantDetails): Boolean {
            return oldItem==newItem
        }
    }

    val differ= AsyncListDiffer(this,differCallBack)
}



