package com.example.go4launch.viewmodel
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
class MapsViewModel(private val repository:RestaurantRepository):ViewModel() {
    val myResponse: MutableLiveData<Response<RestaurantDetails>> = MutableLiveData()
    fun getRestaurantDetails(loc:String,type:String,key:String,radius:String){
        viewModelScope.launch {
           val response: Response<RestaurantDetails> = repository.getRestaurants(loc = loc,
               type = type, key = key,radius=radius)
            myResponse.value=response
        }
    }
    }