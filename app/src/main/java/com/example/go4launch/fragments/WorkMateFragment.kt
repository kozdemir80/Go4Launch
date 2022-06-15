package com.example.go4launch.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.go4launch.BuildConfig
import com.example.go4launch.R
import com.example.go4launch.adapters.UserAdapter
import com.example.go4launch.api.RestaurantRepository
import com.example.go4launch.model.userdetails.CurrentUser
import com.example.go4launch.viewmodel.ConvertorFactory
import com.example.go4launch.viewmodel.MapsViewModel
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.protobuf.DescriptorProtos

class WorkMateFragment:Fragment(R.layout.fragment_work_mate) {


    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: UserAdapter
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var firebaseData: FirebaseDatabase
    private lateinit var workmatesList: ArrayList<CurrentUser>
    private lateinit var mapsViewModel: MapsViewModel
    private lateinit var currentUser: CurrentUser
    private lateinit var mapView: MapView


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth
        auth = FirebaseAuth.getInstance()
        firebaseData = FirebaseDatabase.getInstance()
        database = firebaseData.getReference("Users")
        saveWorkmates()
        getData()

    }

    private fun saveWorkmates() {
        val apikey = BuildConfig.MAPS_API_KEY
        val locLat = DescriptorProtos.SourceCodeInfo.Location.getDefaultInstance()
        val locLng = DescriptorProtos.SourceCodeInfo.Location.getDefaultInstance()
        val type = "restaurant"
        val radius = null
        val repository = RestaurantRepository()
        val convertorFactory = ConvertorFactory(repository)
        mapsViewModel = MapsViewModel(repository)
        mapsViewModel = ViewModelProvider(this, convertorFactory).get(mapsViewModel::class.java)
        mapsViewModel.getRestaurantDetails(key = apikey,
            loc = "${locLat},${locLng}",
            type = type,
            radius = radius.toString())
        mapsViewModel.myResponse.observe(viewLifecycleOwner) { response ->
            if (response.isSuccessful) {
                response.body().let { myResponse ->

                    val lat = myResponse!!.results[0].geometry.location.lat
                    val lng = myResponse.results[0].geometry.location.lng
                    val locations = LatLng(lat, lng)
                    auth = Firebase.auth
                    auth = FirebaseAuth.getInstance()
                    firebaseData = FirebaseDatabase.getInstance()
                    database = firebaseData.getReference("Users")
                    val preferences =
                        activity?.getSharedPreferences("workMates", Context.MODE_PRIVATE)
                    val editor = preferences?.edit()
                    val userNames = editor?.putString("names", auth.currentUser?.displayName)
                    val currentRestaurant = editor?.putString("location", locations.toString())
                    editor?.apply()
                    val currentUser= CurrentUser(userNames.toString(),null,"",currentRestaurant.toString())
                    auth = Firebase.auth
                    auth = FirebaseAuth.getInstance()
                    firebaseData = FirebaseDatabase.getInstance()
                    database = firebaseData.getReference("Users")
                    val id =database.push().key
                    database.child(id!!).setValue(currentUser)

                }
            }


        }}
    private fun getData(){

        database.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var arrayList=ArrayList<CurrentUser>()
                for (data in snapshot.children){
                    var model=data.getValue(CurrentUser::class.java)
                    arrayList.add(model as CurrentUser)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("cancel",error.toString())
            }

        })
    }
}
