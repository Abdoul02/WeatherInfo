package com.example.weatherinfo.di.component

import android.content.Context
import com.example.weatherinfo.di.module.ContextModule
import com.example.weatherinfo.di.module.DatabaseModule
import com.example.weatherinfo.di.module.MyApplicationModule
import com.example.weatherinfo.di.module.RetrofitModule
import com.example.weatherinfo.di.qualifier.ApplicationContext
import com.example.weatherinfo.di.scope.ApplicationScope
import com.example.weatherinfo.network.ApiInterface
import com.example.weatherinfo.ui.favorite.FavoriteFragment
import com.example.weatherinfo.ui.home.HomeFragment
import com.example.weatherinfo.ui.places.PlacesFragment
import dagger.Component

@ApplicationScope
@Component(modules = [ContextModule::class, RetrofitModule::class, MyApplicationModule::class, DatabaseModule::class])
interface ApplicationComponent {

    fun getApiInterface(): ApiInterface

    @ApplicationContext
    fun getContext(): Context?

    fun injectApplication(homeFragment: HomeFragment)
    fun injectFavoriteFragment(favoriteFragment: FavoriteFragment)
    fun injectPlaceFragment(placesFragment: PlacesFragment)
}