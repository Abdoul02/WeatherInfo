package com.example.weatherinfo.network

import com.example.weatherinfo.model.currentWeather.CurrentWeatherModel
import com.example.weatherinfo.model.forecast.ForecastModel
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
// base url = http://api.openweathermap.org/

    @GET("data/2.5/weather")
    fun getCurrentWeather(
        @Query("lat") latitude: String,
        @Query("lon") longitude: String,
        @Query("units") metric: String,
        @Query("appid") key: String
    ): Observable<CurrentWeatherModel>

    @GET("/data/2.5/forecast")
    fun getForeCastWeather(
        @Query("lat") latitude: String,
        @Query("lon") longitude: String,
        @Query("units") metric: String,
        @Query("appid") key: String
    ): Observable<ForecastModel>
}