package com.example.go4launch.model.restaturantDetails

import com.google.android.libraries.places.api.model.OpeningHours
import com.google.android.libraries.places.api.model.PhotoMetadata
import com.google.android.libraries.places.api.model.Place

data class RestaurantDetails(var name :String,
                             var address:String,
                             var phone:String,
                             var photo:List<PhotoMetadata>,
                             var type:List<Place.Type>,
                             var review:Float,
                             var openingHours:OpeningHours
)
