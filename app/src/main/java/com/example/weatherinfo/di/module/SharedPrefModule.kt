package com.example.weatherinfo.di.module

import android.content.Context
import com.example.weatherinfo.data.sharedPref.IPreferencesManager
import com.example.weatherinfo.data.sharedPref.PreferencesManager
import com.example.weatherinfo.di.qualifier.ApplicationContext
import com.example.weatherinfo.di.scope.ApplicationScope
import dagger.Module
import dagger.Provides

@Module
class SharedPrefModule(@ApplicationContext context: Context) {

    @ApplicationContext
    private val mContext: Context = context

    @ApplicationScope
    @Provides
    fun provideSharedPreference(): IPreferencesManager {
        return PreferencesManager.newInstance(mContext)
    }
}