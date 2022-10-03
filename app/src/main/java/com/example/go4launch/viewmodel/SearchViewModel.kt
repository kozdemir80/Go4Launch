package com.example.go4launch.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.go4launch.api.SearchRepository
import com.example.go4launch.model.restaturantDetails.RestaurantDetails
import kotlinx.coroutines.launch
import retrofit2.Response

class SearchViewModel(private val repository: SearchRepository) : ViewModel() {
   private val _searchResponse: MutableLiveData<Response<RestaurantDetails>> = MutableLiveData()
    val searchResponse: LiveData<Response<RestaurantDetails>>
        get()=_searchResponse

    fun searchRestaurants(query: String, location: String, key: String) {
        viewModelScope.launch {
            val response: Response<RestaurantDetails> =
                repository.searchRestaurants(query = query, location = location, key = key)
            _searchResponse.value = response
        }
    }
}