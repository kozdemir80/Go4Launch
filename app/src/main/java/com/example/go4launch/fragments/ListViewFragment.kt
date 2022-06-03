package com.example.go4launch.fragments

import android.annotation.SuppressLint
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.go4launch.R
import com.example.go4launch.adapters.RestaurantAdapter
import com.example.go4launch.model.restaturantDetails.RestaurantDetails
import com.example.go4launch.viewmodel.MapsViewModel
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener

class ListViewFragment : Fragment(R.layout.fragment_list_view) {
    private lateinit var viewModel: MapsViewModel
    private lateinit var restaurantAdapter: RestaurantAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var restaurantDetails: RestaurantDetails

    private lateinit var packageManager: PackageManager
    private lateinit var textView:TextView
    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
         textView=view.findViewById(R.id.list_recyclerView)

        val ai: ApplicationInfo = requireContext().packageManager
            .getApplicationInfo(requireContext().packageName, PackageManager.GET_META_DATA)
        val value = ai.metaData["api_key"]
        val apiKey = value.toString()

        // Initializing the Places API
        // with the help of our API_KEY
        if (!Places.isInitialized()) {
            Places.initialize(requireContext(), apiKey)
        }

        // Initialize Autocomplete Fragments
        // from the main activity layout file
        val autocompleteSupportFragment1 = childFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment?

        // Information that we wish to fetch after typing
        // the location and clicking on one of the options
        autocompleteSupportFragment1!!.setPlaceFields(
            listOf(

                Place.Field.NAME,
                Place.Field.ADDRESS,
                Place.Field.PHONE_NUMBER,
                Place.Field.PHOTO_METADATAS,
                Place.Field.OPENING_HOURS,
                Place.Field.RATING,
                Place.Field.TYPES



            )
        )

        // Display the fetched information after clicking on one of the options
        autocompleteSupportFragment1.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            @SuppressLint("SetTextI18n")
            override fun onPlaceSelected(place: Place) {
                // Text view where we will
                // append the information that we fetch


                // Information about the place
                val name = place.name
                val address = place.address
                val phone = place.phoneNumber.toString()
                val photo = place.photoMetadatas
                val type = place.types
                val review =place.rating?.toFloat()
                val openingHours=place.openingHours




                restaurantDetails= RestaurantDetails(name=name!!,address=address!!,phone=phone!!, type = type!!, review =review!!,
                    photo = photo!!, openingHours = openingHours!! )
                textView.text = "Name: $name \nAddress: $address \nPhone Number: $phone \n"
                        "\nIs open : $openingHours \n" +
                        "Rating: $review "

            }

            override fun onError(status: Status) {
                Toast.makeText(requireContext(),"Some error occurred", Toast.LENGTH_SHORT).show()
            }
        })

    }
        }



