package com.example.go4launch

import com.example.go4launch.model.restaturantDetails.RestaurantDetails
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock

@RunWith(JUnit4::class)
class ResponseTest {
    @Mock
    private lateinit var restaurantDetails: RestaurantDetails

    @Test
    fun listResponseTest() {
        restaurantDetails = RestaurantDetails(html_attributions = listOf(),
            next_page_token = String(),
            results = listOf(),
            status = String())
        val name= "Turkish Restaurant"
        val types= listOf("cafe,restaurant,japanese,turkish,,indian,french")
        val phoneNumber= "009055259295"
        val website="www.kebabHouse.com"
        val lng=13.5000
        val lat=15.5000
        val address="Ankara,Turkey"
        restaurantDetails.results.apply {
            restaurantDetails.results.firstOrNull()?.name
            restaurantDetails.results.firstOrNull()?.types
            restaurantDetails.results.firstOrNull()?.international_phone_number
            restaurantDetails.results.firstOrNull()?.website
            restaurantDetails.results.firstOrNull()?.geometry?.location?.lng
            restaurantDetails.results.firstOrNull()?.geometry?.location?.lat
            restaurantDetails.results.firstOrNull()?.vicinity
            assertEquals(name,"Turkish Restaurant")
            assertEquals(types,listOf("cafe,restaurant,japanese,turkish,,indian,french"))
            assertEquals(phoneNumber,"009055259295")
            assertEquals(website,"www.kebabHouse.com")
            assertEquals(lat,15.5000,13.5)
            assertEquals(lng,13.5000,15.5)
            assertEquals(address,"Ankara,Turkey")
        }

    }
}