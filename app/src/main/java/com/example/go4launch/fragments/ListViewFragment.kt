package com.example.go4launch.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
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
import com.example.go4launch.api.SearchRepository
import com.example.go4launch.viewmodel.ConvertorFactory
import com.example.go4launch.viewmodel.MapsViewModel
import com.example.go4launch.viewmodel.SearchConvertorFactory
import com.example.go4launch.viewmodel.SearchViewModel

@Suppress("NAME_SHADOWING", "DEPRECATION")
class ListViewFragment : Fragment(R.layout.fragment_list_view) {
    private lateinit var restaurantAdapter: RestaurantAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var mapsViewModel: MapsViewModel
    private lateinit var searchViewModel: SearchViewModel
    private lateinit var editText: EditText
    private lateinit var restaurantList: ArrayList<com.example.go4launch.model.restaturantDetails.RestaurantDetails>

    @SuppressLint("NotifyDataSetChanged", "MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchRestaurants()
        recyclerView = view.findViewById(R.id.list_recyclerView)
        restaurantAdapter = RestaurantAdapter()
        recyclerView.adapter = restaurantAdapter
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.setHasFixedSize(true)
        restaurantAdapter.notifyDataSetChanged()
        restaurantList = arrayListOf()
        val preferences = activity?.getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
        val lng = preferences?.getString("currentLng", null)
        val lat = preferences?.getString("currentLat", null)
        val apikey = BuildConfig.MAPS_API_KEY
        val type = "restaurant"
        val radius = 1000
        val repository = RestaurantRepository()
        val convertorFactory = ConvertorFactory(repository)
        mapsViewModel = MapsViewModel(repository)
        mapsViewModel = ViewModelProvider(this, convertorFactory)[mapsViewModel::class.java]
        mapsViewModel.getRestaurantDetails(
            key = apikey,
            loc = "$lat,$lng",
            type = type,
            radius = radius.toString(),
        )
        // observing response from google map's api to display nearby restaurants in a list
        mapsViewModel.restaurantDetailsResponse.observe(viewLifecycleOwner) { response ->
            if (response.isSuccessful) {
                response.body().let { myResponse ->
                    restaurantAdapter.differ.submitList(myResponse?.results)
                    restaurantAdapter.setOnItemClickListener(object :
                        RestaurantAdapter.OnItemClickListener {
                        override fun onItemClick(position: Int) {
                            val intent = Intent(activity, RestaurantDetails::class.java)

                            intent.putExtra("phone_number",
                                myResponse!!.results[position].formatted_phone_number)
                            intent.putExtra("website", myResponse.results[position].website)
                            intent.putExtra("rating",
                                myResponse.results[position].rating.toFloat())
                            intent.putExtra("name", myResponse.results[position].name)
                            intent.putExtra("address", myResponse.results[position].vicinity)
                            intent.putExtra("image", myResponse.results[position].icon)
                            intent.putExtra("myLat",
                                myResponse.results[position].geometry.location.lat.toString())
                            intent.putExtra("myLng",
                                myResponse.results[position].geometry.location.lng.toString())
                            startActivity(intent)
                        }
                    })

                }
            }
        }
    }

    /*
     * Function to search nearby restaurants in the list view
     */
    private fun searchRestaurants() {
        val preferences =
            activity?.getSharedPreferences("myPreferences",
                Context.MODE_PRIVATE)
        editText = EditText(requireContext())
        editText = requireView().findViewById(R.id.edit_query)
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int,
            ) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val currentLat = preferences!!.getString("currentLat", null)
                val currentLng = preferences.getString("currentLng", null)
                val query = editText.text.toString()
                val key = BuildConfig.MAPS_API_KEY
                val repository = SearchRepository()
                val searchConvertorFactory = SearchConvertorFactory(repository)
                searchViewModel = SearchViewModel(repository)
                searchViewModel = ViewModelProvider(this@ListViewFragment,
                    searchConvertorFactory)[searchViewModel::class.java]
                searchViewModel.searchRestaurants(query,
                    "$currentLat,$currentLng", key)
                searchViewModel.searchResponse.observe(viewLifecycleOwner) { response ->
                    if (response.isSuccessful) {
                        response.body().let { searchResponse ->
                            restaurantAdapter.differ.submitList(searchResponse?.results)
                            restaurantAdapter.setOnItemClickListener(object :
                                RestaurantAdapter.OnItemClickListener {
                                override fun onItemClick(position: Int) {
                                    val intent = Intent(activity, RestaurantDetails::class.java)
                                    startActivity(intent)
                                }
                            })
                        }
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

    }
}