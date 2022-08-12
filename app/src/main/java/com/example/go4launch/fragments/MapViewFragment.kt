package com.example.go4launch.fragments
import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.go4launch.BuildConfig.MAPS_API_KEY
import com.example.go4launch.R
import com.example.go4launch.activities.RestaurantDetails
import com.example.go4launch.api.RestaurantRepository
import com.example.go4launch.viewmodel.ConvertorFactory
import com.example.go4launch.viewmodel.MapsViewModel
import com.example.go4launch.viewmodel.SearchViewModel
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.google.android.libraries.places.api.net.PlacesClient

@Suppress("DEPRECATION")
class MapViewFragment: Fragment(R.layout.fragment_map_view), OnMapReadyCallback {


    private var mapView: GoogleMap? = null
    private lateinit var lastLocation: Location
    private lateinit var placesClient: PlacesClient
    private var cameraPosition: CameraPosition? = null
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient


    companion object {
        private const val KEY_CAMERA_POSITION = "camera_position"
        private const val KEY_LOCATION = "location"
        const val TAG = "myLocation"
        const val DEFAULT_ZOOM = 15
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
    }

    private lateinit var mapsViewModel: MapsViewModel
    private lateinit var searchViewModel: SearchViewModel
    private val defaultLocation = LatLng(37.076526, 36.242001)
    private var locationPermissionGranted = false
    private val AUTOCOMPLETE_REQUEST_CODE = 1


