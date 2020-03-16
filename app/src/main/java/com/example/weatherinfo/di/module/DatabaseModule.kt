package com.example.weatherinfo.di.module

import android.content.Context
import androidx.room.Room
import com.example.weatherinfo.data.WeatherDatabase
import com.example.weatherinfo.data.dao.LocationDao
import com.example.weatherinfo.data.dao.WeatherDataDao
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
    fun provideWeatherDataDao(db: WeatherDatabase): WeatherDataDao {
        return db.weatherDataDao()
    }

    @ApplicationScope
    @Provides
    fun provideLocationDao(db: WeatherDatabase): LocationDao {
        return db.locationDao()
    }
}