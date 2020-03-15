package com.example.weatherinfo.model.forecast

import androidx.room.Entity
import androidx.room.PrimaryKey

const val FORECAST_TABLE_NAME = "forecast_weather"
const val FORECAST_WEATHER_ID = 1

@Entity(tableName = FORECAST_TABLE_NAME)
data class ForecastModel(
    //val city: City,  //No use at the moment
    val cnt: Int,
    val cod: String,
    val list: List<ForecastDetail>,
    val message: Int
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = FORECAST_WEATHER_ID
}