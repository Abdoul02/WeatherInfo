package com.example.weatherinfo.network

import com.example.weatherinfo.model.currentWeather.CurrentWeatherModel
import com.example.weatherinfo.model.forecast.ForecastModel
import com.example.weatherinfo.model.places.PlacesResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

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

    @GET
    fun getLocationInfo(
        @Url url: String,
        @Query("location") location: String,
        @Query("type") type: String,
        @Query("radius") radius: Int,
        @Query("key") key: String
    ): Observable<PlacesResponse>
}