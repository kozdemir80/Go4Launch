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
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageException
import com.google.firebase.storage.StorageReference

class WorkMateFragment:Fragment(R.layout.fragment_work_mate) {


    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: UserAdapter
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var firebaseData: FirebaseDatabase
    private lateinit var workmatesList: ArrayList<CurrentUser>
    private lateinit var currentUser: CurrentUser
    private lateinit var storage: StorageReference



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setWorkMates()
        recyclerView=view.findViewById(R.id.work_mate_list)
        recyclerView.layoutManager= LinearLayoutManager(activity)
        recyclerView.setHasFixedSize(true)
        workmatesList= arrayListOf()

        val getData=database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {


                if (snapshot.exists()) {

                    for (i in snapshot.children) {



                            val id = i.child("id").value.toString()
                            val name = i.child("name").value.toString()
                            val user = CurrentUser(name, id)

                            workmatesList.add(user)


                            recyclerView.adapter = UserAdapter(workmatesList)

                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        database.addValueEventListener(getData)


    }
    private fun setWorkMates(){

        auth = Firebase.auth
        auth = FirebaseAuth.getInstance()
        val name=auth.currentUser?.displayName.toString()
        val id=auth.currentUser?.uid.toString()
         database=FirebaseDatabase.getInstance().getReference("Users")
        database.child(id).setValue(CurrentUser(name,id))
          try {

           storage = FirebaseStorage.getInstance().getReference("Images")
        val photo=auth.currentUser?.photoUrl
        storage.putFile(photo!!).addOnSuccessListener {

        }}catch (e:StorageException){}



    }




    }


