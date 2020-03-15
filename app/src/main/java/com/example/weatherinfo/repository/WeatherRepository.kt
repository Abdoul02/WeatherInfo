package com.example.weatherinfo.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weatherinfo.data.NetworkData
import com.example.weatherinfo.model.WeatherData
import com.example.weatherinfo.model.WeatherRequestData
import com.example.weatherinfo.model.currentWeather.CurrentWeatherModel
import com.example.weatherinfo.model.forecast.ForecastModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class WeatherRepository @Inject constructor(private val networkData: NetworkData) {

    private val disposable: CompositeDisposable? = CompositeDisposable()

    private val weatherDataMutable = MutableLiveData<WeatherData>()
    fun loadData(weatherRequestData: WeatherRequestData): MutableLiveData<WeatherData> {
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
                        val weatherData = WeatherData(it.first, it.second)
                        weatherDataMutable.postValue(weatherData)
                    },
                    { this.showError(it) })
        )
        return weatherDataMutable
    }

    private fun getData(currentWeatherModel: CurrentWeatherModel, forecastModel: ForecastModel) {
    }

    private fun showError(error: Throwable) {
        Log.d("DrinkRepository", "Error: ${error.message}")
    }
}