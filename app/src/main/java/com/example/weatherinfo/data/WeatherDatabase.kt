package com.example.weatherinfo.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.weatherinfo.data.dao.CurrentWeatherDao
import com.example.weatherinfo.data.dao.ForecastDao
import com.example.weatherinfo.model.currentWeather.CurrentWeatherModel
import com.example.weatherinfo.model.forecast.ForecastModel
import other.Converters

@Database(entities = [CurrentWeatherModel::class, ForecastModel::class], version = 1)
@TypeConverters(Converters::class)
abstract class WeatherDatabase : RoomDatabase() {

    abstract fun currentWeatherDao(): CurrentWeatherDao
    abstract fun forecastDao(): ForecastDao
}
