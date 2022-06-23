package com.example.go4launch.fragments

import android.os.Bundle
import android.view.View
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
    private lateinit var adapter: UserAdapter
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var firebaseData: FirebaseDatabase
    private lateinit var workmatesList: ArrayList<CurrentUser>
    private lateinit var currentUser: CurrentUser



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setWorkMates()
        recyclerView=view.findViewById(R.id.work_mate_list)
        recyclerView.layoutManager= LinearLayoutManager(activity)
        recyclerView.setHasFixedSize(true)
        workmatesList= arrayListOf<CurrentUser>()
        getUserData()


    }
    private fun setWorkMates(){
        auth = Firebase.auth
        auth = FirebaseAuth.getInstance()
        val name=auth.currentUser?.displayName.toString()
        val uid=auth.currentUser?.uid.toString()
        database=FirebaseDatabase.getInstance().getReference("Users")
        val user=CurrentUser(name,uid)
        database.child("Users").child(uid).setValue(user)



    }
    private fun getUserData(){
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (UserSnapshot in snapshot.children){
                        val user=UserSnapshot.child("Users").value.toString()
                        val user1=CurrentUser(user)
                        workmatesList.add(user1)
                    }

                  recyclerView.adapter= UserAdapter(workmatesList)


                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

}