    @SuppressLint("MissingPermission", "PotentialBehaviorOverride")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState != null) {
            lastLocation = savedInstanceState.getParcelable(KEY_LOCATION)!!
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION)
        }


        // Construct a PlacesClient
        Places.initialize(requireContext(), MAPS_API_KEY)
        placesClient = Places.createClient(requireContext())

        // Construct a FusedLocationProviderClient.
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())

        // Build the map.
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    /**
     * Saves the state of the map when the activity is paused.
     */
    override fun onSaveInstanceState(outState: Bundle) {
        mapView?.let { map ->
            outState.putParcelable(KEY_CAMERA_POSITION, map.cameraPosition)
            outState.putParcelable(KEY_LOCATION, lastLocation)
        }
        super.onSaveInstanceState(outState)
    }


    @SuppressLint("PotentialBehaviorOverride")
    override fun onMapReady(map: GoogleMap) {
        this.mapView = map

        // Use a custom info window adapter to handle multiple lines of text in the
        // info window contents.
        this.mapView?.setInfoWindowAdapter(@SuppressLint("PotentialBehaviorOverride")
        object : GoogleMap.InfoWindowAdapter {
            // Return null here, so that getInfoContents() is called next.
            override fun getInfoWindow(arg0: Marker): View? {
                return null
            }

            override fun getInfoContents(marker: Marker): View {
                // Inflate the layouts for the info window, title and snippet.
                val infoWindow = layoutInflater.inflate(R.layout.custom_info_contents,
                    view?.findViewById<FrameLayout>(R.id.map), false)
                val title = infoWindow.findViewById<TextView>(R.id.title)
                title.text = marker.title
                val snippet = infoWindow.findViewById<TextView>(R.id.snippet)
                snippet.text = marker.snippet
                return infoWindow
            }
        })
        mapView?.setOnMarkerClickListener(GoogleMap.OnMarkerClickListener {
            return@OnMarkerClickListener false
        })

        // Prompt the user for permission.
        getLocationPermission()
        getDeviceLocation()
        nearByRestaurants()
        searchRestaurants()

    }

    @SuppressLint("MissingPermission")
    private fun getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (locationPermissionGranted) {
                val locationResult = fusedLocationProviderClient.lastLocation
                locationResult.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Set the map's camera position to the current location of the device.
                        lastLocation = Location(LocationManager.NETWORK_PROVIDER)
                        lastLocation = task.result
                        mapView?.moveCamera(CameraUpdateFactory.newLatLngZoom(
                            LatLng(lastLocation.latitude,
                                lastLocation.longitude), DEFAULT_ZOOM.toFloat()))
                    } else {
                        Log.d(TAG, "Current location is null. Using defaults.")
                        Log.e(TAG, "Exception: %s", task.exception)
                        mapView?.moveCamera(CameraUpdateFactory
                            .newLatLngZoom(defaultLocation, DEFAULT_ZOOM.toFloat()))
                        mapView?.uiSettings?.isMyLocationButtonEnabled = false
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    /**
     * Prompts the user for permission to use the device location.
     */
    private fun getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        locationPermissionGranted = false
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    locationPermissionGranted = true
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }

    }


    @SuppressLint("PotentialBehaviorOverride", "MissingPermission")
    private fun nearByRestaurants(){
        val preferences =
            activity?.getSharedPreferences("myPreferences",
                Context.MODE_PRIVATE)
        val editor = preferences?.edit()
        lastLocation = Location(LocationManager.NETWORK_PROVIDER)
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        fusedLocationProviderClient.lastLocation.addOnCompleteListener {

            val currentLat = lastLocation.latitude
            val currentLng = lastLocation.longitude
            editor?.putString("currentLat", lastLocation.latitude.toString())
            editor?.putString("currentLng", lastLocation.longitude.toString())

            val apikey = MAPS_API_KEY
            val type = "restaurant"
            val radius = 1000
            val repository = RestaurantRepository()
            val convertorFactory = ConvertorFactory(repository)
            mapsViewModel = MapsViewModel(repository)
            mapsViewModel =
                ViewModelProvider(this, convertorFactory)[mapsViewModel::class.java]
            mapsViewModel.getRestaurantDetails(key = apikey,
                loc = "$currentLat,$currentLng",
                type = type,
                radius = radius.toString())
            mapsViewModel.myResponse.observe(viewLifecycleOwner) { response ->
                if (response.isSuccessful) {
                    response.body().let { myResponse ->
                        for (i in 0 until myResponse!!.results.size) {

                            val Lat = myResponse.results[i].geometry.location.lat
                            val Lng = myResponse.results[i].geometry.location.lng
                            val locations = LatLng(Lat, Lng)
                            mapView?.addMarker(MarkerOptions().position(locations).title(
                                myResponse.results[i].name))
                            mapView?.setOnMarkerClickListener(GoogleMap.OnMarkerClickListener { marker ->
                                editor?.putString("phone_number1",
                                    myResponse.results[marker.zIndex.toInt()].formatted_phone_number)
                                editor?.putFloat("rating",
                                    myResponse.results[marker.zIndex.toInt()].rating.toFloat())
                                editor?.putString("name",
                                    myResponse.results[marker.zIndex.toInt()].name)
                                editor?.putString("address",
                                    myResponse.results[marker.zIndex.toInt()].vicinity)
                                editor?.putString("image",
                                    myResponse.results[marker.zIndex.toInt()].icon)
                                editor?.putString("lat",
                                    myResponse.results[marker.zIndex.toInt()].geometry.location.lat.toString())
                                editor?.putString("lng",
                                    myResponse.results[marker.zIndex.toInt()].geometry.location.lng.toString())
                                editor?.apply()
                                val intent =
                                    Intent(requireContext(), RestaurantDetails::class.java)
                                startActivity(intent)
                                return@OnMarkerClickListener false


                            })
                            mapView?.clear()

                        }
                    }
                }
            }
        }
    }

    private fun searchRestaurants(){

        val intent=Intent()
        val preferences =
            activity?.getSharedPreferences("myPreferences",
                Context.MODE_PRIVATE)
        val currentLat=preferences!!.getString("currentLat",null)
        val currentLng=preferences.getString("currentLng",null)
        val latLng=LatLng(currentLat!!.toDouble(),currentLng!!.toDouble())
        val query=intent.getStringExtra("query")
                    val token = AutocompleteSessionToken.newInstance()
                    val bounds = RectangularBounds.newInstance(
                        LatLng(currentLat.toDouble(), currentLng.toDouble()),
                        LatLng(currentLat.toDouble(), currentLat.toDouble()))
                    val request =
                        FindAutocompletePredictionsRequest.builder()
                            .setLocationBias(bounds)
                            .setOrigin(LatLng(currentLat.toDouble(), currentLng.toDouble()))
                            .setCountry("US")
                            .setTypeFilter(TypeFilter.ADDRESS)
                            .setSessionToken(token)
                            .setQuery(query)
                            .build()

                    placesClient.findAutocompletePredictions(request)
                        .addOnSuccessListener { response: FindAutocompletePredictionsResponse ->
                            for (prediction in response.autocompletePredictions) {

                                mapView?.addMarker(MarkerOptions().position(latLng))
                                Log.d("mySearch",prediction.placeId)
                                mapView?.moveCamera(CameraUpdateFactory.newLatLng())

                            }
                        }.addOnFailureListener { exception: Exception? ->
                            if (exception is ApiException) {
                                Log.e(TAG, "Place not found: " + exception.statusCode)
                            }
                        }
                }
            }


