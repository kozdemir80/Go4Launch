package com.example.go4launch.fragments
import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.go4launch.BuildConfig.MAPS_API_KEY
import com.example.go4launch.R
import com.example.go4launch.viewmodel.MapsViewModel
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener

@Suppress("DEPRECATION")
class MapViewFragment: Fragment(R.layout.fragment_map_view), OnMapReadyCallback,GoogleMap.OnMarkerClickListener {

    private var mapView: GoogleMap? = null
    private lateinit var lastLocation: Location
    private lateinit var placesClient: PlacesClient
    // The entry point to the Fused Location Provider.
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    companion object {
        private const val REQUEST_CODE=1
    }
    private lateinit var mapsViewModel: MapsViewModel




    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)





        Places.initialize(requireActivity(),MAPS_API_KEY )
        placesClient = Places.createClient(requireContext())
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        val autocompleteFragment =
            childFragmentManager.findFragmentById(R.id.autocomplete_fragment)
                    as AutocompleteSupportFragment
        autocompleteFragment.setPlaceFields(listOf( Place.Field.NAME))

        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: ${place.name}")
            }

            override fun onError(p0: Status) {
                Log.i(TAG, "An error occurred")
            }
        })



    }




    @SuppressLint("PotentialBehaviorOverride")
    override fun onMapReady(googleMap: GoogleMap) {
        mapView = googleMap
        mapView?.uiSettings?.isZoomControlsEnabled=true
        mapView?.setOnMarkerClickListener(this)
         setUpMap()

        }



    @SuppressLint("MissingPermission")
    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE)
            return
        }

        mapView?.isMyLocationEnabled=true
        fusedLocationProviderClient.lastLocation.addOnSuccessListener(requireActivity()) {location->
            if (location != null){
                lastLocation=location
                val currentLatLng=LatLng(location.latitude,location.longitude)
                placeMarker(currentLatLng)
                mapView?.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng,12f))
            }
        }
    }

    private fun placeMarker(currentLatLng: LatLng) {
        val markerOptions= MarkerOptions().position(currentLatLng)
        markerOptions.title("$currentLatLng")
        mapView?.addMarker(markerOptions)
    }

    override fun onMarkerClick(p0: Marker)=false

}

