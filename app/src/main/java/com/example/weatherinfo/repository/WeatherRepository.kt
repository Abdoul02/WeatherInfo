package com.example.weatherinfo.repository

import android.annotation.SuppressLint
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weatherinfo.MyApplication
import com.example.weatherinfo.data.NetworkData
import com.example.weatherinfo.data.dao.CurrentWeatherDao
import com.example.weatherinfo.data.dao.ForecastDao
import com.example.weatherinfo.model.WeatherData
import com.example.weatherinfo.model.WeatherRequestData
import com.example.weatherinfo.model.currentWeather.CurrentWeatherModel
import com.example.weatherinfo.model.forecast.ForecastModel
import com.google.android.gms.location.*
import io.reactivex.Flowable
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
    private val currentWeatherDao: CurrentWeatherDao,
    private val forecastDao: ForecastDao
) {
    private var mFusedLocationProviderClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(application)
    private val disposable: CompositeDisposable? = CompositeDisposable()

    init {
        loadData()
        // getDataFromDb()
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
            val weatherRequestData = WeatherRequestData(
                latitude = mLastLocation.latitude.toString(),
                longitude = mLastLocation.longitude.toString(), metric = "metric",
                key = "553c6868e55911a25016bd12138e0974"
            )
            networkLaunch(weatherRequestData)
        }
    }
    private val weatherDataMutable = MutableLiveData<WeatherData>()
    val weatherData: LiveData<WeatherData>
        get() = getDataFromDb()

    private val errorMutable = MutableLiveData<Throwable>()
    val error: LiveData<Throwable>
        get() = errorMutable

    private fun insertWeatherData(
        currentWeatherModel: CurrentWeatherModel,
        forecastModel: ForecastModel
    ) {
        Log.d("insertWeatherData", "Inserting Data...")
        GlobalScope.launch(Dispatchers.IO) {
            currentWeatherDao.insert(currentWeatherModel)
            forecastDao.insert(forecastModel)
        }
    }

    @SuppressLint("CheckResult")
    private fun getDataFromDb(): LiveData<WeatherData> {
        val currentWeather = Observable.fromCallable {
            getCurrentWeather()
        }
        val forecastWeather = Observable.fromCallable { getForecastWeather() }

        Observable.zip(
            currentWeather,
            forecastWeather,
            BiFunction<CurrentWeatherModel, ForecastModel, Pair<CurrentWeatherModel, ForecastModel>> { t1, t2 ->
                Pair(t1, t2)
            }).subscribeOn(Schedulers.io())
            .subscribe({
                val weatherData = WeatherData(it.first, it.second)
                weatherDataMutable.postValue(weatherData)
            }, {})
        return weatherDataMutable
    }

    private fun clearTables() {
        GlobalScope.launch(Dispatchers.IO) {
            currentWeatherDao.clearTable()
            forecastDao.clearTable()
        }
    }

    private fun getCurrentWeather(): CurrentWeatherModel {
        return currentWeatherDao.getCurrentWeather()
    }

/*    private fun insertForecast(forecastModel: ForecastModel) {
        GlobalScope.launch(Dispatchers.IO) {
            forecastDao.insert(forecastModel)
        }
    }*/

    private fun getForecastWeather(): ForecastModel {
        return forecastDao.getForecastWeather()
    }

    private fun loadData() {

        mFusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
            val location: Location? = task.result
            if (location == null) {
                requestNewLocationData()
            } else {
                val weatherRequestData = WeatherRequestData(
                    latitude = location.latitude.toString(),
                    longitude = location.longitude.toString(), metric = "metric",
                    key = "553c6868e55911a25016bd12138e0974"
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
                        //this.getData(it.first, it.second)
                        clearTables()
                        insertWeatherData(it.first, it.second)
                    },
                    { errorMutable.postValue(it) })
        )
    }
}