package com.example.weatherinfo.data

import com.example.weatherinfo.model.currentWeather.CurrentWeatherModel
import com.example.weatherinfo.model.forecast.ForecastModel
import com.example.weatherinfo.model.places.PlacesResponse
import com.example.weatherinfo.network.ApiInterface
import io.reactivex.Observable
import javax.inject.Inject

class NetworkData @Inject constructor(private val apiInterface: ApiInterface) {

    fun getCurrentWeather(
        latitude: String,
        longitude: String,
        metric: String,
        key: String
    ): Observable<CurrentWeatherModel> {
        return apiInterface.getCurrentWeather(latitude, longitude, metric, key)
    }

    fun getForeCastWeather(
        latitude: String,
        longitude: String,
        metric: String,
        key: String
    ): Observable<ForecastModel> {
        return apiInterface.getForeCastWeather(latitude, longitude, metric, key)
    }

    fun getLocationInformation(
        url: String, location: String,
        type: String,
        radius: Int,
        key: String
    ): Observable<PlacesResponse> {
        return apiInterface.getLocationInfo(url, location, type, radius, key)
    }
}