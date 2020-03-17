package com.example.weatherinfo.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

const val LOCATION_TABLE = "location_table"

@Parcelize
@Entity(tableName = LOCATION_TABLE)
data class UserLocation(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val latitude: Double,
    val longitude: Double,
    val name: String
) : Parcelable