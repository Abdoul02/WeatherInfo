package com.example.weatherinfo.di.module

import com.example.weatherinfo.di.scope.WorkerKey
import com.example.weatherinfo.workManager.ChildWorkerFactory
import com.example.weatherinfo.workManager.WeatherInfoWorker
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface WorkerModule {

    @Binds
    @IntoMap
    @WorkerKey(WeatherInfoWorker::class)
    fun bindWeatherInfoWorker(factory: WeatherInfoWorker.Factory): ChildWorkerFactory
}