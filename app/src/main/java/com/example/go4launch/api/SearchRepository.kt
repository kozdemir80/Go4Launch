package com.example.go4launch.api

import com.example.go4launch.model.restaturantDetails.RestaurantDetails
import retrofit2.Response

class SearchRepository {
    suspend fun searchRestaurants(query:String,location:String,key:String): Response<RestaurantDetails> {
        return SearchInstance.api.getAllNearbyRest(query,location, key)
    }
}