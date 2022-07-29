package com.example.go4launch.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.go4launch.R
import com.example.go4launch.model.restaturantDetails.Result
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.ktx.utils.sphericalDistance

class RestaurantAdapter:RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder>(){

    private lateinit var mListener:onItemClickListener
    interface onItemClickListener{

        fun onItemClick(position: Int)
    }
    fun setOnItemClickListener(listener:onItemClickListener) {
        mListener = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        return RestaurantViewHolder(
            LayoutInflater.from(parent.context).inflate
                (R.layout.item_restaurant,parent,false),mListener
        )
    }
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        val restaurants=differ.currentList[position]
        val preferences=holder.itemView.context.getSharedPreferences("myPreferences",Context.MODE_PRIVATE)
        val lat=preferences.getString("currentLat",null)
        val lng=preferences.getString("currentLng",null)
        val latLng=LatLng(lat!!.toDouble(),lng!!.toDouble())
        val restaurantLat=restaurants.geometry.location.lat
        val restaurantLng=restaurants.geometry.location.lng
        val restaurantLatLng=LatLng(restaurantLat,restaurantLng)
        val distance=latLng.sphericalDistance(restaurantLatLng).toInt()


       try {
         if (!restaurants.opening_hours.open_now){
         holder.rTimeTable.text="Open"
         }else{
             holder.rTimeTable.text="Closed"}
        holder.view.apply {
        holder.rName.text=restaurants.name
        holder.rAddress.text=restaurants.vicinity
        holder.rRating.rating=restaurants.rating.toFloat()
        holder.rDistance.text=distance.toString()
        holder.rIcon.load(restaurants.icon)
        holder.rNumber.text=restaurants.types[0] }
        }catch (e:NullPointerException){}
    }

    override fun getItemCount(): Int {
      return  differ.currentList.size
    }

    class RestaurantViewHolder( val view: View,listener: onItemClickListener): RecyclerView.ViewHolder(view) {
        val rIcon: AppCompatImageView = view.findViewById(R.id.iv_restaurant_icon)
        val rName: AppCompatTextView = view.findViewById(R.id.tv_restaurant_name)
        val rAddress: AppCompatTextView = view.findViewById(R.id.tv_restaurant_address)
        val rTimeTable: AppCompatTextView = view.findViewById(R.id.tv_restaurant_timetable)
        val rDistance: AppCompatTextView = view.findViewById(R.id.tv_restaurant_distance)
        val rPerson: AppCompatImageView = view.findViewById(R.id.iv_restaurant_person)
        val rNumber: AppCompatTextView = view.findViewById(R.id.tv_restaurant_number)
        val rRating: AppCompatRatingBar = view.findViewById(R.id.rb_restaurant_rating)
        init {
            view.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    private val differCallBack =object : DiffUtil.ItemCallback<Result>(){
        override fun areItemsTheSame(oldItem:Result, newItem:Result): Boolean {
            return oldItem == newItem
        }
        override fun areContentsTheSame(oldItem:Result, newItem:Result): Boolean {
            return oldItem==newItem
        }
    }

    val differ= AsyncListDiffer(this,differCallBack)

}


