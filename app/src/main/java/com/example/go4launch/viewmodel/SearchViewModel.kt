package com.example.go4launch.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.go4launch.api.SearchRepository
import com.example.go4launch.model.restaturantDetails.RestaurantDetails
import kotlinx.coroutines.launch
import retrofit2.Response

class SearchViewModel(private val repository: SearchRepository):ViewModel(){
    val myResponse: MutableLiveData<Response<RestaurantDetails>> = MutableLiveData()
    fun searchRestaurants(loc:String,type:String,key:String,radius:String,keyword:String){
        viewModelScope.launch {
            val response: Response<RestaurantDetails> = repository.searchRestaurants(loc = loc,
                type = type, key = key,radius=radius, keyword = keyword)
            myResponse.value=response
        }
    }
}