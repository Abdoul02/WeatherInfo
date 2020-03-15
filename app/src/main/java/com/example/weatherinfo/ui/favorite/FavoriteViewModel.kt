package com.example.weatherinfo.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherinfo.repository.WeatherRepository
import javax.inject.Inject

class FavoriteViewModel @Inject constructor(private val weatherRepository: WeatherRepository) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is slideshow Fragment"
    }
    val text: LiveData<String> = _text
}