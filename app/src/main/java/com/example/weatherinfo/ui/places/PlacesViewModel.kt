package com.example.weatherinfo.ui.places

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.weatherinfo.model.places.PlacesModel
import com.example.weatherinfo.repository.PlacesRepository
import javax.inject.Inject

class PlacesViewModel @Inject constructor(private val placesRepository: PlacesRepository) :
    ViewModel() {

    fun getPlaceInfo(
        url: String,
        location: String,
        radius: Int,
        type: String,
        key: String
    ): LiveData<PlacesModel> {
        return placesRepository.getLocationInfo(url, location, radius, type, key)
    }
}
