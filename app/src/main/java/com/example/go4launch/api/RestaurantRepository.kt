package com.example.go4launch.api
import com.example.go4launch.model.restaturantDetails.RestaurantDetails
import retrofit2.Response
class RestaurantRepository {
    suspend fun getRestaurants(loc:String,type:String,key:String,radius:String): Response<RestaurantDetails> {
        return RestaurantInstance.api.nearbyPlaces(loc, type, key, radius)
    }
}