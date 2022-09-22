package com.example.go4launch.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.go4launch.R
import com.example.go4launch.model.userdetails.CurrentUser
import com.google.firebase.database.*

/*
* RecyclerView Adapter to display list of attending co-workers
*/
class AttendeesAdapter(private var attendeesList: ArrayList<CurrentUser>) :
    RecyclerView.Adapter<AttendeesAdapter.AttendeesViewHolder>() {
    private lateinit var database: DatabaseReference

    class AttendeesViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.photo)
        val username: TextView = view.findViewById(R.id.name)
    }

    @SuppressLint("ResourceType")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendeesViewHolder {
        return AttendeesViewHolder(LayoutInflater.from(parent.context).inflate
            (R.layout.attendees_layout, parent, false))
    }

    override fun onBindViewHolder(
        holder: AttendeesViewHolder,
        @SuppressLint("RecyclerView") position: Int,
    ) {
        database = FirebaseDatabase.getInstance().getReference("Users")
        database.addValueEventListener(object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (i in snapshot.children) {
                        val attendingUsers = attendeesList[position]
                        holder.imageView.load(attendingUsers.Photo)
                        holder.username.text = attendingUsers.Name + " " + "is joining!!"
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    override fun getItemCount(): Int {
        return attendeesList.size
    }
}