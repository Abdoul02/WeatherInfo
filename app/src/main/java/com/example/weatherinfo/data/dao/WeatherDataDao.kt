package com.example.weatherinfo.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherinfo.model.WEATHER_DATA_TABLE
import com.example.weatherinfo.model.WeatherData

@Dao
interface WeatherDataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(weatherData: WeatherData)

    @Query("DELETE FROM $WEATHER_DATA_TABLE")
    suspend fun clearTable()

    @Query("SELECT * FROM $WEATHER_DATA_TABLE")
    fun getWeatherData(): LiveData<WeatherData>
}