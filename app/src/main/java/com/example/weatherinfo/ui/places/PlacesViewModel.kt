package com.example.weatherinfo.ui.places

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.weatherinfo.model.places.PlacesResponse
import com.example.weatherinfo.repository.WeatherRepository
import javax.inject.Inject

class PlacesViewModel @Inject constructor(private val weatherRepository: WeatherRepository) :
    ViewModel() {

    fun getPlaceInfo(
        url: String,
        location: String,
        radius: Int,
        type: String,
        key: String
    ): LiveData<PlacesResponse> {
        return weatherRepository.getLocationInfo(url, location, radius, type, key)
    }
}
