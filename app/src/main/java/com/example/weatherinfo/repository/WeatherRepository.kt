package com.example.weatherinfo.repository

import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weatherinfo.MyApplication
import com.example.weatherinfo.data.NetworkData
import com.example.weatherinfo.data.dao.LocationDao
import com.example.weatherinfo.data.dao.WeatherDataDao
import com.example.weatherinfo.model.UserLocation
import com.example.weatherinfo.model.WeatherData
import com.example.weatherinfo.model.WeatherRequestData
import com.example.weatherinfo.model.currentWeather.CurrentWeatherModel
import com.example.weatherinfo.model.forecast.ForecastModel
import com.example.weatherinfo.model.places.PlacesModel
import com.example.weatherinfo.model.places.PlacesResponse
import com.example.weatherinfo.other.ReusableData
import com.example.weatherinfo.other.ReusableData.WEATHER_API_KEY
import com.example.weatherinfo.other.ReusableData.WEATHER_UNIT
import com.google.android.gms.location.*
import dagger.Reusable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val networkData: NetworkData,
    private val application: MyApplication,
    private val weatherDataDao: WeatherDataDao,
    private val locationDao: LocationDao
) {
    private var mFusedLocationProviderClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(application)
    private val disposable: CompositeDisposable? = CompositeDisposable()
    private val placesDisposable: CompositeDisposable? = CompositeDisposable()

    init {
        if (ReusableData.isOnline(application)) {
            clearTables()
            loadData()
        }
    }

    private fun requestNewLocationData() {
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 10000
        mLocationRequest.fastestInterval = 5000
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(application)
        mFusedLocationProviderClient.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation: Location = locationResult.lastLocation
            val weatherRequestData = WeatherRequestData(
                latitude = mLastLocation.latitude.toString(),
                longitude = mLastLocation.longitude.toString(), metric = WEATHER_UNIT,
                key = WEATHER_API_KEY
            )
            networkLaunch(weatherRequestData)
        }
    }
    val weatherData: LiveData<WeatherData>
        get() = getDbWeatherData()

    private val errorMutable = MutableLiveData<Throwable>()
    val error: LiveData<Throwable>
        get() = errorMutable

    private fun insertWeatherData(weatherData: WeatherData) {
        GlobalScope.launch(Dispatchers.IO) {
            weatherDataDao.insert(weatherData)
        }
    }

    fun insertLocation(userLocation: UserLocation) {
        GlobalScope.launch {
            locationDao.insertLocation(userLocation)
        }
    }

    fun deleteLocation(userLocation: UserLocation) {
        GlobalScope.launch {
            locationDao.deleteLocation(userLocation)
        }
    }

    fun getLocations(): LiveData<List<UserLocation>> {
        return locationDao.getLocations()
    }

    private fun clearTables() {
        Log.d("insertWeatherData", "Clearing Data...")
        GlobalScope.launch(Dispatchers.IO) {
            weatherDataDao.clearTable()
        }
    }

    private fun getDbWeatherData(): LiveData<WeatherData> {
        return weatherDataDao.getWeatherData()
    }

    private fun loadData() {
        mFusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
            val location: Location? = task.result
            if (location == null) {
                requestNewLocationData()
            } else {
                val weatherRequestData = WeatherRequestData(
                    latitude = location.latitude.toString(),
                    longitude = location.longitude.toString(), metric = WEATHER_UNIT,
                    key = WEATHER_API_KEY
                )
                networkLaunch(weatherRequestData)
            }

        }
    }

    private fun networkLaunch(weatherRequestData: WeatherRequestData) {
        val current = networkData.getCurrentWeather(
            weatherRequestData.latitude,
            weatherRequestData.longitude,
            weatherRequestData.metric,
            weatherRequestData.key
        ).observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.newThread())

        val foreCastWeather = networkData.getForeCastWeather(
            weatherRequestData.latitude,
            weatherRequestData.longitude,
            weatherRequestData.metric,
            weatherRequestData.key
        ).observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.newThread())

        disposable?.add(
            Observable.zip(current,
                foreCastWeather,
                BiFunction<CurrentWeatherModel, ForecastModel, Pair<CurrentWeatherModel, ForecastModel>> { t1, t2 ->
                    Pair(
                        t1,
                        t2
                    )
                }).subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        val weatherData = WeatherData(it.first, it.second)
                        insertWeatherData(weatherData)
                    },
                    { errorMutable.postValue(it) })
        )
    }
}