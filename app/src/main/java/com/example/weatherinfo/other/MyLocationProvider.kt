package com.example.weatherinfo.other

import android.content.Context
import com.example.weatherinfo.di.qualifier.ApplicationContext
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import javax.inject.Inject

class MyLocationProvider @Inject constructor(@ApplicationContext context: Context) {
    @ApplicationContext
    private val mContext: Context = context

    fun getFusedLocation(): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(mContext)
    }

    fun getLocationRequest(): LocationRequest {
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 10000
        mLocationRequest.fastestInterval = 5000
        return mLocationRequest
    }

    /*
    //Get Address from Location
    Geocoder geocoder = new Geocoder(this, Locale.getDefault());
 List<Address> addresses = geocoder.getFromLocation(MyLat, MyLong, 1);
 String cityName = addresses.get(0).getAddressLine(0);
 String stateName = addresses.get(0).getAddressLine(1);
 String countryName = addresses.get(0).getAddressLine(2);*/
}