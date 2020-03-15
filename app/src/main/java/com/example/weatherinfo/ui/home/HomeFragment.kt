package com.example.weatherinfo.ui.home

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SimpleAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherinfo.MyApplication
import com.example.weatherinfo.R
import com.example.weatherinfo.model.WeatherData
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.fragment_home.*
import other.ForecastAdapter
import javax.inject.Inject

class HomeFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var forecastAdapter: ForecastAdapter
    lateinit var forecastRecycleView: RecyclerView

    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient
    private val LOCATION_PERMISSION = 101

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.let {
            (it.application as MyApplication).get(it).getApplicationComponent()
                ?.injectApplication(this) }

        val root = inflater.inflate(R.layout.fragment_home, container, false)
        forecastRecycleView = root.findViewById(R.id.forecastRecycleView)
        homeViewModel = ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)
        this.context?.let { mContext ->
            forecastAdapter = ForecastAdapter(mContext)
            forecastRecycleView.layoutManager = LinearLayoutManager(mContext)
            forecastRecycleView.setHasFixedSize(true)
            forecastRecycleView.adapter = forecastAdapter
            mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mContext)
            getLastLocation()
        }

        homeViewModel.weatherData.observe(viewLifecycleOwner, Observer {
            updateViews(it)
        })
        return root
    }

    private fun checkPermission(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this.context!!,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this.context!!, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermission() {
        this.activity?.let {
            ActivityCompat.requestPermissions(
                it,
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                LOCATION_PERMISSION
            )
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun getLastLocation() {
        if (checkPermission()) {
            if (isLocationEnabled()) {

                this.activity?.let {
                    mFusedLocationProviderClient.lastLocation.addOnCompleteListener(it) { task ->
                        val location: Location? = task.result
                        if (location == null) {
                            requestNewLocationData()
                        } else {
                            Toast.makeText(
                                this.context,
                                "Latitude: ${location.latitude}, Longitude: ${location.longitude}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            } else {
                Toast.makeText(this.context, "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermission()
        }
    }

    private fun requestNewLocationData() {
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 10000
        mLocationRequest.fastestInterval = 5000

        this.activity?.let {
            mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(it)
            mFusedLocationProviderClient!!.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
            )
        }
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation: Location = locationResult.lastLocation
            showMessage("Latitude: ${mLastLocation.latitude}, Longitude: ${mLastLocation.longitude}")
        }
    }

    fun showMessage(msg: String) {
        Toast.makeText(this.context, msg, Toast.LENGTH_SHORT).show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == LOCATION_PERMISSION) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLastLocation()
            }
        }
    }

    private fun updateViews(weatherData: WeatherData) {
        val currentWeather = weatherData.currentWeatherModel
        val forecast = weatherData.forecastModel
        forecastAdapter.setData(forecast.list)

        if (!currentWeather.weather.isNullOrEmpty()) {
            clProgress.visibility = View.GONE
            clCurrentWeather.visibility = View.VISIBLE
            clForeCastWeather.visibility = View.VISIBLE
            tvTemperature.text =
                getString(
                    R.string.current_temp,
                    currentWeather.main.temp.toInt().toString(),
                    "\u2103"
                )
            tvTemperatureDetail.text = currentWeather.weather[0].main
            tvMinTemp.text = getString(
                R.string.current_min_temp,
                currentWeather.main.temp_min.toInt().toString(),
                "\u2103"
            )
            tvCurrentTemp.text = getString(
                R.string.current_temp,
                currentWeather.main.temp.toInt().toString(),
                "\u2103"
            )
            tvMaxTemp.text = getString(
                R.string.current_max_temp,
                currentWeather.main.temp_max.toInt().toString(),
                "\u2103"
            )
        }
    }
}
