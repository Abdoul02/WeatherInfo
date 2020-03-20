package com.example.weatherinfo.ui.map

import android.location.Location
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherinfo.other.MyLocationProvider
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import javax.inject.Inject

class MapsViewModel @Inject constructor(private val myLocationProvider: MyLocationProvider) : ViewModel() {

    private val fusedLocation = myLocationProvider.getFusedLocation()
    private val mutableCurrentLocation = MutableLiveData<Location>()
    val currentLocation: LiveData<Location>
        get() = mutableCurrentLocation

    init {
        getCurrentLocation()
    }

    private fun getCurrentLocation() {
        fusedLocation.lastLocation.addOnCompleteListener { task ->
            val location: Location? = task.result
            if (location == null) {
                requestNewLocation()
            } else {
                mutableCurrentLocation.postValue(location)
            }
        }
    }

    private fun requestNewLocation() {
        val mLocationRequest = myLocationProvider.getLocationRequest()
        fusedLocation.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation: Location = locationResult.lastLocation
            mutableCurrentLocation.postValue(mLastLocation)
        }
    }
}