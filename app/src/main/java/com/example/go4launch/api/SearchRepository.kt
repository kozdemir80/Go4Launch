package com.example.go4launch.api

import com.example.go4launch.model.restaturantDetails.RestaurantDetails
import retrofit2.Response

class SearchRepository {
    suspend fun searchRestaurants(loc:String,type:String,key:String,radius:String,keyword:String): Response<RestaurantDetails> {
        return SearchInstance.api.getAllNearbyRest(loc, type, key, radius,keyword)
    }
}