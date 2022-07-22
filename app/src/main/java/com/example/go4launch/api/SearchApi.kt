package com.example.go4launch.api

import com.example.go4launch.model.restaturantDetails.RestaurantDetails
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApi {
    @GET("place/autocomplete/json")
    suspend fun currentRestaurant(
        @Query("location") loc: String?,
        @Query("type") type: String?,
        @Query("key") key: String?,
        @Query("radius") radius:String?,
        @Query("keyword") keyword:String?

    ): Response<RestaurantDetails>
}