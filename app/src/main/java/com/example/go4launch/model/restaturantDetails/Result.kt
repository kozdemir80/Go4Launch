package com.example.go4launch.model.restaturantDetails
/*
 * Data class for google maps response items
 */
data class Result(
    val business_status: String,
    val geometry: Geometry,
    val icon: String,
    val icon_background_color: String,
    val icon_mask_base_uri: String,
    var name: String,
    val opening_hours: OpeningHours,
    val photos: List<Photo>,
    val place_id: String,
    val plus_code: PlusCode,
    val price_level: Int,
    val rating: Double,
    val reference: String,
    val scope: String,
    var types: List<String>,
    val user_ratings_total: Int,
    var vicinity: String,
    var international_phone_number: String,
    val formatted_phone_number: String,
    var website:String
)