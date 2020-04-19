package com.example.weatherinfo.workManager

import android.content.Context
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.work.ListenableWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.weatherinfo.other.MyLocationProvider
import com.example.weatherinfo.other.Notification
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import javax.inject.Inject
import javax.inject.Provider

class WeatherInfoWorker(
    private val appContext: Context,
    private val params: WorkerParameters,
    private val myLocationProvider: MyLocationProvider
) : Worker(appContext, params) {

    private val fusedLocation = myLocationProvider.getFusedLocation()

    override fun doWork(): Result {
        Log.d("WeatherInfoWorker", "Testing start")
        getCurrentLocationWeather()
        return Result.success()
    }

    class Factory @Inject constructor(private val myLocationProvider: Provider<MyLocationProvider>) :
        ChildWorkerFactory {
        override fun create(appContext: Context, params: WorkerParameters): ListenableWorker {
            return WeatherInfoWorker(appContext, params, myLocationProvider.get())
        }

    }

    private fun getCurrentLocationWeather() {
        fusedLocation.lastLocation.addOnCompleteListener { task ->
            val location: Location? = task.result
            if (location == null) {
                Log.d("WeatherInfoWorker", "location is null ")
                requestNewLocation()
            } else {
                Log.d(
                    "WeatherInfoWorker",
                    "mLocationCallback Lat: ${location.latitude}, Long:${location.longitude}"
                )
                Notification.showNotification(
                    appContext,
                    "Current location Lat: ${location.latitude}, Long:${location.longitude}"
                )
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
            val location: Location = locationResult.lastLocation
            Log.d(
                "WeatherInfoWorker",
                "mLocationCallback Lat: ${location.latitude}, Long:${location.longitude}"
            )
            Notification.showNotification(
                appContext,
                "Current location Lat: ${location.latitude}, Long:${location.longitude}"
            )
        }
    }
}