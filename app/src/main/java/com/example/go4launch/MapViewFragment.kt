@file:Suppress("DEPRECATION")

package com.example.go4launch






import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.extension.style.layers.generated.circleLayer
import com.mapbox.maps.extension.style.layers.getLayer
import com.mapbox.maps.extension.style.layers.properties.generated.Visibility
import com.mapbox.maps.extension.style.sources.generated.vectorSource
import com.mapbox.maps.extension.style.style


class MapViewFragment: Fragment(R.layout.fragment_map_view),OnMapReadyCallback {
        private var mapView:MapView?=null
    private lateinit var fabLayerToggle:FloatingActionButton

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fabLayerToggle= FloatingActionButton(requireContext())
        mapView=getView()?.findViewById(R.id.map_view)
        mapView?.getMapboxMap()?.loadStyle(style(Style.LIGHT){
            +vectorSource(SOURCE_ID) {
                url(SOURCE_URL)
            }
            +circleLayer(LAYER_ID, SOURCE_ID) {
                sourceLayer(SOURCE_LAYER)
                visibility(Visibility.VISIBLE)
                circleRadius(8.0)
                circleColor(Color.argb(255, 55, 148, 179))
            }
        })
        fabLayerToggle.setOnClickListener {
            mapView?.getMapboxMap()?.getStyle{
                it.getLayer(LAYER_ID)?.let {layer->
                    if (layer.visibility==Visibility.VISIBLE){
                        layer.visibility(Visibility.NONE)
                    }else {
                        layer.visibility(Visibility.VISIBLE)
                    }

                }
            }
        }

    }

    override fun onMapReady(p0: GoogleMap) {
        TODO("Not yet implemented")
    }

    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView?.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
    }
    companion object {
        private const val SOURCE_ID = "museums_source"
        private const val SOURCE_URL = "mapbox://mapbox.2opop9hr"
        private const val SOURCE_LAYER = "museum-cusco"
        private const val LAYER_ID = "museums"
    }
}