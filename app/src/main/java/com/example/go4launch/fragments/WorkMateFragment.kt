package com.example.go4launch.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.go4launch.R
import com.example.go4launch.adapters.UserAdapter
import com.example.go4launch.model.userdetails.CurrentUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class WorkMateFragment:Fragment(R.layout.fragment_work_mate) {


    private lateinit var recyclerView: RecyclerView

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var imageView:ImageView
    private lateinit var workmatesList: ArrayList<CurrentUser>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setWorkMates()
        recyclerView=view.findViewById(R.id.work_mate_list)
        recyclerView.layoutManager= LinearLayoutManager(activity)
        recyclerView.setHasFixedSize(true)
        workmatesList= arrayListOf()
        imageView= ImageView(requireContext())
        imageView.findViewById<ImageView>(R.id.imageView)

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {


                if (snapshot.exists()) {

                    for (i in snapshot.children) {
                        val id = i.child("id").value.toString()
                        val name = i.child("name").value.toString()
                        val photoUrl=i.child("photo").value.toString()
                        val preferences=activity?.getSharedPreferences("myPreferences",Context.MODE_PRIVATE)
                        val restaurantName=preferences?.getString("Name",null)
                        val user = CurrentUser(name, id,photoUrl,restaurantName)
                        workmatesList.add(user)
                        recyclerView.adapter = UserAdapter(workmatesList)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun setWorkMates() {


        auth = Firebase.auth
        auth = FirebaseAuth.getInstance()
        val name = auth.currentUser?.displayName.toString()
        val id = auth.currentUser?.uid.toString()
        val photoUrl = auth.currentUser?.photoUrl.toString()


        database = FirebaseDatabase.getInstance().getReference("Users")
        database.child(id).setValue(CurrentUser(name, id, photoUrl))
    }
}