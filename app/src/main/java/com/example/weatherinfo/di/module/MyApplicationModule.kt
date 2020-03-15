package com.example.weatherinfo.di.module

import com.example.weatherinfo.MyApplication
import com.example.weatherinfo.di.scope.ApplicationScope
import dagger.Module
import dagger.Provides

@Module
class MyApplicationModule(private val myApplication: MyApplication) {

    @Provides
    @ApplicationScope
    fun getMyApplication(): MyApplication {
        return myApplication
    }
}