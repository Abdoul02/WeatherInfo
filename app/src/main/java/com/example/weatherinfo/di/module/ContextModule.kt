package com.example.weatherinfo.di.module

import android.content.Context
import com.example.weatherinfo.di.qualifier.ApplicationContext
import com.example.weatherinfo.di.scope.ApplicationScope
import dagger.Module
import dagger.Provides

@Module
class ContextModule(val context: Context) {

    @Provides
    @ApplicationScope
    @ApplicationContext
    fun provideContext(): Context {
        return context
    }
}