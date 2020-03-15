package com.example.weatherinfo.model

data class WeatherRequestData(
    val latitude: String,
    val longitude: String,
    val metric: String,
    val key: String
)