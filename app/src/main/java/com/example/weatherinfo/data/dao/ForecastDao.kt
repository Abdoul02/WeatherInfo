package com.example.weatherinfo.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherinfo.model.forecast.FORECAST_TABLE_NAME
import com.example.weatherinfo.model.forecast.ForecastModel

@Dao
interface ForecastDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(forecastModel: ForecastModel)

    @Query("DELETE FROM $FORECAST_TABLE_NAME")
    suspend fun clearTable()

    @Query("SELECT * FROM $FORECAST_TABLE_NAME")
    fun getForecastWeather(): ForecastModel
}