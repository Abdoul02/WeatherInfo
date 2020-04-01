package com.example.weatherinfo.ui.map

import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.weatherinfo.MyApplication
import com.example.weatherinfo.R
import com.example.weatherinfo.model.UserLocation
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import javax.inject.Inject


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var mapsViewModel: MapsViewModel
    private lateinit var mMap: GoogleMap
    var locationList = arrayListOf<UserLocation>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        (this.application as MyApplication).getApplicationComponent()?.injectMapActivity(this)
        mapsViewModel = ViewModelProvider(this, viewModelFactory).get(MapsViewModel::class.java)

        val w: Window = window
        w.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        actionBar?.hide()

        val bundle = intent.extras
        locationList =
            bundle?.getParcelableArrayList<UserLocation>("locationList") as ArrayList<UserLocation>
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.activityMap) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val zoom = CameraUpdateFactory.zoomTo(11f)
        for (locations in locationList) {
            val point = LatLng(locations.latitude, locations.longitude)
            addMarker(point, locations.name)
        }
        mapsViewModel.currentLocation.observe(this, Observer {
            it?.let {
                val currentPoint = LatLng(it.latitude, it.longitude)
                addCurrentMarker(currentPoint, "Current location")
                mMap.moveCamera(CameraUpdateFactory.newLatLng(currentPoint))
                mMap.animateCamera(zoom)
            }
        })
    }

    private fun addMarker(point: LatLng, name: String) {
        mMap.addMarker(MarkerOptions().position(point).title(name))
    }

    private fun addCurrentMarker(point: LatLng, name: String) {
        mMap.addMarker(MarkerOptions().position(point).title(name))
            .setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
    }
}
