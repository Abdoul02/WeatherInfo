package com.example.weatherinfo.data.sharedPref

interface IPreferencesManager {

    fun getLastCallTime(): Long?
    fun saveLastCallTime(time: Long)
}