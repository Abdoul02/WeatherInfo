package com.example.weatherinfo.other

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import java.text.SimpleDateFormat
import java.util.*

object ReusableData {

    const val WEATHER_API_KEY = "553c6868e55911a25016bd12138e0974"
    const val WEATHER_UNIT = "metric"
    const val PLACES_API_KEY = "AIzaSyCVkcik_E3WcjxYBELZfXa1zPzu2Nw-dus"
    const val PLACE_TYPE = "restaurant"
    const val RADIUS = 1500
    const val PLACE_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json"
    private const val ONE_HOUR = 60 * 60 * 1000L

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

    fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.getDefault())
        return format.format(date)
    }

    /*
    *     private fun getDate(milliSeconds: Long): String {
        val formatter = SimpleDateFormat("dd-MM-yyyy hh:mm:ss", Locale.getDefault())
        val calender = Calendar.getInstance()
        calender.timeInMillis = milliSeconds
        return formatter.format(calender.time)
    }*/

    fun currentTimeToLong(): Long {
        return System.currentTimeMillis()
    }

    fun convertDateToLong(date: String): Long? {
        val df = SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.getDefault())
        return df.parse(date)?.time
    }

    fun callRequired(lastCallTime: Long): Boolean {
        return (currentTimeToLong() - lastCallTime) > ONE_HOUR
    }
}