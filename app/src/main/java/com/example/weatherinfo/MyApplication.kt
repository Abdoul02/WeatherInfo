package com.example.weatherinfo

import android.app.Activity
import android.app.Application
import com.example.weatherinfo.di.component.ApplicationComponent
import com.example.weatherinfo.di.component.DaggerApplicationComponent
import com.example.weatherinfo.di.module.ContextModule
import com.example.weatherinfo.di.module.DatabaseModule
import com.example.weatherinfo.di.module.MyApplicationModule

class MyApplication : Application() {

    var component: ApplicationComponent? = null
    override fun onCreate() {
        super.onCreate()
        component = DaggerApplicationComponent
            .builder()
            .contextModule(ContextModule(this))
            .databaseModule(DatabaseModule(this))
            .myApplicationModule(MyApplicationModule(this))
            .build()
    }

    fun get(activity: Activity): MyApplication {
        return activity.application as MyApplication
    }

    fun getApplicationComponent(): ApplicationComponent? {
        return component
    }
}