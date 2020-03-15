package com.example.weatherinfo.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherinfo.model.currentWeather.CurrentWeatherModel
import com.example.weatherinfo.model.currentWeather.CURRENT_TABLE_NAME

@Dao
interface CurrentWeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(currentWeatherModel: CurrentWeatherModel)

    @Query("DELETE FROM $CURRENT_TABLE_NAME")
    suspend fun clearTable()

    @Query("SELECT * FROM $CURRENT_TABLE_NAME")
    fun getCurrentWeather(): CurrentWeatherModel

}