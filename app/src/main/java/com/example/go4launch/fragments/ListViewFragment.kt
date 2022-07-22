package com.example.go4launch.fragments
import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.go4launch.BuildConfig
import com.example.go4launch.R
import com.example.go4launch.activities.RestaurantDetails
import com.example.go4launch.adapters.RestaurantAdapter
import com.example.go4launch.api.RestaurantRepository
import com.example.go4launch.viewmodel.ConvertorFactory
import com.example.go4launch.viewmodel.MapsViewModel
import com.example.go4launch.viewmodel.SearchViewModel
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener

@Suppress("NAME_SHADOWING")
class ListViewFragment : Fragment(R.layout.fragment_list_view) {
    private lateinit var restaurantAdapter: RestaurantAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var mapsViewModel: MapsViewModel
    private lateinit var searchViewModel: SearchViewModel
    private lateinit var lastLocation: Location
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var searchView: EditText
    private lateinit var searchButton:Button
    private val AUTOCOMPLETE_REQUEST_CODE = 1
    @SuppressLint("NotifyDataSetChanged", "MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.list_recyclerView)
        restaurantAdapter = RestaurantAdapter()
        recyclerView.adapter = restaurantAdapter
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.setHasFixedSize(true)
        restaurantAdapter.notifyDataSetChanged()

        val preferences = activity?.getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
        val lng = preferences?.getString("currentLng", null)
        val lat = preferences?.getString("currentLat", null)
        val currentLat = lat
        val currentLng = lng
        val keyword = preferences?.getString("keyword", null)
        val apikey = BuildConfig.MAPS_API_KEY
        val type = "restaurant"
        val radius = 1000
        val repository = RestaurantRepository()
        val convertorFactory = ConvertorFactory(repository)
        mapsViewModel = MapsViewModel(repository)
        mapsViewModel = ViewModelProvider(this, convertorFactory).get(mapsViewModel::class.java)
        mapsViewModel.getRestaurantDetails(
            key = apikey,
            loc = "${currentLat},${currentLng}",
            type = type,
            radius = radius.toString(),
        )
        mapsViewModel.myResponse.observe(viewLifecycleOwner) { response ->
            if (response.isSuccessful) {
                response.body().let { myResponse ->
                    restaurantAdapter.differ.submitList(myResponse?.results)
                    restaurantAdapter.setOnItemClickListener(object :
                        RestaurantAdapter.onItemClickListener {
                        override fun onItemClick(position: Int) {
                            val preferences =
                                activity?.getSharedPreferences("myPreferences",
                                    Context.MODE_PRIVATE)
                            val editor = preferences?.edit()
                            editor?.putString("phone_number",
                                myResponse!!.results[position].formatted_phone_number)
                            editor?.putString("website", myResponse!!.results[position].website)
                            editor?.putFloat("rating",
                                myResponse!!.results[position].rating.toFloat())
                            editor?.putString("name", myResponse!!.results[position].name)
                            editor?.putString("address", myResponse!!.results[position].vicinity)
                            editor?.putString("image", myResponse!!.results[position].icon)
                            editor?.putString("lat",
                                myResponse!!.results[position].geometry.location.lat.toString())
                            editor?.putString("lng",
                                myResponse!!.results[position].geometry.location.lng.toString())
                            editor?.apply()
                            val intent = Intent(activity, RestaurantDetails::class.java)
                            startActivity(intent)
                        }
                    })
                }
            }
        }
        val autocompleteFragment =
            childFragmentManager.findFragmentById(R.id.autocomplete_fragment)
                    as AutocompleteSupportFragment


        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME))
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: ${place.name}, ${place.id}")

            }

            override fun onError(status: Status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: $status")
            }
        })
        }

    }




