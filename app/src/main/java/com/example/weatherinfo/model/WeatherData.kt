package com.example.weatherinfo.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.weatherinfo.model.currentWeather.CurrentWeatherModel
import com.example.weatherinfo.model.forecast.ForecastModel

const val WEATHER_DATA_TABLE = "weather_data"
const val WEATHER_DATA_ID = 1

@Entity(tableName = WEATHER_DATA_TABLE)
data class WeatherData(
    val currentWeatherModel: CurrentWeatherModel,
    val forecastModel: ForecastModel
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = WEATHER_DATA_ID
}