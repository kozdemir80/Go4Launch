package com.example.go4launch.api

import com.example.go4launch.model.restaturantDetails.RestaurantDetails
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleMapsApi {
    @GET("nearbysearch/json")
    suspend fun nearbyPlaces(
        @Query("location") loc: String?,
        @Query("type") type: String?,
        @Query("key") key: String?,
        @Query("radius") radius:String?
    ):Response<RestaurantDetails>
    @GET("nearbysearch/json")
    suspend fun currentRestaurant(
        @Query("location") loc: String?,
        @Query("type") type: String?,
        @Query("key") key: String?,

    ):Response<RestaurantDetails>
}