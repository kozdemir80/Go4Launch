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
 * RecyclerView Adapter to display list of co-workers and their list of where they eat currently
 */
class UserAdapter(private var workmatesList: ArrayList<CurrentUser>):RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    private lateinit var database: DatabaseReference
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            LayoutInflater.from(parent.context).inflate
                (R.layout.item_workmates, parent, false)
        )
    }
    override fun onBindViewHolder(
        holder: UserViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        database = FirebaseDatabase.getInstance().getReference("Users")
        database.addValueEventListener(object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (i in snapshot.children) {
                        val userList = workmatesList[position]
                        holder.userName.text=userList.Name
                        if (userList.restaurantId.isNullOrEmpty() && userList.restaurantId.isNullOrBlank()){
                           holder.restaurantChoice.text = " hasn't decided yet"
                        }else {holder.restaurantChoice.text = " is easting at" + " (${userList.restaurantId})"}
                        holder.userPhoto.load(userList.Photo)
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
    override fun getItemCount(): Int {
        return workmatesList.size
    }
    class UserViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val userPhoto: ImageView = view.findViewById(R.id.user_photo)
        val userName: TextView = view.findViewById(R.id.user_name)
        val restaurantChoice: TextView = view.findViewById(R.id.choice_restaurant)
    }
}