package com.example.weatherinfo.model

import com.example.weatherinfo.model.currentWeather.CurrentWeatherModel
import com.example.weatherinfo.model.forecast.ForecastModel

data class WeatherData(
    val currentWeatherModel: CurrentWeatherModel,
    val forecastModel: ForecastModel
)