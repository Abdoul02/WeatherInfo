package com.example.weatherinfo.data.sharedPref

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

object PreferencesManager : IPreferencesManager {

    private const val KEY_CALL_TIME = "lastCallTime"
    private lateinit var prefs: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    fun newInstance(context: Context): IPreferencesManager {
        prefs = PreferenceManager.getDefaultSharedPreferences(context)
        editor = prefs.edit()
        return this
    }

    override fun getLastCallTime(): Long? {
        return this.prefs.getLong(KEY_CALL_TIME, 0L)
    }

    override fun saveLastCallTime(time: Long) {
        this.editor.putLong(KEY_CALL_TIME, time)
        this.editor.commit()
    }
}