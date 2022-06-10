package com.example.go4launch.model.restaturantDetails

import com.google.android.libraries.places.api.model.Period

data class OpeningHours(
    val open_now: Boolean,
    val periods: List<Period>,
    val weekday_text: List<String>
)