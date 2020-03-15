package com.example.weatherinfo.model.forecast

import androidx.room.Entity
import androidx.room.PrimaryKey

data class ForecastModel(
    //val city: City,  //No use at the moment
    val cnt: Int,
    val cod: String,
    val list: List<ForecastDetail>,
    val message: Int
)