package com.example.weatherinfo

import android.app.Activity
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.work.*
import com.example.weatherinfo.di.component.ApplicationComponent
import com.example.weatherinfo.di.component.DaggerApplicationComponent
import com.example.weatherinfo.di.module.ContextModule
import com.example.weatherinfo.di.module.DatabaseModule
import com.example.weatherinfo.di.module.MyApplicationModule
import com.example.weatherinfo.di.module.SharedPrefModule
import com.example.weatherinfo.workManager.SampleWorkerFactory
import com.example.weatherinfo.workManager.WeatherInfoWorker
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MyApplication : Application() {

    private var component: ApplicationComponent? = null

    override fun onCreate() {
        super.onCreate()

        component = DaggerApplicationComponent
            .builder()
            .contextModule(ContextModule(this))
            .databaseModule(DatabaseModule(this))
            .myApplicationModule(MyApplicationModule(this))
            .sharedPrefModule(SharedPrefModule(this))
            .build()
        component?.let {
            val factory = it.factory()

            WorkManager.initialize(
                this,
                Configuration.Builder().setWorkerFactory(factory).build()
            )
        }
        createNotificationChanel()
        val periodicWorkRequest = periodicWork()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            PERIODIC_WORK,
            ExistingPeriodicWorkPolicy.KEEP,
            periodicWorkRequest
        )
    }

    fun get(activity: Activity): MyApplication {
        return activity.application as MyApplication
    }

    fun getApplicationComponent(): ApplicationComponent? {
        return component
    }

    private fun createNotificationChanel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel(CHANEL_ID, CHANEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.description = "Weather alert notification"
            notificationChannel.enableLights(true)
            val manager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(notificationChannel)
        }
    }

    private fun periodicWork(): PeriodicWorkRequest {
        val workerConstraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .build()
        return PeriodicWorkRequest.Builder(
            WeatherInfoWorker::class.java, 2, TimeUnit.HOURS
        )
            .setConstraints(workerConstraints)
            .setInitialDelay(5, TimeUnit.MINUTES)
            .build()
    }

    companion object {
        const val CHANEL_ID = "weather_alert"
        const val CHANEL_NAME = "Weather alert"
        const val PERIODIC_WORK = "weatherInfo"
        const val NOTIFICATION_REQUEST_CODE = 100
    }
}