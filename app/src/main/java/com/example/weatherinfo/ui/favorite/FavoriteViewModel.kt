package com.example.weatherinfo.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherinfo.model.UserLocation
import com.example.weatherinfo.repository.WeatherRepository
import javax.inject.Inject

class FavoriteViewModel @Inject constructor(private val weatherRepository: WeatherRepository) :
    ViewModel() {

    val locations: LiveData<List<UserLocation>> = weatherRepository.getLocations()

    fun deleteLocation(userLocation: UserLocation) {
        weatherRepository.deleteLocation(userLocation)
    }
}