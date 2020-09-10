package com.example.weatherinfo.other

import android.R
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Toast
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability


object ReusableData {

    const val WEATHER_API_KEY = "553c6868e55911a25016bd12138e0974"
    const val WEATHER_UNIT = "metric"
    const val PLACES_API_KEY = "AIzaSyCVkcik_E3WcjxYBELZfXa1zPzu2Nw-dus"
    const val PLACE_TYPE = "restaurant"
    const val RADIUS = 1500
    const val PLACE_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json"

    fun showAlertDialog(
        context: Context,
        title: String,
        message: String?,
        onClickListener: DialogInterface.OnClickListener
    ) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("OK", onClickListener)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return networkCapabilities != null && networkCapabilities.hasCapability(
            NetworkCapabilities.NET_CAPABILITY_INTERNET
        ) &&
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }

     fun checkPlayServices(context: Context): Boolean {
        val gApi = GoogleApiAvailability.getInstance()
        val resultCode = gApi.isGooglePlayServicesAvailable(context)
        if (resultCode != ConnectionResult.SUCCESS) {
            return false
        }
        return true
    }
}