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
    private val weatherRepository: WeatherRepository,
    private val application: MyApplication
) :
    ViewModel() {

    lateinit var requestData: WeatherRequestData
    private var mFusedLocationProviderClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(application)

    init {
        mFusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
            val location: Location? = task.result
            if (location == null) {
                requestNewLocationData()
            } else {
                requestData = WeatherRequestData(
                    latitude = location.latitude.toString(),
                    longitude = location.longitude.toString(), metric = "metric",
                    key = "553c6868e55911a25016bd12138e0974"
                )
                Log.d("requestData","Latitude: ${requestData.latitude}, Longitude: ${requestData.longitude}")
            }

        }
    }

    private fun requestNewLocationData() {
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 10000
        mLocationRequest.fastestInterval = 5000
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(application)
        mFusedLocationProviderClient!!.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )

    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation: Location = locationResult.lastLocation
            requestData = WeatherRequestData(
                latitude = mLastLocation.latitude.toString(),
                longitude = mLastLocation.longitude.toString(), metric = "metric",
                key = "553c6868e55911a25016bd12138e0974"
            )
        }
    }

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    private val weatherRequest =
        WeatherRequestData(
            latitude = "-26.195246",
            longitude = "28.034088",
            metric = "metric",
            key = "553c6868e55911a25016bd12138e0974"
        )
    private val mutableWeatherData = weatherRepository.loadData(weatherRequest)
    val weatherData: LiveData<WeatherData> = mutableWeatherData


}