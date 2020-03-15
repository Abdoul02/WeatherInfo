package com.example.weatherinfo.di.module

import android.content.Context
import androidx.room.Room
import com.example.weatherinfo.data.WeatherDatabase
import com.example.weatherinfo.data.dao.CurrentWeatherDao
import com.example.weatherinfo.data.dao.ForecastDao
import com.example.weatherinfo.di.qualifier.ApplicationContext
import com.example.weatherinfo.di.scope.ApplicationScope
import dagger.Module
import dagger.Provides

@Module
class DatabaseModule(@ApplicationContext context: Context) {
    @ApplicationContext
    private val mContext: Context = context

    @ApplicationScope
    @Provides
    fun provideDataBase(): WeatherDatabase {
        return Room.databaseBuilder<WeatherDatabase>(
            mContext,
            WeatherDatabase::class.java,
            "weatherDatabase.db"
        ).fallbackToDestructiveMigration().build()
    }

    @ApplicationScope
    @Provides
    fun provideCurrentWeatherDao(db: WeatherDatabase): CurrentWeatherDao {
        return db.currentWeatherDao()
    }

    @ApplicationScope
    @Provides
    fun provideForecastDao(db: WeatherDatabase): ForecastDao {
        return db.forecastDao()
    }
}