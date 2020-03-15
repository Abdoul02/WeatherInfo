package com.example.weatherinfo.model.currentWeather

data class Weather(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)