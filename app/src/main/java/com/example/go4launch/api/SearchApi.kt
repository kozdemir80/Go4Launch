package com.example.go4launch.api

import com.example.go4launch.model.restaturantDetails.RestaurantDetails
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApi {
    @GET("textsearch/json")
    suspend fun getAllNearbyRest(
        @Query("query") query: String?,
        @Query("location") location: String?,
        @Query("key") key: String?,
    ): Response<RestaurantDetails>
}