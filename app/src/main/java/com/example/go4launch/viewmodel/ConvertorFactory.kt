package com.example.go4launch.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.go4launch.api.RestaurantRepository

@Suppress("UNCHECKED_CAST")
class ConvertorFactory(private val repository: RestaurantRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MapsViewModel(repository) as T
    }
}