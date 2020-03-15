package com.example.weatherinfo.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.weatherinfo.data.dao.WeatherDataDao
import com.example.weatherinfo.model.WeatherData
import other.Converters

@Database(
    entities = [WeatherData::class],
    version = 2
)
@TypeConverters(Converters::class)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun weatherDataDao(): WeatherDataDao
}
