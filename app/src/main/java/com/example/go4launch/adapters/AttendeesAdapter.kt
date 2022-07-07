package com.example.go4launch.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.go4launch.R
import com.example.go4launch.model.userdetails.CurrentUser

class AttendeesAdapter(private var attendeesList:ArrayList<CurrentUser>):RecyclerView.Adapter<AttendeesAdapter.AttendeesViewHolder> (){
    class AttendeesViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    }

    @SuppressLint("ResourceType")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendeesViewHolder {
               return AttendeesViewHolder(LayoutInflater.from(parent.context).inflate
                   (R.id.restaurant_attendees_recycler_view,parent,false))
    }

    override fun onBindViewHolder(holder: AttendeesViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return attendeesList.size

    }

}