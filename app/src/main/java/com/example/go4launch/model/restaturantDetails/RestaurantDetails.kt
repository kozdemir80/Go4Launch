package com.example.go4launch.model.restaturantDetails

data class RestaurantDetails(
    val html_attributions: List<Any>,
    val next_page_token: String,
    val results: List<Result>,
    val status: String
)