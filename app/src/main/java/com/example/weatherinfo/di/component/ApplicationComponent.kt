package com.example.weatherinfo.di.component

import android.content.Context
import com.example.weatherinfo.data.sharedPref.IPreferencesManager
import com.example.weatherinfo.data.sharedPref.PreferencesManager
import com.example.weatherinfo.di.module.*
import com.example.weatherinfo.di.qualifier.ApplicationContext
import com.example.weatherinfo.di.scope.ApplicationScope
import com.example.weatherinfo.network.ApiInterface
import com.example.weatherinfo.ui.map.MapsActivity
import com.example.weatherinfo.ui.favorite.FavoriteFragment
import com.example.weatherinfo.ui.home.HomeFragment
import com.example.weatherinfo.ui.places.PlacesFragment
import com.example.weatherinfo.workManager.SampleWorkerFactory
import dagger.Component

@ApplicationScope
@Component(modules = [ContextModule::class, RetrofitModule::class, MyApplicationModule::class, DatabaseModule::class, WorkerModule::class, SharedPrefModule::class])
interface ApplicationComponent {

    fun getApiInterface(): ApiInterface

    @ApplicationContext
    fun getContext(): Context?

    fun injectApplication(homeFragment: HomeFragment)
    fun injectFavoriteFragment(favoriteFragment: FavoriteFragment)
    fun injectPlaceFragment(placesFragment: PlacesFragment)
    fun injectMapActivity(mapsActivity: MapsActivity)
    fun factory(): SampleWorkerFactory
}