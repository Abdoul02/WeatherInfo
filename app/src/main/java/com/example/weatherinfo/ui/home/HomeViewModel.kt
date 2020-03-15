package com.example.weatherinfo.ui.home

import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherinfo.MyApplication
import com.example.weatherinfo.model.WeatherData
import com.example.weatherinfo.model.WeatherRequestData
import com.example.weatherinfo.repository.WeatherRepository
import com.google.android.gms.location.*
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    weatherRepository: WeatherRepository
) :
    ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    val weatherData: LiveData<WeatherData> = weatherRepository.weatherData
    val networkError: LiveData<Throwable> = weatherRepository.error

}