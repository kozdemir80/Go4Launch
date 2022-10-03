package com.example.go4launch.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.go4launch.api.RestaurantRepository
import com.example.go4launch.model.restaturantDetails.RestaurantDetails
import kotlinx.coroutines.launch
import retrofit2.Response

/*
 * ViewModel class to display google maps responses
 */
class MapsViewModel(private val repository: RestaurantRepository) : ViewModel() {
  private val _restaurantDetailsResponse: MutableLiveData<Response<RestaurantDetails>> = MutableLiveData()
    val restaurantDetailsResponse:LiveData<Response<RestaurantDetails>>
    get() = _restaurantDetailsResponse
    fun getRestaurantDetails(loc: String, type: String, key: String, radius: String) {

        viewModelScope.launch {
            val response: Response<RestaurantDetails> = repository.getRestaurants(loc = loc,
                type = type, key = key, radius = radius)
            _restaurantDetailsResponse.value = response
        }
    }
}