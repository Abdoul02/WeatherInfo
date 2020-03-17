package com.example.weatherinfo.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.weatherinfo.data.dao.LocationDao
import com.example.weatherinfo.data.dao.WeatherDataDao
import com.example.weatherinfo.model.UserLocation
import com.example.weatherinfo.model.WeatherData
import com.example.weatherinfo.other.Converters

@Database(
    entities = [WeatherData::class, UserLocation::class],
    version = 3
)
@TypeConverters(Converters::class)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun weatherDataDao(): WeatherDataDao
    abstract fun locationDao(): LocationDao
}
