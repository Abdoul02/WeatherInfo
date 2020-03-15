package com.example.weatherinfo.model.currentWeather

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

const val CURRENT_TABLE_NAME = "current_weather"

@Entity(tableName = CURRENT_TABLE_NAME)
data class CurrentWeatherModel(
    val base: String,
    @Embedded(prefix = "clouds_")
    val clouds: Clouds,
    val cod: Int,
    @Embedded(prefix = "coord_")
    val coord: Coord,
    val dt: Int,
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    @Embedded(prefix = "main_")
    val main: Main,
    val name: String,
    @Embedded(prefix = "sys_")
    val sys: Sys,
    val timezone: Int,
    val visibility: Int,
    val weather: List<Weather>,
    @Embedded(prefix = "wind_")
    val wind: Wind
)