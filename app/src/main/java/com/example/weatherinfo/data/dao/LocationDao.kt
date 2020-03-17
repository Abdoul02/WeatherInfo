package com.example.weatherinfo.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.weatherinfo.model.LOCATION_TABLE
import com.example.weatherinfo.model.UserLocation

@Dao
interface LocationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocation(userLocation: UserLocation)

    @Delete
    suspend fun deleteLocation(userLocation: UserLocation)

    @Query("SELECT * FROM $LOCATION_TABLE ORDER BY id")
    fun getLocations(): LiveData<List<UserLocation>>
}