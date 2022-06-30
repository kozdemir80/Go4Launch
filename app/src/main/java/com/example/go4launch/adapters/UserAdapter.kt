package com.example.go4launch.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.go4launch.R
import com.example.go4launch.model.userdetails.CurrentUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class UserAdapter(private var workmatesList: ArrayList<CurrentUser>):RecyclerView.Adapter<UserAdapter.UserViewHolder>(){
         private lateinit var auth:FirebaseAuth
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            LayoutInflater.from(parent.context).inflate
                (R.layout.item_workmates, parent, false)
        )
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        auth = Firebase.auth
        auth = FirebaseAuth.getInstance()
          val userList=workmatesList[position]
          holder.userName.text=userList.Name
          holder.userPhoto.load(auth.currentUser!!.photoUrl)
          holder.restaurantChoice.text=userList.restaurantId
          

    }

    override fun getItemCount(): Int {
        return  workmatesList.size
    }
    class UserViewHolder(val view:View):RecyclerView.ViewHolder(view) {
         val userPhoto:ImageView=view.findViewById(R.id.user_photo)
         val userName:TextView=view.findViewById(R.id.user_name)
         val restaurantChoice:TextView=view.findViewById(R.id.choice_restaurant)
         val bar:View=view.findViewById(R.id.bar)




    }

}