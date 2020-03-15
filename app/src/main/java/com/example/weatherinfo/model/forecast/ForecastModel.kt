package com.example.weatherinfo.model.forecast

data class ForecastModel(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<ForecastDetail>,
    val message: Int
)